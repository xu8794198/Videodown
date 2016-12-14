package com.example.videoplayer.entity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import com.example.videoplayer.PhoneApplication;

/**
 * 网络状态检查工具类
 */
public class NetUtils {

    /**
     * 网络状态
     */
    public enum Status {
        /**
         * 无网络
         */
        NONE,
        /**
         * Wi-Fi
         */
        WIFI,
        /**
         * 3G,GPRS
         */
        MOBILE
    }

    /**
     * 获取当前网络状态
     *
     * @param  context
     * @return
     */
    public static Status getNetworkState() {
        ConnectivityManager connManager = (ConnectivityManager) PhoneApplication.CONTEXT
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        State state;
        // 手机网络判断
        NetworkInfo mobileInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(mobileInfo != null) {
        	state = mobileInfo.getState();
            if (state == State.CONNECTED || state == State.CONNECTING) {
                return Status.MOBILE;
            }
        }

        // Wifi网络判断
        NetworkInfo wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiInfo != null) {
        	state = wifiInfo.getState();
            if (state == State.CONNECTED || state == State.CONNECTING) {
                return Status.WIFI;
            }
        }
        
        return Status.NONE;
    }
    
    public static final boolean isNetworkAvailable() {
    	return getNetworkState() != Status.NONE;
    }
}
