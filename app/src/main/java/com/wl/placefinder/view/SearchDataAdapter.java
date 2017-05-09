package com.wl.placefinder.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wl.placefinder.R;

import com.wl.placefinder.model.SearchResultModel;

import java.util.List;

/**
 * Adapter for startSearch data.
 */
public class SearchDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SearchResultModel> searchResultModelList;
    private RecyclerViewItemClickListener listener;

    public SearchDataAdapter(List<SearchResultModel> searchResults) {
        searchResultModelList = searchResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_search_list_item, parent, false);

            return new SearchViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchViewHolder viewHolder = (SearchViewHolder) holder;
        SearchResultModel searchResult = searchResultModelList.get(position);
        viewHolder.getPlaceNameTextView().setText(searchResult.getPlaceName());
        viewHolder.getPlaceDescTextView().setText(searchResult.getFormattedAddress());
    }

    @Override
    public int getItemCount() {
        if (null == searchResultModelList) {
            return 0;
        }

        return searchResultModelList.size();
    }

    public void setOnItemClickListener(RecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     *
     */
    public class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView tvPlaceName;
        private final TextView tvPlaceDesc;

        public SearchViewHolder(View v) {
            super(v);

            v.setOnClickListener(this);
            tvPlaceName = (TextView) v.findViewById(R.id.tvPlaceName);
            tvPlaceDesc = (TextView) v.findViewById(R.id.tvPlaceDesc);
        }

        public TextView getPlaceNameTextView() {
            return tvPlaceName;
        }

        public TextView getPlaceDescTextView() {
            return tvPlaceDesc;
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(SearchDataAdapter.this.searchResultModelList.get(getAdapterPosition()), v, getAdapterPosition());
        }
    }


}
