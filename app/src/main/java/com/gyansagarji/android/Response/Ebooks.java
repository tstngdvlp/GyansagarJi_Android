package com.gyansagarji.android.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by New android on 23-03-2017.
 */

public class Ebooks extends BaseResponse {

    public Ebooks(String status, String return_short_message, String return_long_message) {
        super(status, return_short_message, return_long_message);
    }

    @SerializedName("result")
    ArrayList<EbooksList> ebookslist;



    public ArrayList<EbooksList> getEbookslist() {
        return ebookslist;
    }

    public void setEbookslist(ArrayList<EbooksList> ebookslist) {
        this.ebookslist = ebookslist;
    }

    public class EbooksList {

        @SerializedName("id")
        String id;
        @SerializedName("title")
        String title;
        @SerializedName("image")
        String image;
        @SerializedName("pdf")
        String pdf;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPdf() {
            return pdf;
        }

        public void setPdf(String pdf) {
            this.pdf = pdf;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}
