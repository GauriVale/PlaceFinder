package com.wl.placefinder.model;

import com.google.gson.annotations.SerializedName;

/**
 */

public class Location {

    @SerializedName("lat")
    private double latitude;

    @SerializedName("lng")
    private double longitude;

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

}
