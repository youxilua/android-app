package com.youxilua.design.change;

import com.youxilua.oschina.v.tweet.TweetDetails;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AppPageAdapter extends FragmentPagerAdapter {
	
	public static Fragment [] mainFgm = new Fragment[2]; 

	public AppPageAdapter(FragmentManager fm) {
		super(fm);
		mainFgm[0] = new ContentFgm();
		mainFgm[1] = new TweetDetails();
		
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return mainFgm[arg0];
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mainFgm.length;
	}

}
