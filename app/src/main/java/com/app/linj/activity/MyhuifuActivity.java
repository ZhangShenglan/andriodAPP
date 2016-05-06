package com.app.linj.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.linj.R;
import com.app.linj.adapter.HfAdapter;
import com.app.linj.bean.Comment;
import com.app.linj.bean.UserBean;
import com.app.linj.lib.PullToRefreshBase;
import com.app.linj.lib.PullToRefreshListView;
import com.app.linj.util.BmobConstants;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhangshenglan on 16/5/6.
 */
public class MyhuifuActivity extends Activity {
    private PullToRefreshListView mPullRefreshListView;
    private boolean pullFromUser;
    private int pageNum;
    private ListView hfListView;
    private ArrayList<Comment> hfListItems;

    public enum RefreshType {
        REFRESH, LOAD_MORE
    }

    private RefreshType mRefreshType = RefreshType.LOAD_MORE;
    private HfAdapter adapter;
    private UserBean user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myhuifu);
        user = BmobUser.getCurrentUser(MyhuifuActivity.this, UserBean.class);

        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id._huifupull_refresh_list);

        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    // 下拉刷新
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        String label = DateUtils.formatDateTime(
                                MyhuifuActivity.this,
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);
                        refreshView.getLoadingLayoutProxy()
                                .setLastUpdatedLabel(label);
                        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
                        pullFromUser = true;
                        mRefreshType = RefreshType.REFRESH;
                        pageNum = 0;
                        fetchData();
                    }

                    // 上拉加载
                    @Override
                    public void onPullUpToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        mRefreshType = RefreshType.LOAD_MORE;
                        fetchData();
                    }
                });

        hfListView = mPullRefreshListView.getRefreshableView();
        hfListItems = new ArrayList<Comment>();
        adapter = new HfAdapter(this, hfListItems);
        hfListView.setAdapter(adapter);
        if (hfListItems.size() == 0) {
            fetchData();
        }
        hfListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                Intent intent = new Intent();
                intent.setClass(MyhuifuActivity.this, CommentActivity.class);
                intent.putExtra("data", hfListItems.get(position - 1)
                        .getGozhuti());
                intent.putExtra("tbname", hfListItems.get(position - 1)
                        .getCtbname());
                startActivity(intent);

            }
        });

    }

    private void fetchData() {
        BmobQuery<Comment> eq1 = new BmobQuery<Comment>();
        eq1.addWhereEqualTo("zhuhfname", user.getUsername());

        BmobQuery<Comment> eq2 = new BmobQuery<Comment>();
        eq2.addWhereEqualTo("cihfname", user.getUsername());

        BmobQuery<Comment> eq3 = new BmobQuery<Comment>();
        eq3.addWhereEqualTo("cicihfname", user.getUsername());

        List<BmobQuery<Comment>> queries = new ArrayList<BmobQuery<Comment>>();
        queries.add(eq1);
        queries.add(eq2);
        queries.add(eq3);

        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.or(queries);
        query.order("-createdAt");
        query.setLimit(BmobConstants.NUMBERS_PER_PAGE);
        // query.addWhereEqualTo("zhuhfname",user.getUsername());
        query.include("user,gozhuti.author");
        query.setSkip(BmobConstants.NUMBERS_PER_PAGE * pageNum);
        query.findObjects(MyhuifuActivity.this, new FindListener<Comment>() {

            @Override
            public void onSuccess(List<Comment> list) {
                if (list.size() != 0 && list.get(list.size() - 1) != null) {
                    if (mRefreshType == RefreshType.REFRESH) {
                        hfListItems.clear();
                    }
                    if (list.size() < BmobConstants.NUMBERS_PER_PAGE) {
                        // LogUtils.i(TAG,"已加载完所有数据~");
                    }
                    hfListItems.addAll(list);
                    adapter.notifyDataSetChanged();
                    pageNum = pageNum + 1;

                    mPullRefreshListView.onRefreshComplete();
                } else {
                    // ActivityUtil.show(getActivity(), "暂无更多数据~");
                    mPullRefreshListView.onRefreshComplete();
                }
            }

            @Override
            public void onError(int arg0, String arg1) {

            }
        });

    }
}
