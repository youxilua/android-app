package com.oschina.model;

import java.util.ArrayList;
import java.util.List;

import net.oschina.app.bean.Active;
import net.oschina.app.bean.Blog;
import net.oschina.app.bean.Comment;
import net.oschina.app.bean.Messages;
import net.oschina.app.bean.News;
import net.oschina.app.bean.NewsList;
import net.oschina.app.bean.Post;
import net.oschina.app.bean.Tweet;

public class LvData {
	
	//资讯
	public static int lvNewsSumData;
	public static int lvBlogSumData;
	public static List<News> lvNewsData = new ArrayList<News>();
	
	//评论
	public static List<Comment> lvCommentData = new ArrayList<Comment>();
	
	public static List<Blog> lvBlogData = new ArrayList<Blog>();
	public static List<Post> lvQuestionData = new ArrayList<Post>();
	public static List<Tweet> lvTweetData = new ArrayList<Tweet>();
	public static List<Active> lvActiveData = new ArrayList<Active>();
	public static List<Messages> lvMsgData = new ArrayList<Messages>();
}
