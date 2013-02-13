package com.youxilua.oschina.actionbar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.youxilua.dongtan.R;

public class ActionBarCompat extends Fragment {

	private static final String[] m = { "最新动弹", "最热动弹" };
	private ArrayAdapter<String> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.actbar_normal, null, false);
	}

	public void showSpinner() {
		getView().findViewById(R.id.actList).setVisibility(View.VISIBLE);
	}

	public void hideSpinner() {
		getView().findViewById(R.id.actList).setVisibility(View.GONE);
	}

	public void showProgress() {
		getView().findViewById(R.id.actProgress).setVisibility(View.VISIBLE);
	}

	public void hideProgress() {
		getView().findViewById(R.id.actProgress).setVisibility(View.GONE);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Spinner sp = (Spinner) getView().findViewById(R.id.actList);
		adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.item_spinner_title, android.R.id.text1, m);
		// 设置下拉列表的风格
		// adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

		// Ar
		sp.setAdapter(adapter);
		// sp.setOnItemSelectedListener(new OnItemSelectedListener() {
		//
		// @Override
		// public void onItemSelected(AdapterView<?> arg0, View arg1,
		// int arg2, long arg3) {
		// // TODO Auto-generated method stub
		//
		// Toast.makeText(getActivity(), "pos-->" + arg3,
		// Toast.LENGTH_SHORT).show();
		// }
		//
		// @Override
		// public void onNothingSelected(AdapterView<?> arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

	}

	public Spinner getSpinner() {
		return (Spinner) getView().findViewById(R.id.actList);
	}
}
