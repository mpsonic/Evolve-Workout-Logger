package edu.umn.paull011.evolveworkoutlogger.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

/**
 * A numerical EditText with two arrow buttons to increase or decrease the number
 */
public class ButtonEditText extends LinearLayout {

    private boolean mActivated = false;
    private boolean mAlwaysPositive = false;
    private float mIncrement = 1;
    private float mNumber = 0;
    private boolean mIsInteger;
    private boolean mIsTime;
    private String mHint = null;
    private OnNumberChangedListener onNumberChangedListener = null;
    private static String TAG = ButtonEditText.class.getSimpleName();

    private EditText mEditText;
    private ImageButton mMoreButton;
    private ImageButton mLessButton;

    public ButtonEditText(Context context) {
        super(context);
        Log.d(TAG,"ButtonEditText");
        init(context, null, 0);
    }

    public ButtonEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG,"ButtonEditText");
        init(context, attrs, 0);
    }

    public ButtonEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Log.d(TAG,"ButtonEditText");
        init(context, attrs, defStyle);
    }


    private void init(Context context, AttributeSet attrs, int defStyle) {
        Log.d(TAG,"init");
        // Load custom attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, edu.umn.paull011.evolveworkoutlogger.R.styleable.ButtonEditText, defStyle, 0);

        mActivated = a.getBoolean(
                edu.umn.paull011.evolveworkoutlogger.R.styleable.ButtonEditText_activated,
                mActivated);
        mAlwaysPositive = a.getBoolean(
                edu.umn.paull011.evolveworkoutlogger.R.styleable.ButtonEditText_always_positive,
                mAlwaysPositive);
        mIncrement = a.getFloat(
                edu.umn.paull011.evolveworkoutlogger.R.styleable.ButtonEditText_increment,
                mIncrement);
        mNumber = a.getFloat(
                edu.umn.paull011.evolveworkoutlogger.R.styleable.ButtonEditText_default_number,
                mNumber);
        mHint = a.getString(edu.umn.paull011.evolveworkoutlogger.R.styleable.ButtonEditText_hint);
        int mode = a.getInteger(edu.umn.paull011.evolveworkoutlogger.R.styleable.ButtonEditText_mode, 1);
        mIsInteger = (mode == 0);
        mIsTime = (mode == 2);

        a.recycle();

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(edu.umn.paull011.evolveworkoutlogger.R.layout.component_button_edit_text, this, true);

        LayoutParams params = new LinearLayout.LayoutParams(context, attrs);
        /*int pixelHeight = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                R.dimen.clickable_target_dim,
                getResources().getDisplayMetrics());
        params.height = pixelHeight;

        setLayoutParams(params);*/
        setGravity(Gravity.CENTER);
        setFocusable(true);

        // Get the views
        mEditText = (EditText) this.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.edit_text);
        mMoreButton = (ImageButton) this.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.button_more);
        mLessButton = (ImageButton) this.findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.button_less);
        assert mEditText != null;
        assert mMoreButton != null;
        assert mLessButton != null;

        if (mIsInteger) {
            mNumber = Math.round(mNumber);
            mEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        else {
            if (mIsTime) {
                mEditText.setRawInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_TIME);
            }
            else {
                mEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }
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
                    if (!mIsTime) {
                        mNumber = Float.valueOf(text);
                    }
                    else {
                        mNumber = parseTimeString(text);
                    }
                    onNumberChanged();
                } catch (NumberFormatException e) {
                    if (!text.equals("") && !mIsTime) {
                        mEditText.setText(getNumberString());
                    }
                }
            }
        });

        mEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    setActivated(false);
                }
                mEditText.setText(getNumberString());
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
        Log.d(TAG,"handleButtonClick");
        ImageButton button = (ImageButton) view;
        EditText editText = (EditText) findViewById(edu.umn.paull011.evolveworkoutlogger.R.id.edit_text);
        switch (button.getId()) {
            case edu.umn.paull011.evolveworkoutlogger.R.id.button_less:
                if (!(mAlwaysPositive && mNumber <= 0)) {
                    mNumber -= mIncrement;
                    editText.setText(getNumberString());
                }
                break;
            case edu.umn.paull011.evolveworkoutlogger.R.id.button_more:
                mNumber += mIncrement;
                editText.setText(getNumberString());
                break;
        }
    }

    private void onNumberChanged() {
        Log.d(TAG,"onNumberChanged");
        if (onNumberChangedListener != null) {
            onNumberChangedListener.numberChanged();
        }
    }

    public void setOnNumberChangedListener(OnNumberChangedListener listener) {
        Log.d(TAG,"setOnNumberChangedListener");
        onNumberChangedListener = listener;
    }

    public String getNumberString() {
        Log.d(TAG,"getNumberString");
        if (mIsInteger) {
            int intNum = (int) mNumber;
            return String.valueOf(intNum);
        }
        else if (mIsTime) {
            int intNum = (int) mNumber;
            return createTimeString(intNum);
        }
        return String.valueOf(mNumber);
    }

    /***
     * Make a time-formatted string from an integer number of seconds
     */
    private String createTimeString(int seconds) {
        int hours = seconds/3600;
        int minutes = (seconds % 3600)/60;
        seconds = seconds % 60;
        String result;
        if (hours != 0) {
            result = String.valueOf(hours) + ":"
                    + fillOutLeadingZeros(String.valueOf(minutes),2) + ":"
                    + fillOutLeadingZeros(String.valueOf(seconds),2);
        }
        else if (minutes != 0) {
            result = String.valueOf(minutes) + ":"
                    + fillOutLeadingZeros(String.valueOf(seconds),2);
        }
        else {
            result = "0:" + fillOutLeadingZeros(String.valueOf(seconds), 2);
        }
        return result;
    }

    private int parseTimeString(String timeString) {
        String[] timeComponents = timeString.split(":");
        int seconds = 0;
        if (timeComponents.length == 3) { // has hour component
            seconds += Integer.valueOf(timeComponents[0]) * 3600;
            seconds += Integer.valueOf(timeComponents[1]) * 60;
            seconds += Integer.valueOf(timeComponents[2]);
        }
        else if (timeComponents.length == 2) {
            seconds += Integer.valueOf(timeComponents[0]) * 60;
            seconds += Integer.valueOf(timeComponents[1]);
        }
        else if (timeComponents.length == 1) {
            seconds += Integer.valueOf(timeComponents[0]);
        }
        return seconds;
    }

    /***
     * If the given string is shorter than the desired length, pad out the beginning of
     * the string with zeros so that the string has a length of desiredLength
     * @param numString An unpadded number string
     * @param desiredLength The desired length of the string with zeroes prepended
     * @return numString padded with leading zeros
     */
    private String fillOutLeadingZeros(String numString, int desiredLength) {
        String result = "";
        int numZerosToAdd = desiredLength - numString.length();
        if (numZerosToAdd <= 0) {
            return numString;
        }
        else {
            for (int i = 0; i < numZerosToAdd; i++) {
                result += "0";
            }
            result += numString;
            return result;
        }
    }


    /**
     * Gets the activated attribute value.
     *
     * @return The activated attribute value.
     */
    public boolean getActivated() {
        Log.d(TAG,"getActivated");
        return mActivated;
    }

    /**
     * Sets the view's activated attribute value. If the view is active, the
     * buttons are shown, otherwise the buttons are hidden.
     *
     * @param activated The activated attribute value to use.
     */
    public void setActivated(boolean activated) {
        Log.d(TAG,"setActivated");
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
        Log.d(TAG,"getIncrement");
        return mIncrement;
    }

    /**
     * Sets the view's increment attribute value. The edittext number will
     * change by the given increment value for each button click.
     *
     * @param increment The activated increment value to use
     */
    public void setIncrement(float increment) {
        Log.d(TAG,"setIncrement");
        mIncrement = increment;
    }

    public float getNumber() {
        Log.d(TAG,"getNumber");
        return mNumber;
    }

    public void setNumber(float number) {
        Log.d(TAG,"setNumber");
        mNumber = number;
        mEditText.setText(getNumberString());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.d(TAG,"onInterceptTouchEvent");
        return !mActivated;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG,"onTouchEvent");
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (!mActivated) {
                setActivated(true);
                this.requestFocus();
            }
        }
        return true;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        Log.d(TAG,"onFocusChanged");
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (!gainFocus) {
            if (this.findFocus() == null) {
                setActivated(false);
            }
        }
    }
}