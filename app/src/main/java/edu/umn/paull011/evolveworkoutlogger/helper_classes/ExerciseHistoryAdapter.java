package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Exercise;
import edu.umn.paull011.evolveworkoutlogger.data_structures.ExerciseStats;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Set;

/**
 * Adapter that makes cards displaying all of an exercise's sessions
 * Created by Mitchell on 7/11/2016.
 */
public class ExerciseHistoryAdapter extends RecyclerView.Adapter<ExerciseHistoryAdapter.ViewHolder> {

    public static final String TAG = ExerciseHistoryAdapter.class.getSimpleName();
    public Context mContext;
    public ExerciseStats mExerciseStats;
    public Exercise mExercise;

    public ExerciseHistoryAdapter(Context context, Exercise exercise, ExerciseStats exerciseStats) {
        Log.d(TAG, "ExerciseHistoryAdapter Constructor");
        mExercise = exercise;
        mExerciseStats = exerciseStats;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "OnCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.component_card_exercise_session_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        RecyclerView recyclerView = holder.mSetsRecyclerView;
        TextView dateText = holder.mDateText;
        String dateString = mExerciseStats.getDateString(position);
        List<Set> setList = mExerciseStats.getSets(position);
        ExerciseHistoryCardAdapter cardAdapter = new ExerciseHistoryCardAdapter(mExercise, setList);

        dateText.setText(dateString);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cardAdapter);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return mExerciseStats.getNumSessions();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mDateText;
        public RecyclerView mSetsRecyclerView;

        public ViewHolder(View v) {
            super(v);
            mDateText = (TextView) v.findViewById(R.id.text_card_exercise_session_history_date);
            mSetsRecyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_card_exercise_session_history);
        }
    }
}
