package com.gyansagarji.android.Response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by New android on 23-03-2017.
 */

public class Gallery extends BaseResponse {

    public Gallery(String status, String return_short_message, String return_long_message) {
        super(status, return_short_message, return_long_message);
    }

    @SerializedName("main_result")
    ArrayList<SubInfo> subInfos;

    public ArrayList<SubInfo> getSubInfos() {
        return subInfos;
    }

    public void setSubInfos(ArrayList<SubInfo> subInfos) {
        this.subInfos = subInfos;
    }
}
