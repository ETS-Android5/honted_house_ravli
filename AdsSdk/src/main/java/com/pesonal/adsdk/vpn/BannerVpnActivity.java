package com.pesonal.adsdk.vpn;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.VpnService;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.pesonal.adsdk.BaseActivity;
import com.pesonal.adsdk.R;
import com.pesonal.adsdk.model.vpnmodel.Server;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.remote.TinyDB;
import com.pesonal.adsdk.utils.CheckInternetConnection;
import com.pesonal.adsdk.utils.ConnectionListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import de.blinkt.openvpn.OpenVpnApi;
import de.blinkt.openvpn.core.OpenVPNService;
import de.blinkt.openvpn.core.OpenVPNThread;

public class BannerVpnActivity extends BaseActivity {


    private ImageView ivUnconnected;
    private TextView tvTitle;
    private ImageView ivConnecting;
    private ImageView ivConnected;
    private ImageView iv_bg;
    private ImageView iv_icon;
    private ImageView ivBGImage;
    boolean vpnStart = false;
    boolean isProgress = false;
    private ConnectionListener connectionListener = null;
    private ImageView bgTexture;

    public void setBannerView(ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = (View) inflater.inflate(R.layout.layout_vpn, null);
        viewGroup.removeAllViews();
        ivUnconnected = (ImageView) view.findViewById(R.id.iv_unconnected);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        ivConnecting = (ImageView) view.findViewById(R.id.iv_connecting);
        ivConnected = (ImageView) view.findViewById(R.id.iv_connected);
        iv_bg = (ImageView) view.findViewById(R.id.iv_bg);
        iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
        ivBGImage = (ImageView) view.findViewById(R.id.ivBGImage);
        bgTexture = (ImageView) view.findViewById(R.id.bgTexture);
        iv_bg.setOnClickListener(view1 -> {
            if (vpnStart) {
                confirmDisconnect();
            } else {
                connectVpn();
            }
        });
        isServiceRunning();
        viewGroup.addView(view);
    }

    public void setBackgroundColor(int color) {
        ivBGImage.setBackgroundColor(color);
        ivConnected.setColorFilter(color);
        ivConnecting.setColorFilter(color);
        ivUnconnected.setColorFilter(color);
    }

    public void setTextColor(int color) {
        tvTitle.setTextColor(color);
        bgTexture.setColorFilter(color);
    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("connectionState"));
        isServiceRunning();
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
        }
    };

    public void connectVpnListener(ConnectionListener connectionListener) {
        this.connectionListener = connectionListener;
        connectVpn();
    }

    public void connectVpn() {
        if (!vpnStart) {
            if (getInternetStatus()) {
                Intent intent = VpnService.prepare(this);
                if (intent != null) {
                    someActivityResultLauncher.launch(intent);
                } else startVpn();
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

    public boolean getInternetStatus() {
        return new CheckInternetConnection().netCheck(this);
    }

    private void startVpn() {
        setStatus("RECONNECTING");
        Server server = new Server(new TinyDB(this).getString("vpnServer", "United Status"), "",
                APIManager.getInstance(this).getVpnServer(),
                APIManager.getInstance(this).getVpnUser(),
                APIManager.getInstance(this).getVpnPass());

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
            if (connectionListener != null)
                connectionListener.onStatus(CONNECTION_STATE.FAIL, "FAIL");
            Log.e("TAG", "startVpn: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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

    public void isServiceRunning() {
        setStatus(OpenVPNService.getStatus());
    }

    public boolean getConnection() {
        return OpenVPNService.getStatus().equalsIgnoreCase("CONNECTED");
    }

    public void setStatus(String connectionState) {
        if (APIManager.isLog) {
            if (connectionState != null)
                Log.e("TAG", "setStatus:Banner " + connectionState);
        }

        switch (connectionState) {
            case "CONNECTED":
                vpnStart = true;
                uiConnect();
                if (connectionListener != null)
                    connectionListener.onStatus(CONNECTION_STATE.CONNECTED, connectionState);
                break;
            case "WAIT":
            case "CONNECTRETRY":
            case "RECONNECTING":
            case "AUTH":
            case "VPN_GENERATE_CONFIG":
            case "NOPROCESS":
            case "GET_CONFIG":
            case "ASSIGN_IP":
            case "RESOLVE":
            case "EXITING":
            case "TCP_CONNECT":
            case "AUTH_PENDING":
            case "ADD_ROUTES":
                if (!isProgress) {
                    uiProgress();
                }
                if (connectionListener != null)
                    connectionListener.onStatus(CONNECTION_STATE.CONNECTING, connectionState);
                break;
            case "NONETWORK":
                if (isProgress) {
                    uiProgress();
                } else {
                    uiDisconnect();
                }
                if (connectionListener != null)
                    connectionListener.onStatus(CONNECTION_STATE.CONNECTING, connectionState);
                break;
            case "AUTH_FAILED":
            case "DISCONNECTED":
                uiDisconnect();
                if (connectionListener != null)
                    connectionListener.onStatus(CONNECTION_STATE.DISCONNECTED, connectionState);
                break;
            default:
                isProgress = false;
                uiDisconnect();
                if (connectionListener != null)
                    connectionListener.onStatus(CONNECTION_STATE.DISCONNECTED, connectionState);
                break;
        }
    }

    public void uiProgress() {
        isProgress = true;
        if (tvTitle != null) {
            tvTitle.setText("Connecting..");
            iv_icon.setImageResource(R.drawable.icon_deactive);
            iv_bg.setImageResource(R.drawable.bg_switch_deactive);
            ivConnecting.setVisibility(View.VISIBLE);
            ivConnected.setVisibility(View.GONE);
            ivUnconnected.setVisibility(View.GONE);
            ObjectAnimator ofFloat = ObjectAnimator.ofFloat(ivConnecting, "rotation", 0.0f, 360.0f);
            ofFloat.setRepeatCount(-1);
            ofFloat.setRepeatMode(ValueAnimator.RESTART);
            ofFloat.setInterpolator(new LinearInterpolator());
            ofFloat.setDuration(800L);
            ofFloat.start();
        }
    }


    public void uiConnect() {
        if (tvTitle != null) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText("Protected");
            iv_icon.setImageResource(R.drawable.icon_active);
            iv_bg.setImageResource(R.drawable.bg_switch_active);
            ivConnecting.setVisibility(View.GONE);
            ivConnected.setVisibility(View.VISIBLE);
            ivUnconnected.setVisibility(View.GONE);
        }
    }

    public void uiDisconnect() {
        isProgress = false;
        if (tvTitle != null) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText("Protect Your\nNetwork");
            iv_icon.setImageResource(R.drawable.icon_deactive);
            iv_bg.setImageResource(R.drawable.bg_switch_deactive);
            ivConnecting.setVisibility(View.GONE);
            ivConnected.setVisibility(View.GONE);
            ivUnconnected.setVisibility(View.VISIBLE);
            OpenVPNService.setDefaultStatus();
        }
    }

}
