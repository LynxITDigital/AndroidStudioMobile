package com.example.blah.mobilestudio.Resizer;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mattgale on 2/02/2016.
 */
public class LocationState {
    String TAG = LocationState.class.getSimpleName();
    View firstView, thirdView;
    ViewGroup.LayoutParams firstParams, thirdParams;
    boolean isHorizontal;
    Coordinate previousCoordinate;
    SizableRegion region;

    /**
     *
     * @param region contains the minimum and maximum values for resizing (absolute value for the screen getRawX() and getRawY()).
     *               use X values if isHorizontal is true.
     *               use Y values is isHorizontal is false.
     * @param firstView left view in horizontal space. top view vertical space.
     * @param thirdView right view in horizontal space. bottom view vertical space.
     * @param isHorizontal this only works in 2 dimension at a time (left <-> right or top <-> bottom).
     * @param event the raw motion event from the onTouchListener
     */
    public LocationState(
            SizableRegion region,
            View firstView, View thirdView,
            boolean isHorizontal,
            MotionEvent event) {

        this.region = region;

        this.firstView = firstView;
        this.thirdView = thirdView;

        firstParams = firstView.getLayoutParams();
        thirdParams = thirdView.getLayoutParams();

        previousCoordinate = new Coordinate(event.getRawX(), event.getRawY());

        this.isHorizontal = isHorizontal;
    }

    /**
     * Coordinate system that's only currently used in this class.
     */
    class Coordinate {
        float x, y;

        public Coordinate(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    /**
     * movement from the ontouchlistener.
     * sets the layout params of the first and third view.
     * It just sets the width and height, it does not move elements via x,y.
     * @param motionEvent motion event from the users input.
     */
    public void move(MotionEvent motionEvent) {

        float amount;

        if(isHorizontal) {
            // prevent movement below a certain minimum amount or maximum.
            Log.d(TAG, "move: motionEvent.getRawX() " + motionEvent.getRawX() + " region.minX " + region.minX + " region.maxX " + region.maxX);
            if (motionEvent.getRawX() > region.minX && motionEvent.getRawX() < region.maxX ) {
                Log.d(TAG, "move: moved left");
                amount = previousCoordinate.x - motionEvent.getRawX();
                firstParams.width = Math.round(firstParams.width - amount);
                thirdParams.width = Math.round(thirdParams.width + amount);
            }
        } else {
            if (motionEvent.getRawY() > region.minY && motionEvent.getRawY() < region.maxY ) {
//                Log.d(TAG, "move: motionEvent.getRawY() " + motionEvent.getRawY());
                amount = previousCoordinate.y - motionEvent.getRawY();

                firstParams.height = Math.round(firstParams.height - amount);
                thirdParams.height = Math.round(thirdParams.height + amount);
            }
        }

        firstView.setLayoutParams(firstParams);
        thirdView.setLayoutParams(thirdParams);

        previousCoordinate = new Coordinate(motionEvent.getRawX(), motionEvent.getRawY());
    }
}
