package com.github.abdularis.rxlocation.geocoding;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;
import java.util.Locale;

import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;

public class GeocodingOnSubscribe implements MaybeOnSubscribe<List<Address>> {

    private final Context mContext;
    private Locale mLocale;
    private int mMaxResults;
    private String mLocationName;
    private LatLngBounds mLatLngBounds;

    public GeocodingOnSubscribe(Context context, Locale locale,
                                String locationName, int maxResults, LatLngBounds latLngBounds) {
        mContext = context;
        mLocale = locale != null ? locale : Locale.getDefault();
        mLocationName = locationName;
        mMaxResults = maxResults;
        mLatLngBounds = latLngBounds;
    }

    @Override
    public void subscribe(MaybeEmitter<List<Address>> emitter) throws Exception {
        Geocoder geocoder = new Geocoder(mContext, mLocale);

        try {
            List<Address> addresses;
            if (mLatLngBounds != null) {
                double lowerLeftLatitude = mLatLngBounds.southwest.latitude;
                double lowerLeftLongitude = mLatLngBounds.southwest.longitude;
                double upperRightLatitude = mLatLngBounds.northeast.latitude;
                double upperRightLongitude = mLatLngBounds.northeast.longitude;
                addresses = geocoder.getFromLocationName(
                        mLocationName, mMaxResults,
                        lowerLeftLatitude, lowerLeftLongitude,
                        upperRightLatitude, upperRightLongitude);
            } else {
                addresses = geocoder.getFromLocationName(mLocationName, mMaxResults);
            }

            if (!emitter.isDisposed()) {
                if (addresses != null && addresses.size() > 0) {
                    emitter.onSuccess(addresses);
                } else {
                    emitter.onComplete();
                }
            }
        } catch (Exception e) {
            if (!emitter.isDisposed()) {
                emitter.onError(e);
            }
        }
    }
}
