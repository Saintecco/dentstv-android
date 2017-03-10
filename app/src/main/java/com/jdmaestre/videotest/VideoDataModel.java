package com.jdmaestre.videotest;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by jdmaestre on 7/03/17.
 */

public class VideoDataModel extends Observable{

    private static final String TAG = VideoDataModel.class.getSimpleName();

    public void getVideosFromStaticData(Observer observer){

        VideoInfo video1 = new VideoInfo("Video 1", "Muela del juicio",
                "https://dl.dropbox.com/s/k5jhdvvfmsjff5f/Pericoronaritis%20y%20extracci%C3%B3n%20de%20las%20muelas%20del%20juicio%20%C2%A9.mp4");

        VideoInfo[] videoArray = {video1};
    }

    public void getVideosFromFirebase(Observer observer){

        addObserver(observer);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("videos");
        final ArrayList<VideoInfo> arrayList = new ArrayList<VideoInfo>();

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                VideoInfo video = null;
                Iterable<DataSnapshot> videos = dataSnapshot.getChildren();
                for (DataSnapshot snapshot : videos){
                    video = snapshot.getValue(VideoInfo.class);
                    arrayList.add(video);
                    Log.d(TAG, "Value is: " + video.getName());

                    setChanged();
                    notifyObservers(arrayList);
                }
                //VideoInfo value = dataSnapshot.child("video_1").getValue(VideoInfo.class);
                //Log.d(TAG, "Value is: " + video.getLink());

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
}
