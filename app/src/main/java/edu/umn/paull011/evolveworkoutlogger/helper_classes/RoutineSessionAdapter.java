package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.umn.paull011.evolveworkoutlogger.data_structures.ExerciseSession;
import edu.umn.paull011.evolveworkoutlogger.data_structures.RoutineSession;

/**
 * Created by Mitchell on 1/18/2016.
 *
 * This adapter takes a routine session and generates a recycler view list
 * showing the progress of each exercise
 *
 */
public class RoutineSessionAdapter
        extends RecyclerView.Adapter<RoutineSessionAdapter.ViewHolder>
        implements ItemTouchHelperAdapter{

    private static final String TAG = RoutineSessionAdapter.class.getSimpleName();
    private RoutineSession mRoutineSession;
    private Context mContext;


    public class ViewHolder extends RemovableViewHolder{
        public TextView mExerciseName;
        public ImageView mCompletedCheckmark;
        public ProgressBar mProgressBar;

        public ViewHolder(View v){
            super(v);
            mExerciseName = (TextView) v.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.exercise_session_display_name);
            mCompletedCheckmark = (ImageView) v.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.exercise_session_finished_checkbox);
            mProgressBar = (ProgressBar) v.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.exercise_session_progressBar);
        }
    }

    public RoutineSessionAdapter(Context context, RoutineSession rs) {
        mContext = context;
        mRoutineSession = rs;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        // Create a new exercise-session view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(edu.umn.paull011.evolveworkoutlogger.R.layout.view_exercise_session_display, parent, false);
        // pass the view to into the view holder
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        ExerciseSession exerciseSession = mRoutineSession.getExerciseSession(position);
        TextView nameText = holder.mExerciseName;
        ImageView checkmark = holder.mCompletedCheckmark;
        ProgressBar progressBar = holder.mProgressBar;
        String exerciseName = exerciseSession.getExerciseName();
        int progress = exerciseSession.getSetProgress();

        nameText.setText(exerciseName);
        progressBar.setProgress(progress);

        if(exerciseSession.isCompleted()){
            progressBar.setVisibility(View.GONE);
            checkmark.setVisibility(View.VISIBLE);
        }
        else{
            progressBar.setVisibility(View.VISIBLE);
            checkmark.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return mRoutineSession.getExerciseSessionCount();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        Log.d(TAG,"onItemDismiss (" + position + ")");
//        DatabaseHelper db = DatabaseHelper.getInstance(mContext);
        mRoutineSession.getRoutine().removeExercise(position);
        mRoutineSession.removeExerciseSession(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mRoutineSession.getExerciseSessionCount() - position);
    }
}
