package com.example.blah.mobilestudio.breadcrumbview;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import com.example.blah.mobilestudio.R;

/**
 * Created by alit on 25/01/2016.
 */
public class BreadcrumbFragmentVertical extends Fragment {

    OnItemSelectedListener mListener;
    BreadcrumbView breadCrumb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View v =  inflater.inflate(R.layout.breadcrumb_fragment_vertical, container, false);

        ArrayList<String> verticalMenuItems = new ArrayList<>();
        breadCrumb =  (BreadcrumbView)v.findViewById(R.id.bread_bar_vertical);

        // Set items in the Vertical menu
        verticalMenuItems.add("Project");
        verticalMenuItems.add("Structure");
        verticalMenuItems.add("Captures");
        breadCrumb.SetElements(verticalMenuItems);
        return  v;
    }

    @Override
    public void onAttach(Activity activity) {
        // CHECK IF IT"S UNDER 23
        super.onAttach(activity);
        try {
            mListener = (OnItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnItemSelected");
        }
    }


    @Override
    public void onAttach(Context context) {
        // CHECK IF IT"S MOre THAN 23

        super.onAttach(context);
        try {
            mListener = (OnItemSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Z must implement OnItemSelected");
        }
    }

}
