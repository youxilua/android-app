package com.youxilua.oschina.controll;

import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.youxilua.framework.action.ApiBaseAction;
import com.youxilua.oschina.view.R;

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
		
		String [] na2 = new String [] {"最新资讯","最新博客","推荐阅读"};
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);
		
		
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// For each of the sections in the app, add a tab to the action bar.
				actionBar.addTab(actionBar.newTab().setText(na2[0])
						.setTabListener(this));
				actionBar.addTab(actionBar.newTab().setText(na2[1])
						.setTabListener(this));
				actionBar.addTab(actionBar.newTab().setText(na2[2])
						.setTabListener(this));
		actionBar.setTitle("资讯");
		actionBar.setDisplayHomeAsUpEnabled(true);
		//actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setListNavigationCallbacks(
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, navList), this);
		
		
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub
		// When the given dropdown item is selected, show its contents in the
		// container view.
		Fragment fragment = new DummySectionFragment();
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, itemPosition + 1);
		fragment.setArguments(args);
		getFragmentActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.container, fragment).commit();
		
		//没记错的默认是false,返回ture 是消费掉这个事件
		return true;
	}
	
	
	public static class DummySectionFragment extends Fragment {
	
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Create a new TextView and set its text to the fragment's section
			// number argument value.
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));
			return textView;
		}
	}


	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
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
