package com.demo.mycommonapp;


import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.pesonal.adsdk.dialog.PrivacyDialog;
import com.pesonal.adsdk.model.MOREAPPSPLASH;
import com.pesonal.adsdk.remote.APIManager;

import java.util.List;


public class StartSecondActivity extends AppCompatActivity implements View.OnClickListener {

    private Button ivPlayer;
    private ImageView ivShare;
    private ImageView ivPrivacy;
    private RelativeLayout adContainer;
    private RecyclerView recyclerApp;
    private TextView txtMoreApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
                APIManager.getInstance(StartSecondActivity.this).showAdsStart(StartSecondActivity.this, () -> {
                    Intent intent = new Intent(StartSecondActivity.this, MainAAActivity.class);
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

        if(APIManager.getInstance(StartSecondActivity.this).isUpdate()){
            showUpdateDialog("https://play.google.com/store/apps/details?id=" + getPackageName());
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
                    PrivacyDialog.show(StartSecondActivity.this,getResources().getStringArray(R.array.terms_of_service));
                return;

        }
    }

    @Override
    public void onBackPressed() {
    }

    public void showUpdateDialog(final String url) {

        final Dialog dialog = new Dialog(StartSecondActivity.this);
        dialog.setCancelable(false);
        View view = getLayoutInflater().inflate(R.layout.installnewappdialog, null);
        dialog.setContentView(view);
        TextView update = view.findViewById(R.id.update);
        TextView later = view.findViewById(R.id.later);
        TextView txt_title = view.findViewById(R.id.txt_title);
        TextView txt_decription = view.findViewById(R.id.txt_decription);

        update.setText("Update Now");
        txt_title.setText("Looks Like you have an older \n version of the app.");
        txt_decription.setText("");
        txt_decription.setVisibility(View.GONE);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri marketUri = Uri.parse(url);
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
                    startActivity(marketIntent);
                } catch (ActivityNotFoundException ignored1) {
                }
            }
        });
        later.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.create();
        }

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

}