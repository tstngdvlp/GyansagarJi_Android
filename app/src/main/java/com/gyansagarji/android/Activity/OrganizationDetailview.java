package com.gyansagarji.android.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.LruCache;
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
import com.gyansagarji.android.Response.Organization;
import com.gyansagarji.android.utils.Constants;
import com.gyansagarji.android.utils.PrefUtils;
import com.gyansagarji.android.utils.VolleySingleton;

import java.util.ArrayList;


/**
 * Created by New android on 17-03-2017.
 */

public class OrganizationDetailview extends Activity {
    TextView titleTV,placeTV;
    ImageView backIMGV;
    LinearLayout backLL;
    NetworkImageView orgnizationIMGV;
    WebView descTV;
    ImageLoader imageLoader;
    RequestQueue requestQueue;
    ArrayList<Organization.OrganizationList> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organization_detailview);
        backIMGV            =       (ImageView)findViewById(R.id.backIMGV);
        titleTV             =       (TextView)findViewById(R.id.titleTV);
        backLL              =       (LinearLayout)findViewById(R.id.backLL);
        placeTV             =       (TextView)findViewById(R.id.placeTV);
        orgnizationIMGV     =       (NetworkImageView)findViewById(R.id.orgnizationIMGV);
        descTV              =       (WebView)findViewById(R.id.descTV);
        descTV.setVerticalScrollBarEnabled(true);
        descTV.setBackgroundColor(this.getResources().getColor(R.color.transparent));
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
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
        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

        loadOrgDetails();
    }

    private void loadOrgDetails() {
        imageLoader     = VolleySingleton.getInstance().getImageLoader();
        String orglist = PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.OrganizationList,"");
        Gson gson       =   new Gson();
        Organization orgnization       =   gson.fromJson(orglist,Organization.class);
        list            =   orgnization.getOraganizationlist();
        if(list.size()>0){
            for (int i=0;i<list.size();i++){
                if(Constants.OrganizationId == i){
                    titleTV.setText(list.get(i).getTitle());
                    placeTV.setText(list.get(i).getPlace());

                    try{
                        byte pretext[] = list.get(i).getDescription().toString().getBytes("ISO-8859-1");
                        String value = new String(pretext,"UTF-8");
                        WebSettings settings = descTV.getSettings();
                        settings.setDefaultTextEncodingName("utf-8");
                        descTV.loadData(value, "text/html; charset=utf-8", null);

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    orgnizationIMGV.setImageUrl(Constants.ImageBaseUrl+list.get(i).getImage().toString().replace(" ","%20"),imageLoader);
                }
            }
        }
    }
}
