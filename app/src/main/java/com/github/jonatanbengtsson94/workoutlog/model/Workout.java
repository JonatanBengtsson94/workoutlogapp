package com.github.jonatanbengtsson94.workoutlog.model;

import com.github.jonatanbengtsson94.workoutlog.database.DatabaseHelper;
import com.github.jonatanbengtsson94.workoutlog.ui.activities.WorkoutHistoryActivity;

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
        DatabaseHelper db = DatabaseHelper.getInstance();
        exercisesPerformed = db.getExercisesPerformedByWorkoutId(id);
    }
}
