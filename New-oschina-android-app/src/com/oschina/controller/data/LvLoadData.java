package com.oschina.controller.data;

import net.oschina.app.AppContext;
import net.oschina.app.AppException;
import net.oschina.app.bean.CommentList;
import net.oschina.app.bean.NewsList;
import net.oschina.app.common.UIHelper;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.oschina.model.LvData;

public class LvLoadData {
	
	private AppContext appContext;
	
	public LvLoadData(Context app){
		this.appContext = (AppContext) app.getApplicationContext();
	}
	
	/**
	 * 线程加载新闻数据
	 * 
	 * @param catalog
	 *            分类
	 * @param pageIndex
	 *            当前页数
	 * @param handler
	 *            处理器
	 * @param action
	 *            动作标识
	 */
	public void loadLvNewsData(final int catalog, final int pageIndex,
			final Handler handler, final int action) {
		// 加载提示,到时候实现个更好的
		// mHeadProgress.setVisibility(ProgressBar.VISIBLE);
		new Thread() {
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				if (action == UIHelper.LISTVIEW_ACTION_REFRESH
						|| action == UIHelper.LISTVIEW_ACTION_SCROLL)
					isRefresh = true;
				try {
					NewsList list = appContext.getNewsList(catalog, pageIndex,
							isRefresh);
					msg.what = list.getPageSize();
					msg.obj = list;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				msg.arg1 = action;
				msg.arg2 = UIHelper.LISTVIEW_DATATYPE_NEWS;
				if (LvData.curNewsCatalog == catalog)
					handler.sendMessage(msg);
			}
		}.start();
	}
	
	
    /**
     * 线程加载评论数据
     * @param id 当前文章id
     * @param catalog 分类
     * @param pageIndex 当前页数
     * @param handler 处理器
     * @param action 动作标识
     */
	public void loadLvCommentData(final int id,final int catalog,final int pageIndex,final Handler handler,final int action){  
		new Thread(){
			public void run() {
				Message msg = new Message();
				boolean isRefresh = false;
				if(action == UIHelper.LISTVIEW_ACTION_REFRESH || action == UIHelper.LISTVIEW_ACTION_SCROLL)
					isRefresh = true;
				try {
					CommentList commentlist = appContext.getCommentList(catalog, id, pageIndex, isRefresh);				
					msg.what = commentlist.getPageSize();
					msg.obj = commentlist;
	            } catch (AppException e) {
	            	e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
	            }
				msg.arg1 = action;//告知handler当前action
                handler.sendMessage(msg);
			}
		}.start();
	} 
	
}
