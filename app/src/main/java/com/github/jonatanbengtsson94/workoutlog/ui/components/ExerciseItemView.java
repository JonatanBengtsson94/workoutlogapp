package com.github.jonatanbengtsson94.workoutlog.ui.components;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.jonatanbengtsson94.workoutlog.R;
import com.github.jonatanbengtsson94.workoutlog.model.Set;

import java.util.ArrayList;

public class ExerciseItemView extends ConstraintLayout {
    private TextView txtExerciseName;
    private LinearLayout setsContainer;

    public ExerciseItemView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_exercise, this, true);
        txtExerciseName = findViewById(R.id.txtExerciseName);
        setsContainer = findViewById(R.id.setsContainer);
    }

    public void setTxtExerciseName(String exerciseName) {
        txtExerciseName.setText(exerciseName);
    }

    public void setSets(ArrayList<Set> sets) {
        for (int i = 0, n = sets.size(); i < n; i++) {
            Set set = sets.get(i);

            View setView = LayoutInflater.from(getContext()).inflate(R.layout.item_set, setsContainer, false);

            TextView txtSetNumber = setView.findViewById(R.id.txtSetNumber);
            TextView txtReps = setView.findViewById(R.id.txtReps);
            TextView txtWeight = setView.findViewById(R.id.txtWeight);

            txtSetNumber.setText(i + 1);
            txtReps.setText(set.getReps());
            txtWeight.setText(set.getWeight() + " kg");

            setsContainer.addView(setView);
        }
    }
}
