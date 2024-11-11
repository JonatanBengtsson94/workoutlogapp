package com.jonatanbengtsson.workoutlog.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jonatanbengtsson.workoutlog.R;
import com.jonatanbengtsson.workoutlog.model.ExercisePerformed;

import java.util.ArrayList;

public class ExercisePerformedAdapter extends RecyclerView.Adapter<ExercisePerformedAdapter.ExercisePerformedViewHolder> {
    private ArrayList<ExercisePerformed> exercisesPerformed;

    public ExercisePerformedAdapter(ArrayList<ExercisePerformed> exercisesPerformed) {
        this.exercisesPerformed = exercisesPerformed;
    }

    @Override
    public ExercisePerformedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_performed, parent, false);
        return new ExercisePerformedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExercisePerformedViewHolder holder, int position) {
        ExercisePerformed exercisePerformed = exercisesPerformed.get(position);
        exercisePerformed.getExerciseAsync(exercise -> {
            holder.txtExerciseName.setText(exercise.getName());
        }, e -> {
            holder.txtExerciseName.setText("Error loading exercise");
        });
        exercisePerformed.getSetsAsync(sets -> {
            SetAdapter setAdapter = new SetAdapter(sets);
            holder.recyclerView.setAdapter(setAdapter);
            if (holder.recyclerView.getLayoutManager() == null) {
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.recyclerView.getContext()));
            }
        }, e -> {

        });
    }

    @Override
    public int getItemCount() {
        return exercisesPerformed.size();
    }

    public static class ExercisePerformedViewHolder extends RecyclerView.ViewHolder {
        TextView txtExerciseName;
        RecyclerView recyclerView;

        public ExercisePerformedViewHolder(View itemView) {
            super(itemView);
            txtExerciseName = itemView.findViewById(R.id.txtExerciseName);
            recyclerView = itemView.findViewById(R.id.recyclerView);
        }
    }
}
