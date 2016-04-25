package com.example.androidapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.constants.Style;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int PERMISSIONS_LOCATION = 0;
    private MapView mMapView;
    private MapboxMap mMapBoxMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_dark:
                mMapView.setStyleUrl(Style.DARK);
                break;
            case R.id.action_emerald:
                mMapView.setStyleUrl(Style.EMERALD);
                break;
            case R.id.action_light:
                mMapView.setStyleUrl(Style.LIGHT);
                break;
            case R.id.action_mapbox_streets:
                mMapView.setStyleUrl(Style.MAPBOX_STREETS);
                break;
            case R.id.action_satelite:
                mMapView.setStyleUrl(Style.SATELLITE);
                break;
            case R.id.action_satelite_streets:
                mMapView.setStyleUrl(Style.SATELLITE_STREETS);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMapBoxMap.setMyLocationEnabled(true);
                }
            }
        }
    }

    @Override
    public void onMapReady(MapboxMap map) {
        mMapBoxMap = map;
        mMapBoxMap.setStyleUrl(Style.EMERALD);
        mMapBoxMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(0, 0)));

        // Show user location (purposely not in follow mode)
        if ((ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_LOCATION);
        } else {
            mMapBoxMap.setMyLocationEnabled(true);
        }

        // Set the camera's starting position
        Location myLocation = mMapBoxMap.getMyLocation();
        if (myLocation == null) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_LONG).show();
            return;
        }
        LatLng userLocation = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userLocation) // set the camera's center position
                .zoom(16)  // set the camera's zoom level
                .tilt(20)  //get  set the camera's tilt
                .build();

        mMapBoxMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        IconFactory iconFactory = IconFactory.getInstance(MainActivity.this);
        Drawable iconDrawable = ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_directions_boat_black);
        Icon icon = iconFactory.fromDrawable(iconDrawable);

        mMapBoxMap.addMarker(new MarkerOptions()
                .position(userLocation)
                .title("Hello Margge!")
                .snippet("Welcome!"))
                .setIcon(icon);
    }
}
