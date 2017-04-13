package com.gyansagarji.android.fcm;

import android.util.Log;


import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.gyansagarji.android.utils.PrefUtils;

/**
 * Created by New android on 27-02-2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
       String refrehedToken = FirebaseInstanceId.getInstance().getToken();
        PrefUtils.saveToPrefs(getApplicationContext(),PrefUtils.DEVICETOKEN,refrehedToken);
        sendRegistrationToServer(refrehedToken);

    }

    private void sendRegistrationToServer(String refrehedToken) {

    }

}
