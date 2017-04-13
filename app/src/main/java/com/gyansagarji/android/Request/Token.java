package com.gyansagarji.android.Request;

import com.google.gson.annotations.SerializedName;


/**
 * Created by New android on 25-03-2017.
 */

public class Token {

    @SerializedName("token")
    String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
