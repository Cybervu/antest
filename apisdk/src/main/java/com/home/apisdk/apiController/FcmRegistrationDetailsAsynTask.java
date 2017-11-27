/**
 * SDK initialization, platform and device information classes.
 */


package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.FcmRegistrationDetailsInputModel;
import com.home.apisdk.apiModel.FcmRegistrationDetailsOutputModel;

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
 * This Class is use to delete the movies/series from the "Add Favorite" list section.
 *
 * @author MUVI
 */

public class FcmRegistrationDetailsAsynTask extends AsyncTask<FcmRegistrationDetailsInputModel, Void, Void> {

    private FcmRegistrationDetailsInputModel fcmRegistrationDetailsInputModel;
    private FcmRegistrationDetailsListener listener;
    private Context context;
    private String PACKAGE_NAME;
    private String responseStr;
    private String responseMsg;

    /**
     * Interface used to allow the caller of a DeleteFavAsync to run some code when get
     * responses.
     */


    public interface FcmRegistrationDetailsListener {

        /**
         * This method will be invoked before controller start execution.
         * This method to handle pre-execution work.
         */

        void onFcmRegistrationDetailsPreExecuteStarted();

        /**
         * This method will be invoked after controller complete execution.
         * This method to handle post-execution work.
         *
         * @param fcmRegistrationDetailsOutputModel A Model Class which contain responses. To get that responses we need to call the respective getter methods.
         * @param sucessMsg            On Success Message
         */

        void onFcmRegistrationDetailsPostExecuteCompleted(FcmRegistrationDetailsOutputModel fcmRegistrationDetailsOutputModel, String sucessMsg);
    }

    FcmRegistrationDetailsOutputModel fcmRegistrationDetailsOutputModel = new FcmRegistrationDetailsOutputModel();

    /**
     * Constructor to initialise the private data members.
     *
     * @param fcmRegistrationDetailsInputModel A Model Class which is use for background task, we need to set all the attributes through setter methods of input model class,
     *                            For Example: to use this API we have to set following attributes:
     *                            setAuthToken(),setMovieUniqueId() etc.
     * @param listener            DeleteFav Listener
     * @param context             android.content.Context
     */

    public FcmRegistrationDetailsAsynTask(FcmRegistrationDetailsInputModel fcmRegistrationDetailsInputModel, FcmRegistrationDetailsListener listener, Context context) {
        this.fcmRegistrationDetailsInputModel = fcmRegistrationDetailsInputModel;
        this.listener = listener;
        this.context = context;
        PACKAGE_NAME = context.getPackageName();
    }

    /**
     * Background thread to execute.
     *
     * @return null
     * @throws org.apache.http.conn.ConnectTimeoutException,IOException,JSONException
     */


    @Override
    protected Void doInBackground(FcmRegistrationDetailsInputModel... params) {
        // String urlRouteList = Util.rootUrl().trim() + Util.DeleteFavList.trim();

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getFcmregistration());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader(HeaderConstants.AUTH_TOKEN, this.fcmRegistrationDetailsInputModel.getAuthToken().trim());
            httppost.addHeader(HeaderConstants.DEVICE_ID, this.fcmRegistrationDetailsInputModel.getDevice_id().trim());
            httppost.addHeader(HeaderConstants.FCM_TOKEN, this.fcmRegistrationDetailsInputModel.getFcm_token().trim());
            httppost.addHeader(HeaderConstants.DEVICE_TYPE, "1");
            Log.v("ANU","fcmRegistrationDetailsInputModel.getAuthToken().trim()===="+fcmRegistrationDetailsInputModel.getAuthToken().trim());
            Log.v("ANU","fcmRegistrationDetailsInputModel.getDevice_id().trim()===="+fcmRegistrationDetailsInputModel.getDevice_id().trim());
            Log.v("ANU","fcmRegistrationDetailsInputModel.getFcm_token().trim()===="+fcmRegistrationDetailsInputModel.getFcm_token().trim());

            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("ANU","responseStr===="+responseStr);


            } catch (org.apache.http.conn.ConnectTimeoutException e) {

            }
        } catch (IOException e) {

            e.printStackTrace();
        }

        if(responseStr!=null){

            JSONObject myJson = null;

            try {
                myJson = new JSONObject(responseStr);
                responseMsg=myJson.getString("status");
                fcmRegistrationDetailsOutputModel.setResponseMsg(responseMsg);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        else {
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onFcmRegistrationDetailsPostExecuteCompleted(fcmRegistrationDetailsOutputModel,responseMsg);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onFcmRegistrationDetailsPreExecuteStarted();
    }
}
