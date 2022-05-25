package com.example.project_110;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateDirectionsActivity extends AppCompatActivity { //Rename to ExhibitActivity
    private List<String> directionsList = new ArrayList<>();;
    private ArrayAdapter adapter;
    private List<VertexInfoStorable> shortestVertexOrder;
    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;
    private Graph<String, IdentifiedWeightedEdge> g;
    private int counter;
    private ListView listView;
    private Switch dirSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_directions);
        shortestVertexOrder = getIntent().getParcelableArrayListExtra("shortestVertexOrder");
        vInfo = ZooData.loadVertexInfoJSON(this, "sample_node_info.json");
        eInfo = ZooData.loadEdgeInfoJSON(this, "sample_edge_info.json");
        g = ZooData.loadZooGraphJSON(this, "sample_zoo_graph.json");

        counter = 0;
        dirSwitch = (Switch) findViewById(R.id.d_b_switch);
        generateDetailed();
        updateView();
        dirSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(dirSwitch.isChecked()){
                    generateBrief();
                }
                else{
                    generateDetailed();
                }
                updateView();
            }
        });

    }


    private void updateView(){
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, directionsList);
        listView = (ListView) findViewById(R.id.directions_list);
        listView.setAdapter(adapter);
    }

    public void onNextBtnClick(View view) {
        directionsList.clear();
        dirSwitch.setChecked(false);
        counter+=1;
        generateDetailed();
        updateView();
    }


    public void generateDetailed(){
        directionsList.clear();
        updateView();
        if(counter == shortestVertexOrder.size()-2){
            Button disableNext = (Button) findViewById(R.id.next_button);
            disableNext.setClickable(false);
        }
        String start = shortestVertexOrder.get(counter).id;
        //counter+=1;
        int nextCounter = counter+1;
        String next = shortestVertexOrder.get(nextCounter).id;

        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, start, next);
        int i = 1;

        String currVertex = start;

        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            ZooData.VertexInfo source = vInfo.get(g.getEdgeSource(e).toString());
            ZooData.VertexInfo target = vInfo.get(g.getEdgeTarget(e).toString());
            if (currVertex.equals(target.id)) {
                //swap print statement
                ZooData.VertexInfo temp = target;
                target = source;
                source = temp;
            }

            String direction = String.format("  %d. Walk %.0f meters along %s from '%s' to '%s'.\n",
                    i,
                    g.getEdgeWeight(e),
                    eInfo.get(e.getId()).street,
                    source.name,
                    target.name);
            i++;
            directionsList.add(direction);
            currVertex = target.id;
        }
    }

    public void generateBrief(){
        directionsList.clear();
        String test = "Brief Directions";
        directionsList.add(test);
        updateView();
    }

}