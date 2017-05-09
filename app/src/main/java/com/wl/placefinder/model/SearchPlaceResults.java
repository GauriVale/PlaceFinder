package com.wl.placefinder.model;

import com.google.gson.annotations.SerializedName;

/**
 */

public class SearchPlaceResults {

    @SerializedName("formatted_address")
    private String formattedAddress;

    @SerializedName("place_id")
    private String placeId;

    @SerializedName("geometry")
    private Geometry geometry;

    @SerializedName("name")
    private String name;

    public String getName() {
        return name;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

}
