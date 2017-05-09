package com.wl.placefinder.view;

import com.wl.placefinder.model.PhotoModel;

import java.util.List;

/**
 */

public interface IPlaceView {
    //TODO: download complete
    //recycler view loading finish

    public void onPlaceDetailsDownloadStart();
    public void onPlaceDetailsDownloadProgress(PhotoModel photoModel);
    public void onPlaceDetailsDownloadComplete(List<PhotoModel> searchResults);

    public void updatePhotoThumbnail(int position);

    public void onPhotoDownloadStart();
    public void onPhotoDownloadComplete(String fileNamewithDownloadPath);
}
