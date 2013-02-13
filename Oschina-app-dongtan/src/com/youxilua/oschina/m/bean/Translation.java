package com.youxilua.oschina.m.bean;

import java.util.List;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;
import com.youxilua.dongtan.R;

public class Translation {
//	final AQuery aq = new AQuery(this);
//	
//	aq.ajax("http://www.oschina.net/translate/rss?type=2", XmlDom.class, new AjaxCallback<XmlDom>(){
//		@Override
//		public void callback(String url, XmlDom xml, AjaxStatus status) {
//			super.callback(url, xml, status);
//			XmlDom xd = xml.child("channel");
//			
//			XmlDom title = xd.child("title");
			
//			List<XmlDom> item = xd.children("item");
//			for (XmlDom xmlDom : item) {
//				AppDebug.MethodLog(getClass(), xmlDom.child("title").text());
//			}
//			XmlDom xd1 = item.get(0);
//			
//			Translation tsl = new Translation();
//			tsl.title = xd1.text(Translation.NODE_TITLE);
//			tsl.link = xd1.text(Translation.NODE_LINK);
//			tsl.description = xd1.text(Translation.NODE_DESCRIPTION);
//			tsl.pubDate = xd1.text(Translation.NODE_PUBDATE);
//			tsl.guid = xd1.text(Translation.NODE_GUID);
//			
//			aq.id(R.id.textView1).text(tsl.title+"\n"+tsl.link+"\n"+tsl.description+"\n"+tsl.pubDate+"\n"+tsl.guid);
//			
//			
//		}
//	});
	public final static String NODE_TITLE = "title";
	public final static String NODE_LINK = "link";
	public final static String NODE_CATEGORY = "category";
	public final static String NODE_DESCRIPTION = "description";
	public final static String NODE_PUBDATE = "pubDate";
	public final static String NODE_GUID = "guid";
	
	
	public String title;
	public String link;
	public String category;
	public String description;
	public String pubDate;
	public String guid;
	
	
}
