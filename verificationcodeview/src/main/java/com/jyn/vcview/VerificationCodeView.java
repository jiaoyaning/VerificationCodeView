package com.jyn.vcview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

/**
 * @author jiaoyaning
 * @package com.b2cf.nonghe.widget
 * @filename VerificationCodeView
 * @date 2017/10/31
 */

public class VerificationCodeView extends LinearLayout implements TextWatcher, View.OnKeyListener, View.OnFocusChangeListener {

    private Context mContext;
    private long endTime = 0;
    private OnCodeFinishListener onCodeFinishListener;

    /**
     * 输入框数量
     */
    private int mEtNumber;

    /**
     * 输入框的宽度
     */
    private int mEtWidth;

    /**
     * 文字颜色
     */
    private int mEtTextColor;

    /**
     * 文字大小
     */
    private float mEtTextSize;

    /**
     * 输入框背景
     */
    private int mEtTextBg;

    private int mCursorDrawable;

    public VerificationCodeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        @SuppressLint({"Recycle", "CustomViewStyleable"})
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.vericationCodeView);
        mEtNumber = typedArray.getInteger(R.styleable.vericationCodeView_vcv_et_number, 4);
        mEtWidth = typedArray.getDimensionPixelSize(R.styleable.vericationCodeView_vcv_et_width, 120);
        mEtTextColor = typedArray.getColor(R.styleable.vericationCodeView_vcv_et_text_color, Color.BLACK);
        mEtTextSize = typedArray.getDimensionPixelSize(R.styleable.vericationCodeView_vcv_et_text_size, 16);
        mEtTextBg = typedArray.getResourceId(R.styleable.vericationCodeView_vcv_et_bg, R.drawable.et_login_code);
        mCursorDrawable = typedArray.getResourceId(R.styleable.vericationCodeView_vcv_et_cursor, R.drawable.et_cursor);

        //释放资源
        typedArray.recycle();
        initView();
    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        for (int i = 0; i < mEtNumber; i++) {
            EditText editText = new EditText(mContext);
            int childHPadding = 14;
            int childVPadding = 14;

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mEtWidth, mEtWidth);
            layoutParams.bottomMargin = childVPadding;
            layoutParams.topMargin = childVPadding;
            layoutParams.leftMargin = childHPadding;
            layoutParams.rightMargin = childHPadding;
            layoutParams.gravity = Gravity.CENTER;

            editText.setLayoutParams(layoutParams);
            editText.setGravity(Gravity.CENTER);
            editText.setId(i);
            editText.setCursorVisible(true);
            editText.setMaxEms(1);
            editText.setTextColor(mEtTextColor);
            editText.setTextSize(mEtTextSize);
            editText.setMaxLines(1);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setPadding(0, 0, 0, 0);
            editText.setOnKeyListener(this);
            editText.setBackgroundResource(mEtTextBg);
            try {//修改光标的颜色（反射）
                Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                f.setAccessible(true);
                f.set(editText, mCursorDrawable);
            } catch (Exception ignored) {
            }
            editText.addTextChangedListener(this);
            editText.setOnFocusChangeListener(this);
            editText.setOnKeyListener(this);
            addView(editText);
            if (i == 0) {
                editText.setFocusable(true);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() != 0) {
            focus();
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL) {
            backFocus();
        }
        return false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.setEnabled(enabled);
        }
    }

    /**
     * 获取焦点
     */
    private void focus() {
        int count = getChildCount();
        EditText editText;
        for (int i = 0; i < count; i++) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() < 1) {
                editText.setCursorVisible(true);
                editText.requestFocus();
                return;
            } else {
                editText.setCursorVisible(false);
            }
        }
        EditText lastEditText = (EditText) getChildAt(mEtNumber - 1);
        if (lastEditText.getText().length() > 0) {
            lastEditText.requestFocus();
            getResult();
        }
    }

    private void backFocus() {
        long startTime = System.currentTimeMillis();
        EditText editText;
        for (int i = mEtNumber - 1; i >= 0; i--) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() >= 1 && startTime - endTime > 100) {
                editText.setText("");
                editText.setCursorVisible(true);
                editText.requestFocus();
                endTime = startTime;
                return;
            }
        }
    }

    private void getResult() {
        StringBuffer stringBuffer = new StringBuffer();
        EditText editText;
        for (int i = 0; i < mEtNumber; i++) {
            editText = (EditText) getChildAt(i);
            stringBuffer.append(editText.getText());
        }
        if (onCodeFinishListener != null) {
            onCodeFinishListener.onComplete(stringBuffer.toString());
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            focus();
        }
    }

    public interface OnCodeFinishListener {
        void onComplete(String content);
    }
}
