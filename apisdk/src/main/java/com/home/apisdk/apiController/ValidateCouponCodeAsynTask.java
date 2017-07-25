package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.ValidateCouponCodeInputModel;
import com.home.apisdk.apiModel.ValidateCouponCodeOutputModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by User on 12-12-2016.
 */
public class ValidateCouponCodeAsynTask extends AsyncTask<ValidateCouponCodeInputModel, Void, Void> {

    ValidateCouponCodeInputModel validateCouponCodeInputModel;
    String responseStr;
    int status;
    float planPrice = 0.0f;
    float chargedPrice = 0.0f;
    float previousChargedPrice = 0.0f;
    String message, PACKAGE_NAME;

    public interface ValidateCouponCode {
        void onValidateCouponCodePreExecuteStarted();

        void onValidateCouponCodePostExecuteCompleted(ValidateCouponCodeOutputModel validateCouponCodeOutputModel, int status, String message);
    }

    private ValidateCouponCode listener;
    private Context context;
    ValidateCouponCodeOutputModel validateCouponCodeOutputModel = new ValidateCouponCodeOutputModel();

    public ValidateCouponCodeAsynTask(ValidateCouponCodeInputModel validateCouponCodeInputModel, ValidateCouponCode listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.validateCouponCodeInputModel = validateCouponCodeInputModel;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "validate voucher");

    }

    @Override
    protected Void doInBackground(ValidateCouponCodeInputModel... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getValidateCouponCodeUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader(CommonConstants.AUTH_TOKEN, this.validateCouponCodeInputModel.getAuthToken());

            httppost.addHeader(CommonConstants.USER_ID, this.validateCouponCodeInputModel.getUser_id());
            httppost.addHeader(CommonConstants.COUPAN_CODE, this.validateCouponCodeInputModel.getCouponCode());
            httppost.addHeader(CommonConstants.CURRENCY_ID, this.validateCouponCodeInputModel.getCurrencyId());


            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("MUVISDK", "RES" + responseStr);

            } catch (org.apache.http.conn.ConnectTimeoutException e) {
                status = 0;
                message = "";

            } catch (IOException e) {
                status = 0;
                message = "";
            }

            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                status = Integer.parseInt(myJson.optString("code"));
                message = myJson.optString("msg");
            }


            if ((myJson.has("discount_type")) && myJson.optString("discount_type").trim() != null && !myJson.optString("discount_type").trim().isEmpty() && !myJson.optString("discount_type").trim().equals("null") && !myJson.optString("discount_type").trim().matches("")) {
                String discountTypeStr = myJson.optString("discount_type").trim();
                validateCouponCodeOutputModel.setDiscount_type(myJson.optString("discount_type").trim());

                if ((myJson.has("discount")) && myJson.optString("discount").trim() != null && !myJson.optString("discount").trim().isEmpty() && !myJson.optString("discount").trim().equals("null") && !myJson.optString("discount").trim().matches("")) {

                    validateCouponCodeOutputModel.setDiscount(myJson.optString("discount").trim());
                }

                {

                    if (discountTypeStr.equalsIgnoreCase("%")) {

                        chargedPrice = planPrice - planPrice * (Float.parseFloat(myJson.getString("discount")) / 100);

                        if (chargedPrice < 0.0f) {
                            chargedPrice = 0.0f;
                        }
                    } else {

                        chargedPrice = planPrice - Float.parseFloat(myJson.getString("discount").trim());

                        if (chargedPrice < 0.0f) {
                            chargedPrice = 0.0f;
                        }
                    }

                }
            }


        } catch (Exception e) {
            status = 0;
            message = "";
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onValidateCouponCodePreExecuteStarted();
        responseStr = "0";
        status = 0;
           /* if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
            {
                this.cancel(true);
                message = "Packge Name Not Matched";
                listener.onGetContentListPostExecuteCompleted(featureContentOutputModel,status,totalItems,message);
                return;
            }
            if(CommonConstants.hashKey.equals(""))
            {
                this.cancel(true);
                message = "Hash Key Is Not Available. Please Initialize The SDK";
                listener.onGetContentListPostExecuteCompleted(featureContentOutputModel,status,totalItems,message);
            }*/


    }


    @Override
    protected void onPostExecute(Void result) {
        listener.onValidateCouponCodePostExecuteCompleted(validateCouponCodeOutputModel, status, responseStr);

    }

    //}
}
