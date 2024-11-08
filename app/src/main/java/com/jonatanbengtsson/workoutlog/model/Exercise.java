package com.jonatanbengtsson.workoutlog.model;

public class Exercise {
    private int id;
    private String name;
    public Exercise(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() { return id; }
}
