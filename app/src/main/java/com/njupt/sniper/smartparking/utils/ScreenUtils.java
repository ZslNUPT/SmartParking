package com.njupt.sniper.smartparking.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author xujiahui
 * @date 2015/8/13.
 */
public class ScreenUtils {
	private static int screenWidth = 0;
	private static int screenHeight = 0;

	public static int getScreenHeight(Context c) {
		if (screenHeight == 0) {
			WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			screenHeight = size.y;
		}

		return screenHeight;
	}

	public static int getScreenWidth(Context c) {
		if (screenWidth == 0) {
			WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			Point size = new Point();
			display.getSize(size);
			screenWidth = size.x;
		}

		return screenWidth;
	}
}
