package com.gyansagarji.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.gyansagarji.android.Adapter.NewsAdapter;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.News;
import com.gyansagarji.android.Rest.ApiClient;
import com.gyansagarji.android.Rest.ApiInterface;
import com.gyansagarji.android.utils.Constants;
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

public class NewsActivity extends Activity {
    ApiInterface apiService;
    NewsAdapter newsAdapter;
    ListView newsLV;
    TextView titleTV;
    ImageView backIMGV;
    LinearLayout backLL;
    LinearLayout newsLL;
    ArrayList<News.NewsList> list;
    Progressdialog progressDialog;
    public static LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);
        backIMGV            =       (ImageView)findViewById(R.id.backIMGV);
        titleTV             =       (TextView)findViewById(R.id.titleTV);
        newsLL              =       (LinearLayout)findViewById(R.id.newsLL);
        newsLV              =       (ListView)findViewById(R.id.newsLV);
        backLL              =       (LinearLayout)findViewById(R.id.backLL);
        titleTV.setText(getResources().getString(R.string.news));
        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewsActivity.this,MainActivity.class));
                finish();
            }
        });
        newsLV.setItemsCanFocus(false);
        newsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(NewsActivity.this, NewsDetailActivity.class));
                Constants.NewItemId = i;
            }
        });

        newWorkChecking();

    }

    private void loadInitials() {

        progressDialog          =   new Progressdialog();
        progressDialog.dialog(this);
        progressDialog.showDialog();
        apiService             =       ApiClient.getClient().create(ApiInterface.class);
        Call<News>call          =       apiService.NewsList();
        call.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                progressDialog.closeDialog();
                if(response.code()==200) {
                    if(response.body().getStatus().equals("1")) {

                        Gson gson   =   new Gson();
                        String newslist = gson.toJson(response.body());
                        PrefUtils.saveToPrefs(getApplicationContext(),PrefUtils.NewsList,newslist);
                        newsData();
                    }
                }
            }
            @Override
            public void onFailure(Call<News> call, Throwable t) {
                progressDialog.closeDialog();
            }
        });

    }

    private void newsData() {
        try {

            Gson gson   =   new Gson();
        String newslist = PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.NewsList,"");
        News news = gson.fromJson(newslist, News.class);
        if(newslist!=null) {

            list = news.getNewsList();

            if (list.size() > 0) {
                newsAdapter = new NewsAdapter(this, list);
                newsLV.setAdapter(newsAdapter);
            }
        }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void newWorkChecking() {
        if(NetworkConnectivity.isNetworkAvailable(NewsActivity.this)){
            loadInitials();
        }else{
            if(PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.NewsList,"")!=""){
                newsData();
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(NewsActivity.this,MainActivity.class));
        finish();
    }
}
