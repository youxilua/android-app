package com.youxilua.oschina.app;

import android.app.Application;

import com.youxilua.oschina.actionbar.ActionBarCompat;

public class TweetApp extends Application {
	
	public static ActionBarCompat actionbar;
	@Override
	public void onCreate() {
		super.onCreate();
		actionbar = new ActionBarCompat();
	}
}
