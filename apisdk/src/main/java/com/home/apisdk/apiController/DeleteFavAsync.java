package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;

import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.DeleteFavInputModel;
import com.home.apisdk.apiModel.DeleteFavOutputModel;

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
 * Created by MUVI on 7/21/2017.
 */

public class DeleteFavAsync extends  AsyncTask<DeleteFavInputModel,Void,Void>{
    DeleteFavInputModel deleteFavInputModel;
    String PACKAGE_NAME,  responseStr,sucessMsg;
    int status;


    public  interface DeleteFavListener{
        void onDeleteFavPreExecuteStarted();
        void onDeleteFavPostExecuteCompleted(DeleteFavOutputModel deleteFavOutputModel, int status, String sucessMsg);
    }
    DeleteFavOutputModel deleteFavOutputModel=new DeleteFavOutputModel();
    DeleteFavListener listener;
    private Context context;
    public DeleteFavAsync(DeleteFavInputModel deleteFavInputModel,DeleteFavListener listener,Context context){
        this.deleteFavInputModel=deleteFavInputModel;
        this.listener=listener;
        this.context=context;
        PACKAGE_NAME = context.getPackageName();
    }


    @Override
    protected Void doInBackground(DeleteFavInputModel... params) {
           // String urlRouteList = Util.rootUrl().trim() + Util.DeleteFavList.trim();

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(APIUrlConstant.getDeleteFavList());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader(CommonConstants.AUTH_TOKEN, this.deleteFavInputModel.getAuthTokenStr().trim());
                httppost.addHeader(CommonConstants.MOVIE_UNIQ_ID, this.deleteFavInputModel.getMovieUniqueId());
                httppost.addHeader(CommonConstants.CONTENT_TYPE, this.deleteFavInputModel.getIsEpisode());
                httppost.addHeader(CommonConstants.USER_ID, this.deleteFavInputModel.getLoggedInStr());

                try {
                    HttpResponse response = httpclient.execute(httppost);
                    responseStr = EntityUtils.toString(response.getEntity());


                } catch (org.apache.http.conn.ConnectTimeoutException e) {

                }
            } catch (IOException e) {

                e.printStackTrace();
            }
            if (responseStr != null) {
                JSONObject myJson = null;
                try {
                    myJson = new JSONObject(responseStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                status = Integer.parseInt(myJson.optString("code"));
                sucessMsg = myJson.optString("msg");
//                statusmsg = myJson.optString("status");
                Log.v("BISHAL","response delete=="+myJson);


            }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onDeleteFavPostExecuteCompleted(deleteFavOutputModel,status,sucessMsg);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onDeleteFavPreExecuteStarted();
    }
}
