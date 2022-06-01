package com.example.project_110;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.antlr.v4.runtime.misc.IntegerList;
import org.antlr.v4.runtime.misc.IntegerStack;
import org.jgrapht.Graph;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@RunWith(AndroidJUnit4.class)
public class UpdatePathAlgorithmTest {
    private List<VertexInfoStorable> visitList;
    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;
    Graph<String, IdentifiedWeightedEdge> g;
    private VertexInfoStorableDao dao;
    private VertexDatabase db;
    private List<VertexInfoStorable> selectedList;

    @After
    public void closeDb() throws IOException{
        db.close();
    }

    @Before
    public void setup(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, VertexDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.vertexInfoDao();
        g= ZooData.loadZooGraphJSON(context, "zoo_graph_new.json");
        vInfo = ZooData.loadVertexInfoJSON(context, "zoo_node_new.json");
         eInfo= ZooData.loadEdgeInfoJSON(context, "zoo_edge_new.json");
         VertexList.nodeList=vInfo;
        boolean toAdd = false;
        selectedList = new ArrayList<>();
        int count = 0;
        for(ZooData.VertexInfo i : vInfo.values()){
        if (count==4){
            count=0;
            toAdd=!toAdd;
        }else count++;
        if (toAdd){

            if(i.kind== ZooData.VertexInfo.Kind.EXHIBIT) selectedList.add(new VertexInfoStorable(i));
        }


        }

        visitList = PathAlgorithm.shortestPath(g,selectedList);
        /*
        IntegerStack removeList = new IntegerStack();
        for(int i=0;i<visitList.size();i++){
            if (visitList.get(i)==null) removeList.add(i);
        }
        while(!removeList.isEmpty()){
            visitList.remove(removeList.pop());
        }*/
    }

    @Test
    public void UpdatePath(){

        int j = visitList.size();
        VertexInfoStorable s = visitList.get(2);

        VertexInfoStorable k = null;
        int i = 1;
        while(k==null){
            k=visitList.get(j-i);
            i++;
        }
        String start = k.id;
        List<VertexInfoStorable> updatedList = UpdatePathAlgorithm.shortestPath(g,selectedList,start);
        assertNotEquals(visitList, updatedList);
    }
    @Test
    public void SameStartPath(){
        String start = "entrance_exit_gate";
        List<VertexInfoStorable> updatedList = UpdatePathAlgorithm.shortestPath(g,selectedList,start);
        assertEquals(visitList, updatedList);
    }

}
