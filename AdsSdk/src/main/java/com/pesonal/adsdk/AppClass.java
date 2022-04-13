package com.pesonal.adsdk;


import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
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

import de.blinkt.openvpn.DisconnectVPNActivity;
import de.blinkt.openvpn.core.OpenVPNService;

public abstract class AppClass extends Application
        implements ActivityLifecycleCallbacks, LifecycleObserver {

    private Activity currentActivity;
    Class aClass;

    public abstract void onState(AdvertisementState state);

    public void setClass(Class aClass) {
        this.aClass = aClass;
        OpenVPNService.setNotificationActivityClass(aClass);
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
        if (currentActivity != null)
            if (APIManager.getApp_adShowStatus() == 1 && !APIManager.getInstance(currentActivity).getQureka())
                if (aClass != null) {
                    if (APIManager.isLog)
                        Log.e("TAG", "onMoveToForeground: " + aClass.getName() + "  " + currentActivity.getLocalClassName());
                    if (!aClass.getName().contains(currentActivity.getLocalClassName())) {
                        if (!DisconnectVPNActivity.class.getName().contains(currentActivity.getLocalClassName())) {
                            APIManager.getInstance(currentActivity).showOpenCall(currentActivity, this::onState);
                        }
                    }
                }
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
