package com.yzss.activity;

import android.os.Bundle;

import com.yzss.fragment.HeaderFragment;
import com.yzss.utils.BaseActivity;

/**
 * 给我评分
 * @author yourname
 *
 */
public class ScoreActivity extends BaseActivity {

	private HeaderFragment headerFragment;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_score);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("给我评分");
	}
}
