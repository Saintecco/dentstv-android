package com.jdmaestre.videotest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class VideoViewActivity extends Activity {

    private final static String TAG = VideoViewActivity.class.getSimpleName();

    VideoView videoView;

    String videoLink = null;
    int position;
    boolean isWaitingDialogOnScreen = false;

    VideoViewCustom videoViewCustom;

    FrameLayout thisLayout;
    ProgressDialog progDailog;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);
        //position = savedInstanceState.getInt("Position");


        videoLink = getIntent().getStringExtra("videoLink");
        if (videoLink == null){
            alert("Oops!", "No fue posible encontrar el video que esta buscando.");
        }else{
            videoView = (VideoView) findViewById(R.id.videoViewTest);
            thisLayout = (FrameLayout) findViewById(R.id.activity_main);
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            videoViewCustom = new VideoViewCustom(this);

            thisLayout.addView(videoViewCustom);
            videoViewCustom.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));


            String vidAddress = videoLink;
            Uri vidUri = Uri.parse(vidAddress);

            videoView.setVideoURI(vidUri);
            videoView.start();
            startWaitingDialog();

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    stopWaitingDialog();

                }
            });

            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    progDailog.dismiss();
                    return false;
                }
            });

            MediaController vidControl = new MediaController(this);
            videoView.setMediaController(vidControl);
            vidControl.setAnchorView(videoView);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (videoLink!=null) {
            position = videoView.getCurrentPosition();
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoLink != null){
            videoView.seekTo(position);
            videoView.start();
            startWaitingDialog();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("Position", videoView.getCurrentPosition());
        Log.v(TAG, "Pauset");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int position = savedInstanceState.getInt("Position");
        videoView.seekTo(position);
        videoView.pause();
        //startWaitingDialog();
        Log.v(TAG, "Startt: " + String.valueOf(position) );
    }

//            android:configChanges="keyboardHidden|orientation|screenSize"

    private void startWaitingDialog(){
        if (isWaitingDialogOnScreen == false){
           // progDailog = ProgressDialog.show(this, "Please wait ...", "Retrieving data ...", true);
            isWaitingDialogOnScreen = true;
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void stopWaitingDialog(){

        //progDailog.dismiss();
        progressBar.setVisibility(View.INVISIBLE);
        isWaitingDialogOnScreen = false;

    }

    void alert(String title ,String message) {

        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setTitle(title);
        bld.setMessage(message);
        bld.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        //Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }
}
