package com.wl.placefinder.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.wl.placefinder.R;
import com.wl.placefinder.model.PhotoModel;
import com.wl.placefinder.model.SearchPlaceDetailsResponse;
import com.wl.placefinder.model.SearchPlaceResponse;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 */

public class PlaceSearchHelper {

    // TODO: 5/9/2017 rename as PlaceApiClient

    private final Context context;

    public static final String BASE_API = "https://maps.googleapis.com/maps/api/place/";
    public static final String TEXT_SEARCH_API = "textsearch";
    public static final String RESPONSE_FORMAT = "json";
    public static final String KEY_QUERY = "query";
    public static final String KEY_KEY = "key";


    public static final String PLACE_PHOTO_DOWNLOAD_API = "photo";

    public static final String PLACE_DETAILS_SEARCH_API = "details";
    public static final String KEY_PLACE_ID = "placeid";

    public static final String KEY_MAX_WIDTH = "maxwidth";
    public static final String KEY_MAX_HEIGHT = "maxheight";

    public static final String KEY_PHOTO_REF_ID = "photoreference";

    public static final String SEPARATOR_EQUAL_TO = "=";
    public static final String SEPARATOR_QUESTION_MARK = "?";
    public static final String SEPARATOR_AMPERSAND = "&";
    public static final String SEPARATOR_FORWARD_SLASH = "/";


    public PlaceSearchHelper(Context context) {
        this.context = context;
    }

