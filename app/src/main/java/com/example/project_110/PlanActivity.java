package com.example.project_110;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.jgrapht.Graph;

import java.util.List;

public class PlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        List<VertexInfoStorable> selectedExhibitsList = getIntent().getParcelableArrayListExtra("selectedExhibitsList");
        Graph<String, IdentifiedWeightedEdge> g = ZooData.loadZooGraphJSON(this, "sample_zoo_graph.json");

    }
}