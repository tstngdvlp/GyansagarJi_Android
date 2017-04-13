package com.gyansagarji.android.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

public class Aboutus2 extends Fragment implements View.OnClickListener {
    ApiInterface apiServices;
    Progressdialog progressdialog;
    ImageView facebookIMGV,youtubeIMGV;
    ArrayList<AboutUs.AboutUsList> list;
    String facebook,googleplus,twitter,youtube;
    AboutusActivity aboutusActivity;

    public Aboutus2(AboutusActivity aboutusActivity) {
        this.aboutusActivity=aboutusActivity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view           =       inflater.inflate(R.layout.aboutus2,null);
        facebookIMGV        =       (ImageView)view.findViewById(R.id.facebookIMGV);
        youtubeIMGV         =       (ImageView)view.findViewById(R.id.youtubeIMGV);

        facebookIMGV.setOnClickListener(this);
        youtubeIMGV.setOnClickListener(this);
        aboutData();
        return view;
    }



    private void aboutData() {

        String aboutlist = PrefUtils.getFromPrefs(getActivity(),PrefUtils.AboutList,"");
        Gson gson = new Gson();
        AboutUs aboutUs    =   aboutusActivity.aboutUs;
        list              =   aboutUs.getAboutlist();

        if(list.size()>0){
            youtube       =   list.get(0).getYoutube_url();
            facebook      =   list.get(0).getFacebook_url();

        }

    }

    @Override
    public void onClick(View view) {
    switch (view.getId()){
        case R.id.facebookIMGV:

            showLinkInBrowser(facebook);
            break;

        case R.id.youtubeIMGV:
            showLinkInBrowser(youtube);
            break;


    }
    }

    private void showLinkInBrowser(String slink) {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse(slink));
        startActivity(intent);
    }
}
