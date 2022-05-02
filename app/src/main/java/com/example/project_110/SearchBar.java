package com.example.project_110;

import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class SearchBar implements SearchView.OnQueryTextListener {
    public SearchView search;
    private String currentQuery;
    public VertexList vertexList;
    public List<ZooData.VertexInfo> currentAnimalsFromQuery;

    public SearchBar(SearchView search, VertexList vertexList) {
        this.search = search;
        this.vertexList = vertexList;
        currentAnimalsFromQuery = new ArrayList<ZooData.VertexInfo>();
        search.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        if (s.equals("")) {
            currentAnimalsFromQuery = new ArrayList<>();
            return false;
        }
        this.currentQuery = s;
        System.out.println(s);
        currentAnimalsFromQuery = vertexList.search(s);
        System.out.println(currentAnimalsFromQuery.size());
        return true;
    }

    public String getCurrentQuery() {
        return currentQuery;
    }
}
