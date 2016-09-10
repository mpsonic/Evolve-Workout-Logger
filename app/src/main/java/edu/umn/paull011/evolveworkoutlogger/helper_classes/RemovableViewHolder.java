package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import edu.umn.paull011.evolveworkoutlogger.R;

/**
 * A RecyclerView.ViewHolder class that encapsulates the "swipable" part of the view.
 * Created by mitchell on 9/9/16.
 */
public class RemovableViewHolder extends RecyclerView.ViewHolder {
    private View mRemovableView;

    public RemovableViewHolder(View itemView) {
        super(itemView);

        mRemovableView = itemView.findViewById(R.id.removable);
    }

    public View getSwipableView() {
        return mRemovableView;
    }
}
