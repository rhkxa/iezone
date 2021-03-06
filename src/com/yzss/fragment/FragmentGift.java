package com.yzss.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.yzss.activity.R;
import com.yzss.adapter.GitftListViewAdapter;
import com.yzss.bean.BnCharge;
import com.yzss.bean.GiftBean;
import com.yzss.custom.View.Generally.FancyButton;
import com.yzss.utils.BaseFragment;
import com.yzss.utils.HttpUtil;
import com.yzss.utils.StringUtils;
import com.yzss.utils.UrlConfig;
import com.yzss.utils.Utils;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentGift extends BaseFragment {

	private View view;
	private FancyButton gift_ok;
	private ListView gift_list;
	private GitftListViewAdapter mAdapter;
	private List<BnCharge> data = new ArrayList<BnCharge>();

	public static FragmentGift newInstance() {
		FragmentGift fragment = new FragmentGift();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_gift, null);
		init();
		return view;
	}

	private void init() {
		// TODO Auto-generated method stub
		gift_list = (ListView) view.findViewById(R.id.gift_list);
		mAdapter = new GitftListViewAdapter(getActivity(), data);
		gift_list.setAdapter(mAdapter);
		gift_ok = (FancyButton) view.findViewById(R.id.gift_ok);
		gift_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String money = mAdapter.getMoney();
				if (StringUtils.isEmpty(money)) {
					Utils.ToastMessage(getActivity(), "请选择充值项");
				} else {
					Utils.toPay(getActivity(),"", "充值", money);
				}
			}
		});
		loadData();
	}

	private void loadData() {
		HttpUtil.get(UrlConfig.getCharge(), new jsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject arg0) {
				// TODO Auto-generated method stub
				GiftBean giftBean = JSON.parseObject(Utils.getResult(arg0),
						GiftBean.class);
				data.clear();
				data.addAll(giftBean.getDatas());
				mAdapter.notifyDataSetChanged();
				super.onSuccess(arg0);
			}
		});
	}
}
