package com.app.linj.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * 存放所有的list在最后退出时一起关闭

 * Created by zhangshenglan on 16/4/22.
 */
public class PublicWay {
    public static List<Activity> activityList = new ArrayList<Activity>();

    public static int num = 9;

}
