package com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Exercise;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Routine;

/**
 *
 * A RecyclerView Adapter that displays a routine's exercises
 *
 * Created by Mitchell on 2/15/2016.
 */

public class RecyclerViewRoutineAdapter
        extends RecyclerView.Adapter<RecyclerViewRoutineAdapter.ViewHolder> {

    private Routine mRoutine;

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mExerciseName;
        public TextView mExerciseDescription;

        public ViewHolder(View v) {
            super(v);
            mExerciseName = (TextView) v.findViewById(R.id.list_item_name);
            mExerciseDescription = (TextView) v.findViewById(R.id.list_item_description);
        }
    }

    // Constructor sets the adapter data
    public RecyclerViewRoutineAdapter(Routine routine){
        mRoutine = routine;
    }

    // Create new views (Invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new two-line-list-item view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.two_line_list_item, parent, false);

        // pass the view into the view holder
        return new ViewHolder(v);
    }

    // Replace the contents of a view (Invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Exercise exercise = mRoutine.getExercise(position);
        TextView nameText = holder.mExerciseName;
        TextView descriptionText = holder.mExerciseDescription;

        nameText.setText(exercise.getName());
        descriptionText.setText("remove this text");
    }

    // Return the size of your data set
    @Override
    public int getItemCount() {
        return mRoutine.getNumExercises();
    }
}
