package com.example.project_110;

import android.util.Log;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LocationUpdater extends Thread{

    private List<VertexInfoStorable> unvisitedExhibits;
    private List<VertexInfoStorable> exhibitsInPlan;
    private List<VertexInfoStorable> visitedExhibits;
    private String nextExhibit;
    private Coord currentLocation;
    private LocationModel locationModel;
    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;
    private Graph<String, IdentifiedWeightedEdge> graph;
    private Graph<String, CoordinateEdge> coordGraph;





    //construct
    public LocationUpdater(List<VertexInfoStorable> exhibitsInPlan, String nextExhibit,LocationModel locationModel,
                           Map<String, ZooData.VertexInfo> vInfo,Map<String, ZooData.EdgeInfo> eInfo,
                           Graph<String, IdentifiedWeightedEdge> graph){
        unvisitedExhibits=Collections.EMPTY_LIST;
        visitedExhibits=Collections.EMPTY_LIST;
        this.exhibitsInPlan=exhibitsInPlan;

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
        coordGraph = new DefaultUndirectedWeightedGraph<>(CoordinateEdge.class);

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
             sourceCoord = new Coord(sourceInfo.lat,sourceInfo.lng);
             targetCoord = new Coord(targetInfo.lat, targetInfo.lng);
            coordGraph.addEdge(source,target,
                    new CoordinateEdge(i, Coords.interpolate(sourceCoord,targetCoord,100)));
        }


    }
    private void updateVisitedExhibits(){
        unvisitedExhibits=Collections.EMPTY_LIST;
        visitedExhibits=Collections.EMPTY_LIST;
        boolean beginAdding = false;
        for(VertexInfoStorable i: exhibitsInPlan){
            if(i.id.equals(nextExhibit)){
                beginAdding=true;
            }
            if(beginAdding)unvisitedExhibits.add(i);
            else visitedExhibits.add(i);
        }
    }


    public void updateNextExhibit(String nextExhibit){

        this.nextExhibit=nextExhibit;
        updateVisitedExhibits();


    }

    private ExactLocation findExactLocation(Coord currentLoc){
        Coord currentPosition=currentLoc;
        Coord closestCoord = new Coord(0,0);
        String closestEdge = "";
        Coord targetCoord = null;
        Coord sourceCoord=null;
        String sourceName = "";
        String targetName="";
        for (CoordinateEdge i: coordGraph.edgeSet()){
            for(Coord c: i.getCoords().toArray(Coord[]::new)){
                if (DistanceChecker.getDistance(currentPosition,closestCoord)<DistanceChecker.getDistance(currentPosition,c)){
                    closestCoord = c;
                    closestEdge = i.getEdge().getId();
                    sourceCoord= new Coord(vInfo.get(coordGraph.getEdgeSource(i)).lat,vInfo.get(coordGraph.getEdgeSource(i)).lng);
                    targetCoord = new Coord(vInfo.get(coordGraph.getEdgeTarget(i)).lat,vInfo.get(coordGraph.getEdgeTarget(i)).lng);
                    sourceName = graph.getEdgeSource(i.getEdge());
                    targetName = graph.getEdgeTarget(i.getEdge());

                }

            }
        }
        return new ExactLocation(closestEdge,closestCoord,sourceCoord,targetCoord,sourceName,targetName);
    }


    public ExactLocation getExactLocation(){
        return findExactLocation(currentLocation);
    }

    public void updateExhibitOrder(List<VertexInfoStorable> updatedExhibitOrder){
        exhibitsInPlan = updatedExhibitOrder;
        updateVisitedExhibits();
    }

    //Thread Method: launch and maintain thread
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
                    tempGraph.setEdgeWeight(i, exactLocation.getDisanceToTarget());
                }
            }
            List<VertexInfoStorable> updatedVertexOrder = UpdatePathAlgorithm.shortestPath(tempGraph, unvisitedExhibits, exactLocation.getSourceName());
            if (!updatedVertexOrder.equals(exhibitsInPlan)) {
                //Call Prompt User and pause thread
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
