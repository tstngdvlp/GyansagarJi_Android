package com.gyansagarji.android.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by New android on 24-03-2017.
 */

public class AboutUs extends BaseResponse {
    public AboutUs(String status, String return_short_message, String return_long_message) {
        super(status, return_short_message, return_long_message);
    }

    @SerializedName("result")
    ArrayList<AboutUsList> aboutlist;

    public ArrayList<AboutUsList> getAboutlist() {
        return aboutlist;
    }

    public void setAboutlist(ArrayList<AboutUsList> aboutlist) {
        this.aboutlist = aboutlist;
    }

    public class AboutUsList {

        @SerializedName("id")
        String id;
        @SerializedName("title")
        String title;
        @SerializedName("description")
        String description;
        @SerializedName("googleplus_url")
        String googleplus_url;
        @SerializedName("youtube_url")
        String youtube_url;
        @SerializedName("twitter_url")
        String twitter_url;
        @SerializedName("facebook_url")
        String facebook_url;
        @SerializedName("date")
        String date;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getGoogleplus_url() {
            return googleplus_url;
        }

        public void setGoogleplus_url(String googleplus_url) {
            this.googleplus_url = googleplus_url;
        }

        public String getYoutube_url() {
            return youtube_url;
        }

        public void setYoutube_url(String youtube_url) {
            this.youtube_url = youtube_url;
        }

        public String getTwitter_url() {
            return twitter_url;
        }

        public void setTwitter_url(String twitter_url) {
            this.twitter_url = twitter_url;
        }

        public String getFacebook_url() {
            return facebook_url;
        }

        public void setFacebook_url(String facebook_url) {
            this.facebook_url = facebook_url;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }


    }
}
