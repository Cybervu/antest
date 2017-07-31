package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.CommonConstants;
import com.home.apisdk.apiModel.ContentListInput;
import com.home.apisdk.apiModel.ContentListOutput;
import com.home.apisdk.apiModel.GetCastDetailsInput;
import com.home.apisdk.apiModel.GetCastDetailsOutputModel;
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
public class GetCastDetailsAsynTask extends AsyncTask<GetCastDetailsInput, Void, Void> {

    GetCastDetailsInput getCastDetailsInput;
    String responseStr;
    int status;
    int totalItems;
    String message, PACKAGE_NAME;

    public interface GetCastDetailsListener {
        void onGetCastDetailsPreExecuteStarted();

        void onGetCastDetailsPostExecuteCompleted(GetCastDetailsOutputModel getCastDetailsOutputModelArray, int status, String message);
    }
   /* public class GetContentListAsync extends AsyncTask<Void, Void, Void> {*/

    private GetCastDetailsListener listener;
    private Context context;

    GetCastDetailsOutputModel getCastDetailsOutputModel;
    GetCastDetailsOutputModel.CastDetails castDetails ;
    ArrayList<GetCastDetailsOutputModel.CastDetails> castDetailsArrayList;

    public GetCastDetailsAsynTask(GetCastDetailsInput getCastDetailsInput, GetCastDetailsListener listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.getCastDetailsInput = getCastDetailsInput;
        PACKAGE_NAME = context.getPackageName();
        Log.v("MUVISDK", "pkgnm :" + PACKAGE_NAME);
        Log.v("MUVISDK", "GetContentListAsynTask");

    }

