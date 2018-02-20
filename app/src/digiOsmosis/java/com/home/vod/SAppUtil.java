package com.home.vod;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

public class SAppUtil {
    public static int getAppVersionCode(Context context) {
        int version = -1;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;

            Log.v("SUBHA","version -===== "+ version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();

        }

        return version;
    }
}
