package com.example.blah.mobilestudio.activities;

import android.os.Bundle;

/**
 * Created by Stephen on 11/02/2016.
 * Intermediate class for SafeActivity that defines all of the lifecycle methods as empty methods.
 * This means that subclasses of SafeIntermediateActivity will not need to implement all of the
 * lifecycle methods, just the ones they need.
 */
public abstract class SafeIntermediateActivity extends SafeActivity {
    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {

    }

    @Override
    protected void onActivityResume() {

    }

    @Override
    protected void onActivityStart() {

    }

    @Override
    protected void onActivityRestart() {

    }

    @Override
    protected void onActivityStop() {

    }

    @Override
    protected void onActivityPause() {

    }

    @Override
    protected void onActivityDestroy() {

    }
}
