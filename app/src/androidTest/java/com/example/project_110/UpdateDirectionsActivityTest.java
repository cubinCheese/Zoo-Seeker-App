package com.example.project_110;


import static org.junit.Assert.assertEquals;

import android.widget.Switch;

import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4. class)
public class UpdateDirectionsActivityTest {

    @Rule
    public ActivityScenarioRule rule = new ActivityScenarioRule<>(UpdateDirectionsActivity.class);

    @Test
    public void test() throws InterruptedException {
        ActivityScenario scenario = rule.getScenario();
        scenario.onActivity(activity -> {
            Switch sw = (Switch) activity.findViewById(R.id.d_b_switch);
            assertEquals(false, sw.isChecked());
        });
    }

}
