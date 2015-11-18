package com.yzss.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.yzss.activity.R;
import com.yzss.activity.SearchActivity;
import com.yzss.adapter.HomeActiveListViewAdapter;
import com.yzss.adapter.HomeCateGridViewAdapter;
import com.yzss.adapter.HomeGameListViewAdapter;
import com.yzss.adapter.HomeGoodsListViewAdapter;
import com.yzss.adapter.HomeOptionsGridViewAdapter;
import com.yzss.adapter.MainFragmentAdapter;
import com.yzss.bean.BnHActive;
import com.yzss.bean.BnHCate;
import com.yzss.bean.BnHGame;
import com.yzss.bean.BnHGoods;
import com.yzss.bean.BnHome;
import com.yzss.bean.BnSlider;
import com.yzss.custom.View.Indicator.UnderlinePageIndicator;
import com.yzss.utils.BaseFragment;
import com.yzss.utils.FileUtils;
import com.yzss.utils.HttpUtil;
import com.yzss.utils.StringUtils;
import com.yzss.utils.UrlConfig;
import com.yzss.utils.Utils;

/**
 * 主页面
 * 
 * @author JIA
 * 
 */

public class FragmentHome extends BaseFragment {

	private List<BnSlider> slider = new ArrayList<BnSlider>();

	private View view;
	private ViewPager mPager;
	private UnderlinePageIndicator mIndicator;
	private MainFragmentAdapter fAdapter;
	private PullToRefreshScrollView mPullRefreshScrollView;
	private ScrollView mScrollView;
	private ListView goodsListView;
	private ListView gameListView;
	private GridView cateGridView;
	private ListView activeListView;
	private ImageView main_head_search;
	private GridView home_options_gridview;
	private HomeOptionsGridViewAdapter homeOptionsGridViewAdapter;
	private HomeActiveListViewAdapter activeListViewAdapter;
	private HomeGameListViewAdapter gameListViewAdapter;
	private HomeCateGridViewAdapter cateGridViewAdapter;
	private HomeGoodsListViewAdapter goodsListViewAdapter;
	private List<BnHGame> game = new ArrayList<BnHGame>();
	private List<BnHActive> active = new ArrayList<BnHActive>();
	private List<BnHCate> cate = new ArrayList<BnHCate>();
	private List<List<BnHGoods>> goods = new ArrayList<List<BnHGoods>>();
	private BnHome home;
	private int currentItem;
	private ScheduledExecutorService scheduledExecutorService;
	private LinearLayout ll_time;
	private TextView daysTv;
	private TextView hoursTv;
	private TextView minutesTv;
	private TextView secondsTv;

