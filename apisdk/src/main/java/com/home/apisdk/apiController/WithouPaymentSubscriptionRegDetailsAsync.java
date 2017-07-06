package com.home.apisdk.apiController;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
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
                        .appendQueryParameter("authToken", this.withouPaymentSubscriptionRegDetailsInput.getAuthToken())
                        .appendQueryParameter("is_advance", this.withouPaymentSubscriptionRegDetailsInput.getIs_advance())
                        .appendQueryParameter("card_name", this.withouPaymentSubscriptionRegDetailsInput.getCard_name())
                        .appendQueryParameter("exp_month", this.withouPaymentSubscriptionRegDetailsInput.getExp_month())
                        .appendQueryParameter("card_number", this.withouPaymentSubscriptionRegDetailsInput.getCard_number())
                        .appendQueryParameter("exp_year", this.withouPaymentSubscriptionRegDetailsInput.getExp_year())
                        .appendQueryParameter("email", this.withouPaymentSubscriptionRegDetailsInput.getEmail())
                        .appendQueryParameter("movie_id", this.withouPaymentSubscriptionRegDetailsInput.getMovie_id())
                        .appendQueryParameter("user_id", this.withouPaymentSubscriptionRegDetailsInput.getUser_id())
                        .appendQueryParameter("coupon_code", this.withouPaymentSubscriptionRegDetailsInput.getCoupon_code())
                        .appendQueryParameter("card_type", this.withouPaymentSubscriptionRegDetailsInput.getCard_type())
                        .appendQueryParameter("card_last_fourdigit", this.withouPaymentSubscriptionRegDetailsInput.getCard_last_fourdigit())
                        .appendQueryParameter("profile_id", this.withouPaymentSubscriptionRegDetailsInput.getProfile_id())
                        .appendQueryParameter("token", this.withouPaymentSubscriptionRegDetailsInput.getToken())
                        .appendQueryParameter("cvv", this.withouPaymentSubscriptionRegDetailsInput.getCvv())
                        .appendQueryParameter("country", this.withouPaymentSubscriptionRegDetailsInput.getCountry())
                        .appendQueryParameter("season_id", this.withouPaymentSubscriptionRegDetailsInput.getSeason_id())
                        .appendQueryParameter("episode_id", this.withouPaymentSubscriptionRegDetailsInput.getEpisode_id())
                        .appendQueryParameter("currency_id", this.withouPaymentSubscriptionRegDetailsInput.getCurrency_id())
                        .appendQueryParameter("is_save_this_card", this.withouPaymentSubscriptionRegDetailsInput.getIs_save_this_card())
                        .appendQueryParameter("existing_card_id", this.withouPaymentSubscriptionRegDetailsInput.getExisting_card_id());
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
