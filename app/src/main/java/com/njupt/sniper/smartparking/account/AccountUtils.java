package com.njupt.sniper.smartparking.account;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.njupt.sniper.smartparking.utils.PreferenceUtils;

import static android.content.SharedPreferences.Editor;

public class AccountUtils {

	public static void setCurrentAccount(final Activity activity, String name) {
		SharedPreferences accountPreference = PreferenceUtils.getAccountPreference(activity);
		Editor editor = accountPreference.edit().putString(PreferenceUtils.CURRENT_ACCOUNT, name);
		PreferenceUtils.save(editor);
	}

	/**
	 * save current account
	 *
	 * @param context
	 * @param id
	 */
	public static void setCurrentAccountId(Context context, String id) {
		SharedPreferences accountPreference = PreferenceUtils.getAccountPreference(context);
		Editor editor = accountPreference.edit().putString(PreferenceUtils.CURRENT_ACCOUNT_ID, id);
		PreferenceUtils.save(editor);
	}

	/**
	 * get current account
	 *
	 * @param context
	 * @return
	 */
	public static String currentAccountId(Context context) {
		SharedPreferences accountPreference = PreferenceUtils.getAccountPreference(context);
		return accountPreference.getString(PreferenceUtils.CURRENT_ACCOUNT_ID, null);
	}

	/**
	 * get current account
	 *
	 * @param context
	 * @return
	 */
	public static String currentAccount(Context context) {
		SharedPreferences accountPreference = PreferenceUtils.getAccountPreference(context);
		return accountPreference.getString(PreferenceUtils.CURRENT_ACCOUNT, null);
	}


}
