package com.jonatanbengtsson.workoutlog.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jonatanbengtsson.workoutlog.R;
import com.jonatanbengtsson.workoutlog.model.Exercise;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> implements Filterable {
    private ArrayList<Exercise> exercises;
    private ArrayList<Exercise> filteredExercises;
    private ExerciseFilter exerciseFilter;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Exercise exercise);
    }

    public ExerciseAdapter(ArrayList<Exercise> exercises, OnItemClickListener listener) {
        this.exercises = exercises;
        this.listener = listener;
        this.filteredExercises = new ArrayList<>(exercises);
    }

    @Override
    public ExerciseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exercise, parent, false);
       return new ExerciseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ExerciseViewHolder holder, int position) {
        Exercise exercise = filteredExercises.get(position);
        holder.txtExerciseName.setText(exercise.getName());
        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(exercise);
        });
    }

    @Override
    public int getItemCount() { return filteredExercises.size(); }

    @Override
    public Filter getFilter() {
        if (exerciseFilter == null) {
            exerciseFilter = new ExerciseFilter();
        }
        return exerciseFilter;
    }

    private class ExerciseFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<Exercise> filteredExercises = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredExercises.addAll(exercises);
            } else {
                String query = constraint.toString().toLowerCase().trim();
                for (Exercise exercise: exercises) {
                    if (exercise.getName().toLowerCase().contains(query)) {
                        filteredExercises.add(exercise);
                    }
                }
            }
            results.values = filteredExercises;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredExercises.clear();
            filteredExercises.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView txtExerciseName;

        public  ExerciseViewHolder(View itemView) {
            super(itemView);
            txtExerciseName = itemView.findViewById(R.id.txtExerciseName);
        }
    }
}
