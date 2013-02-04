package com.youxilua.oschina.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.FrameLayout;

import com.youxilua.dongtan.R;
import com.youxilua.oschina.v.tweet.TweetView;
import com.youxilua.utils.frgment.FragmentUtils;

public class MainActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		FragmentUtils.replaceDefault(android.R.id.custom, getSupportFragmentManager(), TweetApp.actionbar);
		FragmentUtils.replaceDefault(android.R.id.empty, getSupportFragmentManager(), new TweetView());
	
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
