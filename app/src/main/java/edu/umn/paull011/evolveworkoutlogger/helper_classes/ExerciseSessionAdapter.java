package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.components.ButtonEditText;
import edu.umn.paull011.evolveworkoutlogger.components.OnNumberChangedListener;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Exercise;
import edu.umn.paull011.evolveworkoutlogger.data_structures.ExerciseSession;
import edu.umn.paull011.evolveworkoutlogger.data_structures.MeasurementCategory;
import edu.umn.paull011.evolveworkoutlogger.data_structures.Set;


/**
 * Adapter that populates a RecyclerView with the sets inside of an exercise session
 */
public class ExerciseSessionAdapter extends RecyclerView.Adapter<ExerciseSessionAdapter.ViewHolder>
    implements ItemTouchHelperAdapter{

    private ExerciseSession mExerciseSession;
    private Exercise mExercise;
    private int mCompletedColor;
    private int mIncompletedColor;
    private Context mContext;
    private ExerciseSessionDataHolder dataHolder = ExerciseSessionDataHolder.getInstance();
    private static final String TAG = ExerciseSessionAdapter.class.getSimpleName();

    public class SetMeasurementChangedListener implements OnNumberChangedListener {
        int mSetPosition;
        MeasurementCategory mCategory;
        ButtonEditText mButtonEditText;
        private final String TAG = SetMeasurementChangedListener.class.getSimpleName();

        public SetMeasurementChangedListener(int setPosition, MeasurementCategory category, ButtonEditText buttonEditText) {
            Log.d(TAG,"setMeasurementChangedListener");
            mSetPosition = setPosition;
            mCategory = category;
            mButtonEditText = buttonEditText;
        }

        @Override
        public void numberChanged() {
            Log.d(TAG,"numberChanged");
            float number = mButtonEditText.getNumber();
            Set editedSet = mExerciseSession.getSet(mSetPosition);
            editedSet.setMeasurement(mCategory, number);
        }
    }

    public class ViewHolder extends RemovableViewHolder{
        public RelativeLayout mRelativeLayout;
        public TextView mSetNumber;
        public ImageView mCurrentSetCircle;
        public List<FrameLayout> mMeasurementLayouts;
        public List<ButtonEditText> mButtonEditTexts;
        public final String TAG = ExerciseSessionAdapter.ViewHolder.class.getSimpleName();

        public ViewHolder(View v){
            super(v);
            Log.d(TAG,"ViewHolder");
            mRelativeLayout = (RelativeLayout) this.getSwipableView();
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

    public ExerciseSessionAdapter(Context context){
        Log.d(TAG,"ExerciseSessionAdapter");
        mContext = context;
        mExercise = dataHolder.getExercise();
        mExerciseSession = dataHolder.getExerciseSession();
        mCompletedColor = ContextCompat.getColor(context, R.color.accent_green);
        mIncompletedColor = ContextCompat.getColor(context, R.color.white);
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
    public void onViewDetachedFromWindow(ViewHolder holder) {
        int position = holder.getAdapterPosition();
        Log.d(TAG, "onViewDetachedFromWindow (" + position + ")");
        super.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        Set set = mExerciseSession.getSet(position);
        TextView setNumberText = holder.mSetNumber;
        setNumberText.setText(String.valueOf(position + 1));
        holder.mRelativeLayout.setBackgroundColor(mIncompletedColor);
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
                SetMeasurementChangedListener listener =
                        new SetMeasurementChangedListener(position, category, buttonEditText);
                buttonEditText.setOnNumberChangedListener(listener);
                buttonEditText.setNumber(categoryMeasurement);
                if (category == MeasurementCategory.WEIGHT || category == MeasurementCategory.DISTANCE) {
                    String unitText = mExercise.getUnit(category).getDisplayName();
                    buttonEditText.setUnit(unitText);
                }
            }
        }
        if (set.isCompleted()) {
            holder.mRelativeLayout.setBackgroundColor(mCompletedColor);
        }
    }


    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount");
        return mExerciseSession.getNumSets();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Log.d(TAG,"onItemMove");
        mExerciseSession.swapSets(fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(RecyclerView.ViewHolder viewHolder) {
        ExerciseSessionAdapter.ViewHolder vh = (ExerciseSessionAdapter.ViewHolder) viewHolder;
        int position = vh.getAdapterPosition();
        Log.d(TAG,"onItemDismiss (" + position + ")");

        List<ButtonEditText> bets = vh.mButtonEditTexts;
        for (ButtonEditText bet: bets) {
            bet.clearFocus();
        }

        mExerciseSession.removeSet(position);
        notifyItemRemoved(position);
        if (position != 0) {
            notifyItemRangeChanged(position, mExerciseSession.getNumSets() - position);
        }
        else {
            notifyDataSetChanged();
        }
    }


    // Methods below overridden to provide log messages
    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        Log.d(TAG, "onBindViewHolder (" + position + ")");
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType");
        return super.getItemViewType(position);
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
        Log.d(TAG, "setHasStableIds");
    }

    @Override
    public long getItemId(int position) {
        Log.d(TAG, "getItemId");
        return super.getItemId(position);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        int position = holder.getAdapterPosition();
        Log.d(TAG, "onViewRecycled (" + position + ")");
    }

    @Override
    public boolean onFailedToRecycleView(ViewHolder holder) {
        int position = holder.getAdapterPosition();
        Log.d(TAG, "onFailedToRecycleView (" + position + ")");
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();
        Log.d(TAG, "onViewAttachedToWindow (" + position + ")");
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(observer);
        Log.d(TAG, "registerAdapterDataObserver");
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        Log.d(TAG, "unregisterAdapterDataObserver");
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        Log.d(TAG, "onAttachedToRecyclerView");
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        Log.d(TAG, "onDetachedFromRecyclerView");
    }

}
