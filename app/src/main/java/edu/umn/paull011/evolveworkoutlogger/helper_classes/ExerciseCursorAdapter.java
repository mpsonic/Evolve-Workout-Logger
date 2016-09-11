package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.umn.paull011.evolveworkoutlogger.data_structures.DatabaseHelper;

/**
 * A RecyclerView adapter that lists Exercises gotten from a database Cursor
 *
 * Created by Mitchell on 5/31/2016.
 */
public class ExerciseCursorAdapter
        extends RecyclerView.Adapter<ExerciseCursorAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

    private Cursor mCursor;
    private Context mContext;
    private int mPosition;
    private String mExerciseName;
    private static final String TAG = ExerciseCursorAdapter.class.getSimpleName();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RemovableViewHolder{
        public TextView text;

        public ViewHolder(View v){
            super(v);
            text = (TextView) v.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.text_line);
        }
    }

    // Constructor sets the adapter data
    public ExerciseCursorAdapter(Context context){
        mContext = context;
        refreshCursor();
    }

    public void refreshCursor() {
        if (mCursor != null) {
            mCursor.close();
        }
        DatabaseHelper db = DatabaseHelper.getInstance(mContext);
        mCursor = db.getExercisesCursor(null);
    }

    // Create new views (Invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(edu.umn.paull011.evolveworkoutlogger.R.layout.one_line_list_item, parent, false);

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

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        return false;
    }

    @Override
    public void onItemDismiss(RecyclerView.ViewHolder viewHolder) {
        int position = viewHolder.getAdapterPosition();
        Log.d(TAG,"onItemDismiss (" + position + ")");
        mCursor.moveToPosition(position);
        String exerciseName = mCursor.getString(0);
        mPosition = position;
        mExerciseName = exerciseName;
        AreYouSureDialog dialog = AreYouSureDialog.newInstance(
                "Delete exercise \"" + exerciseName + "\"? All data related to this exercise " +
                        "will be deleted."
        );
        dialog.show(((Activity) mContext).getFragmentManager(), "AreYouSureDialog");
    }


    public void deleteSwipedExercise() {
        DatabaseHelper db = DatabaseHelper.getInstance(mContext);
        db.deleteExercise(mExerciseName);
        refreshCursor();
        notifyItemRemoved(mPosition);
    }

    public void unDismissSwipedExercise() {
        notifyItemChanged(mPosition);
    }

    public String getExerciseNameAtPosition(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getString(0);
    }

    public boolean isEmpty() {
        return (mCursor.getCount() == 0);
    }
}
