package com.pesonal.adsdk.vpn;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.WorkRequest;

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
import com.pesonal.adsdk.R;
import com.pesonal.adsdk.remote.APIManager;
import com.pesonal.adsdk.remote.TinyDB;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

public abstract class TubeVpnActivity extends AppCompatActivity implements TrafficListener, VpnStateListener {

    protected static final String TAG = TubeVpnActivity.class.getSimpleName();
    private Handler mUIHandler = new Handler(Looper.getMainLooper());
    final Runnable mUIUpdateRunnable = new Runnable() {
        public void run() {
            updateUI();
            checkRemainingTraffic();
            mUIHandler.postDelayed(mUIUpdateRunnable, WorkRequest.MIN_BACKOFF_MILLIS);
        }
    };
    private ImageView ivUnconnected;
    private TextView tvTitle;
    private ImageView ivConnecting;
    private ImageView ivConnected;
    private ImageView iv_bg;
    private ImageView iv_icon;
    private ImageView ivBGImage;
    private String selectedCountry = "us";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.selectedCountry = "us";

    }

    public void addView(ViewGroup viewGroup) {
        String[] s = APIManager.getInstance(this).getVpnLocation().split(" ");
        for (String s1 : s) {
            if (s1.length() == 2)
                selectedCountry = s1.toLowerCase();
        }
        if (APIManager.isLog)
            Log.e(TAG, "addView:selectedCountry " + selectedCountry);
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
        initView();
        handleAds();
        viewGroup.addView(view);
    }


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


    public void connectToVpn() {
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
                            new TinyDB(TubeVpnActivity.this).putBoolean("firstTimeVPN", true);
                            Toast.makeText((Context) TubeVpnActivity.this, (CharSequence) "Connected", Toast.LENGTH_SHORT).show();
                            startUIUpdateTask();
                        }

                        @Override
                        public void error(VpnException vpnException) {
                            updateUI();
//                            Toast.makeText((Context) TubeVpnActivity.this, (CharSequence) "Error Connecting", Toast.LENGTH_SHORT).show();
                            handleError(vpnException);
                        }
                    });
                    return;
                }
                showMessage("Login please");
            }
        });
    }

    public void disconnectFromVnp() {

        UnifiedSDK.getInstance().getVPN().stop(TrackingConstants.GprReasons.M_UI, new CompletableCallback() {
            @Override
            public void complete() {
                Toast.makeText((Context) TubeVpnActivity.this, (CharSequence) "Disconnected", Toast.LENGTH_SHORT).show();
                stopUIUpdateTask();
            }

            @Override
            public void error(VpnException vpnException) {
                updateUI();
                handleError(vpnException);
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
                Log.e(TAG, "failure: " + vpnException.getMessage());
                callback.success(false);
            }
        });
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
    public void onPause() {
        super.onPause();
        stopUIUpdateTask();
    }

    public void startUIUpdateTask() {
        stopUIUpdateTask();
        this.mUIHandler.post(this.mUIUpdateRunnable);
    }


    public void stopUIUpdateTask() {
        this.mUIHandler.removeCallbacks(this.mUIUpdateRunnable);
        updateUI();
    }


    public void updateUI() {
        UnifiedSDK.getVpnState(new Callback<VPNState>() {


            @Override
            public void failure(VpnException vpnException) {
            }

            public void success(VPNState vPNState) {
                switch (vPNState) {
                    case DISCONNECTING:
                        tvTitle.setVisibility(View.VISIBLE);
                        tvTitle.setText("Protect Your\nNetwork");
                        ivBGImage.setImageResource(R.drawable.bg_red_vpn);
                        iv_icon.setImageResource(R.drawable.icon_deactive);
                        ivConnecting.setVisibility(View.GONE);
                        ivConnected.setVisibility(View.GONE);
                        ivUnconnected.setVisibility(View.VISIBLE);
                        return;
                    case CONNECTED:
                        tvTitle.setVisibility(View.VISIBLE);
                        tvTitle.setText("Protected");
                        ivBGImage.setImageResource(R.drawable.bg_green_vpn);
                        iv_icon.setImageResource(R.drawable.icon_active);
                        ivConnecting.setVisibility(View.GONE);
                        ivConnected.setVisibility(View.VISIBLE);
                        ivUnconnected.setVisibility(View.GONE);
                        return;
                    case CONNECTING_VPN:
                    case CONNECTING_PERMISSIONS:
                    case CONNECTING_CREDENTIALS:
                        tvTitle.setText("Connecting..");

                        ivConnecting.setVisibility(View.VISIBLE);
                        ivConnected.setVisibility(View.GONE);
                        ivUnconnected.setVisibility(View.GONE);
                        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(ivConnecting, "rotation", 0.0f, 360.0f);
                        ofFloat.setRepeatCount(-1);
                        ofFloat.setRepeatMode(ValueAnimator.RESTART);
                        ofFloat.setInterpolator(new LinearInterpolator());
                        ofFloat.setDuration(800L);
                        ofFloat.start();

                        return;
                    case PAUSED:
                        tvTitle.setText(R.string.paused);
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


    public void showMessage(String str) {
        Toast.makeText(this, str, Toast.LENGTH_LONG).show();
    }


    private void handleAds() {
        tvTitle.setVisibility(View.GONE);
        isConnected(new Callback<Boolean>() {
            @Override
            public void failure(VpnException vpnException) {
            }

            public void success(Boolean bool) {
                if (bool) {
                    tvTitle.setVisibility(View.VISIBLE);
                    tvTitle.setText("Protected");
                    ivBGImage.setImageResource(R.drawable.bg_green_vpn);
                    iv_icon.setImageResource(R.drawable.icon_active);
                    ivConnecting.setVisibility(View.GONE);
                    ivConnected.setVisibility(View.VISIBLE);
                    ivUnconnected.setVisibility(View.GONE);
                } else {
                    tvTitle.setVisibility(View.VISIBLE);
                    tvTitle.setText("Protect Your\nNetwork");
                    ivBGImage.setImageResource(R.drawable.bg_red_vpn);
                    iv_icon.setImageResource(R.drawable.icon_deactive);
                    ivConnecting.setVisibility(View.GONE);
                    ivConnected.setVisibility(View.GONE);
                    ivUnconnected.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void initView() {
        iv_bg.setOnClickListener(view -> {
            setConnect();
        });
    }

    public boolean isItFirstTime() {
        if (!new TinyDB(this).getBoolean("firstTimeVPN")) {
            return true;
        } else {
            return false;
        }
    }

    public void setConnect() {
        isConnected(new Callback<Boolean>() {
            @Override
            public void failure(@NonNull VpnException vpnException) {
                Log.e(TAG, "failure: " + vpnException.getMessage());
            }

            public void success(@NonNull Boolean bool) {
                if (bool) {
                    disconnectFromVnp();
                } else {
                    isConnected(new Callback<Boolean>() {
                        @Override
                        public void failure(@NonNull VpnException vpnException) {
                        }

                        public void success(@NonNull Boolean bool) {
                            if (bool) {
                                disconnectFromVnp();
                            } else {
                                connectToVpn();
                            }
                        }
                    });
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

    public void handleError(Throwable th) {
        Log.w(TAG, th);
        if (th instanceof NetworkRelatedException) {
            showMessage("Check internet connection");
        } else if (th instanceof VpnException) {
            if (th instanceof VpnPermissionRevokedException) {
                showMessage("User revoked vpn permissions");
            } else if (th instanceof VpnPermissionDeniedException) {
                showMessage("User canceled to grant vpn permissions");
            } else if (th instanceof HydraVpnTransportException) {
                HydraVpnTransportException hydraVpnTransportException = (HydraVpnTransportException) th;
                if (hydraVpnTransportException.getCode() == 181) {
                    showMessage("Connection with vpn server was lost");
                } else if (hydraVpnTransportException.getCode() == 191) {
                    showMessage("Client traffic exceeded");
                } else {
                    showMessage("Error in VPN transport");
                }
            } else {
                showMessage("Error in VPN Service");
            }
        } else if (th instanceof PartnerApiException) {
            String content = ((PartnerApiException) th).getContent();
            char c = 65535;
            int hashCode = content.hashCode();
            if (hashCode != -1928371114) {
                if (hashCode == -157160793 && content.equals("NOT_AUTHORIZED")) {
                    c = 0;
                }
            } else if (content.equals("TRAFFIC_EXCEED")) {
                c = 1;
            }
            if (c == 0) {
                showMessage("User unauthorized");
            } else if (c != 1) {
                showMessage("Other error. Check PartnerApiException constants");
            } else {
                showMessage("Server unavailable");
            }
        }
    }
}
