package com.jdmaestre.videotest;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by jdmaestre on 7/03/17.
 */

public class VideoDataModel extends Observable{

    private static final String TAG = VideoDataModel.class.getSimpleName();
    private String language =Locale.getDefault().getISO3Language();

    private static VideoDataModel instance = null;

    public static VideoDataModel getInstance(){
        if (instance == null){
            instance = new VideoDataModel();
        }
        return instance;
    }

    private ArrayList<String> sectionsList = new ArrayList<String>();
    private ArrayList<VideoInfo> videoInfos = new ArrayList<VideoInfo>();

    public boolean isDataLoaded() {
        return isDataLoaded;
    }

    private boolean isDataLoaded = false;

    public void loadData(Observer observer){
        addObserver(observer);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("videosDataBase");

        // Read from database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataBase) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                if (!isDataLoaded){
                    DataSnapshot dataSnapshot = dataBase.child("categorias");
                    String section = "";
                    Iterable<DataSnapshot> sections = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : sections){
                        DataSnapshot child = snapshot.child("name");
                        section = child.getValue(String.class);
                        sectionsList.add(section);
                    }

                    dataSnapshot = dataBase.child("videos");
                    VideoInfo video = null;
                    Iterable<DataSnapshot> videos = dataSnapshot.getChildren();
                    for (DataSnapshot snapshot : videos){
                        video = snapshot.getValue(VideoInfo.class);
                        videoInfos.add(video);
                    }

                    isDataLoaded = true;
                }

                setChanged();
                notifyObservers();
                //VideoInfo value = dataSnapshot.child("video_1").getValue(VideoInfo.class);
                //Log.d(TAG, "Value is: " + video.getLink());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public void loadData2(Observer observer){
        addObserver(observer);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("VideosDB");

        // Read from database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataBase) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                if (!isDataLoaded){

                    //Get the categories
                    String section = "";
                    Iterable<DataSnapshot> sections = dataBase.getChildren();
                    for (DataSnapshot snapshot : sections){
                        DataSnapshot child = snapshot.child("spa");
                        section = child.getValue(String.class);
                        sectionsList.add(section);
                        //Log.d(TAG, "Secciones:" );
                        //Log.d(TAG, "Value is: " + section);
                    }

                    VideoInfo video = null;
                    sections = dataBase.getChildren();
                    for (DataSnapshot snapshot : sections){
                        Iterable<DataSnapshot> videos = snapshot.getChildren();
                        for (DataSnapshot videosSnap : videos){
                            if (videosSnap.hasChildren()){
                                video = videosSnap.child("spa").getValue(VideoInfo.class);
                                if (video != null){
                                    videoInfos.add(video);
                                    //Log.d(TAG, "Value is: " + video.getName());
                                }
                            }

                        }
                    }

                    isDataLoaded = true;
                }

                setChanged();
                notifyObservers();
                //VideoInfo value = dataSnapshot.child("video_1").getValue(VideoInfo.class);
                //Log.d(TAG, "Value is: " + video.getLink());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    public ArrayList<VideoInfo> getVideoInfos() {
        return videoInfos;
    }

    public ArrayList<String> getSectionsList() {
        return sectionsList;
    }

}
