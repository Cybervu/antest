/**
 * SDK initialization, platform and device information classes.
 */


package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.ContentData;
import com.home.apisdk.apiModel.RelatedContentInput;
import com.home.apisdk.apiModel.RelatedContentOutput;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This Class gives all the important content about movie/series such as story, poster, Release Date etc.
 * This Class tells the user all the necessary things that user is looking for like Video Duration, whether the content is free or paid, banner, rating, reviews etc.
 *
 * @author MUVI
 */
public class GetRelatedContentAsynTask extends AsyncTask<RelatedContentInput, Void, Void> {

    private RelatedContentInput relatedContentInput;
    private String responseStr;
    private int code, totalCount;
    private String message, status;
    private String PACKAGE_NAME;
    private GetRelatedContentListener listener;
    private ArrayList<ContentData> contentDataArrayList;
    private Context context;

    /**
     * Interface used to allow the caller of a GetRelatedContentAsynTask to run some code when get
     * responses.
     */

    public interface GetRelatedContentListener {

        /**
         * This method will be invoked before controller start execution.
         * This method to handle pre-execution work.
         */


        void onGetRelatedContentPreExecuteStarted();

        /**
         * This method will be invoked after controller complete execution.
         * This method to handle post-execution work.
         *
         * @param relatedContentOutput A Model Class which contain responses. To get that responses we need to call the respective getter methods.
         * @param status               Response Code From The Server
         * @param message              On Success Message
         */

        void onGetRelatedContentPostExecuteCompleted(RelatedContentOutput relatedContentOutput, int status, String message);
    }


    RelatedContentOutput relatedContentOutput = new RelatedContentOutput();

    /**
     * Constructor to initialise the private data members.
     *
     * @param relatedContentInput A Model Class which is use for background task, we need to set all the attributes through setter methods of input model class,
     *                            For Example: to use this API we have to set following attributes:
     *                            setAuthToken(),setContentId() etc.
     * @param listener            GetRelatedContentListener Listener
     * @param context             android.content.Context
     */

    public GetRelatedContentAsynTask(RelatedContentInput relatedContentInput, GetRelatedContentListener listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.relatedContentInput = relatedContentInput;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "GetContentListAsynTask");


    }

    /**
     * Background thread to execute.
     *
     * @return null
     * @throws org.apache.http.conn.ConnectTimeoutException,IOException,JSONException
     */
    @Override
    protected Void doInBackground(RelatedContentInput... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getRelatedContent());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader(HeaderConstants.AUTH_TOKEN, this.relatedContentInput.getAuthToken());
            httppost.addHeader(HeaderConstants.CONTENT_ID, this.relatedContentInput.getContentId());
            httppost.addHeader(HeaderConstants.CONTENT_STREAM_ID, this.relatedContentInput.getContent_stream_id());
            httppost.addHeader(HeaderConstants.LANGUAGE_CODE, this.relatedContentInput.getLanguage());

            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("SUBHA", "responseStr====== " + responseStr);


            } catch (org.apache.http.conn.ConnectTimeoutException e) {

                code = 0;
                message = "Error";


            } catch (IOException e) {
                code = 0;
                message = "Error";
            }

            JSONObject myJson = null;
            if (responseStr != null) {      
                myJson = new JSONObject(responseStr);
                code = Integer.parseInt(myJson.optString("code"));
                message = myJson.optString("msg");
                status = myJson.optString("status");
                try {
                    totalCount = Integer.parseInt(myJson.optString("total_count"));
                    relatedContentOutput.setTotal_count(totalCount);
                } catch (Exception e) {
                    e.printStackTrace();
                    totalCount = 0;
                    relatedContentOutput.setTotal_count(totalCount);
                }
            }


            if (code > 0) {

                /** rating*///

                contentDataArrayList= new ArrayList<>();
                if (code == 200) {

                    JSONArray mainJson = myJson.optJSONArray("contentData");
                    if (mainJson != null && mainJson.length() > 0) {
                        for (int i = 0; i < mainJson.length(); i++) {
                            JSONObject obj = mainJson.getJSONObject(i);
                            ContentData contentData = new ContentData();
                            if (obj.optString("movie_id").trim() != null)
                                contentData.setMovie_id(obj.optString("movie_id").trim());
                            if (obj.optString("content_category_value").trim() != null)
                                contentData.setContent_category_value(obj.optString("content_category_value").trim());
                            if (obj.optString("is_episode").trim() != null)
                                contentData.setIs_episode(obj.optString("is_episode").trim());

                            if (obj.optString("episode_number").trim() != null)
                                contentData.setEpisode_number(obj.optString("episode_number").trim());

                            if (obj.optString("season_number").trim() != null)
                                contentData.setSeason_number(obj.optString("season_number").trim());

                            if (obj.optString("title").trim() != null)
                                contentData.setTitle(obj.optString("title").trim());

                            if (obj.optString("parent_content_title").trim() != null)
                                contentData.setParent_content_title(obj.optString("parent_content_title").trim());

                            if (obj.optString("content_details").trim() != null)
                                contentData.setContent_details(obj.optString("content_details").trim());

                            if (obj.optString("content_title").trim() != null)
                                contentData.setContent_title(obj.optString("content_title").trim());

                            if (obj.optString("permalink").trim() != null)
                                contentData.setPermalink(obj.optString("permalink").trim());

                            if (obj.optString("c_permalink").trim() != null)
                                contentData.setC_permalink(obj.optString("c_permalink").trim());

                            if (obj.optString("poster").trim() != null)
                                contentData.setPoster(obj.optString("poster").trim());

                            if (obj.optString("release_date").trim() != null)
                                contentData.setRelease_date(obj.optString("release_date").trim());

                            if (obj.optString("censor_rating").trim() != null)
                                contentData.setCensor_rating(obj.optString("censor_rating").trim());

                            if (obj.optString("movie_uniq_id").trim() != null)
                                contentData.setMovie_id(obj.optString("movie_uniq_id").trim());

                            if (obj.optString("stream_uniq_id").trim() != null)
                                contentData.setStream_uniq_id(obj.optString("stream_uniq_id").trim());

                            if (obj.optString("video_duration").trim() != null)
                                contentData.setVideo_duration(obj.optString("video_duration").trim());

                            if (obj.optString("watch_duration").trim() != null)
                                contentData.setWatch_duration(obj.optString("watch_duration").trim());

                            if (obj.optString("video_duration_text").trim() != null)
                                contentData.setVideo_duration_text(obj.optString("video_duration_text").trim());

                            if (obj.optString("ppv").trim() != null)
                                contentData.setPpv(obj.optString("ppv").trim());

                            if (obj.optString("payment_type").trim() != null)
                                contentData.setPayment_type(obj.optString("payment_type").trim());

                            if (obj.optString("is_converted").trim() != null)
                                contentData.setIs_converted(obj.optString("is_converted").trim());

                            if (obj.optString("movie_stream_id").trim() != null)
                                contentData.setMovie_stream_id(obj.optString("movie_stream_id").trim());

                            if (obj.optString("uniq_id").trim() != null)
                                contentData.setUniq_id(obj.optString("uniq_id").trim());

                            if (obj.optString("content_types_id").trim() != null)
                                contentData.setContent_types_id(obj.optString("content_types_id").trim());

                            if (obj.optString("ppv_plan_id").trim() != null)
                                contentData.setPpv_plan_id(obj.optString("ppv_plan_id").trim());

                            if (obj.optString("full_movie").trim() != null)
                                contentData.setFull_movie(obj.optString("full_movie").trim());

                            if (obj.optString("story").trim() != null)
                                contentData.setStory(obj.optString("story").trim());

                            if (obj.optString("short_story").trim() != null)
                                contentData.setShort_story(obj.optString("short_story").trim());

                            if (obj.optString("genres").trim() != null)
                                contentData.setGenres(obj.optString("genres").trim());

                            if (obj.optString("display_name").trim() != null)
                                contentData.setDisplay_name(obj.optString("display_name").trim());

                            if (obj.optString("content_permalink").trim() != null)
                                contentData.setContent_permalink(obj.optString("content_permalink").trim());

                            if (obj.optString("trailer_url").trim() != null)
                                contentData.setTrailer_url(obj.optString("trailer_url").trim());

                            if (obj.optString("trailer_is_converted").trim() != null)
                                contentData.setTrailer_is_converted(obj.optString("trailer_is_converted").trim());

                            if (obj.optString("casts").trim() != null)
                                contentData.setCasts(obj.optString("casts").trim());

                            if (obj.optString("casting").trim() != null)
                                contentData.setCasting(obj.optString("casting").trim());

                            if (obj.optString("content_banner").trim() != null)
                                contentData.setContent_banner(obj.optString("content_banner").trim());

                            if (obj.optString("defaultResolution").trim() != null)
                                contentData.setDefaultResolution(obj.optString("defaultResolution").trim());

                            if (obj.optString("duration").trim() != null)
                                contentData.setDuration(obj.optString("duration").trim());

                            if (obj.optString("movie_stream_uniq_id").trim() != null)
                                contentData.setMovie_stream_uniq_id(obj.optString("movie_stream_uniq_id").trim());

                            if (obj.optString("name").trim() != null)
                                contentData.setName(obj.optString("name").trim());

                            if (obj.optString("content_form_id").trim() != null)
                                contentData.setContent_form_id(obj.optString("content_form_id").trim());

                            if (obj.optString("posterForTv").trim() != null)
                                contentData.setPosterForTv(obj.optString("posterForTv").trim());


                            contentDataArrayList.add(contentData);
                        }
                        relatedContentOutput.setContentData(contentDataArrayList);

                    }
                }
            } else {

                responseStr = "0";
                code = 0;
                message = "Error";
            }
        } catch (final JSONException e1) {

            responseStr = "0";
            code = 0;
            message = "Error";
        } catch (Exception e) {

            responseStr = "0";
            code = 0;
            message = "Error";
        }
        return null;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetRelatedContentPreExecuteStarted();

        code = 0;
        if (!PACKAGE_NAME.equals(SDKInitializer.getUser_Package_Name_At_Api(context))) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetRelatedContentPostExecuteCompleted(relatedContentOutput, code, message);
            return;
        }
        if (SDKInitializer.getHashKey(context).equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetRelatedContentPostExecuteCompleted(relatedContentOutput, code, message);
        }


    }

    /**
     * @param result
     */
    @Override
    protected void onPostExecute(Void result) {
        listener.onGetRelatedContentPostExecuteCompleted(relatedContentOutput, code, message);

    }


}
