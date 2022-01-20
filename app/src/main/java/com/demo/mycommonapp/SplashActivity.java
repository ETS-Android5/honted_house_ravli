package com.demo.mycommonapp;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.anchorfree.partner.api.auth.AuthMethod;
import com.anchorfree.partner.api.response.User;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.vpnsdk.callbacks.Callback;
import com.anchorfree.vpnsdk.exceptions.VpnException;
import com.pesonal.adsdk.AppClass;
import com.pesonal.adsdk.BaseAdsActivity;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.utils.SplashListner;
import com.pesonal.adsdk.utils.getDataListner;

import org.json.JSONObject;

public class SplashActivity extends BaseAdsActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initializeSplash(SplashActivity.this, new SplashListner() {
            @Override
            public void onSuccess() {
                APIManager.getInstance(SplashActivity.this).showSplashAD(SplashActivity.this, () -> {
                    Intent intent = new Intent(SplashActivity.this, StartSecondActivity.class);
                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onExtra(String extraData) {

            }
        });
    }
}