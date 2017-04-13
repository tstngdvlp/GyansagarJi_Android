package com.gyansagarji.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Spanned;
import android.util.Log;
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
import com.gyansagarji.android.Adapter.EventsAdapter;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.Event;
import com.gyansagarji.android.utils.Constants;
import com.gyansagarji.android.utils.HtmlConverter;
import com.gyansagarji.android.utils.PrefUtils;
import com.gyansagarji.android.utils.VolleySingleton;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by New android on 23-02-2017.
 */

public class EventDetailView extends Activity {
    TextView titleTV;
    ImageView backIMGV;
    ImageView reminderIMGV;
    ArrayList<Event.EventList> list;
    NetworkImageView eventIMGV;
    RequestQueue requestQueue;
    ImageLoader imageLoader;
    TextView dateTV;
    TextView timeTV;
    WebView descTV;
    String title,time,date;
    LinearLayout backLL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventsdetailview);

        backIMGV            =       (ImageView)findViewById(R.id.backIMGV);
        titleTV             =       (TextView)findViewById(R.id.titleTV);
        reminderIMGV        =       (ImageView)findViewById(R.id.reminderIMGV);
        backLL              =       (LinearLayout)findViewById(R.id.backLL);

        eventIMGV                   =       (NetworkImageView)findViewById(R.id.eventIMGV);
        dateTV                      =       (TextView)findViewById(R.id.dateTV);
        descTV                      =       (WebView) findViewById(R.id.descTV);
        timeTV                      =       (TextView)findViewById(R.id.timeTV);

        descTV.setVerticalScrollBarEnabled(true);
        descTV.setBackgroundColor(this.getResources().getColor(R.color.transparent));
        reminderIMGV.setVisibility(View.VISIBLE);
        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });

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


        reminderIMGV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                String mdate[] = date.split("-");
                int date    =  Integer.parseInt(mdate[0]);
                int mmonth  =  Integer.parseInt(mdate[1]);
                int  myear   =  Integer.parseInt(mdate[2]);
                String mtime[] = time.split(":");
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
                intent.putExtra("title",title);
                startActivity(intent);
            }
        });


        loadEventData();
    }

    private void loadEventData() {

        String eventslist = PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.EventsList,"");
        Gson gson   =   new Gson();

        Event event        =   gson.fromJson(eventslist,Event.class);
        list             =      event.getEventlist();
        if(list.size()>0){
            for (int i=0;i<list.size();i++){
                if(Constants.EventId == i){
                    titleTV.setText(list.get(i).getTitle());
                    dateTV.setText(list.get(i).getDate());
                    timeTV.setText(list.get(i).getTime1());
                    title = list.get(i).getTitle().toString();
                    date = list.get(i).getDate();
                    time = list.get(i).getTime();


                    try{
                        byte pretext[] = list.get(i).getDescription().toString().getBytes("ISO-8859-1");

                        String value = new String(pretext,"UTF-8");
                        WebSettings settings = descTV.getSettings();
                        settings.setDefaultTextEncodingName("utf-8");
                        descTV.loadData(value, "text/html; charset=utf-8", null);

                    }catch (Exception e){
                        e.printStackTrace();
                    }


                    eventIMGV.setImageUrl(Constants.ImageBaseUrl+list.get(i).getImage().toString().replace(" ","%20"),imageLoader);
                }
            }
        }

    }
}
