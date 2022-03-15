package com.pesonal.adsdk.customAd;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pesonal.adsdk.R;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.model.AdvertiseList;


public class CustomIntAds extends Dialog {
    private LinearLayout LLTop;
    public Activity mContext;
    public OnCloseListener listener_positive;
    private ImageView ad_media_view;
    private RelativeLayout int_bg;
    private TextView txt_title;
    private TextView txt_body;
    private TextView txt_rate;
    private TextView txt_download;
    AdvertiseList advertiseList;


    public CustomIntAds(@NonNull Activity context, AdvertiseList customAdModel) {
        super(context);
        this.mContext = context;
        this.advertiseList = customAdModel;
    }

    public interface OnCloseListener {
        void onAdsCloseClick();

        void setOnKeyListener();
    }


    public CustomIntAds setOnCloseListener(OnCloseListener onCloseListener) {
        this.listener_positive = onCloseListener;
        return this;
    }

    @SuppressLint("WrongConstant")
    public Point screen_size_get(Context context) {
        Point point = new Point();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getSize(point);
        return point;
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cust_int);

        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = screen_size_get(getContext()).x;
        attributes.height = screen_size_get(getContext()).y;
        attributes.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        getWindow().setAttributes(attributes);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mContext.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        if (advertiseList != null) {
            try {
                ad_media_view = (ImageView) findViewById(R.id.native_ad_media);
                txt_title = (TextView) findViewById(R.id.native_ad_title);
                txt_body = (TextView) findViewById(R.id.native_ad_social_context);
                txt_rate = (TextView) findViewById(R.id.txt_rate);
                txt_download = (TextView) findViewById(R.id.txt_download);
                int_bg = findViewById(R.id.int_bg);
                Glide
                        .with(mContext)
                        .load(advertiseList.getApp_logo())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into((ImageView) findViewById(R.id.native_ad_icon));


                Glide
                        .with(mContext)
                        .load(advertiseList.getApp_banner())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ad_media_view
                        );

                txt_title.setText(advertiseList.getApp_name());
                txt_body.setText(advertiseList.getApp_shortDecription());
                txt_rate.setText(advertiseList.getApp_rating());
                txt_download.setText(advertiseList.getApp_download());

                findViewById(R.id.native_ad_call_to_action).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {

                        String action_str = advertiseList.getApp_packageName();
                        if (action_str.contains("http")) {
                            Uri marketUri = Uri.parse(action_str);
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                            mContext.startActivity(marketIntent);
                        } else {
                            mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + action_str)));
                        }

                    }
                });

                findViewById(R.id.ImgClose).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        OnCloseListener onCloseListener = listener_positive;
                        if (onCloseListener != null) {
                            onCloseListener.onAdsCloseClick();
                        }
                    }
                });

                APIManager.count_custIntAd++;
            } catch (Exception e) {
                OnCloseListener onCloseListener = listener_positive;
                if (onCloseListener != null) {
                    onCloseListener.onAdsCloseClick();
                }
            }
        } else {
            OnCloseListener onCloseListener = listener_positive;
            if (onCloseListener != null) {
                onCloseListener.onAdsCloseClick();
            }
        }

        setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                OnCloseListener onCloseListener = listener_positive;
                if (onCloseListener != null) {
                    onCloseListener.onAdsCloseClick();
                }
                return false;
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
