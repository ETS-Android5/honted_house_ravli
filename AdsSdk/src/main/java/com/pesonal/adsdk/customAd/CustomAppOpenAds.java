package com.pesonal.adsdk.customAd;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.pesonal.adsdk.R;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.remote.TinyDB;
import com.pesonal.adsdk.model.AdvertiseList;


public class CustomAppOpenAds extends Dialog  {
    public Context mContext;
    public OnCloseListener listener_positive;

    private TextView txt_title;
    private TextView txt_context;
    private TextView txt_rate;
    private TextView txt_download;

    private LinearLayout ll_continue_app;
    private ImageView iv_myapp_logo;
    private ImageView media_view;
    private ImageView iv_ad_icon;
    private TextView btn_call_to_action;

    public  SharedPreferences mysharedpreferences;
    private TextView txt_myapp_name;
    CustomAdModel customAdModel;
    AdvertiseList advertiseList;

    public CustomAppOpenAds(@NonNull Context context) {
        super(context);
        this.mContext = context;

    }

    public CustomAppOpenAds(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;

    }

    public CustomAppOpenAds(@NonNull Context context, int themeResId,CustomAdModel customAdModel) {
        super(context, themeResId);
        this.mContext = context;
        this.customAdModel = customAdModel;
    }

    public CustomAppOpenAds(@NonNull Context context, int themeResId, AdvertiseList customAdModel) {
        super(context, themeResId);
        this.mContext = context;
        this.advertiseList = customAdModel;
    }

    protected CustomAppOpenAds(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public interface OnCloseListener {
        void onAdsCloseClick();

        void setOnKeyListener();
    }


    public CustomAppOpenAds setOnCloseListener(OnCloseListener onCloseListener) {
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

        setContentView(R.layout.cust_appopen);

        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.width = screen_size_get(getContext()).x;
        attributes.height = screen_size_get(getContext()).y;
        attributes.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        getWindow().setAttributes(attributes);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        if(customAdModel!=null)
        {
            try
            {
                ll_continue_app = findViewById(R.id.ll_continue_app);
                iv_myapp_logo = findViewById(R.id.iv_myapp_logo);
                txt_myapp_name = findViewById(R.id.txt_myapp_name);
                media_view = (ImageView) findViewById(R.id.media_view);
                txt_title = (TextView) findViewById(R.id.txt_appname);
                iv_ad_icon = findViewById(R.id.iv_ad_icon);
                txt_rate = (TextView) findViewById(R.id.txt_rate);
                txt_download = (TextView) findViewById(R.id.txt_download);
                txt_context = (TextView) findViewById(R.id.txt_context);
                btn_call_to_action = findViewById(R.id.btn_call_to_action);


                mysharedpreferences = mContext.getSharedPreferences(mContext.getPackageName(), Context.MODE_PRIVATE);

                txt_myapp_name.setText(mysharedpreferences.getString("app_name",""));
                if(!mysharedpreferences.getString("app_logo","").isEmpty())
                {
                    Glide
                            .with(mContext)
                            .load(mysharedpreferences.getString("app_logo",""))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into((ImageView) findViewById(R.id.iv_myapp_logo));
                }

                Glide
                        .with(mContext)
                        .load(customAdModel.getApp_banner())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(media_view);

                txt_title.setText(customAdModel.getApp_name());
                txt_context.setText(customAdModel.getApp_shortDecription());
                txt_rate.setText(customAdModel.getApp_rating());
                txt_download.setText(customAdModel.getApp_download());

                Glide
                        .with(mContext)
                        .load(customAdModel.getApp_logo())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(iv_ad_icon);




                btn_call_to_action.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String action_str = customAdModel.getApp_packageName();
                        if (action_str.contains("http")) {
                            Uri marketUri = Uri.parse(action_str);
                            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                            mContext.startActivity(marketIntent);
                        } else {
                            mContext.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + action_str)));
                        }
                    }
                });


                ll_continue_app.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OnCloseListener onCloseListener = listener_positive;
                        if (onCloseListener != null) {
                            onCloseListener.onAdsCloseClick();
                        }
                    }
                });
                APIManager.count_custAppOpenAd++;
            }catch (Exception e)
            {
                OnCloseListener onCloseListener = listener_positive;
                if (onCloseListener != null) {
                    onCloseListener.onAdsCloseClick();
                }
            }
        }else if(advertiseList!=null)
        {
            try
            {
                ll_continue_app = findViewById(R.id.ll_continue_app);
                iv_myapp_logo = findViewById(R.id.iv_myapp_logo);
                txt_myapp_name = findViewById(R.id.txt_myapp_name);
                media_view = (ImageView) findViewById(R.id.media_view);
                txt_title = (TextView) findViewById(R.id.txt_appname);
                iv_ad_icon = findViewById(R.id.iv_ad_icon);
                txt_rate = (TextView) findViewById(R.id.txt_rate);
                txt_download = (TextView) findViewById(R.id.txt_download);
                txt_context = (TextView) findViewById(R.id.txt_context);
                btn_call_to_action = findViewById(R.id.btn_call_to_action);



                txt_myapp_name.setText(new TinyDB(mContext).getString("app_name"));
                if(!new TinyDB(mContext).getString("app_logo").isEmpty())
                {
                    Glide
                            .with(mContext)
                            .load(new TinyDB(mContext).getString("app_logo"))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into((ImageView) findViewById(R.id.iv_myapp_logo));
                }

                Glide
                        .with(mContext)
                        .load(advertiseList.getApp_banner())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(media_view);

                txt_title.setText(advertiseList.getApp_name());
                txt_context.setText(advertiseList.getApp_shortDecription());
                txt_rate.setText(advertiseList.getApp_rating());
                txt_download.setText(advertiseList.getApp_download());

                Glide
                        .with(mContext)
                        .load(advertiseList.getApp_logo())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(iv_ad_icon);




                btn_call_to_action.setOnClickListener(new View.OnClickListener() {
                    @Override
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


                ll_continue_app.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        OnCloseListener onCloseListener = listener_positive;
                        if (onCloseListener != null) {
                            onCloseListener.onAdsCloseClick();
                        }
                    }
                });
                APIManager.count_custAppOpenAd++;

            }catch (Exception e)
            {
                OnCloseListener onCloseListener = listener_positive;
                if (onCloseListener != null) {
                    onCloseListener.onAdsCloseClick();
                }
            }
        }
        else
        {
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
