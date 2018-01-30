package com.example.ahmed.bakingapp.Controller.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.ahmed.bakingapp.Controller.Fragments.DetailedFragment;
import com.example.ahmed.bakingapp.Controller.Fragments.StepDetailFragment;
import com.example.ahmed.bakingapp.Controller.Interfaces.OnStepActivityClickListener;
import com.example.ahmed.bakingapp.Model.Recipe;
import com.example.ahmed.bakingapp.R;

public class DetailedActivity extends AppCompatActivity implements OnStepActivityClickListener {
    FrameLayout frameLayout;
    boolean isTwoPane;
    DetailedFragment detailedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        frameLayout = findViewById(R.id.detailed_activity_step_detail_fragment);
        isTwoPane = frameLayout != null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        detailedFragment = new DetailedFragment();
        detailedFragment.setOnStepActivityClickListener(this);
    }

    @Override
    public void onClickStepActivity(Recipe recipe, int Position) {
        if (isTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("recipe_data", recipe);
            bundle.putInt("step_position", Position);
            StepDetailFragment stepDetailFragment = new StepDetailFragment();
            stepDetailFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.detailed_activity_step_detail_fragment, stepDetailFragment);
            fragmentTransaction.commit();
        } else {
            Intent intent = new Intent(this, StepDetailActivity.class);
            intent.putExtra("recipe_data", recipe);
            intent.putExtra("step_position", Position);
            startActivity(intent);
        }
    }


}
