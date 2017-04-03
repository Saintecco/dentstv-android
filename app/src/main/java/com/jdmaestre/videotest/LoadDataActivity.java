package com.jdmaestre.videotest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

public class LoadDataActivity extends Activity implements Observer {

    ProgressDialog progDailog;
    boolean isWaitingDialogOnScreen = false;
    private VideoDataModel videoDataModel = VideoDataModel.getInstance();

    Button tryAgainButton;
    Button exitButton;
    ImageView logoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_data);

        tryAgainButton = (Button) findViewById(R.id.tryAgain_LoadData_Button);
        exitButton = (Button) findViewById(R.id.exit_LoadData_button);
        logoImageView = (ImageView) findViewById(R.id.loadData_imageView);

        loadData();

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void loadData() {
        if (isNetworkAvailable()){
            startWaitingDialog();
            videoDataModel.loadData(this);
        }else{
            Toast.makeText(getApplication(),"No esta conectado a internet", Toast.LENGTH_LONG).show();
            tryAgainButton.setVisibility(View.VISIBLE);
            exitButton.setVisibility(View.VISIBLE);
            logoImageView.setVisibility(View.VISIBLE);
        }
    }

    private void startWaitingDialog(){
        if (isWaitingDialogOnScreen == false){
            progDailog = ProgressDialog.show(this, "Please wait ...", "Retrieving data ...", true);
            isWaitingDialogOnScreen = true;
        }
    }

    private void stopWaitingDialog(){

        progDailog.dismiss();
        isWaitingDialogOnScreen = false;

    }

    @Override
    public void update(Observable observable, Object o) {
        stopWaitingDialog();
        Intent intent = new Intent(getApplication(), Main2Activity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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
