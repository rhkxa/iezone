package com.yzss.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.yzss.adapter.RankListViewAdapter;
import com.yzss.bean.BnRank;
import com.yzss.bean.RankBean;
import com.yzss.fragment.HeaderFragment;
import com.yzss.utils.BaseActivity;
import com.yzss.utils.HttpUtil;
import com.yzss.utils.UrlConfig;
import com.yzss.utils.Utils;

/**
 * 加盟商排名
 * 
 * @author yourname
 * 
 */
public class FranchiseeActivity extends BaseActivity {

	private ListView franchisee_list;
	private RankListViewAdapter mAdapter;
	private List<BnRank> data = new ArrayList<BnRank>();
	private HeaderFragment headerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_franchisee);
		init();
		loadData();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("加盟商排名");
		franchisee_list = (ListView) findViewById(R.id.franchisee_list);
		mAdapter = new RankListViewAdapter(FranchiseeActivity.this, data);
		franchisee_list.setAdapter(mAdapter);

	}

	private void loadData() {

		HttpUtil.get(UrlConfig.getAgentRank(), new jsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject arg0) {
				// TODO Auto-generated method stub
				RankBean bill = JSON.parseObject(Utils.getResult(arg0),
						RankBean.class);
				setValue(bill);
				super.onSuccess(arg0);
			}

		});
	}

	private void setValue(RankBean bill) {
		// TODO Auto-generated method stub
		data.addAll(bill.getDatas());
		mAdapter.notifyDataSetChanged();
	}
}
