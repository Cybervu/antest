package com.home.vod.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiController.GetUserProfileAsynctask;
import com.home.apisdk.apiController.UpadteUserProfileAsynctask;
import com.home.apisdk.apiModel.Get_UserProfile_Input;
import com.home.apisdk.apiModel.Get_UserProfile_Output;
import com.home.apisdk.apiModel.Update_UserProfile_Input;
import com.home.apisdk.apiModel.Update_UserProfile_Output;
import com.home.vod.LoginHandler;
import com.home.vod.R;
import com.home.vod.RegisterUIHandler;
import com.home.vod.SideMenuHandler;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static com.androidquery.util.AQUtility.getContext;
import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.CHANGE_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.CONFIRM_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CHANGE_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CONFIRM_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FAILURE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_RESTRICT_DEVICE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_MANAGE_DEVICE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NAME_HINT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NEW_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_OLD_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PASSWORDS_DO_NOT_MATCH;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PROFILE_UPDATED;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SLOW_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_UPDATE_PROFILE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_UPDATE_PROFILE_ALERT;
import static com.home.vod.preferences.LanguagePreference.FAILURE;
import static com.home.vod.preferences.LanguagePreference.IS_RESTRICT_DEVICE;
import static com.home.vod.preferences.LanguagePreference.MANAGE_DEVICE;
import static com.home.vod.preferences.LanguagePreference.NAME_HINT;
import static com.home.vod.preferences.LanguagePreference.NEW_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.OLD_PASSWORD;
import static com.home.vod.preferences.LanguagePreference.PASSWORDS_DO_NOT_MATCH;
import static com.home.vod.preferences.LanguagePreference.PROFILE_UPDATED;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SLOW_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.preferences.LanguagePreference.UPDATE_PROFILE;
import static com.home.vod.preferences.LanguagePreference.UPDATE_PROFILE_ALERT;
import static com.home.vod.util.Constant.authTokenStr;

public class DigiOsmosisProfileActivity extends AppCompatActivity implements GetUserProfileAsynctask.Get_UserProfileListener {
    SharedPreferences loginPref;

    ImageView bannerImageView,profile_image,editprofile;
    EditText editConfirmPassword, editNewPassword, editProfileNameEditText;
    EditText emailAddressEditText;
    Button changePassword, update_profile, manage_devices;

    String Name, Password;
    boolean password_visibility = false;

    String User_Id = "";
    String Email_Id = "";
    TextView name_of_user;
    ProgressBarHandler pDialog;
    LanguagePreference languagePreference;
    SideMenuHandler sideMenuHandler;


    // load asynctask
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    // Added for country and language spinner
    Spinner country_spinner, language_spinner;
    ArrayAdapter<String> Language_arrayAdapter, Country_arrayAdapter;

    String Selected_Language, Selected_Country = "0", Selected_Language_Id = "", Selected_Country_Id = "";
    PreferenceManager preferenceManager;
    List<String> Country_List, Country_Code_List, Language_List, Language_Code_List;

    String[] requests = {"uses-permission android:name=\"android.permission.CAMERA\"","uses-permission android:name=\"android.permission.WRITE_EXTERNAL_STORAGE\""};
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private File profile_image_file;
    Uri photoURI;
    public static final String IMAGE_DIRECTORY_NAME = "Profile_Image";
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    Bitmap bm;
    String SelectedPath = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        preferenceManager = PreferenceManager.getPreferenceManager(this);
        languagePreference = LanguagePreference.getLanguagePreference(DigiOsmosisProfileActivity.this);

        bannerImageView = (ImageView) findViewById(R.id.bannerImageView);
        editNewPassword = (EditText) findViewById(R.id.editNewPassword);
        editConfirmPassword = (EditText) findViewById(R.id.editConfirmPassword);
        editProfileNameEditText = (EditText) findViewById(R.id.editProfileNameEditText);
        emailAddressEditText = (EditText) findViewById(R.id.emailAddressEditText);
        changePassword = (Button) findViewById(R.id.changePasswordButton);
        update_profile = (Button) findViewById(R.id.update_profile);
        manage_devices = (Button) findViewById(R.id.manage_devices);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        editprofile = (ImageView) findViewById(R.id.pen_icon);


