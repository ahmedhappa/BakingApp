package com.example.ahmed.bakingapp.Controller.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.bakingapp.Model.Recipe;
import com.example.ahmed.bakingapp.R;

/**
 * Created by Ahmed on 29/01/2018.
 */

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.ViewHolder> {
    private Recipe recipe;
    private LayoutInflater inflater;

    public IngredientsAdapter(Recipe recipe, Context context) {
        this.recipe = recipe;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_items_ingredients, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.quantity.setText(recipe.getIngredients().get(position).getQuantity());
        holder.measure.setText(recipe.getIngredients().get(position).getMeasure());
        holder.ingredient.setText(recipe.getIngredients().get(position).getIngredient());
    }

    @Override
    public int getItemCount() {
        return recipe.getIngredients().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView quantity, measure, ingredient;

        public ViewHolder(View itemView) {
            super(itemView);
            quantity = itemView.findViewById(R.id.ingredient_recycler_item_quantity);
            measure = itemView.findViewById(R.id.ingredient_recycler_item_measure);
            ingredient = itemView.findViewById(R.id.ingredient_recycler_item_ingredient);
        }
    }
}
