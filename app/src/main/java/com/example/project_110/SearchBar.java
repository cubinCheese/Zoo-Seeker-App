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
        List<String> grizzList = new ArrayList();
        grizzList.add("grizzly");
        grizzList.add("bear");
        grizzList.add("mammal");
        currentAnimalsFromQuery.add(new ZooData.VertexInfo("grizzly_bears", ZooData.VertexInfo.Kind.EXHIBIT, "Grizzly Bears", grizzList));
        search.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        this.currentQuery = s;
        currentAnimalsFromQuery = vertexList.search(s);
        return false;
    }

    public String getCurrentQuery() {
        return currentQuery;
    }
}
