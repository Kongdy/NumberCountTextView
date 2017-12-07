package com.kongdy.numbercounttextviewlib.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import java.text.NumberFormat;

/**
 * @author kongdy
 * @date 2017/12/6 17:19
 * @describe 可自动计数的textView，只适用于数字类型
 **/
@SuppressLint("AppCompatCustomView")
public class NumberCountTextView extends TextView {

    private final static int UPDATE_TEXT = 0x00001;

    private final static String HANDLER_MSG_TAG = "handlerMsg";

    private final static long ANIMATION_DURATION = 500L;
    private final static long ANIMATION_START_DELAY = 200L;

    private final static int JUMP_INT_TYPE = 0x000011;
    private final static int JUMP_FLOAT_TYPE = 0x000012;

    private int jumpType = JUMP_FLOAT_TYPE;

    private Object value;
    private Object valueStub;

    private CharSequence txtStub;
    private boolean isHide = false;

    private NumberFormat numberFormat;

    public NumberCountTextView(Context context) {
        this(context, null);
    }

    public NumberCountTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public NumberCountTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Handler updateTextHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case UPDATE_TEXT: {
                    Bundle bundle = message.getData();
                    if (null != bundle) {
                        String txt = bundle.getString(HANDLER_MSG_TAG);
                        setText(txt);
                    }
                }
                break;
            }
            return false;
        }
    });

    private Integer getSafeInteger(Object toTransValue) {
        if (null == toTransValue)
            return 0;
        Integer safeInt;
        try {
            safeInt = Integer.parseInt(toTransValue.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
        return safeInt;
    }

    private Float getSafeFloat(Object toTransValue) {
        if (null == toTransValue)
            return 0f;
        Float safeFloat;
        try {
            safeFloat = Float.parseFloat(toTransValue.toString());
        } catch (NumberFormatException e) {
            return 0f;
        }
        return safeFloat;
    }


    private void startAnimationTxt() {
        if (jumpType == JUMP_INT_TYPE) {
            Integer valueStubInt = getSafeInteger(valueStub);
            Integer valueInt = getSafeInteger(value);
            ValueAnimator valueAnimator = ValueAnimator.ofInt(valueStubInt, valueInt);
            valueAnimator.setDuration(ANIMATION_DURATION);
            valueAnimator.setStartDelay(ANIMATION_START_DELAY);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Float tempValue = Float.valueOf(valueAnimator.getAnimatedValue().toString());
                    String toUpdateTxt;
                    if (null != numberFormat)
                        toUpdateTxt = numberFormat.format(tempValue);
                    else
                        toUpdateTxt = String.valueOf(tempValue);
                    sendUpdateMsg(toUpdateTxt);
                }
            });
            valueAnimator.start();
        } else if (jumpType == JUMP_FLOAT_TYPE) {
            Float valueStubFloat = getSafeFloat(valueStub);
            Float valueFloat = getSafeFloat(value);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(valueStubFloat, valueFloat);
            valueAnimator.setDuration(ANIMATION_DURATION);
            valueAnimator.setStartDelay(ANIMATION_START_DELAY);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    Float tempValue = Float.valueOf(valueAnimator.getAnimatedValue().toString());
                    String toUpdateTxt;
                    if (null != numberFormat)
                        toUpdateTxt = numberFormat.format(tempValue);
                    else
                        toUpdateTxt = String.format("%.2f", tempValue);
                    sendUpdateMsg(toUpdateTxt);
                }
            });
            valueAnimator.start();
        } else if (null != value) {
            setText(value.toString());
        }
        valueStub = value;
    }

    private void sendUpdateMsg(String txt) {
        updateTextHandler.removeMessages(UPDATE_TEXT);

        Bundle bundle = new Bundle();
        bundle.putString(HANDLER_MSG_TAG, txt);
        Message message = new Message();
        message.what = UPDATE_TEXT;
        message.setData(bundle);
        updateTextHandler.sendMessage(message);
    }

    public void setAnimationText(Integer value) {
        jumpType = JUMP_INT_TYPE;
        this.value = value;
        startAnimationTxt();
    }

    public void setAnimationText(Float value) {
        jumpType = JUMP_FLOAT_TYPE;
        this.value = value;
        startAnimationTxt();
    }

    public void hideTxt(String hideTxt) {
        txtStub = getText();
        setText(hideTxt);
        isHide = true;
    }

    public void showTxt() {
        isHide = false;
        setText(txtStub);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (isHide)
            return;
        super.setText(text, type);
    }

    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }
}
