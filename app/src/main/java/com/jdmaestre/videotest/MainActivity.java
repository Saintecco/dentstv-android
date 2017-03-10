package com.jdmaestre.videotest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends Activity implements Observer{


    private ListView videoListView;
    private VideoDataModel videoDataModel = new VideoDataModel();
    private ArrayList<VideoInfo> videoInfos;

    ProgressDialog progDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progDailog = ProgressDialog.show(this, "Please wait ...", "Retrieving data ...", true);

        videoListView = (ListView) findViewById(R.id.videoListView);
        videoDataModel.getVideosFromFirebase(this);
        //Update Observer do the job

    }

    private String[] getStringVideoArray(){
        String[] array = new String[videoInfos.size()];
        for (int n = 0; n < videoInfos.size(); n++){
            String videoName = videoInfos.get(n).getName();
            array[n] = videoName;
        }
        return  array;
    }

    private AdapterView.OnItemClickListener mVideoClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // Do something in response to the click
            Intent intent = new Intent(getApplicationContext(), VideoViewActivity.class);
            intent.putExtra("videoLink", videoInfos.get(position).getLink());
            startActivity(intent);
        }
    };

    @Override
    public void update(Observable observable, Object o) {

        videoInfos = (ArrayList<VideoInfo>) o;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, getStringVideoArray());
        videoListView.setAdapter(adapter);
        videoListView.setOnItemClickListener(mVideoClickedHandler);

        progDailog.dismiss();

    }
}
