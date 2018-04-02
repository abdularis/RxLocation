package com.github.abdularis.rxlocation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlacesOptions;

import java.util.List;

import io.reactivex.Single;

public final class RxPlace {

    public static Single<List<PlaceLikelihood>> currentPlace(@NonNull Context context, PlacesOptions placesOptions, PlaceFilter placeFilter) {
        return currentPlaceBuilder(context)
                .placesOptions(placesOptions)
                .placeFilter(placeFilter)
                .build();
    }
    
    public static Single<List<PlaceLikelihood>> currentPlace(@NonNull Context context) {
        return currentPlaceBuilder(context).build();
    }

    public static PlaceBuilder currentPlaceBuilder(@NonNull Context context) {
        return new PlaceBuilder(context);
    }

    public static class PlaceBuilder {

        Context context;
        PlacesOptions placesOptions;
        PlaceFilter placeFilter;

        PlaceBuilder(@NonNull Context context) {
            this.context = context;
        }

        public Single<List<PlaceLikelihood>> build() {
            return Single.create(new CurrentPlaceSingleOnSubscribe(context, placesOptions, placeFilter));
        }

        public PlaceBuilder placesOptions(PlacesOptions placesOptions) {
            this.placesOptions = placesOptions;
            return this;
        }

        public PlaceBuilder placeFilter(PlaceFilter placeFilter) {
            this.placeFilter = placeFilter;
            return this;
        }
    }

}
