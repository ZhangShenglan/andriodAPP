package com.app.linj.adapter;

/**
 * Created by zhangshenglan on 16/5/6.
 */
public interface IconPagerAdapter {
    /**
     * Get icon representing the page at {@code index} in the adapter.
     */
    int getIconResId(int index);

    // From PagerAdapter
    int getCount();
}
