package com.jonatanbengtsson.workoutlog.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.jonatanbengtsson.workoutlog.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnNewEmptyWorkout = findViewById(R.id.btnNewEmptyWorkout);
        Button btnNewTemplateWorkout = findViewById(R.id.btnNewTemplateWorkout);
        Button btnHistory = findViewById(R.id.btnHistory);

        btnNewEmptyWorkout.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateWorkoutActivity.class);
            startActivity(intent);
        });

        btnNewTemplateWorkout.setOnClickListener(v -> {

        });

        btnHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, WorkoutHistoryActivity.class);
            startActivity(intent);
        });
    }
}