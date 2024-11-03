package com.github.jonatanbengtsson94.workoutlog.model;

import java.util.ArrayList;
import java.time.LocalDate;

public class Workout {
    private String name;
    private LocalDate datePerformed;
    private ArrayList<Exercise> exercises;

    public String getName() { return name; }
    public LocalDate getDatePerformed() { return datePerformed; }
    public ArrayList<Exercise> getExercises() { return new ArrayList<Exercise>(exercises); }
}
