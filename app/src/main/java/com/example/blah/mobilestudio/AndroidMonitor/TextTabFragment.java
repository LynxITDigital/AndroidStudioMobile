
package com.example.blah.mobilestudio.AndroidMonitor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blah.mobilestudio.R;

/**
 * Created by mattgale on 1/02/2016.
 */
public class TextTabFragment extends Fragment {

    public static final String BUNDLE_TEXT = "TEXT";
    public static final String TAG = TextTabFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle args = getArguments();
        View rootView = inflater.inflate(R.layout.text, container, false);

        TextView tv = (TextView) rootView.findViewById(R.id.text_view);

        tv.setText(args.getString(BUNDLE_TEXT));

        return rootView;
    }
}
