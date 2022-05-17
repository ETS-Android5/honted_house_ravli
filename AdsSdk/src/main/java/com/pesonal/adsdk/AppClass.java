package com.pesonal.adsdk;


import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.remote.AdvertisementState;

import java.util.ArrayList;

public abstract class AppClass extends Application
        implements ActivityLifecycleCallbacks, LifecycleObserver {

    private Activity currentActivity;
    Class aClass;
    ArrayList<Class> classes = new ArrayList<>();
    private String substring = "";

    public abstract void onState(AdvertisementState state);

    public void setClass(Class aClass) {
        this.aClass = aClass;
//        OpenVPNService.setNotificationActivityClass(aClass);
    }

    public void setMultipleClass(ArrayList<Class> aClass) {
        this.classes = aClass;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(this, config);

        this.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Event.ON_START)
    protected void onMoveToForeground() {
        if (currentActivity != null) {
            substring = currentActivity.getClass().getName();
            boolean adState = getAdState();
            if (APIManager.isLog)
                Log.e("TAG", "onMoveToForeground: " + substring + "  " + adState);
            if (adState && substring != null)
                if (APIManager.getApp_adShowStatus() == 1 && !APIManager.getInstance(currentActivity).getQureka())
                    APIManager.getInstance(currentActivity).showOpenCall(currentActivity, this::onState);
        }
    }

    public boolean getAdState() {
        if (aClass != null) {
            if (aClass.getName().equalsIgnoreCase(substring)) {
                return false;
            }
        }
        if (classes.size() > 0) {
            for (Class aClass : classes) {
                if (aClass.getName().equalsIgnoreCase(substring)) {
                    return false;
                }
            }
        }
        return true;
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
