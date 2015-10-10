package com.yzss.fragment;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzss.activity.R;
import com.yzss.bean.BnSlider;
import com.yzss.utils.Utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public final class MainImageFragment extends Fragment {

	private View view;
	private BnSlider pager;

	public static MainImageFragment newInstance(BnSlider pager) {
		MainImageFragment fragment = new MainImageFragment();
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
		view = inflater.inflate(R.layout.widget_imageview, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
		ImageLoader.getInstance().displayImage(pager.getImg_url(), imageView);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Utils.toAction(getActivity(), pager.getAction().get(0));
			}
		});
		return view;
	}

}