        if (!languagePreference.getTextofLanguage(IS_RESTRICT_DEVICE, DEFAULT_IS_RESTRICT_DEVICE).trim().equals("1")) {
            manage_devices.setVisibility(View.GONE);
        }

        editConfirmPassword.setVisibility(View.GONE);
        editNewPassword.setVisibility(View.GONE);
        name_of_user = (TextView) findViewById(R.id.name_of_user);
        name_of_user.setVisibility(View.INVISIBLE);

        country_spinner = (Spinner) findViewById(R.id.countrySpinner);
        language_spinner = (Spinner) findViewById(R.id.languageSpinner);
        country_spinner.setVisibility(View.GONE);
        language_spinner.setVisibility(View.GONE);
        FontUtls.loadFont(DigiOsmosisProfileActivity.this, getResources().getString(R.string.light_fonts), editProfileNameEditText);
        FontUtls.loadFont(DigiOsmosisProfileActivity.this, getResources().getString(R.string.light_fonts), editConfirmPassword);

        FontUtls.loadFont(DigiOsmosisProfileActivity.this, getResources().getString(R.string.light_fonts), editNewPassword);
        FontUtls.loadFont(DigiOsmosisProfileActivity.this, getResources().getString(R.string.regular_fonts), changePassword);
        FontUtls.loadFont(DigiOsmosisProfileActivity.this, getResources().getString(R.string.regular_fonts), update_profile);
        FontUtls.loadFont(DigiOsmosisProfileActivity.this, getResources().getString(R.string.regular_fonts), manage_devices);

        editProfileNameEditText.setHint(languagePreference.getTextofLanguage(NAME_HINT, DEFAULT_NAME_HINT));
        editConfirmPassword.setHint(languagePreference.getTextofLanguage(CONFIRM_PASSWORD, DEFAULT_CONFIRM_PASSWORD));
        editNewPassword.setHint(languagePreference.getTextofLanguage(NEW_PASSWORD, DEFAULT_NEW_PASSWORD));
        changePassword.setText(languagePreference.getTextofLanguage(CHANGE_PASSWORD, DEFAULT_CHANGE_PASSWORD));
        update_profile.setText(languagePreference.getTextofLanguage(UPDATE_PROFILE, DEFAULT_UPDATE_PROFILE));
        manage_devices.setText(languagePreference.getTextofLanguage(MANAGE_DEVICE, DEFAULT_MANAGE_DEVICE));


        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


       /* userId = getIntent().getStringExtra("LOGID");
        emailId = getIntent().getStringExtra("EMAIL");
*/
/*
        String name=preferenceManager.getDispNameFromPref();
        sideMenuHandler=new SideMenuHandler(this,preferenceManager);
        if (loginPref != null) {

        }*/






        manage_devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                if (NetworkStatus.getInstance().isConnected(DigiOsmosisProfileActivity.this)) {
                    Intent intent = new Intent(DigiOsmosisProfileActivity.this, ManageDevices.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                } else {
                    Toast.makeText(DigiOsmosisProfileActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                }

            }
        });

        // This is used for language and country spunner


        Country_List = Arrays.asList(getResources().getStringArray(R.array.country));
        Country_Code_List = Arrays.asList(getResources().getStringArray(R.array.countrycode));
        Language_List = Arrays.asList(getResources().getStringArray(R.array.languages));
        Language_Code_List = Arrays.asList(getResources().getStringArray(R.array.languagesCode));


        Language_arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.country_language_spinner, Language_List) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                FontUtls.loadFont(DigiOsmosisProfileActivity.this, getResources().getString(R.string.light_fonts), (TextView) v);

/*
                Typeface externalFont = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                ((TextView) v).setTypeface(externalFont);*/
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                FontUtls.loadFont(DigiOsmosisProfileActivity.this, getResources().getString(R.string.light_fonts), (TextView) v);
/*
                Typeface externalFont1 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                ((TextView) v).setTypeface(externalFont1);*/

                return v;
            }

        };

        language_spinner.setAdapter(Language_arrayAdapter);

        Country_arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.country_language_spinner, Country_List) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                FontUtls.loadFont(DigiOsmosisProfileActivity.this, getResources().getString(R.string.light_fonts), (TextView) v);
