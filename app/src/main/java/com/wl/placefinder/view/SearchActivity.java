package com.wl.placefinder.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.wl.placefinder.R;
import com.wl.placefinder.model.QueryModel;
import com.wl.placefinder.model.SearchResultModel;
import com.wl.placefinder.presenter.SearchPresenter;
import com.wl.placefinder.repository.PlaceSearchException;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements ISearchView, View.OnClickListener, RecyclerViewItemClickListener {

    private ImageButton btnSearch;
    private AutoCompleteTextView autoCompleteTextView;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    private SearchPresenter presenter;

    private QueryAdapter queryAdapter;
    private SearchDataAdapter searchDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_search);

        btnSearch = (ImageButton) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.etSearch);
        recyclerView = (RecyclerView) findViewById(R.id.rvSearchList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerViewItemDecoration(this, RecyclerViewItemDecoration.VERTICAL_LIST));

        presenter = new SearchPresenter(this, this);

        ArrayList<QueryModel> queries = new ArrayList<>();

        presenter.populateQueries(queries);
        queryAdapter = new QueryAdapter(this, queries);
        autoCompleteTextView.setAdapter(queryAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // TODO: 5/8/2017 stop asynctask

        presenter.stopSearch();
    }

    public void onClick(View view) {
        String searchData = autoCompleteTextView.getText().toString();
        hideKeyboard();
        presenter.startSearch(searchData);
    }

    private void hideKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    @Override
    public void onSearchStart() {
        //TODO: show progress dialog

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.msg_loading));
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onSearchProgress(SearchResultModel searchResultModel) {
        //TODO:update adapter- notifydata set change
    }

    @Override
    public void onSearchComplete(List<SearchResultModel> searchResults) {
        //TODO: dismiss progress dialog


        searchDataAdapter = new SearchDataAdapter(searchResults);
        searchDataAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(searchDataAdapter);
        searchDataAdapter.notifyDataSetChanged();
        dismissProgressDialog();
    }

    @Override
    public void onSearchFail(PlaceSearchException e) {

    }

    private void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void onItemClick(Object data, View view, int position) {
        // TODO: 5/9/2017 start PlaceMapActivity

        SearchResultModel result = (SearchResultModel) data;

        Intent intent = new Intent(this, PlaceMapActivity.class);
        intent.putExtra(PlaceMapActivity.EXTRA_PLACE_ID, result.getPlaceId());
        intent.putExtra(PlaceMapActivity.EXTRA_LATITUDE, result.getLatitude());
        intent.putExtra(PlaceMapActivity.EXTRA_LONGITUDE, result.getLongitude());
        startActivity(intent);
    }
}
