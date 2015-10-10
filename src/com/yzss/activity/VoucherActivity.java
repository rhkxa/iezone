package com.yzss.activity;

import org.json.JSONObject;

import android.os.Bundle;

import com.yzss.fragment.HeaderFragment;
import com.yzss.utils.BaseActivity;
import com.yzss.utils.HttpUtil;
import com.yzss.utils.PreferenceUtils;
import com.yzss.utils.UrlConfig;

/**
 * 代金券
 * 
 * @author yourname
 * 
 */
public class VoucherActivity extends BaseActivity {
	private HeaderFragment headerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voucher);
		init();
		loadData();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("我的代金券");
	}

	private void loadData() {
		String uid = PreferenceUtils.getInstance(VoucherActivity.this)
				.getStringValue("uid");
		HttpUtil.get(UrlConfig.getCoupons(uid), new jsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject arg0) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0);
			}
		});

	}
}
