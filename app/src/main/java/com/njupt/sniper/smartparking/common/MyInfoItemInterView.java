package com.njupt.sniper.smartparking.common;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.njupt.sniper.smartparking.R;
import com.njupt.sniper.smartparking.utils.DensityUtil;

public class MyInfoItemInterView extends LinearLayout{
    /**
     * the resource id of icon.
     */
    private int iconResId;
    /**
     * the size of icon
     */
    private int iconSize;
    /**
     * the RGB of background color.
     */
    private int iconBackgroundColor;
    /**
     * the text of item.
     */
    private String functionText;
    /**
     * the size of function text.
     */
    private int functionTextSize;
    /**
     * The color of functin text
     */
    private int functionTextColor;
    /**
     * the state of update image.'0' is dismiss, and '1' is show.
     */
    private boolean updateState;
    /**
     * the ImageView of icon.
     */
    private ImageView icon;
    /**
     * the TextView of function
     */
    private TextView function;
    /**
     * the TextView of update.
     */
    private TextView update;
    /**
     * the string of update.
     */
    private String updateText="new";

    private int width;
    private int height;

    public MyInfoItemInterView(Context context) {
        super(context);
        this.iconResId=0;
        this.iconBackgroundColor=0;
        this.functionText=null;
        this.updateState=false;
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        setBackgroundResource(R.drawable.myinfo_item_layout_selector);
        initView();
        refresh();
    }

    public MyInfoItemInterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        String nameSpace = "http://schemas.android.com/apk/res-auto";
        TypedArray a = getResources().obtainAttributes(attrs, R.styleable.MyInfoItemInterView);
        this.iconResId = attrs.getAttributeResourceValue(nameSpace, "iconSrc", 0);
        this.iconSize = (int) a.getDimension(R.styleable.MyInfoItemInterView_iconSize, 36);
        this.iconBackgroundColor = getResources().getColor(attrs.getAttributeResourceValue(nameSpace, "iconBackground", 0));
        this.functionText = getResources().getString(attrs.getAttributeResourceValue(nameSpace, "functionText", 0));
        this.functionTextSize = a.getInt(R.styleable.MyInfoItemInterView_functionTextSize, 14);
        this.functionTextColor = getResources().getColor(attrs.getAttributeResourceValue(nameSpace,
                "functionTextColor", 0));
        this.updateState = attrs.getAttributeBooleanValue(nameSpace, "updateFlag", false);
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);
        setBackgroundResource(R.drawable.myinfo_item_layout_selector);
        initView();
        refresh();
    }

    private void initView() {
        initIcon();
        initFunctionText();
        initUpdateIcon();
        initRightIcon();
    }

    /**
     * init the icon of this item.
     */
    private void initIcon(){
        //set the icon image.
        icon = new ImageView(getContext());
        LayoutParams iconLp = new LayoutParams(iconSize, iconSize);
        iconLp.setMargins(getDp(8), getDp(8), getDp(8), getDp(8));
        addView(icon, iconLp);
    }

    /**
     * init the function text of this item.
     */
    private void initFunctionText(){
        //set the function text.
        function = new TextView(getContext());
        LayoutParams functionLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        function.setTextSize(functionTextSize);
        if (functionTextColor != 0)
            function.setTextColor(functionTextColor);
        function.setGravity(Gravity.CENTER_VERTICAL);
        addView(function, functionLp);
    }

    /**
     * init the icon of updating.
     */
    private void initUpdateIcon(){
        //set the update image
        update = new TextView(getContext());
        LayoutParams updateLp = new LayoutParams(getDp(32), getDp(16));
        updateLp.setMargins(getDp(8), getDp(8), getDp(16), getDp(8));
        updateLp.gravity = Gravity.CENTER;
        update.setBackgroundResource(R.drawable.myinfo_update_shape);
        update.setText(updateText);
        update.setTextSize(10);
        update.setTextColor(Color.WHITE);
        update.setGravity(Gravity.CENTER);
        addView(update, updateLp);
    }

    private void initRightIcon() {
        IconTextView rightIcon=new IconTextView(getContext());
        LayoutParams iconLp = new LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        iconLp.setMargins(0, 0, DensityUtil.dip2px(getContext(), 16), 0);
        rightIcon.setText(getResources().getString(R.string.text_icon_arrow_right));
        rightIcon.setTextColor(getResources().getColor(R.color.dark_grey));
        rightIcon.setTextSize(16f);
        addView(rightIcon, iconLp);
    }

    /**
     * this method is used to draw the icon to the corner backgroud.
     * @param bitmap the source bitmap.
     * @param backgroundColor the background color
     * @return the final bitmap.
     */
    private Bitmap paintIcon(Bitmap bitmap,int backgroundColor) {
        //create a drawable to certain the canvas.
        Drawable imageDrawable = new BitmapDrawable(null, bitmap);
        // create the output bitmap and the canvas.
        Bitmap output = Bitmap.createBitmap(getDp(32), getDp(32), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // draw a rectangle .
        RectF outerRect = new RectF(0, 0, getDp(32), getDp(32));

        // draw the background to the canvas.
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(backgroundColor);
        canvas.drawRoundRect(outerRect, 10, getDp(5), paint);


        // draw the icon to the canvas.
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        imageDrawable.setBounds(getDp(4), getDp(4), getDp(28), getDp(28));
        canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
        imageDrawable.draw(canvas);
        canvas.restore();

        return output;
    }

    /**
     * change the px to the dp.
     * @param px px
     * @return dp
     */
    private int getDp(int px){
        return DensityUtil.dip2px(getContext(), px);
    }

    /**
     * rely on the value of attribute and refresh the layout.
     */
    private void refresh(){
        if(iconBackgroundColor!=0)
            icon.setImageBitmap(paintIcon(BitmapFactory.decodeResource(getResources(), iconResId),iconBackgroundColor));
        else
            icon.setImageResource(iconResId);
        function.setText(functionText);
        update.setVisibility(updateState?VISIBLE:GONE);
        invalidate();
    }

    /**
     * the interface to refresh this view.
     * @param iconResId the resource id of icon.
     * @param iconBackgroundColor the background color
     * @param functionText the function description.
     * @param updateState the state of update.
     */
    public void setFunctionItem(int iconResId, int iconBackgroundColor, String functionText, boolean updateState) {
        this.iconResId=iconResId;
        this.iconBackgroundColor=iconBackgroundColor;
        this.functionText=functionText;
        this.updateState=updateState;
        refresh();
    }

    /**
     * set the resource id of the icon.
     * @param iconResId the resource id of the icon.
     */
    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
        refresh();
    }

    /**
     * set the background color of the icon
     * @param iconBackgroundColor the background color of the icon.
     */
    public void setIconBackgroundColor(int iconBackgroundColor) {
        this.iconBackgroundColor = iconBackgroundColor;
        refresh();
    }

    /**
     * set the function description.
     * @param functionText the description of function.
     */
    public void setFunctionText(String functionText) {
        this.functionText = functionText;
        refresh();
    }

    /**
     * set the state of update icon.
     *
     * @param updateState the state of the icon of update.
     */
    public void setUpdateState(boolean updateState) {
        this.updateState = updateState;
        refresh();
    }

    public void setIconSize(int iconSize) {
        this.iconSize = iconSize;
        refresh();
    }

    public void setFunctionTextSize(int functionTextSize) {
        this.functionTextSize = functionTextSize;
        refresh();
    }

    public void setFunctionTextColor(int functionTextColor) {
        this.functionTextColor = functionTextColor;
        refresh();
    }

    public void setUpdateText(String updateText){
        this.update.setText(updateText);
        refresh();
    }
}
