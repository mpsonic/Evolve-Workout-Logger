package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

/**
 * Callbacks to be triggered by an ItemTouchHelper
 *
 * Created by Mitchell on 8/1/2016.
 */
public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;
    private final static String TAG = ItemTouchHelperCallback.class.getSimpleName();

    public ItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        Log.d(TAG,"ItemTouchHelperCallback");
        mAdapter = adapter;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        Log.d(TAG,"getMovementFlags");
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.START;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        Log.d(TAG,"onMove");
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Log.d(TAG,"onSwiped");
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        Log.d(TAG,"onSwiped");
        return true;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        Log.d(TAG,"isLongPressDragEnabled");
        return true;
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        getDefaultUIUtil().clearView(((RemovableViewHolder) viewHolder).getSwipableView());
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            getDefaultUIUtil().onSelected(((RemovableViewHolder) viewHolder).getSwipableView());
        }
    }

    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        getDefaultUIUtil().onDraw(c, recyclerView, ((RemovableViewHolder) viewHolder).getSwipableView(), dX, dY,    actionState, isCurrentlyActive);
    }

    public void onChildDrawOver(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        getDefaultUIUtil().onDrawOver(c, recyclerView, ((RemovableViewHolder) viewHolder).getSwipableView(), dX, dY,    actionState, isCurrentlyActive);
    }
}
