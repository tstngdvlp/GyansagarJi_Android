package com.gyansagarji.android.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import com.gyansagarji.android.Fragments.DashboardFrag;
import com.gyansagarji.android.R;
import com.gyansagarji.android.utils.NetworkConnectivity;
import com.gyansagarji.android.utils.NetworkError;
import com.gyansagarji.android.utils.PrefUtils;
import com.gyansagarji.android.utils.RateUs;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    public static final int LOCATION_PERMISSION_REQUEST_CODE = 20;
    RateUs rateus;
    Fragment fragment;
    int mId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       if(savedInstanceState==null){
           fragment        =       null;
           Class    fragmentClass   =       DashboardFrag.class;
           try {
               fragment             =       (Fragment)fragmentClass.newInstance();
           } catch (InstantiationException e) {
               e.printStackTrace();
           } catch (IllegalAccessException e) {
               e.printStackTrace();
           }
           FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
           ft.replace(R.id.flContent,fragment);
           ft.commit();
       }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        rateus            =       new RateUs();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.rateus) {
            rateus.alertDialog(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        mId =   id;

        switch (mId) {
            case R.id.nav_parichay:
                if(PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.ParichayList,"").toString()!=""){
                    startActivity(new Intent(this, JeevanParichay.class));
                    finish();
                }else{
                    NetworkError.networkConnectionError(MainActivity.this);
                }
                break;
            case R.id.nav_news:
                if(PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.NewsList,"").toString()!=""){
                    startActivity(new Intent(this, NewsActivity.class));
                    finish();
                }else{
                    NetworkError.networkConnectionError(MainActivity.this);
                }
                break;
            case R.id.nav_media:
                if(PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.MediaList,"").toString() != "") {
                    startActivity(new Intent(this, MediaActivity.class));
                    finish();
                }else{
                    NetworkError.networkConnectionError(MainActivity.this);
                }
                break;
            case R.id.nav_ebooks:
                if(PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.EbooksList,"").toString() != "") {
                    startActivity(new Intent(this, EbooksActivity.class));
                    finish();
                }else{
                    NetworkError.networkConnectionError(MainActivity.this);
                }

            break;
            case R.id.nav_location:
                if(PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.LocationList,"")!=""){
                    startActivity(new Intent(this,MapsActivity.class));
                    finish();
                }else {
                    NetworkError.networkConnectionError(MainActivity.this);
                }

                break;

            case R.id.nav_about:
                if(PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.AboutList,"")!=""){
                    startActivity(new Intent(this,AboutusActivity.class));
                    finish();
                }else{
                    NetworkError.networkConnectionError(MainActivity.this);
                }

                break;
            default:
                checkNetworkConnection();
                break;
        }


        return true;
    }


    private void checkNetworkConnection() {

        if(NetworkConnectivity.isNetworkAvailable(MainActivity.this)){
            changeActivity();

        }else{
            NetworkError.networkConnectionError(MainActivity.this);
        }
    }

    private void changeActivity() {
        Class fragmentClass = null;
        fragment        =       null;

        switch (mId){
            case R.id.nav_parichay:
                startActivity(new Intent(this,JeevanParichay.class));
                finish();
                break;
            case R.id.nav_news:
                startActivity(new Intent(this, NewsActivity.class));
                finish();
                break;
            case R.id.nav_media:
                startActivity(new Intent(this, MediaActivity.class));
                finish();
                break;
            case R.id.nav_ebooks:
                startActivity(new Intent(this,EbooksActivity.class));
                finish();
                break;
            case R.id.nav_gallery:

                startActivity(new Intent(this,GalleryActivity.class));
                finish();
                break;
            case R.id.nav_invite:
                shareWindow();
                break;
            case R.id.nav_events:
                startActivity(new Intent(this,EventsActivity.class));
                finish();
                break;
            case R.id.nav_location:
                startActivity(new Intent(this,MapsActivity.class));
                finish();
                break;
            case R.id.nav_organization:
                startActivity(new Intent(this, OrganizationActivity.class));
                finish();
                break;
            case R.id.nav_about:
                startActivity(new Intent(this,AboutusActivity.class));
                finish();
                break;

        }

        if(fragment!=null){
            try {
                fragment    =   (Fragment)fragmentClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContent,fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(fragment!=null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
        if(requestCode == 100){
            changeActivity();
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
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            startActivity(new Intent(this,MapsActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                startActivity(new Intent(this, MapsActivity.class));
            } else {
            }
        }
    }
}
