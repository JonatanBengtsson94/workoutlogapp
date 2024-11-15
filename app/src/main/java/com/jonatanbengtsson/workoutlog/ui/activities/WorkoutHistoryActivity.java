package com.jonatanbengtsson.workoutlog.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jonatanbengtsson.workoutlog.R;
import com.jonatanbengtsson.workoutlog.model.WorkoutLog;
import com.jonatanbengtsson.workoutlog.ui.adapters.WorkoutAdapter;
import com.jonatanbengtsson.workoutlog.ui.fragments.WorkoutDetailFragment;

public class WorkoutHistoryActivity extends AppCompatActivity {
    private WorkoutLog workoutlog;
    private RecyclerView recyclerView;
    private WorkoutAdapter workoutAdapter;
    private Button btnNewEmptyWorkout;

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

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnNewEmptyWorkout = findViewById(R.id.btnNewEmptyWorkout);
        btnNewEmptyWorkout.setOnClickListener(v -> {
            Intent intent = new Intent(WorkoutHistoryActivity.this, CreateWorkoutActivity.class);
            startActivity(intent);
        });

        workoutlog = new WorkoutLog();
        workoutlog.getWorkoutsAsync(workouts -> {
            workoutAdapter = new WorkoutAdapter(workouts, workout -> {
                WorkoutDetailFragment fragment = WorkoutDetailFragment.newInstance(workout);
                fragment.show(getSupportFragmentManager(), "WorkoutDetails");
            });
            recyclerView.setAdapter(workoutAdapter);
        }, error -> {
            Toast.makeText(this, "Database error", Toast.LENGTH_SHORT).show();
        });
    }
}