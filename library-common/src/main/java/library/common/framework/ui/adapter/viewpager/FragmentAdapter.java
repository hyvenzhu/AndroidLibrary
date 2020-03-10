package library.common.framework.ui.adapter.viewpager;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * FragmentStatePagerAdapter适配器
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {
    FragmentManager fm;
    private List<Fragment> fragments;
    private List<String> titles;

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
        super(fm);
        this.fm = fm;
        this.fragments = fragments;
        this.titles = titles;
    }

    public FragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
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
        if (position >= 0 && position < getCount()) {
            return fragments.get(position);
        } else {
            return null;
        }
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    @Override
    public int getItemPosition(Object object) {
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
