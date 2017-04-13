package com.gyansagarji.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.gyansagarji.android.Adapter.GridAdapter;
import com.gyansagarji.android.Pojo.SubGallery;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.Gallery;
import com.gyansagarji.android.Response.Organization;
import com.gyansagarji.android.Rest.ApiClient;
import com.gyansagarji.android.Rest.ApiInterface;
import com.gyansagarji.android.utils.Constants;
import com.gyansagarji.android.utils.PrefUtils;
import com.gyansagarji.android.utils.Progressdialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by New android on 20-02-2017.
 */

public class GalleryActivity extends Activity {

    ViewPager mviewpager;
    GridView gridView;
    ArrayList<Integer> imglist;
    GridAdapter gridAdapter;
    TextView titleTV;
    ImageView backIMGV;
    LinearLayout backLL;
    public static int width;
    public static int height;
    Progressdialog progressDialog;
    private ApiInterface apiServices;
    Gallery     gallery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        gridView                    =       (GridView)findViewById(R.id.gridView);

        backIMGV                    =       (ImageView)findViewById(R.id.backIMGV);
        backLL                      =       (LinearLayout)findViewById(R.id.backLL);
        titleTV                     =       (TextView)findViewById(R.id.titleTV);

        titleTV.setText(getResources().getString(R.string.gallery));

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        loadInitials();

        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GalleryActivity.this,MainActivity.class));
                finish();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SubGallery subGallery = new SubGallery();
                if (gallery.getSubInfos().get(i).getSunInfo() != null) {
                    if (gallery.getSubInfos().get(i).getSunInfo().size() > 0) {
                        Constants.AlbumName = gallery.getSubInfos().get(i).getAlbum_name();
                        subGallery.setSubGallery(gallery.getSubInfos().get(i).getSunInfo());
                        String subGalleryObject = new Gson().toJson(subGallery);
                        PrefUtils.saveToPrefs(getApplicationContext(), PrefUtils.SubGalleryList, subGalleryObject);
                        startActivity(new Intent(getApplicationContext(), GallerySubActivity.class));
                    }
                }
            }
        });

    }



    private void loadInitials() {

        progressDialog          =   new Progressdialog();
        progressDialog.dialog(this);
        progressDialog.showDialog();
        apiServices         = ApiClient.getClient().create(ApiInterface.class);
        Call<Gallery> call = apiServices.getGalleryImages();

        call.enqueue(new Callback<Gallery>() {
            @Override
            public void onResponse(Call<Gallery> call, Response<Gallery> response) {
                progressDialog.closeDialog();


                if(response.code() == 200){
                    if(response.body().getStatus().equals("1")){
                        Gson gson   =   new Gson();
                        String orglist = gson.toJson(response.body());
                        gallery =response.body();
                        gridAdapter         =       new GridAdapter(GalleryActivity.this,response.body());
                        gridView.setAdapter(gridAdapter);
                        PrefUtils.saveToPrefs(getApplicationContext(),PrefUtils.GalleryList,orglist);

                    }
                }
            }

            @Override
            public void onFailure(Call<Gallery> call, Throwable t) {

                progressDialog.closeDialog();
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(GalleryActivity.this,MainActivity.class));
        finish();
    }
}
