package edu.umn.paull011.evolveworkoutlogger.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Exercise;
import edu.umn.paull011.evolveworkoutlogger.data_structures.ExerciseStats;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.ExerciseStatsAdapter;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.ExerciseStatsDataHolder;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExerciseStatsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExerciseStatsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExerciseStatsFragment extends BaseFragment {

    private RecyclerView mRecyclerView;
    private ExerciseStatsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private OnFragmentInteractionListener mListener;
    private ExerciseStatsDataHolder mDataHolder = ExerciseStatsDataHolder.getInstance();

    private static final String TAG = ExerciseStatsFragment.class.getSimpleName();



    public ExerciseStatsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param exerciseName Parameter 1.
     * @return A new instance of fragment ExerciseStatsFragment.
     */
    public static ExerciseStatsFragment newInstance(String exerciseName) {
        Log.d(TAG, "newInstance");
        return new ExerciseStatsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_exercise_stats, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_exercise_stats);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        ExerciseStats exerciseStats = mDataHolder.getExerciseStats();
        Exercise exercise = mDataHolder.getExercise();
        mAdapter = new ExerciseStatsAdapter(exercise, exerciseStats);
        mRecyclerView.setAdapter(mAdapter);

        // Display the empty view if there are no exercises in the Routine Session
        TextView emptyView = (TextView) view.findViewById(R.id.empty_view_exercise_stats);
        if (exerciseStats == null || exerciseStats.isEmpty()){
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else{
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach");
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
        Log.d(TAG, "onDetach");
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
    }
}
