package com.youxilua.page;

import java.util.List;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class SimplePageAdapter extends PagerAdapter {

	protected transient Activity mContext;

	private List<View> mContainerView;

	public SimplePageAdapter(Activity context, List<View> containerView) {
		this.mContext = context;
		this.mContainerView = containerView;
	}

	@Override
	public int getCount() {
		return mContainerView.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// RelativeLayout v = new RelativeLayout(mContext);
		//
		// TextView t = new TextView(mContext);
		// t.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.FILL_PARENT));
		// t.setText(mData[position]);
		// t.setTextSize(120);
		// t.setGravity(Gravity.CENTER);
		//
		// v.addView(t);

		((ViewPager) container).addView(mContainerView.get(position), position);

		return mContainerView.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object view) {
		((ViewPager) container).removeView((View) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((View) object);
	}

}