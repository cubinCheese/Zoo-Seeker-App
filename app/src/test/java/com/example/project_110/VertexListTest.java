package com.example.project_110;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Public class - testing vertex list
@RunWith(AndroidJUnit4.class)
public class VertexListTest {
    // Mock map for testing
    Map<String, ZooData.VertexInfo> indexedZooData = new HashMap();

    // Class for testing build of vList -- element of searchable map
    @Before
    public void build_vList(){
        // Generating 3 vertex nodes to test - grizzList, pengList, pandaList

        // Adding tags for 'grizzly_bears'
        List<String> grizzList = new ArrayList();
        grizzList.add("grizzly");
        grizzList.add("bear");
        grizzList.add("mammal");

        // Generating 'grizzly_bears' vertexInfo as element and adding to mock map
        indexedZooData.put("grizzly_bears",new ZooData.VertexInfo("grizzly_bears","", ZooData.VertexInfo.Kind.EXHIBIT, "Grizzly Bears", grizzList,0.0,0.0));

        // Generating 'pengList' vertex within vList -- add to mock map
        List<String> pengList = new ArrayList();
        pengList.add("penguin");
        pengList.add("antarctic");
        pengList.add("bird");
        pengList.add("snow");

        indexedZooData.put("penguin_place",new ZooData.VertexInfo("penguin_place","", ZooData.VertexInfo.Kind.EXHIBIT, "Penguin Place", pengList,0.0,0.0));

        // Generating 'pandaList' vertex within vList -- add to mock map
        List<String> pandaList = new ArrayList();
        pandaList.add("panda");
        pandaList.add("bird");

        indexedZooData.put("panda_palace",new ZooData.VertexInfo("panda_palace","", ZooData.VertexInfo.Kind.EXHIBIT, "Panda Palace", pandaList,0.0,0.0));


        List<String> duckList = new ArrayList();
        duckList.add("duck");
        indexedZooData.put("duck_palace",new ZooData.VertexInfo("duck_palace","", ZooData.VertexInfo.Kind.EXHIBIT, "Duck Palace", duckList,0.0,0.0));

    }

    @After
    public void remove_vList(){
        this.indexedZooData = null;
    }

    // Class for testing build of searchable map
    @Test
    public void test_makeMap() {
        VertexList vertexList = new VertexList(indexedZooData);
        assertNotNull(vertexList.searchMap);
        assertEquals(13, vertexList.searchMap.size());               // checking if built Map includes all generated components within vList
        assertEquals(2, vertexList.searchMap.get("bird").size());   // checking if "bird" vertex has valid size
    }

    // Class for testing search method of searchable Map
    @Test
    public void test_search(){
        VertexList vertexList = new VertexList(indexedZooData);
        assertEquals(2, vertexList.search("bird").size());                  // assert that search by "bird" returns proper size of birdList
        assertEquals("duck_palace", vertexList.search("duck").get(0).id);   // assert that search by "duck" tag returns "duck_palace"
    }

}
