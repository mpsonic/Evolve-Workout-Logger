package com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 *
 * A RecyclerView click listener that allows each list item to be clicked
 *
 * Created by Mitchell on 1/14/2016.
 */
public class RecyclerViewItemClickListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;


    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }

    public RecyclerViewItemClickListener(Context context, OnItemClickListener listener){
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View childView = rv.findChildViewUnder(e.getX(), e.getY());
        if( childView != null && mListener != null && mGestureDetector.onTouchEvent(e)){
            mListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }
}
