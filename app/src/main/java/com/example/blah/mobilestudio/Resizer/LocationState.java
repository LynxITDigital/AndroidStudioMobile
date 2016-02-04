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
    View firstView, secondView, thirdView;
    ViewGroup.LayoutParams firstParams, secondParams, thirdParams;
    boolean isHorizontal;
    Coordinate previousCoordinate;
    MovementDirection direction = null;

    public LocationState(View firstView, View secondView, View thirdView, boolean isHorizontal, MotionEvent event) {
        this.firstView = firstView;
        this.secondView = secondView;
        this.thirdView = thirdView;

        firstParams = firstView.getLayoutParams();
        secondParams = secondView.getLayoutParams();
        thirdParams = thirdView.getLayoutParams();

        previousCoordinate = new Coordinate(event.getX(), event.getY());

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
                    .append(motionEvent.getX());

            amount = previousCoordinate.x - motionEvent.getX();
            moveHorizontally(amount);
        } else {
            amount = previousCoordinate.y - motionEvent.getY();
            moveVertically(amount);

            logString.append(" prev y ")
                    .append(previousCoordinate.y)
                    .append(" y ")
                    .append(motionEvent.getY())
                    .append(" direction vertically "));
        }

                .append(" amount ")
                .append(amount);
        Log.d(TAG, "move: " + logString.toString());
        previousCoordinate = new Coordinate(motionEvent.getX(), motionEvent.getY());

    }
    private void moveHorizontally(float offset) {
        StringBuilder logString = new StringBuilder();
            logString.append(" firstParams.width ")
                    .append(firstParams.width)
                    .append(" secondView.getX() ")
                    .append(secondView.getX())
                    .append(" thirdParams.width ")
                    .append(thirdParams.width)
                    .append(" firstView.getWidth() ")
                    .append(firstView.getWidth())
                    .append(" thirdView.getWidth() ")
                    .append(thirdView.getWidth());

        firstParams.width = Math.round(firstParams.width - offset);
        thirdParams.width = Math.round(thirdParams.width + offset);
        firstView.requestLayout();

//        Log.d(TAG, "moveLeft: " + logString.toString());
    }

    private void moveVertically(float offset) {
        StringBuilder logString = new StringBuilder();
        logString.append(" firstParams.height ")
                .append(firstParams.height)
                .append(" secondView.getY() ")
                .append(secondView.getY())
                .append(" thirdParams.height ")
                .append(thirdParams.height)
                .append(" firstView.getHeight() ")
                .append(firstView.getHeight())
                .append(" thirdView.getWidth() ")
                .append(thirdView.getHeight());

        firstParams.height = Math.round(firstParams.height + offset);
        thirdParams.height = Math.round(thirdParams.height - offset);
        firstView.requestLayout();

//        Log.d(TAG, "moveUp: " + logString.toString());
    }
}
