package com.github.abdularis.rxlocationsample;

import android.location.Address;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.abdularis.rxlocation.RxGeocoding;
import com.github.abdularis.rxlocation.RxLocation;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class GeocodingActivity extends AppCompatActivity {

    TextView textLoc;
    TextView textResult;
    Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geocoding);

        textLoc = findViewById(R.id.text_loc);
        textResult = findViewById(R.id.text_result);

        disposable = RxLocation.locationCurrentBuilder(this)
                .build()
                .flatMap((Function<Location, Single<List<Address>>>) location -> {
                    textLoc.setText(location.toString());
                    return RxGeocoding
                            .geocodingReverseBuilder(GeocodingActivity.this)
                            .maxResults(1)
                            .location(location.getLatitude(), location.getLongitude())
                            .build()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .toSingle();
                })
                .subscribe(addresses -> {
                    if (addresses.size() > 0) {
                        Address add = addresses.get(0);
                        textResult.setText(add.getLocality());
                    }
                }, throwable -> {
                    textResult.setText(throwable.toString());
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disposable != null) {
            disposable.dispose();
        }
    }
}
