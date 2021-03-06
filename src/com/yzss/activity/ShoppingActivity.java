package com.yzss.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;

import com.yzss.activity.R;
import com.yzss.adapter.ShoppingListViewAdapter;
import com.yzss.adapter.ShoppingListViewAdapter.OnItemClickListener;
import com.yzss.bean.BnShopping;
import com.yzss.bean.Products;
import com.yzss.bean.ShoppingBean;
import com.yzss.custom.View.Generally.FancyButton;
import com.yzss.custom.View.Swipemenulistview.SwipeMenu;
import com.yzss.custom.View.Swipemenulistview.SwipeMenuCreator;
import com.yzss.custom.View.Swipemenulistview.SwipeMenuItem;
import com.yzss.custom.View.Swipemenulistview.SwipeMenuListView;
import com.yzss.custom.View.Swipemenulistview.SwipeMenuListView.OnMenuItemClickListener;
import com.yzss.fragment.HeaderFragment;
import com.yzss.utils.BaseActivity;
import com.yzss.utils.HttpUtil;
import com.yzss.utils.PreferenceUtils;
import com.yzss.utils.UrlConfig;
import com.yzss.utils.Utils;

/**
 * 购物车的界面
 * 
 * @author rhk
 * 
 */
public class ShoppingActivity extends BaseActivity {
	private HeaderFragment headerFragment;
	private FancyButton allChose;
	private SwipeMenuListView shoppingListView;
	private FancyButton shopping_ok;
	private TextView shopping_all_total;
	private TextView shopping_economize;
	private ShoppingListViewAdapter shoppingAdapter;
	private List<BnShopping> dataList = new ArrayList<BnShopping>();
	private List<BnShopping> selectList = new ArrayList<BnShopping>();
	private Context context;
	private boolean allState = false;
	private List<Products> products=new ArrayList<Products>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shopping);
		init();
		LoadData();

	}

	
	private void init() {
		// TODO Auto-generated method stub
		context = ShoppingActivity.this;
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);
		headerFragment.setTitleText("我的购物车");
		shopping_all_total = (TextView) findViewById(R.id.shopping_all_total);
		shopping_economize = (TextView) findViewById(R.id.shopping_economize);
		shopping_ok = (FancyButton) findViewById(R.id.shopping_ok);
		shoppingListView = (SwipeMenuListView) findViewById(R.id.shopping_listview);
		shoppingAdapter = new ShoppingListViewAdapter(ShoppingActivity.this,
				dataList, selectList);
		shoppingListView.setAdapter(shoppingAdapter);
		// step 1. create a MenuCreator
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// create "delete" item
				SwipeMenuItem deleteItem = new SwipeMenuItem(
						getApplicationContext());
				//set a text
				deleteItem.setTitle("删除");
				deleteItem.setTitleSize(18);// 设置标题文字的大小
                   // set item title font color
				deleteItem.setTitleColor(Color.WHITE);
				// set item background
				deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
						0x3F, 0x25)));
				// set item width
				deleteItem.setWidth(dp2px(90));
				// set a icon
				//deleteItem.setIcon(R.drawable.smyk_delete);
			
				// add to menu
				menu.addMenuItem(deleteItem);
			}
		};
		// set creator
		shoppingListView.setMenuCreator(creator);
		shoppingListView
				.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(int position,
							SwipeMenu menu, int index) {
						// TODO Auto-generated method stub
						delShop(position);
						return false;
					}
				});
		// shoppingListView.setCloseInterpolator(new BounceInterpolator());
		shoppingAdapter.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(CheckBox view, BnShopping shoppingBean,
					boolean isChecked) {
				// TODO Auto-generated method stub
				removePath(shoppingBean);
				if (isChecked) {
					selectList.add(shoppingBean);
					if (selectList.size() == dataList.size()) {
						allState = true;
						allChose.setIconResource(R.drawable.smyk_yes);
					}
				} else {
					allState = false;
					allChose.setIconResource(R.drawable.smyk_no);
				}
				getTotal();
			}

			@Override
			public void onDelete(BnShopping shoppingBean, int position) {
				// TODO Auto-generated method stub
				removePath(shoppingBean);
				dataList.remove(position);
				getTotal();
			}

			@Override
			public void onEdit(int position, int quantity) {
				// TODO Auto-generated method stub
				alterCartQuantity(position, quantity);
			}

		});
		allChose = (FancyButton) findViewById(R.id.all_chose);
		allChose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectList.clear();
				if (allState) {
					allChose.setIconResource(R.drawable.smyk_no);
					allState = false;
				} else {
					allState = true;
					allChose.setIconResource(R.drawable.smyk_yes);
					selectList.addAll(dataList);
				}
				getTotal();
			}
		});

		shopping_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (selectList.size() > 0) {
					submitOrderList();
				} else {
					Utils.ToastMessage(ShoppingActivity.this, "没有需要结算的商品");
				}
			}
		});
	}

	private void getTotal() {
		products.clear();
		shopping_ok.setText("结算(" + selectList.size() + ")");
		int size = selectList.size();
		double totalnew = 0;
		double totalold = 0;
		for (int i = 0; i < size; i++) {
			totalnew = totalnew + selectList.get(i).getPrice()
					* selectList.get(i).getQuantity();
			Products ps=new Products();
			ps.setProduct_id(selectList.get(i).getProduct_id());
			ps.setQuantity(selectList.get(i).getQuantity());
			products.add(ps);
			// totalold = totalold
			// + Double.parseDouble(selectList.get(i).getPrice())
			// * Integer.parseInt(selectList.get(i).getCurnumber());
		}
		shopping_all_total.setText("总计：" + totalnew + "元");
		// shopping_economize.setText("已节省" + (totalold - totalnew) + "元");
		shoppingAdapter.notifyDataSetChanged();
	}

	private void removePath(BnShopping position) {
		int size = selectList.size();
		for (int i = 0; i < size; i++) {
			if (selectList.get(i).getProduct_id()
					.equals(position.getProduct_id())) {
				selectList.remove(i);
				return;
			}
		}

	}

	// 提交订单
	private void submitOrderList() {
		Intent intent = new Intent(ShoppingActivity.this,
				FinishOrderActivity.class);
		intent.putExtra("products", JSON.toJSONString(products));
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		if (arg0 == 0) {
			if (arg1 == RESULT_OK) {
				dataList.clear();
				// dataList.addAll();
				selectList.clear();
				getTotal();
			}
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

		super.onStop();
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	private void LoadData() {
		String uid = PreferenceUtils.getInstance(ShoppingActivity.this)
				.getStringValue("uid");
		HttpUtil.get(UrlConfig.getCartList(uid), new jsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject arg0) {
				// TODO Auto-generated method stub
				ShoppingBean shop = JSON.parseObject(Utils.getResult(arg0),
						ShoppingBean.class);
				dataList.addAll(shop.getDatas());
				getTotal();
				super.onSuccess(arg0);
			}
		});
	}

	public void delShop(final int position) {
		String uid = PreferenceUtils.getInstance(context).getStringValue("uid");
		HttpUtil.get(UrlConfig.getCartDel(uid, dataList.get(position)
				.getProduct_id()), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject arg0) {
				// TODO Auto-generated method stub
				if (Utils.requestOk(arg0)) {
					String count = Utils.getKey(Utils.getResult(arg0), "count");
					PreferenceUtils.getInstance(context).setValue("count",
							count);
					Utils.ToastMessage(context, "删除成功");
					removePath(dataList.get(position));
					dataList.remove(position);
					getTotal();
				} else {
					Utils.ToastMessage(context, "删除失败");

				}
				super.onSuccess(arg0);
			}
		});

	}

	public void alterCartQuantity(final int position, final int quantity) {
		String uid = PreferenceUtils.getInstance(context).getStringValue("uid");
		HttpUtil.get(UrlConfig.getCartQuantity(uid, dataList.get(position)
				.getProduct_id(), quantity), new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject arg0) {
				// TODO Auto-generated method stub
				if (Utils.requestOk(arg0)) {
					dataList.get(position).setQuantity(quantity);
					getTotal();
				} else {
					if (Utils.getKey(arg0, "code").equals("200108")) {
						Utils.ToastMessage(context, Utils.getKey(arg0, "msg"));
					}
				}
				super.onSuccess(arg0);
			}
		});

	}

}
