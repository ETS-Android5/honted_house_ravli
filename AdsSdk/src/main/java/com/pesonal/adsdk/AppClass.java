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

import de.blinkt.openvpn.core.OpenVPNService;

public class AppClass extends Application
        implements ActivityLifecycleCallbacks, LifecycleObserver {

    private Activity currentActivity;
    public static final String SHARED_PREFS = "NORTHGHOST_SHAREDPREFS";
    public static final String STORED_CARRIER_ID_KEY = "com.northghost.afvclient.CARRIER_ID_KEY";
    public static final String STORED_HOST_URL_KEY = "com.northghost.afvclient.STORED_HOST_KEY";
    Class aClass;

    public void setClass(Class aClass) {
        this.aClass = aClass;
        OpenVPNService.setNotificationActivityClass(aClass);
    }


    @Override
    public void onCreate() {
        super.onCreate();
//        initHydraSdk();

        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(this, config);

        this.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

//    public void initHydraSdk() {
//        createNotificationChannel();
//        SharedPreferences prefs = getPrefs();
//        UnifiedSDK.getInstance(ClientInfo.newBuilder().baseUrl(prefs.getString(STORED_HOST_URL_KEY, "https://d2isj403unfbyl.cloudfront.net")).carrierId(prefs.getString(STORED_CARRIER_ID_KEY, "samuy_vpn22")).build(), UnifiedSDKConfig.newBuilder().build());
//        UnifiedSDK.update(NotificationConfig.newBuilder().title(getResources().getString(R.string.app_name)).channelId("vpn").build());
//        UnifiedSDK.setLoggingLevel(2);
//    }

//    public void setNewHostAndCarrier(String str, String str2) {
//        SharedPreferences prefs = getPrefs();
//        if (TextUtils.isEmpty(str)) {
//            prefs.edit().remove(STORED_HOST_URL_KEY).apply();
//        } else {
//            prefs.edit().putString(STORED_HOST_URL_KEY, str).apply();
//        }
//        if (TextUtils.isEmpty(str2)) {
//            prefs.edit().remove(STORED_CARRIER_ID_KEY).apply();
//        } else {
//            prefs.edit().putString(STORED_CARRIER_ID_KEY, str2).apply();
//        }
//        initHydraSdk();
//    }

    public SharedPreferences getPrefs() {
        return getSharedPreferences(SHARED_PREFS, 0);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel("vpn", "Sample VPN", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("VPN notification");
            ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(notificationChannel);
        }
    }


    @OnLifecycleEvent(Event.ON_START)
    protected void onMoveToForeground() {
        if (currentActivity != null)
            if (APIManager.getApp_adShowStatus() == 1 && !APIManager.getInstance(currentActivity).getQureka())
                if (aClass != null) {
                    if (APIManager.isLog)
                        Log.e("TAG", "onMoveToForeground: " + aClass.getName() + "  " + currentActivity.getLocalClassName());
                    if (!aClass.getName().contains(currentActivity.getLocalClassName()))
                        APIManager.getInstance(currentActivity).showOpenCall(currentActivity, () -> {
                        });
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
