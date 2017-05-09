package com.wl.placefinder.view;

import android.view.View;

/**
 * Listener interface for click events of items present in recycler view.
 */
public interface RecyclerViewItemClickListener {

    /**
     * This callback will be called when item present in recycler view is clicked.
     * @param data storing data linked with clicked item.
     * @param view view of clicked item.
     * @param position position of clicked item.
     */
    public void onItemClick(Object data, View view, int position);
}