/*
                Typeface externalFont = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                ((TextView) v).setTypeface(externalFont);*/
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                FontUtls.loadFont(DigiOsmosisProfileActivity.this, getResources().getString(R.string.light_fonts), (TextView) v);

               /* Typeface externalFont1 = Typeface.createFromAsset(getAssets(), getResources().getString(R.string.light_fonts));
                ((TextView) v).setTypeface(externalFont1);*/

                return v;
            }

        };

        country_spinner.setAdapter(Country_arrayAdapter);

        country_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Selected_Country_Id = Country_Code_List.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        language_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Selected_Language_Id = Language_Code_List.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

// =======End ===========================//

        Get_UserProfile_Input get_userProfile_input = new Get_UserProfile_Input();
        get_userProfile_input.setAuthToken(authTokenStr);
        get_userProfile_input.setUser_id(preferenceManager.getUseridFromPref());
        get_userProfile_input.setEmail(preferenceManager.getEmailIdFromPref());
        get_userProfile_input.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));

        GetUserProfileAsynctask asynLoadProfileDetails = new GetUserProfileAsynctask(get_userProfile_input, this, this);
        asynLoadProfileDetails.executeOnExecutor(threadPoolExecutor);


        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                if (changePassword.isClickable() && editConfirmPassword.isShown() && editNewPassword.isShown()) {


                    if (editConfirmPassword.getText().toString().trim() != null && !(editConfirmPassword.getText().toString().trim().equalsIgnoreCase(""))) {
                        if (Util.isConfirmPassword(editConfirmPassword.getText().toString(), editNewPassword.getText().toString()) == false) {
                            Toast.makeText(DigiOsmosisProfileActivity.this, languagePreference.getTextofLanguage(PASSWORDS_DO_NOT_MATCH, DEFAULT_PASSWORDS_DO_NOT_MATCH), Toast.LENGTH_LONG).show();
                            editConfirmPassword.setText("");
                            editNewPassword.setText("");
                            return;

                        } else {
                            if (NetworkStatus.getInstance().isConnected(DigiOsmosisProfileActivity.this)) {
                                UpdateProfile();
                                editConfirmPassword.setText("");
                                editNewPassword.setText("");
                                editConfirmPassword.setVisibility(View.GONE);
                                editNewPassword.setVisibility(View.GONE);
                            }
                        }
                    }
                    if (editConfirmPassword.getText().toString().trim().equalsIgnoreCase("") || (editNewPassword.getText().toString().trim().equalsIgnoreCase(""))) {
                        editConfirmPassword.setText("");
                        editNewPassword.setText("");
                        editConfirmPassword.setVisibility(View.GONE);
                        editNewPassword.setVisibility(View.GONE);
                        editProfileNameEditText.requestFocus();
                    }
                    /*editOldPassword.setText("");
                    editNewPassword.setText("");*/
                } else {
                   /* editOldPassword.setText("");
                    editNewPassword.setText("");*/
                 /*   editOldPassword.setVisibility(View.GONE);
                    editNewPassword.setVisibility(View.GONE);*/
                    editConfirmPassword.setVisibility(View.VISIBLE);
                    editNewPassword.setVisibility(View.VISIBLE);
                    editConfirmPassword.requestFocus();

                }


               /* if (editOldPassword.getText().toString().trim() != null && !(editOldPassword.getText().toString().trim().equalsIgnoreCase(""))) {
                    if (Util.isConfirmPassword(editOldPassword.getText().toString(), editNewPassword.getText().toString()) == false) {
                        Toast.makeText(ProfileActivity.this, languagePreference.getTextofLanguage(PASSWORDS_DO_NOT_MATCH, Util.DEFAULT_PASSWORDS_DO_NOT_MATCH), Toast.LENGTH_LONG).show();

                        return;
                    }
                }*/
            }
        });

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editProfileNameEditText.getText().toString().matches("")) {
                    ShowDialog(languagePreference.getTextofLanguage(FAILURE, DEFAULT_FAILURE), languagePreference.getTextofLanguage(NAME_HINT, DEFAULT_NAME_HINT));

                } else if (!editConfirmPassword.getText().toString().matches(editNewPassword.getText().toString().trim())) {
                    ShowDialog(languagePreference.getTextofLanguage(FAILURE, DEFAULT_FAILURE), languagePreference.getTextofLanguage(PASSWORDS_DO_NOT_MATCH, DEFAULT_PASSWORDS_DO_NOT_MATCH));

                } else {
                    if (NetworkStatus.getInstance().isConnected(DigiOsmosisProfileActivity.this)) {

                        InputMethodManager inputManager = (InputMethodManager)
                                getSystemService(Context.INPUT_METHOD_SERVICE);

                        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);

                        UpdateProfile();
                    }
                }
              /*  boolean isNetwork = Util.checkNetwork(ProfileActivity.this);

                if (isNetwork) {

                    Name = editProfileNameEditText.getText().toString().trim();
                    Password = editNewPassword.getText().toString().trim();

                    if (editOldPassword.getVisibility() == View.VISIBLE) {
                        if (!editProfileNameEditText.getText().toString().trim().equals("")
                                && !emailAddressEditText.getText().toString().trim().equals("")
                                && !editNewPassword.getText().toString().trim().equals("")
                                && !editOldPassword.getText().toString().trim().equals("")) {
                            if (editNewPassword.equals(editOldPassword)) {
                                password_visibility = true;
                                UpdateProfile();
                            } else {
                                ShowDialog(Util.getTextofLanguage(ProfileActivity.this,Util.FAILURE,Util.DEFAULT_FAILURE), Util.getTextofLanguage(ProfileActivity.this,Util.PASSWORDS_DO_NOT_MATCH,Util.DEFAULT_PASSWORDS_DO_NOT_MATCH));

                            }
                        } else {
                            ShowDialog(Util.getTextofLanguage(ProfileActivity.this,Util.FAILURE,Util.DEFAULT_FAILURE), Util.getTextofLanguage(ProfileActivity.this,Util.NO_RECORD,Util.DEFAULT_NO_RECORD));


                        }
                    } else {
                        if (!editProfileNameEditText.getText().toString().trim().equals("") && !emailAddressEditText.getText().toString().trim().equals("")) {
                            password_visibility = false;
                            UpdateProfile();
                        } else {
                            ShowDialog(Util.getTextofLanguage(ProfileActivity.this,Util.FAILURE,Util.DEFAULT_FAILURE), Util.getTextofLanguage(ProfileActivity.this,Util.NO_RECORD,Util.DEFAULT_NO_RECORD));

                        }
                    }

                } else {

                    ShowDialog(Util.getTextofLanguage(ProfileActivity.this,Util.FAILURE,Util.DEFAULT_FAILURE), Util.getTextofLanguage(ProfileActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));
                }*/
            }
        });


        requestPermissions(this,requests,0);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(DigiOsmosisProfileActivity.this);
                builder.setTitle("Add Photo!");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (items[item].equals("Take Photo")) {


                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            photoURI = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                            SelectedPath = photoURI.getPath();
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                            // start the image capture Intent
                            startActivityForResult(intent, REQUEST_CAMERA);


                        } else if (items[item].equals("Choose from Library")) {
                            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                        } else if (items[item].equals("Cancel")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
    }

    public void ShowDialog(String Title, String msg) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(DigiOsmosisProfileActivity.this, R.style.MyAlertDialogStyle);
        dlgAlert.setMessage(msg);
        dlgAlert.setTitle(Title);
        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dlgAlert.create().show();
    }

    public void UpdateProfile() {

     /*   Update_UserProfile_Input update_userProfile_input = new Update_UserProfile_Input();
        update_userProfile_input.setAuthToken(authTokenStr);
        update_userProfile_input.setUser_id(preferenceManager.getUseridFromPref().trim());
        update_userProfile_input.setName(editProfileNameEditText.getText().toString().trim());
        update_userProfile_input.setDisplay_image(profile_image.toString().trim());
        String confirmPasswordStr = editNewPassword.getText().toString().trim();
        if (!confirmPasswordStr.trim().equalsIgnoreCase("") && !confirmPasswordStr.isEmpty() && !confirmPasswordStr.equalsIgnoreCase("null") && !confirmPasswordStr.equalsIgnoreCase(null) && !confirmPasswordStr.equals(null) && !confirmPasswordStr.matches("")) {
            update_userProfile_input.setPassword(confirmPasswordStr.trim());
        }
        update_userProfile_input.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
        update_userProfile_input.setCustom_country(Selected_Country_Id);
        update_userProfile_input.setCustom_languages(Selected_Language_Id);
        UpadteUserProfileAsynctask asyncLoadVideos = new UpadteUserProfileAsynctask(update_userProfile_input, this, this);
        asyncLoadVideos.executeOnExecutor(threadPoolExecutor);*/

        if (NetworkStatus.getInstance().isConnected(DigiOsmosisProfileActivity.this)) {
            AsynUpdateProfile asyncLoadVideos = new AsynUpdateProfile();
            asyncLoadVideos.executeOnExecutor(threadPoolExecutor);
        } else {
            Toast.makeText(DigiOsmosisProfileActivity.this, languagePreference.getTextofLanguage(SLOW_INTERNET_CONNECTION,DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

        }


    }


    private class AsynUpdateProfile extends AsyncTask<Void, Void, Void> {
        ProgressBarHandler pDialog;

        int statusCode;
        String loggedInIdStr;
        String confirmPasswordStr = editNewPassword.getText().toString().trim();
        String nameStr = editProfileNameEditText.getText().toString().trim();

        String responseStr;
        JSONObject myJson = null;
        int serverResponseCode=0;


        @Override
        protected Void doInBackground(Void... params) {
            String selectedFilePath = SelectedPath;

            Log.v("BIBHU2","selectedFilePath=="+selectedFilePath);

            if (loginPref != null) {
                loggedInIdStr = loginPref.getString("PREFS_LOGGEDIN_ID_KEY", null);
            }
            if (!selectedFilePath.equals("")) {
                String urlRouteList = APIUrlConstant.getUpdateProfileUrl();
//            String urlRouteList = "http://192.168.17.136/test/index.php";
                Log.v("BIBHU2","selectedFilePath inside if=="+selectedFilePath);
                int serverResponseCode = 0;

                final HttpURLConnection connection;
                DataOutputStream dataOutputStream;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";


                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 5 * 1024 * 1024;

//            String selectedFilePath = photoURI.getPath();
                File selectedFile = new File(selectedFilePath);

                String[] parts = selectedFilePath.split("/");
                final String fileName = parts[parts.length - 1];

                try {
                    FileInputStream fileInputStream = new FileInputStream(selectedFile);
                    URL url = new URL(urlRouteList);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);//Allow Inputs
                    connection.setDoOutput(true);//Allow Outputs
                    connection.setUseCaches(false);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    connection.setRequestProperty("file", selectedFilePath);
                    connection.setRequestProperty("authToken",authTokenStr);
                    connection.setRequestProperty("user_id", preferenceManager.getUseridFromPref());
                    connection.setRequestProperty("name", nameStr.trim());

                    //creating new dataoutputstream
                    dataOutputStream = new DataOutputStream(connection.getOutputStream());

                    dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                    dataOutputStream.writeBytes("Content-Disposition: form-data ; name=\"file\";filename=\""
                            + selectedFilePath + "\"" + lineEnd);

                    dataOutputStream.writeBytes(lineEnd);

                    //returns no. of bytes present in fileInputStream
                    bytesAvailable = fileInputStream.available();
                    //selecting the buffer size as minimum of available bytes or 1 MB
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    //setting the buffer as byte array of size of bufferSize
                    buffer = new byte[bufferSize];

                    //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                    while (bytesRead > 0) {
                        //write the bytes read from inputstream
                        dataOutputStream.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    dataOutputStream.writeBytes(lineEnd);
                    dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    serverResponseCode = connection.getResponseCode();
                    String serverResponseMessage = connection.getResponseMessage();

                    Log.v("BIBHU3", "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                    //response code of 200 indicates the server status OK
                    if (serverResponseCode == 200) {

                                try {
                                    InputStream ins = connection.getInputStream();
                                    InputStreamReader isr = new InputStreamReader(ins);
                                    BufferedReader in = new BufferedReader(isr);

                                    String inputLine;

                                    while ((inputLine = in.readLine()) != null) {
                                        System.out.println(inputLine);
                                        responseStr = inputLine;
                                    }

                                    if (responseStr != null) {
                                        myJson = new JSONObject(responseStr);
                                        statusCode = Integer.parseInt(myJson.optString("code"));
                                        Log.v("BIBHU3", "statusCode 1==" + statusCode);

                                    }

                                    Log.v("BIBHU3", "Server Response is: " + responseStr);


                                } catch (Exception e) {
                                    Log.v("BIBHU3", "Exception is: " + e.toString());
                                }

                    }

                    //closing the input and output streams
                    fileInputStream.close();
                    dataOutputStream.flush();
                    dataOutputStream.close();


                } catch (Exception e) {
                    statusCode = 0;
                    Log.v("BIBHU3", "Exception 1" + e.toString());

                }
            }else {

                Log.v("BIBHU3","inside else block");

                String urlRouteList =APIUrlConstant.getUpdateProfileUrl().trim();
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(urlRouteList);
                    httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                    httppost.addHeader("user_id", preferenceManager.getUseridFromPref());
                    httppost.addHeader("authToken",authTokenStr.trim());
                    httppost.addHeader("name", nameStr.trim());
                    if (!confirmPasswordStr.trim().equalsIgnoreCase("") && !confirmPasswordStr.isEmpty() && !confirmPasswordStr.equalsIgnoreCase("null") && !confirmPasswordStr.equalsIgnoreCase(null) && !confirmPasswordStr.equals(null) && !confirmPasswordStr.matches("")){
                        httppost.addHeader("password", confirmPasswordStr.trim());
                    }

                    httppost.addHeader("custom_country", Selected_Country_Id);
                    httppost.addHeader("custom_languages",Selected_Language_Id);

                    // Execute HTTP Post Request
                    try {
                        HttpResponse response = httpclient.execute(httppost);
                        responseStr = EntityUtils.toString(response.getEntity());

                        Log.v("BIBHU3","inside else block responseStr="+responseStr);

                    } catch (org.apache.http.conn.ConnectTimeoutException e){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                statusCode = 0;
                                editConfirmPassword.setText("");
                                editNewPassword.setText("");
                                Toast.makeText(DigiOsmosisProfileActivity.this, languagePreference.getTextofLanguage(SLOW_INTERNET_CONNECTION,DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                            }
                        });

                    } catch (IOException e) {
                        statusCode = 0;

                        e.printStackTrace();
                    }
                    if(responseStr!=null) {
                        myJson = new JSONObject(responseStr);
                        statusCode = Integer.parseInt(myJson.optString("code"));
                    }
                }
                catch (Exception e) {
                    statusCode = 0;

                }
            }

            return null;
        }



        protected void onPostExecute(Void result) {

            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                statusCode = 0;

            }

            if (responseStr == null) {

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(DigiOsmosisProfileActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(UPDATE_PROFILE_ALERT, DEFAULT_UPDATE_PROFILE_ALERT));
                dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                editConfirmPassword.setText("");
                                editNewPassword.setText("");
                            }
                        });
                dlgAlert.create().show();
                return;
            }

                if (statusCode == 200) {
                    String confirmPasswordStr = editNewPassword.getText().toString().trim();
                    name_of_user.setText(editProfileNameEditText.getText().toString().trim());
                    if (!confirmPasswordStr.trim().equalsIgnoreCase("") &&
                            !confirmPasswordStr.isEmpty() &&
                            !confirmPasswordStr.equalsIgnoreCase("null") &&
                            !confirmPasswordStr.equalsIgnoreCase(null) && !confirmPasswordStr.equals(null) &&
                            !confirmPasswordStr.matches("")) {
                        preferenceManager.setPwdToPref(confirmPasswordStr);
                    }

                        String displayNameStr = myJson.optString("name");
                        preferenceManager.setDispNameToPref(displayNameStr);
                        if(myJson.optString("profile_image")!=null)
                        preferenceManager.setLoginProfImgoPref(myJson.optString("profile_image"));


                        sideMenuHandler=new SideMenuHandler(DigiOsmosisProfileActivity.this);
                        sideMenuHandler.sendBroadCast();

                    Util.showToast(DigiOsmosisProfileActivity.this, languagePreference.getTextofLanguage(PROFILE_UPDATED, DEFAULT_PROFILE_UPDATED));
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    if (name_of_user != null) {
                        name_of_user.clearFocus();
                        name_of_user.setCursorVisible(false);
                    }

                    if (editConfirmPassword != null) {
                        editConfirmPassword.clearFocus();

                    }
                    if (editNewPassword != null) {
                        editNewPassword.clearFocus();
                    }
                } else {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(DigiOsmosisProfileActivity.this, R.style.MyAlertDialogStyle);
                    dlgAlert.setMessage(languagePreference.getTextofLanguage(UPDATE_PROFILE_ALERT, DEFAULT_UPDATE_PROFILE_ALERT));
                    dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    editConfirmPassword.setText("");
                                    editNewPassword.setText("");
                                }
                            });
                    dlgAlert.create().show();
                }
        }

        @Override
        protected void onPreExecute() {
            Log.v("SUBHA","onPreExecute");

            pDialog = new ProgressBarHandler(DigiOsmosisProfileActivity.this);
            pDialog.show();
        }
    }

