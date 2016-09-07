package edu.umn.paull011.evolveworkoutlogger.helper_classes;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * A dialog that prompts the user to confirm a decision
 *
 * Created by Mitchell on 6/1/2016.
 */
public class AreYouSureDialog extends DialogFragment {

    DialogChooserListener mListener;
    private static final String TAG = AreYouSureDialog.class.getSimpleName();


    public interface DialogChooserListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    public static AreYouSureDialog newInstance(String message) {
        AreYouSureDialog dialog = new AreYouSureDialog();
        Bundle args = new Bundle();
        args.putString("Message", message);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = this.getArguments().getString("Message");
        builder.setMessage(message)
                .setPositiveButton(edu.umn.paull011.evolveworkoutlogger.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null) {
                            mListener.onDialogPositiveClick(AreYouSureDialog.this);
                        }
                    }
                })
                .setNegativeButton(edu.umn.paull011.evolveworkoutlogger.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mListener != null) {
                            mListener.onDialogNegativeClick(AreYouSureDialog.this);
                        }
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogChooserListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DialogChooserListener");
        }
    }
}
