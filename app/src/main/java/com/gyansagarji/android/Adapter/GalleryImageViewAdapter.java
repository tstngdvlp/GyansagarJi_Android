package com.gyansagarji.android.Adapter;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.gyansagarji.android.Activity.GalleryImageView;
import com.gyansagarji.android.Pojo.SubGallery;
import com.gyansagarji.android.R;
import com.gyansagarji.android.utils.Constants;
import com.gyansagarji.android.utils.VolleySingleton;

/**
 * Created by New android on 27-03-2017.
 */

public class GalleryImageViewAdapter extends PagerAdapter {
    GalleryImageView activity;
    SubGallery data;
    RequestQueue requestQueue;
    ImageLoader imageLoader;
    View view;
    NetworkImageView galleryIMGV;
    public GalleryImageViewAdapter(GalleryImageView activity, SubGallery data) {
        this.activity = activity;
        this.data = data;
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
    }

    @Override
    public int getCount() {
        return data.getSubGallery().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        ViewHolder holder = new ViewHolder();

        if(view == null){
            view =  activity.getLayoutInflater().inflate(R.layout.galleyfullimage,null);
            holder.galleryIMGV  =   (NetworkImageView)view.findViewById(R.id.galleryIMGV);
            view.setTag(holder);
        }else{
            holder      =   (ViewHolder)view.getTag();
        }
        holder.galleryIMGV.setDefaultImageResId(R.drawable.loadingbig);
        holder.galleryIMGV.setErrorImageResId(R.drawable.ic_error);

        holder.galleryIMGV.setImageUrl(Constants.ImageBaseUrl+data.getSubGallery().get(position).getImage().toString().replace(" ","%20"),imageLoader);


        ((ViewPager) container).addView(view, 0);
        return view;
    }

    public class ViewHolder{
        NetworkImageView  galleryIMGV;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(((View)view));
    }
}
