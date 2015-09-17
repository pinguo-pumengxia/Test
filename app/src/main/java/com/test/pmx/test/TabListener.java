package com.test.pmx.test;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

/**
 * Created by ws-pumengxia on 15-9-8.
 */
public class TabListener<T extends Fragment> implements ActionBar.TabListener {
    private Fragment mFragment;
    private Activity mActivity;
    private String mTag;
    private Class<T> mClass;

    public TabListener(Activity mActivity, String mTag, Class<T> mClass) {
        this.mActivity = mActivity;
        this.mTag = mTag;
        this.mClass = mClass;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        if (mFragment == null){
            mFragment = Fragment.instantiate(mActivity,mClass.getName());
            ft.add(android.R.id.content,mFragment,mTag);
        }else{
            ft.attach(mFragment);
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        if(mFragment != null){
            ft.detach(mFragment);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
