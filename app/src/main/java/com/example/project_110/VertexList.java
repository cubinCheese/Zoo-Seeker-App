package com.example.project_110;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

// Vertex List Class
public class VertexList {
    public static Map<String, ZooData.VertexInfo> nodeList;                       // Map declaration of (id, Vertex info) -- container for ZooData
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

            if(!info.kind.equals(ZooData.VertexInfo.Kind.EXHIBIT)) {        // exclude vertex nodes with non-"Exhibit" tags
                continue;
            }
            for(String s : info.tags){
                if(searchMap.get(s) == null){
                    List<ZooData.VertexInfo> newList = new ArrayList<>();
                    newList.add(info);
                    searchMap.put(s.toLowerCase(), newList);
                }
                else{
                    searchMap.get(s.toLowerCase()).add(info);
                }
            }
            searchMap.put(m.getValue().name.toLowerCase(), new ArrayList<>(Arrays.asList(m.getValue())));
        }
    }

    // Implementation of search() method by tag, onto existing List of ZooData
    public List<ZooData.VertexInfo> search(String query){
        Set<ZooData.VertexInfo> vertices = new HashSet<>();
        for(Map.Entry<String, List<ZooData.VertexInfo>> m : searchMap.entrySet()){
            System.out.println(m.getKey());
            if(m.getKey().contains(query.toLowerCase()) || query.equals(m.getKey())) {
                vertices.addAll(searchMap.get(m.getKey()));
            }

        }
        // if(searchMap.get(query) == null) return new ArrayList<>();

        return new ArrayList(vertices);
    }




}
