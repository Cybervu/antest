package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.HeaderConstants;
import com.home.apisdk.apiModel.Episode_Details_input;
import com.home.apisdk.apiModel.Episode_Details_output;

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
 * Created by Muvi on 12/14/2016.
 */
public class GetEpisodeDeatailsAsynTask extends AsyncTask<Episode_Details_input, Void, Void> {
    Episode_Details_input episode_details_input;
    String responseStr,movieUniqueId;
    int status,is_ppv,item_count;
    String message,permalink,PACKAGE_NAME;


    public interface GetEpisodeDetails {
        void onGetEpisodeDetailsPreExecuteStarted();
        void onGetEpisodeDetailsPostExecuteCompleted(ArrayList<Episode_Details_output> episode_details_output, int i, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private GetEpisodeDetails listener;
    private Context context;
    ArrayList<Episode_Details_output> episode_details_output=new ArrayList<Episode_Details_output>();

    public GetEpisodeDeatailsAsynTask(Episode_Details_input episode_details_input,GetEpisodeDetails listener, Context context) {
        this.listener=listener;
        this.context=context;
        this.episode_details_input = episode_details_input;
        PACKAGE_NAME=context.getPackageName();
        Log.v("MUVISDK", "pkgnm :"+PACKAGE_NAME);
        Log.v("MUVISDK", "GetContentListAsynTask");

    }

    @Override
    protected Void doInBackground(Episode_Details_input... params) {


        try {
            HttpClient httpclient=new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getGetEpisodeDetailsUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader(HeaderConstants.AUTH_TOKEN, this.episode_details_input.getAuthtoken());
            httppost.addHeader(HeaderConstants.PERMALINK, this.episode_details_input.getPermalink());
            httppost.addHeader(HeaderConstants.LIMIT,this.episode_details_input.getLimit());
            httppost.addHeader(HeaderConstants.OFFSET,this.episode_details_input.getOffset());
            httppost.addHeader(HeaderConstants.COUNTRY,this.episode_details_input.getCountry());
            httppost.addHeader(HeaderConstants.SERIES_NUMBER,this.episode_details_input.getSeries_number());
            httppost.addHeader(HeaderConstants.LANG_CODE,this.episode_details_input.getLang_code());

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
                message = myJson.optString("msg");
                is_ppv= Integer.parseInt(myJson.optString("is_ppv"));
                permalink=myJson.optString("permalink");
                item_count= Integer.parseInt(myJson.optString("item_count"));
                movieUniqueId=myJson.optString("muvi_uniq_id");

            }

            if (status > 0) {

                if (status == 200) {

                    JSONArray jsonMainNode = myJson.getJSONArray("episode");
                    int lengthJsonArr = jsonMainNode.length();
                    for (int i = 0; i < lengthJsonArr; i++) {
                        JSONObject jsonChildNode;
                        try {
                            jsonChildNode = jsonMainNode.getJSONObject(i);
                            Episode_Details_output content = new Episode_Details_output();

                            if ((jsonChildNode.has("episode_title")) && jsonChildNode.optString("episode_title").trim() != null && !jsonChildNode.optString("episode_title").trim().isEmpty() && !jsonChildNode.optString("episode_title").trim().equals("null") && !jsonChildNode.optString("episode_title").trim().matches("")) {
                               // String episode_title=jsonChildNode.optString("episode_title");
                                content.setEpisode_title(jsonChildNode.optString("episode_title"));
                            }
                            if ((jsonChildNode.has("poster_url")) && jsonChildNode.optString("poster_url").trim() != null && !jsonChildNode.optString("poster_url").trim().isEmpty() && !jsonChildNode.optString("poster_url").trim().equals("null") && !jsonChildNode.optString("poster_url").trim().matches("")) {
                                content.setPoster_url(jsonChildNode.optString("poster_url"));

                            }
                            if ((jsonChildNode.has("episode_story")) && jsonChildNode.optString("episode_story").trim() != null && !jsonChildNode.optString("episode_story").trim().isEmpty() && !jsonChildNode.optString("episode_story").trim().equals("null") && !jsonChildNode.optString("episode_story").trim().matches("")) {
                                content.setPermalink(jsonChildNode.optString("episode_story"));
                            }
                           if ((jsonChildNode.has("embeddedUrl")) && jsonChildNode.optString("embeddedUrl").trim() != null && !jsonChildNode.optString("embeddedUrl").trim().isEmpty() && !jsonChildNode.optString("embeddedUrl").trim().equals("null") && !jsonChildNode.optString("embeddedUrl").trim().matches("")) {
                                content.setEmbeddedUrl(jsonChildNode.optString("embeddedUrl"));

                            }
                            //videoTypeIdStr = "1";

                            if ((jsonChildNode.has("video_duration")) && jsonChildNode.optString("video_duration").trim() != null && !jsonChildNode.optString("video_duration").trim().isEmpty() && !jsonChildNode.optString("video_duration").trim().equals("null") && !jsonChildNode.optString("video_duration").trim().matches("")) {
                                content.setVideo_duration(jsonChildNode.optString("video_duration"));

                            }
                           /*  if ((jsonChildNode.has("is_advance")) && jsonChildNode.optString("is_advance").trim() != null && !jsonChildNode.optString("is_advance").trim().isEmpty() && !jsonChildNode.optString("is_advance").trim().equals("null") && !jsonChildNode.optString("is_advance").trim().matches("")) {
                                content.setIsAPV(Integer.parseInt(jsonChildNode.optString("is_advance")));

                            }
                            if ((jsonChildNode.has("is_ppv")) && jsonChildNode.optString("is_ppv").trim() != null && !jsonChildNode.optString("is_ppv").trim().isEmpty() && !jsonChildNode.optString("is_ppv").trim().equals("null") && !jsonChildNode.optString("is_ppv").trim().matches("")) {
                                content.setIsPPV(Integer.parseInt(jsonChildNode.optString("is_ppv")));

                            }
                            if ((jsonChildNode.has("is_episode")) && jsonChildNode.optString("is_episode").trim() != null && !jsonChildNode.optString("is_episode").trim().isEmpty() && !jsonChildNode.optString("is_episode").trim().equals("null") && !jsonChildNode.optString("is_episode").trim().matches("")) {
                                content.setContentTypesId(jsonChildNode.optString("is_episode"));

                            }*/
                           episode_details_output.add(content);
                        } catch (Exception e) {
                            status = 0;
                          // totalItems = 0;
                            message = "";
                        }
                    }
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
        listener.onGetEpisodeDetailsPreExecuteStarted();
        status = 0;
        if(!PACKAGE_NAME.equals(HeaderConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetEpisodeDetailsPostExecuteCompleted(episode_details_output, status, item_count, message);
            return;
        }
        if(HeaderConstants.hashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetEpisodeDetailsPostExecuteCompleted(episode_details_output, status, item_count, message);
        }

    }



    @Override
    protected void onPostExecute(Void result) {
       listener.onGetEpisodeDetailsPostExecuteCompleted(episode_details_output, status, item_count, message);

    }

    //}
}
