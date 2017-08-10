package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.HeaderConstants;
import com.home.apisdk.apiModel.RemoveDeviceInputModel;
import com.home.apisdk.apiModel.RemoveDeviceOutputModel;

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
public class RemoveDeviceAsynTask extends AsyncTask<RemoveDeviceInputModel, Void, Void> {

    RemoveDeviceInputModel removeDeviceInputModel;
    String responseStr;
    int status;
    String message,PACKAGE_NAME;

    public interface RemoveDeviceListner {
        void onRemoveDevicePreExecuteStarted();
        void onRemoveDevicePostExecuteCompleted(RemoveDeviceOutputModel removeDeviceOutputModel, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private RemoveDeviceListner listener;
    private Context context;
    RemoveDeviceOutputModel removeDeviceOutputModel=new RemoveDeviceOutputModel();

    public RemoveDeviceAsynTask(RemoveDeviceInputModel removeDeviceInputModel, RemoveDeviceListner listener, Context context) {
        this.listener = listener;
        this.context=context;

        this.removeDeviceInputModel = removeDeviceInputModel;
        PACKAGE_NAME=context.getPackageName();
        Log.v("MUVISDK", "pkgnm :"+PACKAGE_NAME);
        Log.v("MUVISDK","GetContentListAsynTask");


    }

    @Override
    protected Void doInBackground(RemoveDeviceInputModel... params) {

        try {
            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getRemoveDevice());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader(HeaderConstants.AUTH_TOKEN, this.removeDeviceInputModel.getAuthToken());
            httppost.addHeader(HeaderConstants.LANG_CODE, this.removeDeviceInputModel.getLang_code());
            httppost.addHeader(HeaderConstants.DEVICE_ID, this.removeDeviceInputModel.getDevice());
            httppost.addHeader(HeaderConstants.USER_ID, this.removeDeviceInputModel.getUser_id());



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

                if ((myJson.has("msg")) && myJson.optString("msg").trim() != null && !myJson.optString("msg").trim().isEmpty() && !myJson.optString("msg").trim().equals("null") && !myJson.optString("msg").trim().matches("")) {
                    removeDeviceOutputModel.setMsg(myJson.optString("msg"));
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
        listener.onRemoveDevicePreExecuteStarted();

        status = 0;
            if(!PACKAGE_NAME.equals(HeaderConstants.user_Package_Name_At_Api))
            {
                this.cancel(true);
                message = "Packge Name Not Matched";
                listener.onRemoveDevicePostExecuteCompleted(removeDeviceOutputModel,status,message);
                return;
            }
            if(HeaderConstants.hashKey.equals(""))
            {
                this.cancel(true);
                message = "Hash Key Is Not Available. Please Initialize The SDK";
                listener.onRemoveDevicePostExecuteCompleted(removeDeviceOutputModel,status,message);
            }

        listener.onRemoveDevicePostExecuteCompleted(removeDeviceOutputModel,status,message);

    }



    @Override
    protected void onPostExecute(Void result) {
        listener.onRemoveDevicePostExecuteCompleted(removeDeviceOutputModel,status,message);

    }

    //}
}
