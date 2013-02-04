package com.youxilua.oschina.v.tweet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.youxilua.dongtan.R;
import com.youxilua.oschina.app.TweetApp;
import com.youxilua.oschina.c.TweetAction;

public class TweetView extends Fragment {
	
	TweetAction tweetAction;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frg_tweet, null, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tweetAction = new TweetAction(getActivity(),getView());
		TweetApp.actionbar.getView().findViewById(R.id.actRefresh).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				tweetAction.setTweetList();
			}
		});
		
	}
}
