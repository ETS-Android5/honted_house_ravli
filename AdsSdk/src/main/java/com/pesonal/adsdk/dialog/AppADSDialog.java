package com.pesonal.adsdk.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pesonal.adsdk.R;
import com.pesonal.adsdk.model.AdvertiseList;
import com.pesonal.adsdk.model.MOREAPPSPLASH;
import com.pesonal.adsdk.remote.APIManager;

import java.util.List;

public class AppADSDialog {

    public static void show(final Activity context, int id) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(1);
        dialog.getWindow().addFlags(2);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        View inflate = LayoutInflater.from(dialog.getContext()).inflate(R.layout.app_dialog, null, false);
        dialog.setContentView(inflate);
        ImageView idClose = (ImageView) inflate.findViewById(R.id.id_close);
        ImageView idLogo = (ImageView) inflate.findViewById(R.id.id_logo);
        TextView idTitle = (TextView) inflate.findViewById(R.id.id_title);
        TextView idDownload = (TextView) inflate.findViewById(R.id.id_download);

        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        List<AdvertiseList> adsResponces1 = APIManager.getInstance(context).getAdvertiseLists();
        if (adsResponces1 != null && adsResponces1.size() > 0) {
            final AdvertiseList adsResponces = adsResponces1.get(0);
            idTitle.setText(adsResponces.getApp_name());
            Glide.with(context).load(adsResponces.getApp_logo()).into(idLogo);
            idClose.setOnClickListener(v -> dialog.dismiss());
            idDownload.setOnClickListener(v -> {
                String appPackageName = adsResponces.getApp_packageName().replace("_", ".");
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("market://details?id=");
                    stringBuilder.append(appPackageName);
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString()));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("https://play.google.com/store/apps/details?id=");
                    stringBuilder2.append(appPackageName);
                    Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder2.toString()));
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent2);
                }
            });
            dialog.show();
        } else {
            List<MOREAPPSPLASH> customAdModels = APIManager.getInstance(context).get_SPLASHMoreAppData();
            if (customAdModels != null && customAdModels.size() > 0) {
                final MOREAPPSPLASH adsResponces = customAdModels.get(id);
                idTitle.setText(adsResponces.getApp_name());
                Glide.with(context).load(adsResponces.getApp_logo()).into(idLogo);
                idClose.setOnClickListener(v -> dialog.dismiss());
                idDownload.setOnClickListener(v -> {
                    String appPackageName = adsResponces.getApp_packageName().replace("_", ".");
                    try {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("market://details?id=");
                        stringBuilder.append(appPackageName);
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder.toString()));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("https://play.google.com/store/apps/details?id=");
                        stringBuilder2.append(appPackageName);
                        Intent intent2 = new Intent("android.intent.action.VIEW", Uri.parse(stringBuilder2.toString()));
                        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent2);
                    }
                });
                dialog.show();
            } else
                Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {

    }
}