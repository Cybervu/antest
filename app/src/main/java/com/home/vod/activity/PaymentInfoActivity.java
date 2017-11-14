package com.home.vod.activity;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.home.apisdk.apiController.AuthUserPaymentInfoAsyntask;
import com.home.apisdk.apiController.RegisterUserPaymentAsyntask;
import com.home.apisdk.apiController.VideoDetailsAsynctask;
import com.home.apisdk.apiModel.AuthUserPaymentInfoInputModel;
import com.home.apisdk.apiModel.AuthUserPaymentInfoOutputModel;
import com.home.apisdk.apiModel.GetVideoDetailsInput;
import com.home.apisdk.apiModel.Video_Details_Output;
import com.home.apisdk.apiModel.RegisterUserPaymentInputModel;
import com.home.apisdk.apiModel.RegisterUserPaymentOutputModel;
import com.home.vod.R;
import com.home.vod.adapter.CardSpinnerAdapter;
import com.home.vod.model.CardModel;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;

import com.muvi.muviplayersdk.activity.AdPlayerActivity;
import com.muvi.muviplayersdk.activity.ExoPlayerActivity;
import com.muvi.muviplayersdk.activity.Player;
import com.muvi.muviplayersdk.activity.ThirdPartyPlayer;
import com.muvi.muviplayersdk.activity.YouTubeAPIActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.BUTTON_PAY_NOW;
import static com.home.vod.preferences.LanguagePreference.CARD_WILL_CHARGE;
import static com.home.vod.preferences.LanguagePreference.CREDIT_CARD_DETAILS;
import static com.home.vod.preferences.LanguagePreference.CREDIT_CARD_NAME_HINT;
import static com.home.vod.preferences.LanguagePreference.CREDIT_CARD_NUMBER_HINT;
import static com.home.vod.preferences.LanguagePreference.CVV_ALERT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_PAY_NOW;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CARD_WILL_CHARGE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CREDIT_CARD_DETAILS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CREDIT_CARD_NAME_HINT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CREDIT_CARD_NUMBER_HINT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_CVV_ALERT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_ERROR_IN_SUBSCRIPTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_FAILURE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SORRY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SUBSCRIPTION_COMPLETED;
import static com.home.vod.preferences.LanguagePreference.ERROR_IN_SUBSCRIPTION;
import static com.home.vod.preferences.LanguagePreference.FAILURE;
import static com.home.vod.preferences.LanguagePreference.IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.IS_STREAMING_RESTRICTION;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.SORRY;
import static com.home.vod.preferences.LanguagePreference.SUBSCRIPTION_COMPLETED;
import static com.home.vod.util.Constant.authTokenStr;
import static com.home.vod.util.Util.DEFAULT_IS_ONE_STEP_REGISTRATION;
import static com.muvi.muviplayersdk.utils.Util.DEFAULT_IS_CHROMECAST;
import static com.muvi.muviplayersdk.utils.Util.IS_CHROMECAST;


