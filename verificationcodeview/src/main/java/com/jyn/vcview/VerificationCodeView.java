package com.jyn.vcview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
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
 * jiaoyaning
 * VerificationCodeView
 * 2017/10/31
 * update 2019/8/8
 */

public class VerificationCodeView extends LinearLayout implements TextWatcher, View.OnKeyListener, View.OnFocusChangeListener {

    private Context mContext;
    private OnCodeFinishListener onCodeFinishListener;

    /**
     * 输入框数量
     */
    private int mEtNumber;

    /**
     * 输入框类型
     */
    private VCInputType mEtInputType;
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

    /**
     * 输入框间距
     */
    private int mEtSpacing;

    /**
     * 平分后的间距
     */
    private int mEtBisectSpacing;

    /**
     * 判断是否平分
     */
    private boolean isBisect;

    /**
     * 是否显示光标
     */
    private boolean cursorVisible;

    /**
     * 光标样式
     */
    private int mCursorDrawable;

    /**
     * 输入框宽度
     */
    private int mViewWidth;

    /**
     * 输入框间距
     */
    private int mViewMargin;

    public OnCodeFinishListener getOnCodeFinishListener() {
        return onCodeFinishListener;
    }

    public void setOnCodeFinishListener(OnCodeFinishListener onCodeFinishListener) {
        this.onCodeFinishListener = onCodeFinishListener;
    }

    public int getmEtNumber() {
        return mEtNumber;
    }

    public void setmEtNumber(int mEtNumber) {
        this.mEtNumber = mEtNumber;
    }

    public VCInputType getmEtInputType() {
        return mEtInputType;
    }

    public void setmEtInputType(VCInputType mEtInputType) {
        this.mEtInputType = mEtInputType;
    }

    public int getmEtWidth() {
        return mEtWidth;
    }

    public void setmEtWidth(int mEtWidth) {
        this.mEtWidth = mEtWidth;
    }

    public int getmEtTextColor() {
        return mEtTextColor;
    }

    public void setmEtTextColor(int mEtTextColor) {
        this.mEtTextColor = mEtTextColor;
    }

    public float getmEtTextSize() {
        return mEtTextSize;
    }

    public void setmEtTextSize(float mEtTextSize) {
        this.mEtTextSize = mEtTextSize;
    }

    public int getmEtTextBg() {
        return mEtTextBg;
    }

    public void setmEtTextBg(int mEtTextBg) {
        this.mEtTextBg = mEtTextBg;
    }

    public int getmCursorDrawable() {
        return mCursorDrawable;
    }

    public void setmCursorDrawable(int mCursorDrawable) {
        this.mCursorDrawable = mCursorDrawable;
    }

    public enum VCInputType {
        /**
         * 数字类型
         */
        NUMBER,

        /**
         * 数字密码
         */
        NUMBERPASSWORD,
        /**
         * 文字
         */
        TEXT,
        /**
         * 文字密码
         */
        TEXTPASSWORD,
    }

