package com.jonatanbengtsson.workoutlog.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jonatanbengtsson.workoutlog.R;
import com.jonatanbengtsson.workoutlog.model.Workout;
import com.jonatanbengtsson.workoutlog.ui.adapters.ExercisePerformedAdapter;

public class WorkoutDetailFragment extends DialogFragment {
    private Workout workout;
    private RecyclerView recyclerView;

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
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        workout.getExercisesPerformedAsync(exercisesPerformed -> {
            ExercisePerformedAdapter exerciseAdapter = new ExercisePerformedAdapter(exercisesPerformed);
            recyclerView.setAdapter(exerciseAdapter);
        }, error -> {
            Toast.makeText(getContext(), "Database error", Toast.LENGTH_SHORT).show();
        });


        return rootView;
    }
}
