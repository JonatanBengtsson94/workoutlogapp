package com.github.jonatanbengtsson94.workoutlog.model;

import java.util.ArrayList;
import java.time.LocalDate;

public class Workout {
    private String name;
    private LocalDate datePerformed;
    private ArrayList<Exercise> exercises;
}
