package com.github.abdularis.rxlocationsample;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.github.abdularis.rxlocation.RxLocation;
import com.github.abdularis.rxlocation.RxPlace;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class CurrentPlaceActivity extends AppCompatActivity {

    private static final String TAG = "CurrentPlaceActivity";

    TextView mTextLoc;
    ArrayAdapter<String> mListViewAdapter;
    Disposable mDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_place);

        mTextLoc = findViewById(R.id.text_loc);
        ListView listView = findViewById(R.id.list_view);
        mListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(mListViewAdapter);

        mDisposable = RxLocation.locationCurrentBuilder(this)
                .build()
                .flatMap((Function<Location, Single<List<PlaceLikelihood>>>) location -> {
                    LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                    mTextLoc.setText(ll.toString());
                    return RxPlace
                            .currentPlaceBuilder(CurrentPlaceActivity.this)
                            .build();
                })
                .subscribe(placeLikelihoods -> {
                    ArrayList<String> strs = new ArrayList<>();
                    for (PlaceLikelihood p : placeLikelihoods) {
                        Place place = p.getPlace();
                        strs.add(place.getName() + ": " + place.getAddress());
                    }
                    mListViewAdapter.clear();
                    mListViewAdapter.addAll(strs);
                }, throwable -> {
                    mTextLoc.setText(String.format("Err: %s", throwable.toString()));
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }
}
