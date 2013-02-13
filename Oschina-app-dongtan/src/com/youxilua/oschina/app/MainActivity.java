package com.youxilua.oschina.app;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;

import com.youxilua.design.change.AppPageAdapter;
import com.youxilua.design.change.ContentFgm;
import com.youxilua.design.change.PageContainer;
import com.youxilua.dongtan.R;
import com.youxilua.page.EmbedViewPage;
import com.youxilua.utils.debug.AppDebug;
import com.youxilua.utils.fragment.FragmentUtils;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		FragmentUtils.renderFgm(android.R.id.custom,
				getSupportFragmentManager(), TweetApp.actionbar);
		// FragmentUtils.replaceDefault(android.R.id.empty,
		// getSupportFragmentManager(), new TweetView());
		PageContainer.embedPage = (EmbedViewPage) findViewById(android.R.id.background);
		AppPageAdapter appAdapter = new AppPageAdapter(
				getSupportFragmentManager());
		PageContainer.embedPage.setAdapter(appAdapter);
		PageContainer.embedPage.setChildId(R.id.tweetDetail);
		PageContainer.embedPage
				.setOnPageChangeListener(new OnPageChangeListener() {

					@Override
					public void onPageSelected(int arg0) {
						AppDebug.MethodLog(getClass(), "current-->" + arg0);
						switch (arg0) {
						case 0:
							TweetApp.actionbar.showSpinner();
							break;
						case 1:
							TweetApp.actionbar.hideSpinner();
							break;
						default:
							break;
						}
					}

					@Override
					public void onPageScrolled(int position,
							float positionOffset, int positionOffsetPixels) {
						// TODO Auto-generated method stub
						if (position == 0) {

							AppDebug.MethodLog(getClass(), "off"
									+ positionOffsetPixels);

						} else {
							AppDebug.MethodLog(getClass(), "offset"
									+ positionOffsetPixels);
						}
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {

					}
				});

		// Field mScroller;
		// try {
		// mScroller = ViewPager.class.getDeclaredField("mScroller");
		// mScroller.setAccessible(true);
		// Interpolator sInterpolator = new AccelerateInterpolator();
		// FixedSpeedScroller scroller = new FixedSpeedScroller(
		// PageContainer.contentPager.getContext(), sInterpolator);
		// // scroller.setFixedDuration(5000);
		// mScroller.set(PageContainer.contentPager, scroller);
		// } catch (NoSuchFieldException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IllegalArgumentException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

	public void getX() {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
