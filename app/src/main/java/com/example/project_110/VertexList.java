package com.example.project_110;

import com.example.project_110.ZooData.VertexInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VertexList {
    Map<String, VertexInfo> nodeList;
    Map<String, List<VertexInfo>> searchMap;
    public VertexList(Map<String, VertexInfo> nodeList){
        this.nodeList = nodeList;
        this.makeMap();
    }


    public void makeMap(){
        for(Map.Entry<String, VertexInfo> m : nodeList.entrySet()){
            VertexInfo info = m.getValue();
            for(String s : info.tags){
                if(searchMap.get(s) == null){
                    List<VertexInfo> newList = new ArrayList<>();
                    newList.add(info);
                    searchMap.put(s, newList);
                }
                else{
                    searchMap.get(s).add(info);
                }

            }
        }
    }

    public List<VertexInfo> search(String query){
        if(searchMap.get(query) == null) return new ArrayList<>();

        for(Map.Entry<String, List<VertexInfo>> m : searchMap.entrySet()){
            if(m.getKey().contains(query)){
                return searchMap.get(query);
            }
        }
        return searchMap.get(query);
    }




}
