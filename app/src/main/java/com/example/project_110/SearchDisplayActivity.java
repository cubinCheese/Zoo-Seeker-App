package com.example.project_110;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SearchDisplayActivity extends AppCompatActivity {
    SearchBar searchbar;
    public RecyclerView recyclerView;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();
    private Future<Void> future;
    private SearchListViewModel searchListViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_display);

        searchListViewModel = new ViewModelProvider(this)
                .get(SearchListViewModel.class);
        // initializing new Map searchable by tags // & list of vertex info
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(this, "sample_node_info.json");
        VertexList vertexList = new VertexList(vInfo);
//        System.out.println(vertexList.search("bird").size());

        SearchView search = (SearchView) findViewById(R.id.search);
        searchbar = new SearchBar(search, vertexList);
        SearchDisplayAdapter adapter = new SearchDisplayAdapter();
        adapter.setHasStableIds(true);

        searchListViewModel.getSearchListItems().observe(this, adapter::setSearchListItems);
        recyclerView = findViewById(R.id.seach_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnSearchListItemClickedHandlder(searchListViewModel::selectExhibit);


        recyclerView.setAdapter(adapter);




        this.future = backgroundThreadExecutor.submit(() -> {
            while (true) { // TODO: change true to something that makes sense
                runOnUiThread(() -> {
                    List<VertexInfoStorable> packedList = new ArrayList();
                    for (ZooData.VertexInfo vertex : searchbar.currentAnimalsFromQuery){
                        packedList.add(new VertexInfoStorable(vertex));
                    }
                    adapter.setSearchListItems(packedList);
                });
                Thread.sleep(1000);
            }
        });


        //Log.d("tag", searchbar.currentAnimalsFromQuery.get(0).name);



    }
}



 /*Map<String, ZooData.VertexInfo> indexedZooData = new HashMap();

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

        List<ZooData.VertexInfo> searchList =  vertexList.search(t

        Log.d("tag", searchList.get(0).name);*/