package com.example.project_110;

import static org.junit.Assert.assertEquals;

import android.widget.SearchView;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SearchBarUnitTest {
    @Test
    public void getsCorrectText() {
        ActivityScenario<SearchDisplayActivity> scenario = ActivityScenario.launch((SearchDisplayActivity.class));
        scenario.moveToState(Lifecycle.State.CREATED);
        scenario.onActivity(activity -> {
            SearchView searchView = (SearchView) activity.findViewById(R.id.search);
            searchView.setQuery("Joe", true);
            assertEquals(activity.searchbar.getCurrentQuery(), "Joe");
        });
    }
}
