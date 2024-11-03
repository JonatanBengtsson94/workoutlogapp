package com.github.jonatanbengtsson94.workoutlog.model;

import java.util.ArrayList;

public class Exercise {
    private ArrayList<Set> sets;
    private String name;

    public String getName() { return name; }
    public ArrayList<Set> getSets() { return new ArrayList<>(sets); }
}
