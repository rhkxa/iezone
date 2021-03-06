package com.yzss.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yzss.bean.BnGallery;
import com.yzss.fragment.GoodImageFragment;


public class GoodFragmentAdapter extends FragmentPagerAdapter {

	private List<BnGallery> data;

	public GoodFragmentAdapter(FragmentManager fm, List<BnGallery> data) {

		super(fm);
		this.data = data;
	}

	@Override
	public Fragment getItem(int position) {
		return GoodImageFragment.newInstance(data.get(position));
	}

	@Override
	public int getCount() {
		// return dataList == null ? 0 : dataList.size();
		return data.size();
	}

}
