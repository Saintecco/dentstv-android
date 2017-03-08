package com.jdmaestre.videotest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.VideoView;

public class MainActivity extends Activity {

    private ListView videoListView;
    private VideoDataModel videoDataModel = new VideoDataModel();
    private VideoInfo[] videoInfos = videoDataModel.getVideosFromStaticData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoListView = (ListView) findViewById(R.id.videoListView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getStringVideoArray());

        videoListView.setAdapter(adapter);
        videoListView.setOnItemClickListener(mVideoClickedHandler);

    }

    private String[] getStringVideoArray(){
        String[] array = new String[videoInfos.length];
        for (int n = 0; n < videoInfos.length; n++){
            String videoName = videoInfos[n].getName();
            array[n] = videoName;
        }
        return  array;
    }

    private AdapterView.OnItemClickListener mVideoClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // Do something in response to the click
            Intent intent = new Intent(getApplicationContext(), VideoViewActivity.class);
            startActivity(intent);
        }
    };
}
