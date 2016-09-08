package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.RoutineStats;
import edu.umn.paull011.evolveworkoutlogger.fragments.RoutineHistoryFragment;

/**
 * Created by Mitchell on 7/19/2016.
 * An adapter that populates the Routine History RecyclerView with cards showing details
 * for each routine session.
 */
public class RoutineHistoryAdapter extends RecyclerView.Adapter<RoutineHistoryAdapter.ViewHolder> {
    public static final String TAG = RoutineHistoryAdapter.class.getSimpleName();
    public RoutineHistoryFragment.OnFragmentInteractionListener mListener;
    public RoutineStats mRoutineStats;

    public RoutineHistoryAdapter(RoutineHistoryFragment.OnFragmentInteractionListener listener,
                                 RoutineStats routineStats) {
        Log.d(TAG, "RoutineHistoryAdapter Constructor");
        mRoutineStats = routineStats;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "OnCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.component_card_routine_session_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        RecyclerView recyclerView = holder.mSessionRecyclerView;
        TextView dateText = holder.mDateText;
        String dateString = mRoutineStats.getDateString(position);
        final List<Pair<String, Integer>> exerciseCountList = mRoutineStats.getExerciseSetCounts(position);
        RoutineHistoryCardAdapter cardAdapter = new RoutineHistoryCardAdapter(exerciseCountList);

        dateText.setText(dateString);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager((Context) mListener);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(cardAdapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerViewItemClickListener((Context) mListener,
                        new RecyclerViewItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                String exerciseName = exerciseCountList.get(position).first;
                                mListener.exerciseSelected(exerciseName);
                            }
                        })
        );
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return mRoutineStats.getNumDates();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mDateText;
        public RecyclerView mSessionRecyclerView;

        public ViewHolder(View v) {
            super(v);
            mDateText = (TextView)
                    v.findViewById(R.id.text_card_routine_session_history_date);
            mSessionRecyclerView = (RecyclerView)
                    v.findViewById(R.id.recycler_view_card_routine_session_history);
        }
    }
}
