package com.amazingtech.petrolfinder;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import java.util.HashMap;

@SuppressLint("CommitPrefEdits")
public class SessionForm {

      public static final String KEY_loginstatus = "loginstatus";
	public static final String KEY_email = "email";
	public static final String KEY_name = "name";
	public static final String KEY_mobile = "mobile";
	public static final String Key_password = "password";
	public static final String KEY_user_id = "user_id";
	public static final String KEY_user_status = "user_status";
	public static final String KEY_device_token = "device_token";
	public static final String Key_category_id="category_id";
	public static final String Key_category_name="category_name";
	public static final String Key_category_icon="category_icon";

	public static final String Key_city_name="city_name";
	public static final String Key_city_id="city_id";

	public static final String KEY_add_title="add_title";
	public static final String KEY_add_id="add_id";
	public static final String KEY_add_city_id="add_city_id";
	public static final String KEY_add_image="add_image";
	public static final String KEY_add_category_id="add_category_id";
	public static final String KEY_add_description="add_description";
	public static final String KEY_add_price="add_price";
	public static final String Key_add_image="add_image";
	public static final String KEY_owner_mobile="owner_mobile";
	public static final String KEY_owner_name="owner_name";
	public static final String KEY_owner_email="owner_email";
	public static final String KEY_add_address="add_address";
	public static final String KEY_add_latt="add_latt";
	public static final String KEY_add_long="add_long";

	public static final String Key_area_id="area_id";
	public static final String Key_area_name="area_name";

	public static final String KEY_personName="personName";
	public static final String KEY_personEmail="email";
	public static final String KEY_googlesign_user_image="personPhotoUrl";



	public static HashMap<String,String>selectedmap=new HashMap<String, String>();

	public static String GetSharedPreferences(String KeyValue, Context _context) {
		SharedPreferences SP = PreferenceManager
				.getDefaultSharedPreferences(_context);
		return SP.getString(KeyValue, "");

	}

	public static void SetSharedPreferences(String KeyValue, String keyString, Context _context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(_context);
		Editor spe = prefs.edit();
		spe.putString(KeyValue, keyString);
		spe.commit();
	}
	}
