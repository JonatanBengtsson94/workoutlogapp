package com.jonatanbengtsson.workoutlog.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.jonatanbengtsson.workoutlog.database.DatabaseHelper;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.function.Consumer;

public class Workout implements Parcelable {
    private int id;
    private String name;
    private LocalDate datePerformed;
    private ArrayList<ExercisePerformed> exercisesPerformed;
    public Workout(int id, String name, LocalDate datePerformed) {
        this.id = id;
        this.name = name;
        this.datePerformed = datePerformed;
    }

    public Workout(String name, LocalDate datePerformed) {
       this.name = name;
       this.datePerformed = datePerformed;
       this.exercisesPerformed = new ArrayList<>();
    }

    Workout(Parcel in) {
        id = in.readInt();
        name = in.readString();
        String dateString = in.readString();
        datePerformed = LocalDate.parse(dateString);
    }

    public static final Creator<Workout> CREATOR = new Creator<Workout>() {
        @Override
        public Workout createFromParcel(Parcel source) {
            return new Workout(source);
        }

        @Override
        public Workout[] newArray(int size) {
            return new Workout[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        String dateString = datePerformed.toString();
        dest.writeString(dateString);
    }

    public int describeContents() {
        return 0;
    }

    public String getName() { return name; }
    public LocalDate getDatePerformed() { return datePerformed; }
    public int getId() { return id; }
    public void getExercisesPerformedAsync(Consumer<ArrayList<ExercisePerformed>> onSuccess, Consumer<Exception> onError) {
        if (exercisesPerformed == null) {
            loadExercisesPerformed(onSuccess, onError);
        } else {
            onSuccess.accept(exercisesPerformed);
        }
    }

    public void setName(String name) { this.name = name; }

    public int addExercisePerformed(ExercisePerformed exercisePerformed) {
        exercisesPerformed.add(exercisePerformed);
        return exercisesPerformed.size() - 1;
    }

    public void saveToDatabaseAsync(Consumer<Long> onSuccess, Consumer<Exception> onError) {
        DatabaseHelper.getInstance().addWorkout(this, workoutId -> {
            this.id = (int) workoutId.longValue();
            onSuccess.accept(workoutId);
        }, onError);
    }

    private void loadExercisesPerformed(Consumer<ArrayList<ExercisePerformed>> onSuccess, Consumer<Exception> onError) {
        DatabaseHelper.getInstance().getExercisesPerformedByWorkoutId(id, exercisesPerformedFromDb -> {
            exercisesPerformed = exercisesPerformedFromDb;
            onSuccess.accept(exercisesPerformed);
        }, onError);
    }
}
