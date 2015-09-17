package com.test.pmx.test;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class SecondActivity extends ActionBarActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private SimpleFragmentPagerAdapter simpleFragmentPagerAdapter;
    private List<String> mTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mTitles = new ArrayList<>();
        mTitles.add("春天");
        mTitles.add("夏天");
        mTitles.add("秋天");
        mTitles.add("冬天");
        simpleFragmentPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(),this,mTitles);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(simpleFragmentPagerAdapter);

        tabLayout = (TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);//系统默认模式

    }

}
