package com.github.jonatanbengtsson94.workoutlog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.jonatanbengtsson94.workoutlog.App;
import com.github.jonatanbengtsson94.workoutlog.database.DatabaseContract.WorkoutsTable;
import com.github.jonatanbengtsson94.workoutlog.database.DatabaseContract.ExercisesPerformedTable;
import com.github.jonatanbengtsson94.workoutlog.database.DatabaseContract.SetsTable;
import com.github.jonatanbengtsson94.workoutlog.database.DatabaseContract.ExerciseTable;
import com.github.jonatanbengtsson94.workoutlog.model.ExercisePerformed;
import com.github.jonatanbengtsson94.workoutlog.model.Set;
import com.github.jonatanbengtsson94.workoutlog.model.Workout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static DatabaseHelper instance = null;
    private static final String DATABASE_NAME = "workoutlog.db";
    private static final int DATABASE_VERSION = 1;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DatabaseHelper getInstance() {
        if (instance == null) {
            Context appContext = App.getAppContext();
            instance = new DatabaseHelper(appContext);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EXERCISE_TABLE = "CREATE TABLE " + ExerciseTable.TABLE_NAME + " ("
                + ExerciseTable.COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ExerciseTable.COLUMN_EXERCISE_NAME + " TEXT)";
        db.execSQL(CREATE_EXERCISE_TABLE);

        String CREATE_WORKOUTS_TABLE = "CREATE TABLE " + WorkoutsTable.TABLE_NAME + " ("
                + WorkoutsTable.COLUMN_WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + WorkoutsTable.COLUMN_WORKOUT_NAME + " TEXT, "
                + WorkoutsTable.COLUMN_DATE_PERFORMED + " TEXT)";
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
                + SetsTable.COLUMN_EXERCISE_PERFORMED_ID_FK + " INTEGER, "
                + "FOREIGN KEY(" + SetsTable.COLUMN_EXERCISE_PERFORMED_ID_FK + ") REFERENCES "
                + ExercisesPerformedTable.TABLE_NAME + "(" + ExercisesPerformedTable.COLUMN_EXERCISE_PERFORMED_ID + "))";
        db.execSQL(CREATE_SETS_TABLE);

        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private long insertWorkout (String workoutName, LocalDate datePerformed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        String dateString = datePerformed.format(formatter);

        values.put(WorkoutsTable.COLUMN_WORKOUT_NAME, workoutName);
        values.put(WorkoutsTable.COLUMN_DATE_PERFORMED, dateString);

        long workoutId = db.insert(WorkoutsTable.TABLE_NAME, null, values);

        if (workoutId == -1) {
            throw new RuntimeException("Failed to insert workout");
        }

        return workoutId;
    }

    private long insertExercisePerformed(long exerciseId, long workoutId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ExercisesPerformedTable.COLUMN_EXERCISE_ID_FK, exerciseId);
        values.put(ExercisesPerformedTable.COLUMN_WORKOUT_ID_FK, workoutId);

        long exercisePerformedId = db.insert(ExercisesPerformedTable.TABLE_NAME, null, values);

        if (exercisePerformedId == -1) {
            throw new RuntimeException("Failed to insert exercise");
        }

        return exercisePerformedId;
    }

    private long insertSet(int reps, float weight, long exercisePerformedId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SetsTable.COLUMN_REPS, reps);
        values.put(SetsTable.COLUMN_WEIGHT, weight);
        values.put(SetsTable.COLUMN_EXERCISE_PERFORMED_ID_FK, exercisePerformedId);

        long setId = db.insert(SetsTable.TABLE_NAME, null, values);

        if (setId == -1) {
            throw new RuntimeException("Failed to insert set");
        }

        return setId;
    }

    public void addWorkout(Workout workout) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            long workoutId = insertWorkout(workout.getName(), workout.getDatePerformed());
            for (ExercisePerformed exercisePerformed: workout.getExercisesPerformed()) {
                long exercisePerformedId = insertExercisePerformed(exercisePerformed.getExerciseId(), workoutId);
                for (Set set: exercisePerformed.getSets()) {
                    long setId = insertSet(set.getReps(), set.getWeight(), exercisePerformedId);
                }
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Workout> getAllWorkouts() {
        ArrayList<Workout> workouts = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + WorkoutsTable.TABLE_NAME, null);

        if (cursor.moveToNext()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(WorkoutsTable.COLUMN_WORKOUT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(WorkoutsTable.COLUMN_WORKOUT_NAME));
                String datePerformedString = cursor.getString(cursor.getColumnIndexOrThrow(WorkoutsTable.COLUMN_DATE_PERFORMED));
                LocalDate datePerformed = LocalDate.parse(datePerformedString, DateTimeFormatter.ISO_LOCAL_DATE);
                workouts.add(new Workout(id, name, datePerformed));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return workouts;
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
