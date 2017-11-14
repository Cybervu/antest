package com.home.vod.activity;

/**
 * Created by MUVI on 10/11/2017.
 */

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.home.apisdk.apiController.DownloadContentAsync;
import com.home.apisdk.apiController.HeaderConstants;
import com.home.apisdk.apiModel.DownloadContentInput;
import com.home.vod.R;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;

import static com.home.vod.preferences.LanguagePreference.BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_BUTTON_OK;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DIET_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_DOWNLOAD_BUTTON_TITLE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGIN_STATUS_MESSAGE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_PDF;
import static com.home.vod.preferences.LanguagePreference.DIET_BUTTON;
import static com.home.vod.preferences.LanguagePreference.DOWNLOAD_BUTTON_TITLE;
import static com.home.vod.preferences.LanguagePreference.LOGIN_STATUS_MESSAGE;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.NO_PDF;
import static com.home.vod.util.Constant.authTokenStr;


public class DietPlanActivity extends AppCompatActivity implements DownloadContentAsync.DownloadContentListener {
    static File mediaStorageDir;
    int progress_bar_type = 0;
    int progressStatus = 0;
    String dietData = "";
    LanguagePreference languagePreference;
    ProgressDialog pDialog;
    String filename;
    ProgressBarHandler Ph;
    // TextView textView;
    Context context;
    ProgressBar progresBar;
    WebView dietPlanWebView;
    PreferenceManager preferenceManager;
    String Download_Url;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    ProgressBarHandler pHandler;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_diet_plan);
        context = DietPlanActivity.this;

        preferenceManager = PreferenceManager.getPreferenceManager(this);
        progresBar = (ProgressBar) findViewById(R.id.progress_bar);
        pHandler = new ProgressBarHandler(DietPlanActivity.this);
        pHandler.show();
        dietPlanWebView = (WebView) findViewById(R.id.dietPlanWebView);
        dietPlanWebView.setBackgroundColor(getResources().getColor(R.color.appBackgroundColor));

        DownloadContentInput downloadContentInput = new DownloadContentInput();
        downloadContentInput.setAuthToken(authTokenStr);
        Log.v("SUBHA","getIntent().getStringExtra(HeaderConstants.VLINK)"+getIntent().getStringExtra(HeaderConstants.VLINK));
        downloadContentInput.setvLink(getIntent().getStringExtra(HeaderConstants.VLINK));
        DownloadContentAsync asyndownloadContent = new DownloadContentAsync(downloadContentInput, this, this);
        asyndownloadContent.executeOnExecutor(threadPoolExecutor);

        TextView categoryTitle = (TextView) findViewById(R.id.dietPlanTitle);
        Typeface castDescriptionTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.regular_fonts));
        categoryTitle.setTypeface(castDescriptionTypeface);
