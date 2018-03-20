package com.github.abdularis.rxlocation;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.PlacesOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;

public class CurrentPlaceSingleOnSubscribe implements SingleOnSubscribe<List<PlaceLikelihood>> {
    private final PlaceDetectionClient mPlaceDetectionClient;
    private final PlaceFilter mPlaceFilter;

    CurrentPlaceSingleOnSubscribe(Context context, PlacesOptions placesOptions, PlaceFilter placeFilter) {
        mPlaceDetectionClient = Places.getPlaceDetectionClient(context, placesOptions);
        mPlaceFilter = placeFilter;
    }

    @Override
    public void subscribe(SingleEmitter<List<PlaceLikelihood>> emitter) throws Exception {
        try {
            CurrentPlaceDetectionListener listener = new CurrentPlaceDetectionListener(emitter);
            mPlaceDetectionClient
                    .getCurrentPlace(mPlaceFilter)
                    .addOnSuccessListener(listener)
                    .addOnFailureListener(listener);
        } catch (SecurityException e) {
            emitter.onError(e);
        }
    }

    class CurrentPlaceDetectionListener implements OnSuccessListener<PlaceLikelihoodBufferResponse>,
            OnFailureListener, Disposable {

        PlaceLikelihoodBufferResponse mPlaceLikelihoodBufferResponse;
        SingleEmitter<List<PlaceLikelihood>> mEmitter;

        CurrentPlaceDetectionListener(SingleEmitter<List<PlaceLikelihood>> emitter) {
            mEmitter = emitter;
            mEmitter.setDisposable(this);
        }

        @Override
        public void onFailure(@NonNull Exception e) {
            mEmitter.onError(e);
        }

        @Override
        public void onSuccess(PlaceLikelihoodBufferResponse placeLikelihoods) {
            mPlaceLikelihoodBufferResponse = placeLikelihoods;
            List<PlaceLikelihood> list = new ArrayList<>();
            for (PlaceLikelihood placeLikelihood : placeLikelihoods) {
                list.add(placeLikelihood);
            }
            mEmitter.onSuccess(list);
        }

        @Override
        public void dispose() {
            if (mPlaceLikelihoodBufferResponse != null) {
                mPlaceLikelihoodBufferResponse.release();
                mPlaceLikelihoodBufferResponse = null;
                mEmitter.setDisposable(null);
                mEmitter = null;
            }
        }

        @Override
        public boolean isDisposed() {
            return mEmitter == null;
        }
    }
}
