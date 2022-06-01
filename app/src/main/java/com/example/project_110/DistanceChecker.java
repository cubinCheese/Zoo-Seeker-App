package com.example.project_110;

public class DistanceChecker {

    //since lat and long conversion to feet are not the same, we updated this
    //method to return in feet. this also converts for us
    public static int getDistance(Coord c1, Coord c2){

        int BASE = 100;
        double DEG_LAT_IN_FT = 363843.57;
        double DEG_LNG_IN_FT = 307515.50;

        double d_lat = Math.abs(c1.lat - c2.lat);
        double d_lng = Math.abs(c1.lng - c2.lng);

        double d_ft_v = d_lat * DEG_LAT_IN_FT;
        double d_ft_h = d_lng * DEG_LNG_IN_FT;

        double dist_ft = Math.sqrt(Math.pow(d_ft_h, 2) + Math.pow(d_ft_v, 2));
        int dist_ft_rounded = (int) (BASE * Math.ceil(dist_ft/BASE));
        return dist_ft_rounded;
        //return Math.sqrt((c1.lat-c2.lat)*(c1.lat-c2.lat)+(c1.lng-c2.lng)*(c1.lng-c2.lng));
    }


}
