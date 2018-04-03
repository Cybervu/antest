package com.home.vod.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.home.api.api.APIUrlConstant;
import com.home.api.api.apiController.APICallManager;
import com.home.api.api.apiModel.GetStaticPagedetailsModel;
import com.home.vod.R;
import com.home.vod.activity.MainActivity;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.util.FontUtls;
import com.home.vod.util.ProgressBarHandler;

import java.util.HashMap;

import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.NO_DATA;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.util.Constant.authTokenStr;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment implements APICallManager.ApiInterafce {
    String about;
    // TextView textView;
    Context context;
    ProgressBar progresBar;
    WebView webView;
    ProgressBarHandler pDialog;
    LanguagePreference languagePreference;
    boolean returnValue = false;
    TextView noInternetTextView;

    RelativeLayout noInternet;

    boolean isNetwork;

    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       /* getActionBar().setTitle(getArguments().getString(""));
        setHasOptionsMenu(true);*/

        final View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        context = getActivity();
        languagePreference = LanguagePreference.getLanguagePreference(context);
        //isNetwork = playerOld.utils.Util.checkNetwork(context);
        isNetwork = com.home.api.player.utils.Util.checkNetwork(context);

        noInternet = (RelativeLayout) view.findViewById(R.id.noInternet);
        progresBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        noInternetTextView = (TextView) view.findViewById(R.id.noInternetTextView);
        noInternetTextView.setText(languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION));
        noInternet.setVisibility(View.GONE);


        webView = (WebView) view.findViewById(R.id.aboutUsWebView);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView webView, int progress) {
                progresBar.setVisibility(View.VISIBLE);
              /*  view.setFocusableInTouchMode(true);
                view.requestFocus();*/
                if (progress == 100) {
                    progresBar.setVisibility(View.GONE);
                  /*  view.setFocusableInTouchMode(true);
                    view.requestFocus();*/
                }
            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                // Handle the error
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                return true;
            }
        });


        if (isNetwork) {

            final HashMap parameters = new HashMap<>();
            String strtext = getArguments().getString("item");
            parameters.put("authToken", authTokenStr);
            parameters.put("lang_code", languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
            parameters.put("permalink", strtext);
            final APICallManager apiCallManager = new APICallManager(this, APIUrlConstant.GETSTATICPAGES_URL, parameters, APIUrlConstant.GETSTATICPAGES_URL_REQUEST_ID, APIUrlConstant.BASE_URl);
            apiCallManager.startApiProcessing();


            /*AboutUsInput aboutUsInput = new AboutUsInput();
            aboutUsInput.setAuthToken(authTokenStr);
            aboutUsInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
            String strtext = getArguments().getString("item");
            aboutUsInput.setPermalink(strtext);
            asyncAboutUS = new AboutUsAsync(aboutUsInput, this, context);
            asyncAboutUS.execute();*/
        } else {
            noInternet.setVisibility(View.VISIBLE);
        }
        TextView categoryTitle = (TextView) view.findViewById(R.id.categoryTitle);
        FontUtls.loadFont(context, context.getResources().getString(R.string.regular_fonts), categoryTitle);
        /*Typeface castDescriptionTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.regular_fonts));
        categoryTitle.setTypeface(castDescriptionTypeface);*/
        categoryTitle.setText(Html.fromHtml(getArguments().getString("title")));

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        webView.setFocusableInTouchMode(true);
        webView.requestFocus();


        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        if (webView.canGoBack()) {
                            webView.goBack();
                            returnValue = true;
                        } else {
                            final Intent startIntent = new Intent(getActivity(), MainActivity.class);

                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    getActivity().startActivity(startIntent);
                                    getActivity().finish();

                                }
                            });
                        }
                    }
                }
                return returnValue;
            }
        });

        return view;

    }

 /*   @Override
    public void onStop() {
        super.onStop();
        Log.v("BIBHU11","onStop pressed");

        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            final Intent startIntent = new Intent(getActivity(), MainActivity.class);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().startActivity(startIntent);
                    getActivity().finish();

                }
            });
        }
    }*/

    private ActionBar getActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

   /* @Override
    public void onAboutUsPreExecuteStarted() {
        pDialog = new ProgressBarHandler(context);
        pDialog.show();
    }

    @Override
    public void onAboutUsPostExecuteCompleted(int status, String about) {

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
        } catch (IllegalArgumentException ex) {

        }

        progresBar.setVisibility(View.GONE);
        String bodyData = about;
        if (status == 200) {
          *//*  textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(getStyledTextFromHtml(bodyData));*//*
            int color = getActivity().getResources().getColor(R.color.aboutustextcolor);
            String aboutUSTextColor = "#" + Integer.toHexString(color & 0x00FFFFFF);
            String text = "<html><head>"
                    + "<style type=\"text/css\" >body{color:" + aboutUSTextColor + ";}"
                    + "</style></head>"
                    + "<body style >"
                    + about
                    + "</body></html>";

            webView.loadData(text, "text/html", "utf-8");
            webView.setBackgroundColor(getResources().getColor(R.color.aboutustestcolor));
            webView.getSettings().setJavaScriptEnabled(true);
        }else {

            noInternetTextView.setText(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
            noInternet.setVisibility(View.VISIBLE);
        }
    }*/

    @Override
    public void onTaskPreExecute(int requestID) {

        pDialog = new ProgressBarHandler(context);
        pDialog.show();

    }

    @Override
    public void onTaskPostExecute(Object object, int requestID, String response) {
        if (APIUrlConstant.GETSTATICPAGES_URL_REQUEST_ID == requestID) {
            about_us(object, requestID, response);
        }
    }

    public void about_us(Object object, int requestID, String response) {

        GetStaticPagedetailsModel getStaticPagedetailsModel = (GetStaticPagedetailsModel) object;

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
        } catch (IllegalArgumentException ex) {

        }

        progresBar.setVisibility(View.GONE);
        String bodyData = about;
        if (getStaticPagedetailsModel.getCode() == 200) {
          /*  textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(getStyledTextFromHtml(bodyData));*/
            int color = getActivity().getResources().getColor(R.color.aboutustextcolor);
            String aboutUSTextColor = "#" + Integer.toHexString(color & 0x00FFFFFF);
            String text = "<html><head>"
                    + "<style type=\"text/css\" >body{color:" + aboutUSTextColor + ";}"
                    + "</style></head>"
                    + "<body style >"
                    + about
                    + "</body></html>";

            webView.loadData(text, "text/html", "utf-8");
            webView.setBackgroundColor(getResources().getColor(R.color.aboutustestcolor));
            webView.getSettings().setJavaScriptEnabled(true);
        } else {

            noInternetTextView.setText(languagePreference.getTextofLanguage(NO_DATA, DEFAULT_NO_DATA));
            noInternet.setVisibility(View.VISIBLE);
        }
    }


   /* public static CharSequence getStyledTextFromHtml(String source) {
        return android.text.Html.fromHtml(replaceNewlinesWithBreaks(source));
    }
    public static String replaceNewlinesWithBreaks(String source) {
        return source != null ? source.replaceAll("(?:\n|\r\n)","<br>") : "";
    }*/
}

