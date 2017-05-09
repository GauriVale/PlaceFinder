package com.wl.placefinder.repository;

/**
 */

public interface SearchResultCallback<T> {

    public void onSuccess(T result);
    public void onFailure(Exception e);
}
