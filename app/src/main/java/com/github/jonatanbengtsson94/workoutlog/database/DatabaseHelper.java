package com.github.jonatanbengtsson94.workoutlog.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.github.jonatanbengtsson94.workoutlog.database.DatabaseContract.WorkoutTable;
import com.github.jonatanbengtsson94.workoutlog.database.DatabaseContract.ExercisesTable;
import com.github.jonatanbengtsson94.workoutlog.database.DatabaseContract.SetsTable;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "workoutlog.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WORKOUTS_TABLE = "CREATE TABLE " + WorkoutTable.TABLE_NAME + " ("
                + WorkoutTable.COLUMN_WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WorkoutTable.COLUMN_WORKOUT_NAME + " TEXT, "
                + WorkoutTable.COLUMN_DATE_PERFORMED + "TEXT)";
        db.execSQL(CREATE_WORKOUTS_TABLE);

        String CREATE_EXERCISES_TABLE = "CREATE TABLE " + ExercisesTable.TABLE_NAME + " ("
                + ExercisesTable.COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExercisesTable.COLUMN_EXERCISE_NAME + " TEXT, "
                + ExercisesTable.COLUMN_WORKOUT_ID_FK + "INTEGER, "
                + "FOREIGN KEY(" + ExercisesTable.COLUMN_WORKOUT_ID_FK + ") REFERENCES "
                + WorkoutTable.TABLE_NAME + "(" + WorkoutTable.COLUMN_WORKOUT_ID + "))";
        db.execSQL(CREATE_EXERCISES_TABLE);

        String CREATE_SETS_TABLE = "CREATE TABLE " + SetsTable.TABLE_NAME + " ("
                + SetsTable.COLUMN_SET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SetsTable.COLUMN_REPS + " INTEGER, "
                + SetsTable.COLUMN_WEIGHT + " REAL, "
                + SetsTable.COLUMN_EXERCISE_ID_FK + "INTEGER, "
                + "FOREIGN KEY(" + SetsTable.COLUMN_EXERCISE_ID_FK + ") REFERENCES "
                + ExercisesTable.TABLE_NAME + "(" + ExercisesTable.COLUMN_EXERCISE_ID + "))";
        db.execSQL(CREATE_SETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
