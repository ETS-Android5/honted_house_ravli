package com.pesonal.adsdk.model.vpnmodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class CountryListItem{

	@SerializedName("country_name")
	private String countryName;

	@SerializedName("flag_url")
	private String flagUrl;

	@SerializedName("server_list")
	private List<ServerListItem> serverList;

	public void setCountryName(String countryName){
		this.countryName = countryName;
	}

	public String getCountryName(){
		return countryName;
	}

	public void setFlagUrl(String flagUrl){
		this.flagUrl = flagUrl;
	}

	public String getFlagUrl(){
		return flagUrl;
	}

	public void setServerList(List<ServerListItem> serverList){
		this.serverList = serverList;
	}

	public List<ServerListItem> getServerList(){
		return serverList;
	}

	@Override
 	public String toString(){
		return 
			"CountryListItem{" + 
			"country_name = '" + countryName + '\'' + 
			",flag_url = '" + flagUrl + '\'' + 
			",server_list = '" + serverList + '\'' + 
			"}";
		}
}