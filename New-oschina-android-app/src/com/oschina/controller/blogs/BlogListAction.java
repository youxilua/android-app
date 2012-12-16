package com.oschina.controller.blogs;

import net.oschina.app.AppContext;
import net.oschina.app.holo.R;
import net.oschina.app.adapter.ListViewBlogAdapter;
import net.oschina.app.bean.Blog;
import net.oschina.app.bean.BlogList;
import net.oschina.app.common.StringUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.app.widget.PullToRefreshListView;
import net.oschina.app.widget.PullToRefreshListView.OnRefreshListener;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.oschina.app.*;

import com.androidquery.AQuery;
import com.oschina.controller.data.LvHanlderFactory;
import com.oschina.controller.data.LvLoadData;
import com.oschina.controller.main.UIAction;
import com.oschina.model.LvData;
import com.youxilua.framework.action.ApiBaseAction;

public class BlogListAction extends ApiBaseAction {

	private PullToRefreshListView lvBlog;
	private ListViewBlogAdapter lvBlogAdapter;
	private Handler lvBlogHandler;

	private View lvBlog_footer;

	private TextView lvBlog_foot_more;

	private ProgressBar lvBlog_foot_progress;

	public LvLoadData lvLoadData;
	
	public LvHanlderFactory LvHanlderFactory;
	
	public int isCheckPosition = 0;

	public BlogListAction(Context ctx, AQuery aq) {
		super(ctx, aq);
		// TODO Auto-generated constructor stub
		// 初始化数据加载线程
		lvLoadData = new LvLoadData(getActivity());
		LvHanlderFactory = new LvHanlderFactory(ctx);
	
	}
	
	private OnItemClickListener lvBlogItemClick = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			// 点击头部、底部栏无效
			if (position == 0 || view == lvBlog_footer)
				return;

			Blog blog = null;
			// 判断是否是TextView
			if (view instanceof TextView) {
				blog = (Blog) view.getTag();
			} else {
				TextView tv = (TextView) view
						.findViewById(R.id.blog_listitem_title);
				blog = (Blog) tv.getTag();
			}
			if (blog == null)
				return;
			
			if(getActivity().getResources().getBoolean(R.bool.has_two_panes)){
				if(isCheckPosition != position){
					UIAction.showUrlRedirect(getFragmentManager(), blog.getUrl());
					isCheckPosition = position;
				}
			}else{
				// 跳转到博客详情
				UIHelper.showUrlRedirect(view.getContext(), blog.getUrl());
			}
			
			lvBlog.setItemChecked(lvBlog.getCheckedItemPosition()-1, true);

		}
	};
	
	private OnRefreshListener lvBlogRefreshListener = new PullToRefreshListView.OnRefreshListener() {
		public void onRefresh() {
			lvLoadData.loadLvBlogData(lvCurrentCategory, 0,
					lvBlogHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
		}
	};
	
	private OnScrollListener lvBlogOnScroll = new AbsListView.OnScrollListener() {
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			lvBlog.onScrollStateChanged(view, scrollState);

			// 数据为空--不用继续下面代码了
			if (LvData.lvBlogData.isEmpty())
				return;

			// 判断是否滚动到底部
			boolean scrollEnd = false;
			try {
				if (view.getPositionForView(lvBlog_footer) == view
						.getLastVisiblePosition())
					scrollEnd = true;
			} catch (Exception e) {
				scrollEnd = false;
			}

			int lvDataState = StringUtils.toInt(lvBlog.getTag());
			if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
				lvBlog.setTag(UIHelper.LISTVIEW_DATA_LOADING);
				lvBlog_foot_more.setText(R.string.load_ing);
				lvBlog_foot_progress.setVisibility(View.VISIBLE);
				// 当前pageIndex
				int pageIndex = LvData.lvBlogSumData / AppContext.PAGE_SIZE;
				lvLoadData.loadLvBlogData(lvCurrentCategory, pageIndex,
						lvBlogHandler, UIHelper.LISTVIEW_ACTION_SCROLL);
			}
		}

		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			lvBlog.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	};
	
	private OnItemSelectedListener lvBlogItemSelectListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// 点击头部、底部栏无效
			if (position == 0 || view == lvBlog_footer)
				return;

			Blog blog = null;
			// 判断是否是TextView
			if (view instanceof TextView) {
				blog = (Blog) view.getTag();
			} else {
				TextView tv = (TextView) view
						.findViewById(R.id.blog_listitem_title);
				blog = (Blog) tv.getTag();
			}
			if (blog == null)
				return;
			lvBlog.setItemChecked(position-1, true);
			if(getActivity().getResources().getBoolean(R.bool.has_two_panes)){
				UIAction.showUrlRedirect(getFragmentManager(), blog.getUrl());
				isCheckPosition = position;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	};
	
	
	private int lvCurrentCategory = BlogList.CATALOG_LATEST;
	/**
	 * 初始化博客列表
	 */
	public void initBlogListView(int lvId,int blogCategory) {
		lvCurrentCategory = blogCategory;
		
		lvBlogAdapter = new ListViewBlogAdapter(getActivity(),
				BlogList.CATALOG_LATEST, LvData.lvBlogData,
				R.layout.blog_listitem);
		lvBlog_footer = getLayoutInflater().inflate(R.layout.listview_footer,
				null);
		lvBlog_foot_more = (TextView) lvBlog_footer
				.findViewById(R.id.listview_foot_more);
		lvBlog_foot_progress = (ProgressBar) lvBlog_footer
				.findViewById(R.id.listview_foot_progress);
		lvBlog = (PullToRefreshListView) findViewById(lvId);
		lvBlog.addFooterView(lvBlog_footer);// 添加底部视图 必须在setAdapter前
		
		lvBlogAdapter.setBlogListview(lvBlog);
		lvBlog.setAdapter(lvBlogAdapter);
		
		lvBlog.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		lvBlog.setOnItemClickListener(lvBlogItemClick);
		lvBlog.setOnScrollListener(lvBlogOnScroll);
		lvBlog.setOnRefreshListener(lvBlogRefreshListener);
		
		lvBlog.setOnItemSelectedListener(lvBlogItemSelectListener);
		this.initLvData();
	}

	private void initLvData() {
		// 初始化Handler
		lvBlogHandler = LvHanlderFactory.getLvHandler(lvBlog, lvBlogAdapter,
				lvBlog_foot_more, lvBlog_foot_progress, AppContext.PAGE_SIZE);
		if (LvData.lvBlogData.isEmpty()) {
			lvLoadData.loadLvBlogData(lvCurrentCategory, 0, lvBlogHandler,
					UIHelper.LISTVIEW_ACTION_INIT);
		}else{
			lvLoadData.loadLvBlogData(lvCurrentCategory, 0, lvBlogHandler,
					UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
		}
	}

}
