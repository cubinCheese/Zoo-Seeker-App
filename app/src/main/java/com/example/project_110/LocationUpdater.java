package com.example.project_110;

import android.util.Log;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LocationUpdater extends Thread{

    //changed these to public so UpdateDirectionsActivity can access easily
    public List<VertexInfoStorable> unvisitedExhibits;
    public List<VertexInfoStorable> exhibitsInPlan;
    public List<VertexInfoStorable> visitedExhibits;
    public String nextExhibit;
    public Coord currentLocation;
    public LocationModel locationModel;
    public Map<String, ZooData.VertexInfo> vInfo;
    public Map<String, ZooData.EdgeInfo> eInfo;
    public Graph<String, IdentifiedWeightedEdge> graph;
    //public Graph<String, CoordinateEdge> coordGraph; <-- replaced with map below
    public Map<IdentifiedWeightedEdge, List<Coord>> edgeIDtoCoordStream;




    //construct
    public LocationUpdater(List<VertexInfoStorable> exhibitsInPlan, String nextExhibit,LocationModel locationModel,
                           Map<String, ZooData.VertexInfo> vInfo,Map<String, ZooData.EdgeInfo> eInfo,
                           Graph<String, IdentifiedWeightedEdge> graph){
        unvisitedExhibits = new ArrayList<>();
        visitedExhibits = new ArrayList<>();
        edgeIDtoCoordStream = new HashMap<>();
        this.exhibitsInPlan=exhibitsInPlan;

        //fills unvisitedExhibits and visitedExhibits lists based on nextExhibit
        boolean beginAdding = false;
        for(VertexInfoStorable i: exhibitsInPlan){
            if(i.id.equals(nextExhibit)){
                beginAdding=true;
            }
            if(beginAdding)unvisitedExhibits.add(i);
            else visitedExhibits.add(i);
        }

        this.nextExhibit=nextExhibit;
        List<VertexInfoStorable> visitedExhibits;
        this.locationModel = locationModel;
        this.eInfo=eInfo;
        this.vInfo=vInfo;
        this.graph=graph;
        //coordGraph = new DefaultUndirectedWeightedGraph<>(CoordinateEdge.class);

        String source;
        String target;
        ZooData.VertexInfo sourceInfo;
        ZooData.VertexInfo targetInfo;
        Coord sourceCoord;
        Coord targetCoord;
        //get coord streams for each edge in graph
        for (IdentifiedWeightedEdge i : graph.edgeSet()){
             source = graph.getEdgeSource(i);
             target = graph.getEdgeTarget(i);
             sourceInfo = vInfo.get(source);
             targetInfo = vInfo.get(target);
             System.out.println(source + " " + target);
             sourceCoord = new Coord(sourceInfo.lat,sourceInfo.lng);
             targetCoord = new Coord(targetInfo.lat, targetInfo.lng);
            System.out.println(sourceCoord + " " + targetCoord);

            Stream<Coord> coordStream = Coords.interpolate(sourceCoord,targetCoord,100);
            List<Coord> coordList = coordStream.collect(Collectors.toList());
            edgeIDtoCoordStream.put(i,coordList);
             //coordGraph.addEdge(source,target,
             //       new CoordinateEdge(i, Coords.interpolate(sourceCoord,targetCoord,100)));
        }


    }


    //fills unvisitedExhibits and visitedExhibits lists based on new nextExhibit or new ExhibitOrder (below)
    private void updateVisitedExhibits(){
        unvisitedExhibits=new ArrayList<>();
        visitedExhibits=new ArrayList<>();
        boolean beginAdding = false;
        for(VertexInfoStorable i: exhibitsInPlan){
            if(i.id.equals(nextExhibit)){
                beginAdding=true;
            }
            if(beginAdding)unvisitedExhibits.add(i);
            else visitedExhibits.add(i);
        }
    }
    public void updateExhibitOrder(List<VertexInfoStorable> updatedExhibitOrder){
        exhibitsInPlan = updatedExhibitOrder;
        updateVisitedExhibits();
    }
    public void updateNextExhibit(String nextExhibit){
        this.nextExhibit=nextExhibit;
        updateVisitedExhibits();
    }


    // Takes current location and finds closest Coord on edge, which is ExactLocation obj
    public ExactLocation findExactLocation(Coord currentLoc){
        Coord closestCoord = new Coord(Double.MAX_VALUE,Double.MAX_VALUE);
        String closestEdge = "";
        Coord targetCoord = null;
        Coord sourceCoord=null;
        String sourceName = "";
        String targetName="";
        //for (CoordinateEdge i: coordGraph.edgeSet()){
        for (IdentifiedWeightedEdge i: edgeIDtoCoordStream.keySet()) {
            for(Coord c: edgeIDtoCoordStream.get(i)){
                //If Coord c is closer to current location than closestCoord, update closestCoord.
                //(was previously less than (<), we think that was a mistake)
                if (DistanceChecker.getDistance(currentLoc,closestCoord) > DistanceChecker.getDistance(currentLoc,c)){
                    closestCoord = c;
                    closestEdge = i.getId();
                    sourceCoord= new Coord(vInfo.get(graph.getEdgeSource(i)).lat,vInfo.get(graph.getEdgeSource(i)).lng);
                    targetCoord = new Coord(vInfo.get(graph.getEdgeTarget(i)).lat,vInfo.get(graph.getEdgeTarget(i)).lng);
                    sourceName = graph.getEdgeSource(i);
                    targetName = graph.getEdgeTarget(i);

                }

            }
        }
        return new ExactLocation(closestEdge,currentLoc,closestCoord,sourceCoord,targetCoord,sourceName,targetName);
    }


    public ExactLocation getExactLocation(){
        return findExactLocation(currentLocation);
    }

    //Thread Method: launch and maintain thread
    //NOTE: Instead of directly calling this method, we copied the code into the thread in
    // UpdateDirectionsActivity. We did this cause we need part of this to be on the UI thread.
    public void run(){

        //start thread
        //(while true){
        try {

            currentLocation = locationModel.getLastKnownCoords().getValue();
            ExactLocation exactLocation = findExactLocation(currentLocation);
            Graph<String, IdentifiedWeightedEdge> tempGraph = graph;
            //Done in loop below
            //tempGraph.setEdgeWeight(exactLocation.getSourceName(), exactLocation.getTargetName(), exactLocation.getDisanceToTarget());
            for (IdentifiedWeightedEdge i : tempGraph.edgesOf(exactLocation.getSourceName())) {
                if (!i.getId().equals(exactLocation.edgeName)) {
                    tempGraph.setEdgeWeight(i, tempGraph.getEdgeWeight(i) + exactLocation.getDistanceFromSource());
                }
                if (i.getId().equals(exactLocation.edgeName)) {
                    tempGraph.setEdgeWeight(i, exactLocation.getDistanceToTarget());
                }
            }
            List<VertexInfoStorable> updatedVertexOrder = UpdatePathAlgorithm.shortestPath(tempGraph, unvisitedExhibits, exactLocation.getSourceName());

            //if the newly calculated shortest route order for remaining exhibits is different than what it was before
            // (changed the value in .equals from exhibitsInPlan to unvisitedExhibits
            if (!updatedVertexOrder.equals(unvisitedExhibits)) {
                //Call Prompt User and pause thread
                //TODO: make Replan message/button visible, which, when clicked, reconstructs shortestExhibitOrder, and calls update methods

                wait();
            }
        }
        catch (Exception e){
            Log.d("Thread Error Message: ", "Exception caught in LocationUpdater Thread");
        }


            //iterate through all vertices, all edges
            //if coord of edge/vertex closer than closest
            //closest = new thing

        //}
    }
}
