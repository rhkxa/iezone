package com.yzss.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.yzss.activity.AreaActivity;
import com.yzss.activity.GoodsDetailActivity;
import com.yzss.activity.GoodsGridActivity;
import com.yzss.activity.LoginActivity;
import com.yzss.activity.OrderActivity;
import com.yzss.activity.PayActivity;
import com.yzss.activity.R;
import com.yzss.activity.ShoppingActivity;
import com.yzss.activity.GameActivity;
import com.yzss.bean.BnAction;
import com.yzss.bean.BnUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class Utils {
	/**
	 * 弹出Toast消息
	 * 
	 * @param msg
	 */
	public static void ToastMessage(Context cont, String msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, int msg) {
		Toast.makeText(cont, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ToastMessage(Context cont, String msg, int time) {
		Toast.makeText(cont, msg, time).show();
	}

	/**
	 * 显示application的Toast消息
	 * 
	 * @param message
	 */
	public static void showSuperCardToast(Context cont, String message) {
		try {
			if (message != null && message.length() != 0) {
				Toast.makeText(cont.getApplicationContext(), message,
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检测WIFI是否可用
	 * 
	 * @param context
	 * @return
	 */

	public boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}

		return false;
	}

	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	/**
	 * 屏幕的宽
	 * 
	 * @return
	 */
	public static int getScreenWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	/**
	 * 屏幕的高
	 * 
	 * @return
	 */
	public static int getScreenHight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	/**
	 * 选择图片裁剪
	 * 
	 * @param output
	 */
	public static void startImagePick(Activity activity, int flag) {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		activity.startActivityForResult(Intent.createChooser(intent, "选择图片"),
				flag);
	}

	/**
	 * 相机拍照
	 * 
	 * @param activity
	 * @param output
	 *            相册拍照存储图片的URI
	 * @param flag
	 *            标识
	 */
	public static void startActionCamera(Activity activity, Uri output, int flag) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
		activity.startActivityForResult(intent, flag);
	}

	/**
	 * 
	 * 拍照后裁剪
	 * 
	 * @param activity
	 * @param data
	 *            原始图片
	 * @param output
	 *            裁剪后图片
	 * @param crop
	 *            裁剪大小
	 * @param flag
	 *            标识
	 */
	public static void startActionCrop(Activity activity, Uri data, Uri output,
			int crop, int flag) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		intent.putExtra("output", output);
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", crop);// 输出图片大小
		intent.putExtra("outputY", crop);
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		activity.startActivityForResult(intent, flag);
	}



	public static void Share(Context context, String Title, String content,
			String path, String imagePath) {
		ShareSDK.initSDK(context);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 分享时Notification的图标和文字
		// oks.setNotification(R.drawable.smyk_logo, context.getResources()
		// .getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(Title);
		// text是分享文本，所有平台都需要这个字段
		oks.setText(content);
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/storage/emulated/0/picture/default_.jpg");//
		// 确保SDcard下面存在此张图片
		oks.setImagePath(imagePath);// 确保SDcard下面存在此张图片

		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(path);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(path);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用oks.setComment("我是测试评论文本");

		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(context.getResources().getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(path);
		oks.setSilent(false);
		oks.setDialogMode();// 显示为弹窗效果
		// oks.setTheme(OnekeyShareTheme.SKYBLUE);蓝色主题
		// 启动分享GUI
		oks.show(context);
	}

	private static PlatformActionListener paListener = new PlatformActionListener() {

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(Platform arg0, int arg1,
				HashMap<String, Object> arg2) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCancel(Platform arg0, int arg1) {
			// TODO Auto-generated method stub

		}
	};

	public static void shareOneQQ(Context context, String Title,
			String content, String path, String imagePath) {
		ToastMessage(context, "正在启动QQ分享");
		ShareSDK.initSDK(context);
		QQ.ShareParams sp = new QQ.ShareParams();
		sp.setTitle(Title);
		sp.setTitleUrl(path); // 标题的超链接
		sp.setText(content);
		sp.setImagePath(imagePath);
		sp.setImageUrl(imagePath);
		Platform qzone = ShareSDK.getPlatform(QQ.NAME);
		qzone.setPlatformActionListener(paListener); // 设置分享事件回调
		// 执行图文分享
		qzone.share(sp);
	}

	public static void shareOneSina(Context context, String text, String path,
			String imagePath) {
		ToastMessage(context, "正在启动新浪微博分享");
		ShareSDK.initSDK(context);
		SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
		sp.setText(text);
		sp.setImagePath(imagePath);
		// 分享网络图片，新浪分享网络图片，需要申请高级权限,否则会报10014的错误
		// 权限申请：新浪开放平台-你的应用中-接口管理-权限申请-微博高级写入接口-statuses/upload_url_text
		// 注意：本地图片和网络图片，同时设置时，只分享本地图片
		// oks.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
		sp.setUrl(path);
		Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
		weibo.setPlatformActionListener(paListener); // 设置分享事件回调
		// 执行图文分享
		weibo.share(sp);

	}

	public static void shareOneWechat(Context context, String Title,
			String content, String path, String imagePath) {


		ShareSDK.initSDK(context);
		Wechat.ShareParams sp = new Wechat.ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);
		sp.setTitle(Title);
		sp.setText(content);
		sp.setImagePath(imagePath);
		sp.setUrl(path);
		Platform qzone = ShareSDK.getPlatform(Wechat.NAME);
		qzone.setPlatformActionListener(paListener); // 设置分享事件回调
		// 执行图文分享
		qzone.share(sp);
	}

	public static void shareOneWechatMoments(Context context, String Title,
			String content, String path, String imagePath) {
		ToastMessage(context, "正在启动微信分享");
		ShareSDK.initSDK(context);
		WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
		sp.setShareType(Platform.SHARE_WEBPAGE);
		sp.setTitle(Title);
		sp.setText(content);
		sp.setImagePath(imagePath);
		sp.setUrl(path);
		Platform qzone = ShareSDK.getPlatform(WechatMoments.NAME);
		qzone.setPlatformActionListener(paListener); // 设置分享事件回调
		// 执行图文分享
		qzone.share(sp);
	}

	// 打电话
	public static void callPhone(Context context, String inputStr) {
		if (!StringUtils.isEmpty(inputStr)) {
			Intent phoneIntent = new Intent("android.intent.action.CALL",
					Uri.parse("tel:" + inputStr));
			// 启动
			context.startActivity(phoneIntent);
		}
	}

	public static void toExploer(Context context, String url) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse(url);
		intent.setData(content_url);
		context.startActivity(intent);
	}

	// 手动取一个key的值
	public static String getKey(String str, String key) {

		String result = "";
		try {
			JSONObject json = new JSONObject(str);
			result = json.getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	// 手动取一个key的值
	public static String getKey(JSONObject json, String key) {

		String result = "";
		try {
			result = json.getString(key);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	// 网络请求是否成功
	public static boolean requestOk(JSONObject json) {
		LogUtils.d("iezone", json.toString());
		try {
			if (json.getString("code").equals("100000")) {
				return true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	}

	// 判断访问是否成功，成功后返回结果json
	public static String getResult(JSONObject json) {
		String result = "";
		try {
			if (requestOk(json)) {
				result = json.getString("result");
				LogUtils.d("json数据", "result============" + result);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;

	}

	public static void toGoodDetail(Context context, String goods_id) {
		Intent intent = new Intent(context, GoodsDetailActivity.class);
		intent.putExtra("goods_id", goods_id);
		context.startActivity(intent);
	}

	public static void toGoodsList(Context context, String cate_id,
			String cate_name) {
		Intent intent = new Intent(context, GoodsGridActivity.class);
		intent.putExtra("cate_name", cate_name);
		intent.putExtra("cate_id", cate_id);
		context.startActivity(intent);
	}

	public static void toArea(Context context, int area_id) {
		Intent intent = new Intent(context, AreaActivity.class);
		intent.putExtra("area_id", area_id);
		context.startActivity(intent);
	}

	public static void toOrderDetail(Context context, String order_id) {
		Intent intent = new Intent(context, OrderActivity.class);
		intent.putExtra("order_id", order_id);
		context.startActivity(intent);
	}

	public static void joinShop(final Context context, String product_id,
			int quantity) {
		if (isLogin(context)) {
			String uid = PreferenceUtils.getInstance(context).getStringValue(
					"uid");
			HttpUtil.get(UrlConfig.getCartAdd(uid, product_id, quantity),
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject arg0) {
							// TODO Auto-generated method stub
							if (requestOk(arg0)) {
								String count = getKey(getResult(arg0), "count");
								PreferenceUtils.getInstance(context).setValue(
										"count", count);
								ToastMessage(context, "添加购物车成功");

							} else {
								if (getKey(arg0, "code").equals("200108")) {
									ToastMessage(context, getKey(arg0, "msg"));
								}
							}

							super.onSuccess(arg0);
						}
					});
		} else {
			toLogin(context);
		}

	}

	public static void joinCollect(final Context context, String goods_id) {
		if (isLogin(context)) {
			String uid = PreferenceUtils.getInstance(context).getStringValue(
					"uid");
			HttpUtil.get(UrlConfig.getCollectAdd(uid, goods_id),
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONObject arg0) {
							// TODO Auto-generated method stub
							if (requestOk(arg0)) {
								ToastMessage(context, "添加收藏成功");
							} else {
								if (getKey(arg0, "code").equals("200107")) {
									ToastMessage(context, getKey(arg0, "msg"));
								}
							}
							super.onSuccess(arg0);
						}
					});
		} else {
			toLogin(context);
		}

	}

	public static boolean isLogin(Context context) {
		if (StringUtils.isEmpty(PreferenceUtils.getInstance(context)
				.getStringValue("uid"))) {
			return false;
		}
		return true;
	}

	public static void toLogin(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);

	}

	public static void toShop(Context context) {
		Class<?> cls = null;
		if (isLogin(context)) {
			cls = ShoppingActivity.class;

		} else {
			cls = LoginActivity.class;
		}
		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
	}

	public static void toWebView(Context context, String url) {
		Intent intent = new Intent(context, GameActivity.class);
		intent.putExtra("url", url);
		context.startActivity(intent);
	}

	public static void toPay(Context context, String sn,String title,String money) {
		Intent intent = new Intent(context, PayActivity.class);
		intent.putExtra("sn", sn);
		intent.putExtra("title", title);
		intent.putExtra("money", money);
		context.startActivity(intent);
	}

	public static void toOrder(Context context, String state) {
		if(isLogin(context)){
			Intent intent = new Intent(context, OrderActivity.class);
			intent.putExtra("state", state);
			context.startActivity(intent);
		}else{
			toLogin(context);
		}
		
	}
	

	public static void toAction(Context context, BnAction action) {
		switch (action.getType()) {
		case "out_link":
			toExploer(context, action.getPage_url());
			break;

		case "in_link":
			toWebView(context, action.getPage_url());
			break;
		case "goods_list":
			toGoodsList(context, action.getTarget_id(),"活动");
			break;
		case "goods_info":
			toGoodDetail(context, action.getTarget_id());
			break;
		case "special_area":
			break;
		case "order_list":
			break;
		}
	}

	public static String getUid(Context context) {
		return PreferenceUtils.getInstance(context).getStringValue("uid");
	}

	public static void setUid(Context context, String uid) {
		PreferenceUtils.getInstance(context).setValue("uid", uid);
	}

	public static BnUser getUser(Context context) {
		String str = PreferenceUtils.getInstance(context)
				.getStringValue("user");
		if (StringUtils.isEmpty(str)) {
			return null;
		} else {
			return JSON.parseObject(str, BnUser.class);
		}
	}

	public static void setUser(Context context, String user) {
		PreferenceUtils.getInstance(context).setValue("user", user);
	}

	public static String getAccount(Context context) {
		return PreferenceUtils.getInstance(context).getStringValue("account");
	}

	public static void setAccount(Context context, String user) {
		PreferenceUtils.getInstance(context).setValue("account", user);
	}
	public static String getKeyWords(Context context) {
		String str = PreferenceUtils.getInstance(context)
				.getStringValue("keywords");
		if (StringUtils.isEmpty(str)) {
			return null;
		} else {
			return str;
		}
	}

	public static void setKeyWords(Context context, String keywords) {
		PreferenceUtils.getInstance(context).setValue("keywords", keywords);
	}
	public static Map<Integer, String> setSingleMap(List<String> list) {
		Map<Integer, String> data=new HashMap<Integer, String>();
		int size=list.size();
		for(int i=0;i<size;i++){
			data.put(i, list.get(i));
		}		
		return data;
	}
}
