package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.HeaderConstants;
import com.home.apisdk.apiModel.AddToFavInputModel;
import com.home.apisdk.apiModel.AddToFavOutputModel;

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
 * Created by MUVI on 7/20/2017.
 */

public class AddToFavAsync extends AsyncTask<AddToFavInputModel,Void,Void> {
    public  AddToFavInputModel addToFavInputModel;
    String PACKAGE_NAME,  responseStr,sucessMsg;
    int status;

    public interface AddToFavListener {
        void onAddToFavPreExecuteStarted();

        void onAddToFavPostExecuteCompleted(AddToFavOutputModel addToFavOutputModel, int status, String sucessMsg);
    }
    AddToFavOutputModel AddToFavOutputModel = new AddToFavOutputModel();

    private AddToFavListener listener;
    private Context context;

    public AddToFavAsync (AddToFavInputModel addToFavInputModel, AddToFavListener listener, Context context){
        this.listener = listener;
        this.context = context;
        this.addToFavInputModel=addToFavInputModel;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "GetUserProfileAsynctask");
    }

    @Override
    protected Void doInBackground(AddToFavInputModel... params) {
      //  String urlRouteList = Util.rootUrl().trim() + Util.AddtoFavlist.trim();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getAddtoFavlist());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader(HeaderConstants.AUTH_TOKEN, this.addToFavInputModel.getAuthToken().trim());
            httppost.addHeader(HeaderConstants.MOVIE_UNIQ_ID,this.addToFavInputModel.getMovie_uniq_id() );
            httppost.addHeader(HeaderConstants.CONTENT_TYPE, this.addToFavInputModel.getIsEpisodeStr());
            httppost.addHeader(HeaderConstants.USER_ID, this.addToFavInputModel.getLoggedInStr());

            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());



            } catch (org.apache.http.conn.ConnectTimeoutException e) {
            }
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
            status = Integer.parseInt(myJson.optString("code"));
            sucessMsg = myJson.optString("msg");
//                statusmsg = myJson.optString("status");

        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onAddToFavPreExecuteStarted();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onAddToFavPostExecuteCompleted(AddToFavOutputModel,status,sucessMsg);
    }
}
