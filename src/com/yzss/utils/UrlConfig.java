package com.yzss.utils;

public class UrlConfig {
	//private static final String SERVER = "http://114.215.131.140/shop-api/";
	private static final String SERVER = "http://www.iezhong.com/shop-api/";

	// 登录
	public static final String getLogin(String mobi, String word) {
		return SERVER + "login/submit" + "?mobi=" + mobi + "&password=" + word;
	}

	// 注册
	public static final String getRegister(String mobi, String word,
			String captcha, String promo_code) {
		return SERVER + "register/submit" + "?mobi=" + mobi + "&password="
				+ word + "&captcha=" + captcha + "&promo_code=" + promo_code;
	}

	// 获取验证码
	public static final String getCaptcha(String mobi) {
		return SERVER + "register/captcha?mobi=" + mobi;
	}

	// 修改密码
	public static final String getNewPassword(String password) {
		return SERVER + "getNewPassword" + "&newpassword=" + "";

	}

	// 修改昵称
	public static final String getAlterPerson(String uid, String nickname,
			String sex) {
		return SERVER + "member/update_info" + "?uid=" + uid + "&nickname="
				+ nickname + "&sex=" + sex;
	}

	// 首页
	public static final String getFirstPage() {
		return SERVER + "page/home";
	}

	// 专区
	public static final String getArea(int area_id) {
		return SERVER + "page/special_area?area_id=" + area_id;
	}

	// 商品列表
	public static final String getGoodsList(String cate_id, String sort,
			int psize, int pno, String sales, String price) {
		return SERVER + "product/goods_list" + "?cate_id=" + cate_id
				+ "&psize=" + psize + "&pno=" + pno + "&sales=" + sales
				+ "&price=" + price;
	}

	// 商品详细信息
	public static final String getGoodsInfo(String goods_id) {
		return SERVER + "product/goods_info" + "?goods_id=" + goods_id;
	}

	// 得到活动列表
	public static final String getActivityList() {
		return SERVER + "getActivityList";
	}

	// 得到活动详情
	public static final String getActivity(String atid) {
		return SERVER + "getActivity" + "&atid=" + atid;
	}

	// 点赞功能
	public static final String submitPraise(String id) {
		return SERVER + "submitPraise" + "&id=" + id;
	}

	// 点评
	public static final String submitComment(String id, String comment) {
		return SERVER + "submitComment" + "&id=" + id + "&comment=" + comment;
	}

	// 得到分类列表
	public static final String getCateList() {
		return SERVER + "product/cate_list";
	}

	// 初始化订单
	public static final String loadOrder() {
		return SERVER + "order/init";

	}

	// 提交订单提交一个订单的数组
	public static final String submitOrder(String uid, String sn, String addr_id) {
		return SERVER + "order/add" + "?uid=" + uid + "&sn=" + sn + "&addr_id="
				+ addr_id;

	}

	// 订单状态查询
	public static final String queryOrderStatus() {
		return SERVER + "queryOrderStatus";
	}

	// 订单列表
	public static final String getOrderList(String uid, String status) {
		return SERVER + "order/list" + "?uid=" + uid + "&status=" + status;
	}

	// 订单详情
	public static final String queryOrder() {
		return SERVER + "queryOrder";
	}

	// 搜索
	public static final String getSearch(String keyword, int psize, int pno) {
		return SERVER + "product/search" + "?keyword=" + keyword + "&pno="
				+ pno + "&psize=" + psize;
	}

	// 依据品牌搜索
	public static final String searchByBrand(String brand, int pagenum,
			int oftype, String odirection) {
		return SERVER + "searchByBrand" + "&brand=" + brand
				+ "&currentPageIndex=" + pagenum + "&oftype=" + oftype
				+ "&odirection=" + odirection;
	}

	// 评论及意见
	public static final String submitSuggest(String content) {
		return SERVER + "submitSuggest" + "&msg=" + content;
	}

	// 提交评论
	public static final String getCommentAdd(String uid, String goods_id,
			String content) {
		return SERVER + "comment/add" + "?uid=" + uid + "&goods_id=" + goods_id
				+ "&content=" + content;
	}

	// 评论列表
	public static final String getCommentList(String goods_id, int pno,
			int psize) {
		return SERVER + "comment/list" + "?goods_id=" + goods_id + "&psize="
				+ psize + "&pno=" + pno;
	}

	// 指定分类的子分类列表
	public static final String classList(String typeid) {
		return SERVER + "getSubClassList" + "&typeid" + typeid;
	}

	// 更新用户头像
	public static final String updateHeadImg() {
		return SERVER + "member/upload_photo";
	}

	// 品牌列表
	public static final String getLogoList() {
		return SERVER + "getLogoList";
	}

	// 分类列表
	public static final String getClassList() {
		return SERVER + "product/cate_list";
	}

	// 添加收藏
	public static final String getCollectAdd(String uid, String goods_id) {
		return SERVER + "collect/add" + "?uid=" + uid + "&goods_id=" + goods_id;
	}

	// 收藏列表
	public static final String getCollectList(String uid) {
		return SERVER + "collect/list" + "?uid=" + uid;
	}

	// 删除收藏
	public static final String getCollectDel(String uid, String goods_id) {
		return SERVER + "collect/del" + "?uid=" + uid + "&goods_id=" + goods_id;
	}

	// 用户积分
	public static final String User_score() {
		return "http://122.0.64.40/User_score";
	}

	// 待审核订单：
	public static final String orderListPre() {
		return SERVER + "orderListPre";
	}

	// 往期订单：
	public static final String orderListPast() {
		return SERVER + "orderListPast";
	}

	// 推荐关键字
	public static final String getKeyList() {
		return SERVER + "getKeyList";
	}

	// 地址列表
	public static final String getAddressList(String uid) {
		return SERVER + "address/list" + "?uid=" + uid;
	}

	// 地址详细
	public static final String getAddressInfo(String uid) {
		return SERVER + "address/info" + "?uid=" + uid;
	}

	// 更新收货地址
	public static final String getAddressEdit(String uid, String addr_id,
			String name, int sex, String mobile, int province, int city,
			int district, String addr, String is_default) {
		return SERVER + "address/edit" + "?uid=" + uid + "&addr_id=" + addr_id
				+ "&name=" + name + "&sex=" + sex + "&mobile=" + mobile
				+ "&province=" + province + "&city=" + city + "&district="
				+ district + "&addr=" + addr + "&is_default=" + is_default;
	}

	// 增加收货地址

	public static final String getAddressAdd(String uid, String name, int sex,
			String mobile, int province, int city, int district, String addr,
			String is_default) {
		return SERVER + "address/add" + "?uid=" + uid + "&name=" + name
				+ "&sex=" + sex + "&mobile=" + mobile + "&province=" + province
				+ "&city=" + city + "&district=" + district + "&addr=" + addr
				+ "&is_default=" + is_default;
	}

	// 删除收货地址

	public static final String getAddressDel(String uid, String addr_id) {
		return SERVER + "address/del" + "?uid=" + uid + "&addr_id=" + addr_id;

	}

	// 个人信息
	public static final String getMeberInfo(String uid) {
		return SERVER + "member/info" + "?uid=" + uid;

	}

	// 我的代金券
	public static final String getCoupons(String uid) {
		return SERVER + "coupons/list" + "&uid=" + uid;

	}

	// 购物车列表
	public static final String getCartList(String uid) {
		return SERVER + "cart/list" + "?uid=" + uid;

	}

	// 添加购物车
	public static final String getCartAdd(String uid, String product_id,
			int quantity) {
		return SERVER + "cart/add" + "?uid=" + uid + "&product_id="
				+ product_id + "&quantity=" + quantity;

	}

	// 删除购物车
	public static final String getCartDel(String uid, String product_id) {
		return SERVER + "cart/del" + "?uid=" + uid + "&product_id="
				+ product_id;

	}

	// 修改购物车
	public static final String getCartQuantity(String uid, String product_id,
			int quantity) {
		return SERVER + "cart/set_quantity" + "?uid=" + uid + "&product_id="
				+ product_id + "&quantity=" + quantity;

	}

	/**
	 * 申请加盟
	 * 
	 * @param uid
	 * @param type
	 *            1正式 2体验
	 * @return
	 */
	public static final String getApply(String uid, int type) {
		return SERVER + "agent/apply" + "?uid=" + uid + "&type=" + type;
	}

	// 我的加盟商信息接口
	public static final String getAgent(String uid) {
		return SERVER + "agent/info" + "?uid=" + uid;
	}

	// 加盟商信息排名
	public static final String getAgentRank() {
		return SERVER + "agent/rank";
	}

	// 系统初始化配置接口
	public static final String getSystem() {
		return SERVER + "system/init";
	}

	// 充值
	public static final String getCharge() {
		return SERVER + "charge/list";
	}

	// 提现
	public static final String getAgentOut(String  uid, int amount) {
		return SERVER + "agent/withdraw" + "?uid=" + uid + "&amount=" + amount;
	}

	// 转入余额
	public static final String getAgentIn(String uid, int amount) {
		return SERVER + "agent/to_balance" + "?uid=" + uid + "&amount="
				+ amount;
	}
	public static final String getWx(String uid, String order_sn) {
		return SERVER + "order/weixin" + "?uid=" + uid + "&order_sn="
				+ order_sn;
	}

}
