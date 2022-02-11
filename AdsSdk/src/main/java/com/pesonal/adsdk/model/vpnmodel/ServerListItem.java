package com.pesonal.adsdk.model.vpnmodel;

import com.google.gson.annotations.SerializedName;

public class ServerListItem{

	@SerializedName("city_name")
	private String cityName;

	@SerializedName("config")
	private String config;

	public void setCityName(String cityName){
		this.cityName = cityName;
	}

	public String getCityName(){
		return cityName;
	}

	public void setConfig(String config){
		this.config = config;
	}

	public String getConfig(){
		return config;
	}

	@Override
 	public String toString(){
		return 
			"ServerListItem{" + 
			"city_name = '" + cityName + '\'' + 
			",config = '" + config + '\'' + 
			"}";
		}
}