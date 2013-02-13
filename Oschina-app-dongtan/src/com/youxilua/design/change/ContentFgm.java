package com.youxilua.design.change;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.youxilua.dongtan.R;
import com.youxilua.oschina.v.tweet.TweetView;
import com.youxilua.utils.debug.AppDebug;
import com.youxilua.utils.fragment.FragmentUtils;

public class ContentFgm extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		FrameLayout fl = new FrameLayout(getActivity());
		LayoutParams flParams = new LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		fl.setId(android.R.id.empty);
		fl.setLayoutParams(flParams);
		return fl;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FragmentUtils.renderFgm(android.R.id.empty, getFragmentManager(), new TweetView());
		
	}
	
	public void showBack(){
		getView().findViewById(R.id.fade).setVisibility(View.VISIBLE);
	}
	
	public void hideBack(){
		getView().findViewById(R.id.fade).setVisibility(View.GONE);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AppDebug.MethodLog(getClass(), "content-->onResume" );
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AppDebug.MethodLog(getClass(), "content-->onPause" );
	}

}
