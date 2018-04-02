package com.github.abdularis.rxlocation;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.location.LocationRequest;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Single;

public final class RxLocation {

    public static Flowable<Location> locationUpdates(@NonNull Context context, long interval, long fastestInterval) {
        return locationUpdatesBuilder(context)
                .interval(interval)
                .fastestInterval(fastestInterval)
                .priority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .build();
    }

    public static Flowable<Location> locationUpdates(@NonNull Context context, long interval) {
        return locationUpdatesBuilder(context)
                .interval(interval)
                .fastestInterval(1000)
                .priority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .build();
    }

    public static Flowable<Location> locationUpdates(@NonNull Context context) {
        return locationUpdatesBuilder(context)
                .interval(1000)
                .fastestInterval(1000)
                .priority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .build();
    }

    public Single<Location> locationCurrent(@NonNull Context context) {
        return locationCurrentBuilder(context)
                .priority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .build();
    }


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
                return Single.create(new LocationCurrentSingleOnSubscribe(getContext(), getLocationRequest()));
            }
        };
    }

    public static Builder<Single<Location>> locationLastBuilder(@NonNull Context context) {
        return new Builder<Single<Location>>(context) {
            @Override
            public Single<Location> build() {
                return Single.create(new LocationLastSingleOnSubscribe(getContext()));
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

        public LocationUpdatesBuilder<T> locationRequest(LocationRequest locationRequest) {
            if (locationRequest != null) {
                this.locationRequest = locationRequest;
            }

            return this;
        }

        public LocationUpdatesBuilder<T> interval(long interval) {
            locationRequest.setInterval(interval);
            return this;
        }

        public LocationUpdatesBuilder<T> fastestInterval(long interval) {
            locationRequest.setFastestInterval(interval);
            return this;
        }

        public LocationUpdatesBuilder<T> priority(int priority) {
            locationRequest.setPriority(priority);
            return this;
        }

        public LocationUpdatesBuilder<T> expirationDuration(long duration) {
            locationRequest.setExpirationDuration(duration);
            return this;
        }

        public LocationUpdatesBuilder<T> expirationTime(long l) {
            locationRequest.setExpirationTime(l);
            return this;
        }

        public LocationUpdatesBuilder<T> maxWaitTime(long l) {
            locationRequest.setMaxWaitTime(l);
            return this;
        }

        public LocationUpdatesBuilder<T> numUpdates(int i) {
            locationRequest.setNumUpdates(i);
            return this;
        }

        public LocationUpdatesBuilder<T> smallestDisplacement(float displacement) {
            locationRequest.setSmallestDisplacement(displacement);
            return this;
        }
    }
}
