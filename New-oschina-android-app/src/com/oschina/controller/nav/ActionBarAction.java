package com.oschina.controller.nav;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.oschina.view.R;
import com.oschina.view.blogs.BlogsListView;
import com.oschina.view.news.NewsListView;
import com.youxilua.framework.action.ApiBaseAction;

public class ActionBarAction extends ApiBaseAction implements
		OnNavigationListener, TabListener {

	public static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	public ActionBarAction(Context ctx, AQuery aq) {
		super(ctx, aq);
	}

	public void mainActionBar() {
		ActionBar actionBar = getActivity().getActionBar();

		String[] na2 = new String[] { "最新资讯", "最新博客", "推荐阅读" };

		actionBar.setDisplayShowCustomEnabled(true);
		if (hasTwoPanes()) {
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			actionBar.addTab(actionBar.newTab().setText(na2[0])
					.setTabListener(this));
			actionBar.addTab(actionBar.newTab().setText(na2[1])
					.setTabListener(this));
			actionBar.addTab(actionBar.newTab().setText(na2[2])
					.setTabListener(this));
		} else {
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			actionBar.setListNavigationCallbacks(new ArrayAdapter<String>(
					actionBar.getThemedContext(),
					android.R.layout.simple_list_item_1, android.R.id.text1,
					na2), this);
		}

		actionBar.setTitle("资讯");
		actionBar.setDisplayHomeAsUpEnabled(true);

	}
	private final static int NEWS = 0;
	private final static int BLOGS = 1;
	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
	
			setFragment(itemPosition);

	
		//
		// 没记错的默认是false,返回ture 是消费掉这个事件
		return true;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
	
			setFragment(tab.getPosition());
		

	}

	public boolean hasTwoPanes() {
		return getResources().getBoolean(R.bool.has_two_panes);
	}

	public void setFragment(int pos) {
		Fragment list = null;;
		switch (pos) {
		case NEWS:
			list = new NewsListView();
			break;
		case BLOGS:
			list = new BlogsListView();
			break;
		default:
			break;
		}
		if(list != null){
			getFragmentActivity().getSupportFragmentManager()
			.beginTransaction().replace(R.id.container_list, list)
			.commit();
		}
	
	
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

}
