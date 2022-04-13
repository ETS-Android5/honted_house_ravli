package com.demo.mycommonapp;

import android.util.Log;

import com.pesonal.adsdk.AppClass;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.remote.AdvertisementState;

public class MyApp extends AppClass {


    @Override
    public void onState(AdvertisementState state) {
        Log.e("MyApp", "state: "+state );
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG)
            APIManager.setIsLog(true);
        setClass(SplashActivity.class);
    }
}
