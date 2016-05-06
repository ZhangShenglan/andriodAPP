package com.app.linj.activity;

import android.os.Bundle;

/**
 * Created by zhangshenglan on 16/5/6.
 */

public abstract class BasePageActivity extends BaseActivityAc{

    @Override
    protected void onCreate(Bundle bundle) {
        // TODO Auto-generated method stub
        super.onCreate(bundle);
        setLayoutView();
        init(bundle);
    }

    private void init(Bundle bundle) {
        // TODO Auto-generated method stub
        findViews();
        setupViews(bundle);
        setListener();
        fetchData();
    }

    protected abstract void setLayoutView();

    protected abstract void findViews();

    protected abstract void setupViews(Bundle bundle);

    protected abstract void setListener();

    protected abstract void fetchData();

}
