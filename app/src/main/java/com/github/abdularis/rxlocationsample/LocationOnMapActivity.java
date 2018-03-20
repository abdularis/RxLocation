package com.github.abdularis.rxlocationsample;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.abdularis.rxlocation.RxLocation;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import io.reactivex.disposables.Disposable;

public class LocationOnMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final float DEFAULT_ZOOM_LEVEL = 12f;

    TextView textLoc;
    Disposable disposable;
    GoogleMap mGoogleMap;
    Marker mMyLocMarker;
    boolean mFollowMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_on_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        textLoc = findViewById(R.id.text_loc);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startListenLocationUpdate();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.setOnMarkerClickListener(marker -> {
            mFollowMarker = true;
            return false;
        });
    }

    private void startListenLocationUpdate() {
        disposable = RxLocation
                .getLocationUpdatesBuilder(this)
                .setInterval(1000)
                .build()
                .subscribe(this::updateLocationUi,
                        throwable -> textLoc.setText(String.format("Err: %s", throwable.toString())));
    }

    private void updateLocationUi(Location location) {
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());
        textLoc.setText(pos.toString());

        if (mGoogleMap == null) return;
        if (mMyLocMarker == null) {
            MarkerOptions options = new MarkerOptions().title("Me").position(pos);
            mMyLocMarker = mGoogleMap.addMarker(options);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, DEFAULT_ZOOM_LEVEL));
        } else {
            mMyLocMarker.setPosition(pos);
            if (mFollowMarker) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(pos));
            } else if (!isLatLngOnVisibleRegion(pos)) {
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(pos));
            }
        }
    }

    private boolean isLatLngOnVisibleRegion(LatLng pos) {
        // check apakah pos berada pada posisi yg terlihat dilayar
        LatLngBounds bounds = mGoogleMap.getProjection().getVisibleRegion().latLngBounds;
        return bounds.contains(pos);
    }
}
