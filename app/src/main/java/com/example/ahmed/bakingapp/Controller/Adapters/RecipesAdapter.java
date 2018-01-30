package com.example.ahmed.bakingapp.Controller.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmed.bakingapp.Controller.Interfaces.OnRecipeClickListener;
import com.example.ahmed.bakingapp.Model.Recipe;
import com.example.ahmed.bakingapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Ahmed on 28/01/2018.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.ViewHolder> {
    private List<Recipe> recipes;
    private LayoutInflater inflater;
    private Context context;
    private final int[] backgroundColors = {R.color.recipes1, R.color.recipes2, R.color.recipes3, R.color.recipes4};
    private OnRecipeClickListener onRecipeClickListener;

    public RecipesAdapter(List<Recipe> recipes, Context context) {
        this.recipes = recipes;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_items_recipes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.recipeName.setText(recipes.get(position).getName());
        String Serving = "Servings : " + recipes.get(position).getServings();
        holder.servings.setText(Serving);
        holder.rootView.setBackgroundColor(context.getResources().getColor(backgroundColors[position % 4]));
        if (recipes.get(position).getImage() != null && !recipes.get(position).getImage().equals("")) {
            Picasso.with(context)
                    .load(recipes.get(position).getImage())
                    .into(holder.recipeImage);
        }
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView recipeName, servings;
        ImageView recipeImage;

        private ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView.findViewById(R.id.recipes_recycler_item_root_layout);
            recipeName = itemView.findViewById(R.id.recipes_recycler_item_recipe_name);
            servings = itemView.findViewById(R.id.recipes_recycler_item_recipe_servings);
            recipeImage = itemView.findViewById(R.id.recipes_recycler_item_recipe_image);

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRecipeClickListener.onRecipeClick(getAdapterPosition());
                }
            });
        }
    }

    //for interface
    public void setOnRecipeClickListener(OnRecipeClickListener onRecipeClickListener) {
        this.onRecipeClickListener = onRecipeClickListener;
    }
}
