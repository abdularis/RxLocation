package com.github.abdularis.rxlocation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlacesOptions;

import java.util.List;

import io.reactivex.Single;

public class RxPlace {

    public static Builder<Single<List<PlaceLikelihood>>> getCurrentPlaceBuilder(@NonNull Context context) {
        return new Builder<Single<List<PlaceLikelihood>>>(context) {
            @Override
            protected Single<List<PlaceLikelihood>> doBuild() {
                return Single.create(new CurrentPlaceSingleOnSubscribe(getContext(), placesOptions, placeFilter));
            }
        };
    }

    @Deprecated
    public static Single<List<PlaceLikelihood>> getCurrentPlace(@NonNull Context context) {
        return getCurrentPlaceBuilder(context).build();
    }

    @Deprecated
    public static Single<List<PlaceLikelihood>> getCurrentPlace(@NonNull Context context,
                                                                @Nullable PlacesOptions placesOptions,
                                                                @Nullable PlaceFilter placeFilter) {
        return getCurrentPlaceBuilder(context)
                .setPlacesOptions(placesOptions)
                .setPlaceFilter(placeFilter)
                .build();
    }

    public abstract static class Builder<T> {

        Context context;
        PlacesOptions placesOptions;
        PlaceFilter placeFilter;

        Builder(@NonNull Context context) {
            this.context = context;
        }

        public T build() {
            T obj = doBuild();
            context = null;
            return obj;
        }

        protected abstract T doBuild();

        Context getContext() {
            return context;
        }

        public Builder<T> setPlacesOptions(PlacesOptions placesOptions) {
            this.placesOptions = placesOptions;
            return this;
        }

        public Builder<T> setPlaceFilter(PlaceFilter placeFilter) {
            this.placeFilter = placeFilter;
            return this;
        }
    }

}
