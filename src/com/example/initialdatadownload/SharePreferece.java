package com.example.initialdatadownload;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferece {

	public static final String isDownLoad= "isDownLoad";

	static Context mContext;

	public SharePreferece (Context c)
	{
		mContext = c;
	}
	public void put(String key, boolean value) {

		SharedPreferences pref = mContext.getSharedPreferences(isDownLoad,
				Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public boolean getValue(String key, boolean dftValue) {

		SharedPreferences pref = mContext.getSharedPreferences(isDownLoad,
				Activity.MODE_PRIVATE);
		try {
			return pref.getBoolean(key, dftValue);
		} catch (Exception e) {
			return dftValue;
		}
	}




}
