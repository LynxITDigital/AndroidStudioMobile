package com.example.blah.mobilestudio.Resizer;

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

        String direction = "";
        float amount = 0.0f;

        if (previousCoordinate.x > motionEvent.getX()) {
            direction = " left ";
            amount = previousCoordinate.x - motionEvent.getX();
            moveLeft(amount);
        } else if (previousCoordinate.x < motionEvent.getX()) {
            direction = " right ";
            amount = motionEvent.getX() - previousCoordinate.x;
            moveRight(amount);
        }

        if (previousCoordinate.y > motionEvent.getY()) {
            direction = " down ";
            amount = previousCoordinate.y - motionEvent.getY();
            moveDown(amount);
        } else if (previousCoordinate.y < motionEvent.getY()) {
            direction = " up ";
            amount = motionEvent.getY() - previousCoordinate.y;
            moveUp(amount);
        }

        if (previousCoordinate.y == motionEvent.getY() &&
                previousCoordinate.x == motionEvent.getX()) {
            direction = " nothing ";
            amount = 0.0f;
        }
        logString.append(direction)
                .append(" prev x ")
                .append(previousCoordinate.x)
                .append(" prev y ")
                .append(previousCoordinate.y)
                .append(" x ")
                .append(motionEvent.getX())
                .append(" y ")
                .append(motionEvent.getY())
                .append(" amount ")
                .append(amount);
//        Log.d(TAG, "move: " + logString.toString());
        previousCoordinate = new Coordinate(motionEvent.getX(), motionEvent.getY());

    }

    private void moveLeft(float offset) {
        if (isHorizontal) {
            StringBuilder logString = new StringBuilder();
            if (offset > 0) {
                logString.append(" firstParams.width ")
                        .append(firstParams.width)
                        .append(" secondView.getX() ")
                        .append(secondView.getX())
                        .append(" thirdParams.width ")
                        .append(thirdParams.width);
                logString.append(" firstView.getWidth() ")
                        .append(firstView.getWidth())
                        .append(" thirdView.getWidth() ")
                        .append(thirdView.getWidth());

                firstParams.width = Math.round(firstView.getWidth() - offset);
                thirdParams.width = Math.round(thirdView.getWidth() + offset);
                firstView.requestLayout();
//                thirdView.requestLayout();
            } else if (offset == 0) {
                //nothing to do
            } else {
                //perhaps it should be going the other way.
                offset *= -1;
                moveRight(offset);
            }
//            Log.d(TAG, "moveLeft: " + logString.toString());
        }
    }

    private void moveRight(float offset) {
        if (isHorizontal) {
            if (offset > 0) {
                firstParams.width = Math.round(firstView.getWidth() + offset);
                thirdParams.width = Math.round(thirdView.getWidth() - offset);
                firstView.requestLayout();
            } else if (offset == 0) {
                //nothing to do
            } else {
                //perhaps it should be going the other way.
                offset *= -1;
                moveLeft(offset);
            }
        }
    }

    private void moveUp(float offset) {
        if (!isHorizontal) {
            if (offset > 0) {
                firstParams.height = Math.round(firstView.getHeight() + offset);
                thirdParams.height = Math.round(thirdView.getHeight() - offset);
                firstView.requestLayout();
            } else if (offset == 0) {
                //nothing to do
            } else {
                //perhaps it should be going the other way.
                offset *= -1;
                moveDown(offset);
            }
        }
    }

    private void moveDown(float offset) {
        if (!isHorizontal) {
            if (offset > 0) {
                firstParams.height = Math.round(firstView.getHeight() - offset);
                thirdParams.height = Math.round(thirdView.getHeight() + offset);
                firstView.requestLayout();
            } else if (offset == 0) {
                //nothing to do
            } else {
                //perhaps it should be going the other way.
                offset *= -1;
                moveUp(offset);
            }
        }
    }
}
