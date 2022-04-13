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

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.pesonal.adsdk.R;
import com.pesonal.adsdk.remote.AdvertisementState;
import com.pesonal.adsdk.remote.InterCallback;
import com.pesonal.adsdk.remote.NativeCallback;


public class Nativeutils {

    public static int U = 0;
    private static String str4 = "";

    public static void mediam(ViewGroup viewGroup, Activity context, NativeCallback nativeCallback) {
        LayoutInflater layoutInflater = null;
        if (nativeCallback != null) {
            nativeCallback.onState(AdvertisementState.QUREKA_NATIVE_AD_SHOW);
        }
        Adsresponse e8 = Glob.dataset(context);
        LayoutInflater from = LayoutInflater.from(context);
        int i8 = R.layout.qurea_medium_native;

        View inflate;
        layoutInflater = LayoutInflater.from(context);
        inflate = from.inflate(R.layout.qurea_medium_native, viewGroup, false);

        inflate = layoutInflater.inflate(i8, viewGroup, false);
        ImageView imageView3 = (ImageView) inflate.findViewById(R.id.iv_banner);
        ImageView imageView22 = (ImageView) inflate.findViewById(R.id.iv_logo);
        TextView textView6 = (TextView) inflate.findViewById(R.id.tv_appname);
        CardView main_layout = (CardView) inflate.findViewById(R.id.main_layout);
        RatingBar ratingBar2 = (RatingBar) inflate.findViewById(R.id.ad_stars);
        TextView textView22 = (TextView) inflate.findViewById(R.id.tv_rating);
        TextView textView32 = (TextView) inflate.findViewById(R.id.tv_download);
        TextView textView42 = (TextView) inflate.findViewById(R.id.tv_desc);
        Button button2 = (Button) inflate.findViewById(R.id.btn_install);
        View view2 = inflate;
        Glide.with(context).load("file:///android_asset/" + e8.f18656d).into(imageView3);
        Glide.with(context).load("file:///android_asset/" + e8.f18655c).into(imageView22);
        String str4 = "";
        if (!e8.f18654b.contains("http")) {
            textView6.setText("Play & Win Coins Daily.");
//            linearLayout2.setVisibility(View.GONE);
            str4 = "Play Now";
        } else {
            textView6.setText(e8.f18653a.trim());
//            linearLayout2.setVisibility(View.VISIBLE);
            str4 = "Install";
        }
        button2.setText(str4);
        ratingBar2.setRating(Float.parseFloat(e8.f18658f));
        StringBuilder sb3 = new StringBuilder();
        sb3.append("(");
        sb3.append(e8.f18658f);
        sb3.append(")");
        textView22.setText(sb3.toString());
        StringBuilder sb22 = new StringBuilder();
        sb22.append(e8.f18659g);
        sb22.append(" +");
        textView32.setText(sb22.toString());
        textView42.setText(e8.f18657e.trim());
        main_layout.setOnClickListener(new View.OnClickListener() {
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
        button2.setOnClickListener(view -> {
            if (e8.f18654b.contains("http")) {
                Glob.b(context, e8.f18654b);
                return;
            }
            StringBuilder h8 = Glob.h("market://details?id=");
            h8.append(e8.f18654b);
            Glob.b(context, h8.toString());
            context.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(h8.toString())));
        });
        viewGroup.removeAllViews();
        viewGroup.addView(view2);
        U++;
    }

