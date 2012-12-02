package com.oschina.controller.news;

import java.util.Date;

import net.oschina.app.AppConfig;
import net.oschina.app.AppContext;
import net.oschina.app.AppException;
import net.oschina.app.adapter.ListViewCommentAdapter;
import net.oschina.app.bean.Comment;
import net.oschina.app.bean.CommentList;
import net.oschina.app.bean.News;
import net.oschina.app.bean.News.Relative;
import net.oschina.app.bean.Notice;
import net.oschina.app.bean.Result;
import net.oschina.app.common.StringUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.app.widget.BadgeView;
import net.oschina.app.widget.PullToRefreshListView;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.androidquery.AQuery;
import com.oschina.controller.data.LvLoadData;
import com.oschina.model.LvData;
import com.oschina.view.R;
import com.youxilua.framework.action.ApiBaseAction;

/**
 * @author youxiachai
 * 双击全屏,收藏,分享没处理
 *
 */
public class NewsDetailsAction extends ApiBaseAction {
	private int newsId;
	private String tempCommentKey = AppConfig.TEMP_COMMENT;
	private LvLoadData lvLoadData;
	private News newsDetail;
	private WebView mWebView;
	private Handler mHandler;
	
	 private ViewSwitcher mViewSwitcher;

	private ViewSwitcher mFootViewSwitcher;
	private Button mFootPubcomment;
	private ImageView mFootEditebox;
	private EditText mFootEditer;
	private InputMethodManager imm;
	
    private int curId;
	private int curCatalog;	
	private int curLvDataState;
	private int curLvPosition;//当前listview选中的item位置
	
	private PullToRefreshListView mLvComment;
	private ListViewCommentAdapter lvCommentAdapter;
	private View lvComment_footer;
	private TextView lvComment_foot_more;
	private ProgressBar lvComment_foot_progress;
    private Handler mCommentHandler;
    private int lvSumData;
	
	

	public NewsDetailsAction(Context ctx, AQuery aq) {
		super(ctx, aq);
		// TODO Auto-generated constructor stub
		lvLoadData = new LvLoadData(getActivity());
	}

	private BadgeView bv_comment;
	
