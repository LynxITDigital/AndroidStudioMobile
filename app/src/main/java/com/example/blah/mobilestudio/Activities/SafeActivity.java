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
    private Throwable onRestartError;
    private Throwable onPauseError;
    private Throwable onStopError;
    private Throwable onDestroyError;

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

    @Override
    protected final void onRestart(){
        super.onRestart();
        try {
            // Common logic to all classes
        } catch (Throwable th){
            commonLogicError = th;
            Log.e("Common logic error", commonLogicError.getMessage());
        } try {
            onActivityRestart();
        } catch (Throwable th){
            onRestartError = th;
            Log.e("On restart error", onRestartError.getMessage());
        }
    }

    @Override
    protected final void onPause(){
        super.onPause();
        try {
            // Common logic to all classes
        } catch (Throwable th){
            commonLogicError = th;
            Log.e("Common logic error", commonLogicError.getMessage());
        } try {
            onActivityPause();
        } catch (Throwable th){
            onPauseError = th;
            Log.e("On pause error", onPauseError.getMessage());
        }
    }

    @Override
    protected final void onStop(){
        super.onStop();
        try {
            // Common logic to all classes
        } catch (Throwable th){
            commonLogicError = th;
            Log.e("Common logic error", commonLogicError.getMessage());
        } try {
            onActivityStop();
        } catch (Throwable th){
            onStopError = th;
            Log.e("On stop error", onStopError.getMessage());
        }
    }

    @Override
    protected final void onDestroy(){
        super.onDestroy();
        try {
            // Common logic to all classes
        } catch (Throwable th){
            commonLogicError = th;
            Log.e("Common logic error", commonLogicError.getMessage());
        } try {
            onActivityDestroy();
        } catch (Throwable th){
            onDestroyError = th;
            Log.e("On destroy error", onDestroyError.getMessage());
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

    /**
     * Safe version of onRestart()
     */
    protected abstract void onActivityRestart();

    /**
     * Safe version of onStop()
     */
    protected abstract void onActivityStop();

    /**
     * Safe version of onPause()
     */
    protected abstract void onActivityPause();

    /**
     * Safe version of onDestroy()
     */
    protected abstract void onActivityDestroy();
}
