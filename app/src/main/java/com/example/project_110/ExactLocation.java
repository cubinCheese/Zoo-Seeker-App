package com.example.project_110;

import java.lang.annotation.Target;

public class ExactLocation {
    String edgeName,sourceName,targetName;
    Coord realCoord, closestCoord,source,target;

    public ExactLocation(String edgeName, Coord realCoord, Coord closestCoord,Coord source, Coord target,String sourceName,String targetName) {
        this.realCoord = realCoord;
        this.edgeName = edgeName;
        this.closestCoord = closestCoord;
        this.source=source;
        this.target=target;
        this.sourceName=sourceName;
        this.targetName=targetName;
    }

    public String getEdgeName() {
        return edgeName;
    }

    public Coord getRealCoord() {return realCoord;}

    public Coord getClosestCoord() {
        return closestCoord;
    }

    public String getSourceName(){
        return sourceName;
    }

    public String getTargetName(){
        return targetName;
    }

    public int getDistanceFromSource(){return DistanceChecker.getDistance(source,closestCoord);}

    public int getDistanceToTarget(){return DistanceChecker.getDistance(target, closestCoord);}

    public int getDistanceToEdge() {return DistanceChecker.getDistance(realCoord, closestCoord);}




}
