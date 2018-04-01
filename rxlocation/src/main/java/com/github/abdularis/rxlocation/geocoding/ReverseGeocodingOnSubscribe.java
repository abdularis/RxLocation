package com.github.abdularis.rxlocation.geocoding;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

import io.reactivex.MaybeEmitter;
import io.reactivex.MaybeOnSubscribe;

public class ReverseGeocodingOnSubscribe implements MaybeOnSubscribe<List<Address>> {

    private final Context mContext;
    private Locale mLocale;
    private LatLng mLatLng;
    private int mMaxResults;

    public ReverseGeocodingOnSubscribe(Context context, Locale locale, LatLng latLng, int maxResults) {
        mContext = context;
        mLocale = locale != null ? locale : Locale.getDefault();
        mLatLng = latLng;
        mMaxResults = maxResults;
    }

    @Override
    public void subscribe(MaybeEmitter<List<Address>> emitter) throws Exception {
        if (mLatLng == null) {
            emitter.onComplete();
            return;
        }

        Geocoder geocoder = new Geocoder(mContext, mLocale);

        try {
            List<Address> addresses = geocoder.getFromLocation(mLatLng.latitude, mLatLng.longitude, mMaxResults);

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
