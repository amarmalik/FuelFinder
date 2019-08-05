package com.amazingtech.petrolfinder;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Akshay Raj on 7/17/2016.
 * Snow Corporation Inc.
 * www.snowcorp.org
 */
public class MyApplication extends Application {
    public static final String TAG = MyApplication.class.getSimpleName();
    private RequestQueue mRequestQueue;

    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
       /* String hashKey=printHashKey();
        Log.d("hashKey::",hashKey);*/
        mInstance = this;
        MultiDex.install(this);
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
  /*  public String printHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(this.getPackageName(),PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(),Base64.DEFAULT);
            }
                *//*Log.d("COM", Base64.encodeToString(md.digest(), Base64.DEFAULT));*//*

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }*/
}