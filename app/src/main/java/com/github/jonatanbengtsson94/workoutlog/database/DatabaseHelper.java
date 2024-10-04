package com.github.jonatanbengtsson94.workoutlog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.github.jonatanbengtsson94.workoutlog.database.DatabaseContract.WorkoutsTable;
import com.github.jonatanbengtsson94.workoutlog.database.DatabaseContract.ExercisesPerformedTable;
import com.github.jonatanbengtsson94.workoutlog.database.DatabaseContract.SetsTable;
import com.github.jonatanbengtsson94.workoutlog.database.DatabaseContract.ExerciseTable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "workoutlog.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EXERCISE_TABLE = "CREATE TABLE " + ExerciseTable.TABLE_NAME + " ("
                + ExerciseTable.COLUMN_EXERCISE_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExerciseTable.COLUMN_EXERCISE_NAME + "TEXT)";
        db.execSQL(CREATE_EXERCISE_TABLE);

        String CREATE_WORKOUTS_TABLE = "CREATE TABLE " + WorkoutsTable.TABLE_NAME + " ("
                + WorkoutsTable.COLUMN_WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WorkoutsTable.COLUMN_WORKOUT_NAME + " TEXT, "
                + WorkoutsTable.COLUMN_DATE_PERFORMED + "TEXT)";
        db.execSQL(CREATE_WORKOUTS_TABLE);

        String CREATE_EXERCISES_PERFORMED_TABLE = "CREATE TABLE " + ExercisesPerformedTable.TABLE_NAME + " ("
                + ExercisesPerformedTable.COLUMN_EXERCISE_PERFORMED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExercisesPerformedTable.COLUMN_EXERCISE_ID_FK + " INTEGER, "
                + ExercisesPerformedTable.COLUMN_WORKOUT_ID_FK + " INTEGER, "
                + "FOREIGN KEY(" + ExercisesPerformedTable.COLUMN_WORKOUT_ID_FK + ") REFERENCES "
                + WorkoutsTable.TABLE_NAME + "(" + WorkoutsTable.COLUMN_WORKOUT_ID + "), "
                + "FOREIGN KEY(" + ExercisesPerformedTable.COLUMN_EXERCISE_ID_FK + ") REFERENCES "
                + ExerciseTable.TABLE_NAME + "(" + ExerciseTable.COLUMN_EXERCISE_ID + ")"
                +");";
        db.execSQL(CREATE_EXERCISES_PERFORMED_TABLE);

        String CREATE_SETS_TABLE = "CREATE TABLE " + SetsTable.TABLE_NAME + " ("
                + SetsTable.COLUMN_SET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SetsTable.COLUMN_REPS + " INTEGER, "
                + SetsTable.COLUMN_WEIGHT + " REAL, "
                + SetsTable.COLUMN_EXERCISE_PERFORMED_ID_FK + "INTEGER, "
                + "FOREIGN KEY(" + SetsTable.COLUMN_EXERCISE_PERFORMED_ID_FK + ") REFERENCES "
                + ExercisesPerformedTable.TABLE_NAME + "(" + ExercisesPerformedTable.COLUMN_EXERCISE_PERFORMED_ID + "))";
        db.execSQL(CREATE_SETS_TABLE);

        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void insertInitialData(SQLiteDatabase db) {
        // Insert exercises
        db.execSQL("INSERT INTO exercises (exercise_name) VALUES ('Bench Press')");
        db.execSQL("INSERT INTO exercises (exercise_name) VALUES ('Squat')");
        db.execSQL("INSERT INTO exercises (exercise_name) VALUES ('Deadlift')");

        // Insert template workout
        db.execSQL("INSERT INTO workouts (workout_name, date_performed) VALUES ('Template Workout', '2024-10-04')");

        // Insert completed exercises
        db.execSQL("INSERT INTO exercises_performed (exercise_id_fk, workout_id_fk) VALUES (1 , 1)");
        db.execSQL("INSERT INTO exercises_performed (exercise_id_fk, workout_id_fk) VALUES (2 , 1)");

        // Insert completed sets
        db.execSQL("INSERT INTO sets (reps, weight, exercise_performed_id_fk) VALUES (10, 100, 1)");
        db.execSQL("INSERT INTO sets (reps, weight, exercise_performed_id_fk) VALUES (10, 100, 1)");
        db.execSQL("INSERT INTO sets (reps, weight, exercise_performed_id_fk) VALUES (5, 140, 2)");
        db.execSQL("INSERT INTO sets (reps, weight, exercise_performed_id_fk) VALUES (3, 130, 2)");
    }
}
