package com.example.project_110;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    SearchBar searchbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // initializing new Map searchable by tags // & list of vertex info
        Map<String, ZooData.VertexInfo> vInfo = ZooData.loadVertexInfoJSON(this, "sample_node_info.json");
        VertexList vertexList = new VertexList(vInfo);


        Intent intent = new Intent(this, SearchDisplayActivity.class);
        startActivity(intent);

        SearchView search = (SearchView) findViewById(R.id.search);
        searchbar = new SearchBar(search);
    }

}