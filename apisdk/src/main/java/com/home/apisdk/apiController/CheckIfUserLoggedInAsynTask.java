package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.CheckIfUserLoggedInInputModel;
import com.home.apisdk.apiModel.CheckIfUserLoggedInOutputModel;
import com.home.apisdk.apiModel.IsRegistrationEnabledInputModel;

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
public class CheckIfUserLoggedInAsynTask extends AsyncTask<CheckIfUserLoggedInInputModel, Void, Void> {

    CheckIfUserLoggedInInputModel checkIfUserLoggedInInputModel;
    String responseStr;
    int status;
    String message,PACKAGE_NAME;

    public interface CheckIfUserLogggedInListener {
        void onCheckIfUserLogggedInPreExecuteStarted();
        void onCheckIfUserLogggedInPostExecuteCompleted(CheckIfUserLoggedInOutputModel checkIfUserLoggedInOutputModel, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private CheckIfUserLogggedInListener listener;
    private Context context;
    CheckIfUserLoggedInOutputModel checkIfUserLoggedInOutputModel=new CheckIfUserLoggedInOutputModel();

    public CheckIfUserLoggedInAsynTask(CheckIfUserLoggedInInputModel checkIfUserLoggedInInputModel, CheckIfUserLogggedInListener listener, Context context) {
        this.listener = listener;
        this.context=context;

        this.checkIfUserLoggedInInputModel = checkIfUserLoggedInInputModel;
        PACKAGE_NAME=context.getPackageName();
        Log.v("MUVISDK", "pkgnm :"+PACKAGE_NAME);
        Log.v("MUVISDK","GetContentListAsynTask");


    }

    @Override
    protected Void doInBackground(CheckIfUserLoggedInInputModel... params) {

        try {
            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getCheckIfUserLoggedIn());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader(CommonConstants.AUTH_TOKEN, this.checkIfUserLoggedInInputModel.getAuthToken());
            httppost.addHeader(CommonConstants.USER_ID, this.checkIfUserLoggedInInputModel.getUser_id());
            httppost.addHeader(CommonConstants.DEVICE_ID, this.checkIfUserLoggedInInputModel.getDevice_id());
            httppost.addHeader(CommonConstants.DEVICE_TYPE, this.checkIfUserLoggedInInputModel.getDevice_type());


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

                if ((myJson.has("is_login")) && myJson.optString("is_login").trim() != null && !myJson.optString("is_login").trim().isEmpty() && !myJson.optString("is_login").trim().equals("null") && !myJson.optString("is_login").trim().matches("")) {
                    checkIfUserLoggedInOutputModel.setIs_login(Integer.parseInt(myJson.optString("is_login")));
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
        listener.onCheckIfUserLogggedInPreExecuteStarted();

        status = 0;
            if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
            {
                this.cancel(true);
                message = "Packge Name Not Matched";
                listener.onCheckIfUserLogggedInPostExecuteCompleted(checkIfUserLoggedInOutputModel,status,message);
                return;
            }
            if(CommonConstants.hashKey.equals(""))
            {
                this.cancel(true);
                message = "Hash Key Is Not Available. Please Initialize The SDK";
                listener.onCheckIfUserLogggedInPostExecuteCompleted(checkIfUserLoggedInOutputModel,status,message);
            }

        listener.onCheckIfUserLogggedInPostExecuteCompleted(checkIfUserLoggedInOutputModel,status,message);

    }



    @Override
    protected void onPostExecute(Void result) {
        listener.onCheckIfUserLogggedInPostExecuteCompleted(checkIfUserLoggedInOutputModel,status,message);

    }

    //}
}
