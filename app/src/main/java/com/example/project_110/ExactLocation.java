package com.example.project_110;

import java.lang.annotation.Target;

public class ExactLocation {
    String edgeName,sourceName,targetName;
    Coord coord,source,target;

    public ExactLocation(String edgeName, Coord coord,Coord source, Coord target,String sourceName,String targetName) {
        this.edgeName = edgeName;
        this.coord = coord;
        this.source=source;
        this.target=target;
        this.sourceName=sourceName;
        this.targetName=targetName;
    }

    public String getEdgeName() {
        return edgeName;
    }

    public Coord getCoord() {
        return coord;
    }

    public String getSourceName(){
        return sourceName;
    }

    public String getTargetName(){
        return targetName;
    }

    public double getDistanceFromSource(){return DistanceChecker.getDistance(source,coord);}

    public double getDisanceToTarget(){return DistanceChecker.getDistance(target, coord);}






}
