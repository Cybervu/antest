package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.CelibrityInputModel;
import com.home.apisdk.apiModel.CelibrityOutputModel;

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
 * Created by MUVI on 1/20/2017.
 */

public class GetCelibrityAsyntask extends AsyncTask<CelibrityInputModel,Void ,Void > {

    public CelibrityInputModel celibrityInputModel;
    String PACKAGE_NAME,message,responseStr;
    int code;

    public interface GetCelibrity{
        void onGetCelibrityPreExecuteStarted();
        void onGetCelibrityPostExecuteCompleted(ArrayList<CelibrityOutputModel> celibrityOutputModel, int status);
    }

    private GetCelibrity listener;
    ArrayList<CelibrityOutputModel> celibrityOutputModel = new ArrayList<CelibrityOutputModel>();

    public GetCelibrityAsyntask(CelibrityInputModel celibrityInputModel, Context context) {
        this.listener = (GetCelibrity) context;

        this.celibrityInputModel = celibrityInputModel;
        PACKAGE_NAME=context.getPackageName();
        Log.v("SUBHA", "pkgnm :"+PACKAGE_NAME);
        Log.v("SUBHA","getPlanListAsynctask");

    }

    @Override
    protected Void doInBackground(CelibrityInputModel... params) {


        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getGetCelibrityUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader("authToken", this.celibrityInputModel.getAuthToken());
            httppost.addHeader("movie_id", this.celibrityInputModel.getMovie_id());

            Log.v("SUBHA","authToken = "+ this.celibrityInputModel.getAuthToken());
            Log.v("SUBHA","movie id = "+ this.celibrityInputModel.getMovie_id());
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("SUBHA", "RES" + responseStr);

            } catch (org.apache.http.conn.ConnectTimeoutException e) {
                code = 0;
                message = "";


            } catch (IOException e) {
                code = 0;
                message = "";

            }
            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                code = Integer.parseInt(myJson.optString("code"));
                message = myJson.optString("status");

            }

                if (code == 200) {

                    JSONArray jsonMainNode = myJson.getJSONArray("celibrity");

                    int lengthJsonArr = jsonMainNode.length();
                    for (int i = 0; i < lengthJsonArr; i++) {
                        JSONObject jsonChildNode;
                        try {
                            jsonChildNode = jsonMainNode.getJSONObject(i);
                            CelibrityOutputModel content = new CelibrityOutputModel();

                            if ((jsonChildNode.has("name")) && jsonChildNode.getString("name").trim() != null && !jsonChildNode.getString("name").trim().isEmpty() && !jsonChildNode.getString("name").trim().equals("null") && !jsonChildNode.getString("name").trim().matches("")) {
                                content.setName(jsonChildNode.getString("name"));
                            }
                            if ((jsonChildNode.has("cast_type")) && jsonChildNode.getString("cast_type").trim() != null && !jsonChildNode.getString("cast_type").trim().isEmpty() && !jsonChildNode.getString("cast_type").trim().equals("null") && !jsonChildNode.getString("cast_type").trim().matches("")) {
                                content.setCast_type(jsonChildNode.getString("cast_type"));

                            }
                            if ((jsonChildNode.has("celebrity_image")) && jsonChildNode.getString("celebrity_image").trim() != null && !jsonChildNode.getString("celebrity_image").trim().isEmpty() && !jsonChildNode.getString("celebrity_image").trim().equals("null") && !jsonChildNode.getString("celebrity_image").trim().matches("")) {
                                content.setCelebrity_image(jsonChildNode.getString("celebrity_image"));
                            }


                            celibrityOutputModel.add(content);
                        } catch (Exception e) {
                            code = 0;
                            message = "";
                        }
                    }
                }
        } catch (Exception e) {
            code = 0;
            message = "";

        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetCelibrityPreExecuteStarted();
        code= 0;
     /*   if(!PACKAGE_NAME.equals(CommonConstants.User_Package_Name_At_Api))
        {
            this.cancel(true);
            listener.onGetPlanListPostExecuteCompleted(planListOutput,code);
            return;
        }
        if(CommonConstants.HashKey.equals(""))
        {
            this.cancel(true);
            listener.onGetPlanListPostExecuteCompleted(planListOutput,code);
        }*/

    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onGetCelibrityPostExecuteCompleted(celibrityOutputModel,code);
    }
}
