package com.example.ahmed.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.ahmed.bakingapp.Controller.Activities.RecipesActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.AllOf.allOf;

/**
 * Created by Ahmed on 01/02/2018.
 */

@RunWith(AndroidJUnit4.class)
public class IntentTest {
    @Rule
    public IntentsTestRule<RecipesActivity> intentRecipesActivity = new IntentsTestRule<>(RecipesActivity.class);

    @Before
    public void stubAllExternalIntent() {
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void recipesActivityIntentTest() {
        onView(withId(R.id.recipes_recycler_view)).perform(actionOnItemAtPosition(0, click()));
        intended(allOf(hasExtra("recipe_color",0)));
    }
}
