package com.home.apisdk.apiModel.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.apiModel.APIUrlConstant;
import com.home.apisdk.apiModel.CommonConstants;
import com.home.apisdk.apiModel.apiModel.GetVideoDetailsInput;
import com.home.apisdk.apiModel.apiModel.Get_Video_Details_Output;

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

public class VideoDetailsAsynctask extends AsyncTask<GetVideoDetailsInput, Void, Void> {

    public GetVideoDetailsInput getVideoDetailsInput;
    ArrayList<String> SubTitleName = new ArrayList<>();
    ArrayList<String> SubTitlePath = new ArrayList<>();
    ArrayList<String> FakeSubTitlePath = new ArrayList<>();
    ArrayList<String> ResolutionFormat = new ArrayList<>();
    ArrayList<String>ResolutionUrl = new ArrayList<>();
    String PACKAGE_NAME, message, responseStr, status;
    JSONArray SubtitleJosnArray = null;
    JSONArray ResolutionJosnArray = null;
    int code;
    Get_Video_Details_Output get_video_details_output;

    public interface VideoDetails {
        void onVideoDetailsPreExecuteStarted();

        void onVideoDetailsPostExecuteCompleted(Get_Video_Details_Output get_video_details_output, int code, String status, String message);
    }

    private VideoDetails listener;
    private Context context;

    public VideoDetailsAsynctask(GetVideoDetailsInput getVideoDetailsInput, VideoDetails listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.getVideoDetailsInput = getVideoDetailsInput;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "VideoDetailsAsynctask");

    }

    @Override
    protected Void doInBackground(GetVideoDetailsInput... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getVideoDetailsUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader(CommonConstants.AUTH_TOKEN, this.getVideoDetailsInput.getAuthToken());
            httppost.addHeader(CommonConstants.CONTENT_UNIQ_ID, this.getVideoDetailsInput.getContent_uniq_id());
            httppost.addHeader(CommonConstants.STREAM_UNIQ_ID, this.getVideoDetailsInput.getStream_uniq_id());
            httppost.addHeader(CommonConstants.INTERNET_SPEED, this.getVideoDetailsInput.getInternetSpeed());
            httppost.addHeader(CommonConstants.USER_ID, this.getVideoDetailsInput.getUser_id());

            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("MUVISDK", "RES" + responseStr);

            } catch (org.apache.http.conn.ConnectTimeoutException e) {
                code = 0;
                message = "";
                status = "";

            } catch (IOException e) {
                code = 0;
                message = "";
                status = "";

            }
            JSONArray SubtitleJosnArray = null;
            JSONArray ResolutionJosnArray = null;
            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                code = Integer.parseInt(myJson.optString("code"));
                message = myJson.optString("msg");
                SubtitleJosnArray = myJson.optJSONArray("subTitle");
                ResolutionJosnArray = myJson.optJSONArray("videoDetails");
                status = myJson.optString("status");
            }

            if (code == 200) {
                try {
                    get_video_details_output = new Get_Video_Details_Output();
                    get_video_details_output.setVideoResolution(myJson.optString("videoResolution"));
                    get_video_details_output.setVideoUrl(myJson.optString("videoUrl"));
                    get_video_details_output.setEmed_url(myJson.optString("emed_url"));
                    get_video_details_output.setPlayed_length(myJson.optString("played_length"));
                    get_video_details_output.setThirdparty_url(myJson.optString("thirdparty_url"));

                } catch (Exception e) {
                    code = 0;
                    message = "";
                    status = "";
                }
                if(SubtitleJosnArray!=null)
                {
                    if(SubtitleJosnArray.length()>0)
                    {
                        for(int i=0;i<SubtitleJosnArray.length();i++)
                        {
                            SubTitleName.add(SubtitleJosnArray.getJSONObject(i).optString("language").trim());
                            FakeSubTitlePath.add(SubtitleJosnArray.getJSONObject(i).optString("url").trim());


                        }

                        get_video_details_output.setSubTitleName(SubTitleName);
                        get_video_details_output.setFakeSubTitlePath(FakeSubTitlePath);
                    }
                }

                /******Resolution****/

                if(ResolutionJosnArray!=null)
                {
                    if(ResolutionJosnArray.length()>0)
                    {
                        for(int i=0;i<ResolutionJosnArray.length();i++)
                        {
                            if((ResolutionJosnArray.getJSONObject(i).optString("resolution").trim()).equals("BEST"))
                            {
                                ResolutionFormat.add(ResolutionJosnArray.getJSONObject(i).optString("resolution").trim());
                            }
                            else
                            {
                                ResolutionFormat.add((ResolutionJosnArray.getJSONObject(i).optString("resolution").trim())+"p");
                            }

                            ResolutionUrl.add(ResolutionJosnArray.getJSONObject(i).optString("url").trim());

                            Log.v("MUVISDK","Resolution Format Name ="+ResolutionJosnArray.getJSONObject(i).optString("resolution").trim());
                            Log.v("MUVISDK","Resolution url ="+ResolutionJosnArray.getJSONObject(i).optString("url").trim());
                        }

                        get_video_details_output.setResolutionFormat(ResolutionFormat);
                        get_video_details_output.setResolutionUrl(ResolutionUrl);
                    }

                }

            }
        } catch (Exception e) {
            code = 0;
            message = "";
            status = "";
        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onVideoDetailsPreExecuteStarted();
        code = 0;
        status = "";
        if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onVideoDetailsPostExecuteCompleted(get_video_details_output,code,status,message);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onVideoDetailsPostExecuteCompleted(get_video_details_output,code,status,message);
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onVideoDetailsPostExecuteCompleted(get_video_details_output, code, status, message);
    }
}
