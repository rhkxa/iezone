package com.yzss.activity;

import android.os.Bundle;

import com.yzss.fragment.HeaderFragment;
import com.yzss.utils.BaseActivity;

/*
 * 红包的界面
 */
public class BonusActivity extends BaseActivity {

	private HeaderFragment headerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bonus);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("分享红包");
	}

}
