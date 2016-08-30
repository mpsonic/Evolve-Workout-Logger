package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

/**
 * An ItemTouchHelper that prints debug logging messages
 *
 * Created by Mitchell on 8/3/2016.
 */
public class TestItemTouchHelper extends ItemTouchHelper{

    private static String TAG = TestItemTouchHelper.class.getSimpleName();

    public TestItemTouchHelper(Callback callback) {
        super(callback);
        Log.d(TAG, "TestItemTouchHelper");
    }

    @Override
    public void attachToRecyclerView(@Nullable RecyclerView recyclerView) {
        Log.d(TAG, "attachToRecyclerView");
        super.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        Log.d(TAG, "onDrawOver");
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        Log.d(TAG, "onDraw");
        super.onDraw(c, parent, state);
    }

    @Override
    public void onChildViewAttachedToWindow(View view) {
        Log.d(TAG, "onChildViewAttachedToWindow");
        super.onChildViewAttachedToWindow(view);
    }

    @Override
    public void onChildViewDetachedFromWindow(View view) {
        Log.d(TAG, "onChildViewDetachedFromWindow");
        super.onChildViewDetachedFromWindow(view);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        Log.d(TAG, "getItemOffsets");
        super.getItemOffsets(outRect, view, parent, state);
    }

    @Override
    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        Log.d(TAG, "startDrag");
        super.startDrag(viewHolder);
    }

    @Override
    public void startSwipe(RecyclerView.ViewHolder viewHolder) {
        Log.d(TAG, "startSwipe");
        super.startSwipe(viewHolder);
    }
}
