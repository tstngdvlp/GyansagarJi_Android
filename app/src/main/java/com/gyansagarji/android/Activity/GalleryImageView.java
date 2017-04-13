package com.gyansagarji.android.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyansagarji.android.Adapter.CustomPagerAdapter;
import com.gyansagarji.android.Adapter.GalleryImageViewAdapter;
import com.gyansagarji.android.Pojo.SubGallery;
import com.gyansagarji.android.R;
import com.gyansagarji.android.utils.Constants;
import com.gyansagarji.android.utils.PrefUtils;

/**
 * Created by New android on 27-03-2017.
 */

public class GalleryImageView extends Activity {
    ViewPager viewPager;
    CustomPagerAdapter imageViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.galleryimageviewpager);
        viewPager       =       (ViewPager)findViewById(R.id.viewpager);

        loadImages();


    }

    private void loadImages() {
        String images = PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.GalleryImagesList,"");

        Gson gson = new Gson();

        SubGallery subGallery = gson.fromJson(images,SubGallery.class);

        imageViewAdapter        =       new  CustomPagerAdapter(this,subGallery);
        viewPager.setAdapter(imageViewAdapter);
        viewPager.setCurrentItem(Constants.GalleryCurrentImageId);
    }
}
