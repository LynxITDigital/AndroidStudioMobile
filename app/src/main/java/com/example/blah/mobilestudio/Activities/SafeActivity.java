package com.example.blah.mobilestudio.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * A version of Activity, where the lifecycle methods are protected
 * Created by Stephen on 11/02/2016.
 */
public abstract class SafeActivity extends AppCompatActivity{
    private Throwable commonLogicError;

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
            Log.e("On create error", th.getMessage());
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
            Log.e("On resume error", th.getMessage());
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
            Log.e("On start error", th.getMessage());
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
            Log.e("On restart error", th.getMessage());
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
            Log.e("On pause error", th.getMessage());
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
            Log.e("On stop error", th.getMessage());
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
            Log.e("On destroy error", th.getMessage());
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
