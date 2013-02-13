package com.youxilua.oschina.c;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.oschina.app.AppException;
import net.oschina.app.bean.Comment;
import net.oschina.app.bean.CommentList;
import net.oschina.app.bean.Tweet;
import net.oschina.app.bean.TweetList;
import net.oschina.app.bean.URLs;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.youxilua.design.change.AppPageAdapter;
import com.youxilua.design.change.PageContainer;
import com.youxilua.framework.action.ApiBaseAction;
import com.youxilua.oschina.api.ApiInterface;
import com.youxilua.oschina.app.TweetApp;
import com.youxilua.oschina.v.tweet.LvCommentAdpater;
import com.youxilua.oschina.v.tweet.LvTweetAdpater;
import com.youxilua.oschina.v.tweet.TweetDetails;
import com.youxilua.utils.net.INetCallback;
import com.youxilua.utils.net.NetClient;

public class TweetAction extends ApiBaseAction {

	public static final int TWEETDETILAS = 1;
	private NetClient<InputStream> apiQuery = null;
	private AQuery tweetQuery;
	private AbsListView lvTweetList;
	private TweetDetails tweetDetails;

	private LvTweetAdpater lvTweetAdapter;

	private OnItemClickListener tweetList = new OnItemClickListener() {
	
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// AppPageAdapter app = (AppPageAdapter)
			// TweetApp.mainPager.getAdapter();
	
		}
	};

	private OnClickListener tweetItemButton = new OnClickListener() {
	
		@Override
		public void onClick(View v) {
			AppPageAdapter appAdpater = (AppPageAdapter) PageContainer.embedPage.getAdapter();
			tweetDetails = (TweetDetails) appAdpater.getItem(1);
			Tweet tweet = (Tweet) lvTweetAdapter.getItem((Integer) v.getTag());
			tweetDetails.initTweetDetails(tweet);
			getTweetDetails(String.valueOf(tweet.getId()));
			PageContainer.embedPage.setCurrentItem(1);
		}
	};

	private INetCallback<InputStream> tweetCallback = new INetCallback<InputStream>() {
	
		@Override
		public void onSuccess(InputStream result, int id, AjaxStatus status) {
			TweetApp.actionbar.hideProgress();
			switch (id) {
			case TWEETDETILAS:
				hanldeTweetComment(result);
				break;
			default:
				hanldeTweetList(result);
				break;
			}
	
		}
	
		@Override
		public void onError(int code, String message) {
			// TODO Auto-generated method stub
	
		}
	};

	private void getTweetList(int uid) {
		getTweetList(uid, "0", "20");
	}

	private void getTweetList(int uid, String pageIndex, String pageSize) {
		ApiInterface.getTweetList(apiQuery, uid, pageIndex, pageSize);
	}
	private void hanldeTweetList(InputStream result) {
		try {
			TweetList tw = TweetList.parse(result);
			lvTweetAdapter = new LvTweetAdpater(getActivity(), tw.getTweetlist());
			lvTweetAdapter.setClickLisetener(tweetItemButton);
			lvTweetList.setAdapter(lvTweetAdapter);
			lvTweetAdapter.itemQy.id(lvTweetList).scrolled(new OnScrollListener() {
	
				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
					// TODO Auto-generated method stub
					lvTweetAdapter.isInit(false);
				}
	
				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub
					// lta.isInit(false);
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

	/**动弹评论列表
	 * @param result
	 */
	private void hanldeTweetComment(InputStream result) {
		try {
			CommentList commentList = CommentList.parse(result);
			List<Comment> comment = commentList.getCommentlist();
			if(comment.size() > 0){
				LvCommentAdpater lvCommentAdapter = new LvCommentAdpater(getActivity(), comment);
				tweetDetails.initTweetComment(lvCommentAdapter);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AppException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public TweetAction(Context ctx) {
		super(ctx);
	}

	public TweetAction(Context ctx, View view) {
		super(ctx);
		tweetQuery = new AQuery(view);
		lvTweetList = (AbsListView) tweetQuery.id(android.R.id.list).getView();
		lvTweetList.setOnItemClickListener(tweetList);
		//初始化api接口
		apiQuery = new NetClient<InputStream>(InputStream.class, getActivity());
		//初始化方法监听
		apiQuery.handleCallback = tweetCallback;
	}

	public void setTweetList(int uid) {
		TweetApp.actionbar.showProgress();
		getTweetList(uid);
	}

	public void getTweetDetails(String uid) {
		Bundle reqeust = new Bundle();
		reqeust.putString(NetClient.NETQUERYURL, URLs.COMMENT_LIST);
		reqeust.putInt(NetClient.NETQUEYRID, TWEETDETILAS);
		reqeust.putString("id", uid);
		reqeust.putString("catalog", "3");
		reqeust.putString("pageIndex", "0");
		apiQuery.get(reqeust, tweetCallback);
	}

}
