package com.jonatanbengtsson.workoutlog.ui.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jonatanbengtsson.workoutlog.R;
import com.jonatanbengtsson.workoutlog.model.Workout;
import com.jonatanbengtsson.workoutlog.ui.adapters.ExercisePerformedAdapter;

public class WorkoutDetailActivity extends AppCompatActivity {
    private Workout workout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_workout_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int workoutId = getIntent().getIntExtra("id", -1);
        Workout workout = new Workout(workoutId);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        workout.getExercisesPerformedAsync(exercisesPerformed -> {
            ExercisePerformedAdapter exerciseAdapter = new ExercisePerformedAdapter(exercisesPerformed);
            recyclerView.setAdapter(exerciseAdapter);
        }, error -> {
            Toast.makeText(this, "Database error", Toast.LENGTH_SHORT).show();
        });
    }
}