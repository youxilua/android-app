package com.oschina.controller.main;

import net.oschina.app.bean.News;
import net.oschina.app.common.StringUtils;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.oschina.view.R;
import com.oschina.view.news.NewsDetailView;

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
			case News.NEWSTYPE_BLOG:
				showBlogDetail(fm, StringUtils.toInt(objId));
				break;	
			}
		}
	}
	
	/**
	 * 显示博客详情
	 * @param context
	 * @param blogId
	 */
	public static void showBlogDetail(FragmentManager fm, int blogId)
	{
//		Intent intent = new Intent(context, BlogDetail.class);
//		intent.putExtra("blog_id", blogId);
//		context.startActivity(intent);
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
	
	

}
