package com.demo.mycommonapp.newpk;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.mycommonapp.R;
import com.pesonal.adsdk.remote.APIManager;

public class MainActivity3 extends AppCompatActivity {

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