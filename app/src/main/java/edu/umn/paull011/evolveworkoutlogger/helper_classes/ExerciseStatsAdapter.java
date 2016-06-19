package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.ExerciseStats;
import edu.umn.paull011.evolveworkoutlogger.data_structures.MeasurementCategory;

/**
 * Adapter for displaying ExerciseStats data
 *
 * Created by Mitchell on 6/18/2016.
 */
public class ExerciseStatsAdapter extends RecyclerView.Adapter<ExerciseStatsAdapter.ViewHolder> {

    public ExerciseStats mExerciseStats;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mCategory;
        public TextView mAvg;
        public TextView mMax;

        public ViewHolder(View v) {
            super(v);
            mCategory = (TextView) v.findViewById(R.id.stat_category_text);
            mAvg = (TextView) v.findViewById(R.id.stat_avg_text);
            mMax = (TextView) v.findViewById(R.id.stat_max_text);
        }
    }

    public ExerciseStatsAdapter(ExerciseStats exerciseStats) {
        mExerciseStats = exerciseStats;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.component_stat, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int count = -1;
        for (MeasurementCategory category: MeasurementCategory.values()) {
            if (mExerciseStats.isTracked(category)) {
                count++;
                if (count == position) {
                    holder.mCategory.setText(category.name());
                    holder.mAvg.setText(String.valueOf(mExerciseStats.getAvg(category)));
                    holder.mMax.setText(String.valueOf(mExerciseStats.getMax(category)));
                    break;
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for (MeasurementCategory category: MeasurementCategory.values()) {
            if (mExerciseStats.isTracked(category)) {
                count++;
            }
        }
        return count;
    }
}