public class PaymentInfoActivity extends ActionBarActivity implements VideoDetailsAsynctask.VideoDetailsListener,
        AuthUserPaymentInfoAsyntask.AuthUserPaymentInfoListener,
        RegisterUserPaymentAsyntask.RegisterUserPaymentListener {
    CardModel[] cardSavedArray;

    String filename = "";
    static File mediaStorageDir;
    ArrayList<String> SubTitlePath = new ArrayList<>();

    ProgressDialog pDialog;
    String existing_card_id = "";
    String isCheckedToSavetheCard = "1";

    String videoResolution = "BEST";
    LanguagePreference languagePreference;
    PreferenceManager preferenceManager;
    ProgressBarHandler progressBarHandler;
    Toolbar mActionBarToolbar;
    boolean isCouponCodeAdded = false;
    String validCouponCode;

    final String TAG = getClass().getName();
    private int MY_SCAN_REQUEST_CODE = 100; // arbitrary int

    Spinner cardExpiryYearSpinner;
    Spinner cardExpiryMonthSpinner;
    Spinner creditCardSaveSpinner;
    Spinner spinnerCardTextView;
    private RelativeLayout creditCardLayout;
    private RelativeLayout withoutCreditCardLayout;
    private LinearLayout cardExpiryDetailsLayout;
    private CheckBox saveCardCheckbox;
    ArrayList<String> FakeSubTitlePath = new ArrayList<>();

    private TextView withoutCreditCardChargedPriceTextView;
    private Button nextButton;
    //private Button paywithCreditCardButton;
    //private Button paywithPaypalButton;

    private String movieVideoUrlStr = null;

    private EditText nameOnCardEditText;
    private EditText cardNumberEditText;
    private EditText securityCodeEditText;
    private Button scanButton;
    private Button payNowButton;
    private ImageButton payByPaypalButton;
    private RadioGroup paymentOptionsRadioGroup;
    private RadioButton payWithCreditCardRadioButton;
    private RadioButton payByPalRadioButton;
    Player playerModel;

    private LinearLayout paymentOptionLinearLayout;

    private TextView paymentOptionsTitle;


    private Button applyButton;
    private EditText couponCodeEditText;
    // private TextView entireShowPrice;

    private TextView selectShowRadioButton;

    private TextView chargedPriceTextView;

    String cardLastFourDigitStr;
    String tokenStr;
    String cardTypeStr;
    String responseText;
    String statusStr;

    String movieStreamUniqueIdStr;
    String muviUniqueIdStr;
    String planPriceStr;
    String videoUrlStr;
    String currencyCountryCodeStr;
    String currencyIdStr;
    String currencySymbolStr;
    String videoPreview;
    String videoName = "No Name";
    private int contentTypesId = 0;
    private String movieThirdPartyUrl = null;

    int isPPV = 0;
    int isAPV = 0;
    int isConverted = 0;

    String profileIdStr;
    int expiryMonthStr = 0;
    int planIdForPaypal = 0;

    String movieNameStr = "";
    String videoduration = "";
    String movieTypeStr = "";
    String censorRatingStr = "";
    String movieDetailsStr = "";

    int expiryYearStr = 0;

    ArrayAdapter<Integer> cardExpiryYearSpinnerAdapter;
    ArrayAdapter<Integer> cardExpiryMonthSpinnerAdapter;
    CardSpinnerAdapter creditCardSaveSpinnerAdapter;
    List<Integer> yearArray = new ArrayList<Integer>(21);
    List<Integer> monthsIdArray = new ArrayList<Integer>(12);
    /*Asynctask on background thread*/
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    TextView creditCardDetailsTitleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_info);
        //Set toolbar
        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(PaymentInfoActivity.this);
                onBackPressed();
            }
        });
        languagePreference = LanguagePreference.getLanguagePreference(PaymentInfoActivity.this);
        videoPreview = languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA);
        creditCardDetailsTitleTextView = (TextView) findViewById(R.id.creditCardDetailsTitleTextView);

        playerModel = new Player();
        playerModel.setIsstreaming_restricted(Util.getStreamingRestriction(languagePreference));


        if ((languagePreference.getTextofLanguage(IS_ONE_STEP_REGISTRATION, DEFAULT_IS_ONE_STEP_REGISTRATION)
                .trim()).equals("1")) {
            // mActionBarToolbar.setNavigationIcon(null);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        } else {
            //mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        }

        /*mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(PaymentInfoActivity.this);
                onBackPressed();
            }
        });*/
        if (pDialog == null) {
            pDialog = new ProgressDialog(PaymentInfoActivity.this, R.style.CustomDialogTheme);
            pDialog.setCancelable(false);
            pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            pDialog.setIndeterminate(false);
            pDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_rawable));

        }

        preferenceManager = PreferenceManager.getPreferenceManager(this);

        if (getIntent().getStringExtra("currencyId") != null) {
            currencyIdStr = getIntent().getStringExtra("currencyId");
        } else {
            currencyIdStr = "";
        }

        if (getIntent().getStringExtra("currencyCountryCode") != null) {
            currencyCountryCodeStr = getIntent().getStringExtra("currencyCountryCode");
        } else {
            currencyCountryCodeStr = "";
        }

        if (getIntent().getStringExtra("currencySymbol") != null) {
            currencySymbolStr = getIntent().getStringExtra("currencySymbol");
        } else {
            currencySymbolStr = "";
        }


        nameOnCardEditText = (EditText) findViewById(R.id.nameOnCardEditText);
        cardNumberEditText = (EditText) findViewById(R.id.cardNumberEditText);
        securityCodeEditText = (EditText) findViewById(R.id.securityCodeEditText);
        couponCodeEditText = (EditText) findViewById(R.id.couponCodeEditText);

        FontUtls.loadFont(PaymentInfoActivity.this, getResources().getString(R.string.light_fonts), nameOnCardEditText);
        FontUtls.loadFont(PaymentInfoActivity.this, getResources().getString(R.string.light_fonts), cardNumberEditText);
        FontUtls.loadFont(PaymentInfoActivity.this, getResources().getString(R.string.light_fonts), securityCodeEditText);
        FontUtls.loadFont(PaymentInfoActivity.this, getResources().getString(R.string.light_fonts), couponCodeEditText);


        chargedPriceTextView = (TextView) findViewById(R.id.chargeDetailsTextView);
        creditCardLayout = (RelativeLayout) findViewById(R.id.creditCardLayout);
        cardExpiryDetailsLayout = (LinearLayout) findViewById(R.id.cardExpiryDetailsLayout);
        saveCardCheckbox = (CheckBox) findViewById(R.id.saveCardCheckbox);
        withoutCreditCardLayout = (RelativeLayout) findViewById(R.id.withoutPaymentLayout);
        withoutCreditCardLayout.setVisibility(View.GONE);
        withoutCreditCardChargedPriceTextView = (TextView) findViewById(R.id.withoutPaymentChargeDetailsTextView);
        nextButton = (Button) findViewById(R.id.nextButton);
        //paywithCreditCardButton = (Button) findViewById(R.id.payWithCreditCardButton);
        //paywithPaypalButton = (Button) findViewById(R.id.payWithPaypalCardButton);

        //payByPalRadioButton = (RadioButton) findViewById(R.id.payByPalRadioButton);


        cardExpiryMonthSpinner = (Spinner) findViewById(R.id.cardExpiryMonthEditText);
        cardExpiryYearSpinner = (Spinner) findViewById(R.id.cardExpiryYearEditText);
        creditCardSaveSpinner = (Spinner) findViewById(R.id.creditCardSaveEditText);

        scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setVisibility(View.GONE);
        payNowButton = (Button) findViewById(R.id.payNowButton);
        applyButton = (Button) findViewById(R.id.addCouponButton);

        FontUtls.loadFont(PaymentInfoActivity.this, getResources().getString(R.string.regular_fonts), creditCardDetailsTitleTextView);

        creditCardDetailsTitleTextView.setText(languagePreference.getTextofLanguage(CREDIT_CARD_DETAILS, DEFAULT_CREDIT_CARD_DETAILS));
        FontUtls.loadFont(PaymentInfoActivity.this, getResources().getString(R.string.light_fonts), chargedPriceTextView);
        FontUtls.loadFont(PaymentInfoActivity.this, getResources().getString(R.string.regular_fonts), payNowButton);
        payNowButton.setText(languagePreference.getTextofLanguage(BUTTON_PAY_NOW, DEFAULT_BUTTON_PAY_NOW));



       /* if (planIdForPaypal == 0){
            payByPaypalButton.setVisibility(View.GONE);
        }else{
            payByPaypalButton.setVisibility(View.VISIBLE);
        }

        payByPaypalButton.setVisibility(View.GONE);
        creditCardLayout.setVisibility(View.GONE);*/

        /*paywithCreditCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/


        selectShowRadioButton = (TextView) findViewById(R.id.showNameWithPrice);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        for (int i = 0; i < 21; i++) {
            yearArray.add(year + i);

        }
        for (int i = 1; i < 13; i++) {
            monthsIdArray.add(i);


        }


        cardExpiryMonthSpinnerAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_new, monthsIdArray);
        cardExpiryMonthSpinner.setAdapter(cardExpiryMonthSpinnerAdapter);


        int mn = c.get(Calendar.MONTH);
        if (Util.containsIgnoreCase(monthsIdArray, mn + 1)) {
            // true
            int mnIndex = monthsIdArray.indexOf(mn + 1);

            cardExpiryMonthSpinner.setSelection(mnIndex);
            expiryMonthStr = monthsIdArray.get(mnIndex);
        } else {
            cardExpiryMonthSpinner.setSelection(0);
            expiryMonthStr = monthsIdArray.get(0);

        }

       /* cardExpiryMonthSpinner.setSelection(0);
        expiryMonthStr = monthsIdArray.get(0);*/

        cardExpiryYearSpinnerAdapter = new ArrayAdapter<Integer>(this, R.layout.spinner_new, yearArray);
        cardExpiryYearSpinner.setAdapter(cardExpiryYearSpinnerAdapter);
        cardExpiryYearSpinner.setSelection(0);
        expiryYearStr = yearArray.get(0);

      /*  creditCardSaveSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                creditCardSaveSpinner.setSelection(position);
                if(position == 0)
                {
                    creditCardLayout.setVisibility(View.VISIBLE);
                }else
                {
                    creditCardLayout.setVisibility(View.INVISIBLE);
                }

            }

        });
*/

        saveCardCheckbox.setChecked(true);

        creditCardSaveSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                creditCardSaveSpinner.setSelection(position);
                if (position == 0) {

                    nameOnCardEditText.setVisibility(View.VISIBLE);
                    cardNumberEditText.setVisibility(View.VISIBLE);
                    cardNumberEditText.setVisibility(View.VISIBLE);
                    cardExpiryDetailsLayout.setVisibility(View.VISIBLE);
                    saveCardCheckbox.setVisibility(View.VISIBLE);
                    isCheckedToSavetheCard = "1";
                    saveCardCheckbox.setChecked(true);
                } else {
                    //withoutCreditCardLayout.setVisibility(View.GONE);
                    nameOnCardEditText.setVisibility(View.GONE);
                    cardNumberEditText.setVisibility(View.GONE);
                    cardNumberEditText.setVisibility(View.GONE);
                    cardExpiryDetailsLayout.setVisibility(View.GONE);
                    saveCardCheckbox.setVisibility(View.GONE);
                    isCheckedToSavetheCard = "0";
                    saveCardCheckbox.setChecked(false);


                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                creditCardSaveSpinner.setSelection(0);

                if (creditCardSaveSpinner.getSelectedItemPosition() == 0) {

                    nameOnCardEditText.setVisibility(View.VISIBLE);
                    cardNumberEditText.setVisibility(View.VISIBLE);
                    cardNumberEditText.setVisibility(View.VISIBLE);
                    cardExpiryDetailsLayout.setVisibility(View.VISIBLE);
                    saveCardCheckbox.setVisibility(View.VISIBLE);
                    isCheckedToSavetheCard = "1";
                    saveCardCheckbox.setChecked(true);

                    //withoutCreditCardLayout.setVisibility(View.VISIBLE);
                    //creditCardLayout.setVisibility(View.GONE);
                    //chargedPriceTextView.setText(Util.getTextofLanguage(PPvPaymentInfoActivity.this,Util.CARD_WILL_CHARGE,Util.DEFAULT_CARD_WILL_CHARGE)+" " +currencySymbolStr+chargedPrice);
                } else {
                    //withoutCreditCardLayout.setVisibility(View.GONE);
                    nameOnCardEditText.setVisibility(View.GONE);
                    cardNumberEditText.setVisibility(View.GONE);
                    cardNumberEditText.setVisibility(View.GONE);
                    cardExpiryDetailsLayout.setVisibility(View.GONE);
                    saveCardCheckbox.setVisibility(View.GONE);
                    isCheckedToSavetheCard = "0";
                    saveCardCheckbox.setChecked(false);
                }

            }
        });


        cardExpiryMonthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardExpiryMonthSpinner.setSelection(position);
                expiryMonthStr = monthsIdArray.get(position);

            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                Calendar c = Calendar.getInstance();
                int mn = c.get(Calendar.MONTH);
                if (Util.containsIgnoreCase(monthsIdArray, mn + 1)) {
                    // true
                    int mnIndex = monthsIdArray.indexOf(mn + 1);

                    cardExpiryMonthSpinner.setSelection(mnIndex);
                    expiryMonthStr = monthsIdArray.get(mnIndex);
                } else {
                    cardExpiryMonthSpinner.setSelection(0);
                    expiryMonthStr = monthsIdArray.get(0);

                }
            }
        });
        cardExpiryYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cardExpiryYearSpinner.setSelection(position);
                expiryYearStr = yearArray.get(position);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                cardExpiryYearSpinner.setSelection(0);
                expiryYearStr = yearArray.get(0);

            }
        });

        chargedPriceTextView.setText(languagePreference.getTextofLanguage(CARD_WILL_CHARGE, DEFAULT_CARD_WILL_CHARGE) + " : " + currencySymbolStr + getIntent().getStringExtra("price"));
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameOnCardEditText.setText("");
                securityCodeEditText.setText("");
                cardNumberEditText.setText("");


            }
        });


        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);

                String nameOnCardStr = nameOnCardEditText.getText().toString().trim();
                String cardNumberStr = cardNumberEditText.getText().toString().trim();
                String securityCodeStr = securityCodeEditText.getText().toString().trim();

                if (nameOnCardStr.matches("")) {
                    Toast.makeText(PaymentInfoActivity.this, languagePreference.getTextofLanguage(CREDIT_CARD_NAME_HINT, DEFAULT_CREDIT_CARD_NAME_HINT), Toast.LENGTH_LONG).show();

                } else if (cardNumberStr.matches("")) {
                    Toast.makeText(PaymentInfoActivity.this, languagePreference.getTextofLanguage(CREDIT_CARD_NUMBER_HINT, DEFAULT_CREDIT_CARD_NUMBER_HINT), Toast.LENGTH_LONG).show();

                } else if (securityCodeStr.matches("")) {
                    Toast.makeText(PaymentInfoActivity.this, languagePreference.getTextofLanguage(CVV_ALERT, DEFAULT_CVV_ALERT), Toast.LENGTH_LONG).show();


                } else if (expiryMonthStr <= 0) {
//                    Toast.makeText(PaymentInfoActivity.this, "Please enter expiry month", Toast.LENGTH_LONG).show();

                } else if (expiryYearStr <= 0) {
//                    Toast.makeText(PaymentInfoActivity.this, "Please enter expiry year", Toast.LENGTH_LONG).show();

                } else {

                    if (!NetworkStatus.getInstance().isConnected(PaymentInfoActivity.this)) {
                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
                        dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
                        dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
                        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
                        dlgAlert.setCancelable(false);
                        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        dlgAlert.create().show();

                    } else {
                        String emailIdStr = preferenceManager.getEmailIdFromPref();

                        nameOnCardStr = nameOnCardEditText.getText().toString().trim();
                        cardNumberStr = cardNumberEditText.getText().toString().trim();
                        securityCodeStr = securityCodeEditText.getText().toString().trim();
                        AuthUserPaymentInfoInputModel authUserPaymentInfoInputModel = new AuthUserPaymentInfoInputModel();
                        authUserPaymentInfoInputModel.setAuthToken(authTokenStr);
                        authUserPaymentInfoInputModel.setName_on_card(nameOnCardStr);
                        authUserPaymentInfoInputModel.setExpiryMonth(String.valueOf(expiryMonthStr).trim());
                        authUserPaymentInfoInputModel.setExpiryYear(String.valueOf(expiryYearStr).trim());
                        authUserPaymentInfoInputModel.setCardNumber(cardNumberStr);
                        authUserPaymentInfoInputModel.setCvv(securityCodeStr);
                        authUserPaymentInfoInputModel.setEmail(emailIdStr);
                        AuthUserPaymentInfoAsyntask asyncReg = new AuthUserPaymentInfoAsyntask(authUserPaymentInfoInputModel, PaymentInfoActivity.this, PaymentInfoActivity.this);
                        asyncReg.executeOnExecutor(threadPoolExecutor);


                    }

                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (NetworkStatus.getInstance().isConnected(PaymentInfoActivity.this)) {
                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
                    dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
                    dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
                    dlgAlert.setCancelable(false);
                    dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    dlgAlert.create().show();

                } else {

                   /* AsynWithouPaymentSubscriptionRegDetails asyncSubWithoutPayment = new AsynWithouPaymentSubscriptionRegDetails();
                    asyncSubWithoutPayment.executeOnExecutor(threadPoolExecutor);*/
                }
            }

        });

    }


    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onVideoDetailsPreExecuteStarted() {
        pDialog = new ProgressDialog(PaymentInfoActivity.this);
        pDialog.show();
    }

    @Override
    public void onVideoDetailsPostExecuteCompleted(Video_Details_Output _video_details_output, int statusCode, String stus, String message) {
        // _video_details_output.setThirdparty_url("https://www.youtube.com/watch?v=fqU2FzATTPY&spfreload=10");
        // _video_details_output.setThirdparty_url("https://player.vimeo.com/video/192417650?color=00ff00&badge=0");

     /*check if status code 200 then set the video url before this it check it is thirdparty url or normal if third party
        then set thirdpartyurl true here and assign the url to videourl*/


        if (statusCode == 200) {
            if((_video_details_output.getIs_offline().trim().equals("1")) && _video_details_output.getDownload_status().trim().equals("1")){
                playerModel.canDownload(true);
            }
            else{
                playerModel.canDownload(false);
            }

            if (_video_details_output.getThirdparty_url() == null || _video_details_output.getThirdparty_url().matches("")) {
                if (_video_details_output.getVideoUrl() != null || !_video_details_output.getVideoUrl().matches("")) {
                    playerModel.setVideoUrl(_video_details_output.getVideoUrl());
                    LogUtil.showLog("BISHAL", "videourl===" + playerModel.getVideoUrl());
                    playerModel.setThirdPartyPlayer(false);
                } else {
                    //  Util.dataModel.setVideoUrl(translatedLanuage.getNoData());
                    playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

                }
            } else {
                if (_video_details_output.getThirdparty_url() != null || !_video_details_output.getThirdparty_url().matches("")) {
                    playerModel.setVideoUrl(_video_details_output.getThirdparty_url());
                    playerModel.setThirdPartyPlayer(true);

                } else {
                    //  Util.dataModel.setVideoUrl(translatedLanuage.getNoData());
                    playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));

                }
            }

            Util.dataModel.setVideoResolution(_video_details_output.getVideoResolution());

            playerModel.setVideoResolution(_video_details_output.getVideoResolution());
            if (_video_details_output.getPlayed_length() != null && !_video_details_output.getPlayed_length().equals(""))
                playerModel.setPlayPos((Util.isDouble(_video_details_output.getPlayed_length())));


            //dependency for datamodel
            Util.dataModel.setAdNetworkId(_video_details_output.getAdNetworkId());
            Util.dataModel.setChannel_id(_video_details_output.getChannel_id());
            Util.dataModel.setPreRoll(_video_details_output.getPreRoll());
            Util.dataModel.setPostRoll(_video_details_output.getPostRoll());
            Util.dataModel.setMidRoll(_video_details_output.getMidRoll());
            Util.dataModel.setVideoUrl(_video_details_output.getVideoUrl());
            Util.dataModel.setVideoResolution(_video_details_output.getVideoResolution());
            Util.dataModel.setThirdPartyUrl(_video_details_output.getThirdparty_url());
            Util.dataModel.setAdDetails(_video_details_output.getAdDetails());


            //player model set
            playerModel.setMidRoll(_video_details_output.getMidRoll());
            playerModel.setPostRoll(_video_details_output.getPostRoll());
            playerModel.setChannel_id(_video_details_output.getChannel_id());
            playerModel.setAdNetworkId(_video_details_output.getAdNetworkId());
            playerModel.setPreRoll(_video_details_output.getPreRoll());

            // for online subtitle
            playerModel.setSubTitleName(_video_details_output.getSubTitleName());
            playerModel.setSubTitlePath(_video_details_output.getSubTitlePath());


            // for offline subtitle
            playerModel.setOfflineSubtitleUrl(_video_details_output.getOfflineUrl());
            playerModel.setOfflineSubtitleLanguage(_video_details_output.getOfflineLanguage());


            //for chromecast subtitle
            playerModel.setChromecsatSubtitleUrl(_video_details_output.getSubTitlePath());
            playerModel.setChromecsatSubtitleLanguage(_video_details_output.getSubTitleName());
            playerModel.setChromecsatSubtitleLanguageCode(_video_details_output.getSubTitleLanguage());


            //for resolution change in player
            playerModel.setResolutionFormat(_video_details_output.getResolutionFormat());
            playerModel.setResolutionUrl(_video_details_output.getResolutionUrl());

            playerModel.setNonDrmDownloadFormatList(_video_details_output.getResolutionFormat());
            playerModel.setNonDrmDownloadUrlList(_video_details_output.getResolutionUrl());



            if (languagePreference.getTextofLanguage(IS_STREAMING_RESTRICTION, DEFAULT_IS_IS_STREAMING_RESTRICTION).equals("1")) {
                playerModel.setIsstreaming_restricted(true);
            }else {
                playerModel.setIsstreaming_restricted(false);
            }


            if (languagePreference.getTextofLanguage(IS_CHROMECAST, DEFAULT_IS_CHROMECAST).equals("1")) {
                playerModel.setChromeCastEnable(true);
            }else {
                playerModel.setChromeCastEnable(false);
            }


            // This bolck is not coming from API
            playerModel.useIp(true);
            playerModel.useDate(true);
            playerModel.useEmail(true);
            playerModel.setWaterMark(false);


            playerModel.setFakeSubTitlePath(_video_details_output.getFakeSubTitlePath());
            playerModel.setVideoResolution(_video_details_output.getVideoResolution());
            FakeSubTitlePath = _video_details_output.getFakeSubTitlePath();
            playerModel.setSubTitleLanguage(_video_details_output.getSubTitleLanguage());


            if (playerModel.getVideoUrl() == null ||
                    playerModel.getVideoUrl().matches("")) {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
                }
                Util.showNoDataAlert(PaymentInfoActivity.this);

            } else {
                try {
                    if (pDialog != null && pDialog.isShowing()) {
                        pDialog.hide();
                        pDialog = null;
                    }
                } catch (IllegalArgumentException ex) {
                    playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
                }


                // condition for checking if the response has third party url or not.
                if (_video_details_output.getThirdparty_url() == null ||
                        _video_details_output.getThirdparty_url().matches("")
                        ) {


                    playerModel.setThirdPartyPlayer(false);
                    final Intent playVideoIntent;

                    if (Util.dataModel.getAdNetworkId() == 3) {
                        LogUtil.showLog("responseStr", "playVideoIntent" + Util.dataModel.getAdNetworkId());

                        playVideoIntent = new Intent(PaymentInfoActivity.this, ExoPlayerActivity.class);

                    } else if (Util.dataModel.getAdNetworkId() == 1 && Util.dataModel.getPreRoll() == 1) {
                        if (Util.dataModel.getPlayPos() <= 0) {
                            playVideoIntent = new Intent(PaymentInfoActivity.this, AdPlayerActivity.class);
                        } else {
                            playVideoIntent = new Intent(PaymentInfoActivity.this, ExoPlayerActivity.class);

                        }
                    } else {
                        playVideoIntent = new Intent(PaymentInfoActivity.this, ExoPlayerActivity.class);

                    }
                    /***ad **/
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (FakeSubTitlePath.size() > 0) {
                                // This Portion Will Be changed Later.

                                File dir = new File(Environment.getExternalStorageDirectory() + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/SubTitleList/");
                                if (dir.isDirectory()) {
                                    String[] children = dir.list();
                                    for (int i = 0; i < children.length; i++) {
                                        new File(dir, children[i]).delete();
                                    }
                                }

                                progressBarHandler = new ProgressBarHandler(PaymentInfoActivity.this);
                                progressBarHandler.show();
                                Download_SubTitle(FakeSubTitlePath.get(0).trim());
                            } else {
                                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                playVideoIntent.putExtra("PlayerModel", playerModel);
                                startActivity(playVideoIntent);
                                finish();
                            }

                        }
                    });
                } else {
                    final Intent playVideoIntent = new Intent(PaymentInfoActivity.this, ExoPlayerActivity.class);
                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    playVideoIntent.putExtra("PlayerModel", playerModel);
                    startActivity(playVideoIntent);
                    finish();

                }
            }

        } else {

            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
            try {
                if (pDialog != null && pDialog.isShowing()) {
                    pDialog.hide();
                    pDialog = null;
                }
            } catch (IllegalArgumentException ex) {
                playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
                // movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
            }
            playerModel.setVideoUrl(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
            Util.showNoDataAlert(PaymentInfoActivity.this);
        }


    }

    @Override
    public void onAuthUserPaymentInfoPreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(PaymentInfoActivity.this);
        progressBarHandler.show();
    }

    @Override
    public void onAuthUserPaymentInfoPostExecuteCompleted(AuthUserPaymentInfoOutputModel authUserPaymentInfoOutputModel, int status, String responseMessageStr) {


        if (status == 0) {
            try {
                if (progressBarHandler.isShowing())
                    progressBarHandler.hide();
            } catch (IllegalArgumentException ex) {
                status = 0;
            }
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
            dlgAlert.setMessage(responseMessageStr);
            dlgAlert.setTitle("Failure");
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();
        } else if (status == 1) {

            if (!NetworkStatus.getInstance().isConnected(PaymentInfoActivity.this)) {
                try {
                    if (progressBarHandler.isShowing())
                        progressBarHandler.hide();
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
                dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
                dlgAlert.setCancelable(false);
                dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                dlgAlert.create().show();

            } else {
                RegisterUserPaymentInputModel registerUserPaymentInputModel = new RegisterUserPaymentInputModel();
                registerUserPaymentInputModel.setAuthToken(authTokenStr);
                registerUserPaymentInputModel.setCard_name(nameOnCardEditText.getText().toString().trim());
                registerUserPaymentInputModel.setExp_month(String.valueOf(expiryMonthStr).trim());
                registerUserPaymentInputModel.setCard_number(cardNumberEditText.getText().toString().trim());
                registerUserPaymentInputModel.setExp_year(String.valueOf(expiryYearStr).trim());
                String userIdStr = preferenceManager.getUseridFromPref();
                String emailIdSubStr = preferenceManager.getEmailIdFromPref();
                registerUserPaymentInputModel.setEmail(emailIdSubStr.trim());
                registerUserPaymentInputModel.setUser_id(userIdStr.trim());
                if (isCouponCodeAdded == true) {
                    registerUserPaymentInputModel.setCouponCode(validCouponCode);
                } else {
                    registerUserPaymentInputModel.setCouponCode("");
                }
                registerUserPaymentInputModel.setCard_type(authUserPaymentInfoOutputModel.getCard_type());
                registerUserPaymentInputModel.setCard_last_fourdigit(authUserPaymentInfoOutputModel.getCard_last_fourdigit());
                registerUserPaymentInputModel.setProfile_id(authUserPaymentInfoOutputModel.getProfile_id());
                registerUserPaymentInputModel.setToken(authUserPaymentInfoOutputModel.getToken());
                registerUserPaymentInputModel.setCvv(securityCodeEditText.getText().toString().trim());
                registerUserPaymentInputModel.setCountry(preferenceManager.getCountryCodeFromPref());
                registerUserPaymentInputModel.setEpisode_id("0");
                registerUserPaymentInputModel.setSeason_id("0");
                registerUserPaymentInputModel.setCurrency_id(currencyIdStr.trim());
                registerUserPaymentInputModel.setPlan_id(getIntent().getStringExtra("selected_plan_id").toString().trim());
                registerUserPaymentInputModel.setName(preferenceManager.getDispNameFromPref());


                RegisterUserPaymentAsyntask asyncSubsrInfo = new RegisterUserPaymentAsyntask(registerUserPaymentInputModel, this, this);
                asyncSubsrInfo.executeOnExecutor(threadPoolExecutor);
            }
        }
    }


    //    private class AsynPaymentInfoDetails extends AsyncTask<Void, Void, Void> {
//        ProgressBarHandler progressBarHandler;
//        int status;
//        String responseStr;
//        String responseMessageStr;
//        String emailIdStr = preferenceManager.getEmailIdFromPref();
//
//        String nameOnCardStr = nameOnCardEditText.getText().toString().trim();
//        String cardNumberStr = cardNumberEditText.getText().toString().trim();
//        String securityCodeStr = securityCodeEditText.getText().toString().trim();
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            String urlRouteList = Util.rootUrl().trim() + Util.authenticatedCardValidationUrl.trim();
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("nameOnCard", nameOnCardStr);
//                httppost.addHeader("expiryMonth", String.valueOf(expiryMonthStr).trim());
//                httppost.addHeader("expiryYear", String.valueOf(expiryYearStr).trim());
//                httppost.addHeader("cardNumber", cardNumberStr);
//                httppost.addHeader("cvv", securityCodeStr);
//                httppost.addHeader("email", emailIdStr);
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//
//            /*    LogUtil.showLog("MUVI", "nameOnCardStr = " + nameOnCardStr);
//                LogUtil.showLog("MUVI", "expiryMonth = " + String.valueOf(expiryMonthStr).trim());
//                LogUtil.showLog("MUVI", "expiryYear = " + String.valueOf(expiryYearStr).trim());
//                LogUtil.showLog("MUVI", "cardNumber = " + cardNumberStr);
//                LogUtil.showLog("MUVI", "cvv = " + securityCodeStr);
//                LogUtil.showLog("MUVI", "email = " + emailIdStr);
//                LogUtil.showLog("MUVI", "authToken = " + Util.authTokenStr.trim());
//*/
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//
//                    LogUtil.showLog("MUVI", "response of card validation = " + responseStr);
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (progressBarHandler.isShowing())
//                                progressBarHandler.hide();
//                            status = 0;
//                            Toast.makeText(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//                        }
//
//                    });
//
//                } catch (IOException e) {
//                    if (progressBarHandler.isShowing())
//                        progressBarHandler.hide();
//                    status = 0;
//                    e.printStackTrace();
//
//
//                }
//                JSONObject myJson = null;
//
//                if (responseStr != null) {
//                    myJson = new JSONObject(responseStr);
//                    status = Integer.parseInt(myJson.optString("isSuccess"));
//                }
//                if (status == 1) {
//                    JSONObject mainJson = null;
//
//                    if (myJson.has("card")) {
//                        mainJson = myJson.getJSONObject("card");
//                        if (mainJson.has("status") && mainJson.getString("status").trim() != null && !mainJson.getString("status").trim().isEmpty() && !mainJson.getString("status").trim().equals("null") && !mainJson.getString("status").trim().matches("")) {
//                            statusStr = mainJson.getString("status");
//                        } else {
//                            statusStr = "";
//
//                        }
//
//                        if (mainJson.has("token") && mainJson.getString("token").trim() != null && !mainJson.getString("token").trim().isEmpty() && !mainJson.getString("token").trim().equals("null") && !mainJson.getString("token").trim().matches("")) {
//                            tokenStr = mainJson.getString("token");
//                        } else {
//                            tokenStr = "";
//
//                        }
//
//                        if (mainJson.has("response_text") && mainJson.getString("response_text").trim() != null && !mainJson.getString("response_text").trim().isEmpty() && !mainJson.getString("response_text").trim().equals("null") && !mainJson.getString("response_text").trim().matches("")) {
//                            responseText = mainJson.getString("response_text");
//                        } else {
//                            responseText = "";
//
//                        }
//
//                        if (mainJson.has("profile_id") && mainJson.getString("profile_id").trim() != null && !mainJson.getString("profile_id").trim().isEmpty() && !mainJson.getString("profile_id").trim().equals("null") && !mainJson.getString("profile_id").trim().matches("")) {
//                            profileIdStr = mainJson.getString("profile_id");
//                        } else {
//                            profileIdStr = "";
//
//                        }
//
//                        if (mainJson.has("card_last_fourdigit") && mainJson.getString("card_last_fourdigit").trim() != null && !mainJson.getString("card_last_fourdigit").trim().isEmpty() && !mainJson.getString("card_last_fourdigit").trim().equals("null") && !mainJson.getString("card_last_fourdigit").trim().matches("")) {
//                            cardLastFourDigitStr = mainJson.getString("card_last_fourdigit");
//                        } else {
//                            cardLastFourDigitStr = "";
//
//                        }
//
//                        if (mainJson.has("card_type") && mainJson.getString("card_type").trim() != null && !mainJson.getString("card_type").trim().isEmpty() && !mainJson.getString("card_type").trim().equals("null") && !mainJson.getString("card_type").trim().matches("")) {
//                            cardTypeStr = mainJson.getString("card_type");
//                        } else {
//                            cardTypeStr = "";
//
//                        }
//                    }
//
//
//                }
//                if (status == 0) {
//                    if (myJson.has("Message")) {
//                        responseMessageStr = myJson.optString("Message");
//                    }
//                    if (((responseMessageStr.equalsIgnoreCase("null")) || (responseMessageStr.length() <= 0))) {
//                        responseMessageStr = "No Details found";
//
//                    }
//                }
//
//            } catch (Exception e) {
//                if (progressBarHandler.isShowing())
//                    progressBarHandler.hide();
//                status = 0;
//
//            }
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//           /* try {
//                if (pDialog.isShowing())
//                    pDialog.dismiss();
//            } catch (IllegalArgumentException ex) {
//                status = 0;
//            }*/
//
//            if (status == 0) {
//                try {
//                    if (progressBarHandler.isShowing())
//                        progressBarHandler.hide();
//                } catch (IllegalArgumentException ex) {
//                    status = 0;
//                }
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
//                dlgAlert.setMessage(responseMessageStr);
//                dlgAlert.setTitle("Failure");
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                dlgAlert.create().show();
//            } else if (status == 1) {
//                boolean isNetwork = Util.checkNetwork(PaymentInfoActivity.this);
//                if (isNetwork == false) {
//                    try {
//                        if (progressBarHandler.isShowing())
//                            progressBarHandler.hide();
//                    } catch (IllegalArgumentException ex) {
//                        status = 0;
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
//                    dlgAlert.setMessage(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION));
//                    dlgAlert.setTitle(Util.getTextofLanguage(PaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    dlgAlert.create().show();
//
//                } else {
//                    AsynSubscriptionRegDetails asyncSubsrInfo = new AsynSubscriptionRegDetails();
//                    asyncSubsrInfo.executeOnExecutor(threadPoolExecutor);
//                }
//            }
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            progressBarHandler = new ProgressBarHandler(PaymentInfoActivity.this);
//            progressBarHandler.show();
//        }
//
//
//    }
    @Override
    public void onRegisterUserPaymentPreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(PaymentInfoActivity.this);
        progressBarHandler.show();
    }

    @Override
    public void onRegisterUserPaymentPostExecuteCompleted(RegisterUserPaymentOutputModel registerUserPaymentOutputModel, int status) {

        if (status == 0) {
            try {
                if (progressBarHandler.isShowing())
                    progressBarHandler.hide();
            } catch (IllegalArgumentException ex) {
                status = 0;
            }
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
            dlgAlert.setMessage(languagePreference.getTextofLanguage(ERROR_IN_SUBSCRIPTION, DEFAULT_ERROR_IN_SUBSCRIPTION));
            dlgAlert.setTitle(languagePreference.getTextofLanguage(FAILURE, DEFAULT_FAILURE));
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            dlgAlert.create().show();
        } else if (status > 0) {

            if (status == 200) {
                if (progressBarHandler.isShowing())
                    progressBarHandler.hide();
                Toast.makeText(PaymentInfoActivity.this, languagePreference.getTextofLanguage(SUBSCRIPTION_COMPLETED, DEFAULT_SUBSCRIPTION_COMPLETED), Toast.LENGTH_LONG).show();
                if (Util.check_for_subscription == 0) {
                    Intent intent = new Intent(PaymentInfoActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    finish();
                } else {
                    if (NetworkStatus.getInstance().isConnected(this)) {

                        GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
                        getVideoDetailsInput.setAuthToken(authTokenStr);
                        getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
                        getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
                        getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());

                        VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, PaymentInfoActivity.this, PaymentInfoActivity.this);
                        asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);

                    } else {
                        Intent intent = new Intent(PaymentInfoActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(intent);
                        finish();
                        Toast.makeText(PaymentInfoActivity.this, languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                    }
                }

            } else {
                try {
                    if (progressBarHandler.isShowing())
                        progressBarHandler.hide();
                } catch (IllegalArgumentException ex) {
                    status = 0;
                }
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
                dlgAlert.setMessage(languagePreference.getTextofLanguage(ERROR_IN_SUBSCRIPTION, DEFAULT_ERROR_IN_SUBSCRIPTION));
                dlgAlert.setTitle(languagePreference.getTextofLanguage(SORRY, DEFAULT_SORRY));
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
        }

    }

//    private class AsynSubscriptionRegDetails extends AsyncTask<Void, Void, Void> {
//        ProgressBarHandler progressBarHandler;
//        int status;
//        String responseStr;
//        String nameOnCardStr = nameOnCardEditText.getText().toString().trim();
//        String cardNumberStr = cardNumberEditText.getText().toString().trim();
//        String securityCardStr = securityCodeEditText.getText().toString().trim();
//
//        @Override
//        protected Void doInBackground(Void... params) {
//            LogUtil.showLog("MUVI", "payment at doInBackground called ");
//            String userIdStr = preferenceManager.getUseridFromPref();
//            String emailIdSubStr = preferenceManager.getEmailIdFromPref();
//
//            String urlRouteList = Util.rootUrl().trim() + Util.subscriptionUrl.trim();
//            LogUtil.showLog("MUVI", "payment at urlRouteList = " + urlRouteList);
//            try {
//                HttpClient httpclient = new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(urlRouteList);
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                LogUtil.showLog("MUVI", "=========== 1");
//                httppost.addHeader("card_name", nameOnCardStr);
//                LogUtil.showLog("MUVI", "=========== 11");
//                httppost.addHeader("exp_month", String.valueOf(expiryMonthStr).trim());
//                LogUtil.showLog("MUVI", "=========== 111");
//                httppost.addHeader("card_number", cardNumberStr);
//                LogUtil.showLog("MUVI", "=========== 1111");
//                httppost.addHeader("exp_year", String.valueOf(expiryYearStr).trim());
//                LogUtil.showLog("MUVI", "=========== 2");
//                httppost.addHeader("email", emailIdSubStr.trim()); //Null pointer
//                //  httppost.addHeader("movie_id", muviUniqueIdStr.trim());
//                httppost.addHeader("user_id", userIdStr.trim());
//                LogUtil.showLog("MUVI", "=========== 22=" + emailIdSubStr.trim());
//                if (isCouponCodeAdded == true) {
//                    httppost.addHeader("coupon_code", validCouponCode);
//                    LogUtil.showLog("MUVI", "=========== 222");
//                } else {
//                    LogUtil.showLog("MUVI", "=========== 2222");
//                    httppost.addHeader("coupon_code", "");
//                }
//                LogUtil.showLog("MUVI", "=========== 22222");
//                httppost.addHeader("card_type", cardTypeStr.trim());
//                LogUtil.showLog("MUVI", "=========== 3");
//                httppost.addHeader("card_last_fourdigit", cardLastFourDigitStr.trim());
//                LogUtil.showLog("MUVI", "=========== 33");
//                httppost.addHeader("profile_id", profileIdStr.trim());
//                LogUtil.showLog("MUVI", "=========== 333");
//                httppost.addHeader("token", tokenStr.trim());
//                LogUtil.showLog("MUVI", "=========== 3333=" + tokenStr.trim());
//                httppost.addHeader("cvv", securityCardStr);
//                // httppost.addHeader("country",currencyCountryCodeStr.trim());
//                LogUtil.showLog("MUVI", "=========== 33333");
//                httppost.addHeader("country", preferenceManager.getCountryCodeFromPref());
//                LogUtil.showLog("MUVI", "=========== 4");
//                httppost.addHeader("season_id", "0");
//                LogUtil.showLog("MUVI", "=========== 44");
//                httppost.addHeader("episode_id", "0");
//                LogUtil.showLog("MUVI", "=========== 444");
//                httppost.addHeader("currency_id", currencyIdStr.trim());
//                LogUtil.showLog("MUVI", "=========== 4444");
//
//                httppost.addHeader("plan_id", getIntent().getStringExtra("selected_plan_id").toString().trim());
//                httppost.addHeader("name", preferenceManager.getDispNameFromPref());
//
////                httppost.addHeader("is_save_this_card", isCheckedToSavetheCard.trim());
//
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//                    LogUtil.showLog("MUVI", "response of payment = " + responseStr);
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e) {
//                    LogUtil.showLog("MUVI", "error2=" + e.toString());
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            status = 0;
//                            if (progressBarHandler.isShowing())
//                                progressBarHandler.hide();
//                            Toast.makeText(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this, Util.SLOW_INTERNET_CONNECTION, Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//                        }
//
//                    });
//
//                } catch (IOException e) {
//
//                    status = 0;
//                    if (progressBarHandler.isShowing())
//                        progressBarHandler.hide();
//                    e.printStackTrace();
//                    LogUtil.showLog("MUVI", "error1=" + e.toString());
//                }
//
//                LogUtil.showLog("MUVI", "response of payment = " + responseStr);
//
//                if (responseStr != null) {
//                    JSONObject myJson = new JSONObject(responseStr);
//                    status = Integer.parseInt(myJson.optString("code"));
//                }
//
//            } catch (Exception e) {
//
//                if (progressBarHandler.isShowing())
//                    progressBarHandler.hide();
//                status = 0;
//                LogUtil.showLog("MUVI", "error=" + e.toString());
//            }
//
//            return null;
//        }
//
//
//        protected void onPostExecute(Void result) {
//
//            if (status == 0) {
//                try {
//                    if (progressBarHandler.isShowing())
//                        progressBarHandler.hide();
//                } catch (IllegalArgumentException ex) {
//                    status = 0;
//                }
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
//                dlgAlert.setMessage(Util.getTextofLanguage(PaymentInfoActivity.this, Util.ERROR_IN_SUBSCRIPTION, Util.DEFAULT_ERROR_IN_SUBSCRIPTION));
//                dlgAlert.setTitle(Util.getTextofLanguage(PaymentInfoActivity.this, Util.FAILURE, Util.DEFAULT_FAILURE));
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                dlgAlert.create().show();
//            } else if (status > 0) {
//
//                if (status == 200) {
//                    if (progressBarHandler.isShowing())
//                        progressBarHandler.hide();
//                    Toast.makeText(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this, Util.SUBSCRIPTION_COMPLETED, Util.DEFAULT_SUBSCRIPTION_COMPLETED), Toast.LENGTH_LONG).show();
//                    if (Util.check_for_subscription == 0) {
//                        Intent intent = new Intent(PaymentInfoActivity.this, MainActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        if (Util.checkNetwork(PaymentInfoActivity.this) == true) {
//
//                            GetVideoDetailsInput getVideoDetailsInput = new GetVideoDetailsInput();
//                            getVideoDetailsInput.setAuthToken(Util.authTokenStr);
//                            getVideoDetailsInput.setInternetSpeed(MainActivity.internetSpeed.trim());
//                            getVideoDetailsInput.setStream_uniq_id(Util.dataModel.getStreamUniqueId().trim());
//                            getVideoDetailsInput.setContent_uniq_id(Util.dataModel.getMovieUniqueId().trim());
//
//                            VideoDetailsAsynctask asynLoadVideoUrls = new VideoDetailsAsynctask(getVideoDetailsInput, PaymentInfoActivity.this, PaymentInfoActivity.this);
//                            asynLoadVideoUrls.executeOnExecutor(threadPoolExecutor);
//
//                        } else {
//                            Intent intent = new Intent(PaymentInfoActivity.this, MainActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                            startActivity(intent);
//                            finish();
//                            Toast.makeText(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                } else {
//                    try {
//                        if (progressBarHandler.isShowing())
//                            progressBarHandler.hide();
//                    } catch (IllegalArgumentException ex) {
//                        status = 0;
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
//                    dlgAlert.setMessage(Util.getTextofLanguage(PaymentInfoActivity.this, Util.ERROR_IN_SUBSCRIPTION, Util.DEFAULT_ERROR_IN_SUBSCRIPTION));
//                    dlgAlert.setTitle(Util.getTextofLanguage(PaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this, Util.BUTTON_OK, Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//                    dlgAlert.create().show();
//                }
//            }
//
//        }

//        @Override
//        protected void onPreExecute() {
//            progressBarHandler = new ProgressBarHandler(PaymentInfoActivity.this);
//            progressBarHandler.show();
//
//        }
//
//
//    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if (CardIOActivity.canReadCardWithCamera()) {
            scanButton.setText("Scan");
            scanButton.setEnabled(true);
        }*/ /*else {
            scanButton.setText("Scan");
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                scanButton.setBackgroundDrawable( getResources().getDrawable(R.drawable.button_background_gray) );
            } else {
                scanButton.setBackground( getResources().getDrawable(R.drawable.button_background_gray));
            }
           // scanButton.setBackground(R.drawable.button_background_gray);
            scanButton.setEnabled(false);
        }*/
    }

    /*public void onScanPress(View v) {
        // This method is set up as an onClick handler in the layout xml
        // e.g. android:onClick="onScanPress"

        Intent scanIntent = new Intent(this, CardIOActivity.class);
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true); // default: false

        // customize these values to suit your needs.
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME,true);

        // hides the manual entry button
        // if set, developers should provide their own manual entry mechanism in the app
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, false); // default: false

        // matches the theme of your application
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, false); // default: false

        // MY_SCAN_REQUEST_CODE is arbitrary and is only used within this activity.
        startActivityForResult(scanIntent, MY_SCAN_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
            CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);

            // Never log a raw card number. Avoid displaying it, but if necessary use getFormattedCardNumber()
            cardNumberEditText.setText(scanResult.cardNumber);

            if (scanResult.isExpiryValid()) {

                if (monthsIdArray.contains(scanResult.expiryMonth)) {

                    // true
                    int index = monthsIdArray.indexOf(scanResult.expiryMonth);
                    expiryMonthStr = monthsIdArray.get(index);
                    cardExpiryMonthSpinner.setSelection(index);
                }
                if (yearArray.contains(scanResult.expiryYear)) {
                    // true
                    int index =yearArray.indexOf(scanResult.expiryYear);
                    expiryYearStr = yearArray.get(index);
                    cardExpiryYearSpinner.setSelection(index);
                }

            }



            if (scanResult.cvv != null) {
                // Never log or display a CVV
                securityCodeEditText.setText(scanResult.cvv) ;
            }

        } else {
            Toast.makeText(PaymentInfoActivity.this, "Please enter credit card details manually", Toast.LENGTH_LONG).show();
        }

    }
*/
    /*@Override
    public void onBackPressed()
    {

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (getIntent().getStringExtra("activity").trim()!=null && getIntent().getStringExtra("activity").trim().equalsIgnoreCase("generic")){

            runOnUiThread(new Runnable() {
                public void run() {
                    Intent newIn = new Intent(PaymentInfoActivity.this, MainActivity.class);
                    newIn.putExtra("activity","generic");
                    newIn.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(newIn);
                    finish();
                    overridePendingTransition(0, 0);
                }
            });



        }
        super.onBackPressed();

    }*/


    //    private class AsynLoadVideoUrls extends AsyncTask<Void, Void, Void> {
//        ProgressBarHandler pDialog;
//        String responseStr;
//        int statusCode;
//        @Override
//        protected Void doInBackground(Void... params) {
//            try {
//                HttpClient httpclient=new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(Util.rootUrl().trim()+Util.loadVideoUrl.trim());
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken", Util.authTokenStr.trim());
//                httppost.addHeader("content_uniq_id", Util.dataModel.getMovieUniqueId().trim());
//                httppost.addHeader("stream_uniq_id", Util.dataModel.getStreamUniqueId().trim());
//                httppost.addHeader("internet_speed",MainActivity.internetSpeed.trim());
//
//                // Execute HTTP Post Request
//                try {
//
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//
//                } catch (org.apache.http.conn.ConnectTimeoutException e){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (pDialog != null && pDialog.isShowing()) {
//                                pDialog.hide();
//                                pDialog = null;
//                            }
//                            responseStr = "0";
//                            Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                            Toast.makeText(PaymentInfoActivity.this, Util.getTextofLanguage(PaymentInfoActivity.this,Util.SLOW_INTERNET_CONNECTION,Util.DEFAULT_SLOW_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//
//                        }
//
//                    });
//
//                }catch (IOException e) {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                    responseStr = "0";
//                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                    e.printStackTrace();
//                }
//
//                JSONObject myJson =null;
//                if(responseStr!=null){
//                    myJson = new JSONObject(responseStr);
//                    statusCode = Integer.parseInt(myJson.optString("code"));
//                }
//
//                if (statusCode >= 0) {
//                    if (statusCode == 200) {
//                        if (Util.dataModel.getThirdPartyUrl().matches("") || Util.dataModel.getThirdPartyUrl().equalsIgnoreCase(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
//                            if ((myJson.has("videoUrl")) && myJson.getString("videoUrl").trim() != null && !myJson.getString("videoUrl").trim().isEmpty() && !myJson.getString("videoUrl").trim().equals("null") && !myJson.getString("videoUrl").trim().matches("")) {
//                                Util.dataModel.setVideoUrl(myJson.getString("videoUrl"));
//                                videoUrlStr = myJson.getString("videoUrl");
//                            }
//                            else{
//                                Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                            }
//                        }else{
//                            if ((myJson.has("thirdparty_url")) && myJson.getString("thirdparty_url").trim() != null && !myJson.getString("thirdparty_url").trim().isEmpty() && !myJson.getString("thirdparty_url").trim().equals("null") && !myJson.getString("thirdparty_url").trim().matches("")) {
//                                Util.dataModel.setVideoUrl(myJson.getString("thirdparty_url"));
//
//
//                            }
//                            else{
//                                Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//
//                            }
//                        }
//                        if ((myJson.has("videoResolution")) && myJson.getString("videoResolution").trim() != null && !myJson.getString("videoResolution").trim().isEmpty() && !myJson.getString("videoResolution").trim().equals("null") && !myJson.getString("videoResolution").trim().matches("")) {
//                            Util.dataModel.setVideoResolution(myJson.getString("videoResolution"));
//
//                        }
//
//                    }
//
//                }
//                else {
//
//                    responseStr = "0";
//                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                }
//            } catch (JSONException e1) {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//                responseStr = "0";
//                Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                e1.printStackTrace();
//            }
//
//            catch (Exception e)
//            {
//                if (pDialog != null && pDialog.isShowing()) {
//                    pDialog.hide();
//                    pDialog = null;
//                }
//                responseStr = "0";
//                Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//
//                e.printStackTrace();
//
//            }
//            return null;
//
//        }
//
//        protected void onPostExecute(Void result) {
//
//      /*  try{
//            if(pDialog.isShowing())
//                pDialog.dismiss();
//        }
//        catch(IllegalArgumentException ex)
//        {
//            responseStr = "0";
//            movieVideoUrlStr = getResources().getString(R.string.no_data_str);
//        }*/
//            if (responseStr == null) {
//                responseStr = "0";
//                Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
//            }
//
//            if ((responseStr.trim().equalsIgnoreCase("0"))) {
//                try {
//                    if (pDialog != null && pDialog.isShowing()) {
//                        pDialog.hide();
//                        pDialog = null;
//                    }
//                } catch (IllegalArgumentException ex) {
//                    Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                    // movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
//                }
//                Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                //movieThirdPartyUrl = getResources().getString(R.string.no_data_str);
//                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
//                dlgAlert.setMessage(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                dlgAlert.setTitle(Util.getTextofLanguage(PaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
//                dlgAlert.setCancelable(false);
//                dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                                finish();
//                            }
//                        });
//                dlgAlert.create().show();
//            } else {
//
//                if (Util.dataModel.getVideoUrl() == null) {
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
//                    dlgAlert.setMessage(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                    dlgAlert.setTitle(Util.getTextofLanguage(PaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                    finish();
//                                }
//                            });
//                    dlgAlert.create().show();
//                } else if (Util.dataModel.getVideoUrl().matches("") || Util.dataModel.getVideoUrl().equalsIgnoreCase(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA))) {
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                    }
//                    AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PaymentInfoActivity.this);
//                    dlgAlert.setMessage(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_VIDEO_AVAILABLE, Util.DEFAULT_NO_VIDEO_AVAILABLE));
//                    dlgAlert.setTitle(Util.getTextofLanguage(PaymentInfoActivity.this, Util.SORRY, Util.DEFAULT_SORRY));
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
//                    dlgAlert.setCancelable(false);
//                    dlgAlert.setPositiveButton(Util.getTextofLanguage(PaymentInfoActivity.this,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                    finish();
//                                }
//                            });
//                    dlgAlert.create().show();
//                } else {
//                    try {
//                        if (pDialog != null && pDialog.isShowing()) {
//                            pDialog.hide();
//                            pDialog = null;
//                        }
//                    } catch (IllegalArgumentException ex) {
//                        Util.dataModel.setVideoUrl(Util.getTextofLanguage(PaymentInfoActivity.this, Util.NO_DATA, Util.DEFAULT_NO_DATA));
//                    }
//                    if (!videoUrlStr.equals("")) {
//                        final Intent playVideoIntent = new Intent(PaymentInfoActivity.this, ExoPlayerActivity.class);
//                        runOnUiThread(new Runnable() {
//                            public void run() {
//                                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                startActivity(playVideoIntent);
//                                finish();
//
//                            }
//                        });
//                    }else{
//                        if (Util.dataModel.getVideoUrl().contains("://www.youtube") || Util.dataModel.getVideoUrl().contains("://www.youtu.be")){
//                            if(Util.dataModel.getVideoUrl().contains("live_stream?channel")) {
//                                final Intent playVideoIntent = new Intent(PaymentInfoActivity.this, ThirdPartyPlayer.class);
//                                runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                        startActivity(playVideoIntent);
//                                        finish();
//
//                                    }
//                                });
//                            }else{
//
//                                final Intent playVideoIntent = new Intent(PaymentInfoActivity.this, YouTubeAPIActivity.class);
//                                runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                        startActivity(playVideoIntent);
//                                        finish();
//
//
//                                    }
//                                });
//
//                            }
//                        }else{
//                            final Intent playVideoIntent = new Intent(PaymentInfoActivity.this, ThirdPartyPlayer.class);
//                            runOnUiThread(new Runnable() {
//                                public void run() {
//                                    playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                                    startActivity(playVideoIntent);
//                                    finish();
//
//                                }
//                            });
//                        }
//                    }
//                }
//
//
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//            pDialog = new ProgressBarHandler(PaymentInfoActivity.this);
//            pDialog.show();
//
//        }
//
//
//    }
    public void Download_SubTitle(String Url) {
        new DownloadFileFromURL().execute(Url);
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected String doInBackground(String... f_url) {
            int count;


            try {
                URL url = new URL(f_url[0]);
                String str = f_url[0];
                filename = str.substring(str.lastIndexOf("/") + 1);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                File root = Environment.getExternalStorageDirectory();
                mediaStorageDir = new File(root + "/Android/data/" + getApplicationContext().getPackageName().trim() + "/SubTitleList/", "");

                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        Log.d("App", "failed to create directory");
                    }
                }

                SubTitlePath.add(mediaStorageDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".vtt");
                OutputStream output = new FileOutputStream(mediaStorageDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".vtt");

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

            return null;
        }

        protected void onProgressUpdate(String... progress) {
        }

        @Override
        protected void onPostExecute(String file_url) {
            FakeSubTitlePath.remove(0);
            if (FakeSubTitlePath.size() > 0) {
                Download_SubTitle(FakeSubTitlePath.get(0).trim());
            } else {
                if (progressBarHandler != null && progressBarHandler.isShowing()) {
                    progressBarHandler.hide();
                }
                final Intent playVideoIntent;
                if (Util.dataModel.getAdNetworkId() == 3) {
                    LogUtil.showLog("responseStr", "playVideoIntent" + Util.dataModel.getAdNetworkId());

                    playVideoIntent = new Intent(PaymentInfoActivity.this, ExoPlayerActivity.class);

                } else if (Util.dataModel.getAdNetworkId() == 1 && Util.dataModel.getPreRoll() == 1) {
                    if (Util.dataModel.getPlayPos() <= 0) {
                        playVideoIntent = new Intent(PaymentInfoActivity.this, AdPlayerActivity.class);
                    } else {
                        playVideoIntent = new Intent(PaymentInfoActivity.this, ExoPlayerActivity.class);

                    }
                } else {
                    playVideoIntent = new Intent(PaymentInfoActivity.this, ExoPlayerActivity.class);

                }
                playerModel.setSubTitlePath(SubTitlePath);
                //Intent playVideoIntent = new Intent(PaymentInfoActivity.this, ExoPlayerActivity.class);
                playVideoIntent.putExtra("PlayerModel", playerModel);
                playVideoIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                startActivity(playVideoIntent);
                finish();
            }
        }
    }


}
