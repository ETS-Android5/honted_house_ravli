package com.demo.mycommonapp.newpk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.mycommonapp.R;
import com.pesonal.adsdk.remote.APIManager;

public class MainAAActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnNext;
    private RelativeLayout adContainer;
    private RelativeLayout adContainer1;
    private RelativeLayout adContainer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_a);

        initView();
    }

    private void initView() {
        btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        adContainer = (RelativeLayout) findViewById(R.id.adContainer);
        adContainer1 = (RelativeLayout) findViewById(R.id.adContainer1);
        adContainer2 = (RelativeLayout) findViewById(R.id.adContainer2);
        APIManager.getInstance(MainAAActivity.this).showNative(adContainer);
        APIManager.getInstance(MainAAActivity.this).showSmallNative(adContainer1);
        APIManager.getInstance(MainAAActivity.this).showNative(adContainer2);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnNext:
                APIManager.getInstance(MainAAActivity.this).showAds(false, () -> {
                    startActivity(new Intent(MainAAActivity.this, MainActivity2.class));
                });

                break;
        }
    }

    @Override
    public void onBackPressed() {
        APIManager.getInstance(MainAAActivity.this).showAds(true, () -> {
            startActivity(new Intent(MainAAActivity.this, ExitActivity.class));
        });

    }
}