package com.example.project_110;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

import android.util.Log;
import android.widget.TextView;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

import java.util.ArrayList;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class UpdateDirectionsActivity extends AppCompatActivity {
    private List<String> directionsList = new ArrayList<>();
    private List<String> briefDirectionsList = new ArrayList<>();

    private ArrayAdapter adapter;
    private List<VertexInfoStorable> shortestVertexOrder;
    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;
    private Graph<String, IdentifiedWeightedEdge> g;

    private RouteProgressViewModel viewModel;
    private int counter;
    private boolean restarting;
    private Button mock_button;

    LocationPermissionChecker permissionChecker;
    private LocationModel locationModel;
    ActivityResultLauncher<Intent> activityResultLauncher;
    private ListView listView;
    private Switch dirSwitch;
    private boolean onBrief;

    LocationUpdater locationUpdater;
    private TextView currLocationView;

    private Future<Void> future;
    private ExecutorService backgroundThreadExecutor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("OnCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_directions);

        //Assign instance variables
        {
            restarting = false;

            viewModel = new ViewModelProvider(this)
                    .get(RouteProgressViewModel.class);

            shortestVertexOrder = getIntent().getParcelableArrayListExtra("shortestVertexOrder");
            vInfo = ZooData.loadVertexInfoJSON(this, "zoo_node_new.json");
            eInfo = ZooData.loadEdgeInfoJSON(this, "zoo_edge_new.json");
            g = ZooData.loadZooGraphJSON(this, "zoo_graph_new.json");

            counter = 0;

            onBrief = false;
            // switch
            {
                dirSwitch = (Switch) findViewById(R.id.d_b_switch);
                generateDetailed();
                updateView(directionsList);
                dirSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (dirSwitch.isChecked()) {
                            //doesn't clear directionsList cause generateBrief reuses it
                            onBrief = true;
                            generateBrief();
                            updateView(briefDirectionsList);
                        } else {
                            onBrief = false;
                            directionsList.clear();
                            generateDetailed();
                            updateView(directionsList);
                        }
                    }
                });
            }
        }

        System.out.println("Starting Location Stuff (Permissions)");
        // Location stuff
        {
           permissionChecker = new LocationPermissionChecker(this);
           permissionChecker.ensurePermissions();

           locationModel = new ViewModelProvider(this).get(LocationModel.class);
           LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
           String provider = LocationManager.GPS_PROVIDER;
           locationModel.addLocationProviderSource(locationManager, provider);
        }

        System.out.println("Init activityResultLauncher (for mocking activity)");
        //ActivityResultLauncher for setting mocked location
        {
            activityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            System.out.println("B");
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                String mockLocationString = data.getStringExtra("mockLocation");
                                try {
                                    if (mockLocationString.length() != 0) {
                                        Gson gson = new Gson();
                                        Coord[] mockedCoordsArray = gson.fromJson(mockLocationString, Coord[].class);
                                        List<Coord> mockedCoordsList = new ArrayList<>(Arrays.asList(mockedCoordsArray));
                                        locationModel.removeLocationProviderSource();
                                        locationModel.mockRoute(mockedCoordsList, 10, TimeUnit.SECONDS);
                                        mock_button.setText("Use Real Location");
                                    }
                                    // Log.d("My App", mockLocationJson.toString());
                                } catch (JsonSyntaxException j) {
                                    Log.e("My App", "Could not parse malformed JSON: \"" + mockLocationString + "\"");
                                }
                            }
                        }
                    });
            mock_button = findViewById(R.id.mock_location_button);
        }


        //LOCATION UPDATER THREAD
