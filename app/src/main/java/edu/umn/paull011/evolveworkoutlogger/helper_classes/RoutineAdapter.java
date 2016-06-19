package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Exercise;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Routine;

/**
 *
 * A RecyclerView Adapter that displays a routine's exercises
 *
 * Created by Mitchell on 2/15/2016.
 */

public class RoutineAdapter
        extends RecyclerView.Adapter<RoutineAdapter.ViewHolder> {

    private Routine mRoutine;
    private static final String TAG = RoutineAdapter.class.getSimpleName();

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView exerciseName;

        public ViewHolder(View v) {
            super(v);
            exerciseName = (TextView) v.findViewById(R.id.text_line);
        }
    }

    // Constructor sets the adapter data
    public RoutineAdapter(Routine routine){
        mRoutine = routine;
    }

    // Create new views (Invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder()");
        // Create a new two-line-list-item view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_line_list_item, parent, false);

        // pass the view into the view holder
        return new ViewHolder(v);
    }

    // Replace the contents of a view (Invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder(" + position + ")");
        Exercise exercise = mRoutine.getExercise(position);
        holder.exerciseName.setText(exercise.getName());
    }

    // Return the size of your data set
    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount()");
        return mRoutine.getNumExercises();
    }

}
