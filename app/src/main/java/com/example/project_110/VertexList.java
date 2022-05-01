package com.example.project_110;

import com.example.project_110.ZooData.VertexInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Vertex List Class
public class VertexList {
    Map<String, ZooData.VertexInfo> nodeList;                       // Map declaration of (id, Vertex info) -- container for ZooData
    Map<String, List<ZooData.VertexInfo>> searchMap;                // Map declaration of (tag, all vertices info)
    public VertexList(Map<String, ZooData.VertexInfo> nodeList){    // public class method to call/build nodeList & searchable map
        this.nodeList = nodeList;
        this.searchMap = new HashMap<>();
        this.makeMap();
    }

    // Builds searchable Map
    private void makeMap(){
        for(Map.Entry<String, ZooData.VertexInfo> m : nodeList.entrySet()){
            ZooData.VertexInfo info = m.getValue();
            for(String s : info.tags){
                if(searchMap.get(s) == null){
                    List<ZooData.VertexInfo> newList = new ArrayList<>();
                    newList.add(info);
                    searchMap.put(s, newList);
                }
                else{
                    searchMap.get(s).add(info);
                }

            }
        }
    }

    // Implementation of search() method by tag, onto existing List of ZooData
    public List<ZooData.VertexInfo> search(String query){
        if(searchMap.get(query) == null) return new ArrayList<>();

        for(Map.Entry<String, List<ZooData.VertexInfo>> m : searchMap.entrySet()){
            if(m.getKey().contains(query)){
                return searchMap.get(query);
            }
        }
        return searchMap.get(query);
    }




}
