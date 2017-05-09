package com.wl.placefinder.model;

/**
 */

public class QueryModel {
    private String query;

    public QueryModel(String query) {
        this.query = query;
    }

    public String getQuery() {
        return query;
    }

    @Override
    public String toString() {
        return "QueryModel{" +
                "query='" + query + '\'' +
                '}';
    }
}