//        categoryTitle.setText(languagePreference.getTextofLanguage(DIET_BUTTON,DEFAULT_DIET_BUTTON));
        categoryTitle.setText("Diet Plan");

        languagePreference = LanguagePreference.getLanguagePreference(DietPlanActivity.this);

        ImageView closeButton = (ImageView)findViewById(R.id.dietCloseButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
            }
        });

        Button downloadDietPlanButton = (Button) findViewById(R.id.downloadDietPlanButton);
        Typeface typeface7 = Typeface.createFromAsset(getAssets(),getResources().getString(R.string.regular_fonts));
        downloadDietPlanButton.setTypeface(typeface7);
        downloadDietPlanButton.setText(languagePreference.getTextofLanguage(DOWNLOAD_BUTTON_TITLE, DEFAULT_DOWNLOAD_BUTTON_TITLE));

        downloadDietPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String loggedInStr = "";
                if (preferenceManager.getLoginFeatureFromPref() == 1) {
                    loggedInStr = preferenceManager.getUseridFromPref();
                }

                if (loggedInStr != null) {

                    if (ContextCompat.checkSelfPermission(DietPlanActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(DietPlanActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            ActivityCompat.requestPermissions(DietPlanActivity.this,
                                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_CONTACTS},
                                    111);
                        } else {
                            ActivityCompat.requestPermissions(DietPlanActivity.this,
                                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    111);
                        }
                    } else {

                        //Call whatever you want
                        if (NetworkStatus.getInstance().isConnected(DietPlanActivity.this)) {
                            if (!dietData.equals("")) {
                                Log.v("SUBHALAXMI", "CALLED");
                                DownloadTransactionDetails();
                            } else {
                                com.home.vod.util.Util.showToast(getApplicationContext(), languagePreference.getTextofLanguage(NO_PDF, DEFAULT_NO_PDF));
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();

                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(LOGIN_STATUS_MESSAGE, DEFAULT_LOGIN_STATUS_MESSAGE), Toast.LENGTH_LONG).show();

                }



            }
        });



    }

    public void DownloadTransactionDetails() {
        Log.v("SUBHA", "DH" + dietData);

        LocalBroadcastManager.getInstance(DietPlanActivity.this).registerReceiver(InternetStatusforDietPlan, new IntentFilter("android.net.wifi.STATE_CHANGE"));
        Log.v("SUBHA", "DH1" + dietData);

        new DownloadFileFromURL().execute(dietData);
        Log.v("SUBHA","DH2"+dietData);

    }
    @Override
    public void onDownloadContentPreExecuteStarted() {
        progresBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDownloadContentPostExecuteCompleted(String filepath,String fileName) {
      //  progresBar.setVisibility(View.GONE);
        LogUtil.showLog("SUBHA", "filepath.getPermalink()" + filepath);
        progresBar.setVisibility(View.VISIBLE);
        dietData = filepath;
        filename = fileName;
        dietPlanWebView.getSettings().setJavaScriptEnabled(true);
        dietPlanWebView.getSettings().setDomStorageEnabled(true);
        try {
            String googledoc="http://docs.google.com/gview?embedded=true&url=";
            String encodedurl = URLEncoder.encode(filepath, "UTF-8");
            dietPlanWebView.setWebViewClient(new Callback());
            dietPlanWebView.loadUrl(googledoc + encodedurl);

           /* try {
                String encodedurl = URLEncoder.encode(filepath,"UTF-8");

                PdfWebViewClient pdfWebViewClient = new PdfWebViewClient(this, dietPlanWebView);
                pdfWebViewClient.loadPdfUrl(
                        "http://docs.google.com/gview?embedded=true&url="+encodedurl);

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return (false);
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);
            pHandler.hide();

            //progresBar.setVisibility(View.GONE);
            view.loadUrl("javascript:(function() { " +
                    "document.querySelector('[role=\"toolbar\"]').remove();})()");
        }
    }

    private BroadcastReceiver InternetStatusforDietPlan= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            //  Toast.makeText(getApplicationContext(),""+Util.checkNetwork(TransactionDetailsActivity.this),Toast.LENGTH_SHORT).show();
            if (NetworkStatus.getInstance().isConnected(DietPlanActivity.this)) {
                if (pDialog.isShowing() && pDialog != null) {
                    try {
                        LocalBroadcastManager.getInstance(DietPlanActivity.this).unregisterReceiver(InternetStatusforDietPlan);
                        pDialog.setProgress(0);
                        progressStatus = 0;

                        if(pDialog!=null && pDialog.isShowing())
                            pDialog.dismiss();

                    }catch (Exception e){

                    }


                }
            }
        }
    };

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            showDialog(progress_bar_type);
            ShowDownloadStatus();
        }

        /**
         * Downloading file in background thread
         */
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected String doInBackground(String... f_url) {
            int count;

            try {
//                URL url = new URL("https://static.pexels.com/photos/242616/pexels-photo-242616.jpeg");
                URL url = new URL(f_url[0]);
                String str = f_url[0];
               // filename = str.substring(str.lastIndexOf("/") + 1);
                Log.v("SUBHALAXMI","FILE"+filename);
                Log.v("SUBHALAXMI","FILE"+ f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "");

                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                    }
                }
                // Output stream


                OutputStream output = new FileOutputStream(mediaStorageDir + "/" + filename);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    // Log.v("SUBHA", "Lrngth" + data.length);
                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.v("SUBHALAXMI","Exception"+e.toString());

            } catch (Throwable throwable) {
                Log.v("SUBHALAXMI","throwable"+throwable.toString());

                throwable.printStackTrace();
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));

            if ((Integer.parseInt(progress[0])) == 100) {
                Log.v("SUBHALAXMI", "Exception" + 100);

                try {
                   LocalBroadcastManager.getInstance(DietPlanActivity.this).unregisterReceiver(InternetStatusforDietPlan);
                   pDialog.setProgress(0);
                   progressStatus = 0;
//                   dismissDialog(0);
                   pDialog.dismiss();
                    showDialog(("Download Completed Successfully."), 1);


                }catch (Exception e){
                   Log.v("SUBHALAXMI","Exception==1"+e.toString());
               }

                //Calling Api To Delete Pdf file from the Server.


            }

        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded


        }
    }




    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.show();
                progressStatus = 0;
                return pDialog;
            default:
                return null;
        }
    }

    public void ShowDownloadStatus(){
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Downloading file. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
        pDialog.show();
        progressStatus = 0;
    }


    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 111: {

                if (grantResults.length > 0) {
                    if ((grantResults.length > 0) && (grantResults[0]) == PackageManager.PERMISSION_GRANTED) {
                        //Call whatever you want
                        if (NetworkStatus.getInstance().isConnected(DietPlanActivity.this)) {
                            if (!dietData.equals(""))
                                DownloadTransactionDetails();
                            else
                                Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(NO_PDF, DEFAULT_NO_PDF), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), languagePreference.getTextofLanguage(NO_INTERNET_CONNECTION, DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }

                return;
            }
        }
    }



    public void showDialog(String msg, final int deletevalue) {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(DietPlanActivity.this);
        dlgAlert.setMessage(msg);
        dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        dlgAlert.create().show();

    }


    class PdfWebViewClient extends WebViewClient {
        private static final String TAG = "PdfWebViewClient";
        private static final String PDF_EXTENSION = ".pdf";
        private static final String PDF_VIEWER_URL = "http://docs.google.com/gview?embedded=true&url=";

        private Context mContext;
        private WebView mWebView;
        private ProgressBarHandler mProgressDialog;
        private boolean isLoadingPdfUrl;

        public PdfWebViewClient(Context context, WebView webView) {
            mContext = context;
            mWebView = webView;
            mWebView.setWebViewClient(this);
        }

        public void loadPdfUrl(String url) {
            mWebView.stopLoading();

            if (!TextUtils.isEmpty(url)) {
                isLoadingPdfUrl = isPdfUrl(url);
                if (isLoadingPdfUrl) {
                    mWebView.clearHistory();
                }

                showProgressDialog();
            }

            mWebView.loadUrl(url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            return shouldOverrideUrlLoading(url);
        }

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
            handleError(errorCode, description.toString(), failingUrl);
        }


        @Override
        public void onReceivedError(final WebView webView, final WebResourceRequest request, final WebResourceError error) {
            final Uri uri = request.getUrl();
            handleError(error.getErrorCode(), error.getDescription().toString(), uri.toString());
        }

        @Override
        public void onPageFinished(final WebView view, final String url) {
            Log.i(TAG, "Finished loading. URL : " + url);
            view.loadUrl("javascript:(function() { " +
                    "document.querySelector('[role=\"toolbar\"]').remove();})()");
            dismissProgressDialog();

        }

        private boolean shouldOverrideUrlLoading(final String url) {
            Log.i(TAG, "shouldOverrideUrlLoading() URL : " + url);

            if (!isLoadingPdfUrl && isPdfUrl(url)) {
                mWebView.stopLoading();

                final String pdfUrl = PDF_VIEWER_URL + url;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadPdfUrl(pdfUrl);
                    }
                }, 300);

                return true;
            }

            return false; // Load url in the webView itself
        }

        private void handleError(final int errorCode, final String description, final String failingUrl) {
            Log.e(TAG, "Error : " + errorCode + ", " + description + " URL : " + failingUrl);
        }

        private void showProgressDialog() {
            dismissProgressDialog();
            mProgressDialog = new ProgressBarHandler(mContext);
            mProgressDialog.show();

           // mProgressDialog = ProgressDialog.show(mContext, "", "Loading...");
        }

        private void dismissProgressDialog() {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.hide();
                mProgressDialog = null;
            }
        }

        private boolean isPdfUrl(String url) {
            if (!TextUtils.isEmpty(url)) {
                url = url.trim();
                int lastIndex = url.toLowerCase().lastIndexOf(PDF_EXTENSION);
                if (lastIndex != -1) {
                    return url.substring(lastIndex).equalsIgnoreCase(PDF_EXTENSION);
                }
            }
            return false;
        }
    }

}