package com.example.project_110;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class getDistanceTest {


    @Test
    public void DistanceTest(){
        Coord c1 = new Coord(0,0);
        Coord c2 = new Coord(0,1);
        assertEquals(307600.0, DistanceChecker.getDistance(c1,c2),.0001);
        c2 = new Coord(10,10);
        assertEquals(4764000.0,DistanceChecker.getDistance(c1,c2),0.001);


    }

}
