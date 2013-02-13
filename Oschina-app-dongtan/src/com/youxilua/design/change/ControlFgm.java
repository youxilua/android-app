package com.youxilua.design.change;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.youxilua.dongtan.R;
import com.youxilua.utils.debug.AppDebug;

public class ControlFgm extends Fragment {
	LinearLayout linearLayout;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
//		LinearLayout linearLayout = new LinearLayout(getActivity());
//		LayoutParams linearParams = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
//		
//		TextView tv = new TextView(getActivity());
//		tv.setId(android.R.id.text1);
//		tv.setText("HelloWorld!");
//		
//		linearLayout.addView(tv);
//		
//		linearLayout.setLayoutParams(linearParams);
		
//		return inflater.inflate(R.layout.fgm_control, null, false);
		 linearLayout = new LinearLayout(getActivity());
		LayoutParams linearParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		TextView tv = new TextView(getActivity());
		tv.setId(android.R.id.text1);
		tv.setText("HelloWorld!");
		
		linearLayout.addView(tv);
		linearLayout.setLayoutParams(linearParams);
		
		return linearLayout;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		AppDebug.MethodLog(getClass(), "content-->onResume" );
		
		
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		AppDebug.MethodLog(getClass(), "content-->onPause" );
	}
}
