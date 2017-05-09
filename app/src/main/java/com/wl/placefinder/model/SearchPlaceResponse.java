package com.wl.placefinder.model;

import com.google.gson.annotations.SerializedName;

/**
 */

public class SearchPlaceResponse {

    @SerializedName("next_page_token")
    private String nextPageToken;

    @SerializedName("status")
    private String status;

    @SerializedName("results")
    private SearchPlaceResults[] results;

    @SerializedName("name")
    private String name;

    @SerializedName("place_id")
    private String placeId;

    public String getPlaceId() {
        return placeId;
    }

    public String getNextPageToken() {
        return nextPageToken;
    }

    public String getStatus() {
        return status;
    }

    public SearchPlaceResults[] getResults() {
        return results;
    }

    public String getName() {
        return name;
    }

}
