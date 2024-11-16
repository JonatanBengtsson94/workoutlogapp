package com.jonatanbengtsson.workoutlog.ui.adapters;


import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.jonatanbengtsson.workoutlog.R;
import com.jonatanbengtsson.workoutlog.model.Set;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;
import java.util.function.Consumer;

public class SetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static DecimalFormat decimalFormat = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.getDefault()));
    private ArrayList<Set> sets;
    private boolean isEditMode;
    private Consumer<Boolean> onCheckboxChanged;

    public SetAdapter(ArrayList<Set> sets, boolean isEditMode, Consumer<Boolean> onCheckboxChanged) {
        this.sets = sets;
        this.isEditMode = isEditMode;
        this.onCheckboxChanged = onCheckboxChanged;
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

            editHolder.checkbox.setOnCheckedChangeListener((v, isChecked) -> {
                set.setIsCompleted(isChecked);
                if (isChecked) {
                    int color = ContextCompat.getColor(editHolder.itemView.getContext(), R.color.acceptColor);
                    editHolder.itemView.setBackgroundColor(color);
                } else {
                    editHolder.itemView.setBackgroundColor(Color.TRANSPARENT);
                }
                onCheckboxChanged.accept(areAllSetsCompleted());
            });

            editHolder.btnDeleteSet.setOnClickListener(v -> {
                sets.remove(set);
                notifyItemRemoved(position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    private boolean areAllSetsCompleted() {
        for (Set set: sets) {
            if (!set.getIsCompleted()) {
                return false;
            }
        }
        return true;
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
        CheckBox checkbox;
        ImageButton btnDeleteSet;

        public SetEditViewHolder(View itemView) {
            super(itemView);
            editReps = itemView.findViewById(R.id.editReps);
            editWeight = itemView.findViewById(R.id.editWeight);
            checkbox = itemView.findViewById(R.id.checkbox);
            btnDeleteSet = itemView.findViewById(R.id.btnDeleteSet);
        }
    }

}
