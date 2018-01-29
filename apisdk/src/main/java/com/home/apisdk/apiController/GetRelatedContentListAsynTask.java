/**
 * SDK initialization, platform and device information classes.
 */


package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.RelatedContentListInput;
import com.home.apisdk.apiModel.RelatedContentListOutput;

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
import java.util.Arrays;

/**
 * This Class is use to show the content list to the users.
 * Among the movies they can select their favorite movies/series and can check all the details about that particular movie/series.
 *
 * @author MUVI
 */
public class GetRelatedContentListAsynTask extends AsyncTask<RelatedContentListInput, Void, Void> {

    private RelatedContentListInput relatedContentListInput;
    private String responseStr;
    private int status;
    private int totalItems;
    private String message;
    private String PACKAGE_NAME;
    private GetRelatedContentListListener listener;
    private Context context;

    /**
     * Interface used to allow the caller of a GetContentListAsynTask to run some code when get
     * responses.
     */

    public interface GetRelatedContentListListener {

        /**
         * This method will be invoked before controller start execution.
         * This method to handle pre-execution work.
         */


        void onGetRelatedContentListPreExecuteStarted();

        /**
         * This method will be invoked after controller complete execution.
         * This method to handle post-execution work.
         *
         * @param relatedContentListOutputArray A Model Class which which contain responses. To get that responses we need to call the respective getter methods.
         * @param status                 Response Code From the Server
         * @param totalItems             Total Item Present
         * @param message                On Success Message
         */

        void onGetRelatedContentListPostExecuteCompleted(ArrayList<RelatedContentListOutput> relatedContentListOutputArray, int status, int totalItems, String message);
    }


    ArrayList<RelatedContentListOutput> relatedContentListOutput = new ArrayList<RelatedContentListOutput>();

    /**
     * Constructor to initialise the private data members.
     *
     * @param relatedContentListInput A Model Class which is use for background task, we need to set all the attributes through setter methods of input model class,
     *                         For Example: to use this API we have to set following attributes:
     *                         setAuthToken(),setOffset() etc.
     * @param listener         GetContentList Listener
     * @param context          android.content.Context
     */

    public GetRelatedContentListAsynTask(RelatedContentListInput relatedContentListInput, GetRelatedContentListListener listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.relatedContentListInput = relatedContentListInput;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "GetContentListAsynTask");

    }

    /**
     * Background thread to execute.
     *
     * @return null
     */

    @Override
    protected Void doInBackground(RelatedContentListInput... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getRelatedContent());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader(HeaderConstants.AUTH_TOKEN, this.relatedContentListInput.getAuthToken());
//            httppost.addHeader(HeaderConstants.PERMALINK, this.relatedContentListInput.getPermalink());
//            httppost.addHeader(HeaderConstants.LIMIT, this.relatedContentListInput.getLimit());
//            httppost.addHeader(HeaderConstants.OFFSET, this.relatedContentListInput.getOffset());
            httppost.addHeader(HeaderConstants.CONTENT_STREAM_ID, this.relatedContentListInput.getContent_stream_id());
            httppost.addHeader(HeaderConstants.CONTENT_ID, this.relatedContentListInput.getContent_id());
//            httppost.addHeader("orderby", this.contentListInput.getOrderby());
            httppost.addHeader(HeaderConstants.COUNTRY, this.relatedContentListInput.getCountry());
            httppost.addHeader(HeaderConstants.LANG_CODE, this.relatedContentListInput.getLanguage());
