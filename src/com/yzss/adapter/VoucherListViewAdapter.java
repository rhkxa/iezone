package com.yzss.adapter;

import java.util.List;

import com.yzss.activity.R;
import com.yzss.adapter.ActiveGoodAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class VoucherListViewAdapter extends BaseAdapter {

	private Context context;
	private List<String> dataList;

	public VoucherListViewAdapter(Context context, List<String> dataList) {
		this.context = context;
		this.dataList = dataList;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		// return dataList == null ? 0 : dataList.size();
		return 20;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHolder vh;
		if (arg1 == null) {
			vh = new ViewHolder();
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.item_voucher_list, null);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		return arg1;
	}

	class ViewHolder {

	}
}
