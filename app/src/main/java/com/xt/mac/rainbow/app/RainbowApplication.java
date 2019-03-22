package com.xt.mac.rainbow.app;

import android.app.Application;

public class RainbowApplication extends Application {

    private static RainbowApplication instance = null;

    /*
    * 单例访问全局变量
    * */
    public static RainbowApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }


    private String userID = "";

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
