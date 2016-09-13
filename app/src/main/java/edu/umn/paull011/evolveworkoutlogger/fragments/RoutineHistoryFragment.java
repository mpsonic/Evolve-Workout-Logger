package edu.umn.paull011.evolveworkoutlogger.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.RoutineStats;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.RoutineHistoryAdapter;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.RoutineStatsDataHolder;

/**
 *
 */
public class RoutineHistoryFragment extends BaseFragment {

    private OnFragmentInteractionListener mListener;
    private RoutineStatsDataHolder mDataHolder = RoutineStatsDataHolder.getInstance();

    private RecyclerView mRecyclerView;
    private RoutineHistoryAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public RoutineHistoryFragment() {
        // Required empty public constructor
    }

    public static RoutineHistoryFragment newInstance() {
        return new RoutineHistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_routine_history, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_routine_history);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        RoutineStats routineStats = mDataHolder.getRoutineStats();

        mAdapter = new RoutineHistoryAdapter(mListener, routineStats);

        mRecyclerView.setAdapter(mAdapter);

        // Display the empty view if there are no previous exercise sessions
        TextView emptyView = (TextView) view.findViewById(R.id.empty_view_routine_history);
        if (routineStats == null || routineStats.isEmpty()) {
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void exerciseSelected(String exerciseName);
    }
}
