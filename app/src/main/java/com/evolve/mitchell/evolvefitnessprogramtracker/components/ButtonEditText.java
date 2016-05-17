package com.evolve.mitchell.evolvefitnessprogramtracker.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.evolve.mitchell.evolvefitnessprogramtracker.R;

/**
 * A numerical EditText with two arrow buttons to increase or decrease the number
 */
public class ButtonEditText extends LinearLayout {

    private boolean mActivated = false;
    private float mIncrement = 1;
    private float mNumber = 0;

    private EditText mEditText;

    public ButtonEditText(Context context) {
        super(context);
        init(null, 0);
    }

    public ButtonEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public ButtonEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ButtonEditText, defStyle, 0);

        mActivated = a.getBoolean(
                R.styleable.ButtonEditText_activated,
                mActivated);
        mIncrement = a.getFloat(
                R.styleable.ButtonEditText_increment,
                mIncrement);

        a.recycle();

        mEditText = (EditText) findViewById(R.id.number_edit_text);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                mNumber = Float.valueOf(mEditText.getText().toString());
            }
        });
    }

    private void handleButtonClick(View view) {
        ImageButton button = (ImageButton) view;
        EditText editText = (EditText) findViewById(R.id.number_edit_text);
        float edittextNum = Float.valueOf(editText.getText().toString());
        switch (button.getId()) {
            case R.id.button_less:
                edittextNum = edittextNum - mIncrement;
                editText.setText(String.valueOf(edittextNum));
                break;
            case R.id.button_more:
                edittextNum = edittextNum + mIncrement;
                editText.setText(String.valueOf(edittextNum));
                break;
        }
    }

    /**
     * Gets the activated attribute value.
     *
     * @return The activated attribute value.
     */
    public boolean getActivated() {
        return mActivated;
    }

    /**
     * Sets the view's activated attribute value. If the view is active, the
     * buttons are shown, otherwise the buttons are hidden.
     *
     * @param activated The activated attribute value to use.
     */
    public void setActivated(boolean activated) {
        if (mActivated != activated) {
            mActivated = activated;
            requestLayout();
        }
    }

    /**
     * Gets the increment attribute value.
     *
     * @return The increment attribute value.
     */
    public float getIncrement() {
        return mIncrement;
    }

    /**
     * Sets the view's increment attribute value. The edittext number will
     * change by the given increment value for each button click.
     *
     * @param increment The activated increment value to use
     */
    public void setIncrement(float increment) {
        mIncrement = increment;
    }
}
