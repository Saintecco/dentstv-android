package com.jdmaestre.videotest;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
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

    VideoViewCustom videoViewCustom;

    FrameLayout thisLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view);

        //webView = (WebView) findViewById(R.id.webView);
        videoView = (VideoView) findViewById(R.id.videoViewTest);
        thisLayout = (FrameLayout) findViewById(R.id.activity_main);
        videoViewCustom = new VideoViewCustom(this);

        //thisLayout.removeView(videoView);
        thisLayout.addView(videoViewCustom);
        videoViewCustom.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        //WebSettings webSettings = webView.getSettings();
        //webSettings.setJavaScriptEnabled(true);
        //webView.loadData("<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/n-ANUWEjIV8?rel=0\" frameborder=\"0\" allowfullscreen></iframe>",
        //        "text/html", "utf-8");

        String vidAddress = "https://archive.org/download/popeye_patriotic_popeye/popeye_patriotic_popeye_512kb.mp4";
        Uri vidUri = Uri.parse(vidAddress);

        videoView.setVideoURI(vidUri);
        videoView.start();

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
