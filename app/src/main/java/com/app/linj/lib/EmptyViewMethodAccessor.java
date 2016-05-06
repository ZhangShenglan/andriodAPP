package com.app.linj.lib;

import android.view.View;

/**
 * Created by zhangshenglan on 16/5/6.
 */
public interface EmptyViewMethodAccessor {

    /**
     * Calls upto AdapterView.setEmptyView()
     *
     * @param emptyView - to set as Empty View
     */
    public void setEmptyViewInternal(View emptyView);

    /**
     * Should call PullToRefreshBase.setEmptyView() which will then
     * automatically call through to setEmptyViewInternal()
     *
     * @param emptyView - to set as Empty View
     */
    public void setEmptyView(View emptyView);

}