//         (List<VertexInfoStorable> exhibitsInPlan, String nextExhibit,LocationModel locationModel,
//                                    Map<String, ZooData.VertexInfo> vInfo,Map<String, ZooData.EdgeInfo> eInfo,
//                                    Graph<String, IdentifiedWeightedEdge> graph)
        locationUpdater = new LocationUpdater(shortestVertexOrder, shortestVertexOrder.get(1).id,
                locationModel, vInfo, eInfo, g);


        this.future = backgroundThreadExecutor.submit(() -> {
            while (true) { // TODO: change true to something that makes sense

                try {
                    Coord lastKnownCoords = locationUpdater.locationModel.getLastKnownCoords().getValue();

                    if (lastKnownCoords != null) {
                        locationUpdater.currentLocation = lastKnownCoords;
                        //System.out.println(lastKnownCoords);

                        ExactLocation exactLocation = locationUpdater.findExactLocation(locationUpdater.currentLocation);

                        runOnUiThread(() -> {
                            updateLocationView(locationUpdater.currentLocation.toString());



                            // **** updates directionsList based on exactLocationInfo (US 6) ****
                            {
                                int startNumber = 1;
                                List<String> currDirList = new ArrayList<>();


                                VertexInfoStorable startVert = shortestVertexOrder.get(counter).getParent();
                                //VertexInfoStorable destination = shortestVertexOrder.get(nextCounter);
                                //if ()
                                //**if the distance from currentLocation to startVert is less than 100 then
                                //**skip the special direction stuff below

                                //stores directions to return to edge if more than 100 ft away==========
                                int distancetoEdge = exactLocation.getDistanceToEdge();

                                //System.out.println("distToEdge: " + distancetoEdge);

                                if (distancetoEdge > 100) {
                                    String dir = String.format("  %d. Walk %d feet along %s from '%s' to '%s'.\n",
                                            startNumber,
                                            distancetoEdge,
                                            "the beaten path",
                                            "off-road",
                                            eInfo.get(exactLocation.getEdgeName()).street);
                                    currDirList.add(dir);
                                    startNumber++;
                                }

                                // we have two options: go to edge source or go to edge target.==========
                                // we pick the one that leads us to the goal soonest:
                                //      dijkstras from source to next exhibit
                                //      dijkstras from target to next exhibit
                                //      figure out which path returns shortest distance

                                int nextCounter = counter+1;
                                String next = shortestVertexOrder.get(nextCounter).getParent().id;
                                VertexInfoStorable destination = shortestVertexOrder.get(nextCounter);
                                String edgeSource = exactLocation.getSourceName();
                                String edgeTarget = exactLocation.getTargetName();

                                int edgeSourceDist = exactLocation.getDistanceFromSource();
                                int edgeTargetDist = exactLocation.getDistanceToTarget();

                                //System.out.println("disttoSource: " + edgeSourceDist);
                                //System.out.println("disttoTarget: " + edgeTargetDist);

                                String firstVertex = edgeSource; //<--arbitrary assignment, we change later
                                int distToFirstVertex;

                                //if the closestCoord is close to one of the vertices, we skip
                                //the "run dijkstras at both vertices" section.
                                boolean atFirstVertex = false;
                                if (edgeSourceDist < 100) {
                                    firstVertex = edgeSource;
                                    distToFirstVertex = edgeSourceDist;
                                    atFirstVertex = true;

                                } else if (edgeTargetDist < 100) {
                                    firstVertex = edgeTarget;
                                    distToFirstVertex = edgeTargetDist;
                                    atFirstVertex = true;
                                }

                                if (!atFirstVertex) {
                                    GraphPath<String, IdentifiedWeightedEdge> sourceToNext = DijkstraShortestPath.
                                            findPathBetween(g, edgeSource, next);
                                    GraphPath<String, IdentifiedWeightedEdge> targetToNext = DijkstraShortestPath.
                                            findPathBetween(g, edgeTarget, next);

                                    double distSourcePath = edgeSourceDist;
                                    for(IdentifiedWeightedEdge e : sourceToNext.getEdgeList()) {
                                        distSourcePath += g.getEdgeWeight(e);
                                    }
                                    double distTargetPath = edgeTargetDist;
                                    for(IdentifiedWeightedEdge e : sourceToNext.getEdgeList()) {
                                        distSourcePath += g.getEdgeWeight(e);
                                    }

                                    //GraphPath<String, IdentifiedWeightedEdge> pathToUse;


                                    if (distSourcePath < distTargetPath) {  //source path shorter
                                        firstVertex = edgeSource;
                                        distToFirstVertex = edgeSourceDist;
                                    } else {                                //target path shorter
                                        firstVertex = edgeTarget;
                                        distToFirstVertex = edgeTargetDist;
                                    }

                                    //store direction from closestCoord to first vertex of path==============

                                    if (distToFirstVertex > 100) {
                                        String dir = String.format("  %d. Walk %d feet along %s from '%s' to '%s'.\n",
                                                startNumber,
                                                distToFirstVertex,
                                                eInfo.get(exactLocation.getEdgeName()).street,
                                                "your current location",
                                                vInfo.get(firstVertex).name);
                                        currDirList.add(dir);
                                        startNumber++;
                                    }
                                }



                                //continue printing path (generateDetailed)==============================
                                //---(if switch on brief, generate brief after...

                                //if we were off road/in the middle of an edge, add the 2 dirs to directionsList
                                directionsList.clear();
                                if(!currDirList.isEmpty()) {
                                    for (String dir : currDirList) {
                                        directionsList.add(dir);
                                    }
                                }

                                generateDetailed(startNumber,firstVertex,next,destination);
                                generateBrief();

                                //** ON UI THREAD ** update view (with directionsList or briefDirectionsList
                                runOnUiThread(() -> {
                                    if (!onBrief) {
                                        updateView(directionsList);
                                    }
                                    else {
                                        updateView(briefDirectionsList);
                                    }
                                });

                            }
                        });


                    }




                    /*
                    //TODO: USER STORY 7 REPLAN STUFF
                    List<VertexInfoStorable> updatedVertexOrder = UpdatePathAlgorithm.shortestPath(g, locationUpdater.unvisitedExhibits, exactLocation.getSourceName());
                    //if the newly calculated shortest route order for remaining exhibits is different than what it was before
                    // (changed the value in .equals from exhibitsInPlan to unvisitedExhibits
                    if (!updatedVertexOrder.equals(locationUpdater.unvisitedExhibits)) {
                        //Call Prompt User and pause thread
                        //TODO: make Replan message/button visible, which, when clicked, reconstructs shortestExhibitOrder, and calls update methods
                        wait();
                    }

                    */
                }
                catch (Exception e){
                    Log.d("Thread Error Message: ", "Exception caught in LocationUpdater Thread");
                }

                Thread.sleep(100); //<-- not necessary, the sleep in the Lab was for the countdown timer
            }
        });


        System.out.println("End of OnCreate");
    }

    @Override
    protected void onPause() {
        System.out.println("Pause");
        super.onPause();
        // TODO: change third parameter to what the user is actually seeing once the previous is implemented
        if (!restarting)
            viewModel.storeRouteProgressItem(shortestVertexOrder, counter, counter);
    }

    @Override
    protected void onResume() {
        System.out.println("Resume");
        super.onResume();
        System.out.println(viewModel == null);
        RouteProgressItem item = viewModel.loadRouteProgressItem();
        System.out.println("a");
        if (item != null) {
            System.out.println("Item is not null");
            this.shortestVertexOrder = item.shortestVertexOrder;
            this.counter = item.currDestInd;
            System.out.println("Resume sets counter: " + this.counter);
            directionsList.clear();
            generateDetailed();
            updateView(directionsList);
            //nextDirection();
        }
        System.out.println("b");
        // TODO: set the current view to the direction at index item.currViewInd
    }


    private void updateView(List<String> directions){
        adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, directions);
        listView = (ListView) findViewById(R.id.directions_list);
        listView.setAdapter(adapter);
    }

    private void updateLocationView(String location) {
        currLocationView = (TextView) findViewById(R.id.curr_location_textview);
        currLocationView.setText("Current location: " + location);
    }

    public void onNextBtnClick(View view) {
        directionsList.clear();
        dirSwitch.setChecked(false);
        counter+=1;
        generateDetailed();
        updateView(directionsList);
    }

    //Defaults:
    //startNumber = 1
    //start = vertex of current exhibit in route
    //next = vertex of next exhibit in route
    //destination = vertexinfostorable of next
    public void generateDetailed() {

        String start = shortestVertexOrder.get(counter).getParent().id;
        int nextCounter = counter+1;
        String next = shortestVertexOrder.get(nextCounter).getParent().id;
        VertexInfoStorable destination = shortestVertexOrder.get(nextCounter);

        generateDetailed(1,start,next,destination);
    }

    //int startNumber:  tells what numbered step to start generating directions
    //String start:     vertex id of where to start Dijkstras
    //String next:      vertex id of where to end Dijkstras
    //VertexInfoStorable destination:   VertexInfoStorable with id of next
    public void generateDetailed(int startNumber, String start, String next, VertexInfoStorable destination){
        //directionsList.clear(); <---- these were moved to switch method
        //updateView(directionsList);

        //disables Next button if the counter is on the last exhibit basically
        if(counter == shortestVertexOrder.size()-2){
            Button disableNext = (Button) findViewById(R.id.next_button);
            disableNext.setClickable(false);
        }

        //if the two exhibits share the same parent
        if (start.equals(next)) {
            if (destination.hasParent()) {
                String direction = String.format("  %d. Remain in '%s', and find '%s' inside.\n",
                        1,
                        destination.getParent().name,
                        destination.name);
                directionsList.add(direction);
                return;
            } else {
                String direction = String.format("  %d. You are at '%s'.\n",
                        1,
                        destination.name);
                directionsList.add(direction);
                return;
            }
        }

        GraphPath<String, IdentifiedWeightedEdge> path = DijkstraShortestPath.findPathBetween(g, start, next);
        int i = startNumber;

        String currVertex = start;

        int edgeCount = path.getEdgeList().size();
        int loopCount = 0;

        for (IdentifiedWeightedEdge e : path.getEdgeList()) {


            ZooData.VertexInfo source = vInfo.get(g.getEdgeSource(e).toString());
            ZooData.VertexInfo target = vInfo.get(g.getEdgeTarget(e).toString());
            if (currVertex.equals(target.id)) {
                //swap print statement
                ZooData.VertexInfo temp = target;
                target = source;
                source = temp;
            }

            if (loopCount != edgeCount-1 || !destination.hasParent() ) {
                String direction = String.format("  %d. Walk %.0f feet along %s from '%s' to '%s'.\n",
                        i,
                        g.getEdgeWeight(e),
                        eInfo.get(e.getId()).street,
                        source.name,
                        target.name);
                i++;
                directionsList.add(direction);
                currVertex = target.id;
            } else {
                String direction = String.format("  %d. Walk %.0f feet along %s from '%s' to '%s', and find '%s' inside.\n",
                        i,
                        g.getEdgeWeight(e),
                        eInfo.get(e.getId()).street,
                        source.name,
                        destination.getParent().name,
                        destination.name);
                i++;
                directionsList.add(direction);
                currVertex = target.id;
            }

            loopCount++;
        }

    }

    public void generateBrief(){
//        directionsList.clear();
//        String test = "Brief Directions";
//        directionsList.add(test);
        this.setBrief_Directions_List();
        //updateView(this.briefDirectionsList); <-- moved to switch method
    }

    // sorting a List of List of integers
    // pre-condition : List of integers are of size 1.
    // public List<List<Integer>> sortLL(List<List<Integer>> input) {
    public List<Integer> sortLL(List<List<Integer>> input) {
        TreeSet<Integer> treeSet = new TreeSet<Integer>();
        // List<List<Integer>> output = new ArrayList<>();
        List<Integer> output = new ArrayList<>();

        for (List<Integer> list : input) {
            for (Integer integer : list) {
                treeSet.add(integer);
            }
        }

        for (Integer integer : treeSet) {
            output.add(integer);
        }
        return output;
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

    // returns substring of given parameters
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

        return str_ID;
    }

    // setter for brief directions list
    public void setBrief_Directions_List() {
        // this.brief_Directions_List = brief_Directions_List;
        if (this.directionsList.size() == 1) {
            this.briefDirectionsList = this.directionsList;
            return;
        }

        List<String> tempDetailed_DL = this.directionsList; //detailed_Directions_List; // local copy of list
        List<String> temp_List_ofID = new ArrayList<>(); // ordering parallels detailed_Directions_List
        // temp list of lists of indicies
        // groups indicies of temp_list_ofIDs, by IDs
        List<List<Integer>> temp_List_ofList = new ArrayList<List<Integer>>();

        /* tempDetailed_DL.add("Walk 20 meters along Terrace Lagoon Loop from 'Koi Fish' to 'Front Street / Terrace Lagoon Loop (South)'");
        tempDetailed_DL.add("Walk 30 meters along Front Street from 'Front Street / Terrace Lagoon Loop (South)' to 'Front Street / Treetops Way'.");
        tempDetailed_DL.add("Walk 30 meters along Treetops Way from 'Front Street / Treetops Way' to 'Treetops Way / Fern Canyon Trail'.");
        tempDetailed_DL.add("Walk 30 meters along Treetops Way from 'Treetops Way / Fern Canyon Trail' to 'Treetops Way / Orangutan Trail'.");
        tempDetailed_DL.add("Walk 100 meters along Treetops Way from 'Treetops Way / Orangutan Trail' to 'Treetops Way / Hippo Trail'.");
        tempDetailed_DL.add("Walk 30 meters along Hippo Trail from 'Treetops Way / Hippo Trail' to 'Hippos'.");
        tempDetailed_DL.add("Walk 10 meters along Hippo Trail from 'Hippos' to 'Crocodiles'.");
        Log.d(">> Initial tempDetailed_DL: ", String.valueOf(tempDetailed_DL)); */

        for (String elemStr : tempDetailed_DL) {
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
        // Log.d("<1> tL_IDsize: ", String.valueOf(tL_IDsize));
        // Log.d("<2> tempDetailed_DL: ", String.valueOf(tempDetailed_DL));
        for (int i=0; i<tL_IDsize; i++) {
            List<Integer> tempStrList = new ArrayList<Integer>();
            String toCheck = temp_List_ofID.get(i);          // check if string is within any list
            for (int j=0; j<tL_IDsize; j++) {
                if (toCheck.equals(temp_List_ofID.get(j))) { // found duplicate
                    // add duplicate's index to list of lists
                    tempStrList.add(j);
                    // Log.d("<3> tempStrList: ", String.valueOf(tempStrList));
                    // Log.d("<3> temp_List_ofList: ", String.valueOf(temp_List_ofList));
                }
            }
            temp_List_ofList.add(tempStrList);  // add all indicies of unique string to list of lists
        }

        // eliminate duplicate elements of list
        temp_List_ofList = removeDuplicatesLL(temp_List_ofList);
        // Log.d("<4> temp_List_ofList: ", String.valueOf(temp_List_ofList));

        List<String> tempDetailed_DL_FinalForm = new ArrayList<>();
        //System.out.println("Elements in Temp list of lists: " + temp_List_ofList);
        // now: go through detailed directions list instance, group them by identifiers
        for (List<Integer> intList : temp_List_ofList) { // loop through list of <lists>
            // Log.d("<5> intList: ", String.valueOf(intList.size()));
            if (intList.size() > 1) {                    // if there are multiple directions in a group
                String myStrA = "";
                // Log.d("<6> myStrA (initalized): ", String.valueOf(myStrA));
                Integer externalCounter = 0;
                for (Integer index : intList) {     // go through current <list>
                    // at the indices within <list> // @ detailed directions list of that index
                    // combine the first occurrence and the last occurrence
                    String toModify = tempDetailed_DL.get(index);

                    // take out from A to B
                    // on first element of list // isolate substr from [this] to
                    if (externalCounter==0) {
                        // Log.d("<7> toModify: ", String.valueOf(toModify));
                        myStrA = findSubStr("from",5,"to",toModify); // item we'll insert to final card

                        // Log.d("<8> myStrA: ", String.valueOf(myStrA));

                    } // will modify into last element of list

                    if (externalCounter==intList.size()-1) { // on last loop iteration

                        // locate position 'from [xxx] to' within string
                        String myStrA_toReplace = findSubStr("from",5,"to",toModify); // substring in last card to replace
                        // Log.d("<9> myStrA: ", String.valueOf(myStrA));
                        // Log.d("<10> myStrA_toReplace: ", String.valueOf(myStrA_toReplace));

                        // insert myStrA at located position [xxx] within string
                        String updateWith = tempDetailed_DL.get(index).replace(myStrA_toReplace,myStrA);
                        tempDetailed_DL.set(index,updateWith);  // intList.get(i) should actually be static -- last index
                        // Log.d("<11> updateWith: ", String.valueOf(updateWith));

                    } // last str holds condensed directions
                    externalCounter++;
                } // by here, we've modified the detailed_directions_lists "from A to B"


                int sum = 0; // running sum of meters // condensing into one card
                // want to merge the sum of X meters, and remove unwanted directions cards
                // Log.d("<12> intList: ", String.valueOf(intList));
                Integer CounterExt = 0;
                int lastElem = intList.size();  // the directions card we'll merge everything into
                for (Integer elemInt : intList) {

                    // retrieve directions card
                    String toModify = tempDetailed_DL.get(elemInt);

                    // extract total meters (part of final data)
                    int startIndex = tempDetailed_DL.indexOf("Walk")+5;
                    int endIndex = tempDetailed_DL.indexOf(" feet");
                    //String strMeters = toModify.substring(startIndex,endIndex);
                    //System.out.println(startIndex);
                    String strFeet = findSubStr("Walk",5,"feet",toModify);
                    //System.out.println(strMeters.getClass().getSimpleName());
                    int intFeet = Integer.parseInt(strFeet); // convert to integer
                    //System.out.println(intMeters.getClass().getSimpleName());
                    sum = sum + intFeet; // group dist. (meter) sum

                    // insert sum into tempDetailed_DL at last card
                    if (CounterExt==intList.size()-1) { // on last loop iteration
                        String totFeet = String.valueOf(sum); // convert back to string
                        //System.out.println("totMeters: " + totMeters);
                        String updateWith = tempDetailed_DL.get(elemInt).replace(strFeet,totFeet);
                        tempDetailed_DL.set(elemInt, updateWith);

                        //System.out.println("Check last appearance: " +tempDetailed_DL);
                    }
                    CounterExt++;
                }

                // intList - holds current grouping in view
                // want : destroy everything but the last elem of intList
                // correspondingly in tempDetailed_DL
                // remove all duplicate direction cards -- prevent modifying list when extracting from it (above)
                while (intList.size() > 1) {
                    intList.remove(0);
                } // now intList contains the only index within tempDetailed_DL that needs to be kept

                //System.out.println("point of test");
                //System.out.println(intList);

                //System.out.println("Look here >>>>>>>>>>>>>>>>>5");
                //System.out.println(tempDetailed_DL.size());
            }

        } // temp_List_ofList is now of the form [[int],[int],etc...]
        // tempDetailed_DL now contains modified detailed list, where last direction cards are condensed directions

        // now: Go through tempDetailed_DL and remove all indicies not included within temp_List_ofList

        // convert temp_List_ofList of integers into just a list of integers
        List<Integer> listOfIndicies = sortLL(temp_List_ofList);

        /* System.out.println("Look here >>>>>>>>>>>>>>>>>12312312321");
        System.out.println(listOfIndicies);
        System.out.println("Look here >>>>>>>>>>>>>>>>>12312312321"); */

        // loop through list of indicies, remove all corresponding elements of such indicies within tempDetailed_DL
        List<String> return_briefDirections = new ArrayList<>();
        for(int j = listOfIndicies.size()-1; j>=0; j--){
            for(int i = tempDetailed_DL.size()-1; i>=0; i--){
                if(listOfIndicies.get(j) == i){
                    return_briefDirections.add(tempDetailed_DL.get(i));
                }
            }
        } // return_briefDirections contains desired brief directions (in reverse order)
        Collections.reverse(return_briefDirections); // reverse to obtain actual directions list

        //System.out.println("Look here >>>>>>>>>>>>>>>>>6");
        //log.d("this is final return: ", String.valueof(return_briefDirections));

        //fixes numbers in front of direction steps

        for (int i = 0; i < return_briefDirections.size(); i++ ) {
            String tempString = return_briefDirections.get(i);
            String substring = tempString.substring(3); // <-- substring starting after number
            String newString = "  " + (i+1) + substring;
            return_briefDirections.set(i, newString);
        }

        this.briefDirectionsList = return_briefDirections;

    } // end of brief_directionsList implementation



    public void onClearPlanBtnClick(View view) {
        restarting = true;
        System.out.println("onClearPlanBtnClick");
        viewModel.clearAllItems();
        Intent i=new Intent(this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void onMockLocationBtnClick(View view) {
        if (locationModel.isUsingMockedCoords()) {
             locationModel.useRealCoords();
             mock_button.setText("Mock Location");
        } else {
            Intent intent = new Intent(this, MockLocationActivity.class);
            activityResultLauncher.launch(intent);
        }
    }

    public void onPreviousBtnClick(View view) {
        if (counter > 0) {
            directionsList.clear();
            dirSwitch.setChecked(false);
            counter-=1;
            generateDetailed();
            updateView(directionsList);
            Button disableNext = (Button) findViewById(R.id.next_button);
            disableNext.setClickable(true);
        }
    }

    public void onSkipBtnClick(View view) {
    }
}