package com.gyansagarji.android.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.google.gson.Gson;
import com.gyansagarji.android.Activity.AboutusActivity;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.AboutUs;
import com.gyansagarji.android.Rest.ApiInterface;
import com.gyansagarji.android.utils.PrefUtils;
import com.gyansagarji.android.utils.Progressdialog;

import java.util.ArrayList;

/**
 * Created by New android on 20-02-2017.
 */

public class Aboutus1 extends Fragment {
    WebView aboutTV;
    ArrayList<AboutUs.AboutUsList> list;
    ApiInterface apiServices;
    Progressdialog progressdialog;
    AboutusActivity aboutusActivity;

    public Aboutus1(AboutusActivity aboutusActivity) {
        this.aboutusActivity=aboutusActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view           =       inflater.inflate(R.layout.aboutus1,null);
        aboutTV             =       (WebView)view.findViewById(R.id.aboutTV);
        aboutTV.setVerticalScrollBarEnabled(true);
        aboutTV.setBackgroundColor(this.getResources().getColor(R.color.transparent));
        aboutData();
        return view;
    }

    private void aboutData() {
        String aboutlist = PrefUtils.getFromPrefs(getActivity(),PrefUtils.AboutList,"");
        Gson gson = new Gson();
       AboutUs aboutUs    =   aboutusActivity.aboutUs;
        list              =   aboutUs.getAboutlist();

        if(list.size()>0){
            try{
                byte pretext[] = list.get(0).getDescription().toString().getBytes("ISO-8859-1");

                String value = new String(pretext,"UTF-8");
                WebSettings settings = aboutTV.getSettings();
                settings.setDefaultTextEncodingName("utf-8");
                aboutTV.loadData(value, "text/html; charset=utf-8", null);

            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }
}
