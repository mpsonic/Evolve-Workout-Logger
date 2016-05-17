package com.evolve.mitchell.evolvefitnessprogramtracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.RoutineSession;
import com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes.RecyclerViewItemClickListener;
import com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes.RecyclerViewRoutineSessionAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RoutineSessionExercisesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RoutineSessionExercisesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineSessionExercisesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private OnFragmentInteractionListener mFragmentInteractionListener;
    private RoutineSession mRoutineSession;

    public RoutineSessionExercisesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RoutineSessionExercisesFragment.
     */
    public static RoutineSessionExercisesFragment newInstance() {
        return new RoutineSessionExercisesFragment();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routine_session_exercises, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_active_routine_session);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new RecyclerViewRoutineSessionAdapter(mRoutineSession);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerViewItemClickListener(getActivity(),
                        new RecyclerViewItemClickListener.OnItemClickListener(){
                            @Override
                            public void onItemClick(View view, int position) {
                                onItemSelected(position);
                            }
                        })
        );

        // Display the empty view if there are no exercises in the Routine Session
        TextView emptyView = (TextView) view.findViewById(R.id.empty_view_active_exercise_sessions);
        if (mRoutineSession == null || mRoutineSession.getExerciseCount() == 0){
            mRecyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else{
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }


    // Ensure that the activity is listening to the fragment according to the
    // OnFragmentInteractionListener interface
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mFragmentInteractionListener = (OnFragmentInteractionListener) context;
            mFragmentInteractionListener.setFragmentRoutineSession();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFragmentInteractionListener = null;
    }


    public void onItemSelected(int position){
        mFragmentInteractionListener.exerciseSessionSelected(position);
    }

    public void setRoutineSession(RoutineSession routineSession){
        mRoutineSession = routineSession;
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
        void exerciseSessionSelected(int position);
        void setFragmentRoutineSession();
    }
}
