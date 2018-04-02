package com.github.abdularis.rxlocation;

import android.content.Context;
import android.location.Location;

import com.github.abdularis.rxlocation.errors.NoLocationAvailableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class LocationLastSingleOnSubscribe implements SingleOnSubscribe<Location> {

    private final FusedLocationProviderClient mLocationProviderClient;

    public LocationLastSingleOnSubscribe(Context context) {
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @Override
    public void subscribe(SingleEmitter<Location> emitter) throws Exception {
        try {
            mLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location == null)
                            emitter.onError(new NoLocationAvailableException());
                        else
                            emitter.onSuccess(location);
                    })
                    .addOnFailureListener(emitter::onError);
        } catch (SecurityException e) {
            emitter.onError(e);
        }
    }
}
