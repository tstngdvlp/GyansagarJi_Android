package com.gyansagarji.android.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by New android on 07-03-2017.
 */

public class NetworkConnectivity {

    public static boolean isNetworkAvailable(Context context){
        boolean outcomewifi = false;
        boolean outcomemobile = false;

        if(context!=null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] networkInfo = cm.getAllNetworkInfo();

            for(NetworkInfo nwinfo : networkInfo) {
                if (nwinfo.getType() == ConnectivityManager.TYPE_WIFI){
                    if (nwinfo.isConnected()) {
                        outcomewifi = true;
                    }
            }
            if(nwinfo.getType() == ConnectivityManager.TYPE_MOBILE){
                if (nwinfo.isConnected()) {
                    outcomemobile = true;
                }
            }
            }

        }
        return outcomewifi || outcomemobile;
    }
}
