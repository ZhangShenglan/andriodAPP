package com.app.linj;

import android.app.ActivityManager;
import android.content.Context;
import android.location.LocationManager;

import com.app.core.AppException;
import com.app.core.BaseAppContext;

import java.util.List;

/**
 * 自定义Application类
 * Created by zhangsl on 15/12/8.
 */
public class AppContext extends BaseAppContext {
    @Override
    public void onCreate() {
        super.onCreate();
        String processName = getProcessName(this, android.os.Process.myPid());
        //判断进场是否为主线程，只在主线程内做初始化操作
        if (processName != null) {
            boolean defaultProcess = processName.equals(BuildConfig.APPLICATION_ID);
            if (defaultProcess) {
                initAppForMainProcess();
            }
        }
    }

    private void initAppForMainProcess() {
        appContext = this;
        //注册App异常崩溃处理器
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
}
