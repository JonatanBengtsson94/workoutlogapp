package com.jonatanbengtsson.workoutlog.model;

import com.jonatanbengtsson.workoutlog.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.function.Consumer;

public class WorkoutLog {
    private ArrayList<Workout> workouts;

    public void getWorkoutsAsync(Consumer<ArrayList<Workout>> onSuccess, Consumer<Exception> onError) {
        if (workouts == null) {
            loadWorkouts(onSuccess, onError);
        } else {
            onSuccess.accept(new ArrayList<>(workouts));
        }
    }

    private void loadWorkouts(Consumer<ArrayList<Workout>> onSuccess, Consumer<Exception> onError) {
        DatabaseHelper.getInstance().getAllWorkouts(workoutsFromDb -> {
            workouts = new ArrayList<>(workoutsFromDb);
            onSuccess.accept(workouts);
        }, onError);
    }
}
