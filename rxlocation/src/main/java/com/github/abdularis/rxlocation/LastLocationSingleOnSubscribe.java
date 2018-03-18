package com.github.abdularis.rxlocation;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class LastLocationSingleOnSubscribe implements SingleOnSubscribe<Location> {

    private final FusedLocationProviderClient mLocationProviderClient;

    public LastLocationSingleOnSubscribe(Context context) {
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @Override
    public void subscribe(SingleEmitter<Location> emitter) throws Exception {
        try {
            mLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(emitter::onSuccess)
                    .addOnFailureListener(emitter::onError);
        } catch (SecurityException e) {
            emitter.onError(e);
        }
    }
}
