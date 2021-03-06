package com.njupt.sniper.smartparking.activity;

import android.content.Context;
import android.content.Intent;

import com.njupt.sniper.smartparking.common.WebViewActivity;

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
