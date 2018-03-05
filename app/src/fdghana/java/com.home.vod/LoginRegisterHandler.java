package com.home.vod;

import android.app.Activity;
import android.content.Intent;

import com.home.vod.activity.PreLoginActivity;

/**
 * Created by Muvi Guest on 1/2/2018.
 */

public class LoginRegisterHandler {
    Activity activity;

    public LoginRegisterHandler(Activity activity) {
        this.activity = activity;
    }

    public Intent login() {
        return new Intent(activity, PreLoginActivity.class);
    }
}
