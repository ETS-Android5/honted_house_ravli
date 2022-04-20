package com.pesonal.adsdk.remote;

public enum AdvertisementState {
   FIRST_TIME_AD_OFF, // AD_VISIBLE
   AD_STATUS_FALSE, // getApp_adShowStatus
   AD_TYPE_OFF,


   // SPLASH Inter Response,
   SPLASH_INTER_AD_RESPONSE_NULL,
   SPLASH_INTER_AD_APPSETTING_NULL,

   // START Inter Response,
   STARTSCREEN_INTER_AD_RESPONSE_NULL,
   STARTSCREEN_INTER_AD_APPSETTING_NULL,

   // Inter Response
   INTER_AD_RESPONSE_NULL,
   INTER_AD_APPSETTING_NULL,
   INTER_AD_STATUS_FALSE,
   INTER_AD_CLOSE,
   INTER_AD_FAILED_TO_SHOW,

   // OPEN AD
   OPEN_AD_RESPONSE_NULL,
   OPEN_AD_APPSETTING_NULL,
   OPEN_AD_STATUS_FALSE,
   OPEN_AD_CLOSE,

   // CUSTOM OPEN AD
   CUSTOM_OPEN_AD_RESPONSE_NULL,
   CUSTOM_OPEN_AD_APPSETTING_NULL,
   CUSTOM_OPEN_AD_CLOSE,
   CUSTOM_OPEN_AD_FAIL,
   CUSTOM_OPEN_AD_FAIL_FROM_RESPONSE,

   // Custom Inter Response
   CUSTOM_INTER_AD_CLOSE,
   CUSTOM_INTER_AD_FAIL,
   CUSTOM_INTER_AD_FAIL_FROM_RESPONSE,

   // Custom Reward Response
   CUSTOM_REWARD_AD_CLOSE,
   CUSTOM_REWARD_AD_FAIL,
   CUSTOM_REWARD_AD_FAIL_FROM_RESPONSE,

   // Custom Native Response
   CUSTOM_NATIVE_AD_FAIL_FROM_RESPONSE,
   CUSTOM_NATIVE_AD_SHOW,

   // Custom Native Response
   CUSTOM_BANNER_AD_FAIL_FROM_RESPONSE,
   CUSTOM_BANNER_AD_SHOW,

   // QUREKA
   QUREKA_INTER_AD_CLOSE,
   QUREKA_NATIVE_BANNER_AD_SHOW,
   QUREKA_BANNER_AD_SHOW,
   QUREKA_NATIVE_AD_SHOW,
   QUREKA_REWARD_AD_CLOSE,


   // Banner
   BANNER_AD_RESPONSE_NULL,
   BANNER_AD_APPSETTING_NULL,
   BANNER_AD_STATUS_FALSE,
   BANNER_AD_ID_EMPTY,
   BANNER_AD_FAIL,
   BANNER_AD_SHOW,
   BANNER_AD_CLICKED,
   BANNER_AD_IMPRESSION,

   // NATIVE BANNER,
   NATIVE_BANNER_AD_RESPONSE_NULL,
   NATIVE_BANNER_AD_APPSETTING_NULL,
   NATIVE_BANNER_AD_FAIL,
   NATIVE_BANNER_AD_SHOW,
   NATIVE_BANNER_AD_ID_EMPTY,

   // NATIVE
   NATIVE_AD_RESPONSE_NULL,
   NATIVE_AD_APPSETTING_NULL,
   NATIVE_AD_SHOW,
   NATIVE_AD_FAIL,
   NATIVE_AD_ID_EMPTY,

   // Reward,
   REWARD_AD_RESPONSE_NULL,
   REWARD_AD_APPSETTING_NULL,
   REWARD_AD_CLOSE,
   REWARD_AD_SHOW_FAIL,
}