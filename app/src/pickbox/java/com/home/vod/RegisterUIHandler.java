package com.home.vod;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.home.vod.activity.RegisterActivity;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FeatureHandler;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.home.vod.preferences.LanguagePreference.AGREE_TERMS;
import static com.home.vod.preferences.LanguagePreference.CHOOSE_COUNTRY_ALERT_MESSAGE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_AGREE_TERMS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CHOOSE_COUNTRY_ALERT_MESSAGE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DETAILS_NOT_FOUND_ALERT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ENTER_REGISTER_FIELDS_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_GMAIL_SIGNIN;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGIN_FACEBOOK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NAME_HINT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TERMS;
import static com.home.vod.preferences.LanguagePreference.DETAILS_NOT_FOUND_ALERT;
import static com.home.vod.preferences.LanguagePreference.ENTER_REGISTER_FIELDS_DATA;
import static com.home.vod.preferences.LanguagePreference.GMAIL_SIGNIN;
import static com.home.vod.preferences.LanguagePreference.LOGIN_FACEBOOK;
import static com.home.vod.preferences.LanguagePreference.NAME_HINT;
import static com.home.vod.preferences.LanguagePreference.TERMS;

/**
 * Created by BISHAL on 21-08-2017.
 */

public class RegisterUIHandler {
    private Activity context;
    private TextView termsTextView, termsTextView1;
    private LinearLayout btnLogin;
    LoginButton loginWithFacebookButton;
    private EditText editName;
    String fbUserId = "";
    TextView gmailTest;
    private RelativeLayout googleSignView;
    String fbEmail = "";
    String fbName = "";
    public String selected_Language_Id = "", selected_Country_Id = "", regNameStr, regPhone = "", last_name = "";
    private LanguagePreference languagePreference;
    private Button registerButton;
    TextView fbLoginTextView;
    List<String> country_List, country_Code_List;
    Spinner country_spinner;
    ArrayAdapter<String> Country_arrayAdapter;
    public RegisterUIHandler(Activity context) {
        this.context = context;
        termsTextView = (TextView) context.findViewById(R.id.termsTextView);
        termsTextView1 = (TextView) context.findViewById(R.id.termsTextView1);
        loginWithFacebookButton = (LoginButton) context.findViewById(R.id.loginWithFacebookButton);
        loginWithFacebookButton.setVisibility(View.GONE);
        country_spinner = (Spinner) context.findViewById(R.id.countrySpinner);

        btnLogin = (LinearLayout) context.findViewById(R.id.btnLogin);
        gmailTest=(TextView) context.findViewById(R.id.textView);
        googleSignView = (RelativeLayout) context.findViewById(R.id.sign_in_button);
        loginWithFacebookButton.setReadPermissions("public_profile", "email", "user_friends");
        fbLoginTextView = (TextView) context.findViewById(R.id.fbLoginTextView);
        btnLogin.setVisibility(View.VISIBLE);
        btnLogin = (LinearLayout) context.findViewById(R.id.btnLogin);
        googleSignView.setVisibility(View.VISIBLE);
        editName = (EditText) context.findViewById(R.id.editNameStr);
        languagePreference = LanguagePreference.getLanguagePreference(context);



        FeatureHandler featureHandler = FeatureHandler.getFeaturePreference(context);
        if(featureHandler.getFeatureStatus(FeatureHandler.FACEBOOK,FeatureHandler.DEFAULT_FACEBOOK)) {
            btnLogin.setVisibility(View.VISIBLE);
        }else {
            btnLogin.setVisibility(View.GONE);
        }

        if(featureHandler.getFeatureStatus(FeatureHandler.GOOGLE,FeatureHandler.DEFAULT_GOOGLE)) {
            googleSignView.setVisibility(View.VISIBLE);
        }else {
            googleSignView.setVisibility(View.GONE);
        }
    }


