package com.pesonal.adsdk.vpn;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkRequest;

import com.anchorfree.partner.api.data.Country;
import com.anchorfree.partner.api.response.AvailableCountries;
import com.anchorfree.partner.api.response.RemainingTraffic;
import com.anchorfree.reporting.TrackingConstants;
import com.anchorfree.sdk.SessionConfig;
import com.anchorfree.sdk.SessionInfo;
import com.anchorfree.sdk.UnifiedSDK;
import com.anchorfree.sdk.exceptions.PartnerApiException;
import com.anchorfree.sdk.rules.TrafficRule;
import com.anchorfree.vpnsdk.callbacks.Callback;
import com.anchorfree.vpnsdk.callbacks.CompletableCallback;
import com.anchorfree.vpnsdk.callbacks.TrafficListener;
import com.anchorfree.vpnsdk.callbacks.VpnStateListener;
import com.anchorfree.vpnsdk.compat.CredentialsCompat;
import com.anchorfree.vpnsdk.exceptions.NetworkRelatedException;
import com.anchorfree.vpnsdk.exceptions.VpnException;
import com.anchorfree.vpnsdk.exceptions.VpnPermissionDeniedException;
import com.anchorfree.vpnsdk.exceptions.VpnPermissionRevokedException;
import com.anchorfree.vpnsdk.transporthydra.HydraVpnTransportException;
import com.anchorfree.vpnsdk.vpnservice.VPNState;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.pesonal.adsdk.R;
import com.pesonal.adsdk.remote.APIManager;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class VpnActivity extends AppCompatActivity implements TrafficListener, VpnStateListener {

    protected static final String TAG = VpnActivity.class.getSimpleName();
    private ImageView ivConnect;
    private ImageView ivProgress;
    private TextView tvContryName;
    private TextView tvUpload;
    private TextView tvDownload;
    private TextView tvIpAddress;
    private TextView btnChange;
    private LinearLayout bottomSheet;
    BottomSheetBehavior sheetBehavior;
    private RecyclerView recyclerServer;
    private TextView tvConnectionTitle;
    private List<Country> paidList = new ArrayList();
    private View viewSpace;
    private FrameLayout frameOn;
    private ImageView ivBgBlink;
    private FrameLayout ivDown;
    private ImageView ivConnectionStatus;
    private String selectedCountry = "us";

    private Handler mUIHandler = new Handler(Looper.getMainLooper());
    final Runnable mUIUpdateRunnable = new Runnable() {
        public void run() {
            updateUI();
            checkRemainingTraffic();
            mUIHandler.postDelayed(mUIUpdateRunnable, WorkRequest.MIN_BACKOFF_MILLIS);
        }
    };
    private ImageView regionImage;


    @Override
    public void onStart() {
        super.onStart();
        UnifiedSDK.addTrafficListener(this);
        UnifiedSDK.addVpnStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        UnifiedSDK.removeVpnStateListener(this);
        UnifiedSDK.removeTrafficListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vpn);
        String[] s = APIManager.getInstance(this).getVpnLocation().split(" ");
        for (String s1 : s) {
            if (s1.length() == 2)
                selectedCountry = s1.toLowerCase();
        }
        if (APIManager.isLog)
            Log.e(TAG, "addView:selectedCountry " + selectedCountry);
        initView();
    }

    private void initView() {
        ivConnect = (ImageView) findViewById(R.id.ivConnect);
        ivProgress = (ImageView) findViewById(R.id.ivProgress);
        tvContryName = (TextView) findViewById(R.id.tv_contry_name);
        tvUpload = (TextView) findViewById(R.id.tv_upload);
        tvDownload = (TextView) findViewById(R.id.tv_download);
        tvIpAddress = (TextView) findViewById(R.id.tv_ip_address);
        btnChange = (TextView) findViewById(R.id.btnChange);
        bottomSheet = (LinearLayout) findViewById(R.id.layout_sheet);
        sheetBehavior = BottomSheetBehavior.from(bottomSheet);
        viewSpace = (View) findViewById(R.id.viewSpace);
        recyclerServer = (RecyclerView) findViewById(R.id.recyclerServer);
        tvConnectionTitle = (TextView) findViewById(R.id.tvConnectionTitle);
        ivConnectionStatus = (ImageView) findViewById(R.id.ivConnectionStatus);
        frameOn = (FrameLayout) findViewById(R.id.frameOn);
        recyclerServer.setLayoutManager(new LinearLayoutManager(this));
        ivBgBlink = (ImageView) findViewById(R.id.ivBgBlink);
        ivDown = (FrameLayout) findViewById(R.id.ivDown);
        regionImage = (ImageView) findViewById(R.id.region_image);
        getCountry();
        checkConnection();
        ivConnect.setOnClickListener(view -> setConnect(this::updateTextViews));
        btnChange.setOnClickListener(view -> sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));
        ivDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                else
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
    }

    private void checkConnection() {
        isConnected(new Callback<Boolean>() {
            @Override
            public void failure(VpnException vpnException) {
            }

            public void success(Boolean bool) {
                if (bool) {
                    ivBgBlink.clearAnimation();
                    ivBgBlink.setImageResource(R.drawable.image_bg_connected);
                    tvConnectionTitle.setVisibility(View.VISIBLE);
                    tvConnectionTitle.setText("Protected");
                    ivConnectionStatus.setImageResource(R.drawable.icon_active);
                    frameOn.setBackgroundResource(R.drawable.bg_on);
                    ivConnect.setImageResource(R.drawable.icon_on);
                    getCurrentServer(new Callback<String>() {
                        @Override
                        public void failure(VpnException vpnException) {
                        }

                        public void success(final String str) {
                            selectedCountry = str;
                            updateTextViews(true);
                        }
                    });
                } else {
                    ivBgBlink.clearAnimation();
                    ivBgBlink.setImageResource(R.drawable.image_bg);
                    tvConnectionTitle.setText("Protect Your Network");
                    ivConnectionStatus.setImageResource(R.drawable.icon_deactive);
                    frameOn.setBackgroundResource(R.drawable.bg_off);
                    ivConnect.setImageResource(R.drawable.icon_off);
                }
                updateTextViews(bool);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getConnectionSpeed() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
        int downSpeed = nc.getLinkDownstreamBandwidthKbps();
        int upSpeed = nc.getLinkUpstreamBandwidthKbps();
        tvUpload.setText(getFileSize(upSpeed));
        tvDownload.setText(getFileSize(downSpeed));
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0.0B/s";
        final String[] units = new String[]{"B/s", "KB/s", "MB/s", "GB/s", "TB/s"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
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
            if (APIManager.isLog)
                Log.e(TAG, "onPostExecute: " + publicIp);
            tvIpAddress.setText(publicIp);
        }
    }


    public void setConnect(ConnectListener connectListener) {
        isConnected(new Callback<Boolean>() {
            @Override
            public void failure(@NonNull VpnException vpnException) {
                if (APIManager.isLog)
                    Log.e(TAG, "failure: " + vpnException.getMessage());
            }

            public void success(@NonNull Boolean bool) {
                if (bool) {
                    disconnectFromVnp(connectListener);
                } else {
                    isConnected(new Callback<Boolean>() {
                        @Override
                        public void failure(@NonNull VpnException vpnException) {
                        }

                        public void success(@NonNull Boolean bool) {
                            if (bool) {
                                disconnectFromVnp(connectListener);
                            } else {
                                connectToVpn(connectListener);
                            }
                        }
                    });
                }
            }
        });
    }

    public void connectToVpn(ConnectListener connectListener) {
        isLoggedIn(new Callback<Boolean>() {
            @Override
            public void failure(VpnException vpnException) {
            }

            public void success(Boolean bool) {
                if (bool) {
                    ArrayList<String> arrayList = new ArrayList();
                    arrayList.add("hydra");
                    LinkedList<String> linkedList = new LinkedList();
                    linkedList.add("*facebook.com");
                    linkedList.add("*wtfismyip.com");
                    UnifiedSDK.getInstance().getVPN().start(new SessionConfig.Builder().withReason(TrackingConstants.GprReasons.M_UI).withTransportFallback(arrayList).withTransport("hydra").withVirtualLocation(selectedCountry).addDnsRule(TrafficRule.Builder.bypass().fromDomains(linkedList)).build(), new CompletableCallback() {

                        @Override
                        public void complete() {
                            Toast.makeText((Context) VpnActivity.this, (CharSequence) "Connected", Toast.LENGTH_SHORT).show();
                            startUIUpdateTask();
                            if (connectListener != null)
                                connectListener.onConnect(true);
                        }

                        @Override
                        public void error(VpnException vpnException) {
                            updateUI();
                            handleError(vpnException);
                            if (connectListener != null)
                                connectListener.onConnect(false);
                        }

                    });
                    return;
                }
                showMessage("Login please");
            }
        });
    }

    public void disconnectFromVnp(ConnectListener connectListener) {

        UnifiedSDK.getInstance().getVPN().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
            @Override
            public void complete() {
                Toast.makeText((Context) VpnActivity.this, (CharSequence) "Disconnected", Toast.LENGTH_SHORT).show();
                stopUIUpdateTask();
                if (connectListener != null)
                    connectListener.onConnect(false);
            }

            @Override
            public void error(VpnException vpnException) {
                updateUI();
                handleError(vpnException);
                if (connectListener != null)
                    connectListener.onConnect(false);
            }
        });
    }

    public void updateUI() {
        UnifiedSDK.getVpnState(new Callback<VPNState>() {
            @Override
            public void failure(VpnException vpnException) {
            }

            public void success(VPNState vPNState) {
                switch (vPNState) {
                    case DISCONNECTING:
                        ivBgBlink.clearAnimation();
                        ivBgBlink.setImageResource(R.drawable.image_bg);
                        tvConnectionTitle.setText("Protect Your Network");
                        ivConnectionStatus.setImageResource(R.drawable.icon_deactive);
                        frameOn.setBackgroundResource(R.drawable.bg_off);
                        ivConnect.setImageResource(R.drawable.icon_off);
                        return;
                    case CONNECTED:
                        ivBgBlink.clearAnimation();
                        ivBgBlink.setImageResource(R.drawable.image_bg_connected);
                        tvConnectionTitle.setVisibility(View.VISIBLE);
                        tvConnectionTitle.setText("Protected");
                        ivConnectionStatus.setImageResource(R.drawable.icon_active);
                        frameOn.setBackgroundResource(R.drawable.bg_on);
                        ivConnect.setImageResource(R.drawable.icon_on);
                        return;
                    case CONNECTING_VPN:
                    case CONNECTING_PERMISSIONS:
                    case CONNECTING_CREDENTIALS:
                        tvConnectionTitle.setText("Connecting..");
                        ivBgBlink.setImageResource(R.drawable.image_bg_blink);
                        Animation anim = new AlphaAnimation(0.0f, 1.0f);
                        anim.setDuration(800);
                        anim.setStartOffset(0);
                        anim.setRepeatMode(Animation.REVERSE);
                        anim.setRepeatCount(Animation.INFINITE);
                        ivBgBlink.setAnimation(anim);
                        return;
                    case PAUSED:
                        tvConnectionTitle.setText(R.string.paused);
                        return;
                    default:
                        return;
                }
            }
        });
        UnifiedSDK.getInstance().getBackend().isLoggedIn(new Callback<Boolean>() {
            @Override
            public void failure(VpnException vpnException) {
            }

            public void success(Boolean bool) {
            }
        });
        getCurrentServer(new Callback<String>() {
            @Override
            public void failure(VpnException vpnException) {
            }

            public void success(final String str) {
                runOnUiThread(() -> new Locale("", str));
            }
        });
    }

    public void isLoggedIn(Callback<Boolean> callback) {
        UnifiedSDK.getInstance().getBackend().isLoggedIn(callback);
    }

    public void isConnected(final Callback<Boolean> callback) {
        UnifiedSDK.getVpnState(new Callback<VPNState>() {
            public void success(VPNState vPNState) {
                callback.success(vPNState == VPNState.CONNECTED);
            }

            @Override
            public void failure(VpnException vpnException) {
                if (APIManager.isLog)
                    Log.e(TAG, "failure: " + vpnException.getMessage());
                callback.success(false);
            }
        });
    }

    public void checkRemainingTraffic() {
        UnifiedSDK.getInstance().getBackend().remainingTraffic(new Callback<RemainingTraffic>() {
            public void success(RemainingTraffic remainingTraffic) {
                updateRemainingTraffic(remainingTraffic);
            }

            @Override
            public void failure(VpnException vpnException) {
                updateUI();
                handleError(vpnException);
            }
        });
    }

    public void updateTrafficStats(long j, long j2) {
        Converter.humanReadableByteCountOld(j, false);
        Converter.humanReadableByteCountOld(j2, false);
    }

    public void updateRemainingTraffic(RemainingTraffic remainingTraffic) {
        if (!remainingTraffic.isUnlimited()) {
            Converter.megabyteCount(remainingTraffic.getTrafficUsed());
            Converter.megabyteCount(remainingTraffic.getTrafficLimit());
        }
    }

    public void startUIUpdateTask() {
        stopUIUpdateTask();
        this.mUIHandler.post(this.mUIUpdateRunnable);
    }

    public void stopUIUpdateTask() {
        this.mUIHandler.removeCallbacks(this.mUIUpdateRunnable);
        updateUI();
    }

    public void getCurrentServer(final Callback<String> callback) {
        UnifiedSDK.getVpnState(new Callback<VPNState>() {
            public void success(VPNState vPNState) {
                if (vPNState == VPNState.CONNECTED) {
                    UnifiedSDK.getStatus(new Callback<SessionInfo>() {
                        public void success(SessionInfo sessionInfo) {
                            callback.success(CredentialsCompat.getServerCountry(sessionInfo.getCredentials()));
                        }

                        @Override
                        public void failure(VpnException vpnException) {
                            callback.success(selectedCountry);
                        }
                    });
                } else {
                    callback.success(selectedCountry);
                }
            }

            @Override
            public void failure(VpnException vpnException) {
                callback.failure(vpnException);
            }
        });
    }

    public void getCountry() {
        UnifiedSDK.getInstance().getBackend().countries(new Callback<>() {
            public void success(AvailableCountries availableCountries) {
                paidList = new ArrayList<>();
                String[] stringArray = getResources().getStringArray(R.array.countryNames);
                List<String> arrayList = Arrays.asList(stringArray);
                for (Country country : availableCountries.getCountries()) {
                    Locale locale = new Locale("", country.getCountry());
                    if (!locale.getDisplayCountry().equalsIgnoreCase("Unknown server")) {
                        if (arrayList.contains(country.getCountry()))
                            paidList.add(country);
                    }
                }
                if (paidList.size() > 0)
                    viewSpace.setVisibility(View.VISIBLE);
                recyclerServer.setAdapter(new FreeServersAdapter(VpnActivity.this, paidList, country1 -> {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    selectedCountry = country1.getCountry();
                    if (APIManager.isLog)
                        Log.e(TAG, "onRegionSelected: " + selectedCountry);
                    updateUI();
                    UnifiedSDK.getVpnState(new Callback<VPNState>() {

                        @Override
                        public void failure(VpnException vpnException) {
                        }

                        public void success(VPNState vPNState) {
                            showMessage("Connecting to VPN with " + new Locale("", selectedCountry).getDisplayCountry());
                            UnifiedSDK.getInstance().getVPN().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
                                @Override
                                public void complete() {
                                    connectToVpn(VpnActivity.this::updateTextViews);
                                }

                                @Override
                                public void error(VpnException vpnException) {
                                    connectToVpn(VpnActivity.this::updateTextViews);
                                }
                            });
                        }
                    });
                }));
            }

            @Override
            public void failure(VpnException vpnException) {
            }
        });
    }

    public void updateTextViews(boolean success) {
        if (APIManager.isLog)
            Log.e(TAG, "updateTextViews: " + success + "  " + selectedCountry);
        tvContryName.setText(new Locale("", selectedCountry).getDisplayCountry());
        regionImage.setImageResource(getResources().getIdentifier("drawable/" + selectedCountry, null, getPackageName()));
        new GetPublicIP().execute();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getConnectionSpeed();
        }
    }

    public void handleError(Throwable e) {
        Log.w("TAG", e);
        if (e instanceof NetworkRelatedException) {
//            showMessage("Check internet connection");
        } else if (e instanceof VpnException) {
            if (e instanceof VpnPermissionRevokedException) {
                showMessage("User revoked vpn permissions");
            } else if (e instanceof VpnPermissionDeniedException) {
                showMessage("User canceled to grant vpn permissions");
            } else if (e instanceof HydraVpnTransportException) {
                HydraVpnTransportException hydraVpnTransportException = (HydraVpnTransportException) e;
                if (hydraVpnTransportException.getCode() == HydraVpnTransportException.HYDRA_ERROR_BROKEN) {
                    showMessage("Connection with vpn server was lost");
                } else if (hydraVpnTransportException.getCode() == HydraVpnTransportException.HYDRA_DCN_BLOCKED_BW) {
                    showMessage("Client traffic exceeded");
                } else {
                    showMessage("Error in VPN transport");
                }
            } else if (e instanceof PartnerApiException) {
                switch (((PartnerApiException) e).getContent()) {
                    case PartnerApiException.CODE_NOT_AUTHORIZED:
                        showMessage("User unauthorized");
                        break;
                    case PartnerApiException.CODE_TRAFFIC_EXCEED:
                        showMessage("Server unavailable");
                        break;
                    default:
                        showMessage("Other error. Check PartnerApiException constants");
                        break;
                }
            }
        } else {
            showMessage("Error in VPN Service");
            if (APIManager.isLog)
                Log.e("TAG", "handleError: " + e.getMessage());
        }
    }

    public void showMessage(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        isConnected(new Callback<Boolean>() {
            @Override
            public void failure(VpnException vpnException) {
            }

            public void success(Boolean bool) {
                if (bool.booleanValue()) {
                    startUIUpdateTask();
                }
            }
        });
    }

    @Override
    public void onTrafficUpdate(long j, long j2) {
        updateUI();
        updateTrafficStats(j, j2);
    }

    @Override
    public void vpnStateChanged(VPNState vPNState) {
        updateUI();
    }

    @Override
    public void vpnError(VpnException vpnException) {
        updateUI();
        handleError(vpnException);
    }

    @Override
    public void onBackPressed() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED)
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        super.onBackPressed();
    }
}