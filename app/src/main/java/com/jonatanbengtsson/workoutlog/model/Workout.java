package com.jonatanbengtsson.workoutlog.model;

import com.jonatanbengtsson.workoutlog.database.DatabaseHelper;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.function.Consumer;

public class Workout {
    private int id;
    private String name;
    private LocalDate datePerformed;
    private ArrayList<ExercisePerformed> exercisesPerformed;
    public Workout(int id) {
        this.id = id;
    }

    public Workout(int id, String name, LocalDate datePerformed) {
        this.id = id;
        this.name = name;
        this.datePerformed = datePerformed;
    }

    public String getName() { return name; }
    public LocalDate getDatePerformed() { return datePerformed; }
    public int getId() { return id; }
    public void getExercisesPerformedAsync(Consumer<ArrayList<ExercisePerformed>> onSuccess, Consumer<Exception> onError) {
        if (exercisesPerformed == null) {
            loadExercisesPerformed(onSuccess, onError);
        } else {
            onSuccess.accept(new ArrayList<>(exercisesPerformed));
        }
    }

    private void loadExercisesPerformed(Consumer<ArrayList<ExercisePerformed>> onSuccess, Consumer<Exception> onError) {
        DatabaseHelper.getInstance().getExercisesPerformedByWorkoutId(id, exercisesPerformedFromDb -> {
            exercisesPerformed = exercisesPerformedFromDb;
            onSuccess.accept(exercisesPerformedFromDb);
        }, onError);
    }
}
