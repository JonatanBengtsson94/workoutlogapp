package com.github.jonatanbengtsson94.workoutlog.database;

import android.provider.BaseColumns;

public class DatabaseContract {

    private DatabaseContract() {}

    public static class SetsTable implements BaseColumns {
        public static final String TABLE_NAME = "sets";
        public static final String COLUMN_SET_ID = "set_id";
        public static final String COLUMN_REPS = "reps";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_EXERCISE_ID_FK = "exercise_id_fk";
    }

    public static class ExercisesTable implements BaseColumns {
        public static final String TABLE_NAME = "exercises";
        public static final String COLUMN_EXERCISE_ID = "exercise_id";
        public static final String COLUMN_EXERCISE_NAME = "exercise_name";
        public static final String COLUMN_WORKOUT_ID_FK = "workout_id_fk";
    }

    public static class WorkoutTable implements BaseColumns {
        public static final String TABLE_NAME = "workouts";
        public static final String COLUMN_WORKOUT_NAME = "workout_name";
        public static final String COLUMN_DATE_PERFORMED = "date_performed";
    }
}
