package com.oschina.view.blogs;

import com.androidquery.AQuery;
import com.oschina.controller.blogs.BlogDetailAction;
import net.oschina.app.holo.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BlogDetailView extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.blog_detail, container, false);
	}
	public BlogDetailAction blogDetailAction;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		blogDetailAction = new BlogDetailAction(getActivity(), new AQuery(getView()));
		blogDetailAction.initView(getArguments().getInt("blog_id"));
		blogDetailAction.initData();
	}
}
