package edu.umn.paull011.evolveworkoutlogger.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;

import edu.umn.paull011.evolveworkoutlogger.data_structures.ExerciseSession;
import edu.umn.paull011.evolveworkoutlogger.data_structures.MeasurementCategory;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.ExerciseSessionAdapter;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.ExerciseSessionDataHolder;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.ItemTouchHelperCallback;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.RecyclerViewItemClickListener;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.TestItemTouchHelper;

/**
 * A fragment representing a list of Items.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class ExerciseSessionSetsFragment extends Fragment{

    private RecyclerView mRecyclerView;
    private ExerciseSessionAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private OnFragmentInteractionListener mFragmentInteractionListener;
    private ExerciseSessionDataHolder dataHolder = ExerciseSessionDataHolder.getInstance();
    private ExerciseSession mExerciseSession;
    private TextView mEmptyView;

    private static final String TAG = ExerciseSessionSetsFragment.class.getSimpleName();

    public class ClearFocusTouchListener implements RecyclerView.OnTouchListener{

        private final String TAG = ClearFocusTouchListener.class.getSimpleName();
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Log.d(TAG,"onTouch");
            int action = motionEvent.getActionMasked();
            if (action == MotionEvent.ACTION_MOVE) {
                View currentFocus = getActivity().getCurrentFocus();
                if (currentFocus != null) {
                    currentFocus.clearFocus();
                }
            }
            return false;
        }
    }

    public ExerciseSessionSetsFragment() {
        Log.d(TAG,"ExerciseSessionSetsFragment");
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RoutineSessionExercisesFragment.
     */
    public static ExerciseSessionSetsFragment newInstance() {
        Log.d(TAG,"newInstance");
        return new ExerciseSessionSetsFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        mExerciseSession = dataHolder.getExerciseSession();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        // Inflate the layout for this fragment

        View view = inflater.inflate(edu.umn.paull011.evolveworkoutlogger.R.layout.fragment_exercise_session_sets, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.recycler_view_exercise_session_sets);

        mRecyclerView.setHasFixedSize(false);
        //mRecyclerView.setOnTouchListener(new ClearFocusTouchListener());

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new ExerciseSessionAdapter(this.getContext());
        mRecyclerView.setAdapter(mAdapter);

        // add ItemTouchHelperCallBack to RecyclerView
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new TestItemTouchHelper(callback); //Prints log messages
        touchHelper.attachToRecyclerView(mRecyclerView);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerViewItemClickListener(getActivity(),
                        new RecyclerViewItemClickListener.OnItemClickListener(){
                            @Override
                            public void onItemClick(View view, int position) {
                                onItemSelected(position);
                            }
                        })
        );

        ArrayList<FrameLayout> headerLayouts = new ArrayList<>(4);
        headerLayouts.add((FrameLayout) view.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.reps_layout));
        headerLayouts.add((FrameLayout) view.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.weight_layout));
        headerLayouts.add((FrameLayout) view.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.distance_layout));
        headerLayouts.add((FrameLayout) view.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.time_layout));

        for (MeasurementCategory category: MeasurementCategory.values()) {
            if (!mExerciseSession.hasCategory(category)) {
                headerLayouts.get(category.value()).setVisibility(View.GONE);
            }
        }

        mEmptyView = (TextView) view.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.empty_view_active_exercise_sessions);
        hideOrShowEmptyView();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    // Ensure that the activity is listening to the fragment according to the
    // OnFragmentInteractionListener interface
    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach");
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mFragmentInteractionListener = (OnFragmentInteractionListener) context;
            mFragmentInteractionListener.setFragmentExerciseSession();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach");
        super.onDetach();
        mFragmentInteractionListener = null;
    }

    public void onItemSelected(int position) {
        Log.d(TAG, "onItemSelected("+ position +")");
        mFragmentInteractionListener.setSelected(position);
    }

    public void setExerciseSession(ExerciseSession exerciseSession) {
        Log.d(TAG, "setExerciseSession");
        mExerciseSession = exerciseSession;
    }

    public void refreshSetAdded() {
        Log.d(TAG,"refreshSetAdded");
        mAdapter.notifyItemInserted(mExerciseSession.getNumSets() - 1);
        hideOrShowEmptyView();
    }

    public void refreshNextSet(int currentSetPosition) {
        Log.d(TAG, "refreshNextSet("+ currentSetPosition +")");
        mAdapter.notifyItemChanged(currentSetPosition);
        if (mExerciseSession.getNumSets() - 1 != currentSetPosition) {
            mAdapter.notifyItemChanged(currentSetPosition + 1);
        }
    }

    public void refresh() {
        Log.d(TAG,"refresh");
        mAdapter.notifyDataSetChanged();
        hideOrShowEmptyView();
    }

    private void hideOrShowEmptyView() {
        // Display the empty view if there are no exercises in the Routine Session
        if (mExerciseSession == null || mExerciseSession.getNumSets() == 0){
            mRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
        else{
            mRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void setSelected(int position);
        void setFragmentExerciseSession();
    }
}
