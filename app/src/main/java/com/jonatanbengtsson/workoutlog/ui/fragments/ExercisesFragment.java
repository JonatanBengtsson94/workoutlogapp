package com.jonatanbengtsson.workoutlog.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jonatanbengtsson.workoutlog.R;
import com.jonatanbengtsson.workoutlog.model.Exercise;
import com.jonatanbengtsson.workoutlog.ui.adapters.ExerciseAdapter;

import java.util.ArrayList;

public class ExercisesFragment extends DialogFragment {
   private ArrayList<Exercise> exercises;
   private RecyclerView recyclerView;
   private OnExerciseSelectedListener listener;

   public interface OnExerciseSelectedListener {
      void OnExerciseSelected(Exercise exercise);
   }

   public static ExercisesFragment newInstance(ArrayList<Exercise> exercises) {
      ExercisesFragment fragment = new ExercisesFragment();
      Bundle args = new Bundle();
      args.putParcelableArrayList("exercises", exercises);
      fragment.setArguments(args);
      return fragment;
   }

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setStyle(STYLE_NORMAL, R.style.FullScreenDialog);
      exercises = getArguments().getParcelableArrayList("exercises", Exercise.class);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_exercises, container, false);
      recyclerView = rootView.findViewById(R.id.recyclerView);
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
      ExerciseAdapter exerciseAdapter = new ExerciseAdapter(exercises,  exercise -> {
         listener.OnExerciseSelected(exercise);
         dismiss();
      });
      recyclerView.setAdapter(exerciseAdapter);

      return rootView;
   }

   public void setOnExerciseSelectedListener(OnExerciseSelectedListener listener) {
      this.listener = listener;
   }
}
