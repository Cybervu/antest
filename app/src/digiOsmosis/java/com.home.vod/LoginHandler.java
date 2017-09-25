package com.home.vod;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.vod.activity.LoginActivity;

/**
 * Created by Android on 9/21/2017.
 */

public class LoginHandler {
    LoginActivity context;
    private RelativeLayout googleSignView;
    private Button loginButton;
    private LanguagePreference languagePreference;

    public LoginHandler(LoginActivity context){
        this.context=context;
        googleSignView = (RelativeLayout) context.findViewById(R.id.sign_in_button);


    }

    public void callSignin(){

        googleSignView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               context.signIn();
            }
        });
    }
    public void callFblogin(final CallbackManager callbackManager, Button loginButton, LanguagePreference languagePreference){

    }

}
