package com.example.project_110;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import android.content.Context;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.room.Room;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class SelectedNumberTest {
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


    @Test
    public void displayedCountIsCorrect() {
        ActivityScenario<SearchDisplayActivity> scenario = ActivityScenario.launch((SearchDisplayActivity.class));

        scenario.onActivity(activity -> {
            TextView numberDisplay = (TextView) activity.findViewById(R.id.selected_exhibit_count);
            String toParse = "" + numberDisplay.getText();
            String[] splited = toParse.split("\\s+");
            int numExhibits = Integer.parseInt(splited[2]);
            assertEquals(numExhibits, activity.getSelectedExhibitsList().size());


        });

    }
/*
    @Test
    public void addCountTest(){
        //Crashes on this line
        ActivityScenario<SearchDisplayActivity> scenario = ActivityScenario.launch((SearchDisplayActivity.class));
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.moveToState(Lifecycle.State.STARTED);
        scenario.moveToState(Lifecycle.State.RESUMED);
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
    */

/*
    @Test
    public void clearWorksProperly() {
        ActivityScenario<SearchDisplayActivity> scenario = ActivityScenario.launch((SearchDisplayActivity.class));
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {


        });
    }
*/
}