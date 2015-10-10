package com.yzss.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.yzss.activity.R;
import com.yzss.bean.BnUser;

import com.yzss.utils.BaseActivity;
import com.yzss.utils.HttpUtil;
import com.yzss.utils.PreferenceUtils;
import com.yzss.utils.StringUtils;
import com.yzss.utils.UrlConfig;
import com.yzss.utils.Utils;

/**
 * 登录界面
 * 
 * @author rhk
 * 
 */
public class LoginActivity extends BaseActivity {
	private long mExitTime = 0;
	private Button login;
	private TextView phonenumber;
	private EditText userName;
	private EditText passWord;
	private CheckBox autoLoginBox;
	private CheckBox savePwdBox;
	private RelativeLayout call;

	private PreferenceUtils mPreferences;
	// /private TextView login_serve;
	private TextView login_to_register;
	private ImageButton backBtn;
	List<BnUser> userList = new ArrayList<BnUser>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mPreferences = PreferenceUtils.getInstance(LoginActivity.this);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		if (!StringUtils.isEmpty(Utils.getAccount(LoginActivity.this))) {
			userList.addAll(JSON.parseArray(
					Utils.getAccount(LoginActivity.this), BnUser.class));
		}
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		userName = (EditText) findViewById(R.id.username);
		passWord = (EditText) findViewById(R.id.password);
		autoLoginBox = (CheckBox) findViewById(R.id.autoLoginBox);
		savePwdBox = (CheckBox) findViewById(R.id.savePwdBox);
		login = (Button) findViewById(R.id.login);
		phonenumber = (TextView) findViewById(R.id.phonenumber);
		call = (RelativeLayout) findViewById(R.id.call_phone);
		// login_serve = (TextView) findViewById(R.id.login_serve);
		login_to_register = (TextView) findViewById(R.id.login_to_register);
		// autoLoginBox.setChecked(mPreferences.getAutoLogin());
		//
		// savePwdBox.setChecked(mPreferences.getSavePassWord());
		//
		// if (mPreferences.getSavePassWord()) {
		// if (!StringUtils.isEmpty(mPreferences.getUserName())) {
		// userName.setText(mPreferences.getUserName());
		// }
		// if (!StringUtils.isEmpty(mPreferences.getUserPW())) {
		// passWord.setText(mPreferences.getUserPW());
		// }
		// }
		// if (!StringUtils.isEmpty(mPreferences.getCall())) {
		// phonenumber.setText(mPreferences.getCall());
		// }
		autoLoginBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				// mPreferences.setAutoLogin(arg1);
			}
		});

		savePwdBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				// mPreferences.setSavePassWord(arg1);
			}
		});

		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(userName.getText().toString())) {
					Utils.ToastMessage(LoginActivity.this, getResources()
							.getString(R.string.msg_login_name_null));
					return;
				}
				if (StringUtils.isEmpty(passWord.getText().toString())) {
					Utils.ToastMessage(LoginActivity.this, getResources()
							.getString(R.string.msg_login_pwd_null));
					return;
				}

				if (isExit()) {
					Utils.ToastMessage(LoginActivity.this, "此用户已在帐户列表中，请切换用户");
					return;

				}
				getLogin();

			}
		});
		// if (mPreferences.getAutoLogin()) {
		// if (mPreferences.getUserState() == 0) {
		// login.performClick();
		// }
		// }

		call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// 打电话

				Utils.callPhone(LoginActivity.this, phonenumber.getText()
						.toString());
			}
		});
		// login_serve.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		// Intent intent = new Intent(LoginActiivty.this,
		// AgreementActivity.class);
		// startActivity(intent);
		//
		// }
		// });
		login_to_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this,
						RegisterActivity.class);
				startActivity(intent);
			}
		});

	}

	// 登录
	private void getLogin() {
		if (Utils.isNetWorkConnected(LoginActivity.this)) {
			showProgressDialog(LoginActivity.this, "登录中，请稍等");
			String url = UrlConfig.getLogin(userName.getText().toString(),
					passWord.getText().toString());
			HttpUtil.get(url, new jsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject arg0) {
					// TODO Auto-generated method stub
					if (Utils.requestOk(arg0)) {
						Utils.ToastMessage(LoginActivity.this, "登录成功");
						BnUser data = JSON.parseObject(Utils.getResult(arg0),
								BnUser.class);
						data.setPass(passWord.getText().toString());
						Utils.setUid(LoginActivity.this, data.getUid());
						Utils.setUser(LoginActivity.this, Utils.getResult(arg0));
						userList.add(data);
						Utils.setAccount(LoginActivity.this,
								JSON.toJSONString(userList));
						finish();
					} else {
						Utils.ToastMessage(LoginActivity.this,
								Utils.getKey(arg0, "msg"));
					}

					super.onSuccess(arg0);
				}
			});
		} else {
			Utils.showSuperCardToast(LoginActivity.this, getResources()
					.getString(R.string.network_not_connected));
		}
	}

	private boolean isExit() {
		int size = userList.size();
		for (int i = 0; i < size; i++) {
			if (userList.get(i).getName().equals(userName.getText().toString())) {
				return true;
			}
		}
		return false;
	}
}
