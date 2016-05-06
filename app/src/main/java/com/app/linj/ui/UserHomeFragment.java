package com.app.linj.ui;

import android.os.Bundle;
import android.os.Handler;
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
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.SlideInEffect;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zhangshenglan on 16/4/29.
 */
public class UserHomeFragment extends RefreshBaseFragment implements FootUpdate.LoadMore {
    @Bind(R.id.listView)
    JazzyListView listView;

    private int count = 1;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
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
                count = 1;
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
                count += 1;
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        }, 2000);
    }

    protected class UserFriendAdapter extends BaseAdapter {

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
            convertView = mInflater.inflate(R.layout.fragment_user_home, null);
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
