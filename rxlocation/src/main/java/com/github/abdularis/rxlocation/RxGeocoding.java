package com.github.abdularis.rxlocation;

import android.content.Context;
import android.location.Address;
import android.support.annotation.NonNull;

import com.github.abdularis.rxlocation.geocoding.GeocodingOnSubscribe;
import com.github.abdularis.rxlocation.geocoding.ReverseGeocodingOnSubscribe;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;
import java.util.Locale;

import io.reactivex.Maybe;

public final class RxGeocoding {

    public static GeocodingBuilder getGeocodingBuilder(@NonNull Context context) {
        return new GeocodingBuilder(context);
    }

    public static ReverseGeocodingBuilder getReverseGeocodingBuilder(@NonNull Context context) {
        return new ReverseGeocodingBuilder(context);
    }

    public static class GeocodingBuilder extends RxLocation.Builder<Maybe<List<Address>>> {

        private Locale mLocale;
        private String mLocationName;
        private int mMaxResults;
        private LatLngBounds mLatLngBounds;

        GeocodingBuilder(@NonNull Context context) {
            super(context);
        }

        public GeocodingBuilder setLocationName(String locationName) {
            mLocationName = locationName;
            return this;
        }

        public GeocodingBuilder setMaxResults(int maxResults) {
            mMaxResults = maxResults;
            return this;
        }

        public GeocodingBuilder setLatLngBounds(LatLngBounds latLngBounds) {
            mLatLngBounds = latLngBounds;
            return this;
        }

        public GeocodingBuilder setLocale(Locale locale) {
            mLocale = locale;
            return this;
        }

        public GeocodingBuilder setLatLngBounds(double lowerLeftLatitude, double lowerLeftLongitude,
                                                           double upperRightLatitude, double upperRightLongitude) {
            LatLng soutWest = new LatLng(lowerLeftLatitude, lowerLeftLongitude);
            LatLng northEast = new LatLng(upperRightLatitude, upperRightLongitude);
            mLatLngBounds = new LatLngBounds(soutWest, northEast);
            return this;
        }

        @Override
        protected Maybe<List<Address>> doBuild() {
            return Maybe.create(new GeocodingOnSubscribe(getContext(), mLocale, mLocationName, mMaxResults, mLatLngBounds));
        }
    }

    public static class ReverseGeocodingBuilder extends RxLocation.Builder<Maybe<List<Address>>> {

        private Locale mLocale;
        private double mLatitude;
        private double mLongitude;
        private int mMaxResults;

        ReverseGeocodingBuilder(@NonNull Context context) {
            super(context);
        }

        public ReverseGeocodingBuilder setLocation(double latitude, double longitude) {
            mLatitude = latitude;
            mLongitude = longitude;
            return this;
        }

        public ReverseGeocodingBuilder setMaxResults(int maxResults) {
            mMaxResults = maxResults;
            return this;
        }

        public ReverseGeocodingBuilder setLocale(Locale locale) {
            mLocale = locale;
            return this;
        }

        @Override
        protected Maybe<List<Address>> doBuild() {
            return Maybe.create(new ReverseGeocodingOnSubscribe(getContext(), mLocale, new LatLng(mLatitude, mLongitude), mMaxResults));
        }
    }

}
