package com.home.apisdk.apiModel.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.apiModel.APIUrlConstant;
import com.home.apisdk.apiModel.CommonConstants;
import com.home.apisdk.apiModel.apiModel.AboutUsInput;

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

public class AboutUsAsync extends AsyncTask<AboutUsInput, Void, Void> {

    public AboutUsInput aboutUsInput;
    int status;
    String message,PACKAGE_NAME, about, responseStr;

    public interface AboutUs {
        void onAboutUsPreExecuteStarted();

        void onAboutUsPostExecuteCompleted(String about);
    }

    private AboutUs listener;
    private Context context;

    public AboutUsAsync(AboutUsInput contactUsInputModel, AboutUs listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.aboutUsInput = contactUsInputModel;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "GetUserProfileAsynctask");

    }

    @Override
    protected Void doInBackground(AboutUsInput... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getAboutUs());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader(CommonConstants.AUTH_TOKEN, this.aboutUsInput.getAuthToken());
            httppost.addHeader(CommonConstants.PERMALINK, this.aboutUsInput.getPermalink());
            httppost.addHeader(CommonConstants.LANG_CODE, this.aboutUsInput.getLang_code());

            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("MUVISDK", "RES" + responseStr);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject myJson = null;
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
            try {
                JSONObject jsonMainNode = myJson.getJSONObject("page_details");
                about = jsonMainNode.optString("content");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }catch (Exception e)
        {


        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onAboutUsPreExecuteStarted();
        status = 0;
        if (!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api)) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onAboutUsPostExecuteCompleted(about);
            return;
        }
        if (CommonConstants.hashKey.equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onAboutUsPostExecuteCompleted(about);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onAboutUsPostExecuteCompleted(about);
    }
}