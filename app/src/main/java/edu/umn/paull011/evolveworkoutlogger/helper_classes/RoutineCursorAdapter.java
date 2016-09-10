package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.umn.paull011.evolveworkoutlogger.R;
import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;

/**
 * A RecyclerView adapter that lists Routines gotten from a database cursor
 *
 * Created by Mitchell on 5/31/2016.
 */
public class RoutineCursorAdapter
    extends RecyclerView.Adapter<RoutineCursorAdapter.ViewHolder>
    implements ItemTouchHelperAdapter{

    private static final String TAG = RoutineCursorAdapter.class.getSimpleName();
    private Cursor mCursor;
    private Activity mActivity;
    private String mRoutineName;
    private int mPosition;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RemovableViewHolder{
        public TextView primaryText;
        public TextView secondaryText;

        public ViewHolder(View v){
            super(v);
            primaryText = (TextView) v.findViewById(R.id.primary_text);
            secondaryText = (TextView) v.findViewById(R.id.secondary_text);
        }
    }

    // Constructor sets the adapter data
    public RoutineCursorAdapter(Activity activity){
        mActivity = activity;
        refreshCursor();
    }

    public void refreshCursor() {
        DatabaseHelper db = DatabaseHelper.getInstance(mActivity);
        mCursor = db.getRoutinesCursor();
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

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(int position) {
        Log.d(TAG,"onItemDismiss (" + position + ")");
        String routineName = getRoutineNameFromPosition(position);
        mPosition = position;
        mRoutineName = routineName;
        AreYouSureDialog dialog = AreYouSureDialog.newInstance(
                "Delete routine \"" + routineName + "\"? All data related to this routine " +
                        "will be deleted."
        );
        dialog.show(mActivity.getFragmentManager(), "AreYouSureDialog");
    }

    public String getRoutineNameFromPosition(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getString(0);
    }

    public void deleteSwipedRoutine() {
        DatabaseHelper db = DatabaseHelper.getInstance(mActivity);
        db.deleteRoutine(mRoutineName);
        refreshCursor();
        notifyItemRemoved(mPosition);
    }

    public void unDismissSwipedRoutine() {
        notifyItemChanged(mPosition);
    }

    public boolean isEmpty() {
        return (mCursor.getCount() == 0);
    }
}
