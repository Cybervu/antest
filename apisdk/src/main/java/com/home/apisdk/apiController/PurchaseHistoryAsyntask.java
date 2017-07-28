package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.PurchaseHistoryInputModel;
import com.home.apisdk.apiModel.PurchaseHistoryOutputModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by MUVI on 1/20/2017.
 */

public class PurchaseHistoryAsyntask extends AsyncTask<PurchaseHistoryInputModel, Void, Void> {

    public PurchaseHistoryInputModel purchaseHistoryInputModel;
    String PACKAGE_NAME, message, responseStr;
    int code;

    public interface PurchaseHistory {
        void onPurchaseHistoryPreExecuteStarted();

        void onPurchaseHistoryPostExecuteCompleted(ArrayList<PurchaseHistoryOutputModel> purchaseHistoryOutputModel, int status);
    }

    private PurchaseHistory listener;
    private Context context;
    ArrayList<PurchaseHistoryOutputModel> purchaseHistoryOutputModel = new ArrayList<PurchaseHistoryOutputModel>();

    public PurchaseHistoryAsyntask(PurchaseHistoryInputModel purchaseHistoryInputModel, PurchaseHistory listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.purchaseHistoryInputModel = purchaseHistoryInputModel;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "getPlanListAsynctask");

    }

    @Override
    protected Void doInBackground(PurchaseHistoryInputModel... params) {


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getPurchaseHistoryUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader(CommonConstants.AUTH_TOKEN, this.purchaseHistoryInputModel.getAuthToken());
            httppost.addHeader(CommonConstants.USER_ID, this.purchaseHistoryInputModel.getUser_id());
            httppost.addHeader(CommonConstants.LANG_CODE,this.purchaseHistoryInputModel.getLang_code());
            httppost.addHeader(CommonConstants.ID,this.purchaseHistoryInputModel.getId());


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
                message = myJson.optString("status");

            }

            if (code == 200) {

                JSONArray jsonMainNode = myJson.getJSONArray("section");

                int lengthJsonArr = jsonMainNode.length();
                for (int i = 0; i < lengthJsonArr; i++) {
                    JSONObject jsonChildNode;
                    try {
                        jsonChildNode = jsonMainNode.getJSONObject(i);
                        PurchaseHistoryOutputModel content = new PurchaseHistoryOutputModel();

                        if ((jsonChildNode.has("amount")) && jsonChildNode.optString("amount").trim() != null && !jsonChildNode.optString("amount").trim().isEmpty() && !jsonChildNode.optString("amount").trim().equals("null") && !jsonChildNode.optString("amount").trim().matches("")) {
                            content.setAmount(jsonChildNode.optString("amount"));
                        }
                        if ((jsonChildNode.has("currency_code")) && jsonChildNode.optString("currency_code").trim() != null && !jsonChildNode.optString("currency_code").trim().isEmpty() && !jsonChildNode.optString("currency_code").trim().equals("null") && !jsonChildNode.optString("currency_code").trim().matches("")) {
                            content.setCurrency_code(jsonChildNode.optString("currency_code"));

                        }
                        if ((jsonChildNode.has("currency_symbol")) && jsonChildNode.optString("currency_symbol").trim() != null && !jsonChildNode.optString("currency_symbol").trim().isEmpty() && !jsonChildNode.optString("currency_symbol").trim().equals("null") && !jsonChildNode.optString("currency_symbol").trim().matches("")) {
                            content.setCurrency_symbol(jsonChildNode.optString("currency_symbol"));
                        }
                        if ((jsonChildNode.has("id")) && jsonChildNode.optString("id").trim() != null && !jsonChildNode.optString("id").trim().isEmpty() && !jsonChildNode.optString("id").trim().equals("null") && !jsonChildNode.optString("id").trim().matches("")) {
                            content.setId(jsonChildNode.optString("id"));
                        }
                        if ((jsonChildNode.has("invoice_id")) && jsonChildNode.optString("invoice_id").trim() != null && !jsonChildNode.optString("invoice_id").trim().isEmpty() && !jsonChildNode.optString("invoice_id").trim().equals("null") && !jsonChildNode.optString("invoice_id").trim().matches("")) {
                            content.setInvoice_id(jsonChildNode.optString("invoice_id"));
                        }
                        if ((jsonChildNode.has("statusppv")) && jsonChildNode.optString("statusppv").trim() != null && !jsonChildNode.optString("statusppv").trim().isEmpty() && !jsonChildNode.optString("statusppv").trim().equals("null") && !jsonChildNode.optString("statusppv").trim().matches("")) {
                            content.setStatusppv(jsonChildNode.optString("statusppv"));
                        }
                        if ((jsonChildNode.has("transaction_date")) && jsonChildNode.optString("transaction_date").trim() != null && !jsonChildNode.optString("transaction_date").trim().isEmpty() && !jsonChildNode.optString("transaction_date").trim().equals("null") && !jsonChildNode.optString("transaction_date").trim().matches("")) {
                            content.setTransaction_date(jsonChildNode.optString("transaction_date"));
                        }
                        if ((jsonChildNode.has("transaction_status")) && jsonChildNode.optString("transaction_status").trim() != null && !jsonChildNode.optString("transaction_status").trim().isEmpty() && !jsonChildNode.optString("transaction_status").trim().equals("null") && !jsonChildNode.optString("transaction_status").trim().matches("")) {
                            content.setTransaction_status(jsonChildNode.optString("transaction_status"));
                        }


                        purchaseHistoryOutputModel.add(content);
                    } catch (Exception e) {
                        code = 0;
                        message = "";
                    }
                }
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
        listener.onPurchaseHistoryPreExecuteStarted();
        code = 0;
        if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onPurchaseHistoryPostExecuteCompleted(purchaseHistoryOutputModel, code);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onPurchaseHistoryPostExecuteCompleted(purchaseHistoryOutputModel, code);
        }
        listener.onPurchaseHistoryPostExecuteCompleted(purchaseHistoryOutputModel, code);

    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onPurchaseHistoryPostExecuteCompleted(purchaseHistoryOutputModel, code);
    }
}
