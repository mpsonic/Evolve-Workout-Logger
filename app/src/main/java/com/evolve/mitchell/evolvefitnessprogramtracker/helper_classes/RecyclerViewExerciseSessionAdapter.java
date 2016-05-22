package com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;
import com.evolve.mitchell.evolvefitnessprogramtracker.components.ButtonEditText;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.ExerciseSession;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.MeasurementCategory;
import com.evolve.mitchell.evolvefitnessprogramtracker.data_structures.Set;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class RecyclerViewExerciseSessionAdapter extends RecyclerView.Adapter<RecyclerViewExerciseSessionAdapter.ViewHolder> {

    private static final String TAG = RecyclerViewCursorAdapter.class.getSimpleName();
    private ExerciseSession mExerciseSession;


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mSetNumber;
        public List<FrameLayout> mMeasurementLayouts;
        public List<ButtonEditText> mButtonEditTexts;
        public ButtonEditText mRepsEdit;
        public ButtonEditText mWeightEdit;
        public ButtonEditText mDistanceEdit;
        public ButtonEditText mTimeEdit;

        public ViewHolder(View v){
            super(v);
            mSetNumber = (TextView) v.findViewById(R.id.set_number);
            mMeasurementLayouts = new ArrayList<>(4);
            mButtonEditTexts = new ArrayList<>(4);
            mMeasurementLayouts.set(MeasurementCategory.REPS.value(),
                    (FrameLayout) v.findViewById(R.id.reps_layout));
            mMeasurementLayouts.set(MeasurementCategory.WEIGHT.value(),
                    (FrameLayout) v.findViewById(R.id.weight_layout));
            mMeasurementLayouts.set(MeasurementCategory.DISTANCE.value(),
                    (FrameLayout) v.findViewById(R.id.distance_layout));
            mMeasurementLayouts.set(MeasurementCategory.TIME.value(),
                    (FrameLayout) v.findViewById(R.id.time_layout));
            mButtonEditTexts.set(MeasurementCategory.REPS.value(),
                    (ButtonEditText) v.findViewById(R.id.reps_button_edittext));
            mButtonEditTexts.set(MeasurementCategory.WEIGHT.value(),
                    (ButtonEditText) v.findViewById(R.id.weight_button_edittext));
            mButtonEditTexts.set(MeasurementCategory.DISTANCE.value(),
                    (ButtonEditText) v.findViewById(R.id.distance_button_edittext));
            mButtonEditTexts.set(MeasurementCategory.TIME.value(),
                    (ButtonEditText) v.findViewById(R.id.time_button_edittext));
        }
    }

    public RecyclerViewExerciseSessionAdapter(ExerciseSession exerciseSession){
        mExerciseSession = exerciseSession;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        // Create a new exercise-session view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_exercise_session_display, parent, false);
        // pass the view to into the view holder
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        Set set = mExerciseSession.getSet(position);
        TextView setNumberText = holder.mSetNumber;
        setNumberText.setText(String.valueOf(position));
        // Disappear layouts for untracked measurements
        for (MeasurementCategory category: MeasurementCategory.values()) {
            if (!mExerciseSession.hasCategory(category)) {
                holder.mMeasurementLayouts.get(category.value()).setVisibility(View.GONE);
            }
            else {
                float categoryMeasurement = set.getMeasurementData(category).getMeasurement();
                holder.mButtonEditTexts.get(category.value()).setNumber(categoryMeasurement);
            }
        }
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return mExerciseSession.getNumSets();
    }
}
