package com.home.apisdk.apiController;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.WithouPaymentSubscriptionRegDetailsInput;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by MUVI on 7/6/2017.
 */

public class WithouPaymentSubscriptionRegDetailsAsync extends AsyncTask<WithouPaymentSubscriptionRegDetailsInput, Void, Void> {

    WithouPaymentSubscriptionRegDetailsInput withouPaymentSubscriptionRegDetailsInput;

    String responseStr;
    int status;
    String message, PACKAGE_NAME;

    public interface WithouPaymentSubscriptionRegDetails {
        void onGetWithouPaymentSubscriptionRegDetailsPreExecuteStarted();

        void onGetWithouPaymentSubscriptionRegDetailsPostExecuteCompleted(int status, String Response);
    }

    private WithouPaymentSubscriptionRegDetails listener;
    private Context context;

    public WithouPaymentSubscriptionRegDetailsAsync(WithouPaymentSubscriptionRegDetailsInput withouPaymentSubscriptionRegDetailsInput, WithouPaymentSubscriptionRegDetails listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.withouPaymentSubscriptionRegDetailsInput = withouPaymentSubscriptionRegDetailsInput;
        PACKAGE_NAME = context.getPackageName();

    }

    @Override
    protected Void doInBackground(WithouPaymentSubscriptionRegDetailsInput... params) {


        try {

            try {
                URL url = new URL(APIUrlConstant.getAddSubscriptionUrl());
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter(CommonConstants.AUTH_TOKEN, this.withouPaymentSubscriptionRegDetailsInput.getAuthToken())
                        .appendQueryParameter(CommonConstants.IS_ADVANCE, this.withouPaymentSubscriptionRegDetailsInput.getIs_advance())
                        .appendQueryParameter(CommonConstants.CARD_NAME, this.withouPaymentSubscriptionRegDetailsInput.getCard_name())
                        .appendQueryParameter(CommonConstants.EXP_MONTH, this.withouPaymentSubscriptionRegDetailsInput.getExp_month())
                        .appendQueryParameter(CommonConstants.CARD_NUMBER, this.withouPaymentSubscriptionRegDetailsInput.getCard_number())
                        .appendQueryParameter(CommonConstants.EXP_YEAR, this.withouPaymentSubscriptionRegDetailsInput.getExp_year())
                        .appendQueryParameter(CommonConstants.EMAIL, this.withouPaymentSubscriptionRegDetailsInput.getEmail())
                        .appendQueryParameter(CommonConstants.MOVIE_ID, this.withouPaymentSubscriptionRegDetailsInput.getMovie_id())
                        .appendQueryParameter(CommonConstants.USER_ID, this.withouPaymentSubscriptionRegDetailsInput.getUser_id())
                        .appendQueryParameter(CommonConstants.COUPAN_CODE, this.withouPaymentSubscriptionRegDetailsInput.getCoupon_code())
                        .appendQueryParameter(CommonConstants.CARD_TYPE, this.withouPaymentSubscriptionRegDetailsInput.getCard_type())
                        .appendQueryParameter(CommonConstants.CARD_LAST_FOUR_DIGIT, this.withouPaymentSubscriptionRegDetailsInput.getCard_last_fourdigit())
                        .appendQueryParameter(CommonConstants.PROFILE_ID, this.withouPaymentSubscriptionRegDetailsInput.getProfile_id())
                        .appendQueryParameter(CommonConstants.TOKEN, this.withouPaymentSubscriptionRegDetailsInput.getToken())
                        .appendQueryParameter(CommonConstants.CVV, this.withouPaymentSubscriptionRegDetailsInput.getCvv())
                        .appendQueryParameter(CommonConstants.COUNTRY, this.withouPaymentSubscriptionRegDetailsInput.getCountry())
                        .appendQueryParameter(CommonConstants.SEASON_ID, this.withouPaymentSubscriptionRegDetailsInput.getSeason_id())
                        .appendQueryParameter(CommonConstants.EPISODE_ID, this.withouPaymentSubscriptionRegDetailsInput.getEpisode_id())
                        .appendQueryParameter(CommonConstants.CURRENCY_ID, this.withouPaymentSubscriptionRegDetailsInput.getCurrency_id())
                        .appendQueryParameter(CommonConstants.IS_SAVE_THIS_CARD, this.withouPaymentSubscriptionRegDetailsInput.getIs_save_this_card())
                        .appendQueryParameter(CommonConstants.EXISTING_CARD_ID, this.withouPaymentSubscriptionRegDetailsInput.getExisting_card_id());
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                InputStream ins = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(ins);
                BufferedReader in = new BufferedReader(isr);

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                    responseStr = inputLine;
                    Log.v("MUVISDK", "responseStr" + responseStr);

                }
                in.close();

            }
            // Execute HTTP Post Request
            catch (org.apache.http.conn.ConnectTimeoutException e) {
                Log.v("MUVISDK", "org.apache.http.conn.ConnectTimeoutException e" + e.toString());

                status = 0;
                message = "";

            } catch (IOException e) {
                Log.v("MUVISDK", "IOException" + e.toString());

                status = 0;
                message = "";
            }
            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                status = Integer.parseInt(myJson.optString("code"));
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetWithouPaymentSubscriptionRegDetailsPreExecuteStarted();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onGetWithouPaymentSubscriptionRegDetailsPostExecuteCompleted(status,responseStr);
    }
}
