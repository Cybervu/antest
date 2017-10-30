package com.home.vod.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.home.apisdk.apiController.AboutUsAsync;
import com.home.apisdk.apiModel.AboutUsInput;
import com.home.vod.R;
import com.home.vod.util.FontUtls;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.activity.MainActivity;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.util.Util;

import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.util.Constant.authTokenStr;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment implements AboutUsAsync.AboutUsListener {
    String about;
    // TextView textView;
    Context context;
    ProgressBar progresBar;
    WebView webView;
    ProgressBarHandler pDialog;
    AboutUsAsync asyncAboutUS;
    LanguagePreference languagePreference;


    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       /* getActionBar().setTitle(getArguments().getString(""));
        setHasOptionsMenu(true);*/

        View view = inflater.inflate(R.layout.fragment_about_us, container, false);
        context = getActivity();
        languagePreference = LanguagePreference.getLanguagePreference(context);
        progresBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        webView = (WebView) view.findViewById(R.id.aboutUsWebView);
        AboutUsInput aboutUsInput = new AboutUsInput();
        aboutUsInput.setAuthToken(authTokenStr);
        aboutUsInput.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
        String strtext = getArguments().getString("item");
        aboutUsInput.setPermalink(strtext);
        asyncAboutUS = new AboutUsAsync(aboutUsInput, this, context);
        asyncAboutUS.execute();
        TextView categoryTitle = (TextView) view.findViewById(R.id.categoryTitle);
        FontUtls.loadFont(context, context.getResources().getString(R.string.regular_fonts), categoryTitle);
        /*Typeface castDescriptionTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.regular_fonts));
        categoryTitle.setTypeface(castDescriptionTypeface);*/
        categoryTitle.setText(Html.fromHtml(getArguments().getString("title")));

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

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
                return false;
            }
        });

        return view;

    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onAboutUsPreExecuteStarted() {
        pDialog = new ProgressBarHandler(context);
        pDialog.show();
    }

    @Override
    public void onAboutUsPostExecuteCompleted(String about) {

        try {
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.hide();
                pDialog = null;
            }
        } catch (IllegalArgumentException ex) {

        }

        progresBar.setVisibility(View.GONE);
        String bodyData = about;

          /*  textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(getStyledTextFromHtml(bodyData));*/

        String text = "<html><head>"
                + "<style type=\"text/css\" >body{color:#fff;}"
                + "</style></head>"
                + "<body style >"
                + about
                + "</body></html>";

        webView.loadData(text, "text/html", "utf-8");
        webView.setBackgroundColor(getResources().getColor(R.color.aboutustestcolor));
        webView.getSettings().setJavaScriptEnabled(true);
    }


   /* public static CharSequence getStyledTextFromHtml(String source) {
        return android.text.Html.fromHtml(replaceNewlinesWithBreaks(source));
    }
    public static String replaceNewlinesWithBreaks(String source) {
        return source != null ? source.replaceAll("(?:\n|\r\n)","<br>") : "";
    }*/
}

