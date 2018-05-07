package com.home.vod;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.home.vod.activity.ProfileActivity;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.util.FontUtls;

import player.utils.Util;

import static com.home.vod.preferences.LanguagePreference.ALERT;
import static com.home.vod.preferences.LanguagePreference.CONFIRM_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ALERT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CONFIRM_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FAILURE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FIRST_NAME;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LAST_NAME;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NAME_HINT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NEW_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PASSWORDS_DO_NOT_MATCH;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TEXT_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_VALID_CONFIRM_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.FAILURE;
import static com.home.vod.preferences.LanguagePreference.FIRST_NAME;
import static com.home.vod.preferences.LanguagePreference.LAST_NAME;
import static com.home.vod.preferences.LanguagePreference.NAME_HINT;
import static com.home.vod.preferences.LanguagePreference.NEW_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.PASSWORDS_DO_NOT_MATCH;
import static com.home.vod.preferences.LanguagePreference.TEXT_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.VALID_CONFIRM_PASSWORD;

/**
 * Created by MUVI on 10/27/2017.
 */

public class ProfileHandler {
    private Activity context;
    EditText editNewPassword;
    EditText editConfirmPassword;
    EditText editProfileNameEditText;
    LanguagePreference languagePreference;
    public String first_nameStr="",last_nameStr="";
    public String final_name = "";
    public String phoneStr="";
    String newPasswod;

    public ProfileHandler(Activity context){
        this.context=context;
        editProfileNameEditText = (EditText) context.findViewById(R.id.name);

        editNewPassword = (EditText) context.findViewById(R.id.pwd);
        editConfirmPassword = (EditText) context.findViewById(R.id.confirm_pass);

        newPasswod=editNewPassword.getText().toString().trim();


        FontUtls.loadFont(context, context.getResources().getString(R.string.light_fonts), editProfileNameEditText);
        FontUtls.loadFont(context, context.getResources().getString(R.string.light_fonts), editNewPassword);
        FontUtls.loadFont(context, context.getResources().getString(R.string.light_fonts), editConfirmPassword);
        languagePreference = LanguagePreference.getLanguagePreference(context);
        editProfileNameEditText.setHint(languagePreference.getTextofLanguage(NAME_HINT,DEFAULT_NAME_HINT));


    }
    public void updateProfileHandler() {

        if (editProfileNameEditText.getText().toString().matches("")) {
            ((ProfileActivity) context).ShowDialog(languagePreference.getTextofLanguage(FAILURE, DEFAULT_FAILURE), languagePreference.getTextofLanguage(FIRST_NAME, DEFAULT_FIRST_NAME).toString().toLowerCase());
            return;
        }

        if(!editNewPassword.getText().toString().trim().equals("") || !editConfirmPassword.getText().toString().trim().equals("")) {

            if (editNewPassword.getText().toString().trim().equals("")) {
                ((ProfileActivity) context).ShowDialog(languagePreference.getTextofLanguage(FAILURE, DEFAULT_FAILURE), languagePreference.getTextofLanguage(TEXT_PASSWORD, DEFAULT_TEXT_PASSWORD).toString().toLowerCase());

                return;
            }

            if (editConfirmPassword.getText().toString().trim().equals("")) {
                ((ProfileActivity) context).ShowDialog(languagePreference.getTextofLanguage(FAILURE, DEFAULT_FAILURE), languagePreference.getTextofLanguage(VALID_CONFIRM_PASSWORD, DEFAULT_VALID_CONFIRM_PASSWORD).toString().toLowerCase());

                return;
            }
        }


         if (!((ProfileActivity) context).passwordMatchValidation()) {
            ((ProfileActivity) context).ShowDialog(languagePreference.getTextofLanguage(ALERT, DEFAULT_ALERT), languagePreference.getTextofLanguage(PASSWORDS_DO_NOT_MATCH, DEFAULT_PASSWORDS_DO_NOT_MATCH));

        }else {
            if (NetworkStatus.getInstance().isConnected(context)) {
                InputMethodManager inputManager = (InputMethodManager)
                        context.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                first_nameStr = editProfileNameEditText.getText().toString().trim();
                ((ProfileActivity) context).UpdateProfile(first_nameStr,last_nameStr,phoneStr);

            }
        }
    }


    public void setNameTxt(String nameString,String last_name,String phoneNumber){

        try {
            editProfileNameEditText.setText(nameString.trim());
            editProfileNameEditText.setSelection(editProfileNameEditText.getText().length());
        }catch (Exception e){}


    }

}
