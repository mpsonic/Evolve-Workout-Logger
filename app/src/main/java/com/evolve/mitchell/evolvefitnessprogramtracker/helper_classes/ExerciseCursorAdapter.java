package com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;

/**
 * A RecyclerView adapter that lists Exercises gotten from a database Cursor
 *
 * Created by Mitchell on 5/31/2016.
 */
public class ExerciseCursorAdapter
        extends RecyclerView.Adapter<ExerciseCursorAdapter.ViewHolder>{

    private static final String TAG = ExerciseCursorAdapter.class.getSimpleName();
    private Cursor mCursor;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView text;

        public ViewHolder(View v){
            super(v);
            text = (TextView) v.findViewById(R.id.text_line);
        }
    }

    // Constructor sets the adapter data
    public ExerciseCursorAdapter(Cursor cursor){
        setCursor(cursor);
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
    }


    // Create new views (Invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.one_line_list_item, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (Invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String name = mCursor.getString(0);
        holder.text.setText(name);
    }

    // Return the size of your data set
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    // Close the cursor when the recycler view stops needing the cursor
    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mCursor.close();
    }

}