	protected String currentThreadName = "";
	private boolean isRun = true;
	private Handler timeHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				Log.d("debug", "handleMessage方法所在的线程："
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
						Log.d("debug", "其他线程："
								+ Thread.currentThread().getName());
					}
				}
			}
		}
	};
	private static long mDay = 00;
	private static long mHour = 00;
	private static long mMin = 00;
	private static long mSecond = 00;// 天 ,小时,分钟,秒

	public static FragmentHome newInstance() {
		FragmentHome fragment = new FragmentHome();
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_home, null);
		init();
		loadData();
		return view;
	}

	private void init() {
		ll_time = (LinearLayout) view.findViewById(R.id.ll_time);
		daysTv = (TextView) view.findViewById(R.id.days_tv);
		hoursTv = (TextView) view.findViewById(R.id.hours_tv);
		minutesTv = (TextView) view.findViewById(R.id.minutes_tv);
		secondsTv = (TextView) view.findViewById(R.id.seconds_tv);
		main_head_search = (ImageView) view.findViewById(R.id.main_head_search);
		main_head_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getActivity(), SearchActivity.class);
				startActivity(intent);
			}
		});
		/** 轮播图 **/

		mPager = (ViewPager) view.findViewById(R.id.pager);
		fAdapter = new MainFragmentAdapter(getChildFragmentManager(), slider);
		mPager.setAdapter(fAdapter);
		mIndicator = (UnderlinePageIndicator) view.findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

		/** 四个按钮 **/
		home_options_gridview = (GridView) view
				.findViewById(R.id.home_options_gridview);
		homeOptionsGridViewAdapter = new HomeOptionsGridViewAdapter(
				getActivity());
		home_options_gridview.setAdapter(homeOptionsGridViewAdapter);
		home_options_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 3) {
					Utils.toOrder(getActivity(), "");
				} else {
					// Utils.ToastMessage(getActivity(), "敬请期待……");
					Utils.toArea(getActivity(), arg2 + 1);
				}
			}
		});
		/**** game ******************/
		gameListView = (ListView) view.findViewById(R.id.home_game_listview);
		gameListViewAdapter = new HomeGameListViewAdapter(getActivity(), game);
		gameListView.setAdapter(gameListViewAdapter);
		gameListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Utils.toAction(getActivity(), game.get(arg2).getAction().get(0));
			}
		});
		/****** activity ************/
		activeListView = (ListView) view
				.findViewById(R.id.home_active_listview);
		activeListViewAdapter = new HomeActiveListViewAdapter(getActivity(),
				active);
		activeListView.setAdapter(activeListViewAdapter);

		/****** goods ***********/
		goodsListView = (ListView) view.findViewById(R.id.home_goods_listview);
		goodsListViewAdapter = new HomeGoodsListViewAdapter(getActivity(),
				goods);
		goodsListView.setAdapter(goodsListViewAdapter);

		/****** cate ***********/
		cateGridView = (GridView) view.findViewById(R.id.home_cate_gridview);
		cateGridViewAdapter = new HomeCateGridViewAdapter(getActivity(), cate);
		cateGridView.setAdapter(cateGridViewAdapter);
		cateGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Utils.toGoodsList(getActivity(), cate.get(arg2).getCate_id(),
						cate.get(arg2).getTitle());
			}
		});

		/**** ScrollView下拉 *******/
		mPullRefreshScrollView = (PullToRefreshScrollView) view
				.findViewById(R.id.pull_refresh_scrollview);
		mPullRefreshScrollView
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						mPullRefreshScrollView.setRefreshing(false);
						loadData();
					}
				});

		mScrollView = mPullRefreshScrollView.getRefreshableView();
		mScrollView.smoothScrollTo(0, 0);
		if (!FileUtils.read(getActivity(), "firstPageCache").isEmpty()) {
			home = JSON.parseObject(
					FileUtils.read(getActivity(), "firstPageCache"),
					BnHome.class);
			refresh();
		}
	}

	private void loadData() {
		if (!Utils.isNetWorkConnected(getActivity())) {
			Utils.ToastMessage(getActivity(), "网络状态不可用，请检查网络");
			mPullRefreshScrollView.onRefreshComplete();
			return;
		}
		HttpUtil.get(UrlConfig.getFirstPage(), new jsonHttpResponseHandler() {

			@Override
			public void onSuccess(JSONObject arg1) {

				home = JSON.parseObject(Utils.getResult(arg1), BnHome.class);
				FileUtils.write(getActivity(), "firstPageCache",
						Utils.getResult(arg1));
				refresh();
				super.onSuccess(arg1);
			}

			@Override
			public void onFinish() {
				mPullRefreshScrollView.onRefreshComplete();
				super.onFinish();
			}

		});

	}

	private void refresh() {
		slider.clear();
		slider.addAll(home.getSlider());
		active.clear();
		isRun = true;
		currentThreadName = "";
		if (home.getActivity().size() > 0) {
			String remain_time = home.getActivity().get(0).getRemain_time();

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
		} else {
			ll_time.setVisibility(View.GONE);
		}

		active.addAll(home.getActivity());
		game.clear();
		game.addAll(home.getGame());
		cate.clear();
		cate.addAll(home.getCate());
		goods.clear();
		goods.addAll(home.getGoods_banner());
		fAdapter.notifyDataSetChanged();
		gameListViewAdapter.notifyDataSetChanged();
		activeListViewAdapter.notifyDataSetChanged();
		cateGridViewAdapter.notifyDataSetChanged();
		goodsListViewAdapter.notifyDataSetChanged();
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
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (isRun) {
					try {
						Log.d("debug", "startRun方法所在的线程："
								+ Thread.currentThread().getName());
						Thread.sleep(1000); // sleep 1000ms
						Message message = Message.obtain();
						message.what = 1;
						message.obj = "FragmentHome"
								+ Thread.currentThread().getName();
						timeHandler.sendMessage(message);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
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

	private class SlideShowTask implements Runnable {

		@Override
		public void run() {

			currentItem = (currentItem + 1) % slider.size();
			handler.obtainMessage().sendToTarget();

		}

	}

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 设置当前页面
			mPager.setCurrentItem(currentItem);
		}
	};

	public void onResume() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4,
				TimeUnit.SECONDS);
		super.onResume();
	};

	@Override
	public void onStop() {
		scheduledExecutorService.shutdown();
		super.onStop();
	}

	Handler activehandler = new Handler();
	int recLen = 0;
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			recLen++;
			// txtView.setText("" + recLen);
			if (recLen > 1000) {
				activehandler.postDelayed(this, 1000);
			} else {
				activehandler.removeCallbacks(runnable);
			}

		}
	};

}
