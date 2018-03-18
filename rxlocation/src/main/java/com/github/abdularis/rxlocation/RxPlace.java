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

    public static Single<List<PlaceLikelihood>> getCurrentPlace(@NonNull Context context) {
        return getCurrentPlace(context, null, null);
    }

    public static Single<List<PlaceLikelihood>> getCurrentPlace(@NonNull Context context,
                                                                @Nullable PlacesOptions placesOptions,
                                                                @Nullable PlaceFilter placeFilter) {
        return Single.create(new CurrentPlaceSingleOnSubscribe(context, placesOptions, placeFilter));
    }

}
