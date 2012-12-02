package com.oschina.controller;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.youxilua.framework.action.ApiBaseAction;
import com.oschina.view.R;
import com.oschina.view.news.NewsDetailView;
import com.oschina.view.news.NewsListView;

public class ActionBarAction extends ApiBaseAction implements
		OnNavigationListener, TabListener {

	public static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	public ActionBarAction(Context ctx, AQuery aq) {
		super(ctx, aq);
		// TODO Auto-generated constructor stub
	}

	public void mainActionBar() {
		ActionBar actionBar = getActivity().getActionBar();
		String[] navList = new String[] { "资讯", "问答", "动弹", "我的空间" };

		String[] na2 = new String[] { "最新资讯", "最新博客", "推荐阅读" };

		actionBar.setDisplayShowCustomEnabled(true);
		if (hasPanes()) {
			actionBar.setDisplayShowTitleEnabled(true);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			// For each of the sections in the app, add a tab to the action bar.
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
		// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		// When the given dropdown item is selected, show its contents in the
		// container view.

		if (itemPosition == 0) {
			setFragment();
		} else {
			Toast.makeText(getActivity(), "正在开发中", Toast.LENGTH_SHORT).show();
		}
		//
		// 没记错的默认是false,返回ture 是消费掉这个事件
		return true;
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		switch (tab.getPosition()) {
		case 0:
			setFragment();
			break;

		default:
			break;
		}

	}

	public boolean hasPanes() {
		return getResources().getBoolean(R.bool.has_two_panes);
	}

	public void setFragment() {
		NewsListView nf = new NewsListView();
		if (hasPanes()) {
			getFragmentActivity().getSupportFragmentManager()
					.beginTransaction().replace(R.id.container_list, nf)
					.commit();
			// NewsDetailView n2 = new NewsDetailView();
			// getFragmentActivity().getSupportFragmentManager()
			// .beginTransaction().replace(R.id.container, n2).commit();
		} else {
			getFragmentActivity().getSupportFragmentManager()
					.beginTransaction().replace(R.id.container_list, nf)
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