//            httppost.addHeader(HeaderConstants.ORDER_BY, this.relatedContentListInput.getOrderby());

            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("MUVISDK", "RES value" + responseStr);

            } catch (org.apache.http.conn.ConnectTimeoutException e) {
                status = 0;
                totalItems = 0;
                message = "";

            } catch (IOException e) {
                status = 0;
                totalItems = 0;
                message = "";
            }

            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                status = Integer.parseInt(myJson.optString("code"));
               // totalItems = Integer.parseInt(myJson.optString("total_count"));
                message = myJson.optString("msg");
            }

            Log.v("SUBHA"," status === "+status );


            if (status == 200) {

                JSONArray jsonMainNode = myJson.getJSONArray("contentData");

                int lengthJsonArr = jsonMainNode.length();
                for (int i = 0; i < lengthJsonArr; i++) {
                    JSONObject jsonChildNode;
                    try {
                        jsonChildNode = jsonMainNode.getJSONObject(i);
                        RelatedContentListOutput content = new RelatedContentListOutput();
                        if ((jsonChildNode.has("movie_id")) && jsonChildNode.optString("movie_id").trim() != null && !jsonChildNode.optString("movie_id").trim().isEmpty() && !jsonChildNode.optString("movie_id").trim().equals("null") && !jsonChildNode.optString("movie_id").trim().matches("")) {
                            content.setContentId(jsonChildNode.optString("movie_id"));

                        }
                        if ((jsonChildNode.has("movie_stream_id")) && jsonChildNode.optString("movie_stream_id").trim() != null && !jsonChildNode.optString("movie_stream_id").trim().isEmpty() && !jsonChildNode.optString("movie_stream_id").trim().equals("null") && !jsonChildNode.optString("movie_stream_id").trim().matches("")) {
                            content.setContentStreamId(jsonChildNode.optString("movie_stream_id"));

                        }
                        if ((jsonChildNode.has("story")) && jsonChildNode.optString("story").trim() != null && !jsonChildNode.optString("story").trim().isEmpty() && !jsonChildNode.optString("story").trim().equals("null") && !jsonChildNode.optString("story").trim().matches("")) {
                            content.setStory(jsonChildNode.optString("story"));

                        }
                        if ((jsonChildNode.has("title")) && jsonChildNode.optString("title").trim() != null && !jsonChildNode.optString("title").trim().isEmpty() && !jsonChildNode.optString("title").trim().equals("null") && !jsonChildNode.optString("title").trim().matches("")) {
                            content.setName(jsonChildNode.optString("title"));
                        }
                        if ((jsonChildNode.has("poster")) && jsonChildNode.optString("poster").trim() != null && !jsonChildNode.optString("poster").trim().isEmpty() && !jsonChildNode.optString("poster").trim().equals("null") && !jsonChildNode.optString("poster").trim().matches("")) {
                            content.setPosterUrl(jsonChildNode.optString("poster"));

                        }
                        if ((jsonChildNode.has("permalink")) && jsonChildNode.optString("permalink").trim() != null && !jsonChildNode.optString("permalink").trim().isEmpty() && !jsonChildNode.optString("permalink").trim().equals("null") && !jsonChildNode.optString("permalink").trim().matches("")) {
                            content.setPermalink(jsonChildNode.optString("permalink"));
                        }
                        if ((jsonChildNode.has("c_permalink")) && jsonChildNode.optString("c_permalink").trim() != null && !jsonChildNode.optString("c_permalink").trim().isEmpty() && !jsonChildNode.optString("c_permalink").trim().equals("null") && !jsonChildNode.optString("c_permalink").trim().matches("")) {
                            content.setPermalink(jsonChildNode.optString("c_permalink"));
                        }
                        if ((jsonChildNode.has("content_types_id")) && jsonChildNode.optString("content_types_id").trim() != null && !jsonChildNode.optString("content_types_id").trim().isEmpty() && !jsonChildNode.optString("content_types_id").trim().equals("null") && !jsonChildNode.optString("content_types_id").trim().matches("")) {
                            content.setContentTypesId(jsonChildNode.optString("content_types_id"));

                        }
                        //videoTypeIdStr = "1";

                       /* if ((jsonChildNode.has("is_converted")) && jsonChildNode.optString("is_converted").trim() != null && !jsonChildNode.optString("is_converted").trim().isEmpty() && !jsonChildNode.optString("is_converted").trim().equals("null") && !jsonChildNode.optString("is_converted").trim().matches("")) {
                            content.setIsConverted(Integer.parseInt(jsonChildNode.optString("is_converted")));

                        }
                        if ((jsonChildNode.has("is_advance")) && jsonChildNode.optString("is_advance").trim() != null && !jsonChildNode.optString("is_advance").trim().isEmpty() && !jsonChildNode.optString("is_advance").trim().equals("null") && !jsonChildNode.optString("is_advance").trim().matches("")) {
                            content.setIsAPV(Integer.parseInt(jsonChildNode.optString("is_advance")));

                        }
                        if ((jsonChildNode.has("is_ppv")) && jsonChildNode.optString("is_ppv").trim() != null && !jsonChildNode.optString("is_ppv").trim().isEmpty() && !jsonChildNode.optString("is_ppv").trim().equals("null") && !jsonChildNode.optString("is_ppv").trim().matches("")) {
                            content.setIsPPV(Integer.parseInt(jsonChildNode.optString("is_ppv")));

                        }
                        if ((jsonChildNode.has("is_episode")) && jsonChildNode.optString("is_episode").trim() != null && !jsonChildNode.optString("is_episode").trim().isEmpty() && !jsonChildNode.optString("is_episode").trim().equals("null") && !jsonChildNode.optString("is_episode").trim().matches("")) {
                            content.setIsEpisodeStr(jsonChildNode.optString("is_episode"));

                        }



                        if ((myJson.has("custom")) && myJson.getString("custom").trim() != null && !myJson.getString("custom").trim().isEmpty() && !myJson.getString("custom").trim().equals("null") && !myJson.getString("custom").trim().matches("")) {
                            JSONObject epDetailsJson = myJson.getJSONObject("custom");
                            Log.v("MUVI", "custom====== " + epDetailsJson.getString("duration").split(","));

                            if ((epDetailsJson.has("duration")) && epDetailsJson.getString("duration").trim() != null && !epDetailsJson.getString("duration").trim().isEmpty() && !epDetailsJson.getString("duration").trim().equals("null") && !epDetailsJson.getString("duration").trim().matches("") && !epDetailsJson.getString("duration").trim().matches("0")) {
                             JSONObject durationJson = myJson.getJSONObject("duration");


                                if ((durationJson.has("field_display_name")) && durationJson.getString("field_display_name").trim() != null && !durationJson.getString("field_display_name").trim().isEmpty() && !durationJson.getString("field_display_name").trim().equals("null") && !durationJson.getString("field_display_name").trim().matches("") && !durationJson.getString("field_display_name").trim().matches("0")) {
                                     content.setDuration_field_display_name(durationJson.getString("field_display_name"));

                                }
                                if ((durationJson.has("field_value")) && durationJson.getString("field_value").trim() != null && !durationJson.getString("field_value").trim().isEmpty() && !durationJson.getString("field_value").trim().equals("null") && !durationJson.getString("field_value").trim().matches("") && !durationJson.getString("field_value").trim().matches("0")) {
                                    content.setDuration_value(durationJson.getString("field_value"));

                                }
                            }

                            if ((epDetailsJson.has("repetition")) && epDetailsJson.getString("repetition").trim() != null && !epDetailsJson.getString("repetition").trim().isEmpty() && !epDetailsJson.getString("repetition").trim().equals("null") && !epDetailsJson.getString("repetition").trim().matches("") && !epDetailsJson.getString("repetition").trim().matches("0")) {
                                JSONObject repetitionJson = myJson.getJSONObject("repetition");


                                if ((repetitionJson.has("field_display_name")) && repetitionJson.getString("field_display_name").trim() != null && !repetitionJson.getString("field_display_name").trim().isEmpty() && !repetitionJson.getString("field_display_name").trim().equals("null") && !repetitionJson.getString("field_display_name").trim().matches("") && !repetitionJson.getString("field_display_name").trim().matches("0")) {
                                    content.setRepetition_field_display_name(repetitionJson.getString("field_display_name"));

                                }
                                if ((repetitionJson.has("field_value")) && repetitionJson.getString("field_value").trim() != null && !repetitionJson.getString("field_value").trim().isEmpty() && !repetitionJson.getString("field_value").trim().equals("null") && !repetitionJson.getString("field_value").trim().matches("") && !repetitionJson.getString("field_value").trim().matches("0")) {
                                    content.setRepetition_value(repetitionJson.getString("field_value"));

                                }
                            }
                            if ((epDetailsJson.has("difficulty_level")) && epDetailsJson.getString("difficulty_level").trim() != null && !epDetailsJson.getString("difficulty_level").trim().isEmpty() && !epDetailsJson.getString("difficulty_level").trim().equals("null") && !epDetailsJson.getString("difficulty_level").trim().matches("") && !epDetailsJson.getString("difficulty_level").trim().matches("0")) {
                                JSONObject difficultyJson = myJson.getJSONObject("difficulty_level");


                                if ((difficultyJson.has("field_display_name")) && difficultyJson.getString("field_display_name").trim() != null && !difficultyJson.getString("field_display_name").trim().isEmpty() && !difficultyJson.getString("field_display_name").trim().equals("null") && !difficultyJson.getString("field_display_name").trim().matches("") && !difficultyJson.getString("field_display_name").trim().matches("0")) {
                                    content.setDifficulty_level_field_display_name(difficultyJson.getString("field_display_name"));

                                }
                                if ((difficultyJson.has("field_value")) && difficultyJson.getString("field_value").trim() != null && !difficultyJson.getString("field_value").trim().isEmpty() && !difficultyJson.getString("field_value").trim().equals("null") && !difficultyJson.getString("field_value").trim().matches("") && !difficultyJson.getString("field_value").trim().matches("0")) {
                                    content.setDifficulty_level_value(difficultyJson.getString("field_value"));

                                }
                            }
                            if ((epDetailsJson.has("benefits")) && epDetailsJson.getString("benefits").trim() != null && !epDetailsJson.getString("benefits").trim().isEmpty() && !epDetailsJson.getString("benefits").trim().equals("null") && !epDetailsJson.getString("benefits").trim().matches("") && !epDetailsJson.getString("benefits").trim().matches("0")) {
                                JSONObject benefitsJson = myJson.getJSONObject("benefits");


                                if ((benefitsJson.has("field_display_name")) && benefitsJson.getString("field_display_name").trim() != null && !benefitsJson.getString("field_display_name").trim().isEmpty() && !benefitsJson.getString("field_display_name").trim().equals("null") && !benefitsJson.getString("field_display_name").trim().matches("") && !benefitsJson.getString("field_display_name").trim().matches("0")) {
                                    content.setBenefits_field_display_name(benefitsJson.getString("field_display_name"));

                                }
                                if ((benefitsJson.has("field_value")) && benefitsJson.getString("field_value").trim() != null && !benefitsJson.getString("field_value").trim().isEmpty() && !benefitsJson.getString("field_value").trim().equals("null") && !benefitsJson.getString("field_value").trim().matches("") && !benefitsJson.getString("field_value").trim().matches("0")) {
                                    content.setBenefits_value(benefitsJson.getString("field_value"));

                                }
                            }

                        }
*/

                        relatedContentListOutput.add(content);
                    } catch (Exception e) {
                        status = 0;
                        totalItems = 0;
                        message = "";
                    }
                }
            }

        } catch (Exception e) {
            status = 0;
            totalItems = 0;
            message = "";
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onGetRelatedContentListPreExecuteStarted();
        responseStr = "0";
        status = 0;
        if (!PACKAGE_NAME.equals(SDKInitializer.getUser_Package_Name_At_Api(context))) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetRelatedContentListPostExecuteCompleted(relatedContentListOutput, status, totalItems, message);
            return;
        }
        if (SDKInitializer.getHashKey(context).equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetRelatedContentListPostExecuteCompleted(relatedContentListOutput, status, totalItems, message);
        }

    }


    @Override
    protected void onPostExecute(Void result) {
        listener.onGetRelatedContentListPostExecuteCompleted(relatedContentListOutput, status, totalItems, message);

    }

    //}
}
