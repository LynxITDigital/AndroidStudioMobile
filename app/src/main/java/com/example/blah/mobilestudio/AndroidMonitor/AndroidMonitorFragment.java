package com.example.blah.mobilestudio.AndroidMonitor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.blah.mobilestudio.R;

/**
 * Created by mattgale on 1/02/2016.
 */
public class AndroidMonitorFragment extends Fragment {

    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    View fragmentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.android_monitor_fragment, container, false);
        pagerAdapter = new AndroidMonitorPagerAdapter(getChildFragmentManager(), getActivity());
        viewPager = (ViewPager) fragmentView.findViewById(R.id.android_monitor_pager);
        if(viewPager != null) {
            viewPager.setAdapter(pagerAdapter);
        }
        return fragmentView;
    }
}