package com.android.baseline.framework.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import java.util.List;

/**
 * Fragment适配器
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 17/4/14 11:57]
 */
public class FragmentPagerItemAdapter extends FragmentPagerAdapter {
    FragmentManager fm;
    private List<Fragment> fragments;
    private List<String> titles;

    public FragmentPagerItemAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fragments = fragments;
        this.titles = titles;
    }

    public FragmentPagerItemAdapter(FragmentManager fm, List<Fragment> fragments) {
        this(fm, fragments, null);
    }

    /**
     * 刷新数据源
     *
     * @param fragments
     * @param titles
     */
    public void setDataSource(List<Fragment> fragments, List<String> titles) {
        if (this.fragments != null) {
            // 清除历史 Fragment
            FragmentTransaction ft = fm.beginTransaction();
            for (Fragment f : this.fragments) {
                ft.remove(f);
            }
            ft.commit();
            fm.executePendingTransactions();
        }

        this.fragments = fragments;
        this.titles = titles;
    }

    public void setDataSource(List<Fragment> fragments) {
        setDataSource(fragments, null);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        // 使得 notifyDataSetChanged() 有效
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return fragments != null ? fragments.size() : 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles == null) {
            return null;
        }
        return titles.get(position);
    }
}

