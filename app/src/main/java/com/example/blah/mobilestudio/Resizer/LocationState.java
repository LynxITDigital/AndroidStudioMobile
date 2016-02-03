package com.example.blah.mobilestudio.Resizer;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mattgale on 2/02/2016.
 */
public class LocationState {
    String TAG = LocationState.class.getSimpleName();
    View leftView, middleView, rightView;
    ViewGroup.LayoutParams leftParams, middleParams, rightParams;
    boolean isHorizontal;

    public LocationState(Fragment leftFragment, Fragment middleFragment, Fragment rightFragment, boolean isHorizontal) {
        leftView = leftFragment.getView();
        middleView = middleFragment.getView();
        rightView = rightFragment.getView();

        leftParams = leftView.getLayoutParams();
        middleParams = middleView.getLayoutParams();
        rightParams = rightView.getLayoutParams();

        this.isHorizontal = isHorizontal;
    }

    class Coordinate {
        float x, y;

        public Coordinate(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    Coordinate previousCoordinate = null;

    public void move(MotionEvent motionEvent) {
        if (previousCoordinate == null) {
            previousCoordinate = new Coordinate(motionEvent.getX(), motionEvent.getY());
        } else {
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
//                moveRight(amount);
            }

            if (previousCoordinate.y > motionEvent.getY()) {
                direction = " down ";
                amount = previousCoordinate.y - motionEvent.getY();
//                moveDown(amount);
            } else if (previousCoordinate.y < motionEvent.getY()) {
                direction = " up ";
                amount = motionEvent.getY() - previousCoordinate.y;
//                moveUp(amount);
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
//            Log.d(TAG, "move: " + logString.toString());
            previousCoordinate = new Coordinate(motionEvent.getX(), motionEvent.getY());
        }
    }

    public void moveLeft(float offset) {
        if (isHorizontal) {
            StringBuilder logString = new StringBuilder();
            if (offset > 0) {
//                leftParams.width += offset;
//                middleView.setX(middleView.getX() + offset);
//                rightParams.width -= offset;
                logString.append(" leftParams.width ")
                        .append(leftParams.width)
                        .append(" middleView.getX() ")
                        .append(middleView.getX())
                        .append(" rightParams.width ")
                        .append(rightParams.width);
                logString.append(" leftView.getWidth() ")
                        .append(leftView.getWidth())
                        .append(" rightView.getWidth() ")
                        .append(rightView.getWidth());

                leftParams.width = Math.round(leftView.getWidth() + offset);
                leftView.requestLayout();
                middleView.setX(offset);
                rightParams.width = Math.round(rightView.getWidth() - offset);
                rightView.requestLayout();
            } else if (offset == 0) {
                //nothing to do
            } else {
                //perhaps it should be going the other way.
                offset *= -1;
//                moveRight(offset);
            }
            Log.d(TAG, "moveLeft: " + logString.toString());
        }
    }

    public void moveRight(float offset) {
        if (isHorizontal) {
            if (offset > 0) {
                leftParams.width -= offset;
                middleView.setX(middleView.getX() - offset);
                rightParams.width += offset;
            } else if (offset == 0) {
                //nothing to do
            } else {
                //perhaps it should be going the other way.
                offset *= -1;
                moveLeft(offset);
            }
        }
    }

    public void moveUp(float offset) {
        if (!isHorizontal) {
            if (offset > 0) {
                leftParams.height -= offset;
                middleView.setY(middleView.getY() - offset);
                rightParams.height += offset;
            } else if (offset == 0) {
                //nothing to do
            } else {
                //perhaps it should be going the other way.
                offset *= -1;
                moveDown(offset);
            }
        }
    }

    public void moveDown(float offset) {
        if (!isHorizontal) {
            if (offset > 0) {
                leftParams.height += offset;
                middleView.setY(middleView.getY() + offset);
                rightParams.height -= offset;
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
