package com.gyansagarji.android.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import com.gyansagarji.android.Activity.MediaActivity;
import com.gyansagarji.android.Adapter.AudioAdapter;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.MediaAV;
import com.gyansagarji.android.Rest.ApiInterface;
import com.gyansagarji.android.utils.Progressdialog;

import java.util.ArrayList;


/**
 * Created by New android on 21-02-2017.
 */

public class AudioActivity extends Fragment {

    ArrayList<MediaAV.AudioList> list;
    ArrayList<MediaAV.AudioList> listClone = new ArrayList<MediaAV.AudioList>();
    ListView audioLV;
    AudioAdapter mediaAdapter;
    EditText searchET;
    ApiInterface apiServices;
    Progressdialog progressdialog;
    MediaActivity mediaActivity;
    public static LruCache<String,Bitmap> mcache = new LruCache<String, Bitmap>(1000);
    public AudioActivity(){}

    public AudioActivity(MediaActivity mediaActivity) {
        this.mediaActivity = mediaActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view       =       inflater.inflate(R.layout.audio,null);
        audioLV         =       (ListView)view.findViewById(R.id.audioLV);
        searchET        =       (EditText)  view.findViewById(R.id.searchET);
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                list.clear();
                if(editable.toString().trim().length()==0){
                    list.addAll(listClone);
                }else{
                    String st=editable.toString().replace(" ","");
                    for(MediaAV.AudioList audioList:listClone){
                        if(audioList.getTitle().toLowerCase().contains(st.toLowerCase())){
                            list.add(audioList);
                        }
                    }
                }
                mediaAdapter.notifyDataSetChanged();
            }
        });
        mediaData();
        return view;
    }

    private void mediaData() {
        try {
            list = mediaActivity.mediaAV.getAudiolist();
            listClone.addAll(list);

            if (list.size() > 0) {
                mediaAdapter = new AudioAdapter(mediaActivity, list);
                audioLV.setAdapter(mediaAdapter);

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
