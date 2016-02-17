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

import com.evolve.mitchell.evolvefitnessprogramtracker.R;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Routine;
import com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes.RecyclerViewItemClickListener;
import com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes.RecyclerViewRoutineAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RoutineExercisesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RoutineExercisesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoutineExercisesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Routine mRoutine;
    private OnFragmentInteractionListener mListener;

    public static final String ROUTINE_ID = "RoutineId";
    public static final String EXERCISE_ID = "ExerciseId";

    public RoutineExercisesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment RoutineExercisesFragment.
     */
    public static RoutineExercisesFragment newInstance() {
        return new RoutineExercisesFragment();
    }

    public void setRoutine(Routine routine){
        mRoutine = routine;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routine_exercises, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_routine_exercises);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerViewItemClickListener(getActivity(),
                        new RecyclerViewItemClickListener.OnItemClickListener(){
                            @Override
                            public void onItemClick(View view, int position){
                                onItemSelected(position);
                            }
                        })
        );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // specify an adapter
        mAdapter = new RecyclerViewRoutineAdapter(mRoutine);
        mRecyclerView.setAdapter(mAdapter);

        // Make recyclerView invisible if there are no exercises in the routine
        if(mRoutine.getNumExercises() == 0){
            mRecyclerView.setVisibility(View.GONE);
        }
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

    public void onItemSelected(int position){
        mListener.exerciseSelected(position);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void exerciseSelected(int position);
    }
}
