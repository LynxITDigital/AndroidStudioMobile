package com.example.blah.mobilestudio.Resizer;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.blah.mobilestudio.R;

/**
 * Created by mattgale on 2/02/2016.
 */
public class ResizerFragment extends Fragment {

    String TAG = ResizerFragment.class.getSimpleName();
    private View leftView, middleView, rightView, resizerView;
    private boolean isHorizontal;

    public static final String FIRST_VIEW_BUNDLE_KEY = "LEFTVIEW";
    public static final String THIRD_VIEW_BUNDLE_KEY = "RIGHTVIEW";

    public static final String IS_HORIZONTAL = "IS_HORIZONTAL";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        Activity activity = getActivity();

        Bundle bundle = getArguments();
        isHorizontal = bundle.getBoolean(IS_HORIZONTAL);

        View rootView;
        if(isHorizontal) {
            rootView = inflater.inflate(R.layout.horizontal_resizer, container, false);
            resizerView = rootView.findViewById(R.id.resizer_horizontal_view);
        } else {
            rootView = inflater.inflate(R.layout.vertical_resizer, container, false);
            resizerView = rootView.findViewById(R.id.resizer_vertical_view);
        }

        int firstKey =  bundle.getInt(FIRST_VIEW_BUNDLE_KEY, 0);
        int thirdKey = bundle.getInt(THIRD_VIEW_BUNDLE_KEY, 0);

        if(firstKey == 0 || thirdKey == 0) {
            return rootView;
        }

        resizerView.setOnTouchListener(touchListener);

        leftView = activity.findViewById(firstKey);
        rightView = activity.findViewById(thirdKey);
        return rootView;
    }

    LocationState locationState = null;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: {
                    locationState = new LocationState(leftView, rightView, isHorizontal, event);
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    locationState.move(event);
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    locationState = null;
                    break;
                }
                default: {
                    break;
                }
            }
            return true;
        }
    };
}
