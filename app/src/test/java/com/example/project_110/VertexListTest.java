package com.example.project_110;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(AndroidJUnit4.class)
public class VertexListTest {
    //Mock map for testing
    Map<String, ZooData.VertexInfo> indexedZooData = new HashMap();

    @Before
    public void build_vList(){

        //Adding tags for 'grizzly_bears'
        List<String> grizzList = new ArrayList();
        grizzList.add("grizzly");
        grizzList.add("bear");
        grizzList.add("mammal");

        //Adding 'grizzly_bears' to map
        indexedZooData.put("grizzly_bears",new ZooData.VertexInfo("grizzly_bears", ZooData.VertexInfo.Kind.EXHIBIT, "Grizzly Bears", grizzList));

        List<String> pengList = new ArrayList();
        pengList.add("penguin");
        pengList.add("antarctic");
        pengList.add("bird");
        pengList.add("snow");

        indexedZooData.put("penguin_place",new ZooData.VertexInfo("penguin_place", ZooData.VertexInfo.Kind.EXHIBIT, "Penguin Place", pengList));

        List<String> pandaList = new ArrayList();
        pandaList.add("panda");
        pandaList.add("bird");

        indexedZooData.put("panda_palace",new ZooData.VertexInfo("panda_palace", ZooData.VertexInfo.Kind.EXHIBIT, "Panda Palace", pandaList));

    }

    @Test
    public void test_makeMap(){

    }

}