    public static void small(ViewGroup viewGroup, Activity context, NativeCallback nativeCallback) {
        if (nativeCallback != null) {
            nativeCallback.onState(AdvertisementState.QUREKA_NATIVE_AD_SHOW);
        }
        Adsresponse e8 = Glob.dataset(context);
        LayoutInflater from = LayoutInflater.from(context);
        View inflate = from.inflate(R.layout.qurea_small_native, viewGroup, false);
        ImageView imageView3 = (ImageView) inflate.findViewById(R.id.iv_banner);
        ImageView imageView22 = (ImageView) inflate.findViewById(R.id.iv_logo);
        TextView textView6 = (TextView) inflate.findViewById(R.id.tv_appname);
        CardView main_layout = (CardView) inflate.findViewById(R.id.main_layout);
        RatingBar ratingBar2 = (RatingBar) inflate.findViewById(R.id.ad_stars);
        TextView textView22 = (TextView) inflate.findViewById(R.id.tv_rating);
        TextView textView32 = (TextView) inflate.findViewById(R.id.tv_download);
        TextView textView42 = (TextView) inflate.findViewById(R.id.tv_desc);
        Button button2 = (Button) inflate.findViewById(R.id.btn_install);
        View view2 = inflate;
        Glide.with(context).load("file:///android_asset/" + e8.f18656d).into(imageView3);
        Glide.with(context).load("file:///android_asset/" + e8.f18655c).into(imageView22);
        if (!e8.f18654b.contains("http")) {
            textView6.setText("Play & Win Coins Daily.");
            str4 = "Play Now";
        } else {
            textView6.setText(e8.f18653a.trim());
            str4 = "Install";
        }
        button2.setText(str4);
        ratingBar2.setRating(Float.parseFloat(e8.f18658f));
        StringBuilder sb3 = new StringBuilder();
        sb3.append("(");
        sb3.append(e8.f18658f);
        sb3.append(")");
        textView22.setText(sb3.toString());
        StringBuilder sb22 = new StringBuilder();
        sb22.append(e8.f18659g);
        sb22.append(" +");
        textView32.setText(sb22.toString());
        textView42.setText(e8.f18657e.trim());
        main_layout.setOnClickListener(new View.OnClickListener() {
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
        button2.setOnClickListener(new View.OnClickListener() {
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
        viewGroup.addView(view2);
        U++;


    }

    public static void banner(ViewGroup viewGroup, Activity context, InterCallback interCallback) {
        if (interCallback != null) {
            interCallback.onClose(AdvertisementState.QUREKA_NATIVE_BANNER_AD_SHOW);
        }
        Adsresponse e8 = Glob.dataset(context);
        LayoutInflater from = LayoutInflater.from(context);

        View inflate = from.inflate(R.layout.qurea_tiny_native, viewGroup, false);

        ImageView imageView3 = (ImageView) inflate.findViewById(R.id.iv_banner);
        ImageView imageView22 = (ImageView) inflate.findViewById(R.id.iv_logo);
        TextView textView6 = (TextView) inflate.findViewById(R.id.tv_appname);
        LinearLayout linearLayout2 = (LinearLayout) inflate.findViewById(R.id.ll_app_panel);
        CardView main_layout = (CardView) inflate.findViewById(R.id.main_layout);
        RatingBar ratingBar2 = (RatingBar) inflate.findViewById(R.id.ad_stars);
        TextView textView22 = (TextView) inflate.findViewById(R.id.tv_rating);
        TextView textView32 = (TextView) inflate.findViewById(R.id.tv_download);
        TextView textView42 = (TextView) inflate.findViewById(R.id.tv_desc);
        Button button2 = (Button) inflate.findViewById(R.id.btn_install);
        View view2 = inflate;
        Glide.with(context).load("file:///android_asset/" + e8.f18656d).into(imageView3);
        Glide.with(context).load("file:///android_asset/" + e8.f18655c).into(imageView22);
        if (!e8.f18654b.contains("http")) {
            textView6.setText("Play & Win Coins Daily.");
            str4 = "Play Now";
        } else {
            textView6.setText(e8.f18653a.trim());
            str4 = "Install";
        }
        button2.setText(str4);
        ratingBar2.setRating(Float.parseFloat(e8.f18658f));
        StringBuilder sb3 = new StringBuilder();
        sb3.append("(");
        sb3.append(e8.f18658f);
        sb3.append(")");
        textView22.setText(sb3.toString());
        StringBuilder sb22 = new StringBuilder();
        sb22.append(e8.f18659g);
        sb22.append(" +");
        textView32.setText(sb22.toString());
        textView42.setText(e8.f18657e.trim());
        main_layout.setOnClickListener(new View.OnClickListener() {
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
        button2.setOnClickListener(new View.OnClickListener() {
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
        viewGroup.addView(view2);
        U++;


    }

}
