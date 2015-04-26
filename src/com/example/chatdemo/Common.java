package com.example.chatdemo;

import android.app.Application;

/**
 * Created by jeffreyfried on 4/4/15.
 */
public class Common extends Application {
    private static String baseUrl = "http://10.0.0.4:3000";
    private static String myEmail = "clevcollc@gmail.com";
    public final static String REGID = "regid";
    public final static String EMAIL = "email";
    public final static String MESSAGE = "message";
    public final static String TO = "to";
    public final static String FROM = "from";

    private final static String registerUrl = baseUrl + "/register";

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static String getServerUrl() {
        return baseUrl;
    }

    public static String getRegisterUrl() {
        return registerUrl;
    }

    public static String getEmail() {
        return myEmail;
    }

}
