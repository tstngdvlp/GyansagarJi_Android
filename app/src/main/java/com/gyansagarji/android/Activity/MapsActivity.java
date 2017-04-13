package com.gyansagarji.android.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.gyansagarji.android.R;
import com.gyansagarji.android.Response.Maps;
import com.gyansagarji.android.Rest.ApiClient;
import com.gyansagarji.android.Rest.ApiInterface;
import com.gyansagarji.android.utils.NetworkConnectivity;
import com.gyansagarji.android.utils.PrefUtils;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveCanceledListener, GoogleMap.OnCameraIdleListener {

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private double latitude;
    private double longitude;
    LatLng latLng;
    TextView titleTV;
    ImageView backIMGV;
    ApiInterface apiServices;

    String mLatitude;
    String mLongitude;
    String titlename;
    TextView address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        backIMGV = (ImageView) findViewById(R.id.backIMGV);
        titleTV = (TextView) findViewById(R.id.titleTV);
        address     =   (TextView)findViewById(R.id.address);

        titleTV.setText(getResources().getString(R.string.location));
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        backIMGV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this,MainActivity.class));
                finish();
            }
        });


    }



    private void loadMap() {
        Log.d("maptest1","yes2");
        apiServices     = ApiClient.getClient().create(ApiInterface.class);
        Call<Maps> call = apiServices.mapData();
        call.enqueue(new Callback<Maps>() {
            @Override
            public void onResponse(Call<Maps> call, Response<Maps> response) {
                if(response.code() == 200){
                    if(response.body().getStatus().equals("1")){
                        Log.d("response_map",response.body().getReturn_long_message());
                        if(response.body().getLocationList().size()>0){
                            Maps mapdata = response.body();
                            Gson gson = new Gson();
                            String locationdata = gson.toJson(mapdata);
                            PrefUtils.saveToPrefs(getApplicationContext(),PrefUtils.LocationList,locationdata);
                            showLocation();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Maps> call, Throwable t) {

            }
        });
    }

    private void showLocation() {
        String locationdata = PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.LocationList,"");
        Gson gson = new Gson();
        Maps mapdata  = gson.fromJson(locationdata,Maps.class);

        mLatitude = mapdata.getLocationList().get(0).getLatt();
        mLongitude = mapdata.getLocationList().get(0).getLang();
        titlename  = mapdata.getLocationList().get(0).getTitle();
        address.setText(titlename);
        Log.d("mlatlang1",mLatitude+",,"+mLongitude+",,"+titlename);
        LatLng mlatLng = new LatLng(Double.valueOf(mLatitude),Double.valueOf(mLongitude));
        addMarker(mlatLng,titlename);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),13) );

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(mlatLng)      // Sets the center of the map to location user
                .zoom(17)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("maptest1","yes1");


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }else{
            mMap.setMyLocationEnabled(true);
        }
        mMap.setIndoorEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        if(NetworkConnectivity.isNetworkAvailable(this)){
            loadMap();
        }else if(PrefUtils.getFromPrefs(getApplicationContext(),PrefUtils.LocationList,"")!=""){
            showLocation();
        }
        Log.d("maptest1","yes3");
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("maptest1","yes4");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location mlastlocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        Log.d("mlatlang",mLatitude+",,"+mLongitude);
       mMap.clear();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude),13) );

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(latitude,longitude))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            latLng  =   new LatLng(latitude,longitude);



    }

    private void addMarker(LatLng location, String titlename) {
        mMap.clear();
        MarkerOptions markerOptions =   new MarkerOptions()
                .position(location)
                .draggable(true)
                .title(this.titlename);

        Marker locationmarker  =   mMap.addMarker(markerOptions);
        locationmarker.showInfoWindow();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onCameraIdle() {
        if(mMap!=null){
            mMap.clear();
        }
    }

    @Override
    public void onCameraMoveCanceled() {

    }

    @Override
    public void onCameraMove() {

    }

    @Override
    public void onCameraMoveStarted(int i) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(MapsActivity.this,MainActivity.class));
        finish();
    }
}

