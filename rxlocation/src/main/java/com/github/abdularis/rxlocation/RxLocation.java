package com.github.abdularis.rxlocation;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.location.LocationRequest;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;

public final class RxLocation {

    public static LocationUpdatesBuilder<Flowable<Location>> locationUpdatesBuilder(@NonNull Context context) {
        return new LocationUpdatesBuilder<Flowable<Location>>(context) {
            @Override
            public Flowable<Location> build() {
                // build hot observable
                return Flowable.create(new LocationUpdateFlowableOnSubscribe(getContext(), getLocationRequest()), BackpressureStrategy.LATEST)
                        .replay(1)
                        .refCount();
            }
        };
    }

    public static LocationUpdatesBuilder<Single<Location>> locationCurrentBuilder(@NonNull Context context) {
        return new LocationUpdatesBuilder<Single<Location>>(context) {
            @Override
            public Single<Location> build() {
                return Single.create(new CurrentLocationSingleOnSubscribe(getContext(), getLocationRequest()));
            }
        };
    }

    public static Builder<Single<Location>> locationLastBuilder(@NonNull Context context) {
        return new Builder<Single<Location>>(context) {
            @Override
            public Single<Location> build() {
                return Single.create(new LastLocationSingleOnSubscribe(getContext()));
            }
        };
    }


    public static abstract class Builder<T> {
        Context context;
        Builder(@NonNull Context context) {
            this.context = context;
        }

        public abstract T build();

        Context getContext() {
            return context;
        }


    }

    public static abstract class LocationUpdatesBuilder<T> extends Builder<T> {

        LocationRequest locationRequest;

        LocationUpdatesBuilder(@NonNull Context context) {
            super(context);
            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        LocationRequest getLocationRequest() {
            return locationRequest;
        }

        public LocationUpdatesBuilder<T> setLocationRequest(LocationRequest locationRequest) {
            if (locationRequest != null) {
                this.locationRequest = locationRequest;
            }

            return this;
        }

        public LocationUpdatesBuilder<T> setInterval(long interval) {
            locationRequest.setInterval(interval);
            return this;
        }

        public LocationUpdatesBuilder<T> setFastestInterval(long interval) {
            locationRequest.setFastestInterval(interval);
            return this;
        }

        public LocationUpdatesBuilder<T> setPriority(int priority) {
            locationRequest.setPriority(priority);
            return this;
        }

        public LocationUpdatesBuilder<T> setExpirationDuration(long duration) {
            locationRequest.setExpirationDuration(duration);
            return this;
        }

        public LocationUpdatesBuilder<T> setExpirationTime(long l) {
            locationRequest.setExpirationTime(l);
            return this;
        }

        public LocationUpdatesBuilder<T> setMaxWaitTime(long l) {
            locationRequest.setMaxWaitTime(l);
            return this;
        }

        public LocationUpdatesBuilder<T> setNumUpdates(int i) {
            locationRequest.setNumUpdates(i);
            return this;
        }

        public LocationUpdatesBuilder<T> setSmallestDisplacement(float displacement) {
            locationRequest.setSmallestDisplacement(displacement);
            return this;
        }
    }
}
