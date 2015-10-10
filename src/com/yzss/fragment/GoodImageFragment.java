package com.yzss.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzss.activity.R;
import com.yzss.bean.BnGallery;

public class GoodImageFragment extends Fragment {

	private View view;
	private BnGallery pager;

	public static GoodImageFragment newInstance(BnGallery pager) {
		GoodImageFragment fragment = new GoodImageFragment();
		fragment.pager = pager;
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.widget_image_org, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
		ImageLoader.getInstance().displayImage(pager.getImg_url(), imageView);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}
		});
		return view;
	}

}
