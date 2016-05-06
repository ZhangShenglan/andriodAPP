package com.app.linj.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.app.linj.R;
import com.app.linj.adapter.ReadtieziViewPager;
import com.app.linj.bean.User;
import com.app.linj.bean.UserBean;
import com.app.linj.ui.TieZiHuiFufragment;
import com.app.linj.ui.TieZiZhuTifragment;
import com.app.linj.view.TabPageIndicator;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by zhangshenglan on 16/5/6.
 */
public class ReadTieziActivity extends FragmentActivity {
    private TabPageIndicator titleIndicator;
    private List<Fragment> listfragment;
    private ReadtieziViewPager adapter;
    private ViewPager pager;
    private TextView rhuifu;
    private UserBean user;
    private String[] titlestrs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.readtiezilayout);
        user = (UserBean) getIntent().getSerializableExtra("listdata");
        initView();
        UserBean cUser = BmobUser
                .getCurrentUser(ReadTieziActivity.this, UserBean.class);
        if (user.getUsername().equals(cUser.getUsername())) {
            titlestrs = new String[] { "我的主题", "我的回复" };
            rhuifu.setText("我的帖子");
        } else {
            rhuifu.setText("他的帖子");
            titlestrs = new String[] { "他的主题", "他的回复" };
        }

        adapter = new ReadtieziViewPager(getSupportFragmentManager(),
                listfragment, titlestrs);
        pager.setAdapter(adapter);
        titleIndicator.setViewPager(pager);

    }

    private void initView() {

        rhuifu = (TextView) findViewById(R.id.rhuifu);
        titleIndicator = (TabPageIndicator) findViewById(R.id.titles);
        pager = (ViewPager) findViewById(R.id.tzviewpager);
        listfragment = new ArrayList<Fragment>();
        TieZiZhuTifragment zhuTifragment = new TieZiZhuTifragment(user);
        TieZiHuiFufragment huiFufragment = new TieZiHuiFufragment(user);
        listfragment.add(zhuTifragment);
        listfragment.add(huiFufragment);

    }
}
