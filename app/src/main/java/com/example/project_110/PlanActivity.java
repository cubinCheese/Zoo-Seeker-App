package com.example.project_110;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanActivity extends AppCompatActivity {
    private List<VertexInfoStorable> shortestVertexOrder;
    private List<VertexInfoStorable> shortestVertexOrder_clean;
    private List<String> exhibitNames;
    private List<Integer> exhibitDists;
    private List<String> exhibitStName;
    private ArrayAdapter adapter;
    private ListView listView;
    private Map<String, ZooData.EdgeInfo> eInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        List<VertexInfoStorable> selectedExhibitsList = getIntent().getParcelableArrayListExtra("selectedExhibitsList");
        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(this, "zoo_graph_new.json");
        // re-creating vInfo map locally
        Map<String, ZooData.VertexInfo> vInfoMap = ZooData.loadVertexInfoJSON(this, "zoo_node_new.json");
        eInfo = ZooData.loadEdgeInfoJSON(this, "zoo_edge_new.json");
        selectedExhibitsList.add(new VertexInfoStorable(vInfoMap.get("entrance_exit_gate")));
        // go through map, get(vInfo where .Kind.Gate)

        shortestVertexOrder = PathAlgorithm.shortestPath(g, selectedExhibitsList);
        shortestVertexOrder_clean = shortestVertexOrder;


        exhibitNames = new ArrayList<>();
        exhibitDists = new ArrayList<>();
        exhibitStName = new ArrayList<>();
//        exhibitDists.add(0);

//        shortestVertexOrder_clean.remove(shortestVertexOrder.size()-1);
//        for(int i = 0; i<shortestVertexOrder.size()-1; i++){
//            int index = i;
//            int distanceCount = 0;
//
//            GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, shortestVertexOrder.get(index).id, shortestVertexOrder.get(++index).id);
//            for (int j = 0; j<path.getEdgeList().size(); j++){
//                distanceCount+= g.getEdgeWeight(path.getEdgeList().get(j));
//                if(j == path.getEdgeList().size()-1){
//                    exhibitStName.add(eInfo.get(path.getEdgeList().get(j).getId()).street);
//                }
//            }
//            exhibitDists.add(distanceCount);
//        }
//
//        shortestVertexOrder_clean.remove(0);
//        for(int i = 0; i<shortestVertexOrder_clean.size(); i++){
//            exhibitNames.add(shortestVertexOrder_clean.get(i).name);
//        }
//
//        List<String> exhibitInfo = new ArrayList<>();
//        for(int i = 0; i<exhibitNames.size(); i++){
//            String info = exhibitNames.get(i) + "--" + exhibitDists.get(i) + "--" + exhibitStName.get(i);
//            exhibitInfo.add(info);
//        }
//
//        shortestVertexOrder_clean = shortestVertexOrder;
//        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, exhibitInfo);
//        listView = (ListView) findViewById(R.id.planList);
//        listView.setAdapter(adapter);

        //System.out.println("Optimal exhibit order (PlanActivity.java): ");
        //for (VertexInfoStorable v : shortestVertexOrder)
        //    System.out.println(v.name);
    }

    public void onDirectionsButtonClick(View view) {
         /* Here's some starter code for you :) */
         Intent intent = new Intent(this, UpdateDirectionsActivity.class);
         intent.putParcelableArrayListExtra("shortestVertexOrder", (ArrayList<VertexInfoStorable>) shortestVertexOrder);
         startActivity(intent);
    }
}