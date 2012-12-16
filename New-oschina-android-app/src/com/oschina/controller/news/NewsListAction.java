package com.oschina.controller.news;

import net.oschina.app.AppContext;
import net.oschina.app.adapter.ListViewNewsAdapter;
import net.oschina.app.bean.News;
import net.oschina.app.bean.NewsList;
import net.oschina.app.common.StringUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.app.widget.PullToRefreshListView;
import net.oschina.app.widget.PullToRefreshListView.OnRefreshListener;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.oschina.controller.data.LvHanlderFactory;
import com.oschina.controller.data.LvLoadData;
import com.oschina.controller.main.UIAction;
import com.oschina.model.LvData;
import net.oschina.app.holo.R;
import com.youxilua.framework.action.ApiBaseAction;

public class NewsListAction extends ApiBaseAction {
	private Handler lvNewsHandler;
	private ListViewNewsAdapter lvNewsAdapter;
	private View lvNews_footer;
	private TextView lvNews_foot_more;
	private ProgressBar lvNews_foot_progress;
	private PullToRefreshListView lvNews;
	public LvHanlderFactory LvHanlderFactory;

	public int isCheckPosition = 0;
	public LvLoadData lvLoadData;
	public NewsListAction(Context ctx, AQuery aq) {
		super(ctx, aq);
		// 初始化数据加载线程
		lvLoadData = new LvLoadData(getActivity());
		LvHanlderFactory = new LvHanlderFactory(ctx);
		
	}
	
	public void clearNewsListData(){
		LvData.lvNewsSumData = 0;
		LvData.lvNewsData.clear();
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
		lvNews = (PullToRefreshListView) mActionQuery.id(lvId).getView();
		lvNews.addFooterView(lvNews_footer);// 添加底部视图 必须在setAdapter前
		//lvNewsAdapter.setListView(lvNews);
		lvNews.setAdapter(lvNewsAdapter);
	
		lvNews.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		
		//---------------------------监听
		lvNews.setOnItemClickListener(lvNewsItemClickListener);
		lvNews.setOnScrollListener(lvNewsScrollListener);
		lvNews.setOnRefreshListener(lvNewsPullListener);
		lvNews.setOnItemSelectedListener(lvNewsItemSelectListener);
		//-----------------------------
		this.initLvData();
	}

	private void initLvData() {
		// 初始化Handler
		lvNewsHandler = LvHanlderFactory.getLvHandler(lvNews, lvNewsAdapter,
				lvNews_foot_more, lvNews_foot_progress, AppContext.PAGE_SIZE);
		// 加载资讯数据
		if (LvData.lvNewsData.isEmpty()) {
			lvLoadData.loadLvNewsData(NewsList.CATALOG_ALL, 0, lvNewsHandler,
					UIHelper.LISTVIEW_ACTION_INIT);
		}else{
			lvLoadData.loadLvNewsData(NewsList.CATALOG_ALL, 0, lvNewsHandler, UIHelper.LISTVIEW_ACTION_CHANGE_CATALOG);
		}
	}
	

	private OnItemClickListener lvNewsItemClickListener = new AdapterView.OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			Log.d("oschina","count-->"+ lvNews.getCheckedItemCount() + "item" + lvNews.getCheckedItemPosition() +"check" + id);
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
				Log.d("oschina","before" + "item" + lvNews.getCheckedItemPosition() +"position" + position);
				if(isCheckPosition != position){
					UIAction.showNewsRedirect(getFragmentManager(), news);
					isCheckPosition = position;
				}
			}else{
				UIHelper.showNewsRedirect(view.getContext(), news);
			}
			lvNews.setItemChecked(lvNews.getCheckedItemPosition()-1, true);
		}
	};
	
	private OnScrollListener lvNewsScrollListener = new AbsListView.OnScrollListener() {
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
				lvLoadData.loadLvNewsData(NewsList.CATALOG_ALL, pageIndex, lvNewsHandler,
						UIHelper.LISTVIEW_ACTION_SCROLL);
			}
		}

		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			lvNews.onScroll(view, firstVisibleItem, visibleItemCount,
					totalItemCount);
		}
	};
	
	private OnRefreshListener lvNewsPullListener = new PullToRefreshListView.OnRefreshListener() {
		public void onRefresh() {
			lvLoadData.loadLvNewsData(NewsList.CATALOG_ALL, 0, lvNewsHandler,
					UIHelper.LISTVIEW_ACTION_REFRESH);
		}
	};
	
	private OnItemSelectedListener lvNewsItemSelectListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			News news = null;
			// 判断是否是TextView
			// 数据为空--不用继续下面代码了
			if (LvData.lvNewsData.isEmpty())
				return;
			
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
			lvNews.setItemChecked(position-1, true);
			if(getActivity().getResources().getBoolean(R.bool.has_two_panes)){
				UIAction.showNewsRedirect(getFragmentManager(), news);
				isCheckPosition = position;
			}
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	};

}
