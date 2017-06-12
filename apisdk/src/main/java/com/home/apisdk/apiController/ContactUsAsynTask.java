package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.ContactUsInputModel;
import com.home.apisdk.apiModel.ContactUsOutputModel;

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

public class ContactUsAsynTask extends AsyncTask<ContactUsInputModel,Void ,Void > {

    public ContactUsInputModel contactUsInputModel;
    String PACKAGE_NAME,message,responseStr,status;
    int code;
    ContactUsOutputModel contactUsOutputModel;

    public interface ContactUs{
        void onContactUsPreExecuteStarted();
        void onContactUsPostExecuteCompleted(ContactUsOutputModel contactUsOutputModel, int code, String message, String status);
    }

    private ContactUs listener;

    public ContactUsAsynTask(ContactUsInputModel contactUsInputModel, Context context) {
        this.listener = (ContactUs) context;

        this.contactUsInputModel = contactUsInputModel;
        PACKAGE_NAME=context.getPackageName();
        Log.v("SUBHA", "pkgnm :"+PACKAGE_NAME);
        Log.v("SUBHA","GetUserProfileAsynctask");

    }

    @Override
    protected Void doInBackground(ContactUsInputModel... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.CONTACT_US_URL);
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.contactUsInputModel.getAuthToken());
            httppost.addHeader("email", this.contactUsInputModel.getEmail());
            httppost.addHeader("name", this.contactUsInputModel.getName());
            httppost.addHeader("message", this.contactUsInputModel.getMessage());

            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("SUBHA", "RES" + responseStr);

            } catch (org.apache.http.conn.ConnectTimeoutException e) {
                code = 0;
                message = "";
                status = "";

            } catch (IOException e) {
                code = 0;
                message = "";
                status = "";
            }
            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                code = Integer.parseInt(myJson.optString("code"));
                message = myJson.optString("msg");
                message = myJson.optString("status");
            }

                if (code == 200) {

                            contactUsOutputModel = new ContactUsOutputModel();
                            contactUsOutputModel.setSuccess_msg(myJson.optString("success_msg"));
                            contactUsOutputModel.setError_msg(myJson.optString("error_msg"));

                }
        } catch (Exception e) {
            code = 0;
            message = "";
            status = "";
        }
        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onContactUsPreExecuteStarted();
        code= 0;
       /* if(!PACKAGE_NAME.equals(CommonConstants.User_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onContactUsPostExecuteCompleted(contactUsOutputModel,code,message,status);
            return;
        }
        if(CommonConstants.HashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onContactUsPostExecuteCompleted(contactUsOutputModel,code,message,status);
        }*/


    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onContactUsPostExecuteCompleted(contactUsOutputModel,code,message,status);
    }
}
