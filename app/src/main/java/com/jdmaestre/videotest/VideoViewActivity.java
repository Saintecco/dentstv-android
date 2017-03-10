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
import android.view.Display;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoViewActivity extends Activity {

    WebView webView;
    VideoView videoView;

    String videoLink = null;

    VideoViewCustom videoViewCustom;

    FrameLayout thisLayout;
    ProgressDialog progDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);


        videoLink = getIntent().getStringExtra("videoLink");
        if (videoLink == null){
            videoLink = "https://archive.org/download/popeye_patriotic_popeye/popeye_patriotic_popeye_512kb.mp4";
        }

        //webView = (WebView) findViewById(R.id.webView);
        videoView = (VideoView) findViewById(R.id.videoViewTest);
        thisLayout = (FrameLayout) findViewById(R.id.activity_main);
        videoViewCustom = new VideoViewCustom(this);

        //thisLayout.removeView(videoView);
        thisLayout.addView(videoViewCustom);
        videoViewCustom.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));


        String vidAddress = videoLink;
        Uri vidUri = Uri.parse(vidAddress);

        videoView.setVideoURI(vidUri);
        videoView.start();
        progDailog = ProgressDialog.show(this, "Please wait ...", "Retrieving data ...", true);

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                progDailog.dismiss();
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
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

            videoViewCustom.setDimensions(width, height);
            videoViewCustom.getHolder().setFixedSize(width, height);

        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

            videoViewCustom.setDimensions(width, height);
            videoViewCustom.getHolder().setFixedSize(width, height);

        }
    }


}
