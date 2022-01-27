package com.demo.mycommonapp;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.pesonal.adsdk.BaseActivity;
import com.pesonal.adsdk.remote.APIManager;

public class MainActivity3 extends BaseActivity {

    private RelativeLayout adContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        initView();
    }

    private void initView() {
        adContainer = (RelativeLayout) findViewById(R.id.adContainer);
        APIManager.getInstance(MainActivity3.this).showNative(adContainer);
    }

    @Override
    public void onBackPressed() {
        APIManager.getInstance(MainActivity3.this).showAds(true, this::finish);
    }
}