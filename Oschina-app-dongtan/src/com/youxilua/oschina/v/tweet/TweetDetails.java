package com.youxilua.oschina.v.tweet;

import net.oschina.app.bean.Tweet;
import net.oschina.app.common.StringUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SlidingDrawer;

import com.androidquery.AQuery;
import com.youxilua.dongtan.R;

public class TweetDetails extends Fragment {
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fgm_tweetdetails, null, false);
	}
	
	public void initTweetComment(ListAdapter comment){
		AbsListView lv = (AbsListView) getView().findViewById(R.id.tweetDetailComment);
		lv.setAdapter(comment);
	}
	
	public void initTweetDetails(Tweet tweet){
		AQuery tweetQuery = new AQuery(getView());
		SlidingDrawer sildDrawer = (SlidingDrawer) tweetQuery.id(R.id.tweetDetailSlid).getView();
		String imageUrl = tweet.getImgBig();
		String faceURL = tweet.getFace();
		if(imageUrl != null && !StringUtils.isEmpty(imageUrl)){
			tweetQuery.id(R.id.tweetBigImage).image(tweet.getImgBig());
			sildDrawer.close();
		}else{
			tweetQuery.id(R.id.tweetBigImage).gone();
			sildDrawer.open();
		}
//		
		if (faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL)) {
			tweetQuery.id(R.id.tweetPortrait).image(R.drawable.widget_dface);
		} else {
			tweetQuery.id(R.id.tweetPortrait).image(faceURL);
		}
		tweetQuery.id(R.id.tweetDetailCommentCount).text(tweet.getCommentCount() + "条评论");
		tweetQuery.id(R.id.tweetName).text(tweet.getAuthor());
		tweetQuery.id(R.id.tweetBody).text(tweet.getBody());
		tweetQuery.id(R.id.tweetBefore).text(StringUtils.friendly_time(tweet.getPubDate()));
		tweetQuery.id(R.id.tweetCommentButton).gone();
		
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
//		List<View> lvView = new ArrayList<View>();
//
//		View tv1 = getActivity().getLayoutInflater().inflate(
//				R.layout.fgm_tweetdetails, null);
//		TextView tv2 = new TextView(getActivity());
//		tv2.setText("t2");
//		lvView.add(tv1);
//		lvView.add(tv2);
//
//		SimplePageAdapter simplePage = new SimplePageAdapter(getActivity(),
//				lvView);
//		ViewPager vp = (ViewPager) getView().findViewById(R.id.tweetDetail);
//		vp.setAdapter(simplePage);
//		vp.setOnPageChangeListener(new OnPageChangeListener() {
//
//			@Override
//			public void onPageSelected(int arg0) {
//				// TODO Auto-generated method stub
//				if (arg0 == 1) {
//					getFragmentManager().popBackStackImmediate();
//				}
//				// if(arg0 == 0){
//				// getFragmentManager().popBackStackImmediate();
//				// }else if(arg0 == 2){
//				// getFragmentManager().popBackStackImmediate();
//				// }
//			}
//
//			@Override
//			public void onPageScrolled(int arg0, float arg1, int arg2) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int arg0) {
//				// TODO Auto-generated method stub
//
//			}
//		});
	}

}
