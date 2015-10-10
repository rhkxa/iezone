package com.yzss.activity;

import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.yzss.fragment.FragmentOrder;
import com.yzss.fragment.HeaderFragment;
import com.yzss.utils.BaseActivity;

public class OrderActivity extends BaseActivity {

	private HeaderFragment headerFragment;
	private RadioGroup item_group;
	private String status = "1";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);
		init();

	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("我的订单");
		item_group = (RadioGroup) findViewById(R.id.item_group);
		item_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch (arg1) {
				case R.id.item_radio1:
					status = "1";
					break;
				case R.id.item_radio2:
					status = "2";
					break;
				}
				setContent();
			}
		});
		setContent();
	}

	private void setContent() {
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.order_list_frame, FragmentOrder.newInstance(status))
				.commit();
	}

}
