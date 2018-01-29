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
import com.home.vod.util.Util;


import static com.home.vod.preferences.LanguagePreference.DEFAULT_ENTER_REGISTER_FIELDS_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FAILURE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FIRST_NAME;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_INVALID_PHONE_NUMBER;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LAST_NAME;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MOBILE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NAME_HINT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PASSWORDS_DO_NOT_MATCH;
import static com.home.vod.preferences.LanguagePreference.ENTER_REGISTER_FIELDS_DATA;
import static com.home.vod.preferences.LanguagePreference.FAILURE;
import static com.home.vod.preferences.LanguagePreference.FIRST_NAME;
import static com.home.vod.preferences.LanguagePreference.INVALID_PHONE_NUMBER;
import static com.home.vod.preferences.LanguagePreference.LAST_NAME;
import static com.home.vod.preferences.LanguagePreference.MOBILE;
import static com.home.vod.preferences.LanguagePreference.NAME_HINT;
import static com.home.vod.preferences.LanguagePreference.PASSWORDS_DO_NOT_MATCH;

/**
 * Created by MUVI on 10/27/2017.
 */

public class ProfileHandler {
    private Activity context;
    EditText editProfileNameEditText,phoneNumberEditText;
    LanguagePreference languagePreference;
    public String first_nameStr="",last_nameStr="";
    public String final_name = "";
    public String phoneStr="";

    public ProfileHandler(Activity context){
        this.context=context;
        editProfileNameEditText = (EditText) context.findViewById(R.id.editProfileNameEditText);
        phoneNumberEditText = (EditText) context.findViewById(R.id.phoneNumberEditText);
        FontUtls.loadFont(context, context.getResources().getString(R.string.light_fonts), editProfileNameEditText);
        FontUtls.loadFont(context, context.getResources().getString(R.string.light_fonts), phoneNumberEditText);
        languagePreference = LanguagePreference.getLanguagePreference(context);
        editProfileNameEditText.setHint(languagePreference.getTextofLanguage(NAME_HINT,DEFAULT_NAME_HINT));
        phoneNumberEditText.setHint(languagePreference.getTextofLanguage(MOBILE,DEFAULT_MOBILE));


    }
    public void updateProfileHandler() {

        boolean isValidPhonenumber = Util.isValidPhone(phoneNumberEditText.getText().toString().trim());

        if (editProfileNameEditText.getText().toString().matches("")) {
            ((ProfileActivity) context).ShowDialog(languagePreference.getTextofLanguage(FAILURE, DEFAULT_FAILURE), languagePreference.getTextofLanguage(FIRST_NAME, DEFAULT_FIRST_NAME).toString().toLowerCase());
            return;
        }
        else if(phoneNumberEditText.getText().toString().matches("")){
            ((ProfileActivity) context).ShowDialog(languagePreference.getTextofLanguage(ENTER_REGISTER_FIELDS_DATA,DEFAULT_ENTER_REGISTER_FIELDS_DATA), languagePreference.getTextofLanguage(INVALID_PHONE_NUMBER,DEFAULT_INVALID_PHONE_NUMBER));
            return;
        }
       // boolean isValidPhonenumber = Util.isValidPhone(phoneNumberEditText.getText().toString().trim());

        else if (!((ProfileActivity) context).passwordMatchValidation()) {
            ((ProfileActivity) context).ShowDialog(languagePreference.getTextofLanguage(FAILURE, DEFAULT_FAILURE), languagePreference.getTextofLanguage(PASSWORDS_DO_NOT_MATCH, DEFAULT_PASSWORDS_DO_NOT_MATCH));

        }
        else if (!isValidPhonenumber) {
            ((ProfileActivity) context).ShowDialog(languagePreference.getTextofLanguage(FAILURE,DEFAULT_FAILURE), languagePreference.getTextofLanguage(INVALID_PHONE_NUMBER,DEFAULT_INVALID_PHONE_NUMBER));
            return;
        }
        else {
            if (NetworkStatus.getInstance().isConnected(context)) {
                InputMethodManager inputManager = (InputMethodManager)
                        context.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                final_name = editProfileNameEditText.getText().toString().trim();
                phoneStr = phoneNumberEditText.getText().toString().trim();
                ((ProfileActivity) context).UpdateProfile(first_nameStr,last_nameStr,phoneStr);

            }

        }
    }


    public void setNameTxt(String nameString,String last_name,String phoneNumber){
        editProfileNameEditText.setText(nameString.trim());

    }

}
