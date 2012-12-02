package com.oschina.controller.news;

import net.oschina.app.AppContext;
import net.oschina.app.AppException;
import net.oschina.app.adapter.ListViewNewsAdapter;
import net.oschina.app.bean.News;
import net.oschina.app.bean.NewsList;
import net.oschina.app.common.StringUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.app.widget.PullToRefreshListView;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.oschina.controller.data.LvHanlderFactory;
import com.oschina.controller.data.LvLoadData;
import com.oschina.controller.main.UIAction;
import com.oschina.model.LvData;
import com.oschina.view.R;
import com.youxilua.framework.action.ApiBaseAction;

public class NewsListAction extends ApiBaseAction {
	private Handler lvNewsHandler;
	private ListViewNewsAdapter lvNewsAdapter;
	private View lvNews_footer;
	private TextView lvNews_foot_more;
	private ProgressBar lvNews_foot_progress;
	private PullToRefreshListView lvNews;

	private int curNewsCatalog = NewsList.CATALOG_ALL;

	private LvLoadData lvLoadData;
	public NewsListAction(Context ctx, AQuery aq) {
		super(ctx, aq);
		// 初始化数据加载线程
		lvLoadData = new LvLoadData(getActivity());
	}
	
	public void initNewsList(int lvId) {
		lvNewsAdapter = new ListViewNewsAdapter(getActivity(), LvData.lvNewsData,
				R.layout.news_listitem);
		lvNews_footer = getActivity().getLayoutInflater().inflate(
				R.layout.listview_footer, null);
		lvNews_foot_more = (TextView) lvNews_footer
				.findViewById(R.id.listview_foot_more);
		lvNews_foot_progress = (ProgressBar) lvNews_footer
				.findViewById(R.id.listview_foot_progress);
//		lvNews = (PullToRefreshListView) getView().findViewById(
//				R.id.frame_listview_news);
		lvNews = (PullToRefreshListView) mActionQuery.id(lvId).getView();
		lvNews.addFooterView(lvNews_footer);// 添加底部视图 必须在setAdapter前
		lvNews.setAdapter(lvNewsAdapter);
		lvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击头部、底部栏无效
				if (position == 0 || view == lvNews_footer)
					return;

				News news = null;
				// 判断是否是TextView
				if (view instanceof TextView) {
					news = (News) view.getTag();
				} else {
					TextView tv = (TextView) view
							.findViewById(R.id.news_listitem_title);
					news = (News) tv.getTag();
				}
				if (news == null)
					return;

				// 跳转到新闻详情
				if(getActivity().getResources().getBoolean(R.bool.has_two_panes)){
					UIAction.showNewsRedirect(getFragmentManager(), news);
				}else{
					UIAction.showNewsRedirect(view.getContext(), news);
				}
			}
		});
		lvNews.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				lvNews.onScrollStateChanged(view, scrollState);

				// 数据为空--不用继续下面代码了
				if (LvData.lvNewsData.isEmpty())
					return;

				// 判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if (view.getPositionForView(lvNews_footer) == view
							.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}

				int lvDataState = StringUtils.toInt(lvNews.getTag());
				if (scrollEnd && lvDataState == UIHelper.LISTVIEW_DATA_MORE) {
					lvNews.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					lvNews_foot_more.setText(R.string.load_ing);
					lvNews_foot_progress.setVisibility(View.VISIBLE);
					// 当前pageIndex
					int pageIndex =LvData.lvNewsSumData / AppContext.PAGE_SIZE;
					lvLoadData.loadLvNewsData(curNewsCatalog, pageIndex, lvNewsHandler,
							UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				lvNews.onScroll(view, firstVisibleItem, visibleItemCount,
						totalItemCount);
			}
		});
		lvNews.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				lvLoadData.loadLvNewsData(curNewsCatalog, 0, lvNewsHandler,
						UIHelper.LISTVIEW_ACTION_REFRESH);
			}
		});

		this.initLvData();
	}

	private void initLvData() {
		// 初始化Handler
		lvNewsHandler = LvHanlderFactory.getLvHandler(lvNews, lvNewsAdapter,
				lvNews_foot_more, lvNews_foot_progress, AppContext.PAGE_SIZE);
		// 加载资讯数据
		if (LvData.lvNewsData.isEmpty()) {
			lvLoadData.loadLvNewsData(curNewsCatalog, 0, lvNewsHandler,
					UIHelper.LISTVIEW_ACTION_INIT);
		}
	}
	
	
	
	
	public void clearNewsListData(){
		LvData.lvNewsSumData = 0;
		LvData.lvNewsData.clear();
	}

}
