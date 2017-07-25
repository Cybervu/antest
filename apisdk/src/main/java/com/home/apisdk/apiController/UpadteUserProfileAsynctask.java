package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.Update_UserProfile_Input;
import com.home.apisdk.apiModel.Update_UserProfile_Output;

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

public class UpadteUserProfileAsynctask extends AsyncTask<Update_UserProfile_Input, Void, Void> {

    public Update_UserProfile_Input update_userProfile_input;
    String PACKAGE_NAME, message, responseStr;
    int code;
    Update_UserProfile_Output update_userProfile_output;

    public interface Update_UserProfile {
        void onUpdateUserProfilePreExecuteStarted();

        void onUpdateUserProfilePostExecuteCompleted(Update_UserProfile_Output update_userProfile_output, int code, String message);
    }

    private Update_UserProfile listener;
    private Context context;

    public UpadteUserProfileAsynctask(Update_UserProfile_Input update_userProfile_input, Update_UserProfile listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.update_userProfile_input = update_userProfile_input;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "UpadteUserProfileAsynctask");

    }

    @Override
    protected Void doInBackground(Update_UserProfile_Input... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getUpdateProfileUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader(CommonConstants.AUTH_TOKEN, this.update_userProfile_input.getAuthToken());
            httppost.addHeader(CommonConstants.USER_ID, this.update_userProfile_input.getUser_id());
            httppost.addHeader(CommonConstants.NAME, this.update_userProfile_input.getName());
            httppost.addHeader(CommonConstants.PASSWORD, this.update_userProfile_input.getPassword());
            httppost.addHeader(CommonConstants.CUSTOM_LANGUAGES,this.update_userProfile_input.getCustom_languages());
            httppost.addHeader(CommonConstants.CUSTOM_COUNTRY,this.update_userProfile_input.getCustom_country());
            httppost.addHeader(CommonConstants.LANG_CODE,this.update_userProfile_input.getLang_code());

            // Execute HTTP Post Request
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
                message = myJson.optString("msg");
            }

            if (code == 200) {


                try {
                    update_userProfile_output = new Update_UserProfile_Output();
                    update_userProfile_output.setName(myJson.optString("name"));
                    update_userProfile_output.setEmail(myJson.optString("email"));
                    update_userProfile_output.setNick_name(myJson.optString("nick_name"));
                    update_userProfile_output.setProfile_image(myJson.optString("profile_image"));

                    Log.v("MUVISDK", "user_name====== " + myJson.optString("name"));

                } catch (Exception e) {
                    code = 0;
                    message = "";
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
        listener.onUpdateUserProfilePreExecuteStarted();
        code = 0;
//        if (!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api)) {
//            this.cancel(true);
//            message = "Packge Name Not Matched";
//            listener.onUpdateUserProfilePostExecuteCompleted(update_userProfile_output, code, message);
//            return;
//        }
//        if (CommonConstants.hashKey.equals("")) {
//            this.cancel(true);
//            message = "Hash Key Is Not Available. Please Initialize The SDK";
//            listener.onUpdateUserProfilePostExecuteCompleted(update_userProfile_output, code, message);
//        }
    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onUpdateUserProfilePostExecuteCompleted(update_userProfile_output, code, message);
    }
}
