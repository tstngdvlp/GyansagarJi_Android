package com.gyansagarji.android.Adapter;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.gyansagarji.android.Activity.NewsActivity;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.News;
import com.gyansagarji.android.utils.CircularNetworkImageView2;
import com.gyansagarji.android.utils.Constants;
import com.gyansagarji.android.utils.VolleySingleton;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by New android on 20-02-2017.
 */

public class NewsAdapter extends BaseAdapter {
    NewsActivity activity;
    ArrayList<News.NewsList> data;
    RequestQueue requestQueue;
    ImageLoader imageLoader;
    String downloadurl;
    public NewsAdapter(final NewsActivity activity, ArrayList<News.NewsList> data) {
        this.activity = activity;
        this.data = data;
        requestQueue            =       VolleySingleton.getInstance().getRequestQueue();
        imageLoader             =       new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
             return activity.mCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                activity.mCache.put(url,bitmap);
            }
        });
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
    ViewHolder holder = new ViewHolder();
        if(view==null){
            view                =       activity.getLayoutInflater().inflate(R.layout.news_row,null);
            holder.titleTV      =       (TextView)view.findViewById(R.id.titleTV);
            holder.descTV       =       (TextView) view.findViewById(R.id.descTV);
            holder.dateTV       =       (TextView)view.findViewById(R.id.dateTV);
            holder.clickLL      =       (LinearLayout)view.findViewById(R.id.clickLL);
            holder.newsLL      =       (LinearLayout)view.findViewById(R.id.newsLL);
            holder.listIMGV     =       (CircularNetworkImageView2) view.findViewById(R.id.listIMGV);
            holder.image     =       (NetworkImageView) view.findViewById(R.id.image);
            view.setTag(holder);
        }else{
            holder              =       (ViewHolder)view.getTag();
        }

        holder.titleTV.setText(data.get(position).getTitle());
       holder.descTV.setText(data.get(position).getShort_desc());
        holder.dateTV.setText(data.get(position).getDate());
        holder.listIMGV.setDefaultImageResId(R.drawable.loadingsmall);
        holder.listIMGV.setErrorImageResId(R.drawable.ic_error);
        holder.listIMGV.setImageUrl(Constants.ImageBaseUrl+data.get(position).getImage_icon().toString().replace(" ","%20"),imageLoader);

        holder.image.setImageUrl(Constants.ImageBaseUrl+data.get(position).getImage().toString().replace(" ","%20"),imageLoader);

        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            String imgurl[] = data.get(position).getImage().split("\\?");
            downloadurl       = Constants.ImageBaseUrl+ imgurl[0];

            String fileName = downloadurl.substring(downloadurl.lastIndexOf("/"));
            File file = new File(Environment.getExternalStorageDirectory(),"GyanSagarJi"+File.separator+"News/"+fileName);
            if(!file.exists()) {
                ProgressBack PB = new ProgressBack();
                PB.execute(downloadurl.toString().replace(" ","%20"));
            }
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 104);
        }

        return view;
    }

    public class ViewHolder{
        TextView titleTV;
        TextView descTV;
        TextView dateTV;
        LinearLayout newsLL;
        LinearLayout clickLL;
        CircularNetworkImageView2 listIMGV;
        NetworkImageView image;
        TextView dummyTV;
    }



    private class ProgressBack extends AsyncTask<String, String, String> {

        ProgressDialog PD;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... arg0) {
            downloadFile(arg0[0], arg0[0].substring(arg0[0].lastIndexOf('/')),PD);

            return null;
        }

        protected void onPostExecute(Boolean result) {


        }
    }

    private void downloadFile(String fileURL, String fileName,ProgressDialog PD) {
        try {
            String rootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "GyanSagarJi"+File.separator+"News";
            File rootFile = new File(rootDir);
            int count;
            URL url = new URL(fileURL);
            URLConnection conexion = url.openConnection();
            conexion.connect();

            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }
            fileName=fileName.replace("%20"," ");
            String outputPath=rootFile.getPath()+"/"+fileName;

            BufferedInputStream input = new BufferedInputStream(url.openStream());
            FileOutputStream output = new FileOutputStream(outputPath);

            byte data[] = new byte[1024];

            while ((count = input.read(data, 0, 1024)) != -1) {
                output.write(data, 0, count);

            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                final Uri contentUri = Uri.fromFile(new File(outputPath));
                scanIntent.setData(contentUri);
                activity.sendBroadcast(scanIntent);
            } else {
                final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                activity.sendBroadcast(intent);
            }


            output.flush();
            output.close();
            input.close();


        } catch (Exception e) {

            e.printStackTrace();
            String err = e.toString();
        }
    }
}
