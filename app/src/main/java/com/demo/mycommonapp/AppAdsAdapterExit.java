package com.demo.mycommonapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pesonal.adsdk.model.MOREAPPEXIT;
import com.pesonal.adsdk.model.MOREAPPSPLASH;

import java.util.List;


public class AppAdsAdapterExit extends RecyclerView.Adapter<AppAdsAdapterExit.MyHolder> {
    List<MOREAPPEXIT> adsResponces;
    Context context;

    public AppAdsAdapterExit(Context context, List<MOREAPPEXIT> adsResponces) {
        this.adsResponces = adsResponces;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_app_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        final MOREAPPEXIT adsResponce = (MOREAPPEXIT) this.adsResponces.get(position);
        holder.appname.setText(adsResponce.getApp_name());
        holder.appname.setSelected(true);
//        holder.rate.setText(adsResponce.getAppRating());

        Glide.with(context).load(adsResponce.getApp_logo()).into(holder.logo);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appPackageName = adsResponce.getApp_packageName().replace("_", ".");
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
            }
        });

    }

    public int getItemCount() {
        return this.adsResponces.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView logo;
        TextView appname, rate;
        //        LinearLayout installbg;
        TextView download;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            logo = itemView.findViewById(R.id.id_app_icon);
            appname = itemView.findViewById(R.id.txt_app_name);
//            rate = itemView.findViewById(R.id.id_app_rate);
            download = itemView.findViewById(R.id.id_download);

        }
    }
}
