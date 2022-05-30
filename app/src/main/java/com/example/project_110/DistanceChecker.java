package com.example.project_110;

public class DistanceChecker {

    public static double getDistance(Coord c1, Coord c2){
        return Math.sqrt((c1.lat-c2.lat)*(c1.lat-c2.lat)+(c1.lng-c2.lng)*(c1.lng-c2.lng));
    }


}
