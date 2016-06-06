package com.evolve.mitchell.evolvefitnessprogramtracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Routine;
import com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes.RecyclerViewItemClickListener;
import com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes.RoutineAdapter;

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
    private RoutineAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private Routine mRoutine;
    private OnFragmentInteractionListener mListener;
    private static final String TAG = "CreateRoutine-F";


    public RoutineExercisesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment RoutineExercisesFragment.
     */
    public static RoutineExercisesFragment newInstance() {
        Log.d(TAG, "newInstance()");
        return new RoutineExercisesFragment();
    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "onAttach()");
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate()");
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_routine_exercises, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated()");
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_routine_exercises);
        mRecyclerView.setHasFixedSize(false);

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

    public void setRoutine(Routine routine){
        Log.d(TAG, "setRoutine()");
        mRoutine = routine;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onActivityCreated()");
        super.onActivityCreated(savedInstanceState);

        // specify an adapter
        mAdapter = new RoutineAdapter(mRoutine);
        mRecyclerView.setAdapter(mAdapter);

        // Make recyclerView invisible if there are no exercises in the routine
        if(mRoutine.getNumExercises() == 0){
            mRecyclerView.setVisibility(View.GONE);
        }
    }

    public void refresh() {
        Log.d(TAG, "refresh()");
        //int exerciseCount = mRoutine.getNumExercises();
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach()");
        super.onDetach();
        mListener = null;
    }

    public void onItemSelected(int position){
        Log.d(TAG, "onItemSelected("+ position +")");
        mListener.exerciseSelected(position);
    }

    public boolean isEmpty () {
        Log.d(TAG, "isEmpty()");
        return mAdapter.getItemCount() == 0;
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
