package com.home.apisdk.apiController;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.AboutUsInput;
import com.home.apisdk.apiModel.GmailLoginInput;
import com.home.apisdk.apiModel.GmailLoginOutput;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Android on 9/21/2017.
 */

public class AsyncGmailReg extends AsyncTask<GmailLoginInput,Void,Void> {
    private GmailLoginInput gmailLoginInput;
    private int status;
    private String message;
    private String PACKAGE_NAME;
    private String responseStr;
    private AsyncGmailReg.AsyncGmailListener listener;
    private Context context;

    public interface AsyncGmailListener {

        void onGmailRegPreExecuteStarted();

        void onGmailRegPostExecuteCompleted(GmailLoginOutput gmailLoginOutput, int status, String message);
    }

    GmailLoginOutput gmailLoginOutput=new GmailLoginOutput();


    public AsyncGmailReg(GmailLoginInput gmailLoginInput, AsyncGmailReg.AsyncGmailListener listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.gmailLoginInput = gmailLoginInput;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "GetUserProfileAsynctask");

    }

    @Override
    protected Void doInBackground(GmailLoginInput... params) {
//            String urlRouteList = "https://www.muvi.com/rest/socialAuth";
        try {
            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getGmailRegUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader("name", this.gmailLoginInput.getName());
            httppost.addHeader("email", this.gmailLoginInput.getEmail());
            httppost.addHeader("password","");
            httppost.addHeader("authToken", this.gmailLoginInput.getAuthToken());
            httppost.addHeader("gplus_userid", this.gmailLoginInput.getGmail_userid());
//            httppost.addHeader("fb_userid", this.gmailLoginInput.getGmail_userid());
            httppost.addHeader("profile_image", this.gmailLoginInput.getProfile_image());
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("Nihar",responseStr);
            } catch (org.apache.http.conn.ConnectTimeoutException e){
                status = 0;
                message = "Error";

            } catch (IOException e) {
                status = 0;
                message = "Error";
            }

            JSONObject myJson = null;
            if(responseStr!=null){
                myJson = new JSONObject(responseStr);
                status = Integer.parseInt(myJson.optString("code"));
                message = myJson.optString("status");

                if (status==200){

                    if ((myJson.has("id")) && myJson.optString("id").trim() != null && !myJson.optString("id").trim().isEmpty() && !myJson.optString("id").trim().equals("null") && !myJson.optString("id").trim().matches("")) {
                        gmailLoginOutput.setId(myJson.optString("id"));
                    }
                    else {
                        gmailLoginOutput.setId("");

                    }
                    if ((myJson.has("isSubscribed")) && myJson.optString("isSubscribed").trim() != null && !myJson.optString("isSubscribed").trim().isEmpty() && !myJson.optString("isSubscribed").trim().equals("null") && !myJson.optString("isSubscribed").trim().matches("")) {
                        gmailLoginOutput.setIsSubscribed(myJson.optInt("isSubscribed"));
                    }

                    if ((myJson.has("login_history_id")) && myJson.optString("login_history_id").trim() != null && !myJson.optString("login_history_id").trim().isEmpty() && !myJson.optString("login_history_id").trim().equals("null") && !myJson.optString("login_history_id").trim().matches("")) {
                        gmailLoginOutput.setLogin_history_id(myJson.optString("login_history_id"));
                    }
                    else {
                        gmailLoginOutput.setLogin_history_id("");

                    }
                    if ((myJson.has("email")) && myJson.optString("email").trim() != null && !myJson.optString("email").trim().isEmpty() && !myJson.optString("email").trim().equals("null") && !myJson.optString("email").trim().matches("")) {
                        gmailLoginOutput.setEmail(myJson.optString("email"));
                    } else {
                        gmailLoginOutput.setEmail("");

                    }
                    if ((myJson.has("display_name")) && myJson.optString("display_name").trim() != null && !myJson.optString("display_name").trim().isEmpty() && !myJson.optString("display_name").trim().equals("null") && !myJson.optString("display_name").trim().matches("")) {
                        String hh = myJson.optString("display_name");
                        gmailLoginOutput.setDisplay_name(myJson.optString("display_name"));


                    } else {
                        gmailLoginOutput.setDisplay_name("");

                    }
                    if ((myJson.has("profile_image")) && myJson.optString("profile_image").trim() != null && !myJson.optString("profile_image").trim().isEmpty() && !myJson.optString("profile_image").trim().equals("null") && !myJson.optString("profile_image").trim().matches("")) {
                        gmailLoginOutput.setProfile_image(myJson.optString("profile_image"));


                    } else {
                        gmailLoginOutput.setProfile_image("");

                    }

                    }else {

                    responseStr = "";
                    status = 0;
                    message = "Error";
                }
               /* loggedInIdStr = myJson.optString("id");
                isSubscribedStr = myJson.optString("isSubscribed");
                if (myJson.has("login_history_id")) {
                    loginHistoryIdStr = myJson.optString("login_history_id");
                }
*/

            }

        }
        catch (Exception e) {
            responseStr = "";
            status = 0;
            message = "Error";

        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGmailRegPreExecuteStarted();

        status = 0;
        if (!PACKAGE_NAME.equals(SDKInitializer.getUser_Package_Name_At_Api(context))) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGmailRegPostExecuteCompleted(gmailLoginOutput, status, message);
            return;
        }
        if (SDKInitializer.getHashKey(context).equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGmailRegPostExecuteCompleted(gmailLoginOutput, status, message);
        }

    }


    @Override
    protected void onPostExecute(Void result) {
        listener.onGmailRegPostExecuteCompleted(gmailLoginOutput, status, message);

        Intent Sintent = new Intent("LOGIN_SUCCESS");

        LocalBroadcastManager.getInstance((context)).sendBroadcast(Sintent);

    }
}
