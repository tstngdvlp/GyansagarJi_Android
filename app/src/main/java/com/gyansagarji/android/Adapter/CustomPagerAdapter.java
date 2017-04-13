package com.gyansagarji.android.Adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.gyansagarji.android.Pojo.SubGallery;
import com.gyansagarji.android.R;
import com.gyansagarji.android.utils.Constants;
import com.gyansagarji.android.utils.Progressdialog;
import com.gyansagarji.android.utils.VolleySingleton;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by New android on 27-03-2017.
 */

public class CustomPagerAdapter extends PagerAdapter {

    Activity mContext;
    LayoutInflater mLayoutInflater;
    SubGallery data;
    RequestQueue requestQueue;
    ImageLoader imageLoader;
    String downloadurl;
    public CustomPagerAdapter(Activity context, SubGallery data) {
        mContext = context;
        this.data=data;
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

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.getSubGallery().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

            return view == ((RelativeLayout) object);


    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView downloadIMGV    =       (ImageView)itemView.findViewById(R.id.downloadIMGV);
        ImageView backIMGV        =       (ImageView)itemView.findViewById(R.id.backIMGV);
        TextView titleTV          =       (TextView)itemView.findViewById(R.id.titleTV);

        if(data.getSubGallery().get(position).getImage_name()!=null) {
            titleTV.setText(data.getSubGallery().get(position).getImage_name());
        }
        NetworkImageView galleryIMGV = (NetworkImageView) itemView.findViewById(R.id.imageView);
        galleryIMGV.setDefaultImageResId(R.drawable.loadingbig);
        galleryIMGV.setErrorImageResId(R.drawable.ic_error);
        galleryIMGV.setAdjustViewBounds(true);
        galleryIMGV.setImageUrl(Constants.ImageBaseUrl+data.getSubGallery().get(position).getImage().toString().replace(" ","%20"),imageLoader);
        WebView    description =(WebView) itemView.findViewById(R.id.description);
        description.setBackgroundColor(mContext.getResources().getColor(R.color.transparent));
        try{
            byte pretext[] = data.getSubGallery().get(position).getDes().toString().getBytes("ISO-8859-1");

            String value = new String(pretext,"UTF-8");
            WebSettings settings = description.getSettings();
            settings.setDefaultTextEncodingName("utf-8");
            description.loadData(value, "text/html; charset=utf-8", null);


        }catch (Exception e){
            e.printStackTrace();
        }


        downloadIMGV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    String imgurl[] =  data.getSubGallery().get(position).getImage().split("\\?");
                    downloadurl       = Constants.ImageBaseUrl+ imgurl[0];
                    String fileName = downloadurl.substring(downloadurl.lastIndexOf("/"));
                    File file = new File(Environment.getExternalStorageDirectory(),"GyanSagarJi"+File.separator+"Gallery/"+fileName);
                    if(!file.exists()) {
                        ProgressBack PB = new ProgressBack();
                        PB.execute(downloadurl.toString().replace(" ","%20"));
                    }else{
                        Toast.makeText(mContext,"File Alredy Downloaded",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    ActivityCompat.requestPermissions(mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 104);
                }
            }
        });

        backIMGV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mContext.finish();
            }
        });
        String imgurl[] =  data.getSubGallery().get(position).getImage().split("\\?");
        downloadurl       = Constants.ImageBaseUrl+ imgurl[0];
        String fileName = downloadurl.substring(downloadurl.lastIndexOf("/"));
        File file = new File(Environment.getExternalStorageDirectory(),"GyanSagarJi"+File.separator+"Gallery/"+fileName);
        if(!file.exists()) {
            downloadIMGV.setVisibility(View.VISIBLE);
        }else{
            downloadIMGV.setVisibility(View.GONE);
        }

        container.addView(itemView);
        return itemView;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    private class ProgressBack extends AsyncTask<String, String, String> {

        Progressdialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog         =   new Progressdialog();
            progressDialog.dialog(mContext);
            progressDialog.showDialog();

        }

        @Override
        protected String doInBackground(String... arg0) {
            downloadFile(arg0[0], arg0[0].substring(arg0[0].lastIndexOf('/')),progressDialog);

            return null;
        }

        protected void onPostExecute(Boolean result) {


        }
    }

    private void downloadFile(String fileURL, String fileName, Progressdialog progressDialog) {
        try {
            String rootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "GyanSagarJi"+File.separator+"Gallery";
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
                mContext.sendBroadcast(scanIntent);
            } else {
                final Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageDirectory()));
                mContext.sendBroadcast(intent);
            }
            output.flush();
            output.close();
            input.close();
            progressDialog.closeDialog();
            mContext.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext,"File Downloaded Successfully",Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            progressDialog.closeDialog();
            e.printStackTrace();
            String err = e.toString();
        }
    }
}