package com.oschina.model;

import java.util.ArrayList;
import java.util.List;

import net.oschina.app.bean.Comment;
import net.oschina.app.bean.News;
import net.oschina.app.bean.NewsList;

public class LvData {
	
	//资讯
	public static int lvNewsSumData;
	public static List<News> lvNewsData = new ArrayList<News>();
	public static int curNewsCatalog = NewsList.CATALOG_ALL;
	
	//评论
	public static List<Comment> lvCommentData = new ArrayList<Comment>();
}
