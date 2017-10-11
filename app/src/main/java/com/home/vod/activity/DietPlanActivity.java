package com.home.vod.activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.home.vod.R;
import com.home.vod.util.ProgressBarHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import player.utils.Util;

public class DietPlanActivity extends AppCompatActivity {
    static File mediaStorageDir;
    int progress_bar_type = 0;
    int progressStatus = 0;
    String dietData;
    ProgressDialog progressDialog;
    ProgressDialog pDialog;
    String filename;
    ProgressBarHandler Ph;
    // TextView textView;
    Context context;
    ProgressBar progresBar;
    WebView dietPlanWebView;
    AsyncAboutUS asyncAboutUS;
    String Download_Url;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_plan);
        context = DietPlanActivity.this;


        progresBar = (ProgressBar) findViewById(R.id.progress_bar);
        dietPlanWebView = (WebView) findViewById(R.id.dietPlanWebView);
        asyncAboutUS = new AsyncAboutUS();
        asyncAboutUS.execute();
        TextView categoryTitle = (TextView) findViewById(R.id.dietPlanTitle);
        Typeface castDescriptionTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.regular_fonts));
        categoryTitle.setTypeface(castDescriptionTypeface);
        categoryTitle.setText("DIET PLAN");

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
        downloadDietPlanButton.setText(Util.getTextofLanguage(DietPlanActivity.this, Util.DOWNLOAD_BUTTON_TITLE, Util.DEFAULT_DOWNLOAD_BUTTON_TITLE));

        downloadDietPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(DietPlanActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
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
                    if (Util.checkNetwork(DietPlanActivity.this)) {


                        DownloadDocumentDetails downloadDocumentDetails = new DownloadDocumentDetails();
                        downloadDocumentDetails.executeOnExecutor(threadPoolExecutor);



                        // Toast.makeText(getApplicationContext(),Util.getTextofLanguage(TransactionDetailsActivity.this,Util.NO_PDF,Util.DEFAULT_NO_PDF), Toast.LENGTH_LONG).show();
                    } else {
                        //Util.showToast(getApplicationContext(),Util.getTextofLanguage(getApplicationContext(),Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));

//                        Toast.makeText(getApplicationContext(),Util.getTextofLanguage(TransactionDetailsActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                    }
                }




            }
        });



    }
    public class AsyncAboutUS extends AsyncTask<Void, Void, Void> {
        String responseStr;

        private ProgressBarHandler progressBarHandler = null;

        @Override
        protected void onPostExecute(Void aVoid) {
            progresBar.setVisibility(View.GONE);
            String bodyData = dietData;

          /*  textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(getStyledTextFromHtml(bodyData));*/

            String text = "<html><head>"
                    + "<style type=\"text/css\" >body{color:#333; background-color: #fff;}"
                    + "</style></head>"
                    + "<body style >"
                    + dietData
                    + "</body></html>";

            dietPlanWebView.loadData(text, "text/html", "utf-8");
            dietPlanWebView.getSettings().setJavaScriptEnabled(true);
        }

        @Override
        protected Void doInBackground(Void... voids) {
//            String urlRouteList = "https://sonydadc.muvi.com/rest" + "/getStaticPagedetails";
            String urlRouteList = Util.rootUrl().trim() + Util.AboutUs.trim();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    progresBar.setVisibility(View.VISIBLE);
                }

            });

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlRouteList);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr.trim());
               // String strtext = getArguments().getString("item");
                httppost.addHeader("permalink","about-us");
                httppost.addHeader("lang_code",Util.getTextofLanguage(DietPlanActivity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());

                } catch (org.apache.http.conn.ConnectTimeoutException e) {

                }

            } catch (IOException e) {

                e.printStackTrace();
            }

            JSONObject myJson = null;
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
            try {
                JSONObject jsonMainNode = myJson.getJSONObject("page_details");
                dietData = jsonMainNode.optString("content").trim();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    //Asyntask to get Transaction Details.

    private class DownloadDocumentDetails extends AsyncTask<Void, Void, Void> {

        String responseStr = "";
        int status;

        @Override
        protected Void doInBackground(Void... params) {

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Util.rootUrl().trim() + Util.GetInvoicePDF.trim());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", Util.authTokenStr);

                httppost.addHeader("user_id",getSharedPreferences(Util.LOGIN_PREF,0).getString("PREFS_LOGGEDIN_ID_KEY", null));
               // httppost.addHeader("id", id);
                httppost.addHeader("device_type", "app");
                httppost.addHeader("lang_code",Util.getTextofLanguage(DietPlanActivity.this,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));



                // Execute HTTP Post Request
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());
                } catch (Exception e) {

                }

                JSONObject myJson = null;
                if (responseStr != null) {
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                }
                if (status > 0) {
                    if (status == 200) {


                        Download_Url = myJson.optString("section");
                        if (Download_Url.equals("") || Download_Url == null || Download_Url.equals("null")) {

                            Download_Url = "";
                        }
                    } else {
                        responseStr = "0";
                    }
                } else {
                    responseStr = "0";

                }
            } catch (final JSONException e1) {
                responseStr = "0";
            } catch (Exception e) {
                responseStr = "0";
            }
            return null;

        }

        protected void onPostExecute(Void result) {

            try {
                if (Ph.isShowing())
                    Ph.hide();
            } catch (IllegalArgumentException ex) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       /* primary_layout.setVisibility(View.GONE);
                        noInternet.setVisibility(View.VISIBLE);*/

                    }

                });
                responseStr = "0";
            }
            if (responseStr == null)
                responseStr = "0";

            if ((responseStr.trim().equals("0"))) {
              //  primary_layout.setVisibility(View.GONE);
               // noInternet.setVisibility(View.VISIBLE);
            } else {

                if (!Download_Url.equals("")) {
                    DownloadTransactionDetails();
                }
                else {
                   // Util.showToast(getApplicationContext(), Util.getTextofLanguage(getApplicationContext(), Util.NO_PDF, Util.DEFAULT_NO_PDF));
                }
            }
        }

        @Override
        protected void onPreExecute() {

            Ph = new ProgressBarHandler(DietPlanActivity.this);
            Ph.show();

            /*progressDialog = new ProgressDialog(TransactionDetailsActivity.this,R.style.MyTheme);
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large_Inverse);
            progressDialog.setIndeterminate(false);
            progressDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.progress_rawable));
            progressDialog.show()*/
            ;
        }
    }
    public void DownloadTransactionDetails() {

        registerReceiver(InternetStatus, new IntentFilter("android.net.wifi.STATE_CHANGE"));
        new DownloadFileFromURL().execute(Util.Dwonload_pdf_rootUrl + Download_Url);


    }

    private BroadcastReceiver InternetStatus = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            //  Toast.makeText(getApplicationContext(),""+Util.checkNetwork(TransactionDetailsActivity.this),Toast.LENGTH_SHORT).show();
            if (!Util.checkNetwork(DietPlanActivity.this)) {
                if (pDialog.isShowing() && pDialog != null) {

                    //showDialog(Util.getTextofLanguage(DietPlanActivity.this,Util.DOWNLOAD_INTERRUPTED,Util.DEFAULT_DOWNLOAD_INTERRUPTED), 0);
                    unregisterReceiver(InternetStatus);
                    pDialog.setProgress(0);
                    progressStatus = 0;
                    dismissDialog(progress_bar_type);
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
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         */
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
            } catch (Throwable throwable) {
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

             //   showDialog(Util.getTextofLanguage(DietPlanActivity.this,Util.DOWNLOAD_COMPLETED,Util.DEFAULT_DOWNLOAD_COMPLETED), 1);

                unregisterReceiver(InternetStatus);
                pDialog.setProgress(0);
                progressStatus = 0;
                dismissDialog(progress_bar_type);

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

                if(grantResults.length>0)
                {
                    if ((grantResults.length > 0) && (grantResults[0]) == PackageManager.PERMISSION_GRANTED) {
                        //Call whatever you want
                        if (Util.checkNetwork(DietPlanActivity.this)) {
                            DownloadDocumentDetails downloadDocumentDetails = new DownloadDocumentDetails();
                            downloadDocumentDetails.executeOnExecutor(threadPoolExecutor);
                        } else {
                           // Util.showToast(getApplicationContext(),Util.getTextofLanguage(getApplicationContext(),Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION));
//                            Toast.makeText(getApplicationContext(),Util.getTextofLanguage(TransactionDetailsActivity.this,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        finish();
                    }
                }
                else
                {
                    finish();
                }

                return;
            }
        }
    }
}
