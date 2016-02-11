package com.example.blah.mobilestudio.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * A version of Activity, where the lifecycle methods are protected
 * Created by Stephen on 11/02/2016.
 */
public abstract class SafeActivity extends AppCompatActivity{
    private Throwable onCreateError;
    private Throwable commonLogicError;
    private Throwable onResumeError;
    private Throwable onStartError;

    @Override
    protected final void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        try {
            // Common logic to all classes
        } catch (Throwable th){
            commonLogicError = th;
            Log.e("Common logic error", commonLogicError.getMessage());
        } try {
            onActivityCreate(savedInstanceState);
        } catch (Throwable th){
            onCreateError = th;
            Log.e("On create error", onCreateError.getMessage());
        }

    }

    @Override
    protected final void onResume(){
        super.onResume();
        try {
            // Common logic to all classes
        } catch (Throwable th){
            commonLogicError = th;
            Log.e("Common logic error", commonLogicError.getMessage());
        } try {
            onActivityResume();
        } catch (Throwable th){
            onResumeError = th;
            Log.e("On resume error", onResumeError.getMessage());
        }
    }

    @Override
    protected final void onStart(){
        super.onStart();
        try {
            // Common logic to all classes
        } catch (Throwable th){
            commonLogicError = th;
            Log.e("Common logic error", commonLogicError.getMessage());
        } try {
            onActivityStart();
        } catch (Throwable th){
            onStartError = th;
            Log.e("On start error", onStartError.getMessage());
        }
    }

    /**
     * Safe version of onCreate()
     * @param savedInstanceState - the savedInstanceState passed from onCreate
     */
    protected abstract void onActivityCreate(Bundle savedInstanceState);

    /**
     * Safe version of onResume()
     */
    protected abstract void onActivityResume();

    /**
     * Safe version of onStart()
     */
    protected abstract void onActivityStart();
}
