package com.youxilua.oschina.v.tweet;

import java.util.List;

import net.oschina.app.bean.Tweet;
import net.oschina.app.common.StringUtils;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.youxilua.dongtan.R;
import com.youxilua.utils.debug.AppDebug;

/**
 * @author youxiachai
 *
 */
public class LvTweetAdpater extends BaseAdapter {

	private List<Tweet> tweetLv;
	public AQuery itemQy;
	private LayoutInflater mInflater;

	private OnClickListener clickListener;

	public void setClickLisetener(OnClickListener clicker) {
		this.clickListener = clicker;
	}


	public LvTweetAdpater(Activity ctx, List<Tweet> tweetLv) {
		this.tweetLv = tweetLv;
		this.mInflater = ctx.getLayoutInflater();
		this.itemQy = new AQuery(ctx);
	}

	

	public LvTweetAdpater(Activity ctx, int layout) {
		itemQy = new AQuery(ctx);
		mInflater = ctx.getLayoutInflater();
	}

	public void setData(List<Tweet> lvTweet) {
		this.tweetLv = lvTweet;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return tweetLv.size();
	}

	@Override
	public Object getItem(int position) {
		return tweetLv.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private View getItemView(View v, int id) {
		return v.findViewById(id);
	}

	private boolean isInit = true;

	public void isInit(boolean is) {
		this.isInit = is;
	}
	
	/**回收机制,跟bitmap 的显示有问题
	 * @param url
	 * @param v
	 */
	private void setImageView(String url,View v){
		if (isInit) {
			if (url != null && !StringUtils.isEmpty(url)) {

				ImageOptions io = new ImageOptions();
				itemQy.id(v).image(url, io);

			} else {
				v.setVisibility(View.GONE);
			}
		}
	}
	

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// AQuery
		View itemView = convertView;
		ItemView itemHolder = null;
		Tweet tweet = tweetLv.get(position);
		String faceURL = tweet.getFace();
		String imageUrl = tweet.getImgBig();
		if (itemView == null) {

			itemView = mInflater.inflate(R.layout.item_lv_tweet, null);

			itemHolder = new ItemView();
			itemHolder.userface = getItemView(itemView, R.id.tweetPortrait);
			itemHolder.username = getItemView(itemView, R.id.tweetName);
			itemHolder.body = getItemView(itemView, R.id.tweetBody);
			itemHolder.image = getItemView(itemView, R.id.tweetImage);
			itemHolder.date = getItemView(itemView, R.id.tweetBefore);
			itemHolder.commentCount = getItemView(itemView,
					R.id.tweetCommentCount);
			itemHolder.commentButton = getItemView(itemView,
					R.id.tweetCommentButton);

			itemView.setTag(itemHolder);
			//加载大图片的时候需要主要跟缓存view 的回收显示问题
			setImageView(imageUrl,itemHolder.image);
			

		} else {
			itemHolder = (ItemView) itemView.getTag();
		}
	
		if (!isInit) {
			if (imageUrl != null && !StringUtils.isEmpty(imageUrl)) {
				if (itemQy.shouldDelay(position, itemView, parent, imageUrl)) {
					AppDebug.MethodLog(getClass(), "pos-->delay" + position + "image->"
							+ imageUrl);
				} else {
					AppDebug.MethodLog(getClass(), "pos-->nodelay" + position + "image->"
							+ imageUrl);
					ImageOptions io = new ImageOptions();
					itemQy.id(itemHolder.image).image(imageUrl, io);
				}
			} else {
				itemHolder.image.setVisibility(View.GONE);
			}
		}

	
		if (faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL)) {
			itemQy.id(itemHolder.userface).image(R.drawable.widget_dface);
		} else {
			if (!itemQy.shouldDelay(position, itemView, parent, faceURL)) {
				itemQy.id(itemHolder.userface).image(faceURL);
			}
				
		}
		itemQy.id(itemHolder.userface).clicked(clickListener).tag(tweet.getAuthorId());
		itemQy.id(itemHolder.username).text(tweet.getAuthor());
		itemQy.id(itemHolder.body).text(tweet.getBody());
		itemQy.id(itemHolder.date).text(StringUtils.friendly_time(tweet.getPubDate()));
		itemQy.id(itemHolder.commentCount).text(String.valueOf(tweet.getCommentCount()));
		itemQy.id(itemHolder.commentButton).clicked(clickListener).tag(position);
		return itemView;
	}

	static class ItemView { // 自定义控件集合
		public View userface;
		public View username;
		public View date;
		public View body;
		public View commentCount;
		public View commentButton;
		public View client;
		public View image;
	}

}
