package com.demo.mycommonapp;


import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.pesonal.adsdk.remote.APIManager;

public class MyApplication extends Application
        implements ActivityLifecycleCallbacks, LifecycleObserver {

    private Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        this.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }


    @OnLifecycleEvent(Event.ON_START)
    protected void onMoveToForeground() {
        if (APIManager.getApp_adShowStatus() == 1 && !APIManager.getInstance(currentActivity).getQureka() && !SplashActivity.class.getName().contains(currentActivity.getLocalClassName()))
            APIManager.getInstance(currentActivity).showOpenCall(currentActivity, () -> {
            });
    }


    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
    }
}
