package com.test.pmx.test;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by ws-pumengxia on 15-9-9.
 */
public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<String> tabTitles;
    private Context context;

    public SimpleFragmentPagerAdapter(FragmentManager fm,Context context,List<String> titles){
        super(fm);
        this.context = context;
        this.tabTitles = titles;
    }

    @Override
    public int getCount() {
        return tabTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return ArtistFragment.newInstance(position+1);
    }
}
