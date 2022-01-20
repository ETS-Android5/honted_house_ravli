package com.demo.mycommonapp;

import com.pesonal.adsdk.AppClass;
import com.pesonal.adsdk.remote.APIManager;

public class MyApp extends AppClass {

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG)
            APIManager.setIsLog(true);
        setClass(SplashActivity.class);
    }
}
