package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.umn.paull011.evolveworkoutlogger.R;

/**
 * Created by Mitchell on 7/19/2016.
 * An adapter that populates the RecyclerView for Routine History cards
 */
public class RoutineHistoryCardAdapter extends RecyclerView.Adapter<RoutineHistoryCardAdapter.ViewHolder> {
    private static final String TAG = RoutineHistoryCardAdapter.class.getSimpleName();
    private List<Pair<String, Integer>> mExerciseCounts;

    public RoutineHistoryCardAdapter(List<Pair<String, Integer>> exerciseCounts) {
        Log.d(TAG, "RoutineHistoryCardAdapter constructor");
        mExerciseCounts = exerciseCounts;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.component_card_routine_session_history_exercise, parent, false);
            return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(RoutineHistoryCardAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        Pair<String, Integer> exerciseCount = mExerciseCounts.get(position);
        String exerciseName = exerciseCount.first;
        int setCount = exerciseCount.second;
        holder.mExerciseName.setText(exerciseName);

        String setCountText = setCount + " Set";
        if (setCount > 1) {
            setCountText += "s";
        }
        holder.mSetCount.setText(setCountText);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return mExerciseCounts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mExerciseName;
        public TextView mSetCount;

        public ViewHolder(View itemView) {
            super(itemView);
            mExerciseName = (TextView) itemView.findViewById(R.id.text_card_routine_history_exercise_name);
            mSetCount = (TextView) itemView.findViewById(R.id.text_card_routine_history_exercise_info);
        }
    }
}
