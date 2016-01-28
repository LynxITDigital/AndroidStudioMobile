package com.example.blah.mobilestudio.breadcrumbview;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.blah.mobilestudio.FolderStructureFragment;
import com.example.blah.mobilestudio.R;

/**
 * Created by alit on 25/01/2016.
 */
public class BreadcrumbFragment extends Fragment {

    public ArrayList<String> breadcrumbItems = new ArrayList<>();
    public String currentPath = "";
    OnItemSelectedListener mListener;
    View v ;
    BreadcrumbView breadCrumb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        v =  inflater.inflate(R.layout.breadcrumb_fragment, container, false);
        breadCrumb =  (BreadcrumbView)v.findViewById(R.id.bread_bar);
        return  v;
    }

    @Override
    public void onResume(){
        super.onResume();

        if(currentPath.length() > 0) {
            ArrayList<String> items = new ArrayList(Arrays.asList(currentPath.substring(1).split("\\s*/\\s*")));
            breadCrumb.SetElements(items);
            if (breadCrumb.mListener != null)
                breadCrumb.setOnClickListener(mListener);
        }
    }

    public void setOnClickListener(OnItemSelectedListener onItemSelectedListener) {
        mListener  = onItemSelectedListener;

        breadCrumb.setOnClickListener(onItemSelectedListener);
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