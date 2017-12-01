/**
 * SDK initialization, platform and device information classes.
 */

package com.home.apisdk.apiController;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.AddToFavInputModel;
import com.home.apisdk.apiModel.AddToFavOutputModel;
import com.home.apisdk.apiModel.GetStudioAuthkeyInputModel;
import com.home.apisdk.apiModel.GetStudioAuthkeyOutputModel;

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
 * This Class helps user to add their favorite series/Movie to their favorite list section. Users
 * can watch their favorite series/movies without wasting much time in searching them.
 *
 * @author MUVI
 */

public class GetStudioAuthkeyAsynTask extends AsyncTask<GetStudioAuthkeyInputModel, Void, Void> {
    private GetStudioAuthkeyInputModel getStudioAuthkeyInputModel;
    private String PACKAGE_NAME;
    private String responseStr;
    private GetStudioAuthkeyListener listener;
    private Context context;
    private String message="";
    private String authToken;
    private String statusMsg;

    /**
     * Interface used to allow the caller of a AddToFavAsync to run some code when get
     * responses.
     */

    public interface GetStudioAuthkeyListener {

        /**
         * This method will be invoked before controller start execution.
         * This method to handle pre-execution work.
         */

        void onGetStudioAuthkeyPreExecuteStarted();

        /**
         * This method will be invoked after controller complete execution.
         * This method to handle post-execution work.
         *
         * @param getStudioAuthkeyOutputModel A Model Class which contain responses. To get that responses we need to call the respective getter methods.
         * @param statusMsg              Response Code from the server
         * @param responseStr           On Success Message
         * @param authToken             studio auth token
         * @param message               message
         */

        void onGetStudioAuthkeyPostExecuteCompleted(GetStudioAuthkeyOutputModel getStudioAuthkeyOutputModel, String statusMsg, String responseStr, String authToken,String message);
    }

    GetStudioAuthkeyOutputModel getStudioAuthkeyOutputModel = new GetStudioAuthkeyOutputModel();

    /**
     * Constructor to initialise the private data members.
     *
     * @param getStudioAuthkeyInputModel A Model Class which is use for background task, we need to set all the attributes through setter methods of input model class,
     *                           For Example: to use this API we have to set following attributes:
     *                           setAuthToken(),setMovie_uniq_id() etc.
     * @param listener           AddToFavorite Listener
     * @param context            android.content.Context
     */

    public GetStudioAuthkeyAsynTask(GetStudioAuthkeyInputModel getStudioAuthkeyInputModel, GetStudioAuthkeyListener listener, Context context) {
        this.listener = listener;
        this.context = context;
        this.getStudioAuthkeyInputModel = getStudioAuthkeyInputModel;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "GetUserProfileAsynctask");
    }

    /**
     * Background thread to execute.
     *
     * @return Null
     * @throws org.apache.http.conn.ConnectTimeoutException,IOException,JSONException
     */

    @Override
    protected Void doInBackground(GetStudioAuthkeyInputModel... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getStudioAuthToken());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader(HeaderConstants.EMAIL, this.getStudioAuthkeyInputModel.getEmail().trim());
            httppost.addHeader(HeaderConstants.PASSWORD, this.getStudioAuthkeyInputModel.getPassword());

            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());


            } catch (org.apache.http.conn.ConnectTimeoutException e) {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        if(responseStr!=null){

            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(responseStr);
                message=jsonObject.getString("msg");
                if (jsonObject.getString("status").equals("OK")){


                    statusMsg = jsonObject.getString("status");
                    authToken=jsonObject.getString("authToken");


                   /* preferenceManager.setAuthToken(key);

//                        AsynGetPlanId asynGetPlanId = new AsynGetPlanId();
//                        asynGetPlanId.executeOnExecutor(threadPoolExecutor);


                    if (NetworkStatus.getInstance().isConnected(Login.this)) {
                        SDKInitializer.getInstance().init(Login.this, Login.this, authTokenStr);
                    } else {
                        noInternetLayout.setVisibility(View.VISIBLE);
                        geoBlockedLayout.setVisibility(View.GONE);
                    }*/

                      /*  Log.v("ANU","GetIpAddressAsynTask called");
                        GetIpAddressAsynTask asynGetIpAddress = new GetIpAddressAsynTask(Login.this, Login.this);
                        asynGetIpAddress.executeOnExecutor(threadPoolExecutor);*/

                }else if (jsonObject.getString("status").equals("Failure")){

                    statusMsg = jsonObject.getString("status");
                    /*toastMsg(response);


                    Intent i=new Intent(Login.this,Login.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(i);
                    finish();
                    overridePendingTransition(0,0);*/

                }



            } catch (JSONException e) {
                e.printStackTrace();
            }





        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetStudioAuthkeyPreExecuteStarted();
        statusMsg = "";
        /*if (!PACKAGE_NAME.equals(SDKInitializer.getUser_Package_Name_At_Api(context))) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetStudioAuthkeyPostExecuteCompleted(getStudioAuthkeyOutputModel, statusMsg, responseStr, authToken, message);
            return;
        }
        if (SDKInitializer.getHashKey(context).equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetStudioAuthkeyPostExecuteCompleted(getStudioAuthkeyOutputModel, statusMsg, responseStr, authToken, message);
        }*/
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onGetStudioAuthkeyPostExecuteCompleted(getStudioAuthkeyOutputModel, statusMsg, responseStr, authToken, message);
    }
}
