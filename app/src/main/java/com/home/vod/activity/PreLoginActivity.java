package com.home.vod.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.home.vod.R;

public class PreLoginActivity extends AppCompatActivity {

    public Button loginBtn;
    public Button btnFbLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);

        btnFbLogin= (Button)findViewById(R.id.loginWithFacebookButton);
        btnFbLogin.setText("Login With Facebook");

        loginBtn=(Button)findViewById(R.id.loginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logIntent=new Intent(PreLoginActivity.this,FdGhana_loginActivity.class);
                startActivity(logIntent);
            }
        });


    }
}
