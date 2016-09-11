package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.support.v7.widget.RecyclerView;

/**
 * Interface to make a RecyclerView adapter into an item touch helper
 * (for handling moving and dismissing)
 *
 * Created by Mitchell on 8/1/2016.
 */
public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(RecyclerView.ViewHolder viewHolder);
}