    private boolean isConnectedToInternet() {
        try {
            if (context != null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void searchPlace(String searchData, final SearchResultCallback<SearchPlaceResponse> callback) {
        if (!isConnectedToInternet()) {
            callback.onFailure(new PlaceSearchException("Internet not available.", PlaceSearchException.ERROR_INTERNET_NOT_AVAILABLE));
        }
// TODO: 5/9/2017 set timeout of 1min
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_API + TEXT_SEARCH_API + SEPARATOR_FORWARD_SLASH + RESPONSE_FORMAT).newBuilder();
        urlBuilder.addQueryParameter(KEY_QUERY, searchData);
        urlBuilder.addQueryParameter(KEY_KEY, getApiKey());
        String url = urlBuilder.build().toString();


        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String resp = responseBody.string();
            responseBody.close();
            response.close();
            SearchPlaceResponse resultModel = new Gson().fromJson(resp, SearchPlaceResponse.class);//tddo: parse

            callback.onSuccess(resultModel);

        } catch (IOException e) {
            callback.onFailure(new PlaceSearchException("IO failure while searching place", PlaceSearchException.ERROR_IO_EXCEPTION));
        }

    }

    public void getPlaceDetails(String placeId, final SearchResultCallback<SearchPlaceDetailsResponse> callback) {
        if (!isConnectedToInternet()) {
            callback.onFailure(new PlaceSearchException("Internet not available.", PlaceSearchException.ERROR_INTERNET_NOT_AVAILABLE));
        }

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_API + PLACE_DETAILS_SEARCH_API + SEPARATOR_FORWARD_SLASH + RESPONSE_FORMAT).newBuilder();
        urlBuilder.addQueryParameter(KEY_PLACE_ID, placeId);
        urlBuilder.addQueryParameter(KEY_KEY, getApiKey());
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String resp = responseBody.string();
            responseBody.close();
            response.close();
            SearchPlaceDetailsResponse result = new Gson().fromJson(resp, SearchPlaceDetailsResponse.class);

            callback.onSuccess(result);

        } catch (IOException e) {
            callback.onFailure(new PlaceSearchException("IO failure while searching place details", PlaceSearchException.ERROR_IO_EXCEPTION));
        }


    }

    public void getPhotoThumbnail(PhotoModel photoModel, final SearchResultCallback<Bitmap> callback) {
        Bitmap bitmap = null;

        try {
            bitmap = downloadPhoto(photoModel.getPhotoReferenceId(), 160, 160);
            callback.onSuccess(bitmap);
        } catch (PlaceSearchException e) {
            callback.onFailure(e);
        }
    }

    public void downloadPhoto(PhotoModel photoModel, final SearchResultCallback<Bitmap> callback) {
        Bitmap bitmap = null;

        try {
            bitmap = downloadPhoto(photoModel.getPhotoReferenceId(), photoModel.getWidth(), photoModel.getHeight());
            callback.onSuccess(bitmap);
        } catch (PlaceSearchException e) {
            callback.onFailure(e);
        }
    }

    private Bitmap downloadPhoto(String photoReference, long maxWidth, long maxHeight) throws PlaceSearchException {

        if (!isConnectedToInternet()) {
            throw new PlaceSearchException("Internet not available.", PlaceSearchException.ERROR_INTERNET_NOT_AVAILABLE);
        }
// TODO: 5/9/2017 queue requests to be sent
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_API + PLACE_PHOTO_DOWNLOAD_API).newBuilder();
        urlBuilder.addQueryParameter(KEY_PHOTO_REF_ID, photoReference);
        urlBuilder.addQueryParameter(KEY_MAX_HEIGHT, String.valueOf(maxHeight));
        urlBuilder.addQueryParameter(KEY_MAX_WIDTH, String.valueOf(maxWidth));
        urlBuilder.addQueryParameter(KEY_KEY, getApiKey());
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Bitmap bitmap = null;
        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();

            bitmap = BitmapFactory.decodeStream(responseBody.byteStream());
            responseBody.close();
            response.close();
        } catch (IOException e) {
            throw new PlaceSearchException("IO failure while searching place details", PlaceSearchException.ERROR_IO_EXCEPTION);
        }

        return bitmap;
    }

    private String getApiKey() {
        return context.getString(R.string.google_api_key);
    }


    public SearchPlaceResponse searchPlace(String searchData) throws PlaceSearchException {
        if (!isConnectedToInternet()) {
            throw new PlaceSearchException("Internet not available.", PlaceSearchException.ERROR_INTERNET_NOT_AVAILABLE);
        }
// TODO: 5/9/2017 set timeout of 1min
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_API + TEXT_SEARCH_API + SEPARATOR_FORWARD_SLASH + RESPONSE_FORMAT).newBuilder();
        urlBuilder.addQueryParameter(KEY_QUERY, searchData);
        urlBuilder.addQueryParameter(KEY_KEY, getApiKey());
        String url = urlBuilder.build().toString();


        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String resp = responseBody.string();
            if (TextUtils.isEmpty(resp)) {
                responseBody.close();
                response.close();
                throw new PlaceSearchException("invalid server response", PlaceSearchException.ERROR_INVALID_RESPONSE);
            }
            responseBody.close();
            response.close();
            SearchPlaceResponse resultModel = new Gson().fromJson(resp, SearchPlaceResponse.class);//tddo: parse

            return resultModel;

        } catch (IOException e) {
            throw new PlaceSearchException("IO failure while searching place", PlaceSearchException.ERROR_IO_EXCEPTION);
        }

    }

    public SearchPlaceDetailsResponse getPlaceDetails(String placeId) throws PlaceSearchException {
        if (!isConnectedToInternet()) {
            throw new PlaceSearchException("Internet not available.", PlaceSearchException.ERROR_INTERNET_NOT_AVAILABLE);
        }

        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(BASE_API + PLACE_DETAILS_SEARCH_API + SEPARATOR_FORWARD_SLASH + RESPONSE_FORMAT).newBuilder();
        urlBuilder.addQueryParameter(KEY_PLACE_ID, placeId);
        urlBuilder.addQueryParameter(KEY_KEY, getApiKey());
        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try {
            Response response = client.newCall(request).execute();
            ResponseBody responseBody = response.body();
            String resp = responseBody.string();
            responseBody.close();
            response.close();
            SearchPlaceDetailsResponse result = new Gson().fromJson(resp, SearchPlaceDetailsResponse.class);
            return result;
        } catch (IOException e) {
            throw new PlaceSearchException("IO failure while searching place details", PlaceSearchException.ERROR_IO_EXCEPTION);
        }


    }

    public Bitmap getPhotoThumbnail(PhotoModel photoModel) throws PlaceSearchException {
        Bitmap bitmap = downloadPhoto(photoModel.getPhotoReferenceId(), 160, 160);
        return bitmap;
    }

    public Bitmap downloadPhoto(PhotoModel photoModel) throws PlaceSearchException {
        Bitmap bitmap = downloadPhoto(photoModel.getPhotoReferenceId(), photoModel.getWidth(), photoModel.getHeight());
        return bitmap;
    }

}
