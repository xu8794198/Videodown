package com.example.videoplayer;

import android.app.Application;
import android.content.Context;

public class PhoneApplication extends Application {
	/**
	 * For get the application context
	 */
//	private static final String TAG = "DiaryApplication";
	private static Context instance;
	public static boolean isShow=false;
	public static Context CONTEXT;
//	public static	CPushClient client=new CPushClient(2);
//	public static String token;
	@Override
	public void onCreate() {
		super.onCreate();
		setContext(this);

	}

	private static void setContext(Context mContext) {
		instance = mContext;
		CONTEXT = mContext;
	}

	public static Context getContext() {
		return instance;
	}



}

