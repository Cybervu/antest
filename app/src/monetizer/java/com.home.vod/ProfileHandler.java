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
    public String phoneStr="";

    public ProfileHandler(Activity context){
        this.context=context;
        editProfileNameEditText_first = (EditText) context.findViewById(R.id.editProfileNameEditText_first);
        editProfileNameEditText_last = (EditText) context.findViewById(R.id.editProfileNameEditText_last);
        FontUtls.loadFont(context, context.getResources().getString(R.string.light_fonts), editProfileNameEditText_first);
        FontUtls.loadFont(context, context.getResources().getString(R.string.light_fonts), editProfileNameEditText_last);
        languagePreference = LanguagePreference.getLanguagePreference(context);
        editProfileNameEditText_first.setHint(languagePreference.getTextofLanguage(FIRST_NAME,DEFAULT_FIRST_NAME));
        editProfileNameEditText_last.setHint(languagePreference.getTextofLanguage(LAST_NAME,DEFAULT_LAST_NAME));

    }
    public void updateProfileHandler() {

        if (editProfileNameEditText_first.getText().toString().matches("")) {
            ((ProfileActivity) context).ShowDialog(languagePreference.getTextofLanguage(FAILURE, DEFAULT_FAILURE), languagePreference.getTextofLanguage(FIRST_NAME, DEFAULT_FIRST_NAME).toString().toLowerCase());
            return;
        } else if (editProfileNameEditText_last.getText().toString().matches("")) {
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
                final_name = first_nameStr + " " + last_nameStr;
                ((ProfileActivity) context).UpdateProfile(final_name,phoneStr);

            }

        }
    }


    public void setNameTxt(String nameString,String phoneNumber){
        if(nameString.contains(" "))
        {

            String data[] = nameString.split(" ");
            String fname = "";

            Log.v("BIBHU2","name===size==="+data.length);

            for(int i=0 ;i<data.length-1;i++)
            {
                fname = fname+" "+data[i];
                Log.v("BIBHU2","loop name===="+fname);
            }

            editProfileNameEditText_first.setText(fname.trim());
            editProfileNameEditText_last.setText(data[data.length-1]);
        }
        else
        {
            editProfileNameEditText_first.setText(nameString.trim());
            editProfileNameEditText_last.setText("");
        }

    }

}
