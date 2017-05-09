package com.wl.placefinder.view;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wl.placefinder.R;
import com.wl.placefinder.model.PhotoModel;
import com.wl.placefinder.presenter.PlacePresenter;

import java.util.List;

public class PlaceMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, IPlaceView, RecyclerViewItemClickListener, LocationListener {

    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    private PlacePhotoAdapter adapter;

    private PlacePresenter presenter;

    private GoogleApiClient googleApiClient;
    private GoogleMap googleMap;

    private LocationRequest locationRequest;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    private Location currentLocation;
    private final LatLng defaultLocation = new LatLng(18.5204, 73.8567);//Pune
    private static final int DEFAULT_ZOOM = 30;

    public static final String EXTRA_PLACE_ID = "PLACE_ID";
    public static final String EXTRA_LATITUDE = "LATITUDE";
    public static final String EXTRA_LONGITUDE = "LONGITUDE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_maps);

        presenter = new PlacePresenter(this, this);

        recyclerView = (RecyclerView) findViewById(R.id.rvPlaceList);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, OrientationHelper.HORIZONTAL, false));

        createLocationRequest();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,
                        this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        presenter.startSearchPlaceDetails(getIntent().getStringExtra(EXTRA_PLACE_ID));
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(60 * 1000);
        locationRequest.setFastestInterval(30 * 1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // TODO: 5/9/2017 finish asynctask

        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.msg_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        this.googleMap = googleMap;
        updateDeviceLocation();
    }

    private void updateDeviceLocation() {

        if (!isLocationPermissionGranted()) {
            askLocationPermission();
            return;
        }

        if (currentLocation != null) {
            LatLng latLng = new LatLng(this.currentLocation.getLatitude(),
                    this.currentLocation.getLongitude());
            googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
        } else {
            googleMap.addMarker(new MarkerOptions().position(defaultLocation));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                googleApiClient, locationRequest, this);
    }

    private boolean isLocationPermissionGranted() {
        return (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    private void askLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPlaceDetailsDownloadStart() {
        showProgressDialog();
    }

    @Override
    public void onPlaceDetailsDownloadProgress(PhotoModel photoModel) {

    }

    @Override
    public void onPlaceDetailsDownloadComplete(List<PhotoModel> photos) {
        // TODO: 5/9/2017 run this on ui thread
        adapter = new PlacePhotoAdapter(photos);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);

        dismissProgressDialog();
    }

    @Override
    public void updatePhotoThumbnail(int position) {
        adapter.notifyItemChanged(position);
    }

    @Override
    public void onPhotoDownloadStart() {
        Toast.makeText(this, R.string.msg_downloading, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPhotoDownloadComplete(String fileNamewithDownloadPath) {
        String msg;
        if (null == fileNamewithDownloadPath) {
            msg = getString(R.string.msg_download_fail);
        } else {
            msg = getString(R.string.msg_download_success, fileNamewithDownloadPath);
        }
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(Object data, View view, int position) {
        // TODO: 5/9/2017 download image

        presenter.downloadImage((PhotoModel) data);
    }

    @Override
    public void onLocationChanged(Location location) {
        currentLocation = location;
        updateDeviceLocation();
        stopLocationUpdates();
    }
}
