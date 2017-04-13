package com.gyansagarji.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gyansagarji.android.R;
import com.gyansagarji.android.Request.Token;
import com.gyansagarji.android.Response.Tokenresponse;
import com.gyansagarji.android.Rest.ApiClient;
import com.gyansagarji.android.Rest.ApiInterface;
import com.gyansagarji.android.utils.PrefUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by New android on 21-02-2017.
 */

public class SplashScreen extends Activity {
    ApiInterface apiServices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);

        start();
    }

    private void start() {

        Thread splash = new Thread(){
            @Override
            public void run(){
                super.run();

                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {

                       sendDeviceToken();
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    SplashScreen.this.finish();
                }

            }

        };
        splash.start();
    }

    private void sendDeviceToken() {
        Token token = new Token();
        token.setToken(PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.DEVICETOKEN,""));

        apiServices         = ApiClient.getClient().create(ApiInterface.class);
        Call<Tokenresponse> call = apiServices.deviceToken(token.getToken());
        call.enqueue(new Callback<Tokenresponse>() {
            @Override
            public void onResponse(Call<Tokenresponse> call, Response<Tokenresponse> response) {


            }

            @Override
            public void onFailure(Call<Tokenresponse> call, Throwable t) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
