package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Exercise;
import edu.umn.paull011.evolveworkoutlogger.data_structures.MeasurementCategory;
import edu.umn.paull011.evolveworkoutlogger.data_structures.MeasurementData;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Set;

/**
 * Adapter that fills exercise history cards in the View Exercise activity
 * Created by Mitchell on 7/13/2016.
 */
public class ExerciseHistoryCardAdapter extends RecyclerView.Adapter<ExerciseHistoryCardAdapter.ViewHolder> {
    public static final String TAG = ExerciseHistoryCardAdapter.class.getSimpleName();
    public Exercise mExercise;
    public List<Set> mSetList;

    public ExerciseHistoryCardAdapter(Exercise exercise, List<Set> setList) {
        Log.d(TAG, "ExerciseHistoryCardAdapter Constructor");
        mExercise = exercise;
        mSetList = setList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.component_card_exercise_session_history_set, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        TextView setNumber = holder.mSetNumber;
        TextView setInfo = holder.mSetInfo;
        String setInfoText = "";
        Set set = mSetList.get(position);

        setNumber.setText(String.valueOf(position + 1));

        int suffixLength = 0;
        for (MeasurementData data : set.measurements()) {
            String suffix;
            if (data.getCategory() == MeasurementCategory.REPS) {
                suffix = " X ";
                suffixLength = 3;
            } else {
                suffix = ", ";
                suffixLength = 2;
            }
            setInfoText += data.display() + suffix;
        }
        setInfoText = setInfoText.substring(0, setInfoText.length() - suffixLength);
        setInfo.setText(setInfoText);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return mSetList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mSetNumber;
        public TextView mSetInfo;

        public ViewHolder(View v) {
            super(v);
            mSetNumber = (TextView) v.findViewById(R.id.card_text_exercise_history_set_number);
            assert mSetNumber != null;
            mSetInfo = (TextView) v.findViewById(R.id.card_text_exercise_history_set_info);
            assert mSetInfo != null;
        }
    }
}