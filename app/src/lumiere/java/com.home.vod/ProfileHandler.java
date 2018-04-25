package com.home.vod;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.home.vod.activity.ProfileActivity;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.util.FontUtls;

import static com.home.vod.preferences.LanguagePreference.DEFAULT_FAILURE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FIRST_NAME;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LAST_NAME;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PASSWORDS_DO_NOT_MATCH;
import static com.home.vod.preferences.LanguagePreference.FAILURE;
import static com.home.vod.preferences.LanguagePreference.FIRST_NAME;
import static com.home.vod.preferences.LanguagePreference.LAST_NAME;
import static com.home.vod.preferences.LanguagePreference.PASSWORDS_DO_NOT_MATCH;

/**
 * Created by MUVI on 10/27/2017.
 */

public class ProfileHandler {
    private Activity context;
    EditText editProfileNameEditText_first,editProfileNameEditText_last;
    LanguagePreference languagePreference;
    public String first_nameStr,last_nameStr;
    public String final_name = "";
    public String last_name="";

    public String phoneStr="";
    TextView name_of_user;

    public ProfileHandler(Activity context){
        this.context=context;
        editProfileNameEditText_first = (EditText) context.findViewById(R.id.editProfileNameEditText_first);
        editProfileNameEditText_last = (EditText) context.findViewById(R.id.editProfileNameEditText_last);
        name_of_user = (TextView) context.findViewById(R.id.name_of_user);
        FontUtls.loadFont(context, context.getResources().getString(R.string.light_fonts), editProfileNameEditText_first);
        FontUtls.loadFont(context, context.getResources().getString(R.string.light_fonts), editProfileNameEditText_last);
        languagePreference = LanguagePreference.getLanguagePreference(context);
        editProfileNameEditText_first.setHint(languagePreference.getTextofLanguage(FIRST_NAME,DEFAULT_FIRST_NAME));
        editProfileNameEditText_last.setHint(languagePreference.getTextofLanguage(LAST_NAME,DEFAULT_LAST_NAME));

    }
    public void updateProfileHandler() {

        if (editProfileNameEditText_first.getText().toString().trim().matches("")) {
            ((ProfileActivity) context).ShowDialog(languagePreference.getTextofLanguage(FAILURE, DEFAULT_FAILURE), languagePreference.getTextofLanguage(FIRST_NAME, DEFAULT_FIRST_NAME).toString().toLowerCase());
            return;
        } else if (editProfileNameEditText_last.getText().toString().trim().matches("")) {
            ((ProfileActivity) context).ShowDialog(languagePreference.getTextofLanguage(FAILURE, DEFAULT_FAILURE), languagePreference.getTextofLanguage(LAST_NAME, DEFAULT_LAST_NAME).toString().toLowerCase());
            return;
        }
        else if (!((ProfileActivity) context).passwordMatchValidation()) {
            ((ProfileActivity) context).ShowDialog(languagePreference.getTextofLanguage(FAILURE, DEFAULT_FAILURE), languagePreference.getTextofLanguage(PASSWORDS_DO_NOT_MATCH, DEFAULT_PASSWORDS_DO_NOT_MATCH));

        }else {
            if (NetworkStatus.getInstance().isConnected(context)) {
                InputMethodManager inputManager = (InputMethodManager)
                        context.getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                first_nameStr = editProfileNameEditText_first.getText().toString().trim();
                last_nameStr = editProfileNameEditText_last.getText().toString().trim();
              //  final_name = first_nameStr + " " + last_nameStr;
                ((ProfileActivity) context).UpdateProfile(first_nameStr,last_nameStr,phoneStr);

            }

        }
    }


    public void setNameTxt(String first_name, String last_name, String phoneNumber) {

        try {
            editProfileNameEditText_first.setText(first_name.trim());
            editProfileNameEditText_last.setText(last_name.trim());
            editProfileNameEditText_first.setSelection(editProfileNameEditText_first.getText().length());
            editProfileNameEditText_last.setSelection(editProfileNameEditText_last.getText().length());
            name_of_user.setText(first_name+" "+last_name);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
