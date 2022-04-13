package com.demo.mycommonapp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pesonal.adsdk.BaseActivity;
import com.pesonal.adsdk.dialog.DialogUtils;
import com.pesonal.adsdk.dialog.PrivacyDialog;
import com.pesonal.adsdk.model.MOREAPPSPLASH;
import com.pesonal.adsdk.remote.APIManager;

import java.util.List;


public class StartSecondActivity extends BaseActivity implements View.OnClickListener {

    private Button ivPlayer;
    private ImageView ivShare;
    private ImageView ivPrivacy;
    private RelativeLayout adContainer;
    private RecyclerView recyclerApp;
    private TextView txtMoreApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHideStatusBar(false);
        setLightTheme(false);
        setContentView(R.layout.activity_start_second);
        initView();
    }

    private void initView() {
        txtMoreApp = (TextView) findViewById(R.id.txtMoreApp);
        ivPlayer = (Button) findViewById(R.id.ivPlayer);
        ivShare = (ImageView) findViewById(R.id.ivShare);
        ivPrivacy = (ImageView) findViewById(R.id.ivPrivacy);
        adContainer = (RelativeLayout) findViewById(R.id.adContainer);
        APIManager.getInstance(StartSecondActivity.this).showNative(adContainer);
        ivPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIManager.getInstance(StartSecondActivity.this).showAdsStart(StartSecondActivity.this, (state) -> {
                    Log.e("showAdsStart", "state: "+state );
                    Intent intent = new Intent(StartSecondActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
        });

        ivShare.setOnClickListener(this::onClick);
        ivPrivacy.setOnClickListener(this::onClick);
        recyclerApp = (RecyclerView) findViewById(R.id.recyclerApp);
        List<MOREAPPSPLASH> splash_more_data = APIManager.getInstance(StartSecondActivity.this).get_SPLASHMoreAppData();

        if (splash_more_data.size() > 0) {
            txtMoreApp.setVisibility(View.VISIBLE);
            recyclerApp.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            recyclerApp.setAdapter(new AppAdsAdapter(StartSecondActivity.this, splash_more_data));
        } else {
            txtMoreApp.setVisibility(View.INVISIBLE);
        }

        if (APIManager.getInstance(StartSecondActivity.this).isUpdate()) {
            new DialogUtils().showUpdateDialog(this, "https://play.google.com/store/apps/details?id=" + getPackageName());
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivShare:
                String app = getString(R.string.app_name);
                Intent share = new Intent("android.intent.action.SEND");
                share.setType("text/plain");
                share.putExtra("android.intent.extra.TEXT", app + "\n\n" + "Open this Link on Play Store" + "\n\n" + "https://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(Intent.createChooser(share, "Share Application"));
                break;
            case R.id.ivPrivacy:
                PrivacyDialog.show(StartSecondActivity.this, getResources().getStringArray(R.array.terms_of_service));
                return;

        }
    }

    @Override
    public void onBackPressed() {
    }


}