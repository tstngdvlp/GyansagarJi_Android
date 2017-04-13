package com.gyansagarji.android.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyansagarji.android.Adapter.ViewPagerAdapter;
import com.gyansagarji.android.Fragments.Aboutus1;
import com.gyansagarji.android.Fragments.Aboutus2;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.AboutUs;
import com.gyansagarji.android.Rest.ApiClient;
import com.gyansagarji.android.Rest.ApiInterface;
import com.gyansagarji.android.utils.HtmlConverter;
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

public class AboutusActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView titleTV;
    ImageView backIMGV;
    LinearLayout backLL;
    ApiInterface apiServices;
    Progressdialog progressdialog;
    String title;
    public AboutUs aboutUs;
    ArrayList<AboutUs.AboutUsList> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aboutus);
        getSupportActionBar().hide();

        viewPager = (ViewPager) findViewById(R.id.viewpager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        backIMGV            =       (ImageView)findViewById(R.id.backIMGV);
        titleTV             =       (TextView)findViewById(R.id.titleTV);
        backLL              =       (LinearLayout)findViewById(R.id.backLL);

        titleTV.setText(getResources().getString(R.string.about));

        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AboutusActivity.this,MainActivity.class));
                finish();
            }
        });
        if(NetworkConnectivity.isNetworkAvailable(this)){
            loadInitials();
        }else if(PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.AboutList,"")!=""){
            aboutData();
        }


    }



    private void setupViewPager(ViewPager viewPager, AboutUs body) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new Aboutus1(AboutusActivity.this),body.getAboutlist().get(0).getTitle());
        viewPagerAdapter.addFragment(new Aboutus2(AboutusActivity.this),"Social Connect");
        viewPager.setAdapter(viewPagerAdapter);

    }


    private void loadInitials() {
        progressdialog      =   new Progressdialog();
        progressdialog.dialog(this);
        progressdialog.showDialog();
        apiServices         = ApiClient.getClient().create(ApiInterface.class);
        Call<AboutUs> call   =   apiServices.AboutUsList();
        call.enqueue(new Callback<AboutUs>() {
            @Override
            public void onResponse(Call<AboutUs> call, Response<AboutUs> response) {
                progressdialog.closeDialog();

                if(response.code()==200){

                    if(response.body().getStatus().equals("1")){
                        Gson gson = new Gson();
                        String aboutlist = gson.toJson(response.body());
                        PrefUtils.saveToPrefs(AboutusActivity.this,PrefUtils.AboutList,aboutlist);
                        aboutData();
                    }
                }
            }

            @Override
            public void onFailure(Call<AboutUs> call, Throwable t) {
                progressdialog.closeDialog();
            }
        });
    }

    private void aboutData() {
        String aboutlist = PrefUtils.getFromPrefs(this,PrefUtils.AboutList,"");
        Gson gson = new Gson();
        AboutUs aboutdata    =   gson.fromJson(aboutlist,AboutUs.class);
        aboutUs             =   aboutdata;
        list              =   aboutdata.getAboutlist();

        if(list.size()>0){
            Spanned about = HtmlConverter.fromHtml(list.get(0).getDescription());

        }

        setupViewPager(viewPager,aboutUs);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AboutusActivity.this,MainActivity.class));
        finish();
    }
}
