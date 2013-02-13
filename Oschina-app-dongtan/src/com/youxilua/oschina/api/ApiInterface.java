package com.youxilua.oschina.api;

import java.io.InputStream;

import net.oschina.app.bean.URLs;
import android.os.Bundle;

import com.androidquery.AQuery;
import com.youxilua.utils.net.NetClient;

public final class ApiInterface {
	
	/**
	 * @param queryClient
	 * @param uid
	 * @param pageIndex
	 * @param pageSize
	 */
	public static void getTweetList(NetClient<InputStream> queryClient,int uid, String pageIndex, String pageSize){
		Bundle reqeust = new Bundle();
		reqeust.putString(NetClient.NETQUERYURL, URLs.TWEET_LIST);
		reqeust.putString("uid", String.valueOf(uid));
		reqeust.putString("pageIndex", pageIndex);
		reqeust.putString("pageSize", pageSize);
		queryClient.get(reqeust, null);
	}
}
