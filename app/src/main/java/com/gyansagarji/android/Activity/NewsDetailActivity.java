package com.gyansagarji.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.News;
import com.gyansagarji.android.utils.Constants;
import com.gyansagarji.android.utils.PrefUtils;
import com.gyansagarji.android.utils.VolleySingleton;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by New android on 23-02-2017.
 */

public class NewsDetailActivity extends Activity {
    TextView titleTV;
    ImageView backIMGV;
    LinearLayout backLL;
    ImageView newsIMGV;
    ArrayList<News.NewsList> list;
    ImageLoader imageLoader;
    RequestQueue requestQueue;
    TextView dateTV;
    TextView timeTV;
    WebView newsTV;
    private String downloadurl;
    NetworkImageView newsIMGV2;
    String mTimestamp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsdetailview);

        backIMGV                    =       (ImageView)findViewById(R.id.backIMGV);
        titleTV                     =       (TextView)findViewById(R.id.titleTV);
        backLL                      =       (LinearLayout)findViewById(R.id.backLL);

        newsIMGV                    =       (ImageView)findViewById(R.id.newsIMGV);
        dateTV                      =       (TextView)findViewById(R.id.dateTV);
        newsTV                      =       (WebView)findViewById(R.id.newsTV);
        timeTV                      =       (TextView)findViewById(R.id.timeTV);
        newsIMGV2                   =       (NetworkImageView)findViewById(R.id.newsIMGV2);
        newsTV.setBackgroundColor(this.getResources().getColor(R.color.transparent));
        requestQueue                =       VolleySingleton.getInstance().getRequestQueue();
        imageLoader                 =       new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return NewsActivity.mCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                NewsActivity.mCache.put(url,bitmap);
            }


        });
        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        newsIMGV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NewsDetailActivity.this,FullImageView.class));
            }
        });

    loadNewsItem();

    }

    private void loadNewsItem() {

        String newslist = PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.NewsList,"");
        Gson gson       =   new Gson();
        News news       =   gson.fromJson(newslist,News.class);
        list            =   news.getNewsList();
        if(list.size()>0){
            for (int i=0;i<list.size();i++){
                if(Constants.NewItemId == i) {
                    titleTV.setText(list.get(i).getTitle());
                    dateTV.setText(list.get(i).getDate());
                    timeTV.setText(list.get(i).getTime());


                    String imgurl[] =  list.get(i).getImage().split("\\?");

                    downloadurl       = Constants.ImageBaseUrl+ imgurl[0];
                    String fileName = downloadurl.substring(downloadurl.lastIndexOf("/"));

                    File file = new File(Environment.getExternalStorageDirectory(),"GyanSagarJi"+File.separator+"News/"+fileName);

                    try{
                    if (!file.exists()){

                        newsIMGV.setVisibility(View.GONE);
                        newsIMGV2.setVisibility(View.VISIBLE);
                        newsIMGV2.setDefaultImageResId(R.drawable.loadingbig);
                        newsIMGV2.setErrorImageResId(R.drawable.ic_error);
                        newsIMGV2.setImageUrl(Constants.ImageBaseUrl + list.get(i).getImage().toString().replace(" ","%20"), imageLoader);
                    }else {
                        newsIMGV.setVisibility(View.VISIBLE);
                        newsIMGV2.setVisibility(View.GONE);
                        newsIMGV.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                        newsIMGV2.setDefaultImageResId(R.drawable.loadingbig);
                        newsIMGV2.setErrorImageResId(R.drawable.ic_error);
                        newsIMGV2.setImageUrl(Constants.ImageBaseUrl + list.get(i).getImage().toString().replace(" ","%20"), imageLoader);

                    }


                        byte pretext[] = list.get(i).getDescription().toString().getBytes("ISO-8859-1");
                        String value = new String(pretext,"UTF-8");
                        WebSettings settings = newsTV.getSettings();
                        settings.setDefaultTextEncodingName("utf-8");
                        newsTV.loadData(value, "text/html; charset=utf-8", null);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
