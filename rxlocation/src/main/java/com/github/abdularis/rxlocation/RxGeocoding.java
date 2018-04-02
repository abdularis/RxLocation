package com.github.abdularis.rxlocation;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;
import java.util.Locale;

import io.reactivex.Maybe;

public final class RxGeocoding {

    public static GeocodingBuilder geocodingBuilder(@NonNull Context context) {
        return new GeocodingBuilder(context);
    }

    public static GeocodingReverseBuilder geocodingReverseBuilder(@NonNull Context context) {
        return new GeocodingReverseBuilder(context);
    }

    private static Geocoder getGeocoder(Context context, Locale locale) {
        if (locale != null) return new Geocoder(context, locale);
        return new Geocoder(context);
    }

    public static class GeocodingBuilder {

        private final Context context;
        private Locale locale;
        private String locationName;
        private int maxResults;
        private LatLngBounds latLngBounds;

        GeocodingBuilder(@NonNull Context context) {
            this.context = context;
        }

        public Maybe<List<Address>> build() {
            return Maybe.fromCallable(() -> {
                Geocoder geocoder = getGeocoder(context, locale);
                if (latLngBounds != null) {
                    double lowerLeftLatitude = latLngBounds.southwest.latitude;
                    double lowerLeftLongitude = latLngBounds.southwest.longitude;
                    double upperRightLatitude = latLngBounds.northeast.latitude;
                    double upperRightLongitude = latLngBounds.northeast.longitude;
                    return geocoder.getFromLocationName(
                            locationName, maxResults,
                            lowerLeftLatitude, lowerLeftLongitude,
                            upperRightLatitude, upperRightLongitude);
                } else {
                    return geocoder.getFromLocationName(locationName, maxResults);
                }
            });
        }

        public GeocodingBuilder setLocationName(String locationName) {
            this.locationName = locationName;
            return this;
        }

        public GeocodingBuilder setMaxResults(int maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public GeocodingBuilder setLatLngBounds(LatLngBounds latLngBounds) {
            this.latLngBounds = latLngBounds;
            return this;
        }

        public GeocodingBuilder setLocale(Locale locale) {
            this.locale = locale;
            return this;
        }

        public GeocodingBuilder setLatLngBounds(double lowerLeftLatitude, double lowerLeftLongitude,
                                                           double upperRightLatitude, double upperRightLongitude) {
            LatLng soutWest = new LatLng(lowerLeftLatitude, lowerLeftLongitude);
            LatLng northEast = new LatLng(upperRightLatitude, upperRightLongitude);
            latLngBounds = new LatLngBounds(soutWest, northEast);
            return this;
        }
    }

    public static class GeocodingReverseBuilder {

        private final Context context;
        private Locale locale;
        private double latitude;
        private double longitude;
        private int maxResults;

        GeocodingReverseBuilder(@NonNull Context context) {
            this.context = context;
        }

        public Maybe<List<Address>> build() {
            return Maybe.fromCallable(() -> getGeocoder(context, locale).getFromLocation(latitude, longitude, maxResults));
        }

        public GeocodingReverseBuilder setLocation(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
            return this;
        }

        public GeocodingReverseBuilder setMaxResults(int maxResults) {
            this.maxResults = maxResults;
            return this;
        }

        public GeocodingReverseBuilder setLocale(Locale locale) {
            this.locale = locale;
            return this;
        }
    }

}
