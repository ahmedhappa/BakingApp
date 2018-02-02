package com.example.ahmed.bakingapp.Controller.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ahmed.bakingapp.Controller.Activities.DetailedActivity;
import com.example.ahmed.bakingapp.Controller.Adapters.RecipesAdapter;
import com.example.ahmed.bakingapp.Controller.Interfaces.OnRecipeClickListener;
import com.example.ahmed.bakingapp.Model.Ingredient;
import com.example.ahmed.bakingapp.Model.Recipe;
import com.example.ahmed.bakingapp.Model.Step;
import com.example.ahmed.bakingapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 28/01/2018.
 */

public class RecipesFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>, OnRecipeClickListener {
    RecyclerView recipesRecyclerView;
    List<Recipe> recipes;
    int recipesRecyclerPosition;
    View tabletDevice;
    private static final int getRecipesLoaderId = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        recipesRecyclerView = view.findViewById(R.id.recipes_recycler_view);
        if (savedInstanceState != null) {
            recipesRecyclerPosition = savedInstanceState.getInt("recycler_position", -1);
        }
        tabletDevice = view.findViewById(R.id.tablet_view_test);

        if (checkInternetConnection()) {
            LoaderManager loaderManager = getActivity().getSupportLoaderManager();
            Bundle dataToLoader = new Bundle();
            Uri recipesDataURL = Uri.parse("https://go.udacity.com").buildUpon()
                    .appendPath("android-baking-app-json").build();
            dataToLoader.putString("recipes_url", recipesDataURL.toString());
            Loader<Object> searchLoader = loaderManager.getLoader(getRecipesLoaderId);
            if (searchLoader == null) {
                loaderManager.initLoader(getRecipesLoaderId, dataToLoader, this);
            } else {
                loaderManager.restartLoader(getRecipesLoaderId, dataToLoader, this);
            }
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "Please Connect to the internet", Toast.LENGTH_SHORT).show();
        }


        return view;
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int i, final Bundle bundle) {

        return new AsyncTaskLoader<String>(getActivity().getApplicationContext()) {
            String currentData = null;

            @Override
            protected void onStartLoading() {
                if (bundle != null) {
                    if (currentData != null) {
                        deliverResult(currentData);
                    } else {
                        forceLoad();
                    }
                }
                super.onStartLoading();
            }

            @Override
            public String loadInBackground() {
                String dataUrl = bundle.getString("recipes_url");
                if (dataUrl == null || dataUrl.equals("")) {
                    return "";
                }
                String result = "";
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(dataUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    String bufferTemp;
                    while ((bufferTemp = bufferedReader.readLine()) != null) {
                        result += bufferTemp;
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
                return result;
            }

            @Override
            public void deliverResult(String data) {
                currentData = data;
                super.deliverResult(data);
            }
        };

    }

    @Override
    public void onLoadFinished(Loader<String> loader, String s) {
        if (s != null && !s.equals("")) {
            recipes = jsonParser(s);
            RecipesAdapter recipesAdapter = new RecipesAdapter(recipes, getActivity());
            if (tabletDevice != null) {
                recipesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            } else {
                recipesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            }

            recipesRecyclerView.setAdapter(recipesAdapter);
            recipesAdapter.setOnRecipeClickListener(this);
            if (recipesRecyclerPosition != -1) {
                recipesRecyclerView.scrollToPosition(recipesRecyclerPosition);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    private List<Recipe> jsonParser(String jsonMessage) {
        List<Recipe> returnedRecipes = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonMessage);
            int jsonArraySize = jsonArray.length();
            for (int i = 0; i < jsonArraySize; i++) {
                List<Ingredient> ingredients = new ArrayList<>();
                List<Step> steps = new ArrayList<>();

                //to get ingredient data
                JSONArray jsonArrayIngredients = jsonArray.getJSONObject(i).getJSONArray("ingredients");
                int jsonArrayIngredientsSize = jsonArrayIngredients.length();
                for (int j = 0; j < jsonArrayIngredientsSize; j++) {
                    Ingredient ingredientData = new Ingredient(
                            jsonArrayIngredients.getJSONObject(j).getString("quantity") + ""
                            , jsonArrayIngredients.getJSONObject(j).getString("measure") + ""
                            , jsonArrayIngredients.getJSONObject(j).getString("ingredient") + "");
                    ingredients.add(ingredientData);
                }
                //to get steps data
                JSONArray jsonArraySteps = jsonArray.getJSONObject(i).getJSONArray("steps");
                int jsonArrayStepsSize = jsonArraySteps.length();
                for (int j = 0; j < jsonArrayStepsSize; j++) {
                    Step stepData = new Step(jsonArraySteps.getJSONObject(j).getString("id") + ""
                            , jsonArraySteps.getJSONObject(j).getString("shortDescription") + ""
                            , jsonArraySteps.getJSONObject(j).getString("description") + ""
                            , jsonArraySteps.getJSONObject(j).getString("videoURL") + ""
                            , jsonArraySteps.getJSONObject(j).getString("thumbnailURL") + "");
                    steps.add(stepData);
                }

                Recipe recipe = new Recipe(jsonArray.getJSONObject(i).getInt("id")
                        , jsonArray.getJSONObject(i).getString("name")
                        , jsonArray.getJSONObject(i).getString("servings")
                        , jsonArray.getJSONObject(i).getString("image")
                        , ingredients
                        , steps);
                returnedRecipes.add(recipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnedRecipes;
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public void onRecipeClick(int position) {
        Intent intent = new Intent(getActivity(), DetailedActivity.class);
        intent.putExtra("recipe_data", recipes.get(position));
        intent.putExtra("recipe_color", position);
        startActivity(intent);
    }

    //save recycler position before rotate
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        GridLayoutManager gridLayoutManager = (GridLayoutManager) recipesRecyclerView.getLayoutManager();
        if (gridLayoutManager != null) {
            int recyclerPositionBeforeRotate = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
            outState.putInt("recycler_position", recyclerPositionBeforeRotate);
        }
    }
}
