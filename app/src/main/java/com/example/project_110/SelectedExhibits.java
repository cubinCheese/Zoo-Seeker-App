package com.example.project_110;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class SelectedExhibits {

    List<ZooData.VertexInfo> selectedExhibits;

    public SelectedExhibits(){
        selectedExhibits = new ArrayList<>();
    }

    public SelectedExhibits(List<ZooData.VertexInfo> selectedExhibits){
        this.selectedExhibits=selectedExhibits;
    }





}
