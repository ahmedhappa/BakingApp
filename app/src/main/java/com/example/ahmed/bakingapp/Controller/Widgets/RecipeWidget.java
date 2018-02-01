package com.example.ahmed.bakingapp.Controller.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

import com.example.ahmed.bakingapp.Controller.Activities.DetailedActivity;
import com.example.ahmed.bakingapp.Model.MyDatabase;
import com.example.ahmed.bakingapp.Model.Recipe;
import com.example.ahmed.bakingapp.R;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        MyDatabase myDatabase;
        String recipeName = sharedPreferences.getString("current_recipe_name", "");
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget);
        if (!recipeName.equals("")) {
            myDatabase = new MyDatabase(context);
            Recipe recipe = myDatabase.getRecipesData();
            StringBuilder ingredientData = new StringBuilder();
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                ingredientData.append(recipe.getIngredients().get(i).getMeasure())
                        .append("   ").append(recipe.getIngredients().get(i).getQuantity())
                        .append("   ").append(recipe.getIngredients().get(i).getIngredient()).append("\n");
            }
            views.setTextViewText(R.id.widget_recipe_ingredients, ingredientData.toString());
            views.setTextViewText(R.id.widget_recipe_name, recipeName);
            Intent intent = new Intent(context, DetailedActivity.class);
            intent.putExtra("lunched_from_widget", true);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget_root_view, pendingIntent);
        } else {
            views.setTextViewText(R.id.widget_recipe_name, "Not Available Now");
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

