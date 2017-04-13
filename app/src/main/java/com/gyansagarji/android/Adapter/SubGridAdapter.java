package com.gyansagarji.android.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.gyansagarji.android.Activity.GalleryActivity;
import com.gyansagarji.android.Pojo.SubGallery;
import com.gyansagarji.android.R;
import com.gyansagarji.android.utils.Constants;
import com.gyansagarji.android.utils.VolleySingleton;

/**
 * Created by New android on 21-02-2017.
 */

public class SubGridAdapter extends BaseAdapter {
    Activity activity;
    SubGallery data;
    RequestQueue requestQueue;
    ImageLoader imageLoader;

    public SubGridAdapter(Activity activity, SubGallery data) {
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
    public Object getItem(int position) {
        return data.getSubGallery().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        if(view==null){
            view                        =       activity.getLayoutInflater().inflate(R.layout.gridimage,null);
            holder.grid_item_image      =       (NetworkImageView)view.findViewById(R.id.grid_item_image);
            holder.imageRL              =       (RelativeLayout)view.findViewById(R.id.imageRL);
            holder.imgnameTV            =       (TextView)view.findViewById(R.id.imgnameTV);
            view.setTag(holder);
       }else{
            holder                      =       (ViewHolder)view.getTag();
        }

        if(data.getSubGallery().get(position).getAlbum_name()!=null){
            holder.imgnameTV.setText(data.getSubGallery().get(position).getAlbum_name().toString());
        }else if(data.getSubGallery().get(position).getImage_name()!=null){
            holder.imgnameTV.setText(data.getSubGallery().get(position).getImage_name().toString());
        }

        LinearLayout.LayoutParams     layoutParams = new LinearLayout.LayoutParams(GalleryActivity.width/2-5,GalleryActivity.height/3-5);
        holder.imageRL.setLayoutParams(layoutParams);
        holder.grid_item_image.setDefaultImageResId(R.drawable.loadingbig);
        holder.grid_item_image.setErrorImageResId(R.drawable.ic_error);
        holder.grid_item_image.setImageUrl(Constants.ImageBaseUrl+data.getSubGallery().get(position).getTiny_image().toString().replace(" ","%20"),imageLoader);

        return view;
    }

    public class ViewHolder{
        NetworkImageView grid_item_image;
        RelativeLayout imageRL;
        TextView imgnameTV;
    }
}
