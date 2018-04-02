package com.github.abdularis.rxlocationsample;

import android.annotation.SuppressLint;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.abdularis.rxlocation.RxGeocoding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ReverseGeocodingActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText mLocInput;
    private TextView mTextResult;
    private GoogleMap mGoogleMap;
    private Marker mMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reverse_geocoding);

        mLocInput = findViewById(R.id.loc_input);
        mTextResult = findViewById(R.id.text_result);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("CheckResult")
    public void onGoClick(View view) {
        String loc = mLocInput.getText().toString();
        if (loc.isEmpty()) {
            Toast.makeText(this, "Location name empty", Toast.LENGTH_SHORT).show();
            return;
        }

        mTextResult.setText("Fetching...");
        RxGeocoding.geocodingBuilder(this)
                .setLocationName(loc)
                .setMaxResults(1)
                .build()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(addresses -> {
                    if (addresses.size() > 0) {
                        Address add = addresses.get(0);
                        String str = add.getLocality() + "\n" +
                                "lat:lng = " + add.getLatitude() + ":" + add.getLongitude();
                        mTextResult.setText(str);

                        LatLng pos = new LatLng(add.getLatitude(), add.getLongitude());
                        if (mMarker != null) {
                            mMarker.setPosition(pos);
                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12));
                        } else if (mGoogleMap != null) {
                            MarkerOptions options = new MarkerOptions()
                                    .title(add.getLocality())
                                    .position(pos);
                            mMarker = mGoogleMap.addMarker(options);
                            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 12));
                        }
                    } else {
                        mTextResult.setText("Empty");
                    }
                }, throwable -> mTextResult.setText("Err: " + throwable.toString()),
                        () -> mTextResult.setText("No result"));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
    }
}
