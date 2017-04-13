package com.gyansagarji.android.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.gyansagarji.android.Adapter.ViewPagerAdapter;
import com.gyansagarji.android.Fragments.AudioActivity;
import com.gyansagarji.android.Fragments.VideoActivity;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.MediaAV;
import com.gyansagarji.android.Rest.ApiClient;
import com.gyansagarji.android.Rest.ApiInterface;
import com.gyansagarji.android.utils.NetworkConnectivity;
import com.gyansagarji.android.utils.PrefUtils;
import com.gyansagarji.android.utils.Progressdialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by New android on 20-02-2017.
 */

public class MediaActivity extends AppCompatActivity {

    TextView titleTV;
    ImageView backIMGV;
    LinearLayout backLL;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Progressdialog progressdialog;
    ApiInterface apiServices;
    public MediaAV mediaAV;

    public static LruCache<String,Bitmap> mcache = new LruCache<String, Bitmap>(1000);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media);
        backIMGV            =       (ImageView)findViewById(R.id.backIMGV);
        titleTV             =       (TextView)findViewById(R.id.titleTV);
        backLL              =       (LinearLayout)findViewById(R.id.backLL);

        getSupportActionBar().hide();

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        titleTV.setText(getResources().getString(R.string.media));



        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MediaActivity.this,MainActivity.class));
                finish();
            }
        });
        checkNetworkConnection();


    }

    private void checkNetworkConnection() {

        if(NetworkConnectivity.isNetworkAvailable(this)){

            loadInitials();

        }else{
            if(PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.MediaList,"").toString()!=""){
                loadMediadata();
            }

        }
    }

    private void loadMediadata() {
        String video = PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.MediaList,"");
        Gson gson = new Gson();
        MediaAV media = gson.fromJson(video,MediaAV.class);
        mediaAV     = media;
        setupViewPager(viewPager);
    }

    private void loadInitials() {

        progressdialog          =   new Progressdialog();
        progressdialog.dialog(this);
        progressdialog.showDialog();

        apiServices             =       ApiClient.getClient().create(ApiInterface.class);
        Call<MediaAV> call          =       apiServices.MeadiaList();
        call.enqueue(new Callback<MediaAV>() {
            @Override
            public void onResponse(Call<MediaAV> call, Response<MediaAV> response) {

                progressdialog.closeDialog();
                if(response.code()==200) {
                    if(response.body().getStatus().equals("1")) {

                        Gson gson   =   new Gson();
                        String medialist = gson.toJson(response.body());
                        PrefUtils.saveToPrefs(getApplicationContext(),PrefUtils.MediaList,medialist);
                        loadMediadata();
                    }
                }
            }

            @Override
            public void onFailure(Call<MediaAV> call, Throwable t) {
                progressdialog.closeDialog();
            }
        });


    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new AudioActivity(MediaActivity.this),"Audio");
        viewPagerAdapter.addFragment(new VideoActivity(MediaActivity.this),"Video");
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MediaActivity.this,MainActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }
}
