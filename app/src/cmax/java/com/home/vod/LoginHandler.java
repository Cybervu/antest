package com.home.vod;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.home.vod.activity.LoginActivity;
import com.home.vod.preferences.LanguagePreference;

import static com.home.vod.preferences.LanguagePreference.DEFAULT_GMAIL_SIGNIN;
import static com.home.vod.preferences.LanguagePreference.GMAIL_SIGNIN;

/**
 * Created by Android on 9/21/2017.
 */

public class LoginHandler {
    LoginActivity context;
    TextView gmailTest;
    private RelativeLayout googleSignView;
    private Button loginButton;
    private LinearLayout btnLogin;
    private LanguagePreference languagePreference;
    public LoginHandler(LoginActivity context){
        this.context=context;
        gmailTest=(TextView) context.findViewById(R.id.textView);
        googleSignView = (RelativeLayout) context.findViewById(R.id.sign_in_button);
        btnLogin = (LinearLayout) context.findViewById(R.id.btnLogin);
        btnLogin.setVisibility(View.GONE);

    }

    public void callSignin(LanguagePreference languagePreference){
        gmailTest.setText(languagePreference.getTextofLanguage(GMAIL_SIGNIN, DEFAULT_GMAIL_SIGNIN));
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
