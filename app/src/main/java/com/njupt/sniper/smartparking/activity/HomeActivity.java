package com.njupt.sniper.smartparking.activity;

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
import com.njupt.sniper.smartparking.R;
import com.njupt.sniper.smartparking.fragment.MainFragment;
import com.njupt.sniper.smartparking.fragment.PersonalFragment;
import com.njupt.sniper.smartparking.utils.ToastUtils;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

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

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions 内存缓存文件的最大长宽
                .threadPriority(Thread.NORM_PRIORITY - 2) // default 设置当前线程的优先级
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //可以通过自己的内存缓存实现
                .memoryCacheSize(2 * 1024 * 1024)  // 内存缓存的最大值
                // default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
                .imageDownloader(new BaseImageDownloader(this)) // default
                .imageDecoder(new BaseImageDecoder()) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .build(); //开始构建
        ImageLoader.getInstance().init(config);
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
        ToastUtils.show(this, "双击退出应用");

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
