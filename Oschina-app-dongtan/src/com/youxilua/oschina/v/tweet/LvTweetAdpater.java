package com.youxilua.oschina.v.tweet;

import java.util.List;

import net.oschina.app.bean.Tweet;
import net.oschina.app.common.StringUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.youxilua.dongtan.R;
import com.youxilua.utils.debug.AppDebug;

public class LvTweetAdpater extends BaseAdapter {

	private List<Tweet> tweetLv;
	public AQuery imageQuery;

	private OnClickListener clickListener;

	public void setClickLisetener(OnClickListener clicker) {
		this.clickListener = clicker;
	}

	private Activity ctx;

	public LvTweetAdpater(Activity ctx, List<Tweet> tweetLv) {
		this.tweetLv = tweetLv;
		this.ctx = ctx;
		mFliater = ctx.getLayoutInflater();
		imageQuery = new AQuery(ctx);
	}

	LayoutInflater mFliater;

	public LvTweetAdpater(Activity ctx, int layout) {
		imageQuery = new AQuery(ctx);
		mFliater = ctx.getLayoutInflater();
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// AQuery
		View itemView = convertView;
		ListItemView listItemView = null;
		Tweet tweet = tweetLv.get(position);
		String image = tweet.getImgBig();
		if (itemView == null) {

			itemView = mFliater.inflate(R.layout.item_lv_tweet, null);

			listItemView = new ListItemView();
			listItemView.userface = getItemView(itemView, R.id.tweetPortrait);
			listItemView.username = getItemView(itemView, R.id.tweetName);
			listItemView.body = getItemView(itemView, R.id.tweetBody);
			listItemView.image = getItemView(itemView, R.id.tweetImage);
			listItemView.date = getItemView(itemView, R.id.tweetBefore);
			listItemView.commentCount = getItemView(itemView,
					R.id.tweetCommentCount);
			listItemView.commentButton = getItemView(itemView,
					R.id.tweetCommentButton);

			itemView.setTag(listItemView);

			if (isInit) {
				if (image != null && !StringUtils.isEmpty(image)) {
					AppDebug.MethodLog(getClass(), "pos-->" + position
							+ "image->" + image);

					ImageOptions io = new ImageOptions();
					imageQuery.id(listItemView.image).image(image, io);

				} else {
					listItemView.image.setVisibility(View.GONE);
				}
			}

		} else {
			listItemView = (ListItemView) itemView.getTag();
			AppDebug.MethodLog(getClass(), "reuse-->" + position + "view"
					+ itemView.getVisibility());

		}
		AppDebug.MethodLog(getClass(), "noinit-->" + position + "view"
				+ itemView.getVisibility());
		if (!isInit) {
			if (image != null && !StringUtils.isEmpty(image)) {
				
				//Bitmap placeholder = imageQuery.getCachedImage(R.drawable.image_ph);

				if (imageQuery.shouldDelay(position, itemView, parent, image)) {
					AppDebug.MethodLog(getClass(), "pos-->delay" + position + "image->"
							+ image);
				//	imageQuery.id(listItemView.image).image(placeholder, 0.75f);
				} else {
					AppDebug.MethodLog(getClass(), "pos-->nodelay" + position + "image->"
							+ image);
					ImageOptions io = new ImageOptions();
					imageQuery.id(listItemView.image).image(image, io);
				}

				

			} else {
				listItemView.image.setVisibility(View.GONE);
			}
		}

		String faceURL = tweet.getFace();
		if (faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL)) {
			imageQuery.id(listItemView.userface).image(R.drawable.widget_dface);
		} else {
			imageQuery.id(listItemView.userface).image(faceURL);
		}
		imageQuery.id(listItemView.userface).clicked(clickListener)
				.tag(tweet.getAuthorId());
		imageQuery.id(listItemView.username).text(tweet.getAuthor());

		imageQuery.id(listItemView.body).text(tweet.getBody());

		imageQuery.id(listItemView.date).text(
				StringUtils.friendly_time(tweet.getPubDate()));
		imageQuery.id(listItemView.commentCount).text(
				String.valueOf(tweet.getCommentCount()));
		imageQuery.id(listItemView.commentButton).clicked(clickListener)
				.tag(position);
		return itemView;
	}

	static class ListItemView { // 自定义控件集合
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
