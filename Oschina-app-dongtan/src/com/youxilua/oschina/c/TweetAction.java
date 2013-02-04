package com.youxilua.oschina.c;

import java.io.IOException;
import java.io.InputStream;

import net.oschina.app.AppException;
import net.oschina.app.bean.TweetList;
import net.oschina.app.bean.URLs;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.youxilua.framework.action.ApiBaseAction;
import com.youxilua.oschina.app.TweetApp;
import com.youxilua.oschina.v.tweet.LvTweetAdpater;
import com.youxilua.utils.net.INetCallback;
import com.youxilua.utils.net.NetClient;

public class TweetAction extends ApiBaseAction {

	private NetClient<InputStream> ajaQuery = null;
	private AQuery tweetQuery;
	private ListView lvTweetList;

	public TweetAction(Context ctx) {
		super(ctx);
	}
	
	private OnItemClickListener tweetList = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
		
		}
	};

	public TweetAction(Context ctx, View view) {
		super(ctx);
		tweetQuery = new AQuery(view);
		lvTweetList = tweetQuery.id(android.R.id.list).getListView();
		lvTweetList.setOnItemClickListener(tweetList);
//		lta =new LvTweetAdpater(getActivity(), R.layout.item_lv_tweet);
//		lta.setClickLisetener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//				Toast.makeText(getActivity(), "pos" + v.getTag(), Toast.LENGTH_SHORT).show();
//			}
//		});
		
	}

	public void setTweetList() {
		TweetApp.actionbar.showProgress();
		getTweetList();
	}

	private void getTweetList() {
		getTweetList("0", "0", "20");
	}

	private void getTweetList(String uid, String pageIndex, String pageSize) {
		Bundle reqeust = new Bundle();
		reqeust.putString(NetClient.NETQUERYURL, URLs.TWEET_LIST);
		reqeust.putString("uid", uid);
		reqeust.putString("pageIndex", pageIndex);
		reqeust.putString("pageSize", pageSize);
		if (ajaQuery == null) {
			ajaQuery = new NetClient<InputStream>(InputStream.class,
					getActivity());
		}
		ajaQuery.get(reqeust, tweetCallback);
	}
	LvTweetAdpater lta;
	private INetCallback<InputStream> tweetCallback = new INetCallback<InputStream>() {

		@Override
		public void onSuccess(InputStream result, int id, AjaxStatus status) {
			try {
				TweetApp.actionbar.hideProgress();
				TweetList tw = TweetList.parse(result);
				lta = new LvTweetAdpater(getActivity(), tw.getTweetlist());
				lvTweetList.setAdapter(lta);
				lta.imageQuery.id(lvTweetList).scrolled(new OnScrollListener() {
					
					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						// TODO Auto-generated method stub
						lta.isInit(false);
					}
					
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
						// TODO Auto-generated method stub
						//lta.isInit(false);
					}
				});
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AppException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onError(int code, String message) {
			// TODO Auto-generated method stub

		}
	};

}
