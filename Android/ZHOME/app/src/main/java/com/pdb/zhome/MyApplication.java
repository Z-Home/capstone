package com.pdb.zhome;

import android.app.Application;

/**
 * Created by brmqk3 on 11/24/2014.
 */
public class MyApplication extends Application {

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;
}
