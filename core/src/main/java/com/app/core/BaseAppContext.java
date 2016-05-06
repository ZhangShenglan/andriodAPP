package com.app.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;


import java.io.File;
import java.util.UUID;

//

/**
 * 全局应用程序类：用于保存和调用全局应用配置
 *
 * @author zhangsl
 */
public class BaseAppContext extends Application {

    //singleton
    protected static BaseAppContext appContext = null;
    protected Display display;

    @Override
    public void onCreate() {
        super.onCreate();
        if (display == null) {
            WindowManager windowManager = (WindowManager)
                    getSystemService(Context.WINDOW_SERVICE);
            display = windowManager.getDefaultDisplay();
        }
    }

    public static void showToast(String message) {
        Toast.makeText(getInstance(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(int message) {
        Toast.makeText(getInstance(), message, Toast.LENGTH_SHORT).show();
    }

    public static BaseAppContext getInstance() {
        return appContext;
    }

    public void setProperty(String key, Object value) {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public String getProperty(String key) {
        return AppConfig.getAppConfig(this).get(key);
    }

    public Object getProperty(String key, Object defaultValue) {
        return AppConfig.getAppConfig(this).get(key, defaultValue);
    }

    public void removeProperty(String... key) {
        AppConfig.getAppConfig(this).remove(key);
    }

    public Class<? extends Activity> getChatActivity() {
        return null;
    }

    public Class<? extends Activity> getMainActivity() {
        return null;
    }

    public Class<? extends Activity> getGroupMessageActivity() {
        return null;
    }
    public Class<? extends Activity> getDynamicMessageActivity() {
        return null;
    }
    public  void startActivityDetail(Context context,int id,int batchId){

    }

    public String getCachePath() {
        File cacheDir;
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir = getExternalCacheDir();
        else
            cacheDir = getCacheDir();
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir.getAbsolutePath();
    }

    /**
     * @return
     * @Description： 获取当前屏幕的宽度
     */
    public static int getWindowWidth() {
        return getInstance().display.getWidth();
    }

    /**
     * @return
     * @Description： 获取当前屏幕的高度
     */
    public static int getWindowHeight() {
        return getInstance().display.getHeight();
    }

    /**
     * @return
     * @Description： 获取当前屏幕一半宽度
     */
    public static  int getHalfWidth() {
        return getInstance().display.getWidth() / 2;
    }

    /**
     * @return
     * @Description： 获取当前屏幕1/4宽度
     */
    public static int getQuarterWidth() {
        return getInstance().display.getWidth() / 4;
    }

    /**
     * 清除登录信息
     */
    public void cleanLoginInfo() {

    }

    public SharedPreferences getDefaultSharedPreferences() {
        return AppConfig.getAppConfig(this).getSharedPreferences();
    }
    public int getLoginUid() {
        return 0;
    }

}
