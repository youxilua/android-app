package com.oschina.controller.data;

import java.util.Date;

import net.oschina.app.bean.News;
import net.oschina.app.bean.NewsList;
import net.oschina.app.bean.Notice;
import net.oschina.app.common.UIHelper;
import net.oschina.app.widget.PullToRefreshListView;
import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oschina.model.LvData;
import com.oschina.view.R;

public class LvHanlderFactory {
	
	public final static int INITHEAD = 1;
	/**
	 * 获取listview的初始化Handler
	 * 
	 * @param lv
	 * @param adapter
	 * @return
	 */
	public static Handler getLvHandler(final PullToRefreshListView lv,
			final BaseAdapter adapter, final TextView more,
			final ProgressBar progress, final int pageSize) {
		return new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what >= 0) {
					// listview数据处理
					Notice notice = handleLvData(msg.what, msg.obj, msg.arg2,
							msg.arg1);
					if (msg.what < pageSize) {
						lv.setTag(UIHelper.LISTVIEW_DATA_FULL);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_full);
					} else if (msg.what == pageSize) {
						lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
						adapter.notifyDataSetChanged();
						more.setText(R.string.load_more);
					}
				
					
				} else if (msg.what == -1) {
					// 有异常--显示加载出错 & 弹出错误消息
					lv.setTag(UIHelper.LISTVIEW_DATA_MORE);
					more.setText(R.string.load_error);
					// ((AppException)msg.obj).makeToast(Main.this);
				}
				if (adapter.getCount() == 0) {
					lv.setTag(UIHelper.LISTVIEW_DATA_EMPTY);
					more.setText(R.string.load_empty);
				}
				progress.setVisibility(ProgressBar.GONE);
				if (msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH) {
					lv.onRefreshComplete("最近更新："
							+ new Date().toLocaleString());
					lv.requestFocusFromTouch();
					lv.setSelection(INITHEAD);
					
				} else if (msg.arg1 == UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG) {
					lv.onRefreshComplete();
					lv.requestFocusFromTouch();
					lv.setSelection(INITHEAD);
				}else if(msg.arg1 == UIHelper.LISTVIEW_ACTION_INIT){
					lv.requestFocusFromTouch();
					lv.setSelection(INITHEAD);
				}
			}
		};
	}
	
	//-------------
	/**
     * listview数据处理
     * @param what 数量
     * @param obj 数据
     * @param objtype 数据类型
     * @param actiontype 操作类型
     * @return notice 通知信息
     */
    public static Notice handleLvData(int what,Object obj,int objtype,int actiontype){
    	Notice notice = null;
		switch (actiontype) {
			case UIHelper.LISTVIEW_ACTION_INIT:
			case UIHelper.LISTVIEW_ACTION_REFRESH:
			case UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG:
				int newdata = 0;//新加载数据-只有刷新动作才会使用到
				switch (objtype) {
					case UIHelper.LISTVIEW_DATATYPE_NEWS:
						NewsList nlist = (NewsList)obj;
						notice = nlist.getNotice();
						LvData.lvNewsSumData = what;
						if(actiontype == UIHelper.LISTVIEW_ACTION_REFRESH){
							if(LvData.lvNewsData.size() > 0){
								for(News news1 : nlist.getNewslist()){
									boolean b = false;
									for(News news2 : LvData.lvNewsData){
										if(news1.getId() == news2.getId()){
											b = true;
											break;
										}
									}
									if(!b) newdata++;
								}
							}else{
								newdata = what;
							}
						}
						LvData.lvNewsData.clear();//先清除原有数据
						LvData.lvNewsData.addAll(nlist.getNewslist());
						break;
				}
				if(actiontype == UIHelper.LISTVIEW_ACTION_REFRESH){
					//提示新加载数据
					if(newdata >0){
						//NewDataToast.makeText(getActivity(), getString(R.string.new_data_toast_message, newdata), appContext.isAppSound()).show();
					}else{
					//	NewDataToast.makeText(getActivity(), getString(R.string.new_data_toast_none), false).show();
					}
				}
				break;
			case UIHelper.LISTVIEW_ACTION_SCROLL:
				switch (objtype) {
					case UIHelper.LISTVIEW_DATATYPE_NEWS:
						NewsList list = (NewsList)obj;
						notice = list.getNotice();
						LvData.lvNewsSumData += what;
						if(LvData.lvNewsData.size() > 0){
							for(News news1 : list.getNewslist()){
								boolean b = false;
								for(News news2 : LvData.lvNewsData){
									if(news1.getId() == news2.getId()){
										b = true;
										break;
									}
								}
								if(!b) LvData.lvNewsData.add(news1);
							}
						}else{
							LvData.lvNewsData.addAll(list.getNewslist());
						}
						break;
				}
				break;
		}
		return notice;
    }
	//----------------
	
}
