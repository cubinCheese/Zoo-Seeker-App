package com.example.project_110;

import org.junit.Test;
import static org.junit.Assert.*;

public class getDistanceTest {


    @Test
    public void DistanceTest(){
        Coord c1 = new Coord(0,0);
        Coord c2 = new Coord(0,1);
        assertEquals(1, DistanceChecker.getDistance(c1,c2),.0001);
        c2 = new Coord(10,10);
        assertEquals(14.14213,DistanceChecker.getDistance(c1,c2),0.001);


    }

}
