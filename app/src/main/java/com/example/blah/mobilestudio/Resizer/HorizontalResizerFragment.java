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
public class HorizontalResizerFragment extends Fragment {

    String TAG = HorizontalResizerFragment.class.getSimpleName();
    private View leftView, middleView, rightView, resizerView;

    public static final String LEFTVIEW_BUNDLE_KEY = "LEFTVIEW";
    public static final String MIDDLEVIEW_BUNDLE_KEY = "MIDDLEVIEW";
    public static final String RIGHTVIEW_BUNDLE_KEY = "RIGHTVIEW";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.horizontal_resizer, container, false);
        resizerView = rootView.findViewById(R.id.resizer_view);

        resizerView.setOnTouchListener(touchListener);

        Activity activity = getActivity();

        Bundle bundle = getArguments();
        int leftKey =  bundle.getInt(LEFTVIEW_BUNDLE_KEY, 0);
        int middleKey = bundle.getInt(MIDDLEVIEW_BUNDLE_KEY, 0);
        int rightKey = bundle.getInt(RIGHTVIEW_BUNDLE_KEY, 0);
        if(leftKey == 0 || middleKey == 0 || rightKey == 0) {
            return rootView;
        }

        leftView = activity.findViewById(leftKey);
        middleView = activity.findViewById(middleKey);
        rightView = activity.findViewById(rightKey);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    LocationState locationState = null;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: {
                    locationState = new LocationState(leftView, rightView, true, event);
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
