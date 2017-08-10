package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.HeaderConstants;
import com.home.apisdk.apiModel.ViewContentRatingInputModel;
import com.home.apisdk.apiModel.ViewContentRatingOutputModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by User on 12-12-2016.
 */
public class ViewContentRatingAsynTask extends AsyncTask<ViewContentRatingInputModel, Void, Void> {

    ViewContentRatingInputModel viewContentRatingInputModel;
    String responseStr,movieUniqueId;
    int status;
    String message,permalink,PACKAGE_NAME;


    public interface ViewContentRatingListner {
        void onViewContentRatingPreExecuteStarted();
        void onViewContentRatingPostExecuteCompleted(ViewContentRatingOutputModel viewContentRatingOutputModel,int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private ViewContentRatingListner listener;
    private Context context;
    ViewContentRatingOutputModel viewContentRatingOutputModel;
    ViewContentRatingOutputModel.Rating rating ;
    ArrayList<ViewContentRatingOutputModel.Rating> ratingArrayList;

    public ViewContentRatingAsynTask(ViewContentRatingInputModel viewContentRatingInputModel, ViewContentRatingListner listener, Context context) {
        this.listener=listener;
        this.context=context;
        this.viewContentRatingInputModel = viewContentRatingInputModel;
        PACKAGE_NAME=context.getPackageName();
        Log.v("MUVISDK", "pkgnm :"+PACKAGE_NAME);
        Log.v("MUVISDK", "GetContentListAsynTask");

    }

    @Override
    protected Void doInBackground(ViewContentRatingInputModel... params) {


        try {
            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getViewContentRating());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader(HeaderConstants.AUTH_TOKEN, this.viewContentRatingInputModel.getAuthToken());
            httppost.addHeader(HeaderConstants.USER_ID, this.viewContentRatingInputModel.getUser_id());
            httppost.addHeader(HeaderConstants.CONTENT_ID,this.viewContentRatingInputModel.getContent_id());
            httppost.addHeader(HeaderConstants.LANG_CODE,this.viewContentRatingInputModel.getLang_code());


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

            if (status > 0) {

                if (status == 200) {
                    viewContentRatingOutputModel = new ViewContentRatingOutputModel();

                    if ((myJson.has("showrating")) && myJson.optString("showrating").trim() != null && !myJson.optString("showrating").trim().isEmpty() && !myJson.optString("showrating").trim().equals("null") && !myJson.optString("showrating").trim().matches("")) {
                        viewContentRatingOutputModel.setShowrating(Integer.parseInt(myJson.optString("showrating")));

                    }
                    JSONArray jsonMainNode = myJson.getJSONArray("rating");
                    int lengthJsonArr = jsonMainNode.length();
                    ratingArrayList = new ArrayList<>();
                    for (int i = 0; i < lengthJsonArr; i++) {
                        JSONObject jsonChildNode;
                        try {
                            jsonChildNode = jsonMainNode.getJSONObject(i);
                           rating = new ViewContentRatingOutputModel().new Rating();

                            if ((jsonChildNode.has("display_name")) && jsonChildNode.optString("display_name").trim() != null && !jsonChildNode.optString("display_name").trim().isEmpty() && !jsonChildNode.optString("display_name").trim().equals("null") && !jsonChildNode.optString("display_name").trim().matches("")) {
                                String display_name=jsonChildNode.optString("display_name");
                                rating.setDisplay_name(display_name);

                            }
                            if ((jsonChildNode.has("poster_url")) && jsonChildNode.optString("poster_url").trim() != null && !jsonChildNode.optString("poster_url").trim().isEmpty() && !jsonChildNode.optString("poster_url").trim().equals("null") && !jsonChildNode.optString("poster_url").trim().matches("")) {
                                rating.setRating(jsonChildNode.optString("poster_url"));

                            }
                            if ((jsonChildNode.has("review")) && jsonChildNode.optString("review").trim() != null && !jsonChildNode.optString("review").trim().isEmpty() && !jsonChildNode.optString("review").trim().equals("null") && !jsonChildNode.optString("review").trim().matches("")) {
                                rating.setReview(jsonChildNode.optString("review"));
                            }
                            if ((jsonChildNode.has("status")) && jsonChildNode.optString("status").trim() != null && !jsonChildNode.optString("status").trim().isEmpty() && !jsonChildNode.optString("status").trim().equals("null") && !jsonChildNode.optString("status").trim().matches("")) {
                                rating.setStatus(jsonChildNode.optString("status"));

                            }

                            ratingArrayList.add(rating);
                        } catch (Exception e) {
                            status = 0;
                            // totalItems = 0;
                            message = "";
                        }
                    }

                    viewContentRatingOutputModel.setRatingValue(ratingArrayList);
                } else {
                    responseStr = "0";
                    status = 0;
                    // totalItems = 0;
                    message = "";
                }
            }
        } catch (Exception e) {
            status = 0;
            // totalItems = 0;
            message = "";
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onViewContentRatingPreExecuteStarted();
        status = 0;
        if(!PACKAGE_NAME.equals(HeaderConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onViewContentRatingPostExecuteCompleted(viewContentRatingOutputModel, status, message);
            return;
        }
        if(HeaderConstants.hashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onViewContentRatingPostExecuteCompleted(viewContentRatingOutputModel, status,message);
        }

    }



    @Override
    protected void onPostExecute(Void result) {
        listener.onViewContentRatingPostExecuteCompleted(viewContentRatingOutputModel, status,message);

    }

}
