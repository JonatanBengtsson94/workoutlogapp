package com.jonatanbengtsson.workoutlog.model;

import com.jonatanbengtsson.workoutlog.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ExercisePerformed {
    private int id;
    private int workoutId;
    private int exerciseId;
    private Exercise exercise;
    private ArrayList<Set> sets;

    public ExercisePerformed(int id, int workoutId, int exerciseId) {
        this.id = id;
        this.workoutId = workoutId;
        this.exerciseId = exerciseId;
    }

    public ExercisePerformed(Exercise exercise) {
        this.exercise = exercise;
        this.sets = new ArrayList<>();
        sets.add(new Set());
    }

    public void addSet(Set set) {
        sets.add(set);
    }

    public void getSetsAsync(Consumer<ArrayList<Set>> onSuccess, Consumer<Exception> onError) {
        if (sets == null) {
            loadSets(onSuccess, onError);
        } else {
            onSuccess.accept(sets);
        }
    }

    private void loadSets(Consumer<ArrayList<Set>> onSuccess, Consumer<Exception> onError) {
        DatabaseHelper.getInstance().getSetsByExercisePerformedId(id, setsFromDb -> {
            sets = setsFromDb;
            onSuccess.accept(sets);
        }, onError);
    }

    public int getExerciseId() { return exercise.getId(); }

    public void getExerciseAsync(Consumer<Exercise> onSuccess, Consumer<Exception> onError) {
        if (exercise == null) {
            loadExercise(onSuccess, onError);
        } else {
            onSuccess.accept(exercise);
        }
    }

    private void loadExercise(Consumer<Exercise> onSuccess, Consumer<Exception> onError) {
        DatabaseHelper.getInstance().getExercise(id, exerciseFromDb -> {
           exercise = exerciseFromDb;
           onSuccess.accept(exercise);
        }, onError);
    }
}