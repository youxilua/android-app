package com.oschina.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.androidquery.AQuery;
import com.oschina.controller.main.ActionBarAction;
import com.oschina.controller.main.HomeAction;
import com.oschina.controller.main.MainAction;
import com.oschina.view.R;

public class MainActivity extends FragmentActivity{

	
	
	//控制主面板
	public MainAction mainAction;
	
	//actionbar 控制器
	public ActionBarAction barAction;
	public HomeAction homeAction;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.route_main_layout);
		barAction = new ActionBarAction(this, new AQuery(this));
		barAction.mainActionBar();
		homeAction = new HomeAction(this, new AQuery(this), R.layout.item_list_home);
		
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
	}
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// 恢复数据
		if (savedInstanceState.containsKey(ActionBarAction.STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(ActionBarAction.STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// 针对异常退出的保存
		outState.putInt(ActionBarAction.STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	boolean toggle = false;
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            if(!toggle){
	            	homeAction.showLeftToRight();
	            	toggle =true;
	            }else{
	            	homeAction.hidRightToLeft();
	            	toggle =false;
	            
	            }
	            
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}



}
