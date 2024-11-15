package com.jonatanbengtsson.workoutlog.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jonatanbengtsson.workoutlog.R;
import com.jonatanbengtsson.workoutlog.model.Workout;
import com.jonatanbengtsson.workoutlog.ui.activities.CreateEmptyWorkoutActivity;
import com.jonatanbengtsson.workoutlog.ui.adapters.ExercisePerformedAdapter;

public class WorkoutDetailFragment extends DialogFragment {
    private Workout workout;
    private RecyclerView recyclerView;
    private Button btnPerformAgain;

    public static WorkoutDetailFragment newInstance(Workout workout) {
        WorkoutDetailFragment fragment = new WorkoutDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable("workout", workout);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.FullScreenDialog);
        workout = getArguments().getParcelable("workout", Workout.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_detail, container, false);

        btnPerformAgain = rootView.findViewById(R.id.btnPerformAgain);
        btnPerformAgain.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CreateEmptyWorkoutActivity.class);
            startActivity(intent);
        });

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        workout.getExercisesPerformedAsync(exercisesPerformed -> {
            ExercisePerformedAdapter exerciseAdapter = new ExercisePerformedAdapter(exercisesPerformed, false);
            recyclerView.setAdapter(exerciseAdapter);
        }, error -> {
            Toast.makeText(getContext(), "Database error", Toast.LENGTH_SHORT).show();
        });

        return rootView;
    }
}
