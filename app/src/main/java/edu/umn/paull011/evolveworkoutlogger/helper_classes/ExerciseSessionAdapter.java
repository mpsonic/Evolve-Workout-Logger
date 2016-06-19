package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.components.ButtonEditText;
import edu.umn.paull011.evolveworkoutlogger.components.OnNumberChangedListener;
import edu.umn.paull011.evolveworkoutlogger.data_structures.ExerciseSession;
import edu.umn.paull011.evolveworkoutlogger.data_structures.MeasurementCategory;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Set;


/**
 *
 */
public class ExerciseSessionAdapter extends RecyclerView.Adapter<ExerciseSessionAdapter.ViewHolder> {

    private ExerciseSession mExerciseSession;
    private int mCompletedColor;
    private static final String TAG = ExerciseSessionAdapter.class.getSimpleName();

    public class SetMeasurementChangedListener implements OnNumberChangedListener {
        int mSetPosition;
        MeasurementCategory mCategory;
        ButtonEditText mButtonEditText;

        public SetMeasurementChangedListener(int setPosition, MeasurementCategory category, ButtonEditText buttonEditText) {
            mSetPosition = setPosition;
            mCategory = category;
            mButtonEditText = buttonEditText;
        }

        @Override
        public void numberChanged() {
            mExerciseSession.getSet(mSetPosition).setMeasurement(mCategory, mButtonEditText.getNumber());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewGroup mViewGroup;
        public TextView mSetNumber;
        public ImageView mCurrentSetCircle;
        public List<FrameLayout> mMeasurementLayouts;
        public List<ButtonEditText> mButtonEditTexts;

        public ViewHolder(View v){
            super(v);
            mViewGroup = (ViewGroup) v;
            mSetNumber = (TextView) v.findViewById(R.id.set_number);
            mCurrentSetCircle = (ImageView) v.findViewById(R.id.current_set_circle);
            mMeasurementLayouts = new ArrayList<>(4);
            mButtonEditTexts = new ArrayList<>(4);
            for (int i = 0; i < 4; i++) {
                mMeasurementLayouts.add(null);
                mButtonEditTexts.add(null);
            }
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

    public ExerciseSessionAdapter(ExerciseSession exerciseSession, int completedColor){
        mExerciseSession = exerciseSession;
        mCompletedColor = completedColor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        // Create a new exercise-session view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.component_set, parent, false);
        // pass the view to into the view holder
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        Set set = mExerciseSession.getSet(position);
        TextView setNumberText = holder.mSetNumber;
        setNumberText.setText(String.valueOf(position + 1));
        if (mExerciseSession.getCurrentSetIndex() == position && !mExerciseSession.isCompleted()) {
            holder.mCurrentSetCircle.setVisibility(View.VISIBLE);
        }
        else {
            holder.mCurrentSetCircle.setVisibility(View.GONE);
        }
        // Disappear layouts for untracked measurements
        ButtonEditText buttonEditText;
        for (final MeasurementCategory category: MeasurementCategory.values()) {
            if (!mExerciseSession.hasCategory(category)) {
                holder.mMeasurementLayouts.get(category.value()).setVisibility(View.GONE);
            }
            else {
                float categoryMeasurement = set.getMeasurementData(category).getMeasurement();
                buttonEditText = holder.mButtonEditTexts.get(category.value());
                buttonEditText.setNumber(categoryMeasurement);
                SetMeasurementChangedListener listener =
                        new SetMeasurementChangedListener(position, category, buttonEditText);
                buttonEditText.setOnNumberChangedListener(listener);
            }
        }
        if (set.isCompleted()) {
            holder.mViewGroup.setBackgroundColor(mCompletedColor);
        }
    }


    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return mExerciseSession.getNumSets();
    }
}
