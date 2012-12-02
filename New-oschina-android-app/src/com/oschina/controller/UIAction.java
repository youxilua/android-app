package com.oschina.controller;

import com.oschina.view.R;
import com.oschina.view.news.NewsDetailView;

import net.oschina.app.bean.News;
import net.oschina.app.common.StringUtils;
import net.oschina.app.ui.NewsDetail;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public class UIAction {

	/**
	 * 新闻超链接点击跳转
	 * 
	 * @param context
	 * @param newsId
	 * @param newsType
	 * @param objId
	 */
	public static void showNewsRedirect(FragmentManager fm, News news) {
		String url = news.getUrl();
		// url为空-旧方法
		if (StringUtils.isEmpty(url)) {
			int newsId = news.getId();
			int newsType = news.getNewType().type;
			String objId = news.getNewType().attachment;
			switch (newsType) {
			case News.NEWSTYPE_NEWS:
				showNewsDetail(fm, newsId);
				break;
			}
		}
	}

	/**
	 * 显示新闻详情
	 * 
	 * @param context
	 * @param newsId
	 */
	public static void showNewsDetail(FragmentManager fm, int newsId) {
		// Intent intent = new Intent(context, NewsDetail.class);
		// intent.putExtra("news_id", newsId);
		// context.startActivity(intent);
		Bundle b = new Bundle();
		b.putInt("news_id", newsId);
		NewsDetailView n2 = new NewsDetailView();
		n2.setArguments(b);

		fm.beginTransaction().replace(R.id.container, n2).commit();
	
	}
	
	
	/**
	 * 新闻超链接点击跳转
	 * @param context
	 * @param newsId
	 * @param newsType
	 * @param objId
	 */
	public static void showNewsRedirect(Context context, News news)
	{
		String url = news.getUrl();
		//url为空-旧方法
		if(StringUtils.isEmpty(url)) {
			int newsId = news.getId();
			int newsType = news.getNewType().type;
			String objId = news.getNewType().attachment;
			switch (newsType) {
				case News.NEWSTYPE_NEWS:
					showNewsDetail(context, newsId);
					break;
			}
		} else {
			//showUrlRedirect(context, url);
		}
	}
	
	/**
	 * 显示新闻详情
	 * @param context
	 * @param newsId
	 */
	public static void showNewsDetail(Context context, int newsId)
	{
		Intent intent = new Intent(context, NewsDetail.class);
		intent.putExtra("news_id", newsId);
		context.startActivity(intent);
	}
}
