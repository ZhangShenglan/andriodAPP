package com.app.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;


/**
 * @author linjizong
 * @Description: 应用程序配置类：用于保存用户相关信息及设置
 * @date 2015-4-11
 */
@SuppressLint("NewApi")
public class AppConfig {
	private SharedPreferences sp;
	private Context mContext;
	private static AppConfig appConfig;

	public static AppConfig getAppConfig(Context context) {
		if (appConfig == null) {
			appConfig = new AppConfig();
			appConfig.mContext = context;
			appConfig.sp = context.getSharedPreferences("setting", 0);
		}
		return appConfig;
	}

	/**
	 * 获取Preference设置
	 */
	public SharedPreferences getSharedPreferences() {
		return sp;
	}


	public String get(String key) {
		return (String) get(key, "");
	}

	public Object get(String key, Object defaultValue) {
		if (defaultValue instanceof Boolean) {
			return sp.getBoolean(key, (Boolean) defaultValue);
		} else if (defaultValue instanceof String) {
			return sp.getString(key, (String) defaultValue);
		} else if (defaultValue instanceof Integer) {
			return sp.getInt(key, (Integer) defaultValue);
		} else if (defaultValue instanceof Long) {
			return sp.getLong(key, (Long) defaultValue);
		} else if (defaultValue instanceof Float) {
			return sp.getFloat(key, (Float) defaultValue);
		} else {
			//The object is not of the appropriate type
			String msg = String.format(
					"%s: %s",
					key,
					defaultValue.getClass().getName());
			throw new RuntimeException(msg);
		}
	}


	public void set(String key, Object value) {
		SharedPreferences.Editor editor = sp.edit();
		if (value == null) {
			return;
		}
		if (value instanceof Boolean) {
			editor.putBoolean(key, ((Boolean) value).booleanValue());
		} else if (value instanceof String) {
			editor.putString(key, (String) value);
		} else if (value instanceof Integer) {
			editor.putInt(key, (Integer) value);
		} else if (value instanceof Long) {
			editor.putLong(key, (Long) value);
		} else {
			//The object is not of the appropriate type
			String msg = String.format(
					"%s: %s",
					key,
					value.getClass().getName());
			throw new RuntimeException(msg);
		}
		editor.commit();
	}

	public void remove(String... key) {
		for (String k : key)
			sp.edit().remove(k).commit();
	}

}