    @Override
    protected Void doInBackground(GetCastDetailsInput... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getGetCastDetails());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader(CommonConstants.AUTH_TOKEN, this.getCastDetailsInput.getAuthToken());
            httppost.addHeader(CommonConstants.PERMALINK, this.getCastDetailsInput.getPermalink());
            httppost.addHeader(CommonConstants.LIMIT, this.getCastDetailsInput.getLimit());
            httppost.addHeader(CommonConstants.OFFSET, this.getCastDetailsInput.getOffset());
            httppost.addHeader(CommonConstants.COUNTRY, this.getCastDetailsInput.getCountry());
            httppost.addHeader(CommonConstants.LANG_CODE,this.getCastDetailsInput.getLanguage());

            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("MUVISDK", "RES" + responseStr);

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
                status = Integer.parseInt(myJson.optString("status"));
                totalItems = Integer.parseInt(myJson.optString("item_count"));
                message = myJson.optString("msg");
            }


            if (status == 200) {
                getCastDetailsOutputModel = new GetCastDetailsOutputModel();
                if ((myJson.has("name")) && myJson.getString("name").trim() != null && !myJson.getString("name").trim().isEmpty() && !myJson.getString("name").trim().equals("null") && !myJson.getString("name").trim().matches("")) {
                    getCastDetailsOutputModel.setName(myJson.getString("name"));

                }
                if ((myJson.has("summary")) && myJson.getString("summary").trim() != null && !myJson.getString("summary").trim().isEmpty() && !myJson.getString("summary").trim().equals("null") && !myJson.getString("summary").trim().matches("")) {
                    getCastDetailsOutputModel.setSummary(myJson.getString("summary"));

                }
                if ((myJson.has("cast_image")) && myJson.getString("cast_image").trim() != null && !myJson.getString("cast_image").trim().isEmpty() && !myJson.getString("cast_image").trim().equals("null") && !myJson.getString("cast_image").trim().matches("")) {
                    getCastDetailsOutputModel.setCastImage(myJson.getString("cast_image"));
                }
                JSONArray jsonMainNode = myJson.getJSONArray("movieList");

                int lengthJsonArr = jsonMainNode.length();
                for (int i = 0; i < lengthJsonArr; i++) {
                    JSONObject jsonChildNode;
                    try {
                        jsonChildNode = jsonMainNode.getJSONObject(i);
                        castDetails = new GetCastDetailsOutputModel().new CastDetails();


                        if ((jsonChildNode.has("name")) && jsonChildNode.optString("name").trim() != null && !jsonChildNode.optString("name").trim().isEmpty() && !jsonChildNode.optString("name").trim().equals("null") && !jsonChildNode.optString("name").trim().matches("")) {
                            castDetails.setName(jsonChildNode.optString("name"));
                        }
                        if ((jsonChildNode.has("poster_url")) && jsonChildNode.optString("poster_url").trim() != null && !jsonChildNode.optString("poster_url").trim().isEmpty() && !jsonChildNode.optString("poster_url").trim().equals("null") && !jsonChildNode.optString("poster_url").trim().matches("")) {
                            castDetails.setPosterUrl(jsonChildNode.optString("poster_url"));

                        }
                        if ((jsonChildNode.has("permalink")) && jsonChildNode.optString("permalink").trim() != null && !jsonChildNode.optString("permalink").trim().isEmpty() && !jsonChildNode.optString("permalink").trim().equals("null") && !jsonChildNode.optString("permalink").trim().matches("")) {
                            castDetails.setPermalink(jsonChildNode.optString("permalink"));
                        }
                        if ((jsonChildNode.has("content_types_id")) && jsonChildNode.optString("content_types_id").trim() != null && !jsonChildNode.optString("content_types_id").trim().isEmpty() && !jsonChildNode.optString("content_types_id").trim().equals("null") && !jsonChildNode.optString("content_types_id").trim().matches("")) {
                            castDetails.setContentTypesId(jsonChildNode.optString("content_types_id"));

                        }

                        if ((jsonChildNode.has("is_converted")) && jsonChildNode.optString("is_converted").trim() != null && !jsonChildNode.optString("is_converted").trim().isEmpty() && !jsonChildNode.optString("is_converted").trim().equals("null") && !jsonChildNode.optString("is_converted").trim().matches("")) {
                            castDetails.setIsConverted(Integer.parseInt(jsonChildNode.optString("is_converted")));

                        }
                        if ((jsonChildNode.has("is_advance")) && jsonChildNode.optString("is_advance").trim() != null && !jsonChildNode.optString("is_advance").trim().isEmpty() && !jsonChildNode.optString("is_advance").trim().equals("null") && !jsonChildNode.optString("is_advance").trim().matches("")) {
                            castDetails.setIsAdvance(Integer.parseInt(jsonChildNode.optString("is_advance")));

                        }
                        if ((jsonChildNode.has("is_ppv")) && jsonChildNode.optString("is_ppv").trim() != null && !jsonChildNode.optString("is_ppv").trim().isEmpty() && !jsonChildNode.optString("is_ppv").trim().equals("null") && !jsonChildNode.optString("is_ppv").trim().matches("")) {
                            castDetails.setIsPPV(Integer.parseInt(jsonChildNode.optString("is_ppv")));

                        }
                        if ((jsonChildNode.has("is_episode")) && jsonChildNode.optString("is_episode").trim() != null && !jsonChildNode.optString("is_episode").trim().isEmpty() && !jsonChildNode.optString("is_episode").trim().equals("null") && !jsonChildNode.optString("is_episode").trim().matches("")) {
                            castDetails.setIsEpisode(jsonChildNode.optString("is_episode"));

                        }
                        castDetailsArrayList.add(castDetails);
                    } catch (Exception e) {
                        status = 0;
                        totalItems = 0;
                        message = "";
                    }
                }

                getCastDetailsOutputModel.setCastdetails(castDetailsArrayList);


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
        listener.onGetCastDetailsPreExecuteStarted();
        responseStr = "0";
        status = 0;
        if (!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api)) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetCastDetailsPostExecuteCompleted(getCastDetailsOutputModel, status,  message);
            return;
        }
        if (CommonConstants.hashKey.equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetCastDetailsPostExecuteCompleted(getCastDetailsOutputModel, status,  message);
        }

    }


    @Override
    protected void onPostExecute(Void result) {
        listener.onGetCastDetailsPostExecuteCompleted(getCastDetailsOutputModel, status,  message);

    }

    //}
}
