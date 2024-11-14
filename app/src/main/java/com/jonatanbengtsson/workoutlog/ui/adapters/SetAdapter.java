package com.jonatanbengtsson.workoutlog.ui.adapters;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jonatanbengtsson.workoutlog.R;
import com.jonatanbengtsson.workoutlog.model.Set;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class SetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static DecimalFormat decimalFormat = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.getDefault()));
    private ArrayList<Set> sets;
    private boolean isEditMode;

    public SetAdapter(ArrayList<Set> sets, boolean isEditMode) {
        this.sets = sets;
        this.isEditMode = isEditMode;
    }

    public int getItemViewType(int position) {
        return isEditMode ? 1 : 2;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_set, parent, false);
            return new SetEditViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set, parent, false);
            return new SetReadViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Set set = sets.get(position);

        if (holder instanceof SetReadViewHolder) {
            SetReadViewHolder readHolder = (SetReadViewHolder) holder;
            readHolder.txtReps.setText(String.valueOf(set.getReps()));
            readHolder.txtWeight.setText(decimalFormat.format(set.getWeight()));
        }
        else if (holder instanceof SetEditViewHolder) {
            SetEditViewHolder editHolder = (SetEditViewHolder) holder;
            editHolder.editReps.setText(String.valueOf(set.getReps()));
            editHolder.editWeight.setText(decimalFormat.format(set.getWeight()));
            editHolder.editReps.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0) {
                        int reps = Integer.parseInt(s.toString());
                        set.setReps(reps);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
            editHolder.editWeight.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length() > 0) {
                        float weight = Float.parseFloat(s.toString());
                        set.setWeight(weight);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public static class SetReadViewHolder extends RecyclerView.ViewHolder {
        TextView txtReps, txtWeight;

        public SetReadViewHolder(View itemView) {
            super(itemView);
            txtReps = itemView.findViewById(R.id.txtReps);
            txtWeight = itemView.findViewById(R.id.txtWeight);
        }
    }

    public static class SetEditViewHolder extends RecyclerView.ViewHolder {
        EditText editReps, editWeight;

        public SetEditViewHolder(View itemView) {
            super(itemView);
            editReps = itemView.findViewById(R.id.editReps);
            editWeight = itemView.findViewById(R.id.editWeight);
        }
    }

}