    public VerificationCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        @SuppressLint({"Recycle", "CustomViewStyleable"})
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.vericationCodeView);
        mEtNumber = typedArray.getInteger(R.styleable.vericationCodeView_vcv_et_number, 4);
        int inputType = typedArray.getInt(R.styleable.vericationCodeView_vcv_et_inputType, VCInputType.NUMBER.ordinal());
        mEtInputType = VCInputType.values()[inputType];
        mEtWidth = typedArray.getDimensionPixelSize(R.styleable.vericationCodeView_vcv_et_width, 120);
        mEtTextColor = typedArray.getColor(R.styleable.vericationCodeView_vcv_et_text_color, Color.BLACK);
        mEtTextSize = typedArray.getDimensionPixelSize(R.styleable.vericationCodeView_vcv_et_text_size, 16);
        mEtTextBg = typedArray.getResourceId(R.styleable.vericationCodeView_vcv_et_bg, R.drawable.et_login_code);
        mCursorDrawable = typedArray.getResourceId(R.styleable.vericationCodeView_vcv_et_cursor, R.drawable.et_cursor);
        cursorVisible = typedArray.getBoolean(R.styleable.vericationCodeView_vcv_et_cursor_visible, true);

        isBisect = typedArray.hasValue(R.styleable.vericationCodeView_vcv_et_spacing);
        if (isBisect) {
            mEtSpacing = typedArray.getDimensionPixelSize(R.styleable.vericationCodeView_vcv_et_spacing, 0);
        }
        initView();
        //释放资源
        typedArray.recycle();
    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        for (int i = 0; i < mEtNumber; i++) {
            EditText editText = new EditText(mContext);
            initEditText(editText, i);
            addView(editText);
            //设置第一个editText获取焦点
            if (i == 0) {
                editText.setFocusable(true);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initEditText(EditText editText, int i) {
        editText.setLayoutParams(getETLayoutParams(i));
        editText.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        editText.setGravity(Gravity.CENTER);
        editText.setId(i);
        editText.setCursorVisible(false);
        editText.setMaxEms(1);
        editText.setTextColor(mEtTextColor);
        editText.setTextSize(mEtTextSize);
        editText.setCursorVisible(cursorVisible);
        editText.setMaxLines(1);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
        switch (mEtInputType) {
            case NUMBER:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case NUMBERPASSWORD:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                editText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
                break;
            case TEXT:
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case TEXTPASSWORD:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            default:
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        editText.setPadding(0, 0, 0, 0);
        editText.setOnKeyListener(this);
        editText.setBackgroundResource(mEtTextBg);
        setEditTextCursorDrawable(editText);
        editText.addTextChangedListener(this);
        editText.setOnKeyListener(this);
        editText.setOnFocusChangeListener(this);
    }

    /**
     * 获取EditText 的 LayoutParams
     */
    public LinearLayout.LayoutParams getETLayoutParams(int i) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mEtWidth, mEtWidth);
        if (!isBisect) {
            //平分Margin，把第一个EditText跟最后一个EditText的间距同设为平分
            mEtBisectSpacing = (mViewWidth - mEtNumber * mEtWidth) / (mEtNumber + 1);
            if (i == 0) {
                layoutParams.leftMargin = mEtBisectSpacing;
                layoutParams.rightMargin = mEtBisectSpacing / 2;
            } else if (i == mEtNumber - 1) {
                layoutParams.leftMargin = mEtBisectSpacing / 2;
                layoutParams.rightMargin = mEtBisectSpacing;
            } else {
                layoutParams.leftMargin = mEtBisectSpacing / 2;
                layoutParams.rightMargin = mEtBisectSpacing / 2;
            }
        } else {
            layoutParams.leftMargin = mEtSpacing / 2;
            layoutParams.rightMargin = mEtSpacing / 2;
        }

        layoutParams.gravity = Gravity.CENTER;
        return layoutParams;
    }

    public void setEditTextCursorDrawable(EditText editText) {
        //修改光标的颜色（反射）
        if (cursorVisible) {
            try {
                Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                f.setAccessible(true);
                f.set(editText, mCursorDrawable);
            } catch (Exception ignored) {
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        updateETMargin();
    }

    private void updateETMargin() {
        for (int i = 0; i < mEtNumber; i++) {
            EditText editText = (EditText) getChildAt(i);
            editText.setLayoutParams(getETLayoutParams(i));
        }
    }


    @Override
    public void onFocusChange(View view, boolean b) {
        if (b) {
            focus();
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
        if (onCodeFinishListener != null) {
            onCodeFinishListener.onTextChange(this, getResult());
            //如果最后一个输入框有字符，则返回结果
            EditText lastEditText = (EditText) getChildAt(mEtNumber - 1);
            if (lastEditText.getText().length() > 0) {
                onCodeFinishListener.onComplete(this, getResult());
            }
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
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
        //利用for循环找出还最前面那个还没被输入字符的EditText，并把焦点移交给它。
        for (int i = 0; i < count; i++) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() < 1) {
                if (cursorVisible) {
                    editText.setCursorVisible(true);
                } else {
                    editText.setCursorVisible(false);
                }
                editText.requestFocus();
                return;
            } else {
                editText.setCursorVisible(false);
                if (i == count - 1) {
                    editText.requestFocus();
                }
            }
        }
    }

    private void backFocus() {
        EditText editText;
        //循环检测有字符的`editText`，把其置空，并获取焦点。
        for (int i = mEtNumber - 1; i >= 0; i--) {
            editText = (EditText) getChildAt(i);
            if (editText.getText().length() >= 1) {
                editText.setText("");
                if (cursorVisible) {
                    editText.setCursorVisible(true);
                } else {
                    editText.setCursorVisible(false);
                }
                editText.requestFocus();
                return;
            }
        }
    }

    private String getResult() {
        StringBuilder stringBuffer = new StringBuilder();
        EditText editText;
        for (int i = 0; i < mEtNumber; i++) {
            editText = (EditText) getChildAt(i);
            stringBuffer.append(editText.getText());
        }
        return stringBuffer.toString();
    }

    public interface OnCodeFinishListener {
        /**
         * 文本改变
         */
        void onTextChange(View view, String content);

        /**
         * 输入完成
         */
        void onComplete(View view, String content);
    }

    /**
     * 清空验证码输入框
     */
    public void setEmpty() {
        EditText editText;
        for (int i = mEtNumber - 1; i >= 0; i--) {
            editText = (EditText) getChildAt(i);
            editText.setText("");
            if (i == 0) {
                if (cursorVisible) {
                    editText.setCursorVisible(true);
                } else {
                    editText.setCursorVisible(false);
                }
                editText.requestFocus();
            }
        }
    }
}
