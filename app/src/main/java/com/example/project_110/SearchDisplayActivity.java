package com.example.project_110;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchDisplayActivity extends AppCompatActivity {

        public RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_display);

        SearchDisplayAdapter adapter = new SearchDisplayAdapter();
        adapter.setHasStableIds(true);
        recyclerView = findViewById(R.id.seach_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        Map<String, ZooData.VertexInfo> indexedZooData = new HashMap();

        List<String> grizzList = new ArrayList();
        grizzList.add("grizzly");
        grizzList.add("bear");
        grizzList.add("mammal");

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

        //indexedZooData = ZooData.loadVertexInfoJSON(this, "sample_node_info.json");

        VertexList vertexList = new VertexList(indexedZooData);


        String tag = "bird";

        List<ZooData.VertexInfo> searchList =  vertexList.search(tag);

        Log.d("tag", searchList.get(0).name);


        adapter.setSearchListItems(searchList);

        Log.d("tag", searchList.get(0).name);




    }
}