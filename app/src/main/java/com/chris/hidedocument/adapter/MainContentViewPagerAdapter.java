package com.chris.hidedocument.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.chris.hidedocument.model.TabInfoBean;

import java.util.ArrayList;


/**
 * Created by zoulx on 2015/8/18.
 */
public class MainContentViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<TabInfoBean> tabs = null;
    Context context = null;

    public MainContentViewPagerAdapter(Context context, FragmentManager fm, ArrayList<TabInfoBean> tabs) {
        super(fm);
        this.tabs = tabs;
        this.context = context;
    }

    @Override
    public Fragment getItem(int pos) {
        Fragment fragment = null;
        if (tabs != null && pos < tabs.size()) {
            TabInfoBean tab = tabs.get(pos);
            if (tab == null)
                return null;
            fragment = tab.createFragment();
        }
        return fragment;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        if (tabs != null && tabs.size() > 0)
            return tabs.size();
        return 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TabInfoBean tab = tabs.get(position);
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        tab.fragment = fragment;
        return fragment;
    }
}
