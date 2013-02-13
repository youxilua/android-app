package com.youxilua.oschina.v.tweet;

import java.util.List;

import com.androidquery.AQuery;
import com.youxilua.dongtan.R;

import net.oschina.app.bean.Comment;
import net.oschina.app.common.StringUtils;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class LvCommentAdpater extends BaseAdapter {
	private List<Comment> commentLv;
	private LayoutInflater mInflater;
	private AQuery itemQuery;
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentLv.size();
	}
	
	public LvCommentAdpater(Activity act, List<Comment> comentLv){
		this.commentLv = comentLv;
		this.mInflater = act.getLayoutInflater();
		this.itemQuery = new AQuery(act);
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return commentLv.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ItemView itemHolder;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.item_lv_comment, null);
			itemHolder = new ItemView();
			itemHolder.userFace = AdapterUtils.getItemView(convertView, R.id.face);
			itemHolder.userName = AdapterUtils.getItemView(convertView, R.id.commentName);
			itemHolder.date = AdapterUtils.getItemView(convertView, R.id.commentBefore);
			itemHolder.body = AdapterUtils.getItemView(convertView, R.id.commentBody);
			convertView.setTag(itemHolder);
		}else{
			itemHolder = (ItemView) convertView.getTag();
		}
		
		Comment comment = commentLv.get(position);
		String faceURL = comment.getFace();
		if (faceURL.endsWith("portrait.gif") || StringUtils.isEmpty(faceURL)) {
			itemQuery.id(itemHolder.userFace).image(R.drawable.widget_dface);
		} else {
			itemQuery.id(itemHolder.userFace).image(faceURL);
		}
		itemQuery.id(itemHolder.userName).text(comment.getAuthor());
		itemQuery.id(itemHolder.body).text(comment.getContent());
		itemQuery.id(itemHolder.date).text(StringUtils.friendly_time(comment.getPubDate()));
	
		
		return convertView;
	}
	
	static class ItemView{
		public View userFace;
		public View userName;
		public View date;
		public View body;
	}

}
