package com.gyansagarji.android.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by New android on 23-03-2017.
 */

public class BaseResponse {

    @SerializedName("status")
    String status;
    @SerializedName("return_short_message")
    String return_short_message;
    @SerializedName("return_long_message")
    String return_long_message;

    public BaseResponse(String status, String return_short_message, String return_long_message) {
        this.status = status;
        this.return_short_message = return_short_message;
        this.return_long_message = return_long_message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReturn_short_message() {
        return return_short_message;
    }

    public void setReturn_short_message(String return_short_message) {
        this.return_short_message = return_short_message;
    }

    public String getReturn_long_message() {
        return return_long_message;
    }

    public void setReturn_long_message(String return_long_message) {
        this.return_long_message = return_long_message;
    }
}
