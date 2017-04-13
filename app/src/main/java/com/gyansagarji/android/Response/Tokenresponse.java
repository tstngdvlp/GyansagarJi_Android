package com.gyansagarji.android.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by New android on 25-03-2017.
 */

public class Tokenresponse extends BaseResponse {


    public Tokenresponse(String status, String return_short_message, String return_long_message) {
        super(status, return_short_message, return_long_message);
    }

    @SerializedName("result")
    String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
