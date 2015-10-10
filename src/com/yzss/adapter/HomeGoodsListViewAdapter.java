package com.yzss.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzss.activity.R;
import com.yzss.adapter.HomeActiveListViewAdapter.ViewHolder;
import com.yzss.bean.BnHGoods;
import com.yzss.bean.BnHActive;
import com.yzss.utils.LogUtils;
import com.yzss.utils.Utils;

public class HomeGoodsListViewAdapter extends BaseAdapter {
	private Context context;
	private List<List<BnHGoods>> data;

	public HomeGoodsListViewAdapter(Context context, List<List<BnHGoods>> data) {
		this.context = context;
		this.data = data;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		LogUtils.d("adapter", "homeactive" + data.size());
		return data.size();
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
		final ViewHolder vh;
		if (arg1 == null) {
			vh = new ViewHolder();
			arg1 = LayoutInflater.from(context).inflate(
					R.layout.item_goods_image, null);
			vh.item_goods_image1 = (ImageView) arg1
					.findViewById(R.id.item_goods_image1);
			vh.item_goods_image2 = (ImageView) arg1
					.findViewById(R.id.item_goods_image2);
			vh.item_goods_image3 = (ImageView) arg1
					.findViewById(R.id.item_goods_image3);
			vh.item_goods_image4 = (ImageView) arg1
					.findViewById(R.id.item_goods_image4);
			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}

		ImageLoader.getInstance().displayImage(data.get(arg0).get(0).getImg_url(),
				vh.item_goods_image2);
		ImageLoader.getInstance().displayImage(data.get(arg0).get(1).getImg_url(),
				vh.item_goods_image3);
		ImageLoader.getInstance().displayImage(data.get(arg0).get(2).getImg_url(),
				vh.item_goods_image4);
		ImageLoader.getInstance().displayImage(data.get(arg0).get(3).getImg_url(),
				vh.item_goods_image1);
		vh.item_goods_image1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Utils.toGoodDetail(context, data.get(arg0).get(3).getGoods_id());
			}
		});
		vh.item_goods_image2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Utils.toGoodDetail(context, data.get(arg0).get(0).getGoods_id());
			}
		});
		vh.item_goods_image3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Utils.toGoodDetail(context, data.get(arg0).get(1).getGoods_id());
			}
		});
		vh.item_goods_image4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Utils.toGoodDetail(context, data.get(arg0).get(2).getGoods_id());
			}
		});
		return arg1;
	}

	class ViewHolder {
		ImageView item_goods_image1;
		ImageView item_goods_image2;
		ImageView item_goods_image3;
		ImageView item_goods_image4;
	}


}