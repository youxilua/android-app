package com.oschina.controller.nav;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.oschina.view.R;
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

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
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

	public boolean hasTwoPanes() {
		return getResources().getBoolean(R.bool.has_two_panes);
	}

	public void setFragment() {
		NewsListView nf = new NewsListView();
		if (hasTwoPanes()) {
			getFragmentActivity().getSupportFragmentManager()
					.beginTransaction().replace(R.id.container_list, nf)
					.commit();
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
