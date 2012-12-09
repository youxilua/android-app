package com.youxilua.framework.action;

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.androidquery.AQuery;
import com.youxilua.framework.ApiBase;

/**
 * 处理逻辑的管理
 * @author youxiachai
 *
 */
public abstract class ApiBaseAction extends ApiBase{
	protected AQuery mActionQuery;
	
	public ApiBaseAction(Context ctx, AQuery aq) {
		super(ctx);
		this.mActionQuery = aq;
	}
	
	public View findViewById(int id){
		return mActionQuery.id(id).getView();
	}
	
	public LayoutInflater getLayoutInflater(){
		return getActivity().getLayoutInflater();
	}
	
	public Application getApplication(){
		return getActivity().getApplication();
	}

	
}