    //初始化视图控件
    public void initCommentView()
    {    	
    	lvComment_footer = getActivity().getLayoutInflater().inflate(R.layout.listview_footer, null);
    	lvComment_foot_more = (TextView)lvComment_footer.findViewById(R.id.listview_foot_more);
        lvComment_foot_progress = (ProgressBar)lvComment_footer.findViewById(R.id.listview_foot_progress);

    	lvCommentAdapter = new ListViewCommentAdapter(getActivity(), LvData.lvCommentData, R.layout.comment_listitem); 
    	mLvComment = (PullToRefreshListView)findViewById(R.id.comment_list_listview);
    	
        mLvComment.addFooterView(lvComment_footer);//添加底部视图  必须在setAdapter前
        mLvComment.setAdapter(lvCommentAdapter); 
        mLvComment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        		//点击头部、底部栏无效
        		if(position == 0 || view == lvComment_footer) return;
        		
        		Comment com = null;
        		//判断是否是TextView
        		if(view instanceof TextView){
        			com = (Comment)view.getTag();
        		}else{
            		ImageView img = (ImageView)view.findViewById(R.id.comment_listitem_userface);
            		com = (Comment)img.getTag();
        		} 
        		if(com == null) return;
        		
        		//跳转--回复评论界面
        	//	UIHelper.showCommentReply(NewsDetail.this,curId, curCatalog, com.getId(), com.getAuthorId(), com.getAuthor(), com.getContent());
        	}
		});
        mLvComment.setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				mLvComment.onScrollStateChanged(view, scrollState);
				
				//数据为空--不用继续下面代码了
				if(LvData.lvCommentData.size() == 0) return;
				
				//判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if(view.getPositionForView(lvComment_footer) == view.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}
				
				if(scrollEnd && curLvDataState==UIHelper.LISTVIEW_DATA_MORE)
				{
					mLvComment.setTag(UIHelper.LISTVIEW_DATA_LOADING);
					lvComment_foot_more.setText(R.string.load_ing);
					lvComment_foot_progress.setVisibility(View.VISIBLE);
					//当前pageIndex
					int pageIndex = lvSumData/20;
					lvLoadData.loadLvCommentData(curId, curCatalog, pageIndex, mCommentHandler, UIHelper.LISTVIEW_ACTION_SCROLL);
				}
			}
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				mLvComment.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
		});
        mLvComment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				//点击头部、底部栏无效
        		if(position == 0 || view == lvComment_footer) return false;				
				
        		Comment _com = null;
        		//判断是否是TextView
        		if(view instanceof TextView){
        			_com = (Comment)view.getTag();
        		}else{
            		ImageView img = (ImageView)view.findViewById(R.id.comment_listitem_userface);
            		_com = (Comment)img.getTag();
        		} 
        		if(_com == null) return false;
        		
        		final Comment com = _com;
        		
        		curLvPosition = LvData.lvCommentData.indexOf(com);
        		
        		final AppContext ac = (AppContext)getActivity().getApplication();
				//操作--回复 & 删除
        		int uid = ac.getLoginUid();
        		//判断该评论是否是当前登录用户发表的：true--有删除操作  false--没有删除操作
        		if(uid == com.getAuthorId())
        		{
	        		final Handler handler = new Handler(){
						public void handleMessage(Message msg) {
							if(msg.what == 1){
								Result res = (Result)msg.obj;
								if(res.OK()){
									lvSumData--;
									bv_comment.setText(lvSumData+"");
						    		bv_comment.show();
						    		LvData.lvCommentData.remove(com);
									lvCommentAdapter.notifyDataSetChanged();
								}
							//	UIHelper.ToastMessage(NewsDetail.this, res.getErrorMessage());
							}else{
								//((AppException)msg.obj).makeToast(NewsDetail.this);
							}
						}        			
	        		};
	        		final Thread thread = new Thread(){
						public void run() {
							Message msg = new Message();
							try {
								Result res = ac.delComment(curId, curCatalog, com.getId(), com.getAuthorId());
								msg.what = 1;
								msg.obj = res;
				            } catch (AppException e) {
				            	e.printStackTrace();
				            	msg.what = -1;
				            	msg.obj = e;
				            }
			                handler.sendMessage(msg);
						}        			
	        		};
	        	//	UIHelper.showCommentOptionDialog(NewsDetail.this, curId, curCatalog, com, thread);
        		}
        		else
        		{
        			//UIHelper.showCommentOptionDialog(NewsDetail.this, curId, curCatalog, com, null);
        		}
				return true;
			}        	
		});
        mLvComment.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				lvLoadData.loadLvCommentData(curId, curCatalog, 0, mCommentHandler, UIHelper.LISTVIEW_ACTION_REFRESH);
            }
        });
    }
    //初始化评论数据
	public void initCommentData()
	{
		curId = newsId;
		curCatalog = CommentList.CATALOG_NEWS;
		
    	mCommentHandler = new Handler()
		{
			public void handleMessage(Message msg) 
			{
				if(msg.what >= 0){						
					CommentList list = (CommentList)msg.obj;
					Notice notice = list.getNotice();
					//处理listview数据
					switch (msg.arg1) {
					case UIHelper.LISTVIEW_ACTION_INIT:
					case UIHelper.LISTVIEW_ACTION_REFRESH:
						lvSumData = msg.what;
						LvData.lvCommentData.clear();//先清除原有数据
						LvData.lvCommentData.addAll(list.getCommentlist());
						break;
					case UIHelper.LISTVIEW_ACTION_SCROLL:
						lvSumData += msg.what;
						if(LvData.lvCommentData.size() > 0){
							for(Comment com1 : list.getCommentlist()){
								boolean b = false;
								for(Comment com2 : LvData.lvCommentData){
									if(com1.getId() == com2.getId() && com1.getAuthorId() == com2.getAuthorId()){
										b = true;
										break;
									}
								}
								if(!b) LvData.lvCommentData.add(com1);
							}
						}else{
							LvData.lvCommentData.addAll(list.getCommentlist());
						}
						break;
					}	
					
					//评论数更新
					if(newsDetail != null && LvData.lvCommentData.size() > newsDetail.getCommentCount()){
						newsDetail.setCommentCount(LvData.lvCommentData.size());
						bv_comment.setText(LvData.lvCommentData.size()+"");
						bv_comment.show();
					}
					
					if(msg.what < 20){
						curLvDataState = UIHelper.LISTVIEW_DATA_FULL;
						lvCommentAdapter.notifyDataSetChanged();
						lvComment_foot_more.setText(R.string.load_full);
					}else if(msg.what == 20){					
						curLvDataState = UIHelper.LISTVIEW_DATA_MORE;
						lvCommentAdapter.notifyDataSetChanged();
						lvComment_foot_more.setText(R.string.load_more);
					}
					//发送通知广播
					if(notice != null){
						//UIHelper.sendBroadCast(NewsDetail.this, notice);
					}
				}
				else if(msg.what == -1){
					//有异常--显示加载出错 & 弹出错误消息
					curLvDataState = UIHelper.LISTVIEW_DATA_MORE;
					lvComment_foot_more.setText(R.string.load_error);
				//	((AppException)msg.obj).makeToast(NewsDetail.this);
				}
				if(LvData.lvCommentData.size()==0){
					curLvDataState = UIHelper.LISTVIEW_DATA_EMPTY;
					lvComment_foot_more.setText(R.string.load_empty);
				}
				lvComment_foot_progress.setVisibility(View.GONE);
				if(msg.arg1 == UIHelper.LISTVIEW_ACTION_REFRESH){
					mLvComment.onRefreshComplete("完成刷新" + new Date().toLocaleString());
					mLvComment.setSelection(0);
				}
			}
		};
	
		lvLoadData.loadLvCommentData(curId,curCatalog,0,mCommentHandler,UIHelper.LISTVIEW_ACTION_INIT);
    }

	public void initView(int newsId) {
		this.newsId = newsId;
		if (newsId > 0)
			tempCommentKey = AppConfig.TEMP_COMMENT + "_"
					+ CommentList.CATALOG_NEWS + "_" + newsId;
		mViewSwitcher = (ViewSwitcher) mActionQuery.id(R.id.news_detail_viewswitcher).getView();
		// 设置详情不可点击
		mActionQuery.id(R.id.news_detail_footbar_detail).enabled(false);

		mWebView = mActionQuery.id(R.id.news_detail_webview).getWebView();
		mWebView.getSettings().setJavaScriptEnabled(false);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDefaultFontSize(15);

		// ---初始化头部监听
		setViewListenerById(R.id.news_detail_home, homeClickListener);
		// setViewListenerById(R.id.news_detail_footbar_favorite,
		// favoriteClickListener);
		setViewListenerById(R.id.news_detail_refresh, refreshClickListener);
		setViewListenerById(R.id.news_detail_author, authorClickListener);
		setViewListenerById(R.id.news_detail_footbar_share, shareClickListener);
		setViewListenerById(R.id.news_detail_footbar_detail,
				detailClickListener);
		setViewListenerById(R.id.news_detail_footbar_commentlist,
				commentlistClickListener);
		// ---
		bv_comment = new BadgeView(getActivity(), mActionQuery.id(
				R.id.news_detail_footbar_commentlist).getView());
		bv_comment.setBackgroundResource(R.drawable.widget_count_bg2);
		bv_comment.setIncludeFontPadding(false);
		bv_comment.setGravity(Gravity.CENTER);
		bv_comment.setTextSize(8f);
		bv_comment.setTextColor(Color.WHITE);

		imm = (InputMethodManager) getActivity().getSystemService(
				Activity.INPUT_METHOD_SERVICE);

		mFootViewSwitcher = (ViewSwitcher) findViewById(R.id.news_detail_foot_viewswitcher);
		mFootPubcomment = (Button) findViewById(R.id.news_detail_foot_pubcomment);
		// mFootPubcomment.setOnClickListener(commentpubClickListener);
		mFootEditebox = (ImageView) findViewById(R.id.news_detail_footbar_editebox);
		mFootEditebox.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mFootViewSwitcher.showNext();
				mFootEditer.setVisibility(View.VISIBLE);
				mFootEditer.requestFocus();
				mFootEditer.requestFocusFromTouch();
			}
		});
		mFootEditer = (EditText) findViewById(R.id.news_detail_foot_editer);
		mFootEditer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					imm.showSoftInput(v, 0);
				} else {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
		});
		mFootEditer.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					if (mFootViewSwitcher.getDisplayedChild() == 1) {
						mFootViewSwitcher.setDisplayedChild(0);
						mFootEditer.clearFocus();
						mFootEditer.setVisibility(View.GONE);
					}
					return true;
				}
				return false;
			}
		});
		// TODO
		// 编辑器添加文本监听
		// mFootEditer.addTextChangedListener(UIHelper.getTextWatcher(getActivity(),
		// tempCommentKey));

		// TODO
		// 显示临时编辑内容
		// UIHelper.showTempEditContent(getActivity(), mFootEditer,
		// tempCommentKey);

	}
	private final static int VIEWSWITCH_TYPE_DETAIL = 0x001;
	private final static int VIEWSWITCH_TYPE_COMMENTS = 0x002;
	
	private final static int DATA_LOAD_ING = 0x001;
	private final static int DATA_LOAD_COMPLETE = 0x002;
	private final static int DATA_LOAD_FAIL = 0x003;
	
    //初始化控件数据
   	public void initData()
   	{		
   		mHandler = new Handler()
   		{
   			public void handleMessage(Message msg) 
   			{				
   				if(msg.what == 1)
   				{	
   					//TODO t头部按钮的加载
   					headButtonSwitch(DATA_LOAD_COMPLETE);					
   					mActionQuery.id(R.id.news_detail_title).text(newsDetail.getTitle());
   					mActionQuery.id(R.id.news_detail_author).text(newsDetail.getAuthor());
   					mActionQuery.id(R.id.news_detail_date).text(StringUtils.friendly_time(newsDetail.getPubDate()));
   					mActionQuery.id(R.id.news_detail_commentcount).text(String.valueOf(newsDetail.getCommentCount()));

   					
   					//是否收藏
   					if(newsDetail.getFavorite() == 1)
   						mActionQuery.id(R.id.news_detail_footbar_favorite).image(R.drawable.widget_bar_favorite2);
   					else
   						mActionQuery.id(R.id.news_detail_footbar_favorite).image(R.drawable.widget_bar_favorite);
   					
   					//显示评论数
   					if(newsDetail.getCommentCount() > 0){
   						bv_comment.setText(newsDetail.getCommentCount()+"");
   						bv_comment.show();
   					}else{
   						bv_comment.setText("");
   						bv_comment.hide();
   					}
   					
   					String body = UIHelper.WEB_STYLE + newsDetail.getBody();					
   					//读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
   					boolean isLoadImage;
   					AppContext ac = (AppContext)getActivity().getApplication();
   					if(AppContext.NETTYPE_WIFI == ac.getNetworkType()){
   						isLoadImage = true;
   					}else{
   						isLoadImage = ac.isLoadImage();
   					}
   					if(isLoadImage){
   						//过滤掉 img标签的width,height属性
   						body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+","$1");
   						body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+","$1");
   					}else{
   						//过滤掉 img标签
   						body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>","");
   					}
   					
   					//更多关于***软件的信息
   					String softwareName = newsDetail.getSoftwareName(); 
   					String softwareLink = newsDetail.getSoftwareLink(); 
   					if(!StringUtils.isEmpty(softwareName) && !StringUtils.isEmpty(softwareLink))
   						body += String.format("<div id='oschina_software' style='margin-top:8px;color:#FF0000;font-weight:bold'>更多关于:&nbsp;<a href='%s'>%s</a>&nbsp;的详细信息</div>", softwareLink, softwareName);
   					
   					//相关新闻
   					if(newsDetail.getRelatives().size() > 0)
   					{
   						String strRelative = "";
   						for(Relative relative : newsDetail.getRelatives()){
   							strRelative += String.format("<a href='%s' style='text-decoration:none'>%s</a><p/>", relative.url, relative.title);
   						}
   						body += String.format("<p/><hr/><b>相关资讯</b><div><p/>%s</div>", strRelative);
   					}
   					
   					body += "<div style='margin-bottom: 80px'/>";					
   					
   					mWebView.loadDataWithBaseURL(null, body, "text/html", "utf-8",null);
   					mWebView.setWebViewClient(UIHelper.getWebViewClient());	
   					
   					//发送通知广播
   					if(msg.obj != null){
   						UIHelper.sendBroadCast(getActivity(), (Notice)msg.obj);
   					}
   				}
   				else if(msg.what == 0)
   				{
   					headButtonSwitch(DATA_LOAD_FAIL);
   					
   					//UIHelper.ToastMessage(NewsDetail.this, R.string.msg_load_is_null);
   				}
   				else if(msg.what == -1 && msg.obj != null)
   				{
   					headButtonSwitch(DATA_LOAD_FAIL);
   					
   					((AppException)msg.obj).makeToast(getActivity());
   				}				
   			}
   		};
   		
   		initData(newsId, false);
   	}
    /**
     * 头部按钮展示
     * @param type
     */
    private void headButtonSwitch(int type) {
    	switch (type) {
		case DATA_LOAD_ING:
			mActionQuery.id(R.id.news_detail_scrollview).gone();
			mActionQuery.id(R.id.news_detail_head_progress).visible();
			mActionQuery.id(R.id.news_detail_refresh).gone();
			break;
		case DATA_LOAD_COMPLETE:
			mActionQuery.id(R.id.news_detail_scrollview).visible();
			mActionQuery.id(R.id.news_detail_head_progress).gone();
			mActionQuery.id(R.id.news_detail_refresh).visible();
			break;
		case DATA_LOAD_FAIL:
			mActionQuery.id(R.id.news_detail_scrollview).gone();
			mActionQuery.id(R.id.news_detail_head_progress).gone();
			mActionQuery.id(R.id.news_detail_refresh).visible();
			break;
		}
    }
   	
    private void initData(final int news_id, final boolean isRefresh)
    {		
    	headButtonSwitch(DATA_LOAD_ING);
    	
		new Thread(){
			public void run() {
                Message msg = new Message();
				try {
					newsDetail = ((AppContext)getActivity().getApplication()).getNews(news_id, isRefresh);
	                msg.what = (newsDetail!=null && newsDetail.getId()>0) ? 1 : 0;
	                msg.obj = (newsDetail!=null) ? newsDetail.getNotice() : null;//通知信息
	            } catch (AppException e) {
	                e.printStackTrace();
	            	msg.what = -1;
	            	msg.obj = e;
	            }				
                mHandler.sendMessage(msg);
			}
		}.start();
    }

	private void setViewListenerById(int id, OnClickListener listener) {
		mActionQuery.id(id).clicked(listener);
	}

	private View.OnClickListener homeClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			// UIHelper.showHome(NewsDetail.this);
		}
	};

	private View.OnClickListener refreshClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			 initData(newsId, true);
			 lvLoadData.loadLvCommentData(curId,curCatalog,0,mCommentHandler,UIHelper.LISTVIEW_ACTION_REFRESH);
		}
	};

	private View.OnClickListener authorClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			 UIHelper.showUserCenter(v.getContext(), newsDetail.getAuthorId(),
			 newsDetail.getAuthor());
		}
	};

	private View.OnClickListener shareClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			 if(newsDetail == null){
			 UIHelper.ToastMessage(v.getContext(),
			 R.string.msg_read_detail_fail);
			 return;
			 }
			// 分享到
			// UIHelper.showShareDialog(NewsDetail.this, newsDetail.getTitle(),
			// newsDetail.getUrl());
		}
	};

	private View.OnClickListener detailClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			if (newsId == 0) {
				return;
			}
			// 切换到详情
			 viewSwitch(VIEWSWITCH_TYPE_DETAIL);
		}
	};

	private View.OnClickListener commentlistClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			if (newsId == 0) {
				return;
			}
			// 切换到评论
			 viewSwitch(VIEWSWITCH_TYPE_COMMENTS);
		}
	};
	
	
	/**
     * 底部栏切换
     * @param type
     */
    private void viewSwitch(int type) {
    	switch (type) {
		case VIEWSWITCH_TYPE_DETAIL:
			// 设置详情不可点击
			mActionQuery.id(R.id.news_detail_footbar_detail).enabled(false);
			mActionQuery.id(R.id.news_detail_footbar_commentlist).enabled(true);
			mActionQuery.id(R.id.news_detail_head_title).text(R.string.news_detail_head_title);
			mViewSwitcher.setDisplayedChild(0);			
			break;
		case VIEWSWITCH_TYPE_COMMENTS:
			mActionQuery.id(R.id.news_detail_footbar_detail).enabled(true);
			mActionQuery.id(R.id.news_detail_footbar_commentlist).enabled(false);
			mActionQuery.id(R.id.news_detail_head_title).text(R.string.comment_list_head_title);
			mViewSwitcher.setDisplayedChild(1);
			break;
    	}
    }
}
