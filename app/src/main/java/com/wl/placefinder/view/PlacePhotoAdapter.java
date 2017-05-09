package com.wl.placefinder.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wl.placefinder.R;
import com.wl.placefinder.model.PhotoModel;

import java.util.List;

/**
 */
public class PlacePhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PhotoModel> photoModelList;
    private RecyclerViewItemClickListener mListener;

    public PlacePhotoAdapter(List<PhotoModel> photoModelList) {
        this.photoModelList = photoModelList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_place_item, parent, false);

        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PhotoViewHolder viewHolder = (PhotoViewHolder) holder;
        PhotoModel searchResult = photoModelList.get(position);
        viewHolder.getPlaceImageView().setImageBitmap(searchResult.getBitmap());
    }

    @Override
    public int getItemCount() {
        if (null == photoModelList) {
            return 0;
        }

        return photoModelList.size();
    }

    public void setOnItemClickListener(RecyclerViewItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     *
     */
    public class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView ivPlace;

        public PhotoViewHolder(View v) {
            super(v);

            v.setOnClickListener(this);
            ivPlace = (ImageView) v.findViewById(R.id.ivPlace);
        }

        public ImageView getPlaceImageView() {
            return ivPlace;
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(PlacePhotoAdapter.this.photoModelList.get(getAdapterPosition()), v, getAdapterPosition());
        }
    }


}
