package com.app.linj.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.core.base.RefreshBaseFragment;
import com.app.core.utils.FootUpdate;
import com.app.linj.R;
import com.app.linj.bean.UserB;
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
 * Created by zhangshenglan on 16/4/29.
 */
public class UserFanFragment extends RefreshBaseFragment implements FootUpdate.LoadMore {
    @Bind(R.id.listView)
    JazzyListView listView;

    private List<UserB> fans = new ArrayList<UserB>();
    private int count = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //http请求获取服务端数据
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork()
                .penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects().detectLeakedClosableObjects()
                .penaltyLog().penaltyDeath().build());
        JSONObject jsonObject = HttpUtil.sendPostResquest("http://10.0.2.2:8080/user/list.do");

                try{
                    String rows = jsonObject.get("rows").toString();
                    //把字符串里面的[]截取掉
                    rows = rows.substring(1,rows.length()-1);
                    //rows里有多个对象时要分隔
                    String[] rowStrs = rows.split("\\}" + ",");
                    for(String r : rowStrs){
                        UserB userB = new UserB();
                        //json格式把后面一个大括号补上
                        if(!r.contains("}")){
                            r = r + "}";
                        }
                        JSONObject rowsJson = new JSONObject(r);
                        userB = JsonTools.convertUserB(rowsJson);
                        fans.add(userB);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
        count = fans.size();
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
        listView.setAdapter(new UserFriendAdapter());
        mFootUpdate.init(listView, mInflater, this);
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
                count = 10;
                listView.setAdapter(new UserFriendAdapter());
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

    protected class UserFriendAdapter extends BaseAdapter {
        private Context ct;
        private List<UserB> data;

        public UserFriendAdapter(){

        }
        public UserFriendAdapter(Context ct, List<UserB> datas) {
            this.ct = ct;
            this.data = datas;
        }

        public void updateListView(List<UserB> list) {
        this.data = list;
        notifyDataSetChanged();
    }

        public void remove(UserB user){
            this.data.remove(user);
            notifyDataSetChanged();
        }


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
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.item_user_friend, null);
                viewHolder = new ViewHolder();
                viewHolder.alpha = (TextView) convertView.findViewById(R.id.alpha);
                viewHolder.aline = (View)convertView.findViewById(R.id.aline);
                viewHolder.name = (TextView) convertView
                        .findViewById(R.id.tv_friend_name);
                viewHolder.avatar = (ImageView) convertView
                        .findViewById(R.id.img_friend_avatar);
                convertView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if(position < fans.size()){
                if(fans.get(position) != null){
                    if(fans.get(position).getId() != null){
                        viewHolder.alpha.setText(fans.get(position).getId().toString());
                    }
                    if(fans.get(position).getName() != null){
                        viewHolder.name.setText(fans.get(position).getName());
                    }
                }
            }
            return convertView;
        }

        class ViewHolder {
            TextView alpha;// 首字母提示
            ImageView avatar;
            TextView name;
            View aline;

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
