package com.njupt.sniper.smartparking.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.njupt.sniper.smartparking.R;


public class LeftIconTextView extends RelativeLayout {

    protected final int SPACING_SMALLER = getResources().getDimensionPixelSize(R.dimen.spacing_4);

    private IconTextView iconText;
    private TextView rightText;


    public LeftIconTextView(Context context) {
        this(context, null);
    }

    public LeftIconTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftIconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LeftIconTextView, defStyleAttr, 0);
        String leftIcon = typedArray.getString(R.styleable.LeftIconTextView_leftIcon);
        String rightStr = typedArray.getString(R.styleable.LeftIconTextView_rightStr);

        typedArray.recycle();

        LayoutParams lp;
        iconText = new IconTextView(context);
        iconText.setId(R.id.tv_icon);
        iconText.setText(leftIcon);
        iconText.setTextColor(getResources().getColor(R.color.dark_blue));

        lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(ALIGN_PARENT_LEFT);
        lp.addRule(CENTER_VERTICAL);
        lp.setMargins(0, 0, SPACING_SMALLER, 0);
        this.addView(iconText, lp);

        rightText = new TextView(context);
        rightText.setId(R.id.tv_edit);
        rightText.setText(rightStr);
        rightText.setTextColor(getResources().getColor(R.color.dark_blue));
        rightText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(RIGHT_OF, iconText.getId());
        lp.addRule(CENTER_VERTICAL);
        lp.setMargins(0, 0, 0, 0);
        this.addView(rightText, lp);
    }

    public void setRightStr(CharSequence str) {
        rightText.setText(str);
    }

    /**
     * @param sp 字体大小，单位sp
     */
    public void setRightTextSize(int sp) {
        rightText.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
    }

    /**
     * @param sp 图标大小，单位sp
     */
    public void setIconSize(int sp) {
        iconText.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
    }

    public void setIconColor(int resId) {
        iconText.setTextColor(getResources().getColor(resId));
    }

    public void setRightColor(int resId) {
        rightText.setTextColor(getResources().getColor(resId));
    }

}
