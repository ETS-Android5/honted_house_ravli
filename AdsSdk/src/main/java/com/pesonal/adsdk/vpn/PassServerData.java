package com.pesonal.adsdk.vpn;

import com.pesonal.adsdk.model.vpnmodel.CountryListItem;
import com.pesonal.adsdk.model.vpnmodel.ServerListItem;

import java.util.List;

public interface PassServerData {
    void getSelectedServer(List<ServerListItem> country, CountryListItem countryListItem);
}