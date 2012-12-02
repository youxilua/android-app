package com.oschina.controller;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import com.androidquery.AQuery;
import com.youxilua.framework.action.ApiBaseAction;
import com.oschina.view.R;

public class HomeAction extends ApiBaseAction {
	private final static int HOMEINDEX = 0;

	public HomeAction(Context ctx, AQuery aq) {
		super(ctx, aq);
		// TODO Auto-generated constructor stub
	}

	public HomeAction(Context ctx, AQuery aq, int layoutId) {
		super(ctx, aq);
		// 想了一下应该有个重复设置的bug,那就暂时不提供切换功能
		setHomeMenu(layoutId);
	}

	private void setHomeMenu(int layoutId) {
		View b = getActivity().getLayoutInflater().inflate(
				R.layout.item_list_home, null);
		
		getAppParentView().addView(
				b, 0);
		
	}
	// 如果没有action的时候应该会有bug
	private FrameLayout getAppParentView() {
		return  (FrameLayout) getContentParentView().getParent();
	}
	
	public ViewParent getContentParentView(){
		return getActivity().findViewById(android.R.id.content).getParent();
	}

	public int getHomeWidth() {
		Rect frame = new Rect();
		getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		Log.d("test", "-->"+statusBarHeight);
		
		ViewGroup vg = (ViewGroup) getAppParentView().getChildAt(HOMEINDEX);
		vg.setPadding(0, statusBarHeight, 0, 0);
		return vg.getChildAt(HOMEINDEX).getWidth();
	}
	
	
	
	

	public void showLeftToRight() {
		Log.d("test", "width"+getHomeWidth());
		ObjectAnimator
				.ofFloat(getContentParentView(), "translationX", 0, getHomeWidth())
				.setDuration(500).start();
	}

	public void hidRightToLeft() {
		ObjectAnimator.ofFloat(getContentParentView(), "translationX", getHomeWidth(),0)
				.setDuration(500).start();
	}

}
