package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
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

            httppost.addHeader(CommonConstants.AUTH_TOKEN, this.registerUserPaymentInputModel.getAuthToken());
            httppost.addHeader(CommonConstants.USER_ID, this.registerUserPaymentInputModel.getUser_id());
            httppost.addHeader(CommonConstants.CARD_LAST_FOUR_DIGIT, this.registerUserPaymentInputModel.getCard_last_fourdigit());
            httppost.addHeader(CommonConstants.CARD_NAME, this.registerUserPaymentInputModel.getCard_name());
            httppost.addHeader(CommonConstants.CARD_NUMBER, this.registerUserPaymentInputModel.getCard_number());
            httppost.addHeader(CommonConstants.CARD_TYPE, this.registerUserPaymentInputModel.getCard_type());
            httppost.addHeader(CommonConstants.COUNTRY, this.registerUserPaymentInputModel.getCountry());
            httppost.addHeader(CommonConstants.CURRENCY_ID, this.registerUserPaymentInputModel.getCurrency_id());
            httppost.addHeader(CommonConstants.CVV, this.registerUserPaymentInputModel.getCvv());
            httppost.addHeader(CommonConstants.EMAIL, this.registerUserPaymentInputModel.getEmail());
            httppost.addHeader(CommonConstants.EPISODE_ID, this.registerUserPaymentInputModel.getEpisode_id());
            httppost.addHeader(CommonConstants.EXP_MONTH, this.registerUserPaymentInputModel.getExp_month());
            httppost.addHeader(CommonConstants.EXP_YEAR, this.registerUserPaymentInputModel.getExp_year());
            httppost.addHeader(CommonConstants.SEASON_ID, this.registerUserPaymentInputModel.getSeason_id());
            httppost.addHeader(CommonConstants.PROFILE_ID, this.registerUserPaymentInputModel.getProfile_id());
            httppost.addHeader(CommonConstants.TOKEN, this.registerUserPaymentInputModel.getToken());
            httppost.addHeader(CommonConstants.COUPAN_CODE, this.registerUserPaymentInputModel.getCouponCode());
            httppost.addHeader(CommonConstants.IS_SAVE_THIS_CARD,this.registerUserPaymentInputModel.getIs_save_this_card());
            httppost.addHeader(CommonConstants.IS_ADVANCE,this.registerUserPaymentInputModel.getIs_advance());
            httppost.addHeader(CommonConstants.MOVIE_ID,this.registerUserPaymentInputModel.getMovie_id());


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
        code = 0;
        if (!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api)) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetPPVPaymentPostExecuteCompleted(registerUserPaymentOutputModel,code,responseStr);
            return;
        }
        if (CommonConstants.hashKey.equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetPPVPaymentPostExecuteCompleted(registerUserPaymentOutputModel,code,responseStr);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onGetPPVPaymentPostExecuteCompleted(registerUserPaymentOutputModel,code,responseStr);
    }
}

