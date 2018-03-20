# Android Reactive Library For Location and More

[![](https://jitpack.io/v/abdularis/RxLocation.svg)](https://jitpack.io/#abdularis/RxLocation)

This is a simple library that wraps google location, places API into rxjava 2 Observable, Flowable, Single etc.
___
## Setup
Add repo to your root build.gradle
~~~xml
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
~~~

Add dependency to your app build.gradle (your app)
~~~xml
dependencies {
	compile 'com.github.abdularis:RxLocation:v0.2-alpha'
}
~~~

This library uses features that available on java 8 or later so add this to your app build.gradle
~~~
android {
	...
	compileOptions {
		sourceCompatibility JavaVersion.VERSION_1_8
		targetCompatibility JavaVersion.VERSION_1_8
	}
}
~~~

And don't forget to add google play services location and places to your apps build.gradle file

___
## Usage
There are two main entry point to use this library (however you could access the underlying implementation of *OnSubscribe obervables)

- RxLocation
- RxPlace
- ...more will supported
~~~java
// get the location update for every 2 seconds
Disposable disposable = RxLocation.getLocationUpdatesBuilder(context)
        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        .setInterval(2000)
        .setFastestInterval(1000)
        .build()
        .subscribe(new Consumer<Location>() {
            @Override
            public void accept(Location location) throws Exception {
                // This will called regularly for location update
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                // Your error handling here
            }
        });
        ...
        ...
// don't forget to dispose when you no longer need it
disposable.dispose();
~~~

~~~java
// get current place
Disposable disposable = RxPlace.getCurrentPlaceBuilder(context)
        .build()
        .subscribe(new Consumer<List<PlaceLikelihood>>() {
            @Override
            public void accept(List<PlaceLikelihood> placeLikelihoods) throws Exception {
                // Your code
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                // Your error handling code
            }
        });
        ...
        ...
// don't forget to dispose when you no longer need it
disposable.dispose();
~~~

~~~java
// composing observable, get current location at that time using gps and current places
Disposable disposable = RxLocation.getCurrentLocationBuilder(context)
        .build()
        .flatMap(new Function<Location, Single<List<PlaceLikelihood>>>() {
            @Override
            public Single<List<PlaceLikelihood>> apply(Location location) throws Exception {
                // Do something with the location object
                return RxPlace.getCurrentPlaceBuilder(context).build();
            }
        })
        .subscribe(new Consumer<List<PlaceLikelihood>>() {
            @Override
            public void accept(List<PlaceLikelihood> placeLikelihoods) throws Exception {
                // Do something with places objects
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                // Your error handling code
            }
        });
        ...
        ...
// don't forget to dispose when you no longer need it
disposable.dispose();
~~~
