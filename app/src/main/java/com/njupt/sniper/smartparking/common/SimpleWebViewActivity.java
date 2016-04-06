package com.njupt.sniper.smartparking.common;

import android.content.Context;
import android.content.Intent;

/**
 * 该类用于展示一个简单的网页界面。
 * <p/>
 * Created by lmw on 2015/9/23.
 */
public class SimpleWebViewActivity extends WebViewActivity {

    public static void startSimpleWebViewActivity(Context context, String url, String title) {
        Intent it = new Intent(context, SimpleWebViewActivity.class);
        it.putExtra("url", url);
        it.putExtra("title", title);
        context.startActivity(it);
    }

    @Override
    protected String getWebUrl() {
        return getIntent().getStringExtra("url");
    }

    @Override
    protected String getActivityTitle() {
        return getIntent().getStringExtra("title");
    }

}
