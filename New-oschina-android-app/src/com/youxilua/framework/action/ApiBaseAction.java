package com.youxilua.framework.action;

import android.content.Context;

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

	
}
