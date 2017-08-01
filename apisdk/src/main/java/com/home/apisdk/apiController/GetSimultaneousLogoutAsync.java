package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.SimultaneousLogoutInput;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by MUVI on 7/4/2017.
 */

public class GetSimultaneousLogoutAsync extends AsyncTask<SimultaneousLogoutInput, Void, Void> {
    public SimultaneousLogoutInput simultaneousLogoutInput;
    String PACKAGE_NAME, responseStr,message;
    int code;

    public interface SimultaneousLogoutAsync {
        void onSimultaneousLogoutPreExecuteStarted();

        void onSimultaneousLogoutPostExecuteCompleted(int code);
    }

    private SimultaneousLogoutAsync listener;
    private Context context;

    public GetSimultaneousLogoutAsync(SimultaneousLogoutInput simultaneousLogoutInput, SimultaneousLogoutAsync listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.simultaneousLogoutInput = simultaneousLogoutInput;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "LogoutAsynctask");

    }

    @Override
    protected Void doInBackground(SimultaneousLogoutInput... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getLogoutAll());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader(CommonConstants.AUTH_TOKEN, this.simultaneousLogoutInput.getAuthToken());
            httppost.addHeader(CommonConstants.DEVICE_TYPE, this.simultaneousLogoutInput.getDevice_type());
            httppost.addHeader(CommonConstants.EMAIL_ID, this.simultaneousLogoutInput.getEmail_id());

            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("MUVISDK", "RES" + responseStr);

            } catch (ClientProtocolException e) {
                responseStr = "0";
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                code = Integer.parseInt(myJson.optString("code"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onSimultaneousLogoutPreExecuteStarted();
        code = 0;
        if (!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api)) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onSimultaneousLogoutPostExecuteCompleted(code);
            return;
        }
        if (CommonConstants.hashKey.equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onSimultaneousLogoutPostExecuteCompleted(code);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onSimultaneousLogoutPostExecuteCompleted(code);
    }
}
