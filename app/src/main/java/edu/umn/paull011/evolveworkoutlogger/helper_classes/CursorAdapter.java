package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 *
 * A RecyclerView adapter that can populate the view with
 * list items (two line text views) given a database cursor
 *
 * Created by Mitchell on 1/13/2016.
 */
public class CursorAdapter
        extends RecyclerView.Adapter<CursorAdapter.ViewHolder>{

    private Cursor mCursor;
    private static final String TAG = CursorAdapter.class.getSimpleName();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public View mView;

        public ViewHolder(View v){
            super(v);
            mView = v;
        }

    }

    // Constructor sets the adapter data
    public CursorAdapter(Cursor cursor){
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
                .inflate(edu.umn.paull011.evolveworkoutlogger.R.layout.name_and_description_row, parent, false);

        // set the view's size, margins, padding, and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (Invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        TextView nameText = (TextView) holder.mView.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.rowName);
        TextView descriptionText = (TextView) holder.mView.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.rowDescription);
        String name = mCursor.getString(1);
        String description = mCursor.getString(2);
        nameText.setText(name);
        descriptionText.setText(description);
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
