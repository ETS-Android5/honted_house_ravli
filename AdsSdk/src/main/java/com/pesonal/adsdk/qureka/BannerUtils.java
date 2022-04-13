package com.pesonal.adsdk.qureka;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pesonal.adsdk.R;
import com.pesonal.adsdk.remote.AdvertisementState;
import com.pesonal.adsdk.remote.InterCallback;

public class BannerUtils {

    public static void banner(ViewGroup viewGroup, Activity context, InterCallback interCallback) {
        if (interCallback != null) {
            interCallback.onClose(AdvertisementState.QUREKA_BANNER_AD_SHOW);
        }
        Adsresponse e8 = Glob.dataset(context);
        LayoutInflater from = LayoutInflater.from(context);
        View inflate = from.inflate(R.layout.qurea_tiny_banner, viewGroup, false);
        ImageView imageView3 = (ImageView) inflate.findViewById(R.id.iv_banner);
        Glide.with(context).load("file:///android_asset/"+e8.banner).into(imageView3);
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (e8.f18654b.contains("http")) {
                    Glob.b(context, e8.f18654b);
                    return;
                }
                StringBuilder h8 = Glob.h("market://details?id=");
                h8.append(e8.f18654b);
                Glob.b(context, h8.toString());
                context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(h8.toString())));
            }
        });
        viewGroup.removeAllViews();
        viewGroup.addView(inflate);
    }
}
