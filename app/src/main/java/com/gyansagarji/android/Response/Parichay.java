package com.gyansagarji.android.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by New android on 23-03-2017.
 */

public class Parichay extends BaseResponse{

    public Parichay(String status, String return_short_message, String return_long_message) {
        super(status, return_short_message, return_long_message);
    }

    @SerializedName("result")
    ArrayList<ParichayList> prichaylist;

    public ArrayList<ParichayList> getPrichaylist() {
        return prichaylist;
    }

    public void setPrichaylist(ArrayList<ParichayList> prichaylist) {
        this.prichaylist = prichaylist;
    }

    public class ParichayList {
        @SerializedName("id")
        String id;
        @SerializedName("description")
        String description;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
