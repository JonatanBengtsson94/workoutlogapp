package com.jonatanbengtsson.workoutlog.ui.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jonatanbengtsson.workoutlog.R;
import com.jonatanbengtsson.workoutlog.model.ExercisePerformed;
import com.jonatanbengtsson.workoutlog.model.Set;

import java.util.ArrayList;

public class ExercisePerformedAdapter extends RecyclerView.Adapter<ExercisePerformedAdapter.ExercisePerformedViewHolder> {
    private ArrayList<ExercisePerformed> exercisesPerformed;
    private boolean isEditMode;

    public ExercisePerformedAdapter(ArrayList<ExercisePerformed> exercisesPerformed, boolean isEditMode) {
        this.exercisesPerformed = exercisesPerformed;
        this.isEditMode = isEditMode;
    }

    @Override
    public ExercisePerformedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise_performed, parent, false);
        return new ExercisePerformedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExercisePerformedViewHolder holder, int position) {
        ExercisePerformed exercisePerformed = exercisesPerformed.get(position);
        if (!isEditMode) {
            holder.btnRemoveExercise.setVisibility(View.GONE);
            holder.btnAddSet.setVisibility(View.GONE);
        } else {
            holder.btnAddSet.setOnClickListener(v -> {
                handleAddSetClick(position);
            });
            holder.btnRemoveExercise.setOnClickListener(v -> {
                handleRemoveExerciseClick(position);
            });
        }
        exercisePerformed.getExerciseAsync(exercise -> {
            holder.txtExerciseName.setText(exercise.getName());
        }, e -> {
            holder.txtExerciseName.setText("Error loading exercise");
        });
        exercisePerformed.getSetsAsync(sets -> {
            SetAdapter setAdapter = new SetAdapter(sets, isEditMode, allCompleted -> {
                if (allCompleted) {
                    int color = ContextCompat.getColor(holder.itemView.getContext(), R.color.acceptColor);
                    holder.itemView.setBackgroundColor(color);
                } else {
                    holder.itemView.setBackgroundColor(Color.TRANSPARENT);
                }
            });
            holder.recyclerView.setAdapter(setAdapter);
            if (holder.recyclerView.getLayoutManager() == null) {
                holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.recyclerView.getContext()));
            }
        }, e -> {

            });
        }

    private void handleAddSetClick(int position) {
        ExercisePerformed exercisePerformed = exercisesPerformed.get(position);
        exercisePerformed.addSet(new Set());
        notifyItemChanged(position);
    }

    private void handleRemoveExerciseClick(int position) {
        ExercisePerformed exercisePerformed = exercisesPerformed.get(position);
        exercisesPerformed.remove(exercisePerformed);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return exercisesPerformed.size();
    }

    public static class ExercisePerformedViewHolder extends RecyclerView.ViewHolder {
        TextView txtExerciseName;
        RecyclerView recyclerView;
        Button btnAddSet, btnRemoveExercise;

        public ExercisePerformedViewHolder(View itemView) {
            super(itemView);
            txtExerciseName = itemView.findViewById(R.id.txtExerciseName);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            btnAddSet = itemView.findViewById(R.id.btnAddSet);
            btnRemoveExercise = itemView.findViewById(R.id.btnRemoveExercise);
        }
    }
}
