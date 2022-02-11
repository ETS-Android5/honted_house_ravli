package com.pesonal.adsdk.model.vpnmodel;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ResponseVpn{

	@SerializedName("country_list")
	private List<CountryListItem> countryList;

	public void setCountryList(List<CountryListItem> countryList){
		this.countryList = countryList;
	}

	public List<CountryListItem> getCountryList(){
		return countryList;
	}

	@Override
 	public String toString(){
		return 
			"ResponseVpn{" + 
			"country_list = '" + countryList + '\'' + 
			"}";
		}
}