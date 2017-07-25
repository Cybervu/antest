package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.AuthUserPaymentInfoInputModel;
import com.home.apisdk.apiModel.AuthUserPaymentInfoOutputModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by MUVI on 1/20/2017.
 */

public class AuthUserPaymentInfoAsyntask extends AsyncTask<AuthUserPaymentInfoInputModel, Void, Void> {

    public AuthUserPaymentInfoInputModel authUserPaymentInfoInputModel;
    String PACKAGE_NAME, message, responseStr,responseMessageStr;
    int code;

    public interface AuthUserPaymentInfo {
        void onAuthUserPaymentInfoPreExecuteStarted();

        void onAuthUserPaymentInfoPostExecuteCompleted(AuthUserPaymentInfoOutputModel authUserPaymentInfoOutputModel, int status,String message);
    }

    private AuthUserPaymentInfo listener;
    private Context context;
    AuthUserPaymentInfoOutputModel authUserPaymentInfoOutputModel = new AuthUserPaymentInfoOutputModel();

    public AuthUserPaymentInfoAsyntask(AuthUserPaymentInfoInputModel authUserPaymentInfoInputModel, AuthUserPaymentInfo listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.authUserPaymentInfoInputModel = authUserPaymentInfoInputModel;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "register user payment");
    }

    @Override
    protected Void doInBackground(AuthUserPaymentInfoInputModel... params) {


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getAuthUserPaymentInfoUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader(CommonConstants.AUTH_TOKEN, this.authUserPaymentInfoInputModel.getAuthToken());
            httppost.addHeader("nameOnCard", this.authUserPaymentInfoInputModel.getName_on_card());
            httppost.addHeader("expiryMonth", this.authUserPaymentInfoInputModel.getExpiryMonth());
            httppost.addHeader("expiryYear", this.authUserPaymentInfoInputModel.getExpiryYear());
            httppost.addHeader("cardNumber", this.authUserPaymentInfoInputModel.getCardNumber());
            httppost.addHeader("cvv", this.authUserPaymentInfoInputModel.getCvv());
            httppost.addHeader("email", this.authUserPaymentInfoInputModel.getEmail());


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
                code = Integer.parseInt(myJson.optString("isSuccess"));

            }

            if (code == 1) {

                JSONObject mainJson = null;

                if (myJson.has("card")) {
                    mainJson = myJson.getJSONObject("card");
                    if (mainJson.has("status") && mainJson.optString("status").trim() != null && !mainJson.optString("status").trim().isEmpty() && !mainJson.optString("status").trim().equals("null") && !mainJson.optString("status").trim().matches("")) {
                        authUserPaymentInfoOutputModel.setStatus(mainJson.optString("status"));
                    }

                    if (mainJson.has("token") && mainJson.optString("token").trim() != null && !mainJson.optString("token").trim().isEmpty() && !mainJson.optString("token").trim().equals("null") && !mainJson.optString("token").trim().matches("")) {
                        authUserPaymentInfoOutputModel.setToken(mainJson.optString("token"));
                    }

                    if (mainJson.has("response_text") && mainJson.optString("response_text").trim() != null && !mainJson.optString("response_text").trim().isEmpty() && !mainJson.optString("response_text").trim().equals("null") && !mainJson.optString("response_text").trim().matches("")) {
                        authUserPaymentInfoOutputModel.setResponse_text(mainJson.optString("response_text"));
                    }

                    if (mainJson.has("profile_id") && mainJson.optString("profile_id").trim() != null && !mainJson.optString("profile_id").trim().isEmpty() && !mainJson.optString("profile_id").trim().equals("null") && !mainJson.optString("profile_id").trim().matches("")) {
                        authUserPaymentInfoOutputModel.setProfile_id(mainJson.optString("profile_id"));
                    }
                    if (mainJson.has("card_last_fourdigit") && mainJson.optString("card_last_fourdigit").trim() != null && !mainJson.optString("card_last_fourdigit").trim().isEmpty() && !mainJson.optString("card_last_fourdigit").trim().equals("null") && !mainJson.optString("card_last_fourdigit").trim().matches("")) {
                        authUserPaymentInfoOutputModel.setCard_last_fourdigit(mainJson.optString("card_last_fourdigit"));
                    }

                    if (mainJson.has("card_type") && mainJson.optString("card_type").trim() != null && !mainJson.optString("card_type").trim().isEmpty() && !mainJson.optString("card_type").trim().equals("null") && !mainJson.optString("card_type").trim().matches("")) {
                        authUserPaymentInfoOutputModel.setCard_type(mainJson.optString("card_type"));
                    }
                }

            }
            if (code == 0) {
                if (myJson.has("Message")) {
                    responseMessageStr = myJson.optString("Message");
                }
                if (((responseMessageStr.equalsIgnoreCase("null")) || (responseMessageStr.length() <= 0))) {
                    responseMessageStr="No Details found";

            }
        }


    } catch(
    Exception e)

    {
        code = 0;
        message = "";

    }
        return null;
}


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onAuthUserPaymentInfoPreExecuteStarted();
        code = 0;
     /*   if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            listener.onGetPlanListPostExecuteCompleted(planListOutput,code);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            listener.onGetPlanListPostExecuteCompleted(planListOutput,code);
        }*/

    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onAuthUserPaymentInfoPostExecuteCompleted(authUserPaymentInfoOutputModel, code,responseMessageStr);
    }
}
