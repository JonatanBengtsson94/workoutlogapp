package com.jonatanbengtsson.workoutlog.ui.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jonatanbengtsson.workoutlog.R;
import com.jonatanbengtsson.workoutlog.model.Set;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.SetViewHolder> {
    private static DecimalFormat decimalFormat = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.getDefault()));

    private ArrayList<Set> sets;

    public SetAdapter(ArrayList<Set> sets) {
        this.sets = sets;
    }

    @Override
    public SetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_set, parent, false);
        return new SetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SetViewHolder holder, int position) {
        Set set = sets.get(position);
        holder.txtReps.setText(String.valueOf(set.getReps()));
        holder.txtWeight.setText(decimalFormat.format(set.getWeight()));
    }

    @Override
    public int getItemCount() {
        return sets.size();
    }

    public static class SetViewHolder extends RecyclerView.ViewHolder {
        TextView txtReps, txtWeight;

        public SetViewHolder(View itemView) {
            super(itemView);
            txtReps = itemView.findViewById(R.id.txtReps);
            txtWeight = itemView.findViewById(R.id.txtWeight);
        }
    }
}
