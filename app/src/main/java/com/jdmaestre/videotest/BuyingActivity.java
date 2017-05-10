package com.jdmaestre.videotest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.jdmaestre.videotest.util.IabBroadcastReceiver;
import com.jdmaestre.videotest.util.IabHelper;
import com.jdmaestre.videotest.util.IabResult;
import com.jdmaestre.videotest.util.Inventory;
import com.jdmaestre.videotest.util.Purchase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BuyingActivity extends Activity implements IabBroadcastReceiver.IabBroadcastListener{

    public static final String TAG = BuyingActivity.class.getSimpleName();
    private static final String SKU_VIDEOS = "unlock_videos";
    IInAppBillingService mService;
    IabHelper mHelper;
    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;

    Button buySuscriptionButton;
    TextView termsOfUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying);

        buySuscriptionButton = (Button) findViewById(R.id.buySuscriptionButton);
        termsOfUse = (TextView) findViewById(R.id.termsOfUseLabel);

        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgRl230zd6MYQOJwRZeMDV9J01dlbJhdyObvppFhvF3kw9OsCiV3aW7u1QEWL4XLgmZqi8InOO0h+wxb9Muxwgcw4LinS3T0wtjs7XVlGJhaBvr/TOfUR+CsURJXSzYQo26ZtN52yZm+/DPMafqcnvSlW3csHwzL9eCmyOpo9mOXlHq/rXTD4W0qWbRXr1KijmcMs8FArSlFsu11yIF1lTOHK1TWu0Yt1XQ/AJ5tMBECEH+6pXdggPnOUvhVLaWA9y1tYAi9Hs0YoGSvNjGTe7tFrBHKRcHth6uuJX7ki497RH5BB70oOOtBmXfLQ486uW2WPQ9PlpSP45EsvbQW5BwIDAQAB";

        // compute your public key and store it in base64EncodedPublicKey
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(false);

        if (isNetworkAvailable()){
            mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
                @Override
                public void onIabSetupFinished(IabResult result) {
                    if (!result.isSuccess()) {
                        // Oh no, there was a problem.
                        //Log.d(TAG, "Problem setting up In-app Billing: " + result);
                        Toast.makeText(getApplicationContext(), "Helper no configurado", Toast.LENGTH_LONG).show();
                    }else{
                        // Hooray, IAB is fully set up!
                        //Log.d(TAG, "Succes In-app Billing: " + result);

                        // Important: Dynamically register for broadcast messages about updated purchases.
                        // We register the receiver here instead of as a <receiver> in the Manifest
                        // because we always call getPurchases() at startup, so therefore we can ignore
                        // any broadcasts sent while the app isn't running.
                        // Note: registering this listener in an Activity is a bad idea, but is done here
                        // because this is a SAMPLE. Regardless, the receiver must be registered after
                        // IabHelper is setup, but before first call to getPurchases().
                        //mBroadcastReceiver = new IabBroadcastReceiver(BuyingActivity.this);
                        //IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                        //registerReceiver(mBroadcastReceiver, broadcastFilter);

                        // IAB is fully set up. Now, let's get an inventory of stuff we own.
                        //Log.d(TAG, "Setup successful. Querying inventory.");
                        try {
                            ArrayList<String> skuList = new ArrayList<String> ();
                            skuList.add("unlock_videos");
                            mHelper.queryInventoryAsync(true, null ,skuList, mGotInventoryListener);
                        } catch (IabHelper.IabAsyncInProgressException e) {
                            complain("Error querying inventory. Another async operation in progress.");
                        }

                    }
                }
            });
        }else{
            Toast.makeText(getApplicationContext(), "No esta conectado a internet", Toast.LENGTH_LONG).show();
        }

        buySuscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mHelper != null){
                    comprarHelper();
                }
            }
        });

        termsOfUse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                startActivity(intent);
            }
        });


    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
        @Override
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            //Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null) return;

            // Is it a failure?
            if (result.isFailure()) {
                complain("Failed to query inventory: " + result);
                return;
            }

            Purchase premiumPurchase = inventory.getPurchase("unlock_videos");

            if (premiumPurchase != null){
                Toast.makeText(getApplicationContext(),String.valueOf(premiumPurchase.getPurchaseState()) ,Toast.LENGTH_LONG).show();
                goToApp();
            }

        }
    };



    private void comprarHelper(){
        try {
            mHelper.launchPurchaseFlow(this, SKU_VIDEOS, 10001,
                    mPurchaseFinishedListener, "compra desde la app");
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase)
        {
            if (result.isFailure()) {
                //Log.d(TAG, "Error purchasing: " + result);
                return;
            }

            if (purchase.getSku().equals(SKU_VIDEOS)) {
                // consume the gas and update the UI
                goToApp();
                Toast.makeText(getApplicationContext(), "Compra realizadad con exito", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        }
        else {
            //Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    private void goToApp() {
        Intent intent = new Intent(getApplicationContext(), LoadDataActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }



    void complain(String message) {
        //Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        //Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }



    @Override
    public void receivedBroadcast() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null){
            try {
                mHelper.dispose();
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
        mHelper = null;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }
}
