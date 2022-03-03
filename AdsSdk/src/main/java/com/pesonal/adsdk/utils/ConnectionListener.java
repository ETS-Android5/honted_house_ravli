package com.pesonal.adsdk.utils;

import com.pesonal.adsdk.vpn.CONNECTION_STATE;

public interface ConnectionListener {
    void onStatus(CONNECTION_STATE isConnect, String connectionState);
}
