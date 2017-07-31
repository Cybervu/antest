package com.home.apisdk.apiModel.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.apiModel.APIUrlConstant;
import com.home.apisdk.apiModel.CommonConstants;
import com.home.apisdk.apiModel.apiModel.VoucherSubscriptionInputModel;
import com.home.apisdk.apiModel.apiModel.VoucherSubscriptionOutputModel;

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

public class VoucherSubscriptionAsyntask extends AsyncTask<VoucherSubscriptionInputModel, Void, Void> {

    public VoucherSubscriptionInputModel voucherSubscriptionInputModel;
    String PACKAGE_NAME, message, responseStr;
    int code;

    public interface VoucherSubscription {
        void onVoucherSubscriptionPreExecuteStarted();

        void onVoucherSubscriptionPostExecuteCompleted(VoucherSubscriptionOutputModel voucherSubscriptionOutputModel, int status);
    }

    private VoucherSubscription listener;
    private Context context;
    VoucherSubscriptionOutputModel voucherSubscriptionOutputModel = new VoucherSubscriptionOutputModel();

    public VoucherSubscriptionAsyntask(VoucherSubscriptionInputModel voucherSubscriptionInputModel, VoucherSubscription listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.voucherSubscriptionInputModel = voucherSubscriptionInputModel;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "register user payment");

    }

    @Override
    protected Void doInBackground(VoucherSubscriptionInputModel... params) {


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getVoucherSubscriptionUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader(CommonConstants.AUTH_TOKEN, this.voucherSubscriptionInputModel.getAuthToken());
            httppost.addHeader(CommonConstants.USER_ID, this.voucherSubscriptionInputModel.getUser_id());
            httppost.addHeader(CommonConstants.MOVIE_ID, this.voucherSubscriptionInputModel.getMovie_id());
            httppost.addHeader(CommonConstants.STREAM_ID, this.voucherSubscriptionInputModel.getStream_id());
            httppost.addHeader(CommonConstants.PURCHASE_TYPE, this.voucherSubscriptionInputModel.getPurchase_type());
            httppost.addHeader(CommonConstants.VOUCHER_CODE, this.voucherSubscriptionInputModel.getVoucher_code());
            httppost.addHeader(CommonConstants.IS_PREORDER, this.voucherSubscriptionInputModel.getIs_preorder());
            httppost.addHeader(CommonConstants.SEASON, this.voucherSubscriptionInputModel.getSeason());


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

            if ((myJson.has("msg")) && myJson.optString("msg").trim() != null && !myJson.optString("msg").trim().isEmpty() && !myJson.optString("msg").trim().equals("null") && !myJson.optString("msg").trim().matches("")) {
                voucherSubscriptionOutputModel.setMsg(myJson.optString("msg"));
            }

        } catch (Exception e) {
            code = 0;
            message = "";

        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onVoucherSubscriptionPreExecuteStarted();
        code = 0;
        if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            listener.onVoucherSubscriptionPostExecuteCompleted(voucherSubscriptionOutputModel, code);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            listener.onVoucherSubscriptionPostExecuteCompleted(voucherSubscriptionOutputModel, code);
        }

    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onVoucherSubscriptionPostExecuteCompleted(voucherSubscriptionOutputModel, code);
    }
}
