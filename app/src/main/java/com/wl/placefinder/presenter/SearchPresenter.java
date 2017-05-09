package com.wl.placefinder.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.wl.placefinder.model.QueryModel;
import com.wl.placefinder.model.SearchPlaceResponse;
import com.wl.placefinder.model.SearchPlaceResults;
import com.wl.placefinder.model.SearchResultModel;
import com.wl.placefinder.repository.DatabaseHelper;
import com.wl.placefinder.repository.PlaceSearchException;
import com.wl.placefinder.repository.PlaceSearchHelper;
import com.wl.placefinder.view.ISearchView;

import java.util.ArrayList;
import java.util.List;

/**
 */

public class SearchPresenter {
    private final Context context;
    private final ISearchView searchView;

    private final DatabaseHelper databaseHelper;
    private final PlaceSearchHelper placeSearchHelper;

    private SearchAsyncTask searchTask;

    public SearchPresenter(Context context, ISearchView searchView) {
        this.context = context;
        this.searchView = searchView;
// TODO: 5/9/2017 take PlaceSearchHelper class instance as parameter , db helper also
        databaseHelper = new DatabaseHelper(context);
        placeSearchHelper = new PlaceSearchHelper(context);
    }

    public void saveQuery(String query) {
        databaseHelper.insert(new QueryModel(query));
    }

    public void startSearch(String searchData) {
        if (TextUtils.isEmpty(searchData)) {
            return;
        }
        
        //// TODO: 5/8/2017 asynctask not required
        //TODO: start asynctask/thread
        new SearchAsyncTask().execute(searchData);
    }

    public void stopSearch() {
        if (null != searchTask) {
            if (!searchTask.isCancelled()) {
                searchTask.cancel(true);
            }
            searchTask = null;
        }
    }

    private class SearchAsyncTask extends AsyncTask<String, SearchResultModel, PlaceSearchException> {

        private final List<SearchResultModel> searchResults = new ArrayList<SearchResultModel>();

        @Override
        protected PlaceSearchException doInBackground(String... params) {
            //tODO: fetch from PlaceSearchHelper
            // TODO: 5/9/2017 create models from response received

            saveQuery(params[0]);

            try {
                SearchPlaceResponse result = placeSearchHelper.searchPlace(params[0]);
                if (!"OK".equals(result.getStatus())) {
                    searchView.onSearchComplete(searchResults);
                    return new PlaceSearchException("Invalid response from server", PlaceSearchException.ERROR_INVALID_RESPONSE);
                }

                for (SearchPlaceResults obj : result.getResults()) {
                    SearchResultModel model = new SearchResultModel(obj.getPlaceId(), obj.getName(), obj.getFormattedAddress());
                    model.setLatitude(obj.getGeometry().getLocation().getLatitude());
                    model.setLongitude(obj.getGeometry().getLocation().getLongitude());

                    searchResults.add(model);
                    publishProgress(model);
                }
                return null;
            } catch (PlaceSearchException e) {
                return e;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            searchView.onSearchStart();
        }


        @Override
        protected void onProgressUpdate(SearchResultModel... values) {
            super.onProgressUpdate(values);

            searchView.onSearchProgress(values[0]);
        }

        @Override
        protected void onPostExecute(PlaceSearchException o) {
            super.onPostExecute(o);

            if (null == o) {
                searchView.onSearchComplete(searchResults);
            } else {
                searchView.onSearchFail(o);
            }

            stopSearch();
        }
    }

    public void populateQueries(ArrayList<QueryModel> queries) {
        queries.addAll(databaseHelper.getAllQueries());
    }
}
