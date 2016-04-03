package com.njupt.sniper.smartparking;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.njupt.sniper.smartparking.utils.ToastUtils;

public class HomeActivity extends AppCompatActivity {
    private FragmentTabHost mTabHost;
    private View tabContainer;
    private LayoutInflater mInflater;

    private static final String TAG_MAIN = "tag_main";
    private static final String TAG_MY = "tag_my";

    private TabInfo[] mTabInfos = new TabInfo[]{
            new TabInfo(MainFragment.class, R.drawable.tab_home, R.string.tab_main, TAG_MAIN),
            new TabInfo(PersonalFragment.class, R.drawable.tab_my, R.string.tab_my, TAG_MY),

    };

    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_home);

        mTabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabContainer=  findViewById(R.id.th_tabs);
        initTabs();
        tabContainer.setVisibility(View.VISIBLE);
    }

    private void initTabs() {
        //实例化TabHost对象，得到TabHost
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);

        mInflater = LayoutInflater.from(this);

        //得到fragment的个数
        for (TabInfo tabInfo : mTabInfos) {
            TabHost.TabSpec tabSpec = mTabHost
                    .newTabSpec(tabInfo.tag)
                    .setIndicator(getTabIcon(tabInfo));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, tabInfo.fClz, null);
        }
    }

    private View getTabIcon(TabInfo tabInfo) {
        View view = mInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(tabInfo.tabIcon);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(tabInfo.tabText);

        return view;
    }

    /**
     * 双击back键，退出应用
     */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        doubleBackToExitPressedOnce = true;
        ToastUtils.show(this, "雙擊退出應用");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    private class TabInfo {
        Class<? extends Fragment> fClz;
        int tabIcon;
        int tabText;
        String tag;

        public TabInfo(Class<? extends Fragment> fClz, int tabIcon, int tabText, String tag) {
            this.fClz = fClz;
            this.tabIcon = tabIcon;
            this.tabText = tabText;
            this.tag = tag;
        }
    }

}