    public void setTermsTextView(LanguagePreference languagePreference) {
        termsTextView1.setText(languagePreference.getTextofLanguage(AGREE_TERMS, DEFAULT_AGREE_TERMS));
        termsTextView.setText(languagePreference.getTextofLanguage(TERMS, DEFAULT_TERMS));
        FontUtls.loadFont(context, context.getResources().getString(R.string.light_fonts), editName);
        editName.setHint(languagePreference.getTextofLanguage(NAME_HINT, DEFAULT_NAME_HINT));

        termsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pb.muvi.com/terms-privacy-policy"));
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(browserIntent);
            }
        });
    }

    public void getRegisterName() {
        regNameStr = editName.getText().toString().trim();
        if (!regNameStr.equals("")) {
            ((RegisterActivity) context).registerButtonClicked(regNameStr, last_name, regPhone);
        } else {
            Toast.makeText(context, languagePreference.getTextofLanguage(ENTER_REGISTER_FIELDS_DATA, DEFAULT_ENTER_REGISTER_FIELDS_DATA), Toast.LENGTH_LONG).show();
        }
    }

    public void callFblogin(final CallbackManager callbackManager, Button registerButton, LanguagePreference languagePreference) {
        this.registerButton = registerButton;
        this.languagePreference = languagePreference;
        fbLoginTextView.setText(languagePreference.getTextofLanguage(LOGIN_FACEBOOK,DEFAULT_LOGIN_FACEBOOK));
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginWithFacebookButton.performClick();

                loginWithFacebookButton.setPressed(true);

                loginWithFacebookButton.invalidate();

                loginWithFacebookButton.registerCallback(callbackManager, mCallBack);

                loginWithFacebookButton.setPressed(false);

                loginWithFacebookButton.invalidate();

            }
        });

    }

    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {

            //progressDialog.dismiss();

            // App code
            GraphRequest request = GraphRequest.newMeRequest(
                    loginResult.getAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {


                            JSONObject json = response.getJSONObject();
                            try {
                                if (json != null) {


                                    String fName = "";
                                    if ((json.has("id")) && json.getString("id").trim() != null && !json.getString("id").trim().isEmpty() && !json.getString("id").trim().equals("null") && !json.getString("id").trim().matches("")) {
                                        fbUserId = json.getString("id");
                                    }
                                    if ((json.has("first_name")) && json.getString("first_name").trim() != null && !json.getString("first_name").trim().isEmpty() && !json.getString("first_name").trim().equals("null") && !json.getString("first_name").trim().matches("")) {
                                        if ((json.has("last_name")) && json.getString("last_name").trim() != null && !json.getString("last_name").trim().isEmpty() && !json.getString("last_name").trim().equals("null") && !json.getString("last_name").trim().matches("")) {
                                            fbName = json.getString("first_name") + " " + json.getString("last_name");
                                        }
                                        fName = json.getString("first_name");
                                    } else {

                                        if ((json.has("last_name")) && json.getString("last_name").trim() != null && !json.getString("last_name").trim().isEmpty() && !json.getString("last_name").trim().equals("null") && !json.getString("last_name").trim().matches("")) {
                                            fbName = json.getString("last_name");
                                            fName = json.getString("last_name");
                                        } else {
                                            if ((json.has("name")) && json.getString("name").trim() != null && !json.getString("name").trim().isEmpty() && !json.getString("name").trim().equals("null") && !json.getString("name").trim().matches("")) {
                                                fbName = json.getString("name");
                                                fName = json.getString("name").replace(" ", "").trim();
                                            }
                                        }

                                    }

                                  /*  if (fbName!=null && fbName.matches("")){
                                        fbName = fbUserId;
                                    }*/
                                    if ((json.has("email")) && json.getString("email").trim() != null && !json.getString("email").trim().isEmpty() && !json.getString("email").trim().equals("null") && !json.getString("email").trim().matches("")) {
                                        fbEmail = json.getString("email");
                                    } else {
                                        if (fbName != null && !fbName.matches("")) {
                                            fbEmail = fName + "@facebook.com";
                                        } else {
                                            fbName = fbUserId;
                                            fbEmail = fbUserId + "@facebook.com";
                                        }
                                    }

                                    registerButton.setVisibility(View.GONE);
                                    loginWithFacebookButton.setVisibility(View.GONE);
                                    btnLogin.setVisibility(View.GONE);
                                    ((RegisterActivity) context).handleFbUserDetails(fbUserId, fbEmail, fbName);
//
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


//
                        }

                    });


            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender, birthday");
            request.setParameters(parameters);
            request.executeAsync();
        }


        @Override
        public void onCancel() {

            registerButton.setVisibility(View.VISIBLE);
            loginWithFacebookButton.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            Toast.makeText(context, languagePreference.getTextofLanguage(DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT), Toast.LENGTH_LONG).show();
            //progressDialog.dismiss();
        }

        @Override
        public void onError(FacebookException e) {

            registerButton.setVisibility(View.VISIBLE);
            loginWithFacebookButton.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            Toast.makeText(context, languagePreference.getTextofLanguage(DETAILS_NOT_FOUND_ALERT, DEFAULT_DETAILS_NOT_FOUND_ALERT), Toast.LENGTH_LONG).show();

            //progressDialog.dismiss();
        }
    };


    public void callSignin(LanguagePreference languagePreference) {

        gmailTest.setText(languagePreference.getTextofLanguage(GMAIL_SIGNIN, DEFAULT_GMAIL_SIGNIN));
        googleSignView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((RegisterActivity)context).signIn();
            }
        });
    }

    public void setCountryList(PreferenceManager preferenceManager) {

        country_List = Arrays.asList(context.getResources().getStringArray(R.array.country));
        country_Code_List = Arrays.asList(context.getResources().getStringArray(R.array.countrycode));
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
            /*country_spinner.setSelection(224);
            selected_Country_Id = country_Code_List.get(224); */
            country_spinner.setSelection(0);
            selected_Country_Id = country_Code_List.get(0);
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
                if (country_Code_List.get(position).toLowerCase().equalsIgnoreCase("choose a country")){
                    Toast.makeText(context, languagePreference.getTextofLanguage(CHOOSE_COUNTRY_ALERT_MESSAGE, DEFAULT_CHOOSE_COUNTRY_ALERT_MESSAGE), Toast.LENGTH_SHORT).show();
                }else {
                    selected_Country_Id = country_Code_List.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


}
