package com.evolve.mitchell.evolvefitnessprogramtracker.helper_classes;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;

/**
 * A RecyclerView adapter that lists Routines gotten from a database cursor
 *
 * Created by Mitchell on 5/31/2016.
 */
public class RoutineCursorAdapter
    extends RecyclerView.Adapter<RoutineCursorAdapter.ViewHolder>{

    private static final String TAG = RoutineCursorAdapter.class.getSimpleName();
    private Cursor mCursor;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView primaryText;
        public TextView secondaryText;

        public ViewHolder(View v){
            super(v);
            primaryText = (TextView) v.findViewById(R.id.primary_text);
            secondaryText = (TextView) v.findViewById(R.id.secondary_text);
        }
    }

    // Constructor sets the adapter data
    public RoutineCursorAdapter(Cursor cursor){
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
                .inflate(R.layout.two_line_list_item, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (Invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String name = mCursor.getString(0);
        String description = mCursor.getString(1);
        holder.primaryText.setText(name);
        holder.secondaryText.setText(description);
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
