package com.gyansagarji.android.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by New android on 03-04-2017.
 */

public class Maps extends BaseResponse {


    public Maps(String status, String return_short_message, String return_long_message) {
        super(status, return_short_message, return_long_message);
    }

    @SerializedName("result")
    ArrayList<LocationList> locationList;

    public ArrayList<LocationList> getLocationList() {
        return locationList;
    }

    public void setLocationList(ArrayList<LocationList> locationList) {
        this.locationList = locationList;
    }

    public class LocationList {
        @SerializedName("id")
        String id;
        @SerializedName("title")
        String title;
        @SerializedName("lang")
        String lang;
        @SerializedName("latt")
        String latt;
        @SerializedName("create_date")
        String create_date;

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

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getLatt() {
            return latt;
        }

        public void setLatt(String latt) {
            this.latt = latt;
        }

        public String getCreate_date() {
            return create_date;
        }

        public void setCreate_date(String create_date) {
            this.create_date = create_date;
        }
    }
}
