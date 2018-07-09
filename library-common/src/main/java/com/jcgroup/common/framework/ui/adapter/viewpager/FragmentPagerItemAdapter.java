package com.jcgroup.common.framework.ui.adapter.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

import java.util.List;

/**
 * FragmentPagerAdapter适配器
 *
 * @author hiphonezhu@gmail.com
 * @version [DX-AndroidLibrary, 2018-3-6]
 */
public class FragmentPagerItemAdapter extends FragmentPagerAdapter {
    FragmentManager fm;
    private List<Fragment> fragments;
    private List<String> titles;
    private FragmentTransaction mCurTransaction = null;

    public FragmentPagerItemAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
        this.titles = titles;
    }

    public FragmentPagerItemAdapter(FragmentManager fm, List<Fragment> fragments) {
        this(fm, fragments, null);
    }

    public void setDataSource(List<Fragment> fragments, List<String> titles) {
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
        return POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        if (mCurTransaction == null) {
            mCurTransaction = fm.beginTransaction();
        }
        mCurTransaction.remove((Fragment)object);
        mCurTransaction.commitNowAllowingStateLoss();
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

