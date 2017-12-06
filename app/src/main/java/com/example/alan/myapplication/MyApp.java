package com.example.alan.myapplication;

import android.app.Application;

/**
 * Created by Alan on 2017/12/6.
 */

public class MyApp extends Application {

    public static MyApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static MyApp getInstance() {
        return mInstance;
    }


}
