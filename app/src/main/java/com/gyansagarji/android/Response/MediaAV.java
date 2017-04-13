package com.gyansagarji.android.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by New android on 22-03-2017.
 */

public class MediaAV extends BaseResponse {


    public MediaAV(String status, String return_short_message, String return_long_message) {
        super(status, return_short_message, return_long_message);
    }

    @SerializedName("result")
    ArrayList<AudioList> audiolist;

    public ArrayList<AudioList> getAudiolist() {
        return audiolist;
    }

    public void setAudiolist(ArrayList<AudioList> audiolist) {
        this.audiolist = audiolist;
    }

    public class AudioList {
        @SerializedName("id")
        String id;
        @SerializedName("title")
        String title;
        @SerializedName("image")
        String image;
        @SerializedName("audio")
        String audio;
        @SerializedName("url")
        String url;

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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getAudio() {
            return audio;
        }

        public void setAudio(String audio) {
            this.audio = audio;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }



    @SerializedName("result1")
    ArrayList<VideoList>videolist;

    public ArrayList<VideoList> getVideolist() {
        return videolist;
    }

    public void setVideolist(ArrayList<VideoList> videolist) {
        this.videolist = videolist;
    }

    public class VideoList {
        @SerializedName("id")
        String id;
        @SerializedName("title")
        String title;
        @SerializedName("image")
        String image;
        @SerializedName("video")
        String video;
        @SerializedName("url")
        String url;

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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
