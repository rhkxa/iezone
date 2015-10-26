package com.yzss.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yzss.activity.R;
import com.yzss.utils.StringUtils;
import com.yzss.utils.Utils;

public class TextDialog extends Dialog {

	private Button btn_ok, btn_cancel;
	private EditText comment;
	private Context context;
	private OnClickListener mOnClickListener;
	private TextView title,result;
	private String tag;
	int money = 0;

	public TextDialog(Context context, String tag) {
		super(context, R.style.Dialog);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.tag = tag;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_text);
		init();
	}

	private void init() {
		title = (TextView) findViewById(R.id.title);
		title.setText(tag);
		result=(TextView) findViewById(R.id.result);
		btn_ok = (Button) findViewById(R.id.btn_ok);
		comment = (EditText) findViewById(R.id.price_et);
		if (tag.equals("提现")) {
			comment.setHint("请输入您要提现的金额，以100元为单位");
		} else {
			comment.setHint("请输入您要转入的金额，以元为单位");
		}
		btn_ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(comment.getText().toString())) {
					Utils.ToastMessage(context, "请输入");
					return;
				}				
				mOnClickListener.onItemClick(money);
				dismiss();
			}
		});
		comment.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if (StringUtils.isEmpty(comment.getText().toString())) {
					return;
				}				
				if (tag.equals("提现")) {
					money = Integer.valueOf(comment.getText().toString()) * 100;
				} else {
					money = Integer.valueOf(comment.getText().toString());
				}
				result.setText("您本次的金额为"+money+"元");
			}
		});
	}

	public void setOnClickListener(OnClickListener l) {
		mOnClickListener = l;
	}

	public interface OnClickListener {
		public void onItemClick(int money);
	}
}
