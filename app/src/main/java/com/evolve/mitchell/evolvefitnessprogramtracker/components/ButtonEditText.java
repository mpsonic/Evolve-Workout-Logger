package com.evolve.mitchell.evolvefitnessprogramtracker.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
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
    private boolean mAlwaysPositive = false;
    private float mIncrement = 1;
    private float mNumber = 0;
    private boolean mIsInteger;
    private String mHint = null;
    private OnNumberChangedListener onNumberChangedListener = null;

    private EditText mEditText;
    private ImageButton mMoreButton;
    private ImageButton mLessButton;

    public ButtonEditText(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ButtonEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ButtonEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }


    private void init(Context context, AttributeSet attrs, int defStyle) {

        // Load custom attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ButtonEditText, defStyle, 0);

        mActivated = a.getBoolean(
                R.styleable.ButtonEditText_activated,
                mActivated);
        mAlwaysPositive = a.getBoolean(
                R.styleable.ButtonEditText_always_positive,
                mAlwaysPositive);
        mIncrement = a.getFloat(
                R.styleable.ButtonEditText_increment,
                mIncrement);
        mNumber = a.getFloat(
                R.styleable.ButtonEditText_default_number,
                mNumber);
        mHint = a.getString(R.styleable.ButtonEditText_hint);
        int mode = a.getInteger(R.styleable.ButtonEditText_mode, 1);
        mIsInteger = (mode == 0);

        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.component_button_edit_text, this, true);

        LayoutParams params = new LinearLayout.LayoutParams(context, attrs);
        /*int pixelHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                R.dimen.clickable_target_dim,
                getResources().getDisplayMetrics());
        params.height = pixelHeight;

        setLayoutParams(params);*/
        setGravity(Gravity.CENTER);

        // Get the views
        mEditText = (EditText) this.findViewById(R.id.number_edit_text);
        mMoreButton = (ImageButton) this.findViewById(R.id.button_more);
        mLessButton = (ImageButton) this.findViewById(R.id.button_less);
        assert mEditText != null;
        assert mMoreButton != null;
        assert mLessButton != null;

        if (mIsInteger) {
            mNumber = Math.round(mNumber);
            mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else {
            mEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }

        if (mHint != null) {
            mEditText.setHint(mHint);
        }
        else {
            mEditText.setText(getNumberString());
        }


        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String text = mEditText.getText().toString();
                try {
                    mNumber = Float.valueOf(text);
                    onNumberChanged();
                } catch (NumberFormatException e) {
                    if (!text.equals("")) {
                        mEditText.setText(getNumberString());
                    }
                }
            }
        });

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setActivated(hasFocus);
            }
        });

        mMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(v);
            }
        });

        mLessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(v);
            }
        });

        if (!mActivated) {
            mMoreButton.setVisibility(View.GONE);
            mLessButton.setVisibility(View.GONE);
        }
    }

    private void handleButtonClick(View view) {
        ImageButton button = (ImageButton) view;
        EditText editText = (EditText) findViewById(R.id.number_edit_text);
        switch (button.getId()) {
            case R.id.button_less:
                if (!(mAlwaysPositive && mNumber <= 0)) {
                    mNumber -= mIncrement;
                    editText.setText(getNumberString());
                }
                break;
            case R.id.button_more:
                mNumber += mIncrement;
                editText.setText(getNumberString());
                break;
        }
    }

    private void onNumberChanged() {
        if (onNumberChangedListener != null) {
            onNumberChangedListener.numberChanged();
        }
    }

    public void setOnNumberChangedListener(OnNumberChangedListener listener) {
        onNumberChangedListener = listener;
    }

    public String getNumberString() {
        if (mIsInteger) {
            int intNum = (int) mNumber;
            return String.valueOf(intNum);
        }
        return String.valueOf(mNumber);
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
            if (mActivated) {
                mLessButton.setVisibility(View.VISIBLE);
                mMoreButton.setVisibility(View.VISIBLE);
            }
            else {
                mLessButton.setVisibility(View.GONE);
                mMoreButton.setVisibility(View.GONE);
            }
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

    public float getNumber() {
        return mNumber;
    }

    public void setNumber(float number) {
        mNumber = number;
        mEditText.setText(getNumberString());
    }
}