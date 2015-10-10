/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yzss.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtils {

	/**
	 * 保存Preference的name
	 */

	public static final String PREFERENCE_NAME = "saveInfo";
	private static SharedPreferences mSharedPreferences;
	private static PreferenceUtils mPreferenceUtils;
	private static SharedPreferences.Editor editor;

	private PreferenceUtils(Context cxt) {
		mSharedPreferences = cxt.getSharedPreferences(PREFERENCE_NAME,
				Context.MODE_PRIVATE);
	}

	/**
	 * 单例模式，获取instance实例
	 * 
	 * @param cxt
	 * @return
	 */
	public static PreferenceUtils getInstance(Context cxt) {
		if (mPreferenceUtils == null) {
			mPreferenceUtils = new PreferenceUtils(cxt);
		}
		editor = mSharedPreferences.edit();

		return mPreferenceUtils;
	}

	public void setValue(String key, String value) {
		LogUtils.d("sp", key + "====" + value);
		editor.putString(key, value);
		editor.commit();
	}

	public void setValue(String key, boolean value) {
		LogUtils.d("sp", key + "====" + value);
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void setValue(String key, Float value) {
		LogUtils.d("sp", key + "====" + value);
		editor.putFloat(key, value);
		editor.commit();
	}

	public void setValue(String key, int value) {
		LogUtils.d("sp", key + "====" + value);
		editor.putInt(key, value);
		editor.commit();
	}

	public void setValue(String key, long value) {
		LogUtils.d("sp", key + "====" + value);
		editor.putLong(key, value);
		editor.commit();
	}

	public String getStringValue(String key) {
		LogUtils.d("sp", key + "====" + key);
		return mSharedPreferences.getString(key, "");
	}

	public boolean getBooleanValue(String key) {
		return mSharedPreferences.getBoolean(key, false);
	}

	

}
