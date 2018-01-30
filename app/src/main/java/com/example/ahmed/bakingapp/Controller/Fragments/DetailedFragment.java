package com.example.ahmed.bakingapp.Controller.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ahmed.bakingapp.Controller.Adapters.IngredientsAdapter;
import com.example.ahmed.bakingapp.Controller.Adapters.StepsAdapter;
import com.example.ahmed.bakingapp.Controller.Interfaces.OnStepActivityClickListener;
import com.example.ahmed.bakingapp.Controller.Interfaces.OnStepClickListener;
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
    int ingredientsRecyclerPosition, stepsRecyclerPostion;
    OnStepActivityClickListener onStepActivityClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailed, container, false);
        ingredientsRecycler = view.findViewById(R.id.detailed_recycler_ingredients);
        rootIngredientsView = view.findViewById(R.id.detailed_ingredients_root_view);
        stepsRecycler = view.findViewById(R.id.detailed_recycler_steps);
        if (savedInstanceState != null) {
            ingredientsRecyclerPosition = savedInstanceState.getInt("ingredients_recycler_position", -1);
            stepsRecyclerPostion = savedInstanceState.getInt("steps_recycler_position", -1);
        }

        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("recipe_data")) {
            //ingredients recycler
            recipe = intent.getParcelableExtra("recipe_data");
            if (intent.hasExtra("recipe_color")) {
                int recyclerColor = intent.getIntExtra("recipe_color", 0);
                rootIngredientsView.setBackgroundColor(getActivity().getResources().getColor(backgroundColors[recyclerColor % 4]));
            }
            getActivity().setTitle(recipe.getName());
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
        } else {
            Toast.makeText(getActivity(), "Some Thing Wrong Please Try Again", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    @Override
    public void onStepClick(int position) {
        onStepActivityClickListener.onClickStepActivity(recipe, position);
    }

    public void setOnStepActivityClickListener(OnStepActivityClickListener onStepActivityClickListener) {
        this.onStepActivityClickListener = onStepActivityClickListener;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LinearLayoutManager ingredientsRecyclerManager = (LinearLayoutManager) ingredientsRecycler.getLayoutManager();
        LinearLayoutManager stepsRecyclerManager = (LinearLayoutManager) stepsRecycler.getLayoutManager();
        int ingredientsPosition = ingredientsRecyclerManager.findFirstCompletelyVisibleItemPosition();
        int stepsPosition = stepsRecyclerManager.findFirstCompletelyVisibleItemPosition();
        outState.putInt("ingredients_recycler_position", ingredientsPosition);
        outState.putInt("steps_recycler_position", stepsPosition);
    }

}
