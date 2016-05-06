package com.app.linj.ui;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.core.base.RefreshBaseFragment;
import com.app.core.utils.FootUpdate;
import com.app.linj.R;
import com.app.linj.adapter.ViewPagerAdapter;
import com.app.linj.bean.Activity;
import com.app.linj.com.BitmapUtils;
import com.app.linj.util.HttpUtil;
import com.app.linj.util.JsonTools;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 活动fragment
 * Created by zhangsl on 15/12/4.
 */
public class ActivityFragment extends RefreshBaseFragment implements FootUpdate.LoadMore, View.OnClickListener,
        ViewPager.OnPageChangeListener {
    @Bind(R.id.listView)
    JazzyListView listView;

    private List<ImageView> imageViewContainer = null;
    private List<com.app.linj.bean.Activity> activities = new ArrayList<>();
    private int count = 0;

    private int preDotPosition = 0;

    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    private int width;
    private int height;
    private RelativeLayout relativeLayout;


    private static final int[] pics = new int[]{
            R.drawable.view_1,
            R.drawable.view_2,
            R.drawable.view_3,
            R.drawable.view_4
    };

    private static final int[] activityPics = new int[]{
            R.drawable.huangshan1,
            R.drawable.huangshan2,
            R.drawable.huangshan2,
            R.drawable.huangshan2,
            R.drawable.huangshan2,
            R.drawable.huangshan2,
            R.drawable.huangshan2,
            R.drawable.huangshan2

    };

    // 底部小店图片
    private ImageView[] dots;
    // 记录当前选中位置
    private int currentIndex;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        if(getArguments() != null){
            activities = getArguments().getParcelableArrayList("activities");
            if(activities != null && !activities.isEmpty()){
                count = activities.size()+1;
//                listView.setAdapter(new ActivityAdapter());
                }
        }
        else {
            //http请求获取服务端数据
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads().detectDiskWrites().detectNetwork()
                    .penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                    .penaltyLog().penaltyDeath().build());
            JSONObject jsonObject = HttpUtil.sendPostResquest("http://10.0.2.2:8080/activity/list.do");

            try {
                String rows = jsonObject.get("rows").toString();
                //把字符串里面的[]截取掉
                rows = rows.substring(1, rows.length() - 1);
                //rows里有多个对象时要分隔
                String[] rowStrs = rows.split("\\}" + ",");
                for (String r : rowStrs) {
                    Activity activity = new Activity();
                    //json格式把后面一个大括号补上
                    if (!r.contains("}")) {
                        r = r + "}";
                    }
                    JSONObject rowsJson = new JSONObject(r);
                    activity = JsonTools.convertActivity(rowsJson);
                    activities.add(activity);
                    count = activities.size() + 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initRefreshLayout();
        listView.setTransitionEffect(new UpSlideInEffect());
        listView.setAdapter(new ActivityAdapter());
        mFootUpdate.init(listView, mInflater, this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_maopao, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = super.onOptionsItemSelected(item);
        if (handled) {
            return true;
        }
        int itemId_ = item.getItemId();

        if(itemId_ == R.id.one || itemId_ == R.id.two || itemId_ == R.id.three || itemId_ == R.id.four || itemId_ == R.id.five){
            int itemNum = 0;
            if(itemId_ == R.id.one){
                itemNum = 1;
            }
            if(itemId_ == R.id.two){
                itemNum = 2;
            }
            if(itemId_ == R.id.three){
                itemNum = 3;
            }
            if(itemId_ == R.id.four){
                itemNum = 4;
            }
            if(itemId_ == R.id.five){
                itemNum = 5;
            }
            //http请求获取服务端数据
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads().detectDiskWrites().detectNetwork()
                    .penaltyLog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                    .penaltyLog().penaltyDeath().build());
            try {
                JSONObject jsonObject = HttpUtil.sendPostResquest("http://10.0.2.2:8080/activity/list.do?activityType=" + itemNum);
                activities = new ArrayList<>();
                Object test1 = jsonObject.get("statusCode");
                if(jsonObject.get("statusCode").equals(200)){
                    String rows = jsonObject.get("rows").toString();
                    //把字符串里面的[]截取掉
                    rows = rows.substring(1, rows.length() - 1);
                    //rows里有多个对象时要分隔
                    String[] rowStrs = rows.split("\\}" + ",");
                    for (String r : rowStrs) {
                        Activity activity = new Activity();
                        //json格式把后面一个大括号补上
                        if (!r.contains("}")) {
                            r = r + "}";
                        }
                        JSONObject rowsJson = new JSONObject(r);
                        activity = JsonTools.convertActivity(rowsJson);
                        activities.add(activity);
                    }
                }

                FragmentManager fm = getFragmentManager();
                FragmentTransaction tx = fm.beginTransaction();
                ActivityFragment activityFragment = new ActivityFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("activities", (ArrayList<Activity>) activities);
                activityFragment.setArguments(bundle);
                tx.replace(R.id.container, activityFragment, "Three");
                tx.addToBackStack(null);
                tx.commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (itemId_ == R.id.action_search) {
            Log.d("now", "searching!!");
            getFragmentManager().beginTransaction().replace(R.id.container, new SearchActivityFragment()).commit();
            return true;
        }
        if (itemId_ == R.id.action_more) {
            Log.d("now", "addActivity!!");
            getFragmentManager().beginTransaction().replace(R.id.container, new ActivityAddFragment()).commit();
            return true;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                count = 10;
                listView.setAdapter(new ActivityAdapter());
                setRefreshing(false);
            }
        }, 2000);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void loadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                count += 10;
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        }, 2000);
    }


    protected class ActivityAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            Log.d("count=", String.valueOf(count));
            return count;
        }

        @Override
        public Object getItem(int position) {
            return "ss";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (position == 0) {
                convertView = mInflater.inflate(R.layout.fragment_banner_list_item, null);
                //等到屏幕的大小
                width = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                //以670*240的图片为例,正常中不要这样用
                height = width * 240 / 670;
                relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relative);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) relativeLayout.getLayoutParams();
                layoutParams.width = width;
                layoutParams.height = height;
                relativeLayout.setLayoutParams(layoutParams);

                views = new ArrayList<View>();

                LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                // 初始化引导图片列表
                for (int i = 0; i < pics.length; i++) {
                    ImageView iv = new ImageView(getActivity());
                    iv.setLayoutParams(mParams);
                    //改变大小
//			iv.setImageResource(pics[i]);
                    iv.setImageBitmap(BitmapUtils.zoomImage(BitmapFactory.decodeResource(getResources(), pics[i]), width, height));
                    views.add(iv);
                }
                vp = (ViewPager) convertView.findViewById(R.id.viewpager);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
                params.leftMargin = 10;
                params.topMargin = 10;
                params.rightMargin = 10;
                vp.setLayoutParams(params);
                // 初始化Adapter
                vpAdapter = new ViewPagerAdapter(views);
                vp.setAdapter(vpAdapter);
                // 绑定回调
                vp.setOnPageChangeListener(ActivityFragment.this);
                // 初始化底部小点
                initDots(convertView);
            } else {
                convertView = mInflater.inflate(R.layout.fragment_activity_list_item, null);

                TextView tv_price = (TextView) convertView.findViewById(R.id.activity_item_price);
                TextView tv_summary = (TextView) convertView.findViewById(R.id.activity_item_summary);
                TextView tv_time = (TextView) convertView.findViewById(R.id.activity_item_time);
                TextView tv_days = (TextView) convertView.findViewById(R.id.activity_item_days);
                TextView tv_viewCount = (TextView) convertView.findViewById(R.id.activity_item_viewcount);

                tv_price.setText(String.valueOf(position));

                if (position <= activities.size()) {
                    if (activities.get(position-1) != null) {
                        if (activities.get(position-1).getTitle()!= null && activities.get(position-1).getIntroduce() != null) {
                            tv_summary.setText("[" + activities.get(position-1).getTitle() +"]"+ activities.get(position-1).getIntroduce());
                        }
                        if (activities.get(position-1).getTime() != null) {
                            tv_time.setText(activities.get(position-1).getTime());
                        }
                            tv_days.setText(String.valueOf(activities.get(position-1).getDays()) + "日行" );
                        if(activities.get(position-1).getViewCount() != null){
                            tv_viewCount.setText(String.valueOf(activities.get(position-1).getViewCount()) + "浏览");
                        }

                    }
                }

                ImageView i = (ImageView) convertView.findViewById(R.id.activity_item_icon);
                if (position <= activityPics.length) {
                    i.setBackgroundResource(activityPics[ (activities.get(position-1).getId()).intValue() - 1]);
                }


                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("TAG", "onClick execute" + position);
                        FragmentManager fm = getFragmentManager();
                        FragmentTransaction tx = fm.beginTransaction();
                        ActivityDetailFragment activityDetail = new ActivityDetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("key", activities.get(position - 1).getId().toString());
                        activityDetail.setArguments(bundle);
                        tx.replace(R.id.container, activityDetail, "TWO");
                        tx.commit();
                    }
                });
            }
            return convertView;
        }


    }

    private void initDots(View view) {
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll);
        dots = new ImageView[pics.length];

        // 循环取得小点图片
        for (int i = 0; i < pics.length; i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// 都设为灰色
            dots[i].setOnClickListener(this);
            dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }

    // listview 向上滑才有动画
    class UpSlideInEffect extends SlideInEffect {
        @Override
        public void initView(View item, int position, int scrollDirection) {
            if (scrollDirection > 0) {
                Log.d("now", "upupupupup");
                super.initView(item, position, scrollDirection);
            }
        }

        @Override
        public void setupAnimation(View item, int position, int scrollDirection, ViewPropertyAnimator animator) {
            if (scrollDirection > 0) {
                super.setupAnimation(item, position, scrollDirection, animator);
            }
        }
    }

    /**
     * 设置当前的引导页
     */
    private void setCurView(int position) {
        if (position < 0 || position >= pics.length) {
            return;
        }

        vp.setCurrentItem(position);
    }


    /**
     * 这只当前引导小点的选中
     */
    private void setCurDot(int positon) {
        if (positon < 0 || positon > pics.length - 1 || currentIndex == positon) {
            return;
        }

        dots[positon].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = positon;
    }

    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

        vp.requestDisallowInterceptTouchEvent(true);
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
        setCurDot(arg0);
    }

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        setCurView(position);
        setCurDot(position);
    }


}
