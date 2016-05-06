package com.app.linj.util;

import android.annotation.SuppressLint;
import android.os.Environment;

/**
 * Created by zhangshenglan on 16/4/29.
 */
@SuppressLint("SdCardPath")
public class BmobConstants {
    /**
     * 存放发送图片的目录
     */
    public static String BMOB_PICTURE_PATH = Environment.getExternalStorageDirectory()	+ "/Ibaidutieba/image/";

    /**
     * 我的头像保存目录
     */
    public static String MyAvatarDir = "/sdcard/Ibaidutieba/avatar/";
    /**
     * 拍照回调
     */
    public static final int REQUESTCODE_UPLOADAVATAR_CAMERA = 1;//拍照修改头像
    public static final int REQUESTCODE_UPLOADAVATAR_LOCATION = 2;//本地相册修改头像
    public static final int REQUESTCODE_UPLOADAVATAR_CROP = 3;//系统裁剪头像

    public static final int REQUESTCODE_TAKE_CAMERA = 0x000001;//拍照
    public static final int REQUESTCODE_TAKE_LOCAL = 0x000002;//本地图片
    public static final int REQUESTCODE_TAKE_LOCATION = 0x000003;//位置
    public static final String EXTRA_STRING = "extra_string";
    public static final int NUMBERS_PER_PAGE = 25;// 每次请求返回评论条数

    public static final String ACTION_REGISTER_SUCCESS_FINISH ="register.success.finish";//注册成功之后登陆页面退出
}
