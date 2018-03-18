package com.github.abdularis.rxlocation;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class FusedLocationClientFlowableOnSubscribe implements FlowableOnSubscribe<Location> {

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mLocationProviderClient;

    public FusedLocationClientFlowableOnSubscribe(Context context, LocationRequest locationRequest) {
        mLocationRequest = locationRequest;
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @Override
    public void subscribe(FlowableEmitter<Location> emitter) throws Exception {
        FusedLocationCallback callback = new FusedLocationCallback(emitter);
        try {
            mLocationProviderClient
                    .requestLocationUpdates(mLocationRequest, callback, null)
                    .addOnFailureListener(emitter::onError);
        } catch (SecurityException e) {
            emitter.onError(e);
        }

        emitter.setCancellable(() -> {
            mLocationProviderClient.removeLocationUpdates(callback);
            callback.emitter = null;
        });
    }

    class FusedLocationCallback extends LocationCallback {

        FlowableEmitter<Location> emitter;

        FusedLocationCallback(FlowableEmitter<Location> emitter) {
            this.emitter = emitter;
        }

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (emitter != null) emitter.onNext(locationResult.getLastLocation());
        }
    }

}
