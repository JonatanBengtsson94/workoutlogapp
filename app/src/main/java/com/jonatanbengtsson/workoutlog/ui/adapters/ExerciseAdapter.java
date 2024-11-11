package com.jonatanbengtsson.workoutlog.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jonatanbengtsson.workoutlog.R;
import com.jonatanbengtsson.workoutlog.model.Exercise;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private ArrayList<Exercise> exercises;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Exercise exercise);
    }

    public ExerciseAdapter(ArrayList<Exercise> exercises, OnItemClickListener listener) {
        this.exercises = exercises;
        this.listener = listener;
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
       return new ExerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        Exercise exercise = exercises.get(position);
        holder.txtExerciseName.setText(exercise.getName());
        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(exercise);
        });
    }

    @Override
    public int getItemCount() { return exercises.size(); }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView txtExerciseName;

        public  ExerciseViewHolder(View itemView) {
            super(itemView);
            txtExerciseName = itemView.findViewById(R.id.txtExerciseName);
        }
    }
}
