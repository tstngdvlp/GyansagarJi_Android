package com.gyansagarji.android.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by New android on 22-03-2017.
 */

public class News extends BaseResponse{

    @SerializedName("result")
    ArrayList<NewsList> newsList;

    public News(String status, String return_short_message, String return_long_message) {
        super(status, return_short_message, return_long_message);
    }

    public ArrayList<NewsList> getNewsList() {
        return newsList;
    }

    public void setNewsList(ArrayList<NewsList> newsList) {
        this.newsList = newsList;
    }

    public class NewsList {

        @SerializedName("id")
        String id;
        @SerializedName("image_icon")
        String image_icon;
        @SerializedName("title")
        String title;
        @SerializedName("description")
        String description;
        @SerializedName("short_desc")
        String short_desc;
        @SerializedName("image")
        String image;
        @SerializedName("date")
        String date;
        @SerializedName("time")
        String time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage_icon() {
            return image_icon;
        }

        public void setImage_icon(String image_icon) {
            this.image_icon = image_icon;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getShort_desc() {
            return short_desc;
        }

        public void setShort_desc(String short_desc) {
            this.short_desc = short_desc;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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
    }
}
