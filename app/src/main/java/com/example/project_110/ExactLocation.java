package com.example.project_110;

public class ExactLocation {
    String edgeName;
    Coord coord,source,target;

    public ExactLocation(String edgeName, Coord coord,Coord source, Coord target) {
        this.edgeName = edgeName;
        this.coord = coord;
        this.source=source;
        this.target=target;
    }

    public String getEdgeName() {
        return edgeName;
    }

    public Coord getCoord() {
        return coord;
    }

    public double getDistanceFromSource(){return DistanceChecker.getDistance(source,coord);}

    public double getDisanceToTarget(){return DistanceChecker.getDistance(target, coord);}






}
