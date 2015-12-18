package com.app.linj.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import com.app.core.base.BaseActivity;
import com.app.linj.R;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    NavigationDrawerFragment mNavigationDrawerFragment;

    Toolbar mToolsbar;

    String[] title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        title = getResources().getStringArray(R.array.drawer_title);
        mToolsbar = (Toolbar) findViewById(R.id.toolbar);
        mToolsbar.setTitle(title[0]);
        setSupportActionBar(mToolsbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.init(R.id.navigation_drawer,
                drawer, mToolsbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new ActivityFragment()).commit();
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
// Handle navigation view item clicks here.
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new ActivityFragment();
                break;
            case 1:
                fragment = new DynamicFragment();
                break;
            case 2:
                // 进入冒泡页面，单独处理
                break;
            case 3:
                fragment = new DynamicFragment();
                break;
            case 4:
                fragment = new DynamicFragment();
                break;
        }
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
        }
        mToolsbar.setTitle(title[position]);
    }
}
