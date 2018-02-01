package com.example.ahmed.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.ahmed.bakingapp.Controller.Activities.RecipesActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Ahmed on 01/02/2018.
 */

@RunWith(AndroidJUnit4.class)
public class RecyclerTest {
    @Rule
    public ActivityTestRule<RecipesActivity> recipesActivityActivityTestRule = new ActivityTestRule<>(RecipesActivity.class);

    @Test
    public void recipesActivityRecyclerTest() {
        onView(withId(R.id.recipes_recycler_view))
                .perform(actionOnItemAtPosition(0, click()));

        String stepsString = recipesActivityActivityTestRule.getActivity().getResources().getString(R.string.steps);
        onView(withId(R.id.detailed_steps_text)).check(matches(withText(stepsString)));
    }

}
