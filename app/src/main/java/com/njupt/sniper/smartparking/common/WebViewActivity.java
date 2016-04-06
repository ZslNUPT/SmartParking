package com.njupt.sniper.smartparking.common;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.njupt.sniper.smartparking.R;
import com.njupt.sniper.smartparking.utils.ScreenUtils;


/**
 * @author xujiahui
 * @date 2015/8/19.
 */
public abstract class WebViewActivity extends ActionBarActivity {

	protected ProgressBar mProgressBar;
	protected WebView mWebView;

	private int screenHeight;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setTitle(getActivityTitle());

		mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
		mWebView = (WebView) findViewById(R.id.webview);

		mWebView.getTitle();

		screenHeight = ScreenUtils.getScreenHeight(this);
		// 将mWebView移到屏幕下
		mWebView.setTranslationY(screenHeight);

		setWebViewClient();

		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (newProgress >= 100) {
					showWebView();
				}
			}
		});

		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setDownloadListener(new MyDownloadStart());
		mWebView.loadUrl(getWebUrl());
	}

	protected void setWebViewClient() {
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				showProgressBar();
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				showWebView();
			}
		});
	}



	protected abstract String getWebUrl();

	protected abstract String getActivityTitle();

	protected void showProgressBar() {
		// 进度条出来
		mProgressBar.animate()
				.translationY(0)
				.setDuration(400)
				.setInterpolator(new AccelerateInterpolator(2.f))
				.start();

		// mWebView出去
		mWebView.animate()
				.translationY(screenHeight)
				.setDuration(1000)
				.setInterpolator(new OvershootInterpolator(2.f))
				.setStartDelay(200);
	}

	protected void showWebView() {
		mProgressBar.animate()
				.translationY(-screenHeight / 2)
				.setDuration(400)
				.setInterpolator(new AccelerateInterpolator(2.f))
				.start();

		mWebView.animate()
				.translationY(0)
				.setDuration(1000)
				.setInterpolator(new OvershootInterpolator(2.f))
				.setStartDelay(200);
	}

	@Override
	public void onBackPressed() {
		if (mWebView.canGoBack()) {
			mWebView.goBack();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();

		if (itemId == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	class MyDownloadStart implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
									String contentDisposition, String mimetype, long contentLength) {
			//调用自己的下载方式
//          new HttpThread(url).start();
			//调用系统浏览器下载
			Uri uri = Uri.parse(url);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mWebView.loadUrl("about:blank");
		mWebView.stopLoading();
		mWebView.setWebChromeClient(null);
		mWebView.setWebViewClient(null);
		mWebView.destroy();
		mWebView = null;
	}
}
