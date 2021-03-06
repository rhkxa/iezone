package com.yzss.dialog;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yzss.activity.R;
import com.yzss.utils.HttpUtil;
import com.yzss.utils.StringUtils;
import com.yzss.utils.UrlConfig;
import com.yzss.utils.Utils;

public class AlterPassWordDialog extends Dialog {
	private EditText dialog_old_password;
	private EditText dialog_new_password;
	private EditText dialog_again_password;
	private Button dialog_alter_password_ok;
	private LinearLayout dialog_alter_password_call;
	private Button dialog_alter_password_phone_number;
	private Context context;
	

	public AlterPassWordDialog(Context context) {
		super(context, R.style.Dialog);
		// TODO Auto-generated constructor stub
		this.context = context;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_alter_password);
		init();
	}

	private void init() {
		// TODO Auto-generated method stub
		dialog_old_password = (EditText) findViewById(R.id.dialog_old_password);
		dialog_new_password = (EditText) findViewById(R.id.dialog_new_password);
		dialog_again_password = (EditText) findViewById(R.id.dialog_again_password);
		dialog_alter_password_ok = (Button) findViewById(R.id.dialog_alter_password_ok);
		dialog_alter_password_call = (LinearLayout) findViewById(R.id.dialog_alter_password_call);
		dialog_alter_password_phone_number = (Button) findViewById(R.id.dialog_alter_password_phone_number);
		dialog_alter_password_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (!StringUtils.isEmpty(dialog_old_password.getText()
						.toString())) {
					if (!StringUtils.isEmpty(dialog_new_password.getText()
							.toString())) {
						if (!StringUtils.isEmpty(dialog_again_password
								.getText().toString())) {

							if ((dialog_again_password.getText().toString())
									.equals(dialog_again_password.getText()
											.toString())) {
								getAlterPW();
							} else {
								Utils.ToastMessage(context, "两次密码输入不一致");
							}
						} else {
							Utils.ToastMessage(context, "再次输入密码不能为空");
						}
					} else {
						Utils.ToastMessage(context, "输入新密码不能为空");
					}
				} else {
					Utils.ToastMessage(context, "输入旧密码不能为空");
				}
			}

		});

	}

	private void getAlterPW() {
		if (Utils.isNetWorkConnected(context)) {
			String url = UrlConfig.getNewPassword( dialog_new_password
					.getText().toString());
			HttpUtil.get(url, new JsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject arg0) {
					// TODO Auto-generated method stub
					
//					if (base.getStatusCode().equals("200")) {
//						Utils.ToastMessage(context, "修改成功");
//						PreferenceUtils.getInstance(context).setUserPW(
//								dialog_new_password.getText().toString());
//						dismiss();
//					}
//
//					else {
//						Utils.ToastMessage(context, "修改失败");
//					}
					super.onSuccess(arg0);
				}
			});
		} else {
			Utils.ToastMessage(
					context,
					context.getResources().getString(
							R.string.network_not_connected));
		}

	}

}
