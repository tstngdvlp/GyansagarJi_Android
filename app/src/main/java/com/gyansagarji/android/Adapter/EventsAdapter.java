package com.gyansagarji.android.Adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.gyansagarji.android.Activity.EventsActivity;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.Event;
import com.gyansagarji.android.utils.CircularNetworkImageView2;
import com.gyansagarji.android.utils.Constants;
import com.gyansagarji.android.utils.VolleySingleton;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by New android on 23-03-2017.
 */

public class EventsAdapter extends BaseAdapter {
    EventsActivity activity;
    ArrayList<Event.EventList> data;
    RequestQueue requestQueue;
    ImageLoader imageLoader;
    String downloadurl;

    public EventsAdapter(EventsActivity activity, ArrayList<Event.EventList> data) {
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

        if (view == null) {
            view = activity.getLayoutInflater().inflate(R.layout.events_row, null);
            holder.titleTV = (TextView) view.findViewById(R.id.titleTV);
            holder.descTV = (TextView) view.findViewById(R.id.descTV);
            holder.dateTV = (TextView) view.findViewById(R.id.dateTV);
            holder.timeTV = (TextView) view.findViewById(R.id.timeTV);
            holder.reminder_btnTV = (TextView)view.findViewById(R.id.reminder_btnTV);
            holder.eventIMGV = (CircularNetworkImageView2) view.findViewById(R.id.eventIMGV);
            holder.clickLL = (LinearLayout) view.findViewById(R.id.clickLL);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.titleTV.setText(data.get(position).getTitle());
        holder.descTV.setText(data.get(position).getShort_desc());



        holder.dateTV.setText(data.get(position).getDate());
        holder.timeTV.setText(data.get(position).getTime1());

            holder.eventIMGV.setDefaultImageResId(R.drawable.loadingsmall);
            holder.eventIMGV.setErrorImageResId(R.drawable.ic_error);
            holder.eventIMGV.setImageUrl(Constants.ImageBaseUrl + data.get(position).getImage_icon().toString().replace(" ","%20"), imageLoader);





        holder.reminder_btnTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                String mdate[] = data.get(position).getDate().split("-");
                int date    =  Integer.parseInt(mdate[0]);
                int mmonth  =  Integer.parseInt(mdate[1]);
                int  myear   =  Integer.parseInt(mdate[2]);
                String mtime[] = data.get(position).getTime().split(":");
                int hrs         = Integer.parseInt(mtime[0]);
                int min         = Integer.parseInt(mtime[1]);
                int sec         = Integer.parseInt(mtime[2]);

                cal.set(myear,mmonth-1,date,hrs,min,sec);
                Intent intent = new Intent(Intent.ACTION_EDIT);
                intent.setType("vnd.android.cursor.item/event");
                intent.putExtra("beginTime", cal.getTimeInMillis());
                intent.putExtra("allDay", false);
                //intent.putExtra("rrule", "FREQ=DAILY");
                intent.putExtra("endTime", cal.getTimeInMillis()+60*1000);
                intent.putExtra("title", data.get(position).getTitle().toString());

                activity.startActivity(intent);
            }
        });

        return view;
    }

    public class ViewHolder {
        TextView titleTV;
        TextView descTV;
        TextView dateTV;
        TextView timeTV;
        TextView reminder_btnTV;
        LinearLayout clickLL;
        CircularNetworkImageView2 eventIMGV;
    }

    private class ProgressBack extends AsyncTask<String, String, String> {

        ProgressDialog PD;

        @Override
        protected void onPreExecute() {
            PD = ProgressDialog.show(activity, null, "Dowloading...", true);
            PD.setCancelable(true);
        }

        @Override
        protected String doInBackground(String... arg0) {
            Log.d("filename", arg0[0].substring(arg0[0].lastIndexOf('/')) + ",,,");
            downloadFile(arg0[0], arg0[0].substring(arg0[0].lastIndexOf('/')), PD);

            return null;
        }

        protected void onPostExecute(Boolean result) {
            PD.dismiss();

        }
    }

    private void downloadFile(String fileURL, String fileName, ProgressDialog PD) {
        try {
            String rootDir = Environment.getExternalStorageDirectory()
                    + File.separator + "GyanSagarJi" + File.separator + "Images";
            File rootFile = new File(rootDir);
            int count;
            URL url = new URL(fileURL);
            URLConnection conexion = url.openConnection();
            conexion.connect();

            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }
            String outputPath = rootFile.getPath() + "/" + fileName;

            BufferedInputStream input = new BufferedInputStream(url.openStream());
            FileOutputStream output = new FileOutputStream(outputPath);

            byte data[] = new byte[1024];

            while ((count = input.read(data, 0, 1024)) != -1) {
                output.write(data, 0, count);

            }
            output.flush();
            output.close();
            input.close();
            PD.dismiss();
        } catch (Exception e) {
            PD.dismiss();
            e.printStackTrace();
            String err = e.toString();
        }
    }
}