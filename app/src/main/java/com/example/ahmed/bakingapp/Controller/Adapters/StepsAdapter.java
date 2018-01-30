package com.example.ahmed.bakingapp.Controller.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.bakingapp.Controller.Interfaces.OnStepClickListener;
import com.example.ahmed.bakingapp.Model.Recipe;
import com.example.ahmed.bakingapp.R;

/**
 * Created by Ahmed on 29/01/2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {
    private Recipe recipe;
    private LayoutInflater inflater;
    private Context context;
    OnStepClickListener onStepClickListener;

    private final int[] backgroundColors = {R.color.recipes1, R.color.recipes2, R.color.recipes3, R.color.recipes4};

    public StepsAdapter(Recipe recipe, Context context) {
        this.recipe = recipe;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycler_items_steps, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.shortDescription.setText(recipe.getSteps().get(position).getShortDescription());
        holder.stepRelativeView.setBackgroundColor(context.getResources().getColor(backgroundColors[position % 4]));
    }

    @Override
    public int getItemCount() {
        return recipe.getSteps().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView shortDescription;
        View stepRelativeView;

        ViewHolder(View itemView) {
            super(itemView);
            shortDescription = itemView.findViewById(R.id.steps_recycler_item_shortDescription);
            stepRelativeView = itemView.findViewById(R.id.step_relative_view);

            stepRelativeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onStepClickListener.onStepClick(getAdapterPosition());
                }
            });
        }
    }

    public void setOnStepClickListener(OnStepClickListener onStepClickListener) {
        this.onStepClickListener = onStepClickListener;
    }
}
