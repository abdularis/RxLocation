package com.github.abdularis.rxlocation.errors;

public class GoogleApiClientConnectionSuspended extends RuntimeException {

    private int cause;

    public GoogleApiClientConnectionSuspended(int cause) {
        this.cause = cause;
    }

    @Override
    public String getMessage() {
        return "Connection to google api client was suspended";
    }

    public int getSuspendCause() {
        return cause;
    }
}
