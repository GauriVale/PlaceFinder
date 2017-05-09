package com.wl.placefinder.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.wl.placefinder.model.QueryModel;

import java.util.ArrayList;
import java.util.List;

/**
 */

public class QueryAdapter extends ArrayAdapter<QueryModel> {

    private ArrayList<QueryModel> queryList;
    private ArrayList<QueryModel> suggestions;

    public QueryAdapter(Context context, List<QueryModel> queries) {
        super(context, android.R.layout.simple_list_item_1, queries);

        this.queryList = (ArrayList<QueryModel>) queries;
        this.suggestions = new ArrayList<QueryModel>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }
        QueryModel queryModel = queryList.get(position);
        if (queryModel != null) {
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            if (textView != null) {
                textView.setText(queryModel.getQuery());
            }
        }
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    private Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((QueryModel) (resultValue)).getQuery();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (QueryModel queryModel : queryList) {
                    if (queryModel.getQuery().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(queryModel);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<QueryModel> filteredList = (ArrayList<QueryModel>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (QueryModel query : filteredList) {
                    add(query);
                }
                notifyDataSetChanged();
            }
        }
    };


}
