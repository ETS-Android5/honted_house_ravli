package com.pesonal.adsdk.vpn;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.pesonal.adsdk.R;
import com.pesonal.adsdk.model.vpnmodel.CountryListItem;
import com.pesonal.adsdk.model.vpnmodel.Server;
import com.pesonal.adsdk.model.vpnmodel.ServerListItem;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.remote.TinyDB;
import com.pesonal.adsdk.utils.CheckInternetConnection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

import de.blinkt.openvpn.OpenVpnApi;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.OpenVPNThread;

public class VanishVPNActivity extends AppCompatActivity {

    private ImageView ivBgBlink;
    private ImageView ivProgress;
    private FrameLayout frameOn;
    private ImageView ivConnect;
    private ImageView ivConnectionStatus;
    private TextView tvConnectionTitle;
    private TextView tvUpload;
    private TextView tvDownload;
    private FrameLayout ivDown;
    private ImageView regionImage;
    private TextView tvContryName;
    private TextView tvIpAddress;
    private TextView btnChange;
    private View viewSpace;
    private RecyclerView recyclerServer;
    BottomSheetBehavior sheetBehavior;
    private LinearLayout bottomSheet;

    private Server server;
    boolean vpnStart = false;
    private CheckInternetConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vanish_vpn);
        String vpnServerFlag = new TinyDB(this).getString("vpnServerFlag", "");
        server = new Server(new TinyDB(this).getString("vpnServer", "United Status"),
                vpnServerFlag,
                APIManager.getInstance(this).getVpnServer(),
                APIManager.getInstance(this).getVpnUser(),
                APIManager.getInstance(this).getVpnPass());
        initView();
        isServiceRunning();
    }

    private void initView() {
        ivBgBlink = (ImageView) findViewById(R.id.ivBgBlink);
        ivProgress = (ImageView) findViewById(R.id.ivProgress);
        frameOn = (FrameLayout) findViewById(R.id.frameOn);
        ivConnect = (ImageView) findViewById(R.id.ivConnect);
        bottomSheet = (LinearLayout) findViewById(R.id.layout_sheet);
        ivConnectionStatus = (ImageView) findViewById(R.id.ivConnectionStatus);
        tvConnectionTitle = (TextView) findViewById(R.id.tvConnectionTitle);
        tvUpload = (TextView) findViewById(R.id.tv_upload);
        tvDownload = (TextView) findViewById(R.id.tv_download);
        ivDown = (FrameLayout) findViewById(R.id.ivDown);
        regionImage = (ImageView) findViewById(R.id.region_image);
        tvContryName = (TextView) findViewById(R.id.tv_contry_name);
        tvContryName.setText(server.getCountry());
        new GetPublicIP().execute();
        Glide.with(this).load(new TinyDB(this).getString("vpnServerFlag")).into(regionImage);
        tvIpAddress = (TextView) findViewById(R.id.tv_ip_address);
        btnChange = (TextView) findViewById(R.id.btnChange);
        viewSpace = (View) findViewById(R.id.viewSpace);
        recyclerServer = (RecyclerView) findViewById(R.id.recyclerServer);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        connection = new CheckInternetConnection();
        ivConnect.setOnClickListener(view -> {
            if (vpnStart) {
                confirmDisconnect();
            } else {
                prepareVpn();
            }
        });
        recyclerServer.setLayoutManager(new LinearLayoutManager(this));
        List<CountryListItem> vpnServerList = APIManager.getInstance(this).getVpnServerList();

        for (CountryListItem countryListItem : vpnServerList) {
            if (countryListItem.getCountryName().equalsIgnoreCase(APIManager.getInstance(this).getVpnLocation())) {
                List<ServerListItem> serverList = countryListItem.getServerList();
                Map<String, List<ServerListItem>> collect = new HashMap<>();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    collect = serverList.stream().collect(Collectors.groupingBy(ServerListItem::getCityName));
                }else{
                    for (ServerListItem student : serverList) {
                        String key  = student.getCityName();
                        if(collect.containsKey(key)){
                            List<ServerListItem> list = collect.get(key);
                            list.add(student);
                        }else{
                            List<ServerListItem> list = new ArrayList<ServerListItem>();
                            list.add(student);
                            collect.put(key, list);
                        }

                    }
                }
                recyclerServer.setAdapter(new FreeServersAdapter(this, collect, countryListItem, (country, countryListItem1) -> {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    downloadConfigFile(country, countryListItem1);
                }));
            }
        }

        btnChange.setOnClickListener(view -> sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));
        ivDown.setOnClickListener(view -> {
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            else
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        });

    }

    public void downloadConfigFile(List<ServerListItem> countryListItem, CountryListItem countryListItem1) {
        int max = countryListItem.size() - 1;
        int min = 0;
        int random = new Random().nextInt(max - min + 1) + min;
        Log.e("TAG", "clickFile:random " + random);


        ServerListItem serverListItem = countryListItem.get(random);

        if (APIManager.isLog)
            Log.e("TAG", "downloadConfigFile: " + serverListItem.getConfig() + "  ");
        PRDownloader.download(serverListItem.getConfig(), getCacheDir().getAbsolutePath(), "server.ovpn")
                .build()
                .setOnStartOrResumeListener(() -> {

                })
                .setOnPauseListener(() -> {

                })
                .setOnCancelListener(() -> {

                })
                .setOnProgressListener(progress -> {
                    if (APIManager.isLog)
                        Log.e("TAG", "progress: downloadConfigFile" + progress);
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        if (APIManager.isLog)
                            Log.e("TAG", "progress onDownloadComplete: downloadConfigFile  ");
                        server = new Server(serverListItem.getCityName(), countryListItem1.getFlagUrl(),
                                new File(getCacheDir().getAbsolutePath(), "server.ovpn").getAbsolutePath(),
                                APIManager.getInstance(VanishVPNActivity.this).getVpnUser(),
                                APIManager.getInstance(VanishVPNActivity.this).getVpnPass());
                        startVpn();
                    }

                    @Override
                    public void onError(Error error) {
                        if (APIManager.isLog)
                            Log.e("TAG", "progress onError: downloadConfigFile" + error.getServerErrorMessage());
                    }

                });
    }

    public void isServiceRunning() {
        setStatus(OpenVPNService.getStatus());
    }

    public void setStatus(String connectionState) {
        if (connectionState != null)
            if (APIManager.isLog) {
                Log.e("TAG", "setStatus: " + connectionState);
            }
        switch (connectionState) {
            case "CONNECTED":
                vpnStart = true;
                ivBgBlink.clearAnimation();
                ivBgBlink.setImageResource(R.drawable.image_bg_connected);
                tvConnectionTitle.setVisibility(View.VISIBLE);
                tvConnectionTitle.setText("Protected");
                ivConnectionStatus.setImageResource(R.drawable.icon_active);
                frameOn.setBackgroundResource(R.drawable.bg_on);
                ivConnect.setImageResource(R.drawable.icon_on);
                break;
            case "WAIT":
            case "RECONNECTING":
            case "AUTH":
            case "VPN_GENERATE_CONFIG":
            case "NOPROCESS":
            case "GET_CONFIG":
            case "ASSIGN_IP":
            case "RESOLVE":
                tvConnectionTitle.setText("Connecting..");
                ivBgBlink.setImageResource(R.drawable.image_bg_blink);
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(800);
                anim.setStartOffset(0);
                ivConnectionStatus.setImageResource(R.drawable.icon_active);
                frameOn.setBackgroundResource(R.drawable.bg_on);
                ivConnect.setImageResource(R.drawable.icon_on);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                ivBgBlink.setAnimation(anim);
                break;
            case "NONETWORK":
                ivBgBlink.clearAnimation();
                ivBgBlink.setImageResource(R.drawable.image_bg);
                tvConnectionTitle.setText("Protect Your Network");
                ivConnectionStatus.setImageResource(R.drawable.icon_deactive);
                frameOn.setBackgroundResource(R.drawable.bg_off);
                ivConnect.setImageResource(R.drawable.icon_off);
                break;
            default:
                vpnStart = false;
                ivBgBlink.clearAnimation();
                ivBgBlink.setImageResource(R.drawable.image_bg);
                tvConnectionTitle.setText("Protect Your Network");
                ivConnectionStatus.setImageResource(R.drawable.icon_deactive);
                frameOn.setBackgroundResource(R.drawable.bg_off);
                ivConnect.setImageResource(R.drawable.icon_off);
                OpenVPNService.setDefaultStatus();
        }
    }

    public void confirmDisconnect() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.connection_close_confirm));
        builder.setPositiveButton(getString(R.string.yes), (dialog, id) -> stopVpn());
        builder.setNegativeButton(getString(R.string.no), (dialog, id) -> {
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public class GetPublicIP extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String publicIP = "";
            try {
                Scanner s = new Scanner(
                        new URL(
                                "https://api.ipify.org")
                                .openStream(), "UTF-8")
                        .useDelimiter("\\A");
                publicIP = s.next();
                System.out.println("My current IP address is " + publicIP);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return publicIP;
        }

        @Override
        protected void onPostExecute(String publicIp) {
            super.onPostExecute(publicIp);
            tvIpAddress.setText(publicIp);
        }
    }

    public boolean stopVpn() {
        try {
            OpenVPNThread.stop();
            setStatus("DISCONNECTED");
            vpnStart = false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "stopVpn: " + e.getMessage());
        }
        return false;
    }

    private void prepareVpn() {
        if (!vpnStart) {
            if (getInternetStatus()) {
                Intent intent = VpnService.prepare(this);
                if (intent != null) {
                    someActivityResultLauncher.launch(intent);
                } else startVpn();

                setStatus("RECONNECTING");

            } else {
                showToast("you have no internet connection !!");
            }

        } else if (stopVpn()) {
            showToast("Disconnect Successfully");
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    startVpn();
                }
            });

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public boolean getInternetStatus() {
        return connection.netCheck(this);
    }

    private void startVpn() {
        try {
            InputStream conf = new FileInputStream(server.getOvpn());
            InputStreamReader isr = new InputStreamReader(conf);
            BufferedReader br = new BufferedReader(isr);
            String config = "";
            String line;

            while (true) {
                line = br.readLine();
                if (line == null) break;
                config += line + "\n";
            }

            br.readLine();
            OpenVpnApi.startVpn(this, config, server.getCountry(), server.getOvpnUserName(), server.getOvpnUserPassword());
            vpnStart = true;
        } catch (IOException | RemoteException e) {
            Log.e("TAG", "startVpn: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("connectionState"));
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        super.onPause();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                setStatus(intent.getStringExtra("state"));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                String duration = intent.getStringExtra("duration");
                String lastPacketReceive = intent.getStringExtra("lastPacketReceive");
                String byteIn = intent.getStringExtra("byteIn");
                String byteOut = intent.getStringExtra("byteOut");

                if (duration == null) duration = "00:00:00";
                if (lastPacketReceive == null) lastPacketReceive = "0";
                if (byteIn == null) byteIn = " ";
                if (byteOut == null) byteOut = " ";
                updateConnectionStatus(duration, lastPacketReceive, byteIn, byteOut);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    public void updateConnectionStatus(String duration, String lastPacketReceive, String byteIn, String byteOut) {
        tvDownload.setText(byteIn);
        tvUpload.setText(byteOut);
        tvContryName.setText(server.getCountry());
        Glide.with(this).load(server.getFlagUrl()).into(regionImage);
        new GetPublicIP().execute();
    }


    @Override
    public void onBackPressed() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        super.onBackPressed();
    }

}