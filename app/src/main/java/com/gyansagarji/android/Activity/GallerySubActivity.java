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
import com.gyansagarji.android.Adapter.SubGridAdapter;
import com.gyansagarji.android.Pojo.SubGallery;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.Gallery;
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

public class GallerySubActivity extends Activity {

    ViewPager mviewpager;
    GridView gridView;
    ArrayList<Integer> imglist;
    SubGridAdapter gridAdapter;
    TextView titleTV;
    ImageView backIMGV;
    LinearLayout backLL;
    static int width;
    static int height;
    Progressdialog progressDialog;
    private ApiInterface apiServices;
    SubGallery     gallery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        gallery=new Gson().fromJson(PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.SubGalleryList,""), SubGallery.class);
        gridView                    =       (GridView)findViewById(R.id.gridView);

        backIMGV                    =       (ImageView)findViewById(R.id.backIMGV);
        titleTV                     =       (TextView)findViewById(R.id.titleTV);
        backLL                      =       (LinearLayout)findViewById(R.id.backLL);
        gridAdapter     =   new SubGridAdapter(GallerySubActivity.this,gallery);
        titleTV.setText(Constants.AlbumName);
        gridView.setAdapter(gridAdapter);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SubGallery subGallery=new SubGallery();
                if(gallery.getSubGallery().get(i).getSunInfo()!=null) {
                    if(gallery.getSubGallery().get(i).getAlbum_name()!=null){

                        Constants.AlbumName = gallery.getSubGallery().get(i).getAlbum_name();
                    }else if(gallery.getSubGallery().get(i).getImage_name()!=null){

                        Constants.AlbumName = gallery.getSubGallery().get(i).getImage_name();
                    }

                    subGallery.setSubGallery(gallery.getSubGallery().get(i).getSunInfo());
                    String subGalleryObject = new Gson().toJson(subGallery);
                    PrefUtils.saveToPrefs(getApplicationContext(), PrefUtils.SubGalleryList, subGalleryObject);
                    startActivity(new Intent(getApplicationContext(), GallerySubActivity.class));

                }else if(gallery.getSubGallery().get(i).getSunInfo()==null){
                    if(gallery.getSubGallery().size()>=0){

                        SubGallery Galleryimages=new SubGallery();
                        Galleryimages.setSubGallery(gallery.getSubGallery());
                        String imagesobj = new Gson().toJson(Galleryimages);
                        Constants.GalleryCurrentImageId = i;
                        PrefUtils.saveToPrefs(getApplicationContext(),PrefUtils.GalleryImagesList,imagesobj);
                        startActivity(new Intent(getApplicationContext(),GalleryImageView.class));
                    }


                }
            }
        });

    }


}
