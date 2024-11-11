package com.jonatanbengtsson.workoutlog.model;

import com.jonatanbengtsson.workoutlog.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.function.Consumer;

public class WorkoutLog {
    private ArrayList<Workout> workouts;
    private ArrayList<Exercise> exercises;

    public void getWorkoutsAsync(Consumer<ArrayList<Workout>> onSuccess, Consumer<Exception> onError) {
        if (workouts == null) {
            loadWorkouts(onSuccess, onError);
        } else {
            onSuccess.accept(new ArrayList<>(workouts));
        }
    }

    public void getExercisesAsync(Consumer<ArrayList<Exercise>> onSuccess, Consumer<Exception> onError) {
        if (workouts == null) {
            loadExercises(onSuccess, onError);
        } else {
            onSuccess.accept(new ArrayList<>(exercises));
        }
    }

    private void loadWorkouts(Consumer<ArrayList<Workout>> onSuccess, Consumer<Exception> onError) {
        DatabaseHelper.getInstance().getAllWorkouts(workoutsFromDb -> {
            workouts = workoutsFromDb;
            onSuccess.accept(workouts);
        }, onError);
    }

    private void loadExercises(Consumer<ArrayList<Exercise>> onSuccess, Consumer<Exception> onError) {
        DatabaseHelper.getInstance().getAllExercises(exercisesFromDb -> {
            exercises = exercisesFromDb;
            onSuccess.accept(exercises);
        }, onError);
    }
}
