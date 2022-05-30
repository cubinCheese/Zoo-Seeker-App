package com.example.project_110;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.stream.Stream;

public class CoordinateEdge  {


    private IdentifiedWeightedEdge edge;
    private Stream<Coord> coords;


    public CoordinateEdge(IdentifiedWeightedEdge edge,Stream<Coord> coords){
        this.edge=edge;
        this.coords=coords;
    }


    public Stream<Coord> getCoords(){
        return coords;
    }

    public IdentifiedWeightedEdge getEdge() {
        return edge;
    }



}
