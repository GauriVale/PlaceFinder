package com.wl.placefinder.model;

import com.google.gson.annotations.SerializedName;

/**
 */

public class SearchPlaceDetailsResult {

    @SerializedName("photos")
    private Photo[] photos;

    public Photo[] getPhotos() {
        return photos;
    }

}
