package com.yzss.fragment;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.yzss.activity.R;
import com.yzss.adapter.OrderGroupListViewAdapter;
import com.yzss.adapter.OrderGroupListViewAdapter.OnItemClickListener;
import com.yzss.bean.BnOrder;
import com.yzss.bean.OrderBean;
import com.yzss.utils.BaseFragment;
import com.yzss.utils.HttpUtil;
import com.yzss.utils.PreferenceUtils;
import com.yzss.utils.UrlConfig;
import com.yzss.utils.Utils;

/**
 * 订单的fragment
 * 
 * @author rhk
 * 
 */
public class FragmentOrder extends BaseFragment {

	private View view;
	private PullToRefreshListView pullToRefreshListView;
	private ListView order_list;
	private OrderGroupListViewAdapter mAdapter;
	private List<BnOrder> data = new ArrayList<BnOrder>();
	private String uid;

	public static FragmentOrder newInstance(String tag) {
		FragmentOrder fragment = new FragmentOrder();
		fragment.tag = tag;
		return fragment;
	}

	/* 用来标记订单的状态待收货与往期订单 */
	private String tag;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_order, null);
		init();
		return view;
	}

	private void init() {
		// TODO Auto-generated method stub
		uid = PreferenceUtils.getInstance(getActivity()).getStringValue("uid");
		pullToRefreshListView = (PullToRefreshListView) view
				.findViewById(R.id.order_list);
		order_list = pullToRefreshListView.getRefreshableView();
		mAdapter = new OrderGroupListViewAdapter(getActivity(), data);
		order_list.setAdapter(mAdapter);
		mAdapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onReceive(int position) {
				// TODO Auto-generated method stub
				operation(position, UrlConfig.getOrderReceive(uid,
						data.get(position).getSn()));
			}

			@Override
			public void onDelete(int position) {
				// TODO Auto-generated method stub
				operation(position, UrlConfig.getOrderDelete(uid,
						data.get(position).getSn()));
			}
		});
		loadData();
	}

	private void loadData() {
		// （1 待收货 2往期订单 3 完成 4已取消）
		HttpUtil.get(UrlConfig.getOrderList(uid, tag),
				new jsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject arg0) {
						// TODO Auto-generated method stub
						OrderBean orderBean = JSON.parseObject(
								Utils.getResult(arg0), OrderBean.class);
						data.clear();
						data.addAll(orderBean.getDatas());
						mAdapter.notifyDataSetChanged();
						super.onSuccess(arg0);
					}
				});
	}

	private void operation(final int position, String url) {
		showProgressDialog(getActivity(), "");
		HttpUtil.get(url, new jsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject arg0) {
				// TODO Auto-generated method stub
				if (Utils.requestOk(arg0)) {
					Utils.ToastMessage(getActivity(), "操作成功");
					data.remove(position);
					mAdapter.notifyDataSetChanged();
				}
			}
		});
	}

}
