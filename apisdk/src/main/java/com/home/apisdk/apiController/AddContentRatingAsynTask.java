package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.AddContentRatingInputModel;
import com.home.apisdk.apiModel.AddContentRatingOutputModel;

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
public class AddContentRatingAsynTask extends AsyncTask<AddContentRatingInputModel, Void, Void> {

    AddContentRatingInputModel addContentRatingInputModel;
    String responseStr;
    int status;
    String message,PACKAGE_NAME;

    public interface AddContentRatingListner {
        void onAddContentRatingPreExecuteStarted();
        void onAddContentRatingPostExecuteCompleted(AddContentRatingOutputModel addContentRatingOutputModel, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private AddContentRatingListner listener;
    private Context context;
    AddContentRatingOutputModel addContentRatingOutputModel=new AddContentRatingOutputModel();

    public AddContentRatingAsynTask(AddContentRatingInputModel addContentRatingInputModel, AddContentRatingListner listener, Context context) {
        this.listener = listener;
        this.context=context;

        this.addContentRatingInputModel = addContentRatingInputModel;
        PACKAGE_NAME=context.getPackageName();
        Log.v("MUVISDK", "pkgnm :"+PACKAGE_NAME);
        Log.v("MUVISDK","GetContentListAsynTask");


    }

    @Override
    protected Void doInBackground(AddContentRatingInputModel... params) {

        try {
            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getAddContentRating());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader(CommonConstants.AUTH_TOKEN, this.addContentRatingInputModel.getAuthToken());
            httppost.addHeader(CommonConstants.LANG_CODE, this.addContentRatingInputModel.getLang_code());
            httppost.addHeader(CommonConstants.CONTENT_ID, this.addContentRatingInputModel.getContent_id());
            httppost.addHeader(CommonConstants.USER_ID, this.addContentRatingInputModel.getUser_id());
            httppost.addHeader(CommonConstants.RATING, this.addContentRatingInputModel.getRating());
            httppost.addHeader(CommonConstants.REVIEW, this.addContentRatingInputModel.getReview());


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
                    addContentRatingOutputModel.setMsg(myJson.optString("msg"));
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
        listener.onAddContentRatingPreExecuteStarted();

        status = 0;
            if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
            {
                this.cancel(true);
                message = "Packge Name Not Matched";
                listener.onAddContentRatingPostExecuteCompleted(addContentRatingOutputModel,status,message);
                return;
            }
            if(CommonConstants.hashKey.equals(""))
            {
                this.cancel(true);
                message = "Hash Key Is Not Available. Please Initialize The SDK";
                listener.onAddContentRatingPostExecuteCompleted(addContentRatingOutputModel,status,message);
            }

        listener.onAddContentRatingPostExecuteCompleted(addContentRatingOutputModel,status,message);

    }



    @Override
    protected void onPostExecute(Void result) {
        listener.onAddContentRatingPostExecuteCompleted(addContentRatingOutputModel,status,message);

    }

    //}
}
