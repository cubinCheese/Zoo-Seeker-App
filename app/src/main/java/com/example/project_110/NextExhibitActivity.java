package com.example.project_110;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NextExhibitActivity extends AppCompatActivity {
    private List<String> detailed_Directions_List;
    private List<String> brief_Directions_List;
    private ArrayAdapter adapter;
    private List<VertexInfoStorable> shortestVertexOrder;
    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;
    private Graph<String, IdentifiedWeightedEdge> g;
    private int counter;
    private String elemStr;

    public NextExhibitActivity() {

    }

    // brief directions list constructor
    public NextExhibitActivity(List<String> brief_directions_list) {
        brief_Directions_List = brief_directions_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_exhibit);

        shortestVertexOrder = getIntent().getParcelableArrayListExtra("shortestVertexOrder");
        vInfo = ZooData.loadVertexInfoJSON(this, "sample_node_info.json");
        eInfo = ZooData.loadEdgeInfoJSON(this, "sample_edge_info.json");
        g = ZooData.loadZooGraphJSON(this, "sample_zoo_graph.json");

        counter = 0;

        String start = shortestVertexOrder.get(counter).id;
        counter +=1;
        String next = shortestVertexOrder.get(counter).id;

        // generate shortest path from start to first exhibit
        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, start, next);
        int i = 1;
        detailed_Directions_List = new ArrayList<>();
        for (IdentifiedWeightedEdge e : path.getEdgeList()) {
            String direction = String.format("  %d. Walk %.0f meters along %s from '%s' to '%s'.\n",
                    i,
                    g.getEdgeWeight(e),
                    eInfo.get(e.getId()).street,
                    vInfo.get(g.getEdgeSource(e).toString()).name,
                    vInfo.get(g.getEdgeTarget(e).toString()).name);
            i++;
            detailed_Directions_List.add(direction);
        }
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, detailed_Directions_List);
        ListView listView = (ListView) findViewById(R.id.directions_list);
        listView.setAdapter(adapter);


    }

    public void onNextBtnClick(View view) {
        detailed_Directions_List.clear();
        if(counter == shortestVertexOrder.size()-2){
            Button disableNext = (Button) findViewById(R.id.next_button);
            disableNext.setClickable(false);
        }
        String start = shortestVertexOrder.get(counter).id;
        counter+=1;
        String next = shortestVertexOrder.get(counter).id;

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
            detailed_Directions_List.add(direction);
            currVertex = target.id;
        }
        setBrief_Directions_List();
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, brief_Directions_List); // detailed_Directions_List
        ListView listView = (ListView) findViewById(R.id.directions_list);
        listView.setAdapter(adapter);


        System.out.println(getBrief_Directions_List());
    }

    private void lastExhibit() {
    }

    // getter for brief directions list
    public List<String> getBrief_Directions_List() {
        return brief_Directions_List;
    }

    // function to remove duplicates from List of List of integers
    public List<List<Integer>> removeDuplicatesLL(List<List<Integer>> input) {
        List<List<Integer>> outputLL = new ArrayList<>();
        HashMap<List<Integer>, Integer> tempMap = new HashMap<>(); // List, counter (unused)

        // MANIPULATE dictionaries to eliminate duplicates
        for (List<Integer> list_ofInt : input) {
            tempMap.put(list_ofInt,0);
        }
        for (Map.Entry<List<Integer>,Integer> mapElement : tempMap.entrySet()) {
            List<Integer> uniqueList = mapElement.getKey();
            outputLL.add(uniqueList);
        }
        return outputLL;
    }

    public String findSubStr(String start, int offset, String end, String toExtractFrom) {
        // input: string, offset, string, (String to take from) <-- can refactor as class
        // output: substr from string to string (exclusive)
        // called on: a string

        // this.elemStr = elemStr;

        // extract startIndex [this substr] endIndex
        int startIndex = toExtractFrom.indexOf(start)+offset; // starting index of string ("along")
        // offset 5 for 'along', offset 1 for space
        int endIndex = toExtractFrom.indexOf(" "+end);         // ending index of string ("from")
        String str_ID = toExtractFrom.substring(startIndex, endIndex); // along [str_ID]

        /*
        // extract startIndex [this substr] endIndex
        int startIndex = elemStr.indexOf("along")+6; // starting index of string ("along")
        // offset 5 for 'along', offset 1 for space
        int endIndex = elemStr.indexOf(" from");      // ending index of string ("from")
        String str_ID = elemStr.substring(startIndex, endIndex); // along [str_ID]
         */

        return str_ID;
    }

    // setter for brief directions list
    public void setBrief_Directions_List() {
        // this.brief_Directions_List = brief_Directions_List;

        List<String> tempDetailed_DL = new ArrayList<>(); //detailed_Directions_List; // local copy of list
        tempDetailed_DL.add("Walk 20 meters along Terrace Lagoon Loop from 'Koi Fish' to 'Front Street / Terrace Lagoon Loop (South)'");

        tempDetailed_DL.add("Walk 30 meters along Front Street from 'Front Street / Terrace Lagoon Loop (South)' to 'Front Street / Treetops Way'.");
        tempDetailed_DL.add("Walk 30 meters along Treetops Way from 'Front Street / Treetops Way' to 'Treetops Way / Fern Canyon Trail'.");
        tempDetailed_DL.add("Walk 30 meters along Treetops Way from 'Treetops Way / Fern Canyon Trail' to 'Treetops Way / Orangutan Trail'.");
        tempDetailed_DL.add("Walk 100 meters along Treetops Way from 'Treetops Way / Orangutan Trail' to 'Treetops Way / Hippo Trail'.");
        tempDetailed_DL.add("Walk 30 meters along Hippo Trail from 'Treetops Way / Hippo Trail' to 'Hippos'.");
        tempDetailed_DL.add("Walk 10 meters along Hippo Trail from 'Hippos' to 'Crocodiles'.");

        //System.out.println("Look here >>>>>>>>>>>>>>>>>");
        //System.out.println(tempDetailed_DL);
        List<String> temp_List_ofID = new ArrayList<>(); // ordering parallels detailed_Directions_List
                                                         // temp list of lists of indicies
        // groups indicies of temp_list_ofIDs, by IDs
        List<List<Integer>> temp_List_ofList = new ArrayList<List<Integer>>();

        // declare list -- algo goes here
        for (String elemStr : tempDetailed_DL) { // make callable function

            /*
            // extract startIndex [this substr] endIndex
            int startIndex = elemStr.indexOf("along")+6; // starting index of string ("along")
                                                         // offset 5 for 'along', offset 1 for space
            int endIndex = elemStr.indexOf(" from");      // ending index of string ("from")
            */
            String str_ID = findSubStr("along",6,"from",elemStr); // along [str_ID]

            temp_List_ofID.add(str_ID);
        }
        // for (String str_ID : temp_List_ofID)
        // have: [A,B,A,A,C]
        // want: [[0],[1],[2,3],[4]]
        Integer tL_IDsize = temp_List_ofID.size();
        //List<Boolean> bool_List_ofID = new ArrayList<Boolean>(Arrays.asList(new Boolean[tL_IDsize])); // parallels temp_List_ofID
        //Collections.fill(bool_List_ofID, Boolean.FALSE);
        //System.out.println("Look here >>>>>>>>>>>>>>>>>");
        //System.out.println(tL_IDsize);
        //System.out.println(tempDetailed_DL);
        for (int i=0; i<tL_IDsize; i++) {
            List<Integer> tempStrList = new ArrayList<Integer>();
            String toCheck = temp_List_ofID.get(i); // check if string is within any list
            for (int j=0; j<tL_IDsize; j++) {
                if (toCheck.equals(temp_List_ofID.get(j))) { // found duplicate
                    // add duplicate's index to list of lists
                    //if ()

                    tempStrList.add(j);
                    /*System.out.println("Look here >>>>>>>>>>>>>>>>>");
                    System.out.println(tempStrList);
                    System.out.println(temp_List_ofList);*/

                }
            }
            temp_List_ofList.add(tempStrList);  // add all indicies of unique string to list of lists

        } //[[0,1,2,3,4,5],[0,1,2,3,4,5],[0,1,2,3,4,5],[0,1,2,3,4,5],[0,1,2,3,4,5],[0,1,2,3,4,5]]
        temp_List_ofList = removeDuplicatesLL(temp_List_ofList);

        /*
        for (List<Integer> list : temp_List_ofList) {
            Boolean cond = list.isEmpty();
            if (cond == Boolean.TRUE)
                temp_List_ofList.remove(list);
            System.out.println(temp_List_ofList);
        }
        System.out.println("Look here >>>>>>>>>>>>>>>>>");
        System.out.println(temp_List_ofList);
        */


        // now: go through detailed directions list instance, group them by identifiers
        for (List<Integer> intList : temp_List_ofList) { // loop through list of <lists>
            System.out.println("Look here >>>>>>>>>>>>>>>>>2");
            //System.out.println(intList.size());
            if (intList.size() > 1) {                    // if there are duplicates
                String myStrA = "";
                System.out.println("Look here >>>>>>>>>>>>>>>>>3");
                for (int i = 0; i < intList.size(); i++) { // go through current <list>
                    // at the indices within <list> // @ detailed directions list of that index
                    // combine the first occurrence and the last occurrence
                    String toModify = tempDetailed_DL.get(i);

                    // take out from A to B
                    // on first element of list // isolate substr from [this] to
                    if (i==0) {

                        /*
                        int startIndex = toModify.indexOf("from")+5;
                        int endIndex = toModify.indexOf(" to");
                        myStrA = toModify.substring(startIndex,endIndex); */

                        myStrA = findSubStr("from",5,"to",toModify);

                    } // will modify into last element of list

                    if (i==intList.size()-1) { // on last loop iteration

                        /* int startIndex = toModify.indexOf("from")+5;
                        int endIndex = toModify.indexOf(" to");
                        String myStrA_toReplace = toModify.substring(startIndex,endIndex); */
                        String myStrA_toReplace = findSubStr("from",5,"to",toModify);

                        String updateWith = tempDetailed_DL.get(intList.get(i)).replace(myStrA_toReplace,myStrA);
                        tempDetailed_DL.set(intList.get(i),updateWith); // intList.get(i) should actually be static -- last index
                    } // last str holds condensed directions

                } // by here, we've modified the detailed_directions_lists "from A to B"
                int sum = 0; // running sum of meters // condensing into one card
                // want to merge the sum of X meters, and remove unwanted directions cards
                System.out.println("Look here >>>>>>>>>>>>>>>>>4");
                for (Integer i : intList) {
                    String toModify = tempDetailed_DL.get(i);


                    int startIndex = tempDetailed_DL.indexOf("Walk")+5;
                    int endIndex = tempDetailed_DL.indexOf(" meters");
                    /*String strMeters = toModify.substring(startIndex,endIndex); */
                    String strMeters = findSubStr("Walk",5,"meters",toModify);
                    int intMeters = Integer.parseInt(strMeters); // convert to integer

                    sum = sum + intMeters; // group dist. (meter) sum

                    String totMeters = String.valueOf(sum); // convert back to string

                    // remove all duplicates
                    String myStrMeters_toReplace = toModify.substring(startIndex,endIndex);
                    System.out.print((myStrMeters_toReplace));
                    tempDetailed_DL.get(intList.get(i)).replace(myStrMeters_toReplace,totMeters);
                    // now remove all but last element of list and add to brief_directions_List
                    brief_Directions_List.add(tempDetailed_DL.get(intList.get(i)));
                    //System.out.println("Look here >>>>>>>>>>>>>>>>>");
                    //System.out.println(tempDetailed_DL.get(0));
                    Log.d("Look here >>>>>>>>>>", String.valueOf(tempDetailed_DL.size()));

                }
                //System.out.println(tempDetailed_DL.size());
            }

            /*System.out.println("----------------------------------------------------------------\n\n");
            System.out.println(brief_Directions_List);

            else // List only has one index // don't need to combine any directions cards // is a unique directions card
                continue

             */

        }
    }

} // end of nextBtnClk