package com.gyansagarji.android.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by New android on 25-03-2017.
 */

public class SubInfo {
    @SerializedName("id")
    String id;
    @SerializedName("album_name")
    String album_name;
    @SerializedName("des")
    String des;
    @SerializedName("date")
    String  date;
    @SerializedName("time")
    String time;
    @SerializedName("tiny_image")
    String tiny_image;
    @SerializedName("image")
    String image;
    @SerializedName("image_name")
    String image_name;

    @SerializedName("sub_info")
    ArrayList<SubInfo> sunInfo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTiny_image() {
        return tiny_image;
    }

    public void setTiny_image(String tiny_image) {
        this.tiny_image = tiny_image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public ArrayList<SubInfo> getSunInfo() {
        return sunInfo;
    }

    public void setSunInfo(ArrayList<SubInfo> sunInfo) {
        this.sunInfo = sunInfo;
    }

}
