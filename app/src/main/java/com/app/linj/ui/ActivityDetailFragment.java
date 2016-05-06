package com.app.linj.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.core.base.RefreshBaseFragment;
import com.app.core.utils.FootUpdate;
import com.app.linj.R;
import com.app.linj.activity.GroupListActivity;
import com.app.linj.bean.Activity;
import com.app.linj.bean.Captain;
import com.app.linj.util.HttpUtil;
import com.app.linj.util.JsonTools;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 活动详情页面
 * Created by zhangshenglan on 16/4/17.
 */
public class ActivityDetailFragment extends RefreshBaseFragment implements FootUpdate.LoadMore {
    @Bind(R.id.listView)
    JazzyListView listView;

    private int count = 1;
    private List<Activity> activities = new ArrayList<>();
    private List<Captain> captains = new ArrayList<>();
    String activityId;
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
    private Button btn_apply;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        activityId = getArguments().getString("key");
        //http请求获取服务端数据
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
        JSONObject jsonObject = HttpUtil.sendPostResquest("http://10.0.2.2:8080/activity/getActivityById.do?activityId=" + activityId);

        //活动信息
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //领队信息
        try {
            String rows = jsonObject.get("captain").toString();
            //把字符串里面的[]截取掉
            rows = rows.substring(1, rows.length() - 1);
            //rows里有多个对象时要分隔
            String[] rowStrs = rows.split("\\}" + ",");
            for (String r : rowStrs) {
                Captain captain = new Captain();
                //json格式把后面一个大括号补上
                if (!r.contains("}")) {
                    r = r + "}";
                }
                JSONObject rowsJson = new JSONObject(r);
                captain = JsonTools.convertCaptain(rowsJson);
                captains.add(captain);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_apply_activity, null);
        btn_apply = (Button) view.findViewById(R.id.btn_apply);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initRefreshLayout();
        listView.setTransitionEffect(new UpSlideInEffect());
        listView.setAdapter(new ActivityDetailAdapter());
        mFootUpdate.init(listView, mInflater, this);

        String test = btn_apply.getText().toString();

        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("now", "click!!");
                //http请求获取服务端数据
                StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                        .detectDiskReads().detectDiskWrites().detectNetwork()
                        .penaltyLog().build());
                StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                        .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                        .penaltyLog().penaltyDeath().build());
                try {
                    JSONObject jsonObject = HttpUtil.sendPostResquest("http://10.0.2.2:8080/user/apply.do" + "?activityId=" + activities.get(0).getId() + "&userId=" + "2");
                    if (jsonObject.get("statusCode").equals(200)) {
                        Intent intent = new Intent(getActivity(),GroupListActivity.class);
                        startActivity(intent);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_fragment_maopao, menu);
//        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = super.onOptionsItemSelected(item);
        if (handled) {
            return true;
        }
        int itemId_ = item.getItemId();
        if (itemId_ == R.id.action_search) {
            return true;
        }
        return false;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                count = 1;
                listView.setAdapter(new ActivityDetailAdapter());
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

    protected class ActivityDetailAdapter extends BaseAdapter {

        @Override
        public int getCount() {
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
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = mInflater.inflate(R.layout.fragment_detail_activity, null);
            ImageView activity_item_icon = (ImageView) convertView.findViewById(R.id.activity_item_icon);
            TextView activity_item_price = (TextView) convertView.findViewById(R.id.activity_item_price);
            TextView activity_title_summary = (TextView) convertView.findViewById(R.id.activity_title_summary);
            TextView activity_item_time = (TextView) convertView.findViewById(R.id.activity_item_time);
            TextView activity_item_days = (TextView) convertView.findViewById(R.id.activity_item_days);
            TextView activity_item_viewcount = (TextView) convertView.findViewById(R.id.activity_item_viewcount);
            TextView activity_item_beginPlaceContent = (TextView) convertView.findViewById(R.id.activity_item_beginPlaceContent);
            TextView activity_item_endPlaceContent = (TextView) convertView.findViewById(R.id.activity_item_endPlaceContent);
            TextView captainName = (TextView) convertView.findViewById(R.id.captainName);
            TextView captainIntroduce = (TextView) convertView.findViewById(R.id.captainIntroduce);
            TextView activity_item_likeNum = (TextView) convertView.findViewById(R.id.activity_item_likeNum);

            TextView activity_item_applyNum = (TextView) convertView.findViewById(R.id.activity_item_applyNum);
            TextView activity_item_leftNum = (TextView) convertView.findViewById(R.id.activity_item_leftNum);
            //活动类型
            Button activityType = (Button) convertView.findViewById(R.id.activityType);

            TextView activity_item_endPlaceContent5 = (TextView) convertView.findViewById(R.id.activity_item_endPlaceContent5);

            activity_item_icon.setBackgroundResource(activityPics[Integer.valueOf(activityId) - 1]);
            activity_item_price.setText(String.valueOf(activityId));

            if (activities.get(position).getTitle() != null && activities.get(position).getIntroduce() != null) {
                activity_title_summary.setText("[" + activities.get(position).getTitle() + "]" + activities.get(position).getIntroduce());
            }
            if (activities.get(position).getTime() != null) {
                activity_item_time.setText(activities.get(position).getTime());
            }
            activity_item_days.setText(String.valueOf(activities.get(position).getDays()) + "日行");
            if (activities.get(position).getViewCount() != null) {
                activity_item_viewcount.setText(String.valueOf(activities.get(position).getViewCount()) + "浏览");
            }
            if (activities.get(position).getBeginPlace() != null) {
                activity_item_beginPlaceContent.setText(activities.get(position).getBeginPlace());
            }
            if (activities.get(position).getEndPlace() != null) {
                activity_item_endPlaceContent.setText(activities.get(position).getEndPlace());
            }
            if (captains.get(position).getName() != null) {
                captainName.setText(captains.get(position).getName());
            }
            if (captains.get(position).getIntroduce() != null) {
                captainIntroduce.setText(captains.get(position).getIntroduce());
            }
            if (activities.get(position).getLiked() != null) {
                activity_item_likeNum.setText(String.valueOf(activities.get(position).getLiked()));

            }
            if (activities.get(position).getApplied() != null) {
                activity_item_applyNum.setText(String.valueOf(activities.get(position).getApplied()) + ",");
            }
            if (activities.get(position).getTotalNum() != null && activities.get(position).getApplied() != null) {
                activity_item_leftNum.setText(String.valueOf(activities.get(position).getTotalNum() - activities.get(position).getApplied()));
            }
            if (activities.get(position).getActivityType() != null) {
                activityType.setText(activities.get(position).getActivityType());
            }
            if (activities.get(position).getIntroduce() != null) {
                activity_item_endPlaceContent5.setText(activities.get(position).getIntroduce());
            }
            return convertView;
        }

    }


    // listview 向上滑才有动画
    class UpSlideInEffect extends SlideInEffect {
        @Override
        public void initView(View item, int position, int scrollDirection) {
            if (scrollDirection > 0) {
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
}
