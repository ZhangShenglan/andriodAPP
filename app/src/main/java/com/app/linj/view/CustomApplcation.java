package com.app.linj.view;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

import com.app.linj.R;
import com.app.linj.ui.SharePreferenceUtil;
import com.app.linj.util.CollectionUtils;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * Created by zhangshenglan on 16/5/6.
 */
public class CustomApplcation extends Application {
    public static CustomApplcation mInstance;
    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;

    public static BmobGeoPoint lastPoint = null;// ????味?位?????纬??

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        // ?????debug??--????????
        BmobChat.DEBUG_MODE = true;
        mInstance = this;
        init();
    }



    private void init() {
        mMediaPlayer = MediaPlayer.create(this, R.raw.notify);
        mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        initImageLoader(getApplicationContext());
        // ???????????????????????????????list?????????
        if (BmobUserManager.getInstance(getApplicationContext())
                .getCurrentUser() != null) {
            // ??????????user list?????,?????????????list
            contactList = CollectionUtils.list2map(BmobDB.create(getApplicationContext()).getContactList());
        }
        initBaidu();
    }

    /**
     * ???????????sdk initBaidumap
     * @Title: initBaidumap
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */
    private void initBaidu() {
        // ????????Sdk
        SDKInitializer.initialize(this);
        // ???????位sdk
        initBaiduLocClient();
    }

    /**
     * ?????????位sdk
     * @Title: initBaiduLocClient
     * @Description: TODO
     * @param
     * @return void
     * @throws
     */


    private void initBaiduLocClient() {
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // Receive Location
            double latitude = location.getLatitude();
            double longtitude = location.getLongitude();
            if (lastPoint != null) {
                if (lastPoint.getLatitude() == location.getLatitude()
                        && lastPoint.getLongitude() == location.getLongitude()) {
//					BmobLog.i("???位????????");// ???????????????????位??????????????????位
                    mLocationClient.stop();
                    return;
                }
            }
            lastPoint = new BmobGeoPoint(longtitude, latitude);
        }
    }

    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                "bmobim/Cache");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCache(new UnlimitedDiscCache(cacheDir))// ????寤�?路??
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

    public static CustomApplcation getInstance() {
        return mInstance;
    }

    SharePreferenceUtil mSpUtil;
    public static final String PREFERENCE_NAME = "_sharedinfo";

    public synchronized SharePreferenceUtil getSpUtil() {
        if (mSpUtil == null) {
            String currentId = BmobUserManager.getInstance(
                    getApplicationContext()).getCurrentUserObjectId();
            String sharedName = currentId + PREFERENCE_NAME;
            mSpUtil = new SharePreferenceUtil(this, sharedName);
        }
        return mSpUtil;
    }

    NotificationManager mNotificationManager;

    public NotificationManager getNotificationManager() {
        if (mNotificationManager == null)
            mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        return mNotificationManager;
    }

    MediaPlayer mMediaPlayer;

    public synchronized MediaPlayer getMediaPlayer() {
        if (mMediaPlayer == null)
            mMediaPlayer = MediaPlayer.create(this, R.raw.notify);
        return mMediaPlayer;
    }

    public final String PREF_LONGTITUDE = "longtitude";// ????
    private String longtitude = "";


    public String getLongtitude() {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        longtitude = preferences.getString(PREF_LONGTITUDE, "");
        return longtitude;
    }


    public void setLongtitude(String lon) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_LONGTITUDE, lon).commit()) {
            longtitude = lon;
        }
    }

    public final String PREF_LATITUDE = "latitude";// ????
    private String latitude = "";


    public String getLatitude() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        latitude = preferences.getString(PREF_LATITUDE, "");
        return latitude;
    }


    public void setLatitude(String lat) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        if (editor.putString(PREF_LATITUDE, lat).commit()) {
            latitude = lat;
        }
    }

    private Map<String, BmobChatUser> contactList = new HashMap<String, BmobChatUser>();

    public Map<String, BmobChatUser> getContactList() {
        return contactList;
    }


    public void setContactList(Map<String, BmobChatUser> contactList) {
        if (this.contactList != null) {
            this.contactList.clear();
        }
        this.contactList = contactList;
    }
    public DisplayImageOptions getOptions(int drawableId){
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(drawableId)
                .showImageForEmptyUri(drawableId)
                .showImageOnFail(drawableId)
                .resetViewBeforeLoading(true)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }


    public void logout() {
        BmobUserManager.getInstance(getApplicationContext()).logout();
        setContactList(null);
        setLatitude(null);
        setLongtitude(null);
    }
}
