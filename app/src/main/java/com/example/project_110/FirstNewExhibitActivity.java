package com.example.project_110;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirstNewExhibitActivity extends AppCompatActivity {
    private List<String> directionsList;
    private ArrayAdapter adapter;
    private List<VertexInfoStorable> shortestVertexOrder;
    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;
    private Graph<String, IdentifiedWeightedEdge> g;
    private int counter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_new_exhibit);
        shortestVertexOrder = getIntent().getParcelableArrayListExtra("shortestVertexOrder");
        vInfo = ZooData.loadVertexInfoJSON(this, "sample_node_info.json");
        eInfo = ZooData.loadEdgeInfoJSON(this, "sample_edge_info.json");
        g = ZooData.loadZooGraphJSON(this, "sample_zoo_graph.json");

//        System.out.println("Graph ");
//        for (IdentifiedWeightedEdge v : g.edgeSet())
//            System.out.println(g.getEdgeSource(v)+"--->"+g.getEdgeTarget(v));
        counter = 0;
        String start = shortestVertexOrder.get(counter++).id;
        counter+=1;
        String next = shortestVertexOrder.get(counter).id;

        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, start, next);
        int i = 1;
        directionsList = new ArrayList<>();
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            String direction = String.format("  %d. Walk %.0f meters along %s from '%s' to '%s'.\n",
                    i,
                    g.getEdgeWeight(e),
                    eInfo.get(e.getId()).street,
                    vInfo.get(g.getEdgeSource(e).toString()).name,
                    vInfo.get(g.getEdgeTarget(e).toString()).name);
            i++;
            directionsList.add(direction);
        }
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, directionsList);
        ListView listView = (ListView) findViewById(R.id.directions_list);
        listView.setAdapter(adapter);


    }

    public void onNextBtnClick(View view) {
        directionsList.clear();
        if(counter == shortestVertexOrder.size()-2){
            Button disableNext = (Button) findViewById(R.id.next_button);
            disableNext.setClickable(false);
        }
        String start = shortestVertexOrder.get(counter).id;
        counter+=1;
        String next = shortestVertexOrder.get(counter).id;

        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, start, next);
        int i = 1;
//        directionsList = new ArrayList<>();
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            String direction = String.format("  %d. Walk %.0f meters along %s from '%s' to '%s'.\n",
                    i,
                    g.getEdgeWeight(e),
                    eInfo.get(e.getId()).street,
                    vInfo.get(g.getEdgeSource(e).toString()).name,
                    vInfo.get(g.getEdgeTarget(e).toString()).name);
            i++;
            directionsList.add(direction);
        }
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, directionsList);
        ListView listView = (ListView) findViewById(R.id.directions_list);
        listView.setAdapter(adapter);
    }

    private void lastExhibit() {
    }
}