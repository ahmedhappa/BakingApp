package com.example.ahmed.bakingapp.Controller.Fragments;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ahmed.bakingapp.Controller.Adapters.IngredientsAdapter;
import com.example.ahmed.bakingapp.Controller.Adapters.StepsAdapter;
import com.example.ahmed.bakingapp.Controller.Interfaces.OnStepActivityClickListener;
import com.example.ahmed.bakingapp.Controller.Interfaces.OnStepClickListener;
import com.example.ahmed.bakingapp.Controller.Widgets.RecipeWidget;
import com.example.ahmed.bakingapp.Model.MyDatabase;
import com.example.ahmed.bakingapp.Model.Recipe;
import com.example.ahmed.bakingapp.R;

/**
 * Created by Ahmed on 29/01/2018.
 */

public class DetailedFragment extends Fragment implements OnStepClickListener {
    Recipe recipe;
    RecyclerView ingredientsRecycler, stepsRecycler;
    private final int[] backgroundColors = {R.color.recipes1, R.color.recipes2, R.color.recipes3, R.color.recipes4};
    View rootIngredientsView;
    int ingredientsRecyclerPosition = -1, stepsRecyclerPostion = -1;
    OnStepActivityClickListener onStepActivityClickListener;
    SharedPreferences sharedPreferences;
    MyDatabase myDatabase;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed, container, false);
        ingredientsRecycler = view.findViewById(R.id.detailed_recycler_ingredients);
        rootIngredientsView = view.findViewById(R.id.detailed_ingredients_root_view);
        stepsRecycler = view.findViewById(R.id.detailed_recycler_steps);
        myDatabase = new MyDatabase(getActivity());
        if (savedInstanceState != null) {
            ingredientsRecyclerPosition = savedInstanceState.getInt("ingredients_recycler_position", -1);
            stepsRecyclerPostion = savedInstanceState.getInt("steps_recycler_position", -1);
        }
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Intent intent = getActivity().getIntent();
        int recyclerColor = 0;
        if (intent.hasExtra("recipe_data")) {
            //ingredients recycler
            recipe = intent.getParcelableExtra("recipe_data");
            if (intent.hasExtra("recipe_color")) {
                recyclerColor = intent.getIntExtra("recipe_color", 0);
                rootIngredientsView.setBackgroundColor(getActivity().getResources().getColor(backgroundColors[recyclerColor % 4]));
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (!sharedPreferences.getString("current_recipe_name", "").equals(recipe.getName())) {
                editor.putInt("current_recipe_id", recipe.getId());
                editor.putString("current_recipe_name", recipe.getName());
                editor.putString("current_recipe_servings", recipe.getServings());
                editor.putString("current_recipe_image", recipe.getImage());
                editor.putInt("current_recipe_color", recyclerColor);
                editor.apply();
                myDatabase.insertRecipesData(recipe);
                //update widget Contents if recipe changed
                Intent widgetIntent = new Intent(getActivity(), RecipeWidget.class);
                widgetIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int[] widgetIds = AppWidgetManager.getInstance(getActivity()).
                        getAppWidgetIds(new ComponentName(getActivity(), RecipeWidget.class));
                widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds);
                getActivity().sendBroadcast(widgetIntent);
            }
        }

        if (intent.hasExtra("lunched_from_widget")) {
            if (intent.getBooleanExtra("lunched_from_widget", false)) {
                recipe = myDatabase.getRecipesData();
                recipe.setId(sharedPreferences.getInt("current_recipe_id", 0));
                recipe.setName(sharedPreferences.getString("current_recipe_name", ""));
                recipe.setServings(sharedPreferences.getString("current_recipe_servings", ""));
                recipe.setImage(sharedPreferences.getString("current_recipe_image", ""));
                recyclerColor = sharedPreferences.getInt("current_recipe_color", 0);
                rootIngredientsView.setBackgroundColor(getActivity().getResources().getColor(backgroundColors[recyclerColor % 4]));
            }
        }

        getActivity().setTitle(recipe.getName());
        //ingredient recycler
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(recipe, getActivity());
        ingredientsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        ingredientsRecycler.setAdapter(ingredientsAdapter);
        if (ingredientsRecyclerPosition != -1) {
            ingredientsRecycler.scrollToPosition(ingredientsRecyclerPosition);
        }

        //steps recycler
        StepsAdapter stepsAdapter = new StepsAdapter(recipe, getActivity());
        stepsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        stepsRecycler.setAdapter(stepsAdapter);
        stepsAdapter.setOnStepClickListener(this);
        if (stepsRecyclerPostion != -1) {
            stepsRecycler.scrollToPosition(stepsRecyclerPostion);
        }

        return view;
    }

    @Override
    public void onStepClick(int position) {
        onStepActivityClickListener.onClickStepActivity(recipe, position);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onStepActivityClickListener = (OnStepActivityClickListener) getActivity();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LinearLayoutManager ingredientsRecyclerManager = (LinearLayoutManager) ingredientsRecycler.getLayoutManager();
        LinearLayoutManager stepsRecyclerManager = (LinearLayoutManager) stepsRecycler.getLayoutManager();
        if (ingredientsRecyclerManager != null) {
            int ingredientsPosition = ingredientsRecyclerManager.findFirstCompletelyVisibleItemPosition();
            outState.putInt("ingredients_recycler_position", ingredientsPosition);
        }
        if (stepsRecyclerManager != null) {
            int stepsPosition = stepsRecyclerManager.findFirstCompletelyVisibleItemPosition();
            outState.putInt("steps_recycler_position", stepsPosition);
        }

    }

}
