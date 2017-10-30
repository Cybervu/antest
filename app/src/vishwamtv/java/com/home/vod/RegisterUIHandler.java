package com.home.vod;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;

import java.util.Arrays;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * Created by BISHAL on 21-08-2017.
 */

public class RegisterUIHandler {
    private Activity context;
    List<String> country_List, country_Code_List,language_List,language_Code_List;
    Spinner country_spinner, language_spinner;
    private EditText editName;
    ArrayAdapter<String> Language_arrayAdapter, Country_arrayAdapter;
    public  String selected_Language_Id="", selected_Country_Id="",regNameStr;
    private LanguagePreference languagePreference;

    public RegisterUIHandler(Activity context){
        this.context=context;
        country_spinner = (Spinner) context.findViewById(R.id.countrySpinner);
        language_spinner = (Spinner) context.findViewById(R.id.languageSpinner);
        editName = (EditText) context.findViewById(R.id.editNameStr);
        languagePreference = LanguagePreference.getLanguagePreference(context);
    }
    public void setCountryList(PreferenceManager preferenceManager){

        country_List = Arrays.asList (context.getResources().getStringArray(R.array.country));
        country_Code_List = Arrays.asList(context.getResources().getStringArray(R.array.countrycode));
        language_List = Arrays.asList(context.getResources().getStringArray(R.array.languages));
        language_Code_List = Arrays.asList(context.getResources().getStringArray(R.array.languagesCode));


        Language_arrayAdapter = new ArrayAdapter<String>(context, R.layout.country_language_spinner, language_List) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                FontUtls.loadFont(context,context.getResources().getString(R.string.light_fonts),(TextView) v);

                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                FontUtls.loadFont(context,context.getResources().getString(R.string.light_fonts),(TextView) v);


                return v;
            }

        };

        language_spinner.setAdapter(Language_arrayAdapter);

        Country_arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.country_language_spinner, country_List) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                FontUtls.loadFont(context,context.getResources().getString(R.string.light_fonts),(TextView) v);
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                FontUtls.loadFont(context,context.getResources().getString(R.string.light_fonts),(TextView) v);

                return v;
            }

        };

        country_spinner.setAdapter(Country_arrayAdapter);

        selected_Country_Id =
                preferenceManager.getCountryCodeFromPref();
        LogUtil.showLog("MUVI", "primary Selected_Country_Id=" + selected_Country_Id);
        if (selected_Country_Id.equals("0")) {
            country_spinner.setSelection(224);
            selected_Country_Id = country_Code_List.get(224);
            LogUtil.showLog("MUVI", "country not  matche" + "==" + selected_Country_Id);
        } else {
            for (int i = 0; i < country_Code_List.size(); i++) {

                LogUtil.showLog("MUVI", "Country names =" + country_Code_List.get(i));

                if (selected_Country_Id.trim().equals(country_Code_List.get(i))) {
                    country_spinner.setSelection(i);
                    selected_Country_Id = country_Code_List.get(i);

                    LogUtil.showLog("MUVI", "country  matched =" + selected_Country_Id);
                }
            }
        }

        Country_arrayAdapter.notifyDataSetChanged();


        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                selected_Country_Id = country_Code_List.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        language_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                selected_Language_Id = language_Code_List.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    public void setTermsTextView(LanguagePreference languagePreference) {
        FontUtls.loadFont(context, context.getResources().getString(R.string.light_fonts), editName);
        editName.setHint(languagePreference.getTextofLanguage(NAME_HINT, DEFAULT_NAME_HINT));
    }
    public void getRegisterName(){
        regNameStr = editName.getText().toString().trim();
        if (!regNameStr.equals("")) {
            ((RegisterActivity) context).registerButtonClicked(regNameStr);
        }else {
            Toast.makeText(context, languagePreference.getTextofLanguage(ENTER_REGISTER_FIELDS_DATA, DEFAULT_ENTER_REGISTER_FIELDS_DATA), Toast.LENGTH_LONG).show();
        }
    }
    public void callFblogin(final CallbackManager callbackManager, Button loginButton, LanguagePreference languagePreference){

    }

    public void callSignin(LanguagePreference languagePreference){

    }

}
