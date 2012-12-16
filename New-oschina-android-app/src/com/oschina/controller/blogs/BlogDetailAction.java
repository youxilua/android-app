package com.oschina.controller.blogs;

import java.util.ArrayList;
import java.util.List;
import net.oschina.app.holo.R;
import net.oschina.app.AppContext;
import net.oschina.app.AppException;
import net.oschina.app.adapter.ListViewCommentAdapter;
import net.oschina.app.bean.Blog;
import net.oschina.app.bean.Comment;
import net.oschina.app.bean.Notice;
import net.oschina.app.common.StringUtils;
import net.oschina.app.common.UIHelper;
import net.oschina.app.widget.BadgeView;
import net.oschina.app.widget.PullToRefreshListView;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import net.oschina.app.holo.R;
import com.androidquery.AQuery;
import com.oschina.controller.data.LvLoadData;
import com.youxilua.framework.action.ApiBaseAction;

public class BlogDetailAction extends ApiBaseAction {

	public LvLoadData lvLoadData;

	private ImageView mDocTYpe;
	private TextView mTitle;
	private TextView mAuthor;
	private TextView mPubDate;
	private TextView mCommentCount;

	private WebView mWebView;
	private Handler mHandler;
	private Blog blogDetail;
	private int blogId;

	private PullToRefreshListView mLvComment;
	private ListViewCommentAdapter lvCommentAdapter;
	private List<Comment> lvCommentData = new ArrayList<Comment>();
	private View lvComment_footer;
	private TextView lvComment_foot_more;
	private ProgressBar lvComment_foot_progress;
	private Handler mCommentHandler;
	private int lvSumData;

	private int curId;
	private int curCatalog; // 博客评论分类
	private int curLvDataState;
	private int curLvPosition;// 当前listview选中的item位置

	private int _id;
	private int _uid;
	private String _content;

	private BadgeView bv_comment;
	private ImageView mCommentList;

	public BlogDetailAction(Context ctx, AQuery aq) {
		super(ctx, aq);
		lvLoadData = new LvLoadData(ctx);
	}

	public void initView(int id) {
		
		mActionQuery.id(R.id.blog_detail_header).gone();
		mActionQuery.id(R.id.blog_detail_footer).gone();
		
		blogId = id;

		mDocTYpe = (ImageView) findViewById(R.id.blog_detail_documentType);
		mTitle = (TextView) findViewById(R.id.blog_detail_title);
		mAuthor = (TextView) findViewById(R.id.blog_detail_author);
		mPubDate = (TextView) findViewById(R.id.blog_detail_date);
		mCommentCount = (TextView) findViewById(R.id.blog_detail_commentcount);

		mWebView = (WebView) findViewById(R.id.blog_detail_webview);
		mWebView.getSettings().setJavaScriptEnabled(false);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setDefaultFontSize(15);

	}

	// 初始化控件数据
	public void initData() {
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 1) {

					int docType = blogDetail.getDocumentType();
					if (docType == Blog.DOC_TYPE_ORIGINAL) {
						mDocTYpe.setImageResource(R.drawable.widget_original_icon);
					} else if (docType == Blog.DOC_TYPE_REPASTE) {
						mDocTYpe.setImageResource(R.drawable.widget_repaste_icon);
					}

					mTitle.setText(blogDetail.getTitle());
					mAuthor.setText(blogDetail.getAuthor());
					mPubDate.setText(StringUtils.friendly_time(blogDetail
							.getPubDate()));
					mCommentCount.setText(String.valueOf(blogDetail
							.getCommentCount()));

					// 是否收藏
//					if (blogDetail.getFavorite() == 1)
//						mFavorite
//								.setImageResource(R.drawable.widget_bar_favorite2);
//					else
//						mFavorite
//								.setImageResource(R.drawable.widget_bar_favorite);

//					// 显示评论数
//					if (blogDetail.getCommentCount() > 0) {
//						bv_comment.setText(blogDetail.getCommentCount() + "");
//						bv_comment.show();
//					} else {
//						bv_comment.setText("");
//						bv_comment.hide();
//					}

					String body = UIHelper.WEB_STYLE + blogDetail.getBody()
							+ "<div style=\"margin-bottom: 80px\" />";
					// 读取用户设置：是否加载文章图片--默认有wifi下始终加载图片
					boolean isLoadImage;
					AppContext ac = (AppContext) getActivity().getApplication();
					if (AppContext.NETTYPE_WIFI == ac.getNetworkType()) {
						isLoadImage = true;
					} else {
						isLoadImage = ac.isLoadImage();
					}
					if (isLoadImage) {
						body = body.replaceAll(
								"(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
						body = body.replaceAll(
								"(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
					} else {
						body = body.replaceAll("<\\s*img\\s+([^>]*)\\s*>", "");
					}

					mWebView.loadDataWithBaseURL(null, body, "text/html",
							"utf-8", null);
					mWebView.setWebViewClient(UIHelper.getWebViewClient());

					// 发送通知广播
					if (msg.obj != null) {
						UIHelper.sendBroadCast(getActivity(), (Notice) msg.obj);
					}
				} else if (msg.what == 0) {
					// headButtonSwitch(DATA_LOAD_FAIL);

					UIHelper.ToastMessage(getActivity(),
							R.string.msg_load_is_null);
				} else if (msg.what == -1 && msg.obj != null) {
					// headButtonSwitch(DATA_LOAD_FAIL);

					((AppException) msg.obj).makeToast(getActivity());
				}
			}
		};

		initData(blogId, false);
	}

	private void initData(final int blog_id, final boolean isRefresh) {
		// headButtonSwitch(DATA_LOAD_ING);

		new Thread() {
			public void run() {
				Message msg = new Message();
				try {
					blogDetail = ((AppContext) getApplication()).getBlog(
							blog_id, isRefresh);
					msg.what = (blogDetail != null && blogDetail.getId() > 0) ? 1
							: 0;
					msg.obj = (blogDetail != null) ? blogDetail.getNotice()
							: null;
				} catch (AppException e) {
					e.printStackTrace();
					msg.what = -1;
					msg.obj = e;
				}
				mHandler.sendMessage(msg);
			}
		}.start();
	}

}
