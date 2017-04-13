package com.gyansagarji.android.Activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.gson.Gson;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.MediaAV;
import com.gyansagarji.android.utils.Constants;
import com.gyansagarji.android.utils.PrefUtils;
import com.gyansagarji.android.utils.Progressdialog;

import java.util.ArrayList;

/**
 * Created by New android on 24-03-2017.
 */

public class VideoPlayActivity extends Activity {
    ArrayList<MediaAV.VideoList> list;
    VideoView videoView1;
    Progressdialog progressdialog;
    TextView titleTV;
    ImageView backIMGV;
    LinearLayout backLL;
    String downloadurl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videoplay);
        videoView1      =       (VideoView)findViewById(R.id.videoView1);
        backIMGV            =       (ImageView)findViewById(R.id.backIMGV);
        titleTV             =       (TextView)findViewById(R.id.titleTV);
        backLL              =       (LinearLayout)findViewById(R.id.backLL);
        backLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loadInitials();

    }

    private void loadInitials() {
        String video = PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.MediaList,"");

        Gson gson = new Gson();
        MediaAV media = gson.fromJson(video,MediaAV.class);
        list    =   media.getVideolist();


        if(Constants.VideoLink != "" && Constants.VideoLink !=null){
                    titleTV.setText(Constants.VideoTitle);
                    if(Constants.VideoType == "web") {
                      playvideo(Constants.VideoLink.toString().replace(" ","%20"));

                    }else if(Constants.VideoType == "offline"){
                        playOfflineVideo(Constants.VideoLink);
                    }

        }
    }

    private void playOfflineVideo(String videoLink) {

        final MediaController mediaController = new MediaController(this);

        Uri video = Uri.parse(videoLink);
        try {
            videoView1.setMediaController(mediaController);
            videoView1.setVideoURI(video);

            videoView1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    videoView1.start();

                }
            });
        }catch (Exception e){
            e.printStackTrace();

        }


    }


    private void playvideo(String url) {

        progressdialog      =   new Progressdialog();
        progressdialog.dialog(this);
        progressdialog.showDialog();
        MediaController mediaController = new MediaController(this);
        Uri video = Uri.parse(url);
        videoView1.setMediaController(mediaController);
        videoView1.setVideoURI(video);

        videoView1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoView1.start();
                progressdialog.closeDialog();
            }
        });




    }
}
