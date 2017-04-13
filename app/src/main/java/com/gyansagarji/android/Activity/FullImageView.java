package com.gyansagarji.android.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

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
 * Created by New android on 27-03-2017.
 */

public class FullImageView extends Activity {
NetworkImageView fullimageIMGV;
    RequestQueue requestQueue;
    ImageLoader imageLoader;
    ArrayList<News.NewsList> list;
    ImageView newsIMGV;
    private String downloadurl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullimageview);

        fullimageIMGV               =       (NetworkImageView)findViewById(R.id.fullimageIMGV);
        newsIMGV                    =       (ImageView)findViewById(R.id.newsIMGV);
        requestQueue                =       VolleySingleton.getInstance().getRequestQueue();
        imageLoader                 =       new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String,Bitmap> mcache = new LruCache<String, Bitmap>(1000);
            @Override
            public Bitmap getBitmap(String url) {
                return mcache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                mcache.put(url,bitmap);
            }
        });
        loadImage();

    }

    private void loadImage() {
        String newslist = PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.NewsList,"");
        Gson gson       =   new Gson();
        News news       =   gson.fromJson(newslist,News.class);
        list            =   news.getNewsList();
        if(list.size()>0){
            for (int i=0;i<list.size();i++){
                if(Constants.NewItemId == i){

                    String imgurl[] =  list.get(i).getImage().split("\\?");

                    downloadurl       = Constants.ImageBaseUrl+ imgurl[0];
                    String fileName = downloadurl.substring(downloadurl.lastIndexOf("/"));

                    File file = new File(Environment.getExternalStorageDirectory(),"GyanSagarJi"+File.separator+"News/"+fileName);


                    if (!file.exists()){

                        newsIMGV.setVisibility(View.GONE);
                        fullimageIMGV.setVisibility(View.VISIBLE);
                        fullimageIMGV.setDefaultImageResId(R.drawable.loadingbig);
                        fullimageIMGV.setErrorImageResId(R.drawable.ic_error);
                        fullimageIMGV.setImageUrl(Constants.ImageBaseUrl + list.get(i).getImage().toString().replace(" ","%20"), imageLoader);
                    }else {

                        newsIMGV.setVisibility(View.VISIBLE);
                        fullimageIMGV.setVisibility(View.GONE);
                        newsIMGV.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));

                    }

                }
            }
        }

    }


}
