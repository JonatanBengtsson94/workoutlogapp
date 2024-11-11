package com.jonatanbengtsson.workoutlog.ui.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jonatanbengtsson.workoutlog.R;
import com.jonatanbengtsson.workoutlog.model.Exercise;
import com.jonatanbengtsson.workoutlog.model.WorkoutLog;
import com.jonatanbengtsson.workoutlog.ui.fragments.ExercisesFragment;

public class CreateEmptyWorkoutActivity extends AppCompatActivity {
    private WorkoutLog workoutLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_empty_workout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btnAddExercise = findViewById(R.id.btnAddExercise);
        workoutLog = new WorkoutLog();
        workoutLog.getExercisesAsync(exercises -> {
            btnAddExercise.setOnClickListener(v -> {
                ExercisesFragment fragment = ExercisesFragment.newInstance(exercises);
                fragment.setOnExerciseSelectedListener(exercise -> {
                    Toast.makeText(this, "Exercise: " + exercise.getName(), Toast.LENGTH_SHORT).show();
                });
                fragment.show(getSupportFragmentManager(), "Exercises");
            });
        }, error -> {
            Toast.makeText(this, "Database error", Toast.LENGTH_SHORT).show();
        });
    }
}