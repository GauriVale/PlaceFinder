package com.wl.placefinder.model;

import com.google.gson.annotations.SerializedName;

/**
 */

public class SearchPlaceDetailsResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("result")
    private SearchPlaceDetailsResult result;

    public SearchPlaceDetailsResult getResult() {
        return result;
    }

    public String getStatus() {
        return status;
    }

}
