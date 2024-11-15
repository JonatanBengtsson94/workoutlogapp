package com.jonatanbengtsson.workoutlog.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jonatanbengtsson.workoutlog.R;
import com.jonatanbengtsson.workoutlog.model.Workout;
import com.jonatanbengtsson.workoutlog.utils.DateUtil;

import java.util.ArrayList;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private ArrayList<Workout> workouts;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Workout workout);
    }

    public WorkoutAdapter(ArrayList<Workout> workouts, OnItemClickListener listener) {
        this.workouts = workouts;
        this.listener = listener;
    }

    @Override
    public WorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WorkoutViewHolder holder, int position) {
        Workout workout = workouts.get(position);
        holder.txtWorkoutName.setText(workout.getName());
        holder.txtWorkoutDate.setText(DateUtil.getFormattedDate(workout.getDatePerformed()));
        holder.itemView.setOnClickListener(v -> {
                listener.onItemClick(workout);
        });
    }

    @Override
    public int getItemCount() {
        return workouts.size();
    }

    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView txtWorkoutName, txtWorkoutDate;

        public WorkoutViewHolder(View itemView) {
            super(itemView);
            txtWorkoutName = itemView.findViewById(R.id.txtWorkoutName);
            txtWorkoutDate = itemView.findViewById(R.id.txtWorkoutDate);
        }
    }
}
