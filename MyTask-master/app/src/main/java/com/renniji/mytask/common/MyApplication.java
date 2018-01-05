package com.renniji.mytask.common;

import android.app.Application;
import android.content.Context;

/**
 * Created by heshicaihao on 2017/1/21.
 */

public class MyApplication  extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
//        initData();

    }

    /**
     * 获得当前app运行的Context
     *
     * @return
     */
    public static Context getContext() {
        return context;
    }

}
