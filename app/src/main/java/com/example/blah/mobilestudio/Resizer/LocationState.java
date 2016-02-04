package com.example.blah.mobilestudio.Resizer;

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

    public LocationState(View firstView, View thirdView, boolean isHorizontal, MotionEvent event) {
        this.firstView = firstView;
        this.thirdView = thirdView;

        firstParams = firstView.getLayoutParams();
        thirdParams = thirdView.getLayoutParams();

        previousCoordinate = new Coordinate(event.getRawX(), event.getRawY());

        this.isHorizontal = isHorizontal;
    }

    class Coordinate {
        float x, y;

        public Coordinate(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    public void move(MotionEvent motionEvent) {

        StringBuilder logString = new StringBuilder();

        float amount = 0.0f;

        if(isHorizontal) {
            logString.append(" prev x ")
                    .append(previousCoordinate.x)
                    .append(" x ")
                    .append(motionEvent.getRawX());

            amount = previousCoordinate.x - motionEvent.getRawX();

            firstParams.width = Math.round(firstParams.width - amount);
            thirdParams.width = Math.round(thirdParams.width + amount);
        } else {
            logString.append(" prev y ")
                    .append(previousCoordinate.y)
                    .append(" y ")
                    .append(motionEvent.getRawY())
                    .append(" direction vertically ");

            amount = previousCoordinate.y - motionEvent.getRawY();

            firstParams.height = Math.round(firstParams.height - amount);
            thirdParams.height = Math.round(thirdParams.height + amount);
        }

        firstView.setLayoutParams(firstParams);
        thirdView.setLayoutParams(thirdParams);

        logString.append(" amount ")
                .append(amount);
//        Log.d(TAG, "move: " + logString.toString());
        previousCoordinate = new Coordinate(motionEvent.getRawX(), motionEvent.getRawY());
    }
}
