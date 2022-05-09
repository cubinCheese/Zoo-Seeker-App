package com.example.project_110;

import org.junit.Test;

import static org.junit.Assert.*;

import android.widget.SearchView;
import android.widget.TextView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SelectedNumberTest {
    @Test
    public void displayedCountIsCorrect() {
        ActivityScenario<SearchDisplayActivity> scenario = ActivityScenario.launch((SearchDisplayActivity.class));
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            TextView numberDisplay = (TextView) activity.findViewById(R.id.selected_exhibit_count);
            int numExhibits = Integer.parseInt("" + numberDisplay.getText());
            assertEquals(numExhibits, activity.getSelectedExhibitsList().size());
        });


    }

}