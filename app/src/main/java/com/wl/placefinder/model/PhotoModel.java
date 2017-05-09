package com.wl.placefinder.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 */

public class PhotoModel {

    private int height;
    private int width;
    private String photoReference;
    private Bitmap bitmap;

    public PhotoModel(String photoReference, int width, int height) {
        this.photoReference = photoReference;
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public String getPhotoReferenceId() {
        return photoReference;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


}
