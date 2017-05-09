package com.wl.placefinder.model;

import com.google.gson.annotations.SerializedName;

/**
 */

public class Photo {

    @SerializedName("height")
    private int height;

    @SerializedName("width")
    private int width;

    @SerializedName("photo_reference")
    private String photoReference;

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getPhotoReferenceId() {
        return photoReference;
    }

}
