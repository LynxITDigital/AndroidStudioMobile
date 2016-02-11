package com.example.blah.mobilestudio.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * A variant of activity, with lifecycle methods that cannot crash
 * Created by Stephen on 11/02/2016.
 */
public abstract class SafeActivity extends AppCompatActivity {
    private Throwable onCreateError;

    /**
     * The version of onCreate that the subclasses are allowed to use
     * @param savedInstanceState
     */
    protected abstract void onActivityCreate(Bundle savedInstanceState);
}
