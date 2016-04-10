package com.njupt.sniper.smartparking.common;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.njupt.sniper.smartparking.utils.TypefaceUtils;


public class IconTextView extends TextView {
    public IconTextView(Context context) {
        super(context);
    }

    public IconTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IconTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        TypefaceUtils.setOcticons(this);

    }
}
