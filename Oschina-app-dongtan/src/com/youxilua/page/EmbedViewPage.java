package com.youxilua.page;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class EmbedViewPage extends ViewPager {
	private int childId;

	public EmbedViewPage(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (childId > 0) {
			ViewPager scroll = (ViewPager) findViewById(childId);
			if (scroll != null) {
				return false;
			}

		}
		return super.onInterceptTouchEvent(event);
	}
	
	
	public void setChildId(int id) {
		this.childId = id;
	}
}