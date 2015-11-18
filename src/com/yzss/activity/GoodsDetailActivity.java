package com.yzss.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzss.activity.R;
import com.yzss.adapter.GoodFragmentAdapter;
import com.yzss.bean.BnGallery;
import com.yzss.bean.BnProduct;
import com.yzss.bean.PDetailsBean;
import com.yzss.bean.Products;
import com.yzss.custom.View.Generally.FancyButton;
import com.yzss.custom.View.Generally.SingleSelectCheckBoxs;
import com.yzss.custom.View.Generally.SingleSelectCheckBoxs.OnSelectListener;
import com.yzss.custom.View.Indicator.CirclePageIndicator;
import com.yzss.utils.BaseActivity;
import com.yzss.utils.HttpUtil;
import com.yzss.utils.PreferenceUtils;
import com.yzss.utils.StringUtils;
import com.yzss.utils.UrlConfig;
import com.yzss.utils.Utils;

/**
 * 商品详情的界面 加入时间倒计时
 * 
 * @author rhk
 * 
 */
public class GoodsDetailActivity extends BaseActivity {
	private PDetailsBean data;

	private Map<Integer, String> mSpecData;
	private SingleSelectCheckBoxs goods_details_spec;

	private ImageView item_comment_image;
	private TextView item_comment_content;
	private TextView item_comment_name;
	private TextView item_comment_time;
	private TextView goods_detail_spec_name;
	private TextView goods_details_income;
	private TextView goods_details_rate_sales, goods_details_rate_comment;

	private ImageView goods_detail_toshare;
	private ImageView goods_detail_tocollect;
	private ImageView goods_detail_toshop;
	private ImageButton backBtn;

	private ImageButton currency_add;
	private ImageButton currency_subtract;
	private TextView show_text;

	private TextView goods_details_look_more;
	private FancyButton goods_details_join_shopping, goods_details_my_shopping;
	private FancyButton goods_details_comment;
	private TextView goods_details_comment_totals;
	private TextView goods_details_old_price;
	private TextView goods_details_new_price;

	private TextView goods_details_title;
	private ScrollView goods_details_scrollview;
	private WebView goods_detail_webview;
	private int index = 0;
	private String goods_id = null;
	private LinearLayout comment_layout;
	private ViewPager mPager;
	private CirclePageIndicator mIndicator;
	private GoodFragmentAdapter fAdapter;
	private List<BnGallery> gallery = new ArrayList<BnGallery>();
	private List<Products> products = new ArrayList<Products>();
	private LinearLayout incomeLayout;
	private FrameLayout pager_layout;
	private String product_id;
	// 新增--------------------------------------------------------------------
	private LinearLayout ll_time;
	private TextView daysTv;
	private TextView hoursTv;
	private TextView minutesTv;
	private TextView secondsTv;

	protected String currentThreadName = "";
	private boolean isRun = true;
	private Handler timeHandler_qws = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 2) {
				Log.d("debug", "---handleMessage方法所在的线程："
						+ Thread.currentThread().getName());
				if (StringUtils.isEmpty(currentThreadName)) {
					currentThreadName = msg.obj.toString();
				} else {
					if (currentThreadName.equals(msg.obj.toString())) {
						computeTime();
						daysTv.setText(mDay + "");
						hoursTv.setText(mHour + "");
						minutesTv.setText(mMin + "");
						secondsTv.setText(mSecond + "");
						if (mDay == 0 && mHour == 0 && mMin == 0
								&& mSecond == 0) {
							// 结束Timer计时器
							daysTv.setText("--");
							hoursTv.setText("--");
							minutesTv.setText("--");
							secondsTv.setText("--");
							isRun = false;
							ll_time.setVisibility(View.GONE);
						}
					} else {
						Log.d("debug", "---其他线程："
								+ Thread.currentThread().getName());
					}
				}
			}
		}
	};

	private Thread thread;
	private static long mDay = 00;
	private static long mHour = 00;
	private static long mMin = 00;
	private static long mSecond = 00;// 天 ,小时,分钟,秒

	// ----------------------------------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goods_details);
		init();
		loadData();
	}

	private void init() {
		goods_id = getIntent().getStringExtra("goods_id");
		String is_agent = PreferenceUtils.getInstance(GoodsDetailActivity.this)
				.getStringValue("is_agent");
		incomeLayout = (LinearLayout) findViewById(R.id.income_layout);
		if (!is_agent.equals("1")) {
			incomeLayout.setVisibility(View.GONE);
		}
		ll_time = (LinearLayout) findViewById(R.id.ll_time);
		daysTv = (TextView) findViewById(R.id.days_tv);
		hoursTv = (TextView) findViewById(R.id.hours_tv);
		minutesTv = (TextView) findViewById(R.id.minutes_tv);
		secondsTv = (TextView) findViewById(R.id.seconds_tv);
		pager_layout = (FrameLayout) findViewById(R.id.pager_layout);
		int screenWidth = Utils.getScreenWidth(GoodsDetailActivity.this);
		LayoutParams lp = pager_layout.getLayoutParams();
		lp.width = screenWidth;
		lp.height = screenWidth;
		pager_layout.setLayoutParams(lp);
		// pager_layout.setLayoutParams(new
		// FrameLayout.LayoutParams(screenWidth, screenWidth));
		goods_detail_spec_name = (TextView) findViewById(R.id.goods_detail_spec_name);
		goods_detail_toshare = (ImageView) findViewById(R.id.goods_detail_toshare);
		goods_detail_tocollect = (ImageView) findViewById(R.id.goods_detail_tocollect);
		goods_detail_toshop = (ImageView) findViewById(R.id.goods_detail_toshop);
		backBtn = (ImageButton) findViewById(R.id.backBtn);
		goods_details_rate_sales = (TextView) findViewById(R.id.goods_details_rate_sales);
		goods_details_rate_comment = (TextView) findViewById(R.id.goods_details_rate_comment);
		backBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// if (thread != null) {
				// Thread dummy = thread;
				// thread = null;
				// dummy.interrupt();
				// }
				isRun = false;
				currentThreadName = "";
				finish();
			}
		});
		goods_detail_toshare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Utils.isLogin(GoodsDetailActivity.this)) {

					// Utils.Share(GoodsDetailActivity.this, "宜众食尚", data
					// .getName(), Utils.getUser(GoodsDetailActivity.this)
					// .getShare_url(), ImageLoader.getInstance()
					// .getDiskCache().get(data.getImg_url()).getPath());
					// ImageLoader.getInstance().getMemoryCache()
					// .get(data.getImg_url());

					Utils.Share(GoodsDetailActivity.this, "宜众食尚", data
							.getName(), Utils.getUser(GoodsDetailActivity.this)
							.getShare_url(), data.getImg_url());
				} else {
					Utils.toLogin(GoodsDetailActivity.this);
				}

			}
		});
		goods_detail_toshop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Utils.toShop(GoodsDetailActivity.this);
			}
		});

		goods_detail_tocollect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Utils.joinCollect(GoodsDetailActivity.this, goods_id);
			}
		});

		/** 轮播图 **/

		mPager = (ViewPager) findViewById(R.id.pager);
		fAdapter = new GoodFragmentAdapter(getSupportFragmentManager(), gallery);
		mPager.setAdapter(fAdapter);
		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);
		goods_details_look_more = (TextView) findViewById(R.id.goods_details_look_more);
		goods_details_look_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(GoodsDetailActivity.this,
						CommentActivity.class);
				intent.putExtra("goods_id", goods_id);
				startActivity(intent);
			}
		});
		goods_details_comment = (FancyButton) findViewById(R.id.goods_details_comment);
		comment_layout = (LinearLayout) findViewById(R.id.comment_layout);
		item_comment_name = (TextView) findViewById(R.id.item_comment_name);
		item_comment_time = (TextView) findViewById(R.id.item_comment_time);
		item_comment_content = (TextView) findViewById(R.id.item_comment_content);
		goods_details_income = (TextView) findViewById(R.id.goods_details_income);
		item_comment_image = (ImageView) findViewById(R.id.item_comment_image);
		show_text = (TextView) findViewById(R.id.show_text);
		currency_add = (ImageButton) findViewById(R.id.currency_add);
		currency_subtract = (ImageButton) findViewById(R.id.currency_subtract);
		goods_details_comment_totals = (TextView) findViewById(R.id.goods_details_comment_totals);
		goods_details_join_shopping = (FancyButton) findViewById(R.id.goods_details_join_shopping);
		goods_details_my_shopping = (FancyButton) findViewById(R.id.goods_details_my_shopping);
		goods_details_old_price = (TextView) findViewById(R.id.goods_details_old_price);
		goods_details_new_price = (TextView) findViewById(R.id.goods_details_new_price);
		goods_details_title = (TextView) findViewById(R.id.goods_details_title);
		goods_details_spec = (SingleSelectCheckBoxs) findViewById(R.id.goods_details_spec);
		goods_details_scrollview = (ScrollView) findViewById(R.id.goods_details_scrollview);
		goods_detail_webview = (WebView) findViewById(R.id.goods_detail_webview);
		// goods_detail_webview.getSettings().setJavaScriptEnabled(true);
		goods_detail_webview.getSettings().setLayoutAlgorithm(
				LayoutAlgorithm.NARROW_COLUMNS);

		// goods_detail_webview.getSettings().setBuiltInZoomControls(false);
		// goods_detail_webview.getSettings().setUseWideViewPort(true);
		// goods_detail_webview.getSettings().setLoadWithOverviewMode(true);
		// 会出现放大缩小的按钮
		// goods_detail_webview.setWebViewClient(new WebViewClient() {
		// @Override
		// public boolean shouldOverrideUrlLoading(WebView view, String url) {
		// view.loadUrl(url);
		// return super.shouldOverrideUrlLoading(view, url);
		// }
		// });
		goods_details_spec.setOnSelectListener(new OnSelectListener() {

			@Override
			public void onSelect(int position) {
				if (position >= 0) {
					refresh(data.getProducts().get(position));
				}

			}
		});

		goods_details_my_shopping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!Utils.isLogin(GoodsDetailActivity.this)) {
					Utils.toLogin(GoodsDetailActivity.this);
				} else {
					products.clear();
					Products ps = new Products();
					ps.setProduct_id(product_id);
					ps.setQuantity(Integer.parseInt(show_text.getText()
							.toString()));
					products.add(ps);
					Intent intent = new Intent(GoodsDetailActivity.this,
							FinishOrderActivity.class);
					intent.putExtra("products", JSON.toJSONString(products));
					startActivity(intent);
				}
			}
		});

		goods_details_join_shopping.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				int quantity = Integer.parseInt(show_text.getText().toString());
				Utils.joinShop(GoodsDetailActivity.this, product_id, quantity);
				// Utils.ToastMessage(GoodsDetailActivity.this, "请选择商品数量");

			}
		});
		currency_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				show_text.setText(String.valueOf(Integer.parseInt(show_text
						.getText().toString()) + 1));
			}
		});
		currency_subtract.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (Integer.parseInt(show_text.getText().toString()) > 1) {
					show_text.setText(String.valueOf(Integer.parseInt(show_text
							.getText().toString()) - 1));
				}
			}
		});
		goods_details_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// commentDialog = new CommentDialog(GoodsDetailActivity.this,
				// bean.getProduction().getId());
				// // 显示窗口
				// commentDialog.show();
				if (Utils.isLogin(GoodsDetailActivity.this)) {
					Intent intent = new Intent(GoodsDetailActivity.this,
							SubmitComActivity.class);
					intent.putExtra("goods_img", data.getImg_url());
					intent.putExtra("goods_id", data.getId());
					intent.putExtra("goods_name", data.getName());
					intent.putExtra("goods_price", data.getPrice());
					startActivity(intent);
				} else {
					Utils.toLogin(GoodsDetailActivity.this);
				}

			}
		});

	}

	// 获取网络数据
	private void loadData() {
		if (Utils.isNetWorkConnected(GoodsDetailActivity.this)) {
			showProgressDialog(GoodsDetailActivity.this, "");
			String url = UrlConfig.getGoodsInfo(goods_id);

			HttpUtil.get(url, new jsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject arg0) {
					if (Utils.requestOk(arg0)) {
						data = JSON.parseObject(Utils.getResult(arg0),
								PDetailsBean.class);
						setValue();
					} else {
						Utils.ToastMessage(GoodsDetailActivity.this,
								Utils.getKey(arg0, "msg"));
					}
					super.onSuccess(arg0);
				}

				@Override
				public void onFailure(Throwable error, String message) {

					super.onFailure(error, message);
				}

			});
		} else {
			Utils.showSuperCardToast(GoodsDetailActivity.this, getResources()
					.getString(R.string.network_not_connected));
			finish();
		}
	}

	private void setValue() {
		product_id = data.getProducts().get(0).getId();
		mSpecData = new HashMap<Integer, String>();
		int size = data.getProducts().size();
		for (int i = 0; i < size; i++) {
			if (data.getProducts().get(i).getSpecs().size() > 0) {
				mSpecData.put(i, data.getProducts().get(i).getSpecs().get(0)
						.getSpec_value());
				goods_detail_spec_name.setText(data.getProducts().get(0)
						.getSpecs().get(0).getSpec_name());
			}

		}
		goods_details_spec.setData(mSpecData);

		goods_details_comment_totals.setText("产品评价  ("
				+ data.getComment_count() + ")");

		goods_details_rate_sales.setText(data.getBuy_count() + "组");
		goods_details_rate_comment.setText(data.getComment_count() + "%");
		goods_details_old_price.getPaint()
				.setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

		goods_details_old_price.setText("市场价￥"
				+ data.getProducts().get(0).getMktprice());
		goods_details_income.setText(data.getProducts().get(0)
				.getAgent_income());
		goods_details_new_price.setText("￥"
				+ data.getProducts().get(0).getPrice());
		goods_details_title.setText(data.getName());
		goods_detail_webview.loadData(data.getIntro(),
				"text/html; charset=UTF-8", "UTF-8");
		gallery.clear();
		gallery.addAll(data.getGallery());
		fAdapter.notifyDataSetChanged();
		if (data.getComment_count() > 0) {
			comment_layout.setVisibility(View.GONE);
			// ImageLoader.getInstance().displayImage(
			// data.getCommentList().get(0).getUserimg(),
			// item_comment_image);
			// item_comment_content.setText(data.getCommentList().get(0)
			// .getContent());
			// item_comment_name.setText(data.getCommentList().get(0)
			// .getUsername());
			// item_comment_time.setText(data.getCommentList().get(0)
			// .getInputTime());
		} else {
			comment_layout.setVisibility(View.GONE);
		}

		// 2015年11月9日 18:38:32
		isRun = true;
		currentThreadName = "";
		String remain_time = data.getRemain_time();
		if (StringUtils.isNum(remain_time)) {
			long remain_time_num = Long.parseLong(remain_time) * 1000;
			if (remain_time_num > 0) {
				ll_time.setVisibility(View.VISIBLE);
				formatDuring(remain_time_num - 1 * 1000);
				daysTv.setText(mDay + "");
				hoursTv.setText(mHour + "");
				minutesTv.setText(mMin + "");
				secondsTv.setText(mSecond + "");
				startRun();
			} else {
				ll_time.setVisibility(View.GONE);
			}
		} else {
			ll_time.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		// if (thread != null) {
		// Thread dummy = thread;
		// thread = null;
		// dummy.interrupt();
		// }
		isRun = false;
		currentThreadName = "";
	}

	/**
	 * @param 要转换的毫秒数
	 * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
	 */
	public static void formatDuring(long mss) {
		int DAY_HOUR = 24;
		int HOUR_MIN = 60;
		int MIN_SECOND = 60;
		int SECOND_MSS = 1000;
		long days = mss / (SECOND_MSS * HOUR_MIN * MIN_SECOND * DAY_HOUR);
		long hours = (mss % (SECOND_MSS * HOUR_MIN * MIN_SECOND * DAY_HOUR))
				/ (SECOND_MSS * HOUR_MIN * MIN_SECOND);
		long minutes = (mss % (SECOND_MSS * HOUR_MIN * MIN_SECOND))
				/ (SECOND_MSS * MIN_SECOND);
		long seconds = (mss % (SECOND_MSS * MIN_SECOND)) / SECOND_MSS;
		mDay = days;
		mHour = hours;
		mMin = minutes;
		mSecond = seconds;// 天 ,小时,分钟,秒
		// return days + " days " + hours + " hours " + minutes + " minutes "
		// + seconds + " seconds ";
		System.out.println("===============+" + days + " days " + hours
				+ " hours " + minutes + " minutes " + seconds + " seconds ");
	}

	/**
	 * 开启倒计时
	 */
	private void startRun() {
		thread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (isRun) {
					try {
						Log.d("debug", "---startRun方法所在的线程："
								+ Thread.currentThread().getName());
						Thread.sleep(1000); // sleep 1000ms
						Message message = Message.obtain();
						message.what = 2;
						message.obj = "GoodsDetailActivity"
								+ Thread.currentThread().getName();
						timeHandler_qws.sendMessage(message);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}

	/**
	 * 倒计时计算
	 */
	private void computeTime() {
		mSecond--;
		if (mSecond < 0) {
			mMin--;
			mSecond = 59;
			if (mMin < 0) {
				mMin = 59;
				mHour--;
				if (mHour < 0) {
					// 倒计时结束
					mHour = 23;
					mDay--;
				}
			}
		}
	}

	private void refresh(BnProduct product) {
		product_id = product.getId();
		goods_details_old_price.setText("市场价￥" + product.getMktprice());
		goods_details_income.setText(product.getAgent_income());
		goods_details_new_price.setText("￥" + product.getPrice());
	}

	// class CommentDialog extends Dialog {
	//
	// private Button btn_ok, btn_cancel;
	// private EditText comment;
	// private Context context;
	// private String id;
	//
	// public CommentDialog(Context context, String id) {
	// super(context, R.style.Dialog);
	// this.context = context;
	// this.id = id;
	//
	// }
	//
	// @Override
	// protected void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.dialog_comment);
	// init();
	// }
	//
	// private void init() {
	// btn_ok = (Button) findViewById(R.id.btn_ok);
	// comment = (EditText) findViewById(R.id.comment_et);
	// btn_ok.setOnClickListener(new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View arg0) {
	// if (!StringUtils.isEmpty(comment.getText().toString())) {
	//
	// String url = UrlConfig.submitComment(context, id,
	// comment.getText().toString());
	// HttpUtil.get(url, new JsonHttpResponseHandler() {
	// @Override
	// public void onSuccess(JSONObject arg0) {
	// BaseBean bean = JSON.parseObject(
	// arg0.toString(), BaseBean.class);
	// if (bean.getStatusCode().equals("200")) {
	// Utils.ToastMessage(context, "评论成功");
	// loadData();
	// dismiss();
	// } else {
	// Utils.ToastMessage(context, "评论失败");
	// }
	// super.onSuccess(arg0);
	// }
	// });
	// } else {
	// Utils.ToastMessage(context, "请输入评论内容");
	// }
	// }
	// });
	// }
	// }
}
