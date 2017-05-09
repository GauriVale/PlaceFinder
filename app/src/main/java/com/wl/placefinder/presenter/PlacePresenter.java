package com.wl.placefinder.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import com.wl.placefinder.R;
import com.wl.placefinder.model.Photo;
import com.wl.placefinder.model.PhotoModel;
import com.wl.placefinder.model.SearchPlaceDetailsResponse;
import com.wl.placefinder.repository.PlaceSearchException;
import com.wl.placefinder.repository.PlaceSearchHelper;
import com.wl.placefinder.repository.SearchResultCallback;
import com.wl.placefinder.view.IPlaceView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 */

public class PlacePresenter {

    private final Context context;
    private final IPlaceView placeView;
    private final PlaceSearchHelper placeSearchHelper;

    public PlacePresenter(Context context, IPlaceView placeView) {
        this.context = context;
// TODO: 5/9/2017 take PlaceSearchHelper class instance as parameter
        this.placeView = placeView;

        placeSearchHelper = new PlaceSearchHelper(context);
    }

    /*public void unRegister() {
        this.placeView = null;
    }*/

    public void startSearchPlaceDetails(String placeId) {
        if (TextUtils.isEmpty(placeId)) {
            return;
        }

        // TODO: 5/9/2017 use loader instead

        //// TODO: 5/8/2017 asynctask not required
        //TODO: start asynctask/thread
        new PhotosDetailsDownloaderTask().execute(placeId);
    }

    public void downloadImage(PhotoModel model) {
        new DownloadImageTask().execute(model);
    }

    // TODO: 5/9/2017 use lrucache for adapter
    // TODO: 5/9/2017 store on sd card

    private class PhotosDetailsDownloaderTask extends AsyncTask<String, PhotoModel, PlaceSearchException> {

        private final List<PhotoModel> photos = new ArrayList<PhotoModel>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            placeView.onPlaceDetailsDownloadStart();
        }

        @Override
        protected PlaceSearchException doInBackground(String... params) {
            // TODO: 5/9/2017 create models from response received

            try {
                SearchPlaceDetailsResponse result = placeSearchHelper.getPlaceDetails(params[0]);
                if (!"OK".equals(result.getStatus())) {
                    placeView.onPlaceDetailsDownloadComplete(photos);
                    return new PlaceSearchException("invalid server response", PlaceSearchException.ERROR_INVALID_RESPONSE);
                }

                for (Photo photo : result.getResult().getPhotos()) {
                    PhotoModel model = new PhotoModel(photo.getPhotoReferenceId(), photo.getWidth(), photo.getHeight());
                    model.setBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher));
                    photos.add(model);

                    publishProgress(model);
                }
            } catch (PlaceSearchException e) {
                return e;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(PhotoModel... values) {
            super.onProgressUpdate(values);

            new DownloadThumbnailTask().execute(values[0], photos.indexOf(values[0]));
            placeView.onPlaceDetailsDownloadProgress(values[0]);
        }

        // TODO: 5/9/2017 no need of async task
        @Override
        protected void onPostExecute(PlaceSearchException e) {
            super.onPostExecute(e);

            if (null == e) {
                placeView.onPlaceDetailsDownloadComplete(photos);
            } else {

            }
        }
    }

    private class DownloadThumbnailTask extends AsyncTask<Object, Void, Integer> {

        @Override
        protected Integer doInBackground(final Object... params) {
//TODO: queue this
            placeSearchHelper.getPhotoThumbnail((PhotoModel)params[0], new SearchResultCallback<Bitmap>() {
                @Override
                public void onSuccess(Bitmap result) {
                    ((PhotoModel) params[0]).setBitmap(result);
                }

                @Override
                public void onFailure(Exception e) {
                }
            });
            return ((Integer) params[1]);
        }

        @Override
        protected void onPostExecute(Integer position) {
            super.onPostExecute(position);

            placeView.updatePhotoThumbnail(position);
        }
    }

    private class DownloadImageTask extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            placeView.onPhotoDownloadStart();
        }

        @Override
        protected String doInBackground(final Object... params) {

            try {
                Bitmap result = placeSearchHelper.downloadPhoto((PhotoModel)params[0]);
                return saveBitmap(result);
            } catch (PlaceSearchException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String ret) {
            super.onPostExecute(ret);

            placeView.onPhotoDownloadComplete(ret);

        }
    }

    private String saveBitmap(Bitmap bitmap) {
        File storageDirectory = Environment.getExternalStorageDirectory();
        File downloadDir = new File(storageDirectory.getAbsolutePath() + File.separator + "WLImgStorage");
        if (!downloadDir.exists()) {
            downloadDir.mkdir();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
        String formattedDate = simpleDateFormat.format(Calendar.getInstance().getTime());
        File file = new File(downloadDir.getAbsolutePath() + File.separator + "IMG_" + formattedDate + ".jpg");

        try {
            FileOutputStream fos = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, fos);
            fos.flush();
            fos.close();
            return file.getAbsolutePath();
        } catch (FileNotFoundException fnfe) {

        } catch (IOException ioe) {

        }

        return null;
    }
}
