/**
 * SDK initialization, platform and device information classes.
 */


package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.ContentData;
import com.home.apisdk.apiModel.ContentListOutput;
import com.home.apisdk.apiModel.ContentPriceDetailsInput;
import com.home.apisdk.apiModel.ContentPriceDetailsOutput;
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
public class GetContentPriceDetailsAsyncTask extends AsyncTask<ContentPriceDetailsInput, Void, Void> {

    private ContentPriceDetailsInput contentPriceDetailsInput;
    private String responseStr;
    private int code;
    private String message, status;
    private String PACKAGE_NAME;
    private GetContentPriceDetailsListener listener;
    private ArrayList<ContentData> contentDataArrayList;
    private Context context;

    /**
     * Interface used to allow the caller of a GetRelatedContentAsynTask to run some code when get
     * responses.
     */

    public interface GetContentPriceDetailsListener {

        /**
         * This method will be invoked before controller start execution.
         * This method to handle pre-execution work.
         */


        void onGetContentPriceDetailsPreExecuteStarted();

        /**
         * This method will be invoked after controller complete execution.
         * This method to handle post-execution work.
         *
         * @param contentPriceDetailsOutput A Model Class which contain responses. To get that responses we need to call the respective getter methods.
         * @param status                    Response Code From The Server
         * @param message                   On Success Message
         */

        void onGetContentPriceDetailsPostExecuteCompleted(ContentPriceDetailsOutput contentPriceDetailsOutput, int status, String message);
    }


    ContentPriceDetailsOutput contentPriceDetailsOutput = new ContentPriceDetailsOutput();
    ContentPriceDetailsOutput.contentPrice contentPrice;
    ArrayList<ContentPriceDetailsOutput.contentPrice> contentPriceArray;

    ContentPriceDetailsOutput.contentPrice.ppv ppv;
    ArrayList<ContentPriceDetailsOutput.contentPrice.ppv> ppvArray;

    ContentPriceDetailsOutput.contentPrice.ppv.show show;
    ArrayList<ContentPriceDetailsOutput.contentPrice.ppv.show> showArray;

    ContentPriceDetailsOutput.contentPrice.ppv.season season;
    ArrayList<ContentPriceDetailsOutput.contentPrice.ppv.season> seasonArray;

    ContentPriceDetailsOutput.contentPrice.ppv.season.defaultPrice seasonDefaultPrice;
    ArrayList<ContentPriceDetailsOutput.contentPrice.ppv.season.defaultPrice> seaonDefaultPriceArray;

    ContentPriceDetailsOutput.contentPrice.ppv.season.seasonalPrice seasonSeasonalPrice;
    ArrayList<ContentPriceDetailsOutput.contentPrice.ppv.season.seasonalPrice> seasonSeasonalPriceArray;

    ContentPriceDetailsOutput.contentPrice.ppv.episode episode;
    ArrayList<ContentPriceDetailsOutput.contentPrice.ppv.episode> episodeArray;



    /**
     * Constructor to initialise the private data members.
     *
     * @param contentPriceDetailsInput A Model Class which is use for background task, we need to set all the attributes through setter methods of input model class,
     *                                 For Example: to use this API we have to set following attributes:
     *                                 setAuthToken(),setContentId() etc.
     * @param listener                 GetRelatedContentListener Listener
     * @param context                  android.content.Context
     */

    public GetContentPriceDetailsAsyncTask(ContentPriceDetailsInput contentPriceDetailsInput, GetContentPriceDetailsListener listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.contentPriceDetailsInput = contentPriceDetailsInput;
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
    protected Void doInBackground(ContentPriceDetailsInput... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getContentPriceDetails());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
            httppost.addHeader(HeaderConstants.AUTH_TOKEN, this.contentPriceDetailsInput.getAuthToken());
            httppost.addHeader(HeaderConstants.MOVIE_ID, this.contentPriceDetailsInput.getMovie_id());

            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("KUSH ContenPriceDetails", "responseStr====== " + responseStr);


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
                //message = myJson.optString("msg");
                status = myJson.optString("status");

            }


