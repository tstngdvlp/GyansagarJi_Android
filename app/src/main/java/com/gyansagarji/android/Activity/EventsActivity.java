package com.gyansagarji.android.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gyansagarji.android.Adapter.EventsAdapter;
import com.gyansagarji.android.Adapter.NewsAdapter;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.Event;
import com.gyansagarji.android.Response.News;
import com.gyansagarji.android.Rest.ApiClient;
import com.gyansagarji.android.Rest.ApiInterface;
import com.gyansagarji.android.utils.Constants;
import com.gyansagarji.android.utils.ListviewFullHeight;
import com.gyansagarji.android.utils.PrefUtils;
import com.gyansagarji.android.utils.Progressdialog;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by New android on 20-02-2017.
 */

public class EventsActivity extends Activity {
    TextView titleTV;
    ImageView backIMGV;
    LinearLayout backLL;
    LinearLayout eventLL;
    ApiInterface apiService;
    ArrayList<Event.EventList> list;
    EventsAdapter eventsAdapter;
    ListView eventsLV;
    Progressdialog progressdialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events);

        backIMGV            =       (ImageView)findViewById(R.id.backIMGV);
        titleTV             =       (TextView)findViewById(R.id.titleTV);
        eventLL             =       (LinearLayout)findViewById(R.id.eventLL);
        eventsLV            =       (ListView)findViewById(R.id.eventsLV);
        backLL              =       (LinearLayout)findViewById(R.id.backLL);

        titleTV.setText(getResources().getString(R.string.events));

        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EventsActivity.this,MainActivity.class));
                finish();
            }
        });

        eventsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(EventsActivity.this, EventDetailView.class));
                Constants.EventId = i;
            }
        });

        loadInitials();
    }

    private void loadInitials() {
        progressdialog      =       new Progressdialog();
        progressdialog.dialog(this);
        progressdialog.showDialog();
        apiService          =       ApiClient.getClient().create(ApiInterface.class);
        Call<Event> call    =       apiService.EventsList();
        call.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {

                if(response.body().getStatus().equals("1")) {
                progressdialog.closeDialog();
                if(response.code()==200) {

                        Gson gson   =   new Gson();
                        String eventslist = gson.toJson(response.body());

                        PrefUtils.saveToPrefs(getApplicationContext(),PrefUtils.EventsList,eventslist);
                        eventsData();
                    }
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                progressdialog.closeDialog();
            }
        });

    }

    private void eventsData() {
        String eventslist = PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.EventsList,"");
        Gson gson   =   new Gson();

        Event event        =   gson.fromJson(eventslist,Event.class);
        list             =      event.getEventlist();
        if(list.size()>0) {
            eventsAdapter = new EventsAdapter(this, list);
            eventsLV.setAdapter(eventsAdapter);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EventsActivity.this,MainActivity.class));
        finish();
    }
}
