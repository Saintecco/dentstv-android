package com.jdmaestre.videotest;

/**
 * Created by jdmaestre on 7/03/17.
 */

public class VideoDataModel {


    public VideoInfo[] getVideosFromStaticData(){

        VideoInfo video1 = new VideoInfo("Video 1", "Muela del juicio",
                "https://dl.dropbox.com/s/k5jhdvvfmsjff5f/Pericoronaritis%20y%20extracci%C3%B3n%20de%20las%20muelas%20del%20juicio%20%C2%A9.mp4");

        VideoInfo[] videoArray = {video1};
        return videoArray ;
    }
}
