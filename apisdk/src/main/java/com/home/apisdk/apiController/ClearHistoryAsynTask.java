package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.ClearHistoryInputModel;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by BISHAL on 05-05-2018.
 */

public class ClearHistoryAsynTask extends AsyncTask<ClearHistoryInputModel,Void,Void> {

    private ClearHistoryInputModel clearHistoryInputModel;
    private String responseStr;
    private int status;
    private String message;
    private String PACKAGE_NAME;
    private ClearHistoryAsynTask.ClearHistoryListener listener;
    private Context context;

    public interface ClearHistoryListener{
        void onClearHistoryPreExecuteStarted();
        void onClearHistoryPostExecuteCompleted(int status, String message);

    }
    public ClearHistoryAsynTask(ClearHistoryInputModel clearHistoryInputModel, ClearHistoryAsynTask.ClearHistoryListener listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.clearHistoryInputModel = clearHistoryInputModel;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "GetContentListAsynTask");

    }

    @Override
    protected Void doInBackground(ClearHistoryInputModel... clearHistoryInputModels) {
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getClearWatchHistory());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader(HeaderConstants.AUTH_TOKEN, this.clearHistoryInputModel.getAuthToken());
            httppost.addHeader(HeaderConstants.USER_ID, this.clearHistoryInputModel.getUser_id());
            httppost.addHeader(HeaderConstants.LANG_CODE, this.clearHistoryInputModel.getLang_code());

            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("MUVISDK", "RES" + responseStr);

            } catch (org.apache.http.conn.ConnectTimeoutException e) {
                status = 0;
                message = "";
                Log.v("MUVISDK", "ConnectTimeoutException" + e.toString());

            } catch (IOException e) {
                status = 0;
                message = "";
                Log.v("MUVISDK", "IOException" + e.toString());
            }

            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                status = Integer.parseInt(myJson.optString("code"));
                message = myJson.optString("status");
            }

        } catch (Exception e) {
            status = 0;
            message = "";
            Log.v("MUVISDK", "Exception" + e.toString());
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onClearHistoryPreExecuteStarted();
        responseStr = "0";
        status = 0;
        if (!PACKAGE_NAME.equals(SDKInitializer.getUser_Package_Name_At_Api(context))) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onClearHistoryPostExecuteCompleted(status,message);
            return;
        }
        if (SDKInitializer.getHashKey(context).equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onClearHistoryPostExecuteCompleted(status,message);
        }
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        listener.onClearHistoryPostExecuteCompleted(status,message);
    }
}
