package com.youxilua.framework;

import com.androidquery.AQuery;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * MVC框架的抽象类
 * @author youxiachai
 *
 */
public abstract class ApiBase {
	protected Context mContext;
	
	public ApiBase(Context ctx){
		this.mContext = ctx;
	}
	
	public Activity getActivity() {
		if (mContext instanceof Activity) {
			return (Activity) mContext;
		} else {
			throw new IllegalArgumentException("不是activity");
		}
	}
	
	public AQuery getActivityAuery(){
		return new AQuery(getActivity());
	}
	
	public FragmentActivity getFragmentActivity() {
		if (mContext instanceof FragmentActivity) {
			return (FragmentActivity) mContext;
		} else {
			new IllegalArgumentException("不是FragmentActivity");
			return null;
		}
	}
	
	public FragmentManager getFragmentManager(){
		return getFragmentActivity().getSupportFragmentManager();
	}
}
