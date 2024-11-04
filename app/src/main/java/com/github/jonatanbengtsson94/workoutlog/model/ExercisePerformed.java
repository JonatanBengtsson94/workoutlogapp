package com.github.jonatanbengtsson94.workoutlog.model;

import java.util.ArrayList;

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
    public ArrayList<Set> getSets() { return new ArrayList<>(sets); }
    public int getExerciseId() { return exercise.getId(); }
}