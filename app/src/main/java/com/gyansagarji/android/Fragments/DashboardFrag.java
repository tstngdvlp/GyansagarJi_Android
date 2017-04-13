package com.gyansagarji.android.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gyansagarji.android.Activity.AboutusActivity;
import com.gyansagarji.android.Activity.EbooksActivity;
import com.gyansagarji.android.Activity.EventsActivity;
import com.gyansagarji.android.Activity.GalleryActivity;
import com.gyansagarji.android.Activity.JeevanParichay;
import com.gyansagarji.android.Activity.MapsActivity;
import com.gyansagarji.android.Activity.MediaActivity;
import com.gyansagarji.android.Activity.NewsActivity;
import com.gyansagarji.android.Activity.OrganizationActivity;
import com.gyansagarji.android.R;
import com.gyansagarji.android.utils.NetworkConnectivity;
import com.gyansagarji.android.utils.NetworkError;
import com.gyansagarji.android.utils.PrefUtils;


/**
 * Created by New android on 20-02-2017.
 */

public class DashboardFrag extends Fragment implements View.OnClickListener {
    LinearLayout parichayLL, newsLL, mediaLL, ebooksLL, aboutLL, eventsLL, locationLL, galleryLL,inviteLL,organizationLL;
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 20;
     int mId;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view           =   inflater.inflate(R.layout.dashboard, null);
        parichayLL          =   (LinearLayout) view.findViewById(R.id.parichayLL);
        newsLL              =   (LinearLayout) view.findViewById(R.id.newsLL);
        mediaLL             =   (LinearLayout) view.findViewById(R.id.mediaLL);
        ebooksLL            =   (LinearLayout) view.findViewById(R.id.ebooksLL);
        aboutLL             =   (LinearLayout) view.findViewById(R.id.aboutLL);
        eventsLL            =   (LinearLayout) view.findViewById(R.id.eventsLL);
        locationLL          =   (LinearLayout) view.findViewById(R.id.locationLL);
        galleryLL           =   (LinearLayout) view.findViewById(R.id.galleryLL);
        inviteLL            =   (LinearLayout) view.findViewById(R.id.inviteLL);
        organizationLL      =   (LinearLayout) view.findViewById(R.id.organizationLL);


        parichayLL.setOnClickListener(this);
        newsLL.setOnClickListener(this);
        mediaLL.setOnClickListener(this);
        ebooksLL.setOnClickListener(this);
        aboutLL.setOnClickListener(this);
        eventsLL.setOnClickListener(this);
        locationLL.setOnClickListener(this);
        galleryLL.setOnClickListener(this);
        inviteLL.setOnClickListener(this);
        organizationLL.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        mId =   id;

        switch (mId) {
            case R.id.parichayLL:
                if (PrefUtils.getFromPrefs(getActivity(), PrefUtils.ParichayList, "").toString() != "") {

                    startActivity(new Intent(getActivity(), JeevanParichay.class));
                    getActivity().finish();
                } else {
                    checkNetworkConnection();

                }
                break;
            case R.id.newsLL:

            if (PrefUtils.getFromPrefs(getActivity(), PrefUtils.NewsList, "").toString() != "") {

                startActivity(new Intent(getActivity(), NewsActivity.class));
                getActivity().finish();

            }else {
                checkNetworkConnection();

            }
                break;



            case R.id.mediaLL:

                if(PrefUtils.getFromPrefs(getActivity(),PrefUtils.MediaList,"").toString() != "") {
                    startActivity(new Intent(getActivity(), MediaActivity.class));
                    getActivity().finish();
                }else{
                    checkNetworkConnection();
                }
                break;
            case R.id.ebooksLL:

                if(PrefUtils.getFromPrefs(getActivity(),PrefUtils.EbooksList,"").toString() != "") {
                    startActivity(new Intent(getActivity(), EbooksActivity.class));
                    getActivity().finish();
                }else{
                    checkNetworkConnection();

                }

                break;

            case R.id.locationLL:

                if(PrefUtils.getFromPrefs(getActivity(),PrefUtils.LocationList,"")!=""){
                    startActivity(new Intent(getActivity(), MapsActivity.class));
                    getActivity().finish();
                }else{
                    checkNetworkConnection();

                }

                break;

            case R.id.aboutLL:
                if(PrefUtils.getFromPrefs(getActivity(),PrefUtils.AboutList,"")!=""){
                    startActivity(new Intent(getActivity(), AboutusActivity.class));
                    getActivity().finish();
                }else{
                    checkNetworkConnection();
                }


                break;

                default:

                    checkNetworkConnection();
                    break;
            }



    }

    private void checkNetworkConnection() {

        if(NetworkConnectivity.isNetworkAvailable(getActivity())){
            changeActivity();

        }else{
            NetworkError.networkConnectionError(getActivity());
        }
    }

    private void shareWindow() {

        Intent shareintent = new Intent();
        shareintent.setAction(Intent.ACTION_SEND);
        shareintent.putExtra(Intent.EXTRA_SUBJECT,"Gyansagar Ji App Link");
        shareintent.putExtra(Intent.EXTRA_TEXT,"Hey check out my app at: https://play.google.com/store/apps/details?id=com.gyansaar");
        shareintent.setType("text/plain");
        startActivity(shareintent);
    }
    private void checkPermission() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startActivity(new Intent(getActivity(), MapsActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startActivity(new Intent(getActivity(), MapsActivity.class));
            } else {
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void changeActivity() {
        switch (mId) {
            case R.id.parichayLL:
                startActivity(new Intent(getActivity(), JeevanParichay.class));
                getActivity().finish();
                break;
            case R.id.newsLL:
                startActivity(new Intent(getActivity(), NewsActivity.class));
                getActivity().finish();
                break;
            case R.id.mediaLL:
                startActivity(new Intent(getActivity(), MediaActivity.class));
                getActivity().finish();
                break;
            case R.id.ebooksLL:
                startActivity(new Intent(getActivity(), EbooksActivity.class));
                getActivity().finish();
                break;
            case R.id.locationLL:

                startActivity(new Intent(getActivity(), MapsActivity.class));
                getActivity().finish();
                break;
            case R.id.galleryLL:
                startActivity(new Intent(getActivity(), GalleryActivity.class));
                getActivity().finish();
                break;
            case R.id.inviteLL:
                shareWindow();
                break;
            case R.id.eventsLL:
                startActivity(new Intent(getActivity(), EventsActivity.class));
                getActivity().finish();
                break;
            case R.id.organizationLL:
                startActivity(new Intent(getActivity(), OrganizationActivity.class));
                getActivity().finish();
                break;
            case R.id.aboutLL:
                startActivity(new Intent(getActivity(), AboutusActivity.class));
                getActivity().finish();
                break;


        }
    }
}