package com.wl.placefinder.repository;

/**
 */

public class PlaceSearchException extends Exception {

    public int getErrorCode() {
        return errorCode;
    }

    private int errorCode;

    public static final int ERROR_INTERNET_NOT_AVAILABLE = 1;
    public static final int ERROR_IO_EXCEPTION = 2;
    public static final int ERROR_INVALID_RESPONSE = 3;

    public PlaceSearchException(String message, int errorCode) {
        super(message);

        this.errorCode = errorCode;
    }
}
