package com.oschina.view.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.oschina.controller.news.NewsDetailsAction;
import com.oschina.view.R;

/**
 * @author youxiachai
 * 双击全屏,收藏,分享没处理
 *
 */
public class NewsDetailView extends Fragment{

	private GestureDetector gd;
	private boolean isFullScreen;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.news_detail, null, false);
	}
	public NewsDetailsAction newsDetailsAction;
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
    
		newsDetailsAction = new NewsDetailsAction(getActivity(), new AQuery(getView()));
		newsDetailsAction.initView(getArguments().getInt("news_id", 0));
    	newsDetailsAction.initData();
		
    	//加载评论视图&数据
		
    	newsDetailsAction.initCommentView();
    	newsDetailsAction.initCommentData();
    	
		
		
	}
	
	
	
	
	

     
	

}
