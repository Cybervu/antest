package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.IsRegistrationEnabledInputModel;
import com.home.apisdk.apiModel.IsRegistrationEnabledOutputModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by User on 12-12-2016.
 */
public class IsRegistrationEnabledAsynTask extends AsyncTask<IsRegistrationEnabledInputModel, Void, Void> {

    IsRegistrationEnabledInputModel isRegistrationEnabledInputModel;
    String responseStr;
    int status;
    String message,PACKAGE_NAME;

    public interface IsRegistrationenabled {
        void onIsRegistrationenabledPreExecuteStarted();
        void onIsRegistrationenabledPostExecuteCompleted(IsRegistrationEnabledOutputModel isRegistrationEnabledOutputModel, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

        private IsRegistrationenabled listener;
     IsRegistrationEnabledOutputModel isRegistrationEnabledOutputModel=new IsRegistrationEnabledOutputModel();

        public IsRegistrationEnabledAsynTask(IsRegistrationEnabledInputModel isRegistrationEnabledInputModel, Context context) {
            this.listener = (IsRegistrationenabled)context;
            this.isRegistrationEnabledInputModel = isRegistrationEnabledInputModel;
            PACKAGE_NAME=context.getPackageName();
            Log.v("SUBHA", "pkgnm :"+PACKAGE_NAME);
            Log.v("SUBHA","GetContentListAsynTask");


        }

        @Override
        protected Void doInBackground(IsRegistrationEnabledInputModel... params) {

            try {
                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(APIUrlConstant.IS_REGISTRATIONENABLED_URL);
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader("authToken", this.isRegistrationEnabledInputModel.getAuthToken());


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

                JSONObject myJson =null;
                if(responseStr!=null){
                    myJson = new JSONObject(responseStr);
                    status = Integer.parseInt(myJson.optString("code"));
                    message = myJson.optString("status");
                }



                    if (status == 200) {

                        if ((myJson.has("is_login")) && myJson.getString("is_login").trim() != null && !myJson.getString("is_login").trim().isEmpty() && !myJson.getString("is_login").trim().equals("null") && !myJson.getString("is_login").trim().matches("")) {
                            isRegistrationEnabledOutputModel.setIs_login(Integer.parseInt(myJson.getString("is_login")));
                        }
                        if ((myJson.has("isMylibrary")) && myJson.getString("isMylibrary").trim() != null && !myJson.getString("isMylibrary").trim().isEmpty() && !myJson.getString("isMylibrary").trim().equals("null") && !myJson.getString("isMylibrary").trim().matches("")) {
                            isRegistrationEnabledOutputModel.setIsMylibrary(Integer.parseInt(myJson.getString("isMylibrary")));
                        }
                        if ((myJson.has("signup_step")) && myJson.getString("signup_step").trim() != null && !myJson.getString("signup_step").trim().isEmpty() && !myJson.getString("signup_step").trim().equals("null") && !myJson.getString("signup_step").trim().matches("")) {
                            isRegistrationEnabledOutputModel.setSignup_step(Integer.parseInt(myJson.getString("signup_step")));
                        }
                        if ((myJson.has("has_favourite")) && myJson.getString("has_favourite").trim() != null && !myJson.getString("has_favourite").trim().isEmpty() && !myJson.getString("has_favourite").trim().equals("null") && !myJson.getString("has_favourite").trim().matches("")) {
                            isRegistrationEnabledOutputModel.setSignup_step(Integer.parseInt(myJson.getString("has_favourite")));
                        }


                }

                else{

                    responseStr = "0";
                    status = 0;
                    message = "Error";
                }
            }
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
            listener.onIsRegistrationenabledPreExecuteStarted();

            status = 0;
            /*if(!PACKAGE_NAME.equals(CommonConstants.User_Package_Name_At_Api))
            {
                this.cancel(true);
                message = "Packge Name Not Matched";
                listener.onIsRegistrationenabledPostExecuteCompleted(isRegistrationEnabledOutputModel,status,message);
                return;
            }
            if(CommonConstants.HashKey.equals(""))
            {
                this.cancel(true);
                message = "Hash Key Is Not Available. Please Initialize The SDK";
                listener.onIsRegistrationenabledPostExecuteCompleted(isRegistrationEnabledOutputModel,status,message);
            }*/

            listener.onIsRegistrationenabledPostExecuteCompleted(isRegistrationEnabledOutputModel,status,message);

        }



        @Override
        protected void onPostExecute(Void result) {
            listener.onIsRegistrationenabledPostExecuteCompleted(isRegistrationEnabledOutputModel,status,message);

        }

    //}
}
