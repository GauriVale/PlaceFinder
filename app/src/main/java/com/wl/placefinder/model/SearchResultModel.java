package com.wl.placefinder.model;

/**
 */

public class SearchResultModel {

    // TODO: 5/9/2017 make it parcelable

    private String placeName;
    private String formattedAddress;

    public SearchResultModel(String placeId, String placeName, String formattedAddress) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.formattedAddress = formattedAddress;
    }

    public String getPlaceId() {
        return placeId;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    private String placeId;

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    private double latitude;
    private double longitude;

    public String getPlaceName() {
        return placeName;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

}
