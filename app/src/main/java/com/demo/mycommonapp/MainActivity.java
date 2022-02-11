package com.demo.mycommonapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.vpn.BannerVpnActivity;
import com.pesonal.adsdk.vpn.VanishVPNActivity;

public class MainActivity extends BannerVpnActivity implements View.OnClickListener {

    private Button btnNext;
    private RelativeLayout adContainer;
    private RelativeLayout adContainer1;
    private RelativeLayout adContainer2;
    private FrameLayout iVPN;
    private ConstraintLayout rootViewGuide;
    private LottieAnimationView guideVpn;
    private Button btnOpenVpnScreen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_a);
        initView();
    }

    private void initView() {
        btnOpenVpnScreen = (Button) findViewById(R.id.btnOpenVpnScreen);
        if(APIManager.getInstance(this).getVpnMenuStatus()){
            btnOpenVpnScreen.setVisibility(View.VISIBLE);
        }else btnOpenVpnScreen.setVisibility(View.GONE);

        iVPN = (FrameLayout) findViewById(R.id.iVPN);
        if (APIManager.getInstance(this).getVpnStatus()) {
            setBannerView(iVPN);
            rootViewGuide = (ConstraintLayout) findViewById(R.id.rootViewGuide);
            guideVpn = (LottieAnimationView) findViewById(R.id.guideVpn);
            guideVpn.setOnClickListener(view -> {
                connectVpn();
                rootViewGuide.setVisibility(View.GONE);
            });
            if (getConnection()) {
                rootViewGuide.setVisibility(View.GONE);
            } else {
                rootViewGuide.setVisibility(View.VISIBLE);
            }
        }
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        adContainer = (RelativeLayout) findViewById(R.id.adContainer);
        adContainer1 = (RelativeLayout) findViewById(R.id.adContainer1);
        adContainer2 = (RelativeLayout) findViewById(R.id.adContainer2);
        APIManager.getInstance(MainActivity.this).showNative(adContainer);
        APIManager.getInstance(MainActivity.this).showBanner(adContainer1);
        APIManager.getInstance(MainActivity.this).showSmallNative(adContainer2);
        btnOpenVpnScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, VanishVPNActivity.class));
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                APIManager.getInstance(MainActivity.this).showAds(false, () -> {
                    startActivity(new Intent(MainActivity.this, MainActivity2.class));
                });
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (rootViewGuide.getVisibility() == View.VISIBLE)
            return;
        if (APIManager.getInstance(this).isExitScreen()) {
            startActivity(new Intent(MainActivity.this, ExitActivity.class));
            finish();
        } else {
            APIManager.getInstance(MainActivity.this).showExitDialog();
        }

    }
}