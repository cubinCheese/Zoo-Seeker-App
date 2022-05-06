package com.example.project_110;

import java.util.List;

public class SearchList {

    List<ZooData.VertexInfo> searchList;


    public SearchList(List<ZooData.VertexInfo> searchList) {
        this.searchList = searchList;
    }

    public void add(List<ZooData.VertexInfo> addList){
        searchList.addAll(addList);
    }



}
