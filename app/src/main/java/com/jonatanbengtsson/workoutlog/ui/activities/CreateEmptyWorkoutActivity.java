package com.jonatanbengtsson.workoutlog.ui.activities;

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
import com.jonatanbengtsson.workoutlog.model.Exercise;
import com.jonatanbengtsson.workoutlog.model.ExercisePerformed;
import com.jonatanbengtsson.workoutlog.model.Workout;
import com.jonatanbengtsson.workoutlog.model.WorkoutLog;
import com.jonatanbengtsson.workoutlog.ui.adapters.ExercisePerformedAdapter;
import com.jonatanbengtsson.workoutlog.ui.fragments.ExercisesFragment;

import java.time.LocalDate;

public class CreateEmptyWorkoutActivity extends AppCompatActivity {
    private WorkoutLog workoutLog;
    private Workout workout;
    private RecyclerView recyclerView;

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

        workoutLog = new WorkoutLog();
        workout = new Workout("Unnamed workout", LocalDate.now());

        Button btnAddExercise = findViewById(R.id.btnAddExercise);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        workout.getExercisesPerformedAsync(exercisesPerformed -> {
            ExercisePerformedAdapter exercisePerformedAdapter = new ExercisePerformedAdapter(exercisesPerformed, true);
            recyclerView.setAdapter(exercisePerformedAdapter);
        }, error -> {
            Toast.makeText(this,"Database error", Toast.LENGTH_SHORT).show();
        });

        workoutLog.getExercisesAsync(exercises -> {
            btnAddExercise.setOnClickListener(v -> {
                ExercisesFragment fragment = ExercisesFragment.newInstance(exercises);
                fragment.setOnExerciseSelectedListener(exercise -> {
                    Toast.makeText(this, "Exercise: " + exercise.getName(), Toast.LENGTH_SHORT).show();
                    addExercisePerformed(exercise);
                });
                fragment.show(getSupportFragmentManager(), "Exercises");
            });
        }, error -> {
            Toast.makeText(this, "Database error", Toast.LENGTH_SHORT).show();
        });
    }

    private void addExercisePerformed(Exercise exercise) {
        ExercisePerformed exercisePerformed = new ExercisePerformed(exercise);
        int position = workout.addExercisePerformed(exercisePerformed);
        recyclerView.getAdapter().notifyItemInserted(position);
    }
}