package com.example.blah.mobilestudio.AndroidMonitor;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.blah.mobilestudio.R;

/**
 * Created by mattgale on 1/02/2016.
 */
public class AndroidMonitorPagerAdapter extends FragmentPagerAdapter {

    public static final String TAG = AndroidMonitorPagerAdapter.class.getSimpleName();
    public static final int PAGER_ELEMENTS = 5;

    private Context context;

    //tab element numbers. For future use in the getItem() function.
    public static final int LOGCAT_POSITION = 0;
    public static final int MEMORY_POSITION = 1;
    public static final int CPU_POSITION = 2;
    public static final int GPU_POSITION = 3;
    public static final int NETWORK_POSITION = 4;

    private int[] pagerTitleResources = {
        R.string.logcat,
        R.string.memory,
        R.string.cpu,
        R.string.gpu,
        R.string.network
    };

    private String[] pagerTitles;

    public AndroidMonitorPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        pagerTitles = new String[PAGER_ELEMENTS];
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment = null;
        fragment = new TextTabFragment();
        Bundle arguments = new Bundle();

        // initialise tab with empty contents.
        arguments.putString(TextTabFragment.BUNDLE_TEXT, "");

        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_ELEMENTS;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        if(pagerTitles[position] == null || pagerTitles[position].equals("")) {
            pagerTitles[position] = context.getResources().getString(pagerTitleResources[position]);
        }

        return pagerTitles[position];
    }
}

