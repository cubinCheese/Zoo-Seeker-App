package com.example.project_110;

import android.widget.SearchView;

public class SearchBar implements SearchView.OnQueryTextListener {
    public SearchView search;
    private String currentQuery;

    public SearchBar(SearchView search) {
        this.search = search;
        search.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        this.currentQuery = s;
        //PUT SEARCH METHOD HERE
        return false;
    }

    public String getCurrentQuery() {
        return currentQuery;
    }
}
