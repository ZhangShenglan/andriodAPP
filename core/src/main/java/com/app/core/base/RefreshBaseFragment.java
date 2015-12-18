package com.app.core.base;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;

import com.app.core.R;
import com.app.core.utils.FootUpdate;


/**
 * 封装了下拉刷新
 */
public abstract class RefreshBaseFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final String UPDATE_ALL = "99999999";
    public static final int UPDATE_ALL_INT = 99999999;

    private SwipeRefreshLayout swipeRefreshLayout;
    protected FootUpdate mFootUpdate = new FootUpdate();

    protected final boolean isRefreshing() {
        return swipeRefreshLayout.isRefreshing();
    }

    protected final void setRefreshing(boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    protected final void initRefreshLayout() {
        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue);
    }

    protected final void disableRefreshing() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setEnabled(false);
        }
    }

    protected final void enableSwipeRefresh(boolean enable) {
        swipeRefreshLayout.setEnabled(enable);
    }
}