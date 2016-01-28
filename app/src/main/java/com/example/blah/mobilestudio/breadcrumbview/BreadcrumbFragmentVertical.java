package com.example.blah.mobilestudio.breadcrumbview;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import com.example.blah.mobilestudio.R;

/**
 * Created by alit on 25/01/2016.
 */
public class BreadcrumbFragmentVertical extends Fragment {

    OnItemSelectedListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View v =  inflater.inflate(R.layout.breadcrumb_fragment_vertical, container, false);

        //TODO: Setup the vertical tab selector items
        ArrayList<String> li1 = new ArrayList<>();
        BreadcrumbView breadCrumb =  (BreadcrumbView)v.findViewById(R.id.bread_bar_vertical);
        li1.add("Project");
        li1.add("Structure");
        li1.add("Captures");
        breadCrumb.SetElements(li1);

        return  v;

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnItemSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnItemSelected");
        }
    }


}
