package com.example.project_110;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LocationUpdater {

    private List<VertexInfoStorable> unvisitedExhibits;
    private List<VertexInfoStorable> exhibitsInPlan;
    private List<VertexInfoStorable> visitedExhibits;
    private VertexInfoStorable nextExhibit;
    private Coord currentLocation;
    private LocationModel locationModel;
    private Map<String, ZooData.VertexInfo> vInfo;
    private Map<String, ZooData.EdgeInfo> eInfo;
    private Graph<String, IdentifiedWeightedEdge> graph;
    private Graph<String, CoordinateEdge> coordGraph;





    //construct
    public LocationUpdater(List<VertexInfoStorable> initialExhibits,LocationModel locationModel,
                           Map<String, ZooData.VertexInfo> vInfo,Map<String, ZooData.EdgeInfo> eInfo,
                           Graph<String, IdentifiedWeightedEdge> graph){
        List<VertexInfoStorable> unvisitedExhibits=initialExhibits;
        List<VertexInfoStorable> exhibitsInPlan=initialExhibits;
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

    private ExactLocation findExactLocation(Coord currentLoc){
        Coord currentPosition=currentLoc;
        Coord closestCoord = new Coord(0,0);
        String closestEdge = "";
        Coord targetCoord = null;
        Coord sourceCoord=null;
        for (CoordinateEdge i: coordGraph.edgeSet()){
            for(Coord c: i.getCoords().toArray(Coord[]::new)){
                if (DistanceChecker.getDistance(currentPosition,closestCoord)<DistanceChecker.getDistance(currentPosition,c)){
                    closestCoord = c;
                    closestEdge = i.getEdge().getId();
                    sourceCoord= new Coord(vInfo.get(coordGraph.getEdgeSource(i)).lat,vInfo.get(coordGraph.getEdgeSource(i)).lng);
                    targetCoord = new Coord(vInfo.get(coordGraph.getEdgeTarget(i)).lat,vInfo.get(coordGraph.getEdgeTarget(i)).lng);

                }

            }
        }
        return new ExactLocation(closestEdge,closestCoord,sourceCoord,targetCoord);
    }


    //Thread Method: launch and maintain thread
    public void StartThread(){


        //start thread
        //(while true){

            currentLocation = locationModel.getLastKnownCoords().getValue();
            ExactLocation exactLocation = findExactLocation(currentLocation);





            //iterate through all vertices, all edges
            //if coord of edge/vertex closer than closest
            //closest = new thing










        //}
    }
}
