package com.gyansagarji.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.Parichay;
import com.gyansagarji.android.Rest.ApiClient;
import com.gyansagarji.android.Rest.ApiInterface;
import com.gyansagarji.android.utils.NetworkConnectivity;
import com.gyansagarji.android.utils.PrefUtils;
import com.gyansagarji.android.utils.Progressdialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by New android on 20-02-2017.
 */

public class JeevanParichay extends Activity {
    WebView parichayTV;TextView titleTV;
    ImageView backIMGV;
    LinearLayout backLL;
    ApiInterface apiService;
    ArrayList<Parichay.ParichayList> list;
    Progressdialog progressdialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jeevaparichay);
        backIMGV            =       (ImageView)findViewById(R.id.backIMGV);
        titleTV             =       (TextView)findViewById(R.id.titleTV);
        backLL              =       (LinearLayout)findViewById(R.id.backLL);


        parichayTV          =       (WebView)findViewById(R.id.parichayTV);

        parichayTV.setBackgroundColor(this.getResources().getColor(R.color.transparent));
        titleTV.setText(getResources().getString(R.string.parichay));
        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JeevanParichay.this,MainActivity.class));
                finish();
            }
        });


        newWorkChecking();

    }

    private void loadInitials() {
        progressdialog      =   new Progressdialog();
        progressdialog.dialog(this);
        progressdialog.showDialog();
        apiService          = ApiClient.getClient().create(ApiInterface.class);
        Call<Parichay> call = apiService.ParichayList();
        call.enqueue(new Callback<Parichay>() {
            @Override
            public void onResponse(Call<Parichay> call, Response<Parichay> response) {
                progressdialog.closeDialog();
                if(response.code() == 200){
                    if(response.body().getStatus().equals("1")){

                        Gson gson   =   new Gson();
                        String parichaylist = gson.toJson(response.body());

                        PrefUtils.saveToPrefs(getApplicationContext(),PrefUtils.ParichayList,parichaylist);
                        parichayData();
                    }
                }
            }

            @Override
            public void onFailure(Call<Parichay> call, Throwable t) {
                progressdialog.closeDialog();
            }
        });
    }

    private void parichayData() {
        try {
            Gson gson = new Gson();

            String parichaylist = PrefUtils.getFromPrefs(getApplicationContext(), PrefUtils.ParichayList, "");
            Parichay parichay = gson.fromJson(parichaylist, Parichay.class);

                if (parichay.getPrichaylist().size() > 0) {
                    list = parichay.getPrichaylist();
                }

                if (list.size() > 0) {

                    byte ptext[] = list.get(0).getDescription().getBytes("ISO-8859-1");
                    String value = new String(ptext, "UTF-8");

                    WebSettings settings = parichayTV.getSettings();
                    settings.setDefaultTextEncodingName("utf-8");
                    parichayTV.loadData(value, "text/html; charset=utf-8", null);

                }


            }catch(Exception e){
                e.printStackTrace();
            }
    }

    private void newWorkChecking() {
        if(NetworkConnectivity.isNetworkAvailable(JeevanParichay.this)){
            loadInitials();
        }else{
            if(PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.ParichayList,"")!=null){
                parichayData();
            }else{

            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(JeevanParichay.this,MainActivity.class));
        finish();
    }
}