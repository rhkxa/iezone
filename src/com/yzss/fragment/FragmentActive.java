package com.yzss.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.yzss.activity.R;
import com.yzss.adapter.ActiveGridViewAdapter;
import com.yzss.utils.BaseFragment;

public class FragmentActive extends BaseFragment {
	private View view;

	private ActiveGridViewAdapter mAdapter;
	private ImageButton main_head_search;

	public static FragmentActive newInstance() {
		FragmentActive fragment = new FragmentActive();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_active, null);
		return view;
	}

}
