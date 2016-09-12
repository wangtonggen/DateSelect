package com.dateselect;

import android.app.Application;
import android.content.Context;

/**
 * Created by apple
 * Date 16/6/23
 * Desc 描述
 */
public class BaseApplication extends Application {
    public static Context instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
