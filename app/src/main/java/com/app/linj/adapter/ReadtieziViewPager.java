package com.app.linj.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by zhangshenglan on 16/5/6.
 */
public class ReadtieziViewPager extends FragmentPagerAdapter {
    private List<Fragment> listfragment;
    private String[] titlestr;

    public ReadtieziViewPager(FragmentManager fm, List<Fragment> listfragment,
                              String[] titlestr) {
        super(fm);
        this.listfragment = listfragment;
        this.titlestr = titlestr;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // TODO Auto-generated method stub
        return titlestr[position];
    }

    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        return listfragment.get(position);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return listfragment.size();
    }
}
