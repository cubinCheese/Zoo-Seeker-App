package com.example.project_110;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ItemCountTest {




    @Test
   public void itemCountTest(){

        SelectedDisplayAdapter selectedDisplayAdapter = new SelectedDisplayAdapter();
        List<VertexInfoStorable> addList = new ArrayList<VertexInfoStorable>();
        addList.add(new VertexInfoStorable(
                new ZooData.VertexInfo("grizz","", ZooData.VertexInfo.Kind.EXHIBIT, "grizz", new ArrayList<String>(),0.0,0.0)));

        selectedDisplayAdapter.setSelectedExhibits(addList);
        assertEquals(selectedDisplayAdapter.getItemCount(),1);

    }


    @Test
    public void removeCountTest(){

        SelectedDisplayAdapter selectedDisplayAdapter = new SelectedDisplayAdapter();
        List<VertexInfoStorable> addList = new ArrayList<VertexInfoStorable>();
        addList.add(new VertexInfoStorable(
                new ZooData.VertexInfo("grizz","", ZooData.VertexInfo.Kind.EXHIBIT, "grizz", new ArrayList<String>(),0.0,0.0)));

        selectedDisplayAdapter.setSelectedExhibits(addList);
        selectedDisplayAdapter.setSelectedExhibits(Collections.emptyList());
        assertEquals(selectedDisplayAdapter.getItemCount(),0);
    }



}
