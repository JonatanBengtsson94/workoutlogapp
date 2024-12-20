package com.jonatanbengtsson.workoutlog.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;

import com.jonatanbengtsson.workoutlog.App;
import com.jonatanbengtsson.workoutlog.model.Exercise;
import com.jonatanbengtsson.workoutlog.model.ExercisePerformed;
import com.jonatanbengtsson.workoutlog.model.Set;
import com.jonatanbengtsson.workoutlog.model.Workout;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "workoutlog.db";
    private static final int DATABASE_VERSION = 1;
    private static DatabaseHelper instance = null;
    private ExecutorService executorService;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        executorService = Executors.newCachedThreadPool();
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
        String CREATE_EXERCISE_TABLE = "CREATE TABLE " + DatabaseContract.ExerciseTable.TABLE_NAME + " ("
                + DatabaseContract.ExerciseTable.COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseContract.ExerciseTable.COLUMN_EXERCISE_NAME + " TEXT UNIQUE NOT NULL)";
        db.execSQL(CREATE_EXERCISE_TABLE);

        String CREATE_WORKOUTS_TABLE = "CREATE TABLE " + DatabaseContract.WorkoutsTable.TABLE_NAME + " ("
                + DatabaseContract.WorkoutsTable.COLUMN_WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseContract.WorkoutsTable.COLUMN_WORKOUT_NAME + " TEXT NOT NULL, "
                + DatabaseContract.WorkoutsTable.COLUMN_DATE_PERFORMED + " TEXT NOT NULL)";
        db.execSQL(CREATE_WORKOUTS_TABLE);

        String CREATE_EXERCISES_PERFORMED_TABLE = "CREATE TABLE " + DatabaseContract.ExercisesPerformedTable.TABLE_NAME + " ("
                + DatabaseContract.ExercisesPerformedTable.COLUMN_EXERCISE_PERFORMED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseContract.ExercisesPerformedTable.COLUMN_EXERCISE_ID_FK + " INTEGER NOT NULL, "
                + DatabaseContract.ExercisesPerformedTable.COLUMN_WORKOUT_ID_FK + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + DatabaseContract.ExercisesPerformedTable.COLUMN_WORKOUT_ID_FK + ") REFERENCES "
                + DatabaseContract.WorkoutsTable.TABLE_NAME + "(" + DatabaseContract.WorkoutsTable.COLUMN_WORKOUT_ID + "), "
                + "FOREIGN KEY(" + DatabaseContract.ExercisesPerformedTable.COLUMN_EXERCISE_ID_FK + ") REFERENCES "
                + DatabaseContract.ExerciseTable.TABLE_NAME + "(" + DatabaseContract.ExerciseTable.COLUMN_EXERCISE_ID + ")"
                +");";
        db.execSQL(CREATE_EXERCISES_PERFORMED_TABLE);

        String CREATE_SETS_TABLE = "CREATE TABLE " + DatabaseContract.SetsTable.TABLE_NAME + " ("
                + DatabaseContract.SetsTable.COLUMN_SET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DatabaseContract.SetsTable.COLUMN_REPS + " INTEGER NOT NULL, "
                + DatabaseContract.SetsTable.COLUMN_WEIGHT + " REAL NOT NULL, "
                + DatabaseContract.SetsTable.COLUMN_EXERCISE_PERFORMED_ID_FK + " INTEGER NOT NULL, "
                + "FOREIGN KEY(" + DatabaseContract.SetsTable.COLUMN_EXERCISE_PERFORMED_ID_FK + ") REFERENCES "
                + DatabaseContract.ExercisesPerformedTable.TABLE_NAME + "(" + DatabaseContract.ExercisesPerformedTable.COLUMN_EXERCISE_PERFORMED_ID + "))";
        db.execSQL(CREATE_SETS_TABLE);

        String CREATE_EXERCISE_NAME_INDEX = "CREATE INDEX idx_exercise_name ON " + DatabaseContract.ExerciseTable.TABLE_NAME + " ("
                + DatabaseContract.ExerciseTable.COLUMN_EXERCISE_NAME + ");";
        db.execSQL(CREATE_EXERCISE_NAME_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private long insertWorkout (String workoutName, LocalDate datePerformed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        String dateString = datePerformed.format(formatter);

        values.put(DatabaseContract.WorkoutsTable.COLUMN_WORKOUT_NAME, workoutName);
        values.put(DatabaseContract.WorkoutsTable.COLUMN_DATE_PERFORMED, dateString);

        long workoutId = db.insert(DatabaseContract.WorkoutsTable.TABLE_NAME, null, values);

        if (workoutId == -1) {
            throw new RuntimeException("Failed to insert workout");
        }

        return workoutId;
    }

    private long insertExercisePerformed(long exerciseId, long workoutId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.ExercisesPerformedTable.COLUMN_EXERCISE_ID_FK, exerciseId);
        values.put(DatabaseContract.ExercisesPerformedTable.COLUMN_WORKOUT_ID_FK, workoutId);

        long exercisePerformedId = db.insert(DatabaseContract.ExercisesPerformedTable.TABLE_NAME, null, values);

        if (exercisePerformedId == -1) {
            throw new RuntimeException("Failed to insert exercise");
        }

        return exercisePerformedId;
    }

    private long insertSet(int reps, float weight, long exercisePerformedId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.SetsTable.COLUMN_REPS, reps);
        values.put(DatabaseContract.SetsTable.COLUMN_WEIGHT, weight);
        values.put(DatabaseContract.SetsTable.COLUMN_EXERCISE_PERFORMED_ID_FK, exercisePerformedId);

        long setId = db.insert(DatabaseContract.SetsTable.TABLE_NAME, null, values);

        if (setId == -1) {
            throw new RuntimeException("Failed to insert set");
        }

        return setId;
    }

    private long insertExercise(String exerciseName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseContract.ExerciseTable.COLUMN_EXERCISE_NAME, exerciseName);

        long exerciseId = db.insert(DatabaseContract.ExerciseTable.TABLE_NAME, null, values);

        if (exerciseId == -1) {
            throw new RuntimeException("Failed to insert exercise");
        }

        return exerciseId;
    }

    public void addWorkout(Workout workout, Consumer<Long> onSuccess, Consumer<Exception> onError) {
        DatabaseHelper dbHelper = this;
        executorService.execute(() -> {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                long workoutId = insertWorkout(workout.getName(), workout.getDatePerformed());
                workout.getExercisesPerformedAsync(exercisesPerformed -> {
                   for (ExercisePerformed exercisePerformed: exercisesPerformed) {
                       try {
                           long exercisePerformedId = insertExercisePerformed(exercisePerformed.getExerciseId(), workoutId);
                           exercisePerformed.getSetsAsync(sets -> {
                               for (Set set: sets) {
                                   insertSet(set.getReps(), set.getWeight(), exercisePerformedId);
                               }
                               db.setTransactionSuccessful();
                               new Handler(Looper.getMainLooper()).post(() -> onSuccess.accept(workoutId));
                           }, e -> {
                               new Handler(Looper.getMainLooper()).post(() -> onError.accept(e));
                           });
                       } catch (Exception e) {
                           new Handler(Looper.getMainLooper()).post(() -> onError.accept(e));
                       }
                   }
                }, e -> {
                    new Handler(Looper.getMainLooper()).post(() -> onError.accept(e));
                });
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> onError.accept(e));
            } finally {
                db.endTransaction();
            }
        });
    }

    public void addExercise(Exercise exercise, Consumer<Long> onSuccess, Consumer<Exception> onError) {
        DatabaseHelper dbHelper = this;
        executorService.execute(() -> {
            try {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                long exerciseId = insertExercise(exercise.getName());
                new Handler(Looper.getMainLooper()).post(()-> onSuccess.accept(exerciseId));
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> onError.accept(e));
            }
        });
    }

    public void getAllWorkouts(Consumer<ArrayList<Workout>> onSuccess, Consumer<Exception> onError) {
        DatabaseHelper dbHelper = this;
        executorService.submit(() -> {
            Cursor cursor = null;
            ArrayList<Workout> workouts = new ArrayList<>();
            try {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM " + DatabaseContract.WorkoutsTable.TABLE_NAME + " ORDER BY 1 DESC", null);

                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.WorkoutsTable.COLUMN_WORKOUT_ID));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.WorkoutsTable.COLUMN_WORKOUT_NAME));
                        String datePerformedString = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.WorkoutsTable.COLUMN_DATE_PERFORMED));
                        LocalDate datePerformed = LocalDate.parse(datePerformedString, DateTimeFormatter.ISO_LOCAL_DATE);
                        workouts.add(new Workout(id, name, datePerformed));
                    } while (cursor.moveToNext());
                }
                new Handler(Looper.getMainLooper()).post(() -> onSuccess.accept(workouts));
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> onError.accept(e));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        });
    }

    public void getAllExercises(Consumer<ArrayList<Exercise>> onSuccess, Consumer<Exception> onError) {
        DatabaseHelper dbHelper = this;
        executorService.submit(() -> {
            Cursor cursor = null;
            ArrayList<Exercise> exercises = new ArrayList<>();
            try {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                cursor = db.rawQuery("SELECT * FROM " + DatabaseContract.ExerciseTable.TABLE_NAME
                        + " ORDER BY " + DatabaseContract.ExerciseTable.COLUMN_EXERCISE_NAME + " ASC"
                        , null);

                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ExerciseTable.COLUMN_EXERCISE_ID));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExerciseTable.COLUMN_EXERCISE_NAME));
                        exercises.add(new Exercise(id, name));
                    } while (cursor.moveToNext());
                }
                new Handler(Looper.getMainLooper()).post(() -> onSuccess.accept(exercises));
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> onError.accept(e));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        });
    }

    public void getExercisesPerformedByWorkoutId(int workoutId, Consumer<ArrayList<ExercisePerformed>> onSuccess, Consumer<Exception> onError) {
        DatabaseHelper dbHelper = this;
        executorService.submit(() -> {
            Cursor cursor = null;
            ArrayList<ExercisePerformed> exercisesPerformed = new ArrayList<>();

            try {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String query = "SELECT * FROM " + DatabaseContract.ExercisesPerformedTable.TABLE_NAME + " WHERE " + DatabaseContract.ExercisesPerformedTable.COLUMN_WORKOUT_ID_FK + " = ?";
                String[] selectionArgs = { String.valueOf(workoutId) };
                cursor = db.rawQuery(query, selectionArgs);

                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ExercisesPerformedTable.COLUMN_EXERCISE_PERFORMED_ID));
                        int exerciseId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ExercisesPerformedTable.COLUMN_EXERCISE_ID_FK));
                        exercisesPerformed.add(new ExercisePerformed(id, workoutId, exerciseId));
                    } while (cursor.moveToNext());
                }
                new Handler(Looper.getMainLooper()).post(() -> onSuccess.accept(exercisesPerformed));
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> onError.accept(e));
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        });
    }

    public void getExercise(int exerciseId, Consumer<Exercise> onSuccess, Consumer<Exception> onError) {
        DatabaseHelper dbHelper = this;
        executorService.submit(() -> {
            Cursor cursor = null;
           try {
              SQLiteDatabase db = dbHelper.getReadableDatabase();
              String query = "SELECT * FROM " + DatabaseContract.ExerciseTable.TABLE_NAME + " WHERE " + DatabaseContract.ExerciseTable.COLUMN_EXERCISE_ID + " = ?";
              String[] selectionArgs = { String.valueOf(exerciseId) };
              cursor = db.rawQuery(query, selectionArgs);

              if (cursor.moveToFirst()) {
                  do {
                      int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ExerciseTable.COLUMN_EXERCISE_ID));
                      String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.ExerciseTable.COLUMN_EXERCISE_NAME));
                      Exercise exercise = new Exercise(id, name);
                      new Handler(Looper.getMainLooper()).post(() -> onSuccess.accept(exercise));
                      break;
                  } while (cursor.moveToNext());
              }
           } catch (Exception e) {
               new Handler(Looper.getMainLooper()).post(() -> onError.accept(e));
           }
        });
    }

    public void getSetsByExercisePerformedId(int exercisePerformedId, Consumer<ArrayList<Set>> onSuccess, Consumer<Exception> onError) {
        DatabaseHelper dbHelper = this;
        executorService.submit(() -> {
            Cursor cursor = null;
            ArrayList<Set> sets = new ArrayList<>();
            try {
                SQLiteDatabase db = dbHelper.getReadableDatabase();
                String query = "SELECT * FROM " + DatabaseContract.SetsTable.TABLE_NAME + " WHERE " + DatabaseContract.SetsTable.COLUMN_EXERCISE_PERFORMED_ID_FK+ " = ?";
                String[] selectionArgs = { String.valueOf(exercisePerformedId) };
                cursor = db.rawQuery(query, selectionArgs);

                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.SetsTable.COLUMN_SET_ID));
                        int reps = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.SetsTable.COLUMN_REPS));
                        float weight = cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseContract.SetsTable.COLUMN_WEIGHT));
                        sets.add(new Set(id, exercisePerformedId, reps, weight));
                    } while (cursor.moveToNext());
                }
                new Handler(Looper.getMainLooper()).post(() -> onSuccess.accept(sets));
            } catch (Exception e) {
                new Handler(Looper.getMainLooper()).post(() -> onError.accept(e));
            }
        });
    }
}
