package com.oschina.controller.blogs;

import net.oschina.app.AppContext;
import net.oschina.app.R;
import net.oschina.app.adapter.ListViewBlogAdapter;
import net.oschina.app.bean.Blog;
import net.oschina.app.bean.BlogList;
import net.oschina.app.bean.NewsList;
import net.oschina.app.common.StringUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.app.widget.PullToRefreshListView;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.oschina.controller.data.LvHanlderFactory;
import com.oschina.controller.data.LvLoadData;
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

	public BlogListAction(Context ctx, AQuery aq) {
		super(ctx, aq);
		// TODO Auto-generated constructor stub
		// 初始化数据加载线程
		lvLoadData = new LvLoadData(getActivity());
		LvHanlderFactory = new LvHanlderFactory(ctx);
	
	}

	/**
	 * 初始化博客列表
	 */
	public void initBlogListView(int lvId) {
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
		lvBlog.setAdapter(lvBlogAdapter);
		lvBlog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

				// 跳转到博客详情
				UIHelper.showUrlRedirect(view.getContext(), blog.getUrl());
			}
		});
		lvBlog.setOnScrollListener(new AbsListView.OnScrollListener() {
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
					lvLoadData.loadLvBlogData(BlogList.CATALOG_LATEST, pageIndex,
							lvBlogHandler, UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lvBlog.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
		lvBlog.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				lvLoadData.loadLvBlogData(BlogList.CATALOG_LATEST, 0,
						lvBlogHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
			}
		});
		this.initLvData();
	}

	private void initLvData() {
		// 初始化Handler
		lvBlogHandler = LvHanlderFactory.getLvHandler(lvBlog, lvBlogAdapter,
				lvBlog_foot_more, lvBlog_foot_progress, AppContext.PAGE_SIZE);
		if (LvData.lvBlogData.isEmpty()) {
			lvLoadData.loadLvBlogData(BlogList.CATALOG_LATEST, 0, lvBlogHandler,
					UIHelper.LISTVIEW_ACTION_INIT);
		}else{
			lvLoadData.loadLvBlogData(BlogList.CATALOG_LATEST, 0, lvBlogHandler,
					UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
		}
	}

}
