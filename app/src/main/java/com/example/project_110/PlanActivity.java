package com.example.project_110;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import org.jgrapht.Graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlanActivity extends AppCompatActivity {
    private List<VertexInfoStorable> shortestVertexOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        List<VertexInfoStorable> selectedExhibitsList = getIntent().getParcelableArrayListExtra("selectedExhibitsList");
        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(this, "sample_zoo_graph.json");
        // re-creating vInfo map locally
        Map<String, ZooData.VertexInfo> vInfoMap = ZooData.loadVertexInfoJSON(this, "sample_node_info.json");

        selectedExhibitsList.add(new VertexInfoStorable(vInfoMap.get("entrance_exit_gate")));
        // go through map, get(vInfo where .Kind.Gate)

        shortestVertexOrder = PathAlgorithm.shortestPath(g, selectedExhibitsList);

        //System.out.println("Optimal exhibit order (PlanActivity.java): ");
        //for (VertexInfoStorable v : shortestVertexOrder)
        //    System.out.println(v.name);
    }

    public void onDirectionsButtonClick(View view) {
         /* Here's some starter code for you :) */
         Intent intent = new Intent(this, FirstNewExhibitActivity.class);
         intent.putParcelableArrayListExtra("shortestVertexOrder", (ArrayList<VertexInfoStorable>) shortestVertexOrder);
         startActivity(intent);
    }
}