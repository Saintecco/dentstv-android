package com.jdmaestre.videotest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.MediaController;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);
        //position = savedInstanceState.getInt("Position");


        videoLink = getIntent().getStringExtra("videoLink");
        if (videoLink == null){
            videoLink = "https://archive.org/download/popeye_patriotic_popeye/popeye_patriotic_popeye_512kb.mp4";
        }

        videoView = (VideoView) findViewById(R.id.videoViewTest);
        thisLayout = (FrameLayout) findViewById(R.id.activity_main);
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


    @Override
    protected void onPause() {
        super.onPause();
        position = videoView.getCurrentPosition();
        videoView.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.seekTo(position);
        videoView.start();
        startWaitingDialog();
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
            progDailog = ProgressDialog.show(this, "Please wait ...", "Retrieving data ...", true);
            isWaitingDialogOnScreen = true;
        }
    }

    private void stopWaitingDialog(){

        progDailog.dismiss();
        isWaitingDialogOnScreen = false;

    }
}
