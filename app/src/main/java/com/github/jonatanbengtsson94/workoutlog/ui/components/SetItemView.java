package com.github.jonatanbengtsson94.workoutlog.ui.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.jonatanbengtsson94.workoutlog.R;

public class SetItemView extends ConstraintLayout {

    private TextView txtSetNumber;
    private TextView txtReps;
    private TextView txtWeight;

    public SetItemView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_set, this, true);
        txtSetNumber = findViewById(R.id.txtSetNumber);
        txtReps = findViewById(R.id.txtReps);
        txtWeight = findViewById(R.id.txtWeight);
    }

    private void setSetNumber(int setNumber) {
        txtSetNumber.setText(String.valueOf(setNumber));
    }

    private void setReps(int reps) {
        txtReps.setText(String.valueOf(reps));
    }

    private void setWeight(float weight) {
        txtWeight.setText(weight + " kg");
    }
}
