package com.jonatanbengtsson.workoutlog.model;

import java.util.ArrayList;
import java.time.LocalDate;

public class Workout {
    private int id;
    private String name;
    private LocalDate datePerformed;
    private ArrayList<ExercisePerformed> exercisesPerformed;
    public Workout(int id, String name, LocalDate datePerformed) {
        this.id = id;
        this.name = name;
        this.datePerformed = datePerformed;
        this.exercisesPerformed = new ArrayList<>();
    }

    public String getName() { return name; }
    public LocalDate getDatePerformed() { return datePerformed; }
    public ArrayList<ExercisePerformed> getExercisesPerformed() {
        if (exercisesPerformed == null) {
            loadExercisesPerformed();
        }
        return new ArrayList<>(exercisesPerformed);
    }

    private void loadExercisesPerformed() {
    }
}
