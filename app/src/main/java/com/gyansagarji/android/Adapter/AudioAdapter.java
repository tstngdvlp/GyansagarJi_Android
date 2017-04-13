package com.gyansagarji.android.Adapter;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.gyansagarji.android.Activity.AudioPlayActivity;
import com.gyansagarji.android.Activity.MediaActivity;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.MediaAV;
import com.gyansagarji.android.utils.CircularNetworkImageView2;
import com.gyansagarji.android.utils.Constants;
import com.gyansagarji.android.utils.NetworkConnectivity;
import com.gyansagarji.android.utils.NetworkError;
import com.gyansagarji.android.utils.Progressdialogs;
import com.gyansagarji.android.utils.VolleySingleton;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by New android on 24-03-2017.
 */

public class AudioAdapter extends BaseAdapter {
    MediaActivity activity;
    ArrayList<MediaAV.AudioList> data;
    RequestQueue requestQueue;
    ImageLoader imageLoader;
    String downloadurl;
    public ArrayList<Integer> filelength=new ArrayList<>();

    private int progress = 0;
    boolean downloading = false;
    public AudioAdapter(final MediaActivity activity, ArrayList<MediaAV.AudioList> data) {
        this.activity = activity;
        this.data = data;
        requestQueue = VolleySingleton.getInstance().getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {

            @Override
            public Bitmap getBitmap(String url) {
                return activity.mcache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                activity.mcache.put(url,bitmap);
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
    try {
        ViewHolder holder = new ViewHolder();
        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.media_row, null);
            holder.listIMGV = (CircularNetworkImageView2) view.findViewById(R.id.listIMGV);
            holder.mediaTV = (TextView) view.findViewById(R.id.mediaTV);
            holder.playbuttonIMGV = (ImageView) view.findViewById(R.id.playbuttonIMGV);
            holder.downloadIMGV = (ImageView) view.findViewById(R.id.downloadIMGV);
            holder.donut_progress = (DonutProgress) view.findViewById(R.id.donut_progress);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }


        holder.listIMGV.setDefaultImageResId(R.drawable.loadingsmall);
        holder.listIMGV.setErrorImageResId(R.drawable.ic_error);
        holder.listIMGV.setImageUrl(Constants.ImageBaseUrl + data.get(position).getImage().toString().replace(" ", "%20"), imageLoader);
        holder.mediaTV.setText(data.get(position).getTitle());

        holder.playbuttonIMGV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String audiourl[] = data.get(position).getAudio().split("\\?");
                downloadurl = Constants.ImageBaseUrl + audiourl[0];
                String fileName = downloadurl.substring(downloadurl.lastIndexOf("/"));
                File file = new File(Environment.getExternalStorageDirectory(), "GyanSagarJi" + File.separator + "Music/" + fileName);
                if (!file.exists()) {
                    if (NetworkConnectivity.isNetworkAvailable(activity)) {
                        Constants.AudioType = "web";
                        Constants.AudioLink = Constants.ImageBaseUrl + data.get(position).getAudio();
                        Constants.AudioTitle = data.get(position).getTitle();
                        Progressdialogs.getInstance().getDialog(activity);
                        Progressdialogs.getInstance().showDialog();
                        activity.startActivity(new Intent(activity, AudioPlayActivity.class));

                    } else {
                        NetworkError.networkConnectionError(activity);
                    }
                } else {
                    if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Constants.AudioType = "offline";
                        Constants.AudioLink = file.getPath();
                        Constants.AudioTitle = data.get(position).getTitle();
                        activity.startActivity(new Intent(activity, AudioPlayActivity.class));
                    } else {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 106);
                    }
                }



            }
        });

        final ViewHolder finalHolder = holder;
        final ViewHolder finalHolder1 = holder;
        holder.downloadIMGV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    String audiourl[] = data.get(position).getAudio().split("\\?");
                    downloadurl = Constants.ImageBaseUrl + audiourl[0];
                    String fileName = downloadurl.substring(downloadurl.lastIndexOf("/"));
                    File file = new File(Environment.getExternalStorageDirectory(), "GyanSagarJi" + File.separator + "Music/" + fileName);
                    if (!file.exists()) {
                        if (NetworkConnectivity.isNetworkAvailable(activity)) {

                            finalHolder1.downloadIMGV.setVisibility(View.GONE);
                            downloading = true;
                            ProgressBack PB = new ProgressBack(finalHolder.donut_progress, downloadurl);
                            PB.execute(downloadurl.toString().replace(" ", "%20"));


                        } else {
                            NetworkError.networkConnectionError(activity);
                        }
                    }

                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 104);
                }
            }
        });


        String audiourl[] = data.get(position).getAudio().split("\\?");
        downloadurl = Constants.ImageBaseUrl + audiourl[0];
        String fileName = downloadurl.substring(downloadurl.lastIndexOf("/"));
        File file = new File(Environment.getExternalStorageDirectory(), "GyanSagarJi" + File.separator + "Music/" + fileName);
        if (file.exists()) {
            holder.downloadIMGV.setVisibility(View.GONE);
        } else {
            holder.downloadIMGV.setImageResource(R.drawable.download);
        }
    }catch (Exception e){
        e.printStackTrace();
    }
        return view;
    }

    public class ViewHolder{
        CircularNetworkImageView2 listIMGV;
        TextView mediaTV;
        ImageView playbuttonIMGV;
        ImageView downloadIMGV;
        DonutProgress donut_progress;
    }


    private class ProgressBack extends AsyncTask<String, String, String> {
        DonutProgress donutProgress;
        public ProgressBack(DonutProgress donut_progress, final String fileURL) {
            this.donutProgress = donut_progress;
            donutProgress.setVisibility(View.VISIBLE);
            donutProgress.setMax(100);

            }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... arg0) {
            try {
                URL url = new URL(arg0[0]);
                URLConnection connexion = url.openConnection();
                connexion.connect();
                filelength.add(connexion.getContentLength());
            }catch (Exception e){
                e.printStackTrace();
            }
            downloadFile(arg0[0], arg0[0].substring(arg0[0].lastIndexOf('/')),donutProgress,filelength.get(0));

            return null;
        }

        protected void onPostExecute(Boolean result) {

        }
    }

    private void downloadFile(String fileURL, String fileName, final DonutProgress donutProgress, final int fileLengths) {
        try {
            String rootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "GyanSagarJi"+File.separator+"Music";
            File rootFile = new File(rootDir);

            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }
            fileName=fileName.replace("%20"," ");

            String outputPath=rootFile.getPath()+"/"+fileName;
            URL url= new URL(fileURL);
            int count=0;
            BufferedInputStream input = new BufferedInputStream(url.openStream());
            FileOutputStream output = new FileOutputStream(outputPath);

            byte data[] = new byte[1024];
            long total = 0;

            while ((count = input.read(data, 0, 1024)) != -1) {
                total += count;
                final long finalTotal = total;
                activity.runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         int progerss = (int) (finalTotal * 100 / fileLengths);
                         donutProgress.setDonut_progress(String.valueOf(progerss));
                         donutProgress.setProgress(progerss);
                     }
                 });
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
            downloading = false;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,"File Downloaded Successfully",Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    donutProgress.setVisibility(View.GONE);
                    filelength.remove(0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            String err = e.toString();
        }
    }


}
