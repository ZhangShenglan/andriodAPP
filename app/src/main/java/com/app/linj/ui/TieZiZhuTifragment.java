package com.app.linj.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.linj.R;
import com.app.linj.activity.CommentActivity;
import com.app.linj.adapter.ZhutiAdapter;
import com.app.linj.bean.QiangYu;
import com.app.linj.bean.User;
import com.app.linj.bean.UserBean;
import com.app.linj.lib.PullToRefreshBase;
import com.app.linj.lib.PullToRefreshListView;
import com.app.linj.util.BmobConstants;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhangshenglan on 16/5/6.
 */
public class TieZiZhuTifragment extends Fragment {
    private PullToRefreshListView mPullRefreshListView;
    private UserBean user;
    private ListView listviewzt;
    private boolean pullFromUser;
    private int pageNum;
    private String tbname;

    public enum RefreshType {
        REFRESH, LOAD_MORE
    }

    private RefreshType mRefreshType = RefreshType.LOAD_MORE;
    private List<QiangYu> mListItems;
    private ZhutiAdapter adapter;

    public TieZiZhuTifragment(UserBean user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.teizizhutilayout, container,
                false);
        mPullRefreshListView = (PullToRefreshListView) view
                .findViewById(R.id.zhuti_pull_refresh_list);

        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullRefreshListView.setState(PullToRefreshBase.State.RELEASE_TO_REFRESH, true);
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    // 下拉刷新
                    @Override
                    public void onPullDownToRefresh(
                            PullToRefreshBase<ListView> refreshView) {
                        String label = DateUtils.formatDateTime(getActivity(),
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

        mPullRefreshListView
                .setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {

                    }
                });
        listviewzt = mPullRefreshListView.getRefreshableView();
        mListItems = new ArrayList<QiangYu>();
        adapter = new ZhutiAdapter(getActivity(),mListItems);
        listviewzt.setAdapter(adapter);

        if (mListItems.size() == 0) {
            fetchData();
        }
        listviewzt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                // MyApplication.getInstance().setCurrentQiangYu(mListItems.get(position-1));
                Intent intent = new Intent();
                intent.setClass(getActivity(), CommentActivity.class);
                intent.putExtra("data", mListItems.get(position-1));
                intent.putExtra("tbname", tbname);
                startActivity(intent);
            }
        });


        return view;
    }

    private void fetchData() {
        BmobQuery<QiangYu> query = new BmobQuery<QiangYu>();
        query.order("-createdAt");
        // 查询的数据条数
        query.setLimit(BmobConstants.NUMBERS_PER_PAGE);
        query.addWhereEqualTo("author", user);
        // 跳过查询的数据NUMBERS_PER_PAGE*(pageNum++)
        query.setSkip(BmobConstants.NUMBERS_PER_PAGE * pageNum);
        query.include("author");
        query.findObjects(getActivity(), new FindListener<QiangYu>() {


            @Override
            public void onSuccess(List<QiangYu> list) {
                // TODO Auto-generated method stub
                if (list.size() != 0 && list.get(list.size() - 1) != null) {
                    if (mRefreshType == RefreshType.REFRESH) {
                        mListItems.clear();
                    }
                    if (list.size() < BmobConstants.NUMBERS_PER_PAGE) {
                        // LogUtils.i(TAG,"已加载完所有数据~");
                    }
                    mListItems.addAll(list);
                    adapter.notifyDataSetChanged();
                    tbname = list.get(0).getGltbname();
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
