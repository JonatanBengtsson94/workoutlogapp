package com.jonatanbengtsson.workoutlog.ui.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jonatanbengtsson.workoutlog.R;
import com.jonatanbengtsson.workoutlog.model.Workout;
import com.jonatanbengtsson.workoutlog.model.WorkoutLog;

public class WorkoutHistoryActivity extends AppCompatActivity {
    private WorkoutLog workoutlog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout_history);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        workoutlog = new WorkoutLog();
        workoutlog.getWorkoutsAsync(workouts -> {
            for (Workout workout : workouts) {
                Log.i("workout", workout.getName());
            }
        }, error -> {
                Log.i("Error", "Something went wrong");
        });
    }
}