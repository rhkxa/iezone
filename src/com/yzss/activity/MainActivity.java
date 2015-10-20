package com.yzss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.yzss.custom.View.Generally.BadgeView;
import com.yzss.custom.View.Generally.CircleImageView;
import com.yzss.fragment.FragmentGift;
import com.yzss.fragment.FragmentHome;
import com.yzss.fragment.FragmentMenu;
import com.yzss.fragment.FragmentMyself;
import com.yzss.push.PushUtils;
import com.yzss.utils.BaseActivity;
import com.yzss.utils.PreferenceUtils;
import com.yzss.utils.StringUtils;
import com.yzss.utils.Utils;
import com.yzss.utils.YZSSApplication;

/**
 * 主界面
 * 
 * @author rhk
 * 
 */
public class MainActivity extends BaseActivity {
	private long mExitTime = 0;
	private RadioGroup group;
	private FragmentManager mFragmentMan;
	private Fragment mContent;
	private CircleImageView main_shopcar;
	private BadgeView badge;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		initPush();
	}

	private void initPush() {
		// if (PreferenceUtils.getInstance(context).getBooleanValue("push")) {
		PushManager.startWork(getApplicationContext(),
				PushConstants.LOGIN_TYPE_API_KEY,
				PushUtils.getMetaValue(MainActivity.this, "api_key"));
		// } else {
		// PushManager.stopWork(context);
		// }
	}

	private void init() {
		// TODO Auto-generated method stub
		main_shopcar = (CircleImageView) findViewById(R.id.main_shopcar);
		// *** default badge ***
		badge = new BadgeView(this, main_shopcar);

		main_shopcar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (Utils.isLogin(MainActivity.this)) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, ShoppingActivity.class);
					startActivity(intent);
				} else {
					Utils.toLogin(MainActivity.this);
				}

			}
		});

		final Fragment home = FragmentHome.newInstance();
		final Fragment menu = FragmentMenu.newInstance();
		final Fragment my = FragmentMyself.newInstance();
		final Fragment gift = FragmentGift.newInstance();
		mFragmentMan = getSupportFragmentManager();
		mContent = home;
		mFragmentMan.beginTransaction().add(R.id.content, mContent).commit();
		group = (RadioGroup) findViewById(R.id.main_linearlayout_footer);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub

				switch (arg1) {
				case R.id.main_home:
					switchContent(mContent, home);
					break;
				case R.id.main_gift:
					switchContent(mContent, gift);
					break;
				case R.id.main_menu:
					switchContent(mContent, menu);
					break;
				case R.id.main_myself:
					switchContent(mContent, my);
					// if (Utils.isLogin(MainActivity.this)) {
					// switchContent(mContent, my);
					// } else {
					// Utils.toLogin(MainActivity.this);
					// }
					break;
				}
			}
		});

	}

	public void switchContent(Fragment from, Fragment to) {
		if (mContent != to) {
			mContent = to;
			FragmentTransaction transaction = mFragmentMan.beginTransaction();
			// .setCustomAnimations(android.R.anim.fade_in,
			// android.R.anim.fade_out);
			if (!to.isAdded()) { // 先判断是否被add过
				transaction.hide(from).add(R.id.content, to).commit(); // 隐藏当前的fragment，add下一个到Activity中
			} else {
				transaction.hide(from).show(to).commit(); // 隐藏当前的fragment，显示下一个
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		String count = PreferenceUtils.getInstance(MainActivity.this)
				.getStringValue("count");
		if (!StringUtils.isEmpty(count)) {
			badge.setText(count);
			badge.show();
		}

		super.onResume();
	}

	// 双击退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Utils.showSuperCardToast(MainActivity.this, getResources()
						.getString(R.string.exit));
				mExitTime = System.currentTimeMillis();
			} else {
				YZSSApplication.getInstance().exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
