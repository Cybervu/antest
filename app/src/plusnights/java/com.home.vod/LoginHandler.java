package com.home.vod;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.home.vod.activity.LoginActivity;
import com.home.vod.preferences.LanguagePreference;

/**
 * Created by Android on 9/21/2017.
 */

public class LoginHandler {
    LoginActivity context;
    private RelativeLayout GoogleSignView;
    public LoginHandler(LoginActivity context){
        this.context=context;
       // GoogleSignView = (RelativeLayout) context.findViewById(R.id.sign_in_button);


    }

    public void callSignin(LanguagePreference languagePreference){

    }
    public void callFblogin(final CallbackManager callbackManager, Button loginButton, LanguagePreference languagePreference){

    }
}
