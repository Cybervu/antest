package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.Login_input;
import com.home.apisdk.apiModel.Login_output;

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
 * Created by Muvi on 12/16/2016.
 */
public class LoginAsynTask extends AsyncTask<Login_input, Void, Void> {
     Login_input login_input;

    String responseStr;
    int status;
    String message,PACKAGE_NAME;
    public interface LoinDetails{
        void onLoginPreExecuteStarted();
        void onLoginPostExecuteCompleted(Login_output login_output, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private LoinDetails listener;
    Login_output login_output=new Login_output();

    public LoginAsynTask(Login_input login_input, Context context) {
        this.listener = (LoinDetails)context;
        this.login_input = login_input;
        Log.v("SUBHA", "LoginAsynTask");
        PACKAGE_NAME=context.getPackageName();
        Log.v("SUBHA", "pkgnm :"+PACKAGE_NAME);

    }
    @Override
    protected Void doInBackground(Login_input... params) {


        try {
            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getLoginUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader("authToken", this.login_input.getAuthToken());
            httppost.addHeader("email", this.login_input.getEmail());
            httppost.addHeader("password",this.login_input.getPassword());


            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());


            } catch (org.apache.http.conn.ConnectTimeoutException e){

                status = 0;
                message = "Error";



            }catch (IOException e) {
                status = 0;
                message = "Error";
            }

            JSONObject mainJson =null;
            if(responseStr!=null) {
                mainJson = new JSONObject(responseStr);
                status = Integer.parseInt(mainJson.optString("code"));


                if ((mainJson.has("email")) && mainJson.getString("email").trim() != null && !mainJson.getString("email").trim().isEmpty() && !mainJson.getString("email").trim().equals("null") && !mainJson.getString("email").trim().matches("")) {
                    login_output.setEmail(mainJson.getString("email"));
                } else {
                    login_output.setEmail("");

                }
                if ((mainJson.has("display_name")) && mainJson.getString("display_name").trim() != null && !mainJson.getString("display_name").trim().isEmpty() && !mainJson.getString("display_name").trim().equals("null") && !mainJson.getString("display_name").trim().matches("")) {
                    String hh=mainJson.getString("display_name");
                    login_output.setDisplay_name(mainJson.getString("display_name"));


                } else {
                    login_output.setDisplay_name("");

                }
                if ((mainJson.has("profile_image")) && mainJson.getString("profile_image").trim() != null && !mainJson.getString("profile_image").trim().isEmpty() && !mainJson.getString("profile_image").trim().equals("null") && !mainJson.getString("profile_image").trim().matches("")) {
                    login_output.setProfile_image(mainJson.getString("profile_image"));


                } else {
                    login_output.setProfile_image("");

                }
                if ((mainJson.has("isSubscribed")) && mainJson.getString("isSubscribed").trim() != null && !mainJson.getString("isSubscribed").trim().isEmpty() && !mainJson.getString("isSubscribed").trim().equals("null") && !mainJson.getString("isSubscribed").trim().matches("")) {
                    login_output.setIsSubscribed(mainJson.getString("story"));
                } else {
                    login_output.setIsSubscribed("");

                }
                if ((mainJson.has("nick_name")) && mainJson.getString("nick_name").trim() != null && !mainJson.getString("nick_name").trim().isEmpty() && !mainJson.getString("nick_name").trim().equals("null") && !mainJson.getString("nick_name").trim().matches("")) {
                    login_output.setNick_name(mainJson.getString("nick_name"));
                } else {
                    login_output.setNick_name("");

                }

                if ((mainJson.has("studio_id")) && mainJson.getString("studio_id").trim() != null && !mainJson.getString("studio_id").trim().isEmpty() && !mainJson.getString("studio_id").trim().equals("null") && !mainJson.getString("studio_id").trim().matches("")) {
                    login_output.setStudio_id(mainJson.getString("studio_id"));

                } else {
                    login_output.setStudio_id("");

                }

                if ((mainJson.has("msg")) && mainJson.getString("msg").trim() != null && !mainJson.getString("msg").trim().isEmpty() && !mainJson.getString("msg").trim().equals("null") && !mainJson.getString("msg").trim().matches("")) {
                    login_output.setMsg(mainJson.getString("msg"));
                } else {
                    login_output.setMsg("");

                }
                if ((mainJson.has("login_history_id")) && mainJson.getString("login_history_id").trim() != null && !mainJson.getString("login_history_id").trim().isEmpty() && !mainJson.getString("login_history_id").trim().equals("null") && !mainJson.getString("login_history_id").trim().matches("")) {
                    login_output.setLogin_history_id(mainJson.getString("login_history_id"));
                } else {
                    login_output.setLogin_history_id("");

                }
                if ((mainJson.has("id")) && mainJson.getString("id").trim() != null && !mainJson.getString("id").trim().isEmpty() && !mainJson.getString("id").trim().equals("null") && !mainJson.getString("id").trim().matches("")) {
                    login_output.setId(mainJson.getString("id"));
                } else {
                    login_output.setId("");

                }

            }

            else{
                responseStr = "0";
                status = 0;
                message = "Error";
            }
        } catch (final JSONException e1) {

            responseStr = "0";
            status = 0;
            message = "Error";            }

        catch (Exception e)
        {

            responseStr = "0";
            status = 0;
            message = "Error";
        }
        return null;


    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onLoginPreExecuteStarted();

        status = 0;
        if(!PACKAGE_NAME.equals(CommonConstants.User_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onLoginPostExecuteCompleted(login_output, status, message);
            return;
        }
        if(CommonConstants.HashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onLoginPostExecuteCompleted(login_output, status, message);
        }

    }



    @Override
    protected void onPostExecute(Void result) {
        listener.onLoginPostExecuteCompleted(login_output, status, message);

    }

}
