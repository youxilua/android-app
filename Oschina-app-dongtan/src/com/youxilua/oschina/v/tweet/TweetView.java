package com.youxilua.oschina.v.tweet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;

import com.youxilua.dongtan.R;
import com.youxilua.oschina.app.TweetApp;
import com.youxilua.oschina.c.TweetAction;

public class TweetView extends Fragment {
	
	TweetAction tweetAction;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fgm_tweet, null, false);
	}
	public static final int LASETESTTWEET = 0;
	public static final int HOTTWEET = -1;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		tweetAction = new TweetAction(getActivity(),getView());
		TweetApp.actionbar.getSpinner().setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
				
				switch (position) {
				case LASETESTTWEET:
					tweetAction.setTweetList(LASETESTTWEET);
					break;
				case -HOTTWEET:
					tweetAction.setTweetList(HOTTWEET);
					break;
				default:
					Toast.makeText(getActivity(), "暂未开放", Toast.LENGTH_SHORT).show();
					break;
				}
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
}
