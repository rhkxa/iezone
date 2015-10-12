package com.yzss.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.baidu.frontia.FrontiaApplication;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.yzss.activity.R;
import com.yzss.push.PushUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;

/**
 * @ClassName: HuaShiApplication
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author JIA
 * @date 2013-11-25 下午2:06:20
 * 
 */
public class YZSSApplication extends FrontiaApplication {

	private List<Activity> activities = new ArrayList<Activity>();
	private static YZSSApplication instance;
	// 用于存放倒计时时间
	public static Map<String, Long> map;

	public static YZSSApplication getInstance() {
		if (null == instance) {
			instance = new YZSSApplication();
		}
		return instance;
	}

	@Override
	public void onCreate() {
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
		// StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		// .detectAll().penaltyLog().penaltyDialog().build());
		// StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
		// .detectAll().penaltyLog().penaltyDeath().build());
		// }

		super.onCreate();
		// initLocation(getApplicationContext());
		// initPush(getApplicationContext());
		initImageLoader(getApplicationContext());
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}

	public void addActivity(Activity activity) {
		activities.add(activity);
	}

	public void exit() {
		for (Activity activity : activities) {
			activity.finish();
		}
		System.exit(0);
	}

	private void initPush(Context context) {
		if (PreferenceUtils.getInstance(context).getBooleanValue("push")) {
			PushManager.startWork(getApplicationContext(),
					PushConstants.LOGIN_TYPE_API_KEY,
					PushUtils.getMetaValue(context, "api_key"));
		} else {
			PushManager.stopWork(context);
		}
	}

	private void initLocation(Context context) {

	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.public_pic)
				.showImageForEmptyUri(R.drawable.public_pic)
				.showImageOnFail(R.drawable.public_pic).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.threadPoolSize(3).denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(30 * 1024 * 1024)
				.defaultDisplayImageOptions(options)
				// 10 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		ImageLoader.getInstance().clearMemoryCache();

	}

}
