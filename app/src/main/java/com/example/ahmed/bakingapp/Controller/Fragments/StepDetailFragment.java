package com.example.ahmed.bakingapp.Controller.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.bakingapp.Model.Recipe;
import com.example.ahmed.bakingapp.Model.Step;
import com.example.ahmed.bakingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by Ahmed on 29/01/2018.
 */

public class StepDetailFragment extends Fragment implements View.OnClickListener {
    TextView desciription;
    Button next, previous;
    Step step;
    Recipe recipe;
    int currentPosition;
    SimpleExoPlayerView simpleExoPlayerView;
    SimpleExoPlayer simpleExoPlayer;
    String userAgent;
    long videoPositionState;
    boolean isCurrentVideoPlaying = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_step_detail, container, false);
        simpleExoPlayerView = view.findViewById(R.id.step_detail_video_player);
        desciription = view.findViewById(R.id.step_detail_description);
        next = view.findViewById(R.id.step_detail_next_step);
        previous = view.findViewById(R.id.step_detail_previous_step);
        if (savedInstanceState != null) {
            videoPositionState = savedInstanceState.getLong("video_position", -1);
            currentPosition = savedInstanceState.getInt("step_current_position", 0);
            isCurrentVideoPlaying = savedInstanceState.getBoolean("video_state", true);
        }

        //if app running on tablet
        Bundle bundle = getArguments();
        if (bundle != null) {
            recipe = bundle.getParcelable("recipe_data");
            if (savedInstanceState == null) {
                currentPosition = bundle.getInt("step_position", 0);
            }
            if (bundle.getBoolean("is_two_pane", false)) {
                next.setVisibility(View.GONE);
                previous.setVisibility(View.GONE);
            }
        }
        //if app running on phone
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra("recipe_data") && intent.hasExtra("step_position")) {
            recipe = intent.getParcelableExtra("recipe_data");
            if (savedInstanceState == null) {
                currentPosition = intent.getIntExtra("step_position", 0);
            }
        }

        getActivity().setTitle(recipe.getName());
        step = recipe.getSteps().get(currentPosition);
        desciription.setText(step.getDescription());
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        //play video if Existing
        if (step.getVideoURL() != null && !step.getVideoURL().equals("") && checkInternetConnection()) {
            simpleExoPlayerView.setVisibility(View.VISIBLE);
            LoadControl loadControl = new DefaultLoadControl();
            TrackSelector trackSelector = new DefaultTrackSelector();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            userAgent = Util.getUserAgent(getActivity(), "BakingApp");
            Uri mediaUri = Uri.parse(step.getVideoURL());
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getActivity(), userAgent)
                    , new DefaultExtractorsFactory(), null, null);
            simpleExoPlayer.prepare(mediaSource);
            if (videoPositionState != -1) {
                simpleExoPlayer.seekTo(videoPositionState);
            }
            if (isCurrentVideoPlaying) {
                simpleExoPlayer.setPlayWhenReady(true);
            } else {
                simpleExoPlayer.setPlayWhenReady(false);
            }
        } else {
            Toast.makeText(getActivity(), "No video available", Toast.LENGTH_SHORT).show();
            simpleExoPlayerView.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.step_detail_next_step:
                if (currentPosition != recipe.getSteps().size() - 1) {
                    if (simpleExoPlayer != null) {
                        simpleExoPlayer.stop();
                    } else {
                        LoadControl loadControl = new DefaultLoadControl();
                        TrackSelector trackSelector = new DefaultTrackSelector();
                        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
                        simpleExoPlayerView.setPlayer(simpleExoPlayer);
                        userAgent = Util.getUserAgent(getActivity(), "BakingApp");
                    }
                    currentPosition++;
                    step = recipe.getSteps().get(currentPosition);
                    desciription.setText(step.getDescription());
                    if (step.getVideoURL() != null && !step.getVideoURL().equals("") && checkInternetConnection()) {
                        simpleExoPlayerView.setVisibility(View.VISIBLE);
                        Uri mediaUri = Uri.parse(step.getVideoURL());
                        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getActivity(), userAgent)
                                , new DefaultExtractorsFactory(), null, null);
                        simpleExoPlayer.prepare(mediaSource);
                        simpleExoPlayer.setPlayWhenReady(true);
                    } else {
                        Toast.makeText(getActivity(), "No video available", Toast.LENGTH_SHORT).show();
                        simpleExoPlayerView.setVisibility(View.INVISIBLE);
                    }

                } else {
                    Toast.makeText(getActivity(), "This is The Final Step", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.step_detail_previous_step:
                if (currentPosition != 0) {
                    if (simpleExoPlayer != null) {
                        simpleExoPlayer.stop();
                    } else {
                        LoadControl loadControl = new DefaultLoadControl();
                        TrackSelector trackSelector = new DefaultTrackSelector();
                        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
                        simpleExoPlayerView.setPlayer(simpleExoPlayer);
                        userAgent = Util.getUserAgent(getActivity(), "BakingApp");
                    }
                    currentPosition--;
                    step = recipe.getSteps().get(currentPosition);
                    desciription.setText(step.getDescription());
                    if (step.getVideoURL() != null && !step.getVideoURL().equals("") && checkInternetConnection()) {
                        simpleExoPlayerView.setVisibility(View.VISIBLE);
                        Uri mediaUri = Uri.parse(step.getVideoURL());
                        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(getActivity(), userAgent)
                                , new DefaultExtractorsFactory(), null, null);
                        simpleExoPlayer.prepare(mediaSource);
                        simpleExoPlayer.setPlayWhenReady(true);
                    } else {
                        Toast.makeText(getActivity(), "No video available", Toast.LENGTH_SHORT).show();
                        simpleExoPlayerView.setVisibility(View.INVISIBLE);
                    }

                } else {
                    Toast.makeText(getActivity(), "This is The First Step", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (simpleExoPlayer != null) {
            long videoPosition = simpleExoPlayer.getCurrentPosition();
            boolean isVideoPlaying = simpleExoPlayer.getPlayWhenReady();
            outState.putLong("video_position", videoPosition);
            outState.putBoolean("video_state", isVideoPlaying);
        }
        outState.putInt("step_current_position", currentPosition);
    }
}
