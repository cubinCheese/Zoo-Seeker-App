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
@RunWith(AndroidJUnit4.class)
public class addCountTest {
    private VertexInfoStorableDao dao;
    private VertexDatabase db;

    @Before
    public void createDb(){
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, VertexDatabase.class)
                .allowMainThreadQueries()
                .build();
        dao = db.vertexInfoDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();

    }

    /*
    @Test
    public void addCountTest(){


            VertexInfoStorable grizz = new VertexInfoStorable(
                    new ZooData.VertexInfo("grizz", ZooData.VertexInfo.Kind.EXHIBIT, "grizz", new ArrayList<String>()));
            dao.insert(grizz);
           assertEquals(dao.getAll().size(), 1);


    }
    */

    @Test
    public void addCountTest(){
        ActivityScenario<SearchDisplayActivity> scenario = ActivityScenario.launch((SearchDisplayActivity.class));
        scenario.onActivity(activity -> {
            VertexInfoStorable grizz = new VertexInfoStorable(
                    new ZooData.VertexInfo("grizz", ZooData.VertexInfo.Kind.EXHIBIT, "grizz", new ArrayList<String>()));
            dao.insert(grizz);
            try{
                //Thread.sleep(200);
            }catch (Exception e){

            }
            TextView numberDisplay = (TextView) activity.findViewById(R.id.selected_exhibit_count);
            String toParse = "" + numberDisplay.getText();
            String[] splited = toParse.split("\\s+");
            int numExhibits = Integer.parseInt(splited[2]);
            assertEquals(numExhibits, activity.getSelectedExhibitsList().size());

        });
    }

}
