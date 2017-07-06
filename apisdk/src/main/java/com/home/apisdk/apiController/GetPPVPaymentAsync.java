package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.RegisterUserPaymentInputModel;
import com.home.apisdk.apiModel.RegisterUserPaymentOutputModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by MUVI on 7/6/2017.
 */

public class GetPPVPaymentAsync extends AsyncTask<RegisterUserPaymentInputModel, Void, Void> {

    public RegisterUserPaymentInputModel registerUserPaymentInputModel;
    String PACKAGE_NAME, message, responseStr;
    int code;

    public interface GetPPVPayment {
        void onGetPPVPaymentPreExecuteStarted();

        void onGetPPVPaymentPostExecuteCompleted(RegisterUserPaymentOutputModel registerUserPaymentOutputModel, int status,String response);
    }

    private GetPPVPayment listener;
    private Context context;
    RegisterUserPaymentOutputModel registerUserPaymentOutputModel = new RegisterUserPaymentOutputModel();

    public GetPPVPaymentAsync(RegisterUserPaymentInputModel registerUserPaymentInputModel, GetPPVPayment listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.registerUserPaymentInputModel = registerUserPaymentInputModel;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "register user payment");

    }

    @Override
    protected Void doInBackground(RegisterUserPaymentInputModel... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getAddSubscriptionUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.registerUserPaymentInputModel.getAuthToken());
            httppost.addHeader("user_id", this.registerUserPaymentInputModel.getUser_id());
            httppost.addHeader("card_last_fourdigit", this.registerUserPaymentInputModel.getCard_last_fourdigit());
            httppost.addHeader("card_name", this.registerUserPaymentInputModel.getCard_name());
            httppost.addHeader("card_number", this.registerUserPaymentInputModel.getCard_number());
            httppost.addHeader("card_type", this.registerUserPaymentInputModel.getCard_type());
            httppost.addHeader("country", this.registerUserPaymentInputModel.getCountry());
            httppost.addHeader("currency_id", this.registerUserPaymentInputModel.getCurrency_id());
            httppost.addHeader("cvv", this.registerUserPaymentInputModel.getCvv());
            httppost.addHeader("email", this.registerUserPaymentInputModel.getEmail());
            httppost.addHeader("episode_id", this.registerUserPaymentInputModel.getEpisode_id());
            httppost.addHeader("exp_month", this.registerUserPaymentInputModel.getExp_month());
            httppost.addHeader("exp_year", this.registerUserPaymentInputModel.getExp_year());
            httppost.addHeader("season_id", this.registerUserPaymentInputModel.getSeason_id());
            httppost.addHeader("profile_id", this.registerUserPaymentInputModel.getProfile_id());
            httppost.addHeader("token", this.registerUserPaymentInputModel.getToken());
            httppost.addHeader("coupon_code", this.registerUserPaymentInputModel.getCouponCode());
            httppost.addHeader("is_save_this_card",this.registerUserPaymentInputModel.getIs_save_this_card());
            httppost.addHeader("is_advance",this.registerUserPaymentInputModel.getIs_advance());
            httppost.addHeader("movie_id",this.registerUserPaymentInputModel.getMovie_id());


            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("MUVISDK", "RES" + responseStr);

            } catch (org.apache.http.conn.ConnectTimeoutException e) {
                code = 0;
                message = "";


            } catch (IOException e) {
                code = 0;
                message = "";

            }
            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                code = Integer.parseInt(myJson.optString("code"));

            }

//            if (code == 200) {
//                if ((myJson.has("msg")) && myJson.optString("msg").trim() != null && !myJson.optString("msg").trim().isEmpty() && !myJson.optString("msg").trim().equals("null") && !myJson.optString("msg").trim().matches("")) {
//                    registerUserPaymentOutputModel.setMsg(myJson.optString("msg"));
//                }
//
//            }

        } catch (Exception e) {
            code = 0;
            message = "";

        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetPPVPaymentPreExecuteStarted();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onGetPPVPaymentPostExecuteCompleted(registerUserPaymentOutputModel,code,responseStr);
    }
}

