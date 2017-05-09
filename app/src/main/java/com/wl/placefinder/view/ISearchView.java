package com.wl.placefinder.view;

import com.wl.placefinder.model.SearchResultModel;
import com.wl.placefinder.repository.PlaceSearchException;

import java.util.List;

/**
 */

public interface ISearchView {

    public void onSearchStart();
    public void onSearchProgress(SearchResultModel searchResultModel);
    public void onSearchComplete(List<SearchResultModel> searchResults);
    public void onSearchFail(PlaceSearchException e);

}
