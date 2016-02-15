package com.example.blah.mobilestudio.breadcrumbview;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.blah.mobilestudio.R;

/**
 * Created by alit on 25/01/2016.
 */
public class BreadcrumbFragment extends Fragment {
    public String currentPath = "";
    OnItemSelectedListener mListener;
    View v;
    public BreadcrumbView breadCrumb;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.breadcrumb_fragment, container, false);
        breadCrumb = (BreadcrumbView) v.findViewById(R.id.bread_bar);
        if (savedInstanceState != null) {
            currentPath = savedInstanceState.getString("currentPath");
            breadCrumb.setRootPath(savedInstanceState.getString("rootPath"));
            breadCrumb.highlightedItem = savedInstanceState.getString("highlightedItem");
            breadCrumb.highlightedIndex = savedInstanceState.getInt("highlightedIndex");
        }
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        breadCrumb = (BreadcrumbView) v.findViewById(R.id.bread_bar);
        if (currentPath.length() > 0 && breadCrumb.getRootPath().length() > 0) {
            calculatePathAndSetTheListener(breadCrumb, currentPath);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentPath.length() > 0) {
            calculatePathAndSetTheListener(breadCrumb, currentPath);
        }
    }

    public void setOnClickListener(OnItemSelectedListener onItemSelectedListener) {
        mListener = onItemSelectedListener;
        breadCrumb.setOnClickListener(onItemSelectedListener);

        // Set the root path of the selected folder, with this operation only the last nested folder
        // would consider as path and the rest of the initial path would consider as the root path,
        // we need root path to communicate with tree-view structure
        ArrayList<String> items = new ArrayList(Arrays.asList(currentPath.substring(1).split("\\s*/\\s*")));
        for (int i = 0; i < items.size() - 1; i++)
            breadCrumb.setRootPath(breadCrumb.getRootPath() + "/" + items.get(i));
    }

    public void highlightSelectedItem(String item, int inx) {
        breadCrumb.highlightedItem = item;
        breadCrumb.highlightedIndex = inx;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context instanceof Activity) {
                doAttach((Activity) context);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            doAttach(activity);
        }
    }

    private void doAttach(Activity activity) {
        try {
            mListener = (OnItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement breadCrumb listener");
        }
    }

    public void calculatePathAndSetTheListener(BreadcrumbView breadCrumb, String currentPath) {
        // Remove the root folders path from the beginning of the selected path
        currentPath = currentPath.replace(breadCrumb.getRootPath(), "");
        // Retrive the sequence of the remaining folders in the path
        ArrayList<String> items = new ArrayList(Arrays.asList(currentPath.substring(1).split("\\s*/\\s*")));
        breadCrumb.SetElements(items);
        if (mListener != null)
            breadCrumb.setOnClickListener(mListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentPath", currentPath);
        outState.putString("rootPath", breadCrumb.getRootPath());
        outState.putString("highlightedItem", breadCrumb.highlightedItem);
        outState.putInt("highlightedIndex", breadCrumb.highlightedIndex);
    }
}
