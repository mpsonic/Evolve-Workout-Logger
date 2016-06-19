package edu.umn.paull011.evolveworkoutlogger.fragments;

import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.ExerciseCursorAdapter;
import edu.umn.paull011.evolveworkoutlogger.helper_classes.RecyclerViewItemClickListener;

/**
 * A {@link ListFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SavedExercisesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SavedExercisesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedExercisesFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private RecyclerView mRecyclerView;
    private ExerciseCursorAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private OnFragmentInteractionListener mFragmentInteractionListener;
    private Cursor mCursor;
    private static final String TAG = SavedExercisesFragment.class.getSimpleName();

    public SavedExercisesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SavedExercisesFragment.
     */
    public static SavedExercisesFragment newInstance() {
        return new SavedExercisesFragment();
    }

    // Get saved routines from the database
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
        mCursor = db.getExercisesCursor(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(edu.umn.paull011.evolveworkoutlogger.R.layout.fragment_saved_exercises, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.recycler_view_saved_exercises);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        mAdapter = new ExerciseCursorAdapter(mCursor);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnItemTouchListener(
                new RecyclerViewItemClickListener(getActivity(),
                        new RecyclerViewItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                onExerciseSelected(position);
                            }
                        })
        );

        // Display the empty view if there is nothing in the database
        TextView emptyView = (TextView) view.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.empty_view_saved_exercises);
        if (mCursor.getCount() == 0){
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
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCursor.close();
        mFragmentInteractionListener = null;
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

    // Runs when an Exercise is selected from the list view
    public void onExerciseSelected(int position) {
        if (mFragmentInteractionListener != null) {
            // Get the Exercise Id based on the position selected
            mCursor.moveToPosition(position);
            String exerciseName = mCursor.getString(0);
            mFragmentInteractionListener.exerciseSelected(exerciseName);
        }
    }

    public void refresh() {
        DatabaseHelper db = DatabaseHelper.getInstance(getActivity());
        mCursor.close();
        mCursor = db.getExercisesCursor(null);
        mAdapter.setCursor(mCursor);
        mAdapter.notifyDataSetChanged();
    }

    public interface OnFragmentInteractionListener {
        void exerciseSelected(String exerciseName);
    }
}
