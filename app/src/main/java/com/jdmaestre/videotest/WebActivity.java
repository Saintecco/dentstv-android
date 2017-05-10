package com.jdmaestre.videotest;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;


public class WebActivity extends Activity {

    WebView browser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        browser = (WebView) findViewById(R.id.webPage);
        browser.loadUrl("http://dentstv.com/legal/terms/");
    }
}
