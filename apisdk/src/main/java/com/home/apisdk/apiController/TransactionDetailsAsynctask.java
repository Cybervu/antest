package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.TransactionInputModel;
import com.home.apisdk.apiModel.TransactionOutputModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by User on 12-12-2016.
 */
public class TransactionDetailsAsynctask extends AsyncTask<TransactionInputModel, Void, Void> {

    TransactionInputModel transactionInputModel;
    String responseStr;
    int status;
    String message, PACKAGE_NAME;

    public interface Transaction {
        void onTransactionPreExecuteStarted();

        void onTransactionPostExecuteCompleted(TransactionOutputModel transactionOutputModel, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private Transaction listener;
    private Context context;
    TransactionOutputModel transactionOutputModel = new TransactionOutputModel();

    public TransactionDetailsAsynctask(TransactionInputModel transactionInputModel, Transaction listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.transactionInputModel = transactionInputModel;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "transaction");


    }

    @Override
    protected Void doInBackground(TransactionInputModel... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getTransactionUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader("authToken", this.transactionInputModel.getAuthToken());
            httppost.addHeader("user_id", this.transactionInputModel.getUser_id());
            httppost.addHeader("id", this.transactionInputModel.getId());

            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());


            } catch (org.apache.http.conn.ConnectTimeoutException e) {

                status = 0;
                message = "Error";


            } catch (IOException e) {
                status = 0;
                message = "Error";
            }

            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                status = Integer.parseInt(myJson.optString("code"));
                message = myJson.optString("msg");
            }

            if (status > 0) {

                if (status == 200) {

                    JSONObject mainJson = myJson.getJSONObject("section");
                    if ((mainJson.has("order_number")) && mainJson.optString("order_number").trim() != null && !mainJson.optString("order_number").trim().isEmpty() && !mainJson.optString("order_number").trim().equals("null") && !mainJson.optString("order_number").trim().matches("")) {
                        transactionOutputModel.setOrder_number(mainJson.optString("order_number"));
                    } else {
                        transactionOutputModel.setOrder_number("");

                    }
                    if ((mainJson.has("movie_id")) && mainJson.optString("movie_id").trim() != null && !mainJson.optString("movie_id").trim().isEmpty() && !mainJson.optString("movie_id").trim().equals("null") && !mainJson.optString("movie_id").trim().matches("")) {
                        transactionOutputModel.setMovie_id(mainJson.optString("movie_id"));


                    } else {
                        transactionOutputModel.setMovie_id("");

                    }
                    if ((mainJson.has("transaction_date")) && mainJson.optString("transaction_date").trim() != null && !mainJson.optString("transaction_date").trim().isEmpty() && !mainJson.optString("transaction_date").trim().equals("null") && !mainJson.optString("transaction_date").trim().matches("")) {
                        transactionOutputModel.setTransaction_date(mainJson.optString("transaction_date"));


                    } else {
                        transactionOutputModel.setTransaction_date("");

                    }
                    if ((mainJson.has("transaction_status")) && mainJson.optString("transaction_status").trim() != null && !mainJson.optString("transaction_status").trim().isEmpty() && !mainJson.optString("transaction_status").trim().equals("null") && !mainJson.optString("transaction_status").trim().matches("")) {
                        transactionOutputModel.setTransaction_status(mainJson.optString("transaction_status"));
                    } else {
                        transactionOutputModel.setTransaction_status("");

                    }
                    if ((mainJson.has("plan_name")) && mainJson.optString("plan_name").trim() != null && !mainJson.optString("plan_name").trim().isEmpty() && !mainJson.optString("plan_name").trim().equals("null") && !mainJson.optString("plan_name").trim().matches("")) {
                        transactionOutputModel.setPlan_name(mainJson.optString("plan_name"));
                    } else {
                        transactionOutputModel.setPlan_name("");

                    }

                    if ((mainJson.has("currency_symbol")) && mainJson.optString("currency_symbol").trim() != null && !mainJson.optString("currency_symbol").trim().isEmpty() && !mainJson.optString("currency_symbol").trim().equals("null") && !mainJson.optString("currency_symbol").trim().matches("")) {
                        transactionOutputModel.setCurrency_symbol(mainJson.optString("currency_symbol"));

                    } else {
                        transactionOutputModel.setCurrency_symbol("");

                    }

                    if ((mainJson.has("currency_code")) && mainJson.optString("currency_code").trim() != null && !mainJson.optString("currency_code").trim().isEmpty() && !mainJson.optString("currency_code").trim().equals("null") && !mainJson.optString("currency_code").trim().matches("")) {
                        transactionOutputModel.setCurrency_code(mainJson.optString("currency_code"));
                    } else {
                        transactionOutputModel.setCurrency_code("");

                    }

                    if ((mainJson.has("amount")) && mainJson.optString("amount").trim() != null && !mainJson.optString("amount").trim().isEmpty() && !mainJson.optString("amount").trim().equals("null") && !mainJson.optString("amount").trim().matches("")) {
                        transactionOutputModel.setAmount(mainJson.optString("amount"));

                    } else {
                        transactionOutputModel.setAmount("");

                    }


                }
            } else {

                responseStr = "0";
                status = 0;
                message = "Error";
            }
        } catch (final JSONException e1) {

            responseStr = "0";
            status = 0;
            message = "Error";
        } catch (Exception e) {

            responseStr = "0";
            status = 0;
            message = "Error";
        }
        return null;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onTransactionPreExecuteStarted();

        status = 0;
        /*if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onTransactionPostExecuteCompleted(transactionOutputModel,status,message);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onTransactionPostExecuteCompleted(transactionOutputModel,status,message);
        }*/
        listener.onTransactionPostExecuteCompleted(transactionOutputModel, status, message);

    }


    @Override
    protected void onPostExecute(Void result) {
        listener.onTransactionPostExecuteCompleted(transactionOutputModel, status, message);

    }

    //}
}
