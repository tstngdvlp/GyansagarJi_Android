package com.gyansagarji.android.Activity;

import android.app.Activity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.MediaAV;
import com.gyansagarji.android.utils.Constants;
import com.gyansagarji.android.utils.PrefUtils;
import com.gyansagarji.android.utils.Progressdialogs;
import com.gyansagarji.android.utils.VolleySingleton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class AudioPlayActivity extends Activity {

	private MediaPlayer mediaPlayer;
	public TextView songName, duration;
	private double timeElapsed = 0, finalTime = 0;
	private int forwardTime = 2000, backwardTime = 2000;
	private Handler durationHandler = new Handler();
	private SeekBar seekbar;
	ArrayList<MediaAV.AudioList> list;
	Uri url;
	NetworkImageView mp3Image;
	ImageLoader imageLoader;
	ImageView media_status;
	private boolean manualPause =false;
	ImageView backIMGV;
	TextView titleTV;
	LinearLayout backLL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

			setContentView(R.layout.audioplayer);
			Progressdialogs.getInstance().closeDialog();

			duration 				= 			(TextView) findViewById(R.id.songDuration);
			seekbar 				= 			(SeekBar) findViewById(R.id.seekBar);
			backIMGV 				= 			(ImageView) findViewById(R.id.backIMGV);
			titleTV 				= 			(TextView) findViewById(R.id.titleTV);
			backLL 					= 			(LinearLayout) findViewById(R.id.backLL);
			media_status			=			(ImageView)    findViewById(R.id.media_status);
			backLL.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					finish();
				}
			});
		try {
			initializeViews();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void initializeViews() throws IOException{
		imageLoader     = VolleySingleton.getInstance().getImageLoader();
		String video = PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.MediaList,"");

		media_status.setImageResource(R.drawable.ic_play);
		Gson gson = new Gson();
		MediaAV media = gson.fromJson(video,MediaAV.class);


		list    =   media.getAudiolist();


				if(Constants.AudioLink!=null && Constants.AudioLink!=""){
					 url = Uri.parse(Constants.AudioLink.toString().replace(" ","%20"));
					titleTV.setText(Constants.AudioTitle);
/*					mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mediaPlayer.setDataSource(String.valueOf(url));
					mediaPlayer.prepareAsync();*/
				}

		mediaPlayer = MediaPlayer.create(AudioPlayActivity.this,url);

		if(mediaPlayer!=null) {
			try {
				finalTime = mediaPlayer.getDuration();


				mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer mediaPlayer) {
						mediaPlayer.start();
					}
				});

			}catch (Exception e){
				e.printStackTrace();
			}
		}


		seekbar.setMax((int) finalTime);
		seekbar.setClickable(false);

		if(mediaPlayer.isPlaying()){
			media_status.setImageResource(R.drawable.ic_play);
			pause();
		}else{
			media_status.setImageResource(android.R.drawable.ic_media_pause);
			play();
		}

		media_status.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(mediaPlayer.isPlaying()){
					media_status.setImageResource(R.drawable.ic_play);

					pause();

				}else{
					media_status.setImageResource(android.R.drawable.ic_media_pause);

					play();
				}
			}
		});

		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
				if(b) {
					mediaPlayer.seekTo(i);
				}
			}
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});



	}

	protected void onResume() {
		super.onResume();
		if(!manualPause) {
			try {
				if(mediaPlayer!=null) {
					if (mediaPlayer.getCurrentPosition() != 0) {
						play();
					}
				}
			}catch (Exception e){
				e.printStackTrace();
			}

		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		try{
			systemPause();

		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void systemPause() {
		mediaPlayer.pause();
	}

	public void play() {
		manualPause =false;
		mediaPlayer.start();
		finalTime=mediaPlayer.getDuration();
		seekbar.setMax((int) finalTime);
		timeElapsed = mediaPlayer.getCurrentPosition();
		seekbar.setProgress((int) timeElapsed);
		durationHandler.postDelayed(updateSeekBarTime, 100);
	}


	private Runnable updateSeekBarTime = new Runnable() {
		public void run() {

			timeElapsed = mediaPlayer.getCurrentPosition();

			seekbar.setProgress((int) timeElapsed);

			double timeRemaining = finalTime - timeElapsed;
			if(timeRemaining<=0){

				media_status.setImageResource(R.drawable.ic_play);
			}
			//set time remaing
			//double timeRemaining = finalTime - timeElapsed;
			duration.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));

			//repeat yourself that again in 100 miliseconds
			durationHandler.postDelayed(this, 100);
		}
	};

	// pause mp3 song
	public void pause() {
		manualPause =true;
		mediaPlayer.pause();
	}

	// go forward at forwardTime seconds
	public void forward(View view) {
		//check if we can go forward at forwardTime seconds before song endes
		if ((timeElapsed + forwardTime) <= finalTime) {
			timeElapsed = timeElapsed + forwardTime;

			//seek to the exact second of the track
			mediaPlayer.seekTo((int) timeElapsed);
		}
	}

	// go backwards at backwardTime seconds
	public void rewind(View view) {
		//check if we can go back at backwardTime seconds after song starts
		if ((timeElapsed - backwardTime) > 0) {
			timeElapsed = timeElapsed - backwardTime;

			//seek to the exact second of the track
			mediaPlayer.seekTo((int) timeElapsed);
		}
	}
}