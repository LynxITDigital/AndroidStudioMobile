package com.example.blah.mobilestudio.Resizer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.blah.mobilestudio.Activities.MainActivity;
import com.example.blah.mobilestudio.FileViewer.FileFragment;
import com.example.blah.mobilestudio.FolderStructureFragment;
import com.example.blah.mobilestudio.R;

/**
 * Created by mattgale on 2/02/2016.
 */
public class HorizontalResizerFragment extends Fragment {

    String TAG = HorizontalResizerFragment.class.getSimpleName();
    private View resizerView;
    private Fragment leftFragment, rightFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.horizontal_resizer, container, false);
        resizerView = rootView.findViewById(R.id.resizer_view);

        resizerView.setOnTouchListener(touchListener);
        return rootView;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity mainActivity = (MainActivity)getActivity();
        leftFragment = mainActivity.getFolderStructureFragment();
        rightFragment = mainActivity.getFileFragment();
    }

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN: {
                    beginDrag(event);
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    continueDrag(event);
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    finishDrag(event);
                    break;
                }
                default: {
                    break;
                }
            }
            return true;
        }

    };

    LocationState locationState = null;

    private void beginDrag(MotionEvent motionEvent) {
        locationState = new LocationState(leftFragment, this, rightFragment, true);
        locationState.move(motionEvent);
//        Log.d(TAG, "beginDrag: x " + motionEvent.getX() + " y " + motionEvent.getY());

    }

    private void continueDrag(MotionEvent motionEvent) {
        locationState.move(motionEvent);
//        Log.d(TAG, "continueDrag: x " + motionEvent.getX() + " y " + motionEvent.getY());
    }

    private void finishDrag(MotionEvent motionEvent) {
        locationState = null;
//        Log.d(TAG, "finishDrag: x " + motionEvent.getX() + " y " + motionEvent.getY());
    }
}
