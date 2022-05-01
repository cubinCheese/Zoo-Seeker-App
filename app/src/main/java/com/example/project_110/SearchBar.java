package com.example.project_110;

import android.widget.SearchView;

public class SearchBar implements SearchView.OnQueryTextListener {
    private SearchView search;

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
        System.out.println(s);
        //PUT SEARCH METHOD HERE
        return false;
    }
}
