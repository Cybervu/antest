package com.home.vod.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;



import com.home.vod.R;
import com.home.vod.preferences.LanguagePreference;
//import player.utils.Util;
import com.home.api.player.utils.Util;

import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;

public class AlertActivity extends Activity{

    LanguagePreference languagePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_activity);
        languagePreference = LanguagePreference.getLanguagePreference(this);




        android.support.v7.app.AlertDialog.Builder dlgAlert = new android.support.v7.app.AlertDialog.Builder(AlertActivity.this, R.style.MyAlertDialogStyle);
        dlgAlert.setMessage(getIntent().getStringExtra("msg"));
        dlgAlert.setTitle("");

        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Util.call_finish_at_onUserLeaveHint = true;
                dialog.dismiss();
                finish();
            }
        });



        // dlgAlert.setPositiveButton(getResources().getString(R.string.yes_str), null);
        dlgAlert.setCancelable(false);
        dlgAlert.create().show();

    }

}