            if (code > 0) {

                /** rating*///

                if (code == 200) {
                    JSONObject contentPriceJson = myJson.getJSONObject("content_prices");
                    contentPriceArray= new ArrayList<>();
                    contentPrice= new ContentPriceDetailsOutput().new contentPrice();
                    if (contentPriceJson.optString("voucher").trim() != null)
                        contentPrice.setVoucher(contentPriceJson.optString("voucher"));

                    if (contentPriceJson.has("ppv") && contentPriceJson.opt("ppv") != null) {
                        ppvArray= new ArrayList<>();
                        ppv= new ContentPriceDetailsOutput().new contentPrice().new ppv();
                        JSONObject ppvObject = contentPriceJson.getJSONObject("ppv");
                        if (ppvObject.has("subscriber_price") && ppvObject.optString("subscriber_price").trim() != null)
                            ppv.setSubscriber_price(ppvObject.optString("subscriber_price"));

                        if (ppvObject.has("nonsubscriber_price") && ppvObject.optString("nonsubscriber_price").trim() != null)
                            ppv.setNonsubscriber_price(ppvObject.optString("nonsubscriber_price"));

                        // Check for show pricing
                        if (ppvObject.has("show") && ppvObject.opt("show") != null) {
                            showArray= new ArrayList<>();
                            show= new ContentPriceDetailsOutput().new contentPrice().new ppv().new show();
                            JSONObject showObject = ppvObject.getJSONObject("show");
                            if (showObject.has("subscriber_price") && showObject.optString("subscriber_price").trim() != null)
                                show.setSubscriber_price(showObject.optString("subscriber_price"));

                            if (showObject.has("nonsubscriber_price") && showObject.optString("nonsubscriber_price").trim() != null)
                                show.setNonsubscriber_price(showObject.optString("nonsubscriber_price"));

                            showArray.add(show);
                            ppv.setShow(showArray);

                        }

                        // Check of season pricing
                        if (ppvObject.has("season") && ppvObject.opt("season") != null) {
                            seasonArray= new ArrayList<>();
                            season= new ContentPriceDetailsOutput().new contentPrice().new ppv().new season();

                            JSONObject seasonObject = ppvObject.getJSONObject("season");
                            if (seasonObject.has("default_price") && seasonObject.opt("default_price") != null) {
                                seaonDefaultPriceArray= new ArrayList<>();
                                seasonDefaultPrice= new ContentPriceDetailsOutput().new contentPrice().new ppv().new season().new defaultPrice();
                                JSONObject defaultPriceObject= seasonObject.getJSONObject("default_price");

                                if (defaultPriceObject.has("subscriber_price") && defaultPriceObject.optString("subscriber_price").trim() != null)
                                    seasonDefaultPrice.setSubscriber_price(defaultPriceObject.optString("subscriber_price"));

                                if (defaultPriceObject.has("nonsubscriber_price") && defaultPriceObject.optString("nonsubscriber_price").trim() != null)
                                    seasonDefaultPrice.setNonsubscriber_price(defaultPriceObject.optString("nonsubscriber_price"));

                                seaonDefaultPriceArray.add(seasonDefaultPrice);
                                season.setDefaultPrice(seaonDefaultPriceArray);
                            }

                            if(seasonObject.has("seasonal_price") && seasonObject.opt("seasonal_price") != null){
                                seasonSeasonalPriceArray = new ArrayList<>();
                                JSONArray seasonalPriceArray= seasonObject.getJSONArray("seasonal_price");
                                for (int i=0 ; i<seasonalPriceArray.length();i++){
                                    seasonSeasonalPrice= new ContentPriceDetailsOutput().new contentPrice().new ppv().new season().new seasonalPrice();

                                    JSONObject obj= seasonalPriceArray.getJSONObject(i);
                                    if (obj.has("season_id") && obj.optString("season_id").trim() != null)
                                        seasonSeasonalPrice.setSeason_id(obj.optString("season_id"));

                                    if (obj.has("subscriber_price") && obj.optString("subscriber_price").trim() != null)
                                        seasonSeasonalPrice.setSubscriber_price(obj.optString("subscriber_price"));

                                    if (obj.has("nonsubscriber_price") && obj.optString("nonsubscriber_price").trim() != null)
                                        seasonSeasonalPrice.setNonsubscriber_price(obj.optString("nonsubscriber_price"));


                                    seasonSeasonalPriceArray.add(seasonSeasonalPrice);
                                }
                                season.setSeasonalPrice(seasonSeasonalPriceArray);

                            }
                            seasonArray.add(season);
                            ppv.setSeason(seasonArray);
                        }

                        // Check for episode pricing
                        if (ppvObject.has("episode") && ppvObject.opt("episode") != null) {
                            episodeArray= new ArrayList<>();
                            episode= new ContentPriceDetailsOutput().new contentPrice().new ppv().new episode();

                            JSONObject epispodeObject = ppvObject.getJSONObject("episode");
                            if (epispodeObject.has("subscriber_price") && epispodeObject.optString("subscriber_price").trim() != null)
                                episode.setSubscriber_price(epispodeObject.optString("subscriber_price"));

                            if (epispodeObject.has("nonsubscriber_price") && epispodeObject.optString("nonsubscriber_price").trim() != null)
                                episode.setNonsubscriber_price(epispodeObject.optString("nonsubscriber_price"));

                            episodeArray.add(episode);
                            ppv.setEpisode(episodeArray);
                        }

                        ppvArray.add(ppv);
                        contentPrice.setPPV(ppvArray);
                    }

                    contentPriceArray.add(contentPrice);
                    contentPriceDetailsOutput.setContentPrice(contentPriceArray);

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
        listener.onGetContentPriceDetailsPreExecuteStarted();

        code = 0;
        if (!PACKAGE_NAME.equals(SDKInitializer.getUser_Package_Name_At_Api(context))) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetContentPriceDetailsPostExecuteCompleted(contentPriceDetailsOutput, code, message);
            return;
        }
        if (SDKInitializer.getHashKey(context).equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetContentPriceDetailsPostExecuteCompleted(contentPriceDetailsOutput, code, message);
        }


    }

    /**
     * @param result
     */
    @Override
    protected void onPostExecute(Void result) {
        listener.onGetContentPriceDetailsPostExecuteCompleted(contentPriceDetailsOutput, code, message);

    }


}