/*
    @Override
    public void onUpdateUserProfilePreExecuteStarted() {
        pDialog = new ProgressBarHandler(DigiOsmosisProfileActivity.this);
        pDialog.show();
    }

    @Override
    public void onUpdateUserProfilePostExecuteCompleted(Update_UserProfile_Output update_userProfile_output, int code, String message) {

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
        } catch (IllegalArgumentException ex) {

        }

        if (code > 0) {
            if (code == 200) {
                String confirmPasswordStr = editNewPassword.getText().toString().trim();
                name_of_user.setText(editProfileNameEditText.getText().toString().trim());
                if (!confirmPasswordStr.trim().equalsIgnoreCase("") &&
                        !confirmPasswordStr.isEmpty() &&
                        !confirmPasswordStr.equalsIgnoreCase("null") &&
                        !confirmPasswordStr.equalsIgnoreCase(null) && !confirmPasswordStr.equals(null) &&
                        !confirmPasswordStr.matches("")) {
                    preferenceManager.setPwdToPref(confirmPasswordStr);
                }
                if (update_userProfile_output != null) {

                    String displayNameStr = update_userProfile_output.getName();
                    preferenceManager.setDispNameToPref(displayNameStr);


                    sideMenuHandler=new SideMenuHandler(DigiOsmosisProfileActivity.this);
                    sideMenuHandler.sendBroadCast();

                }
                Util.showToast(DigiOsmosisProfileActivity.this, languagePreference.getTextofLanguage(PROFILE_UPDATED, DEFAULT_PROFILE_UPDATED));
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                if (name_of_user != null) {
                    name_of_user.clearFocus();
                    name_of_user.setCursorVisible(false);
                }

                if (editConfirmPassword != null) {
                    editConfirmPassword.clearFocus();

                }
                if (editNewPassword != null) {
                    editNewPassword.clearFocus();
                }
            } else {

            }
        } else {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(DigiOsmosisProfileActivity.this, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(UPDATE_PROFILE_ALERT, DEFAULT_UPDATE_PROFILE_ALERT));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            editConfirmPassword.setText("");
                            editNewPassword.setText("");
                        }
                    });
            dlgAlert.create().show();
        }



    }
*/

    //Getting Profile Details from The Api

    @Override
    public void onGet_UserProfilePreExecuteStarted() {

        pDialog = new ProgressBarHandler(DigiOsmosisProfileActivity.this);
        pDialog.show();
    }

    @Override
    public void onGet_UserProfilePostExecuteCompleted(Get_UserProfile_Output get_userProfile_output, int code, String message, String status) {

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
        } catch (IllegalArgumentException ex) {

        }

        if (Selected_Country_Id.equals("0")) {
            country_spinner.setSelection(224);
            Selected_Country_Id = Country_Code_List.get(224);
            LogUtil.showLog("Muvi", "country not  matched =" + Selected_Country + "==" + Selected_Country_Id);
        } else {
            for (int i = 0; i < Country_Code_List.size(); i++) {
                if (Selected_Country_Id.trim().equals(Country_Code_List.get(i))) {
                    country_spinner.setSelection(i);
                    Selected_Country_Id = Country_Code_List.get(i);

                    LogUtil.showLog("Muvi", "country  matched =" + Selected_Country_Id + "==" + Selected_Country_Id);
                }
            }
        }
        Country_arrayAdapter.notifyDataSetChanged();


        for (int i = 0; i < Language_Code_List.size(); i++) {
            if (Selected_Language_Id.trim().equals(Language_Code_List.get(i))) {
                language_spinner.setSelection(i);
                Selected_Language_Id = Language_Code_List.get(i);

                LogUtil.showLog("Muvi", "Selected_Language_Id =" + Selected_Language_Id);
            }
        }
        Language_arrayAdapter.notifyDataSetChanged();


        editProfileNameEditText.setText(get_userProfile_output.getDisplay_name());
        name_of_user.setText(get_userProfile_output.getDisplay_name());
        emailAddressEditText.setText(get_userProfile_output.getEmail());

        Log.v("BIBHU2", "Profile image=" + get_userProfile_output.getProfile_image());

        if (get_userProfile_output.getProfile_image() != null) {
            Picasso.with(DigiOsmosisProfileActivity.this)
                    .load(get_userProfile_output.getProfile_image())
                    .placeholder(R.drawable.logo).error(R.drawable.logo).noFade().resize(200, 200).into(profile_image);
        }

            if (get_userProfile_output.getProfile_image().matches(NO_DATA)) {
                Log.v("ANU","NODATA====");
                bannerImageView.setAlpha(0.8f);
                bannerImageView.setImageResource(R.drawable.logo);
            } else {
                Log.v("ANU","NODATA ELSE====");

                Picasso.with(DigiOsmosisProfileActivity.this)
                        .load(get_userProfile_output.getProfile_image())
                        .placeholder(R.drawable.logo).error(R.drawable.logo).noFade().resize(200, 200).into(bannerImageView);


                if (get_userProfile_output.getProfile_image() != null && get_userProfile_output.getProfile_image().length() > 0) {
                    Log.v("ANU","NODATA NULL====");

                    int pos = get_userProfile_output.getProfile_image().lastIndexOf("/");
                    String x = get_userProfile_output.getProfile_image().substring(pos + 1, get_userProfile_output.getProfile_image().length());

                    if (x.equalsIgnoreCase("no-user.png")) {
                        Log.v("ANU","NODATA NULL IF====");

                        bannerImageView.setImageResource(R.drawable.profile);
                        bannerImageView.setAlpha(0.8f);
                        //imagebg.setBackgroundColor(Color.parseColor("#969393"));

                    } else {
                        Log.v("ANU","NODATA NULL ELSE====");

                        Picasso.with(DigiOsmosisProfileActivity.this)
                                .load(get_userProfile_output.getProfile_image())
                                .placeholder(R.drawable.logo).error(R.drawable.logo).noFade().resize(200, 200).into(bannerImageView);

                    }
                }
            }

    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }

    public void removeFocusFromViews() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        removeFocusFromViews();

    }

    @Override
    public void onPause() {
        super.onPause();

        removeFocusFromViews();

    }


    void requestPermissions (Activity activity, String[]permissions, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user asynchronously -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int
            resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            ///for camera
            if (requestCode == REQUEST_CAMERA) {
                profile_image_file = new File(photoURI.getPath());
                Log.v("ANU","photoURI===="+photoURI);
                Log.v("ANU","profile_image_file===="+profile_image_file);

                try {
                    profile_image.setImageURI(photoURI);
                } catch (Exception e) {
                    Log.v("Exception", "" + e.toString());
                    return;
                }
            }
            //for gallery files
            else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                SelectedPath = getRealPathFromURI(selectedImageUri);


                String tempPath = getPath(selectedImageUri, DigiOsmosisProfileActivity.this);
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath);
                Log.v("ANU","bm===="+bm);

                Log.v("temporaryPath", tempPath);
                profile_image.setImageBitmap(bm);
            }
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("BIBHU2", "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
    // Create a media file name
    String timeStamp = ""+System.currentTimeMillis();
    File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
        mediaFile = new File(mediaStorageDir.getPath() + File.separator+ "IMG_" + timeStamp + ".jpg");

    } else {
        return null;
    }

        return mediaFile;
}


    private String getRealPathFromURI(Uri contentURI) {
        Uri contentUri = contentURI;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }


    public String getPath(Uri uri, Activity activity) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}
