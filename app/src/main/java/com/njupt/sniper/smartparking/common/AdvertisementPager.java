package com.njupt.sniper.smartparking.common;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.njupt.sniper.smartparking.R;
import com.njupt.sniper.smartparking.api.model.Banner;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class AdvertisementPager extends RelativeLayout {
    public int MAX_SIZE = (Integer.MAX_VALUE / 2) * 2;

    private int ZOOM_SIZE = 4;
    /**
     * item data list
     */
    private List<Object> pagerData;
    /**
     * Use the list to record the dirs of pictures.
     */
    private List<View> mViews;
    /**
     * The mex size of list.
     */
    private static int pagerSize;
    /**
     * The showed pager's item.
     */
    private int currentItem;
    /**
     * Use the CURSOR_BAR_OPEN and CURSOR_BAR_CLOSE to show or hide
     * the cursor bar.
     */
    private static int CURSOR_BAR_OPEN = 1, CURSOR_BAR_CLOSE = 0;
    /**
     * The delayed time to refresh the viewpager
     */
    private long delayedTime;
    /**
     * The state of scrollable
     */
    private boolean scrollAble;

    /**
     * This view contains a viewpager and a cursor bar.
     */
    private ViewPager viewPager = new ViewPager(getContext());
    private LinearLayout cursorBar = new LinearLayout(getContext());
    private List<ImageView> cursor;

    /**
     * User the handler to control the scroll of pictures
     */
    private MyHandler mHandler;
    private static int HANDLER_MESSAGE;

    public interface OnPagerItemClickListener {
        void onPagerItemClick(Object itemData);
    }

    private OnPagerItemClickListener onPagerItemClickListener;

    public void setOnPagerItemClickListener(OnPagerItemClickListener onPagerItemClickListener) {
        this.onPagerItemClickListener = onPagerItemClickListener;
    }

    public AdvertisementPager(Context context) {
        super(context, null);
        initData();
        initView();
    }

    public AdvertisementPager(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        initData();
        initView();
    }

    /**
     * Init the data of control.
     */
    private void initData() {
        //if update the view, remove the message and reBuild the handler.
        if (mHandler != null)
            mHandler.removeMessages(HANDLER_MESSAGE);
        mHandler = new MyHandler();
        currentItem = 0;
        delayedTime = -1;
        HANDLER_MESSAGE = 0;
        scrollAble = false;
        mViews = new ArrayList<>();
        cursor = new ArrayList<>();
    }

    /**
     * Init the view of control.
     */
    private void initView() {
        removeAllViews();
        viewPager.removeAllViews();
        cursorBar.removeAllViews();

//        this.setClickable(true);
        initViewPager();
        initCursorBar();
    }

    private void initViewPager() {
        viewPager.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //rewrite the pageChangeListener to listen the user's scrolling action.
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
                setCursorItem(currentItem % mViews.size());
                mHandler.removeMessages(HANDLER_MESSAGE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        addView(viewPager);
    }

    private void initCursorBar() {
        cursorBar.setOrientation(LinearLayout.HORIZONTAL);
        cursorBar.setGravity(Gravity.RIGHT);
        for (int i = 0; i < mViews.size() / ZOOM_SIZE; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.navigation_cursor_image, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.navigation_cursor_item_image);
            imageView.setBackgroundResource(R.drawable.shape_navigation_cursor);
            cursorBar.addView(view);
            cursor.add(imageView);
        }
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.addRule(ALIGN_PARENT_BOTTOM);
        lp.setMargins(0, 0, 30, 30);
        if (mViews.size() / ZOOM_SIZE != 1)
            addView(cursorBar, lp);
    }

    private void setCursorItem(int position) {
        position %= mViews.size() / ZOOM_SIZE;
        for (int i = 0; i < cursor.size(); i++) {
            cursor.get(i).setBackgroundResource(R.drawable.shape_navigation_cursor);
        }
        if (cursor != null && !cursor.isEmpty()) {
            cursor.get(position).setBackgroundResource(R.drawable.shape_navigation_cursor_select);
        }
    }

    /**
     * set the current view that viewpager will show
     */
    private void setCurrentItem() {
        viewPager.setCurrentItem(currentItem);
        setCursorItem(currentItem % mViews.size());
        if (scrollAble && delayedTime != -1)
            HANDLER_MESSAGE = 1;
        else
            HANDLER_MESSAGE = 0;
        mHandler.sendEmptyMessageDelayed(HANDLER_MESSAGE, delayedTime);
    }

    /**
     * add image view into viewpager.
     *
     * @param dirs
     */

    public void setImages(Banner[] dirs) {
        if(dirs.length==0)
            return;
        initData();
        pagerData = new ArrayList<>();
        mViews = new ArrayList<>();
        for (int j = 0; j < ZOOM_SIZE; j++)
            for (int i = 0; i < dirs.length; i++) {
                pagerData.add(dirs[i]);
                ImageView imageView = new ImageView(getContext());
                imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                ImageLoader.getInstance().displayImage(dirs[i].getImage(), imageView);
                mViews.add(imageView);
            }
        initView();
        currentItem = MAX_SIZE / 2 - (MAX_SIZE / 2) % dirs.length;
        viewPager.setAdapter(new BaseViewPagerAdapter(mViews));
        setCurrentItem();
    }

    /**
     * set the scrolling state of viewpager.
     *
     * @param scrollAble
     */
    public void setScrollAble(boolean scrollAble) {
        this.scrollAble = scrollAble;
    }

    /**
     * Use teh self adapter to change the base adapter
     *
     * @param pagerAdapter
     */
    public void setAdapter(PagerAdapter pagerAdapter) {
        viewPager.setAdapter(pagerAdapter);
    }

    /**
     * set the delayed time of viewpager
     *
     * @param delayedTime
     */
    public void setDelayedTime(long delayedTime) {
        this.delayedTime = delayedTime;
    }

    /**
     * get the viewpager.
     *
     * @return viewpager.
     */
    public ViewPager getViewPager() {
        return viewPager;
    }

    /**
     * get the current item of the viewpager.
     *
     * @return currentItem.
     */
    public int getCurrentItem() {
        return currentItem;
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                BaseViewPagerAdapter adapter = (BaseViewPagerAdapter) viewPager.getAdapter();
                currentItem++;
                if (mViews == null)
                    return;
            }
            setCurrentItem();
        }
    }

    /**
     * This is a base adapter for the viewpager.
     */
    private class BaseViewPagerAdapter extends PagerAdapter {

        private List<View> itemViews;

        public BaseViewPagerAdapter(List<View> views) {
            itemViews = views;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            if (itemViews.size() / ZOOM_SIZE == 1)
                return 1;
            return MAX_SIZE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mViews.get(position % mViews.size());
            ViewParent parent = itemView.getParent();
            if (parent != null) {
                container.removeView(itemView);
            }
            //add the item image click listener.
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onPagerItemClickListener != null)
                        onPagerItemClickListener.onPagerItemClick(pagerData.get(position % mViews.size()));
                }
            });
            container.addView(itemView);
            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = itemViews.get(position % itemViews.size());
            container.removeView(view);
        }
    }
}
