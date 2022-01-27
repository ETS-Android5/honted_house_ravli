package com.pesonal.adsdk;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    public boolean hideNavigation = true;
    public boolean hideStatusBar = false;

    public void setHideStatusBar(boolean hideStatusBar) {
        this.hideStatusBar = hideStatusBar;
    }

    public void setHideNavigation(boolean hideNavigation) {
        this.hideNavigation = hideNavigation;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hideNavigation) {
            if (hasFocus) {
                getWindow().getDecorView()
                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                if (!hideStatusBar)
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            if (!hideStatusBar)
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
}