package com.jonatanbengtsson.workoutlog.model;

public class Set {
    private int id;
    private int exercisePerformedId;
    private int reps;
    private float weight;
    private boolean isCompleted = false;

    public Set(int id, int exercisePerformedId, int reps, float weight) {
        this.id = id;
        this.exercisePerformedId = exercisePerformedId;
        this.reps = reps;
        this.weight = weight;
    }

    public Set() {
        this.reps = 0;
        this.weight = 0.0F;
    }

    public int getReps() {
        return reps;
    }

    public float getWeight() {
        return weight;
    }

    public boolean getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(boolean isCompleted) { this.isCompleted = isCompleted; }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }
}
