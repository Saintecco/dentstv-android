package com.jdmaestre.videotest;

import java.util.ArrayList;

/**
 * Created by jdmaestre on 7/03/17.
 */

public class VideoInfo {

    private String name;
    private String description;
    private String link;
    private String category;
    private String category_2;
    private String category_3;
    private String image;

    VideoInfo(String name, String description, String link){
        this.name = name;
        this.description = description;
        this.link = link;
    }

    VideoInfo(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory_2() {
        return category_2;
    }

    public void setCategory_2(String category) {
        this.category_2 = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


}
