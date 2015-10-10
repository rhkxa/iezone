package com.yzss.activity;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzss.activity.R;
import com.yzss.adapter.AreaGoodsBannerAdapter;
import com.yzss.adapter.GoodsGridViewAdapter;
import com.yzss.bean.AreaBean;
import com.yzss.bean.BnGoods;
import com.yzss.bean.BnHGoods;
import com.yzss.fragment.HeaderFragment;
import com.yzss.utils.BaseActivity;
import com.yzss.utils.HttpUtil;
import com.yzss.utils.UrlConfig;
import com.yzss.utils.Utils;

/**
 * 买一赠一,鲜到鲜得,食尚鲜果的页面
 * 
 * @author rhk
 * 
 */
public class AreaActivity extends BaseActivity {
	private ImageView active_top_banner;
	private AreaGoodsBannerAdapter mLAdapter;
	private GoodsGridViewAdapter mGAdapter;
	private GridView active_gridview;
	private ListView active_listview;
	private List<BnHGoods> goodlist = new ArrayList<BnHGoods>();
	private List<BnGoods> goodgrid = new ArrayList<BnGoods>();
	private int area_id;
	private AreaBean area;
	private HeaderFragment headerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_active);
		init();
		loadData();
	}

	private void init() {
		// TODO Auto-generated method stub
		headerFragment = (HeaderFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.title);

		area_id = getIntent().getIntExtra("area_id", 0);
		String title = "";
		switch (area_id) {
		case 1:
			title = "买一赠一";
			break;
		case 2:
			title = "食尚鲜果";
			break;
		case 3:
			title = "鲜到鲜得";
			break;

		}
		headerFragment.setTitleText(title);
		active_top_banner = (ImageView) findViewById(R.id.active_top_banner);
		active_top_banner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		active_gridview = (GridView) findViewById(R.id.active_gridview);
		mGAdapter = new GoodsGridViewAdapter(AreaActivity.this, goodgrid);
		active_gridview.setAdapter(mGAdapter);
		active_gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Utils.toGoodDetail(AreaActivity.this, goodgrid.get(arg2).getId());
			}
		});

		active_listview = (ListView) findViewById(R.id.active_listview);
		mLAdapter = new AreaGoodsBannerAdapter(AreaActivity.this, goodlist);
		active_listview.setAdapter(mLAdapter);
		active_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Utils.toGoodDetail(AreaActivity.this, goodlist.get(arg2).getGoods_id());
			}
		});
		
	}

	private void loadData() {
		if (Utils.isNetWorkConnected(AreaActivity.this)) {
			String url = UrlConfig.getArea(area_id);
			HttpUtil.get(url, new jsonHttpResponseHandler() {
				@Override
				public void onSuccess(JSONObject arg0) {
					// TODO Auto-generated method stub
					area = JSON.parseObject(Utils.getResult(arg0),
							AreaBean.class);
					setValue();
					super.onSuccess(arg0);
				}
			});
		} else {
			Utils.showSuperCardToast(AreaActivity.this, getResources()
					.getString(R.string.network_not_connected));
		}
	}

	private void setValue() {
		goodgrid.clear();
		goodgrid.addAll(area.getGoods());
		goodlist.clear();
		goodlist.addAll(area.getGoods_banner());
		mGAdapter.notifyDataSetChanged();
		mLAdapter.notifyDataSetChanged();
		ImageLoader.getInstance().displayImage(
				area.getTop_banner().get(0).getImg_url(), active_top_banner);
	}
}
