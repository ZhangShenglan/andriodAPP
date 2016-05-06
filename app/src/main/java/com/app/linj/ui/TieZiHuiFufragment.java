package com.app.linj.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.linj.R;
import com.app.linj.activity.CommentActivity;
import com.app.linj.adapter.TieZiHuifuAdapter;
import com.app.linj.bean.Comment;
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
public class TieZiHuiFufragment extends Fragment {
    private PullToRefreshListView mPullRefreshListView;
    private ListView listviewhf;
    private UserBean user;
    private boolean pullFromUser;

    public enum RefreshType {
        REFRESH, LOAD_MORE
    }

    private int pageNum;

    private RefreshType mRefreshType = RefreshType.LOAD_MORE;
    private List<Comment> mListItems;
    private TieZiHuifuAdapter adapter;

    public TieZiHuiFufragment(UserBean user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tiezihuifulayout, container,
                false);
        Log.d("CommentActivity", user.getUsername()
                + "++++name");

        mPullRefreshListView = (PullToRefreshListView) view
                .findViewById(R.id.huifu_pull_refresh_list);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullRefreshListView.setState(PullToRefreshBase.State.RELEASE_TO_REFRESH, true);
        mPullRefreshListView
                .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    // ����ˢ��
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
        listviewhf = mPullRefreshListView.getRefreshableView();
        mListItems = new ArrayList<Comment>();
        adapter = new TieZiHuifuAdapter(getActivity(), mListItems);
        listviewhf.setAdapter(adapter);
        if (mListItems.size() == 0) {
            fetchData();
        }

        listviewhf.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {

                Intent intent = new Intent();
                intent.setClass(getActivity(), CommentActivity.class);
                intent.putExtra("data", mListItems.get(position - 1)
                        .getGozhuti());
                intent.putExtra("tbname", mListItems.get(position - 1)
                        .getCtbname());
                startActivity(intent);

            }
        });

        return view;
    }

    protected void fetchData() {

        BmobQuery<Comment> query = new BmobQuery<Comment>();
        query.order("-createdAt");
        query.setLimit(BmobConstants.NUMBERS_PER_PAGE);
        query.addWhereEqualTo("user", user);
        query.include("user,gozhuti.author");

        query.setSkip(BmobConstants.NUMBERS_PER_PAGE * pageNum);
        query.findObjects(getActivity(), new FindListener<Comment>() {

            @Override
            public void onSuccess(List<Comment> list) {
                if (list.size() != 0 && list.get(list.size() - 1) != null) {
                    if (mRefreshType == RefreshType.REFRESH) {
                        mListItems.clear();
                    }
                    if (list.size() < BmobConstants.NUMBERS_PER_PAGE) {
                    }
                    mListItems.addAll(list);
                    adapter.notifyDataSetChanged();
                    pageNum = pageNum + 1;

                    mPullRefreshListView.onRefreshComplete();
                } else {
                    mPullRefreshListView.onRefreshComplete();
                }
            }

            @Override
            public void onError(int arg0, String arg1) {

            }
        });

    }

}
