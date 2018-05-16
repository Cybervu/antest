/**
 * SDK initialization, platform and device information classes.
 */

package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.AboutUsInput;
import com.home.apisdk.apiModel.AppHomeFeatureInputModel;
import com.home.apisdk.apiModel.AppHomeFeatureOutputModel;
import com.home.apisdk.apiModel.HomeFeaturePageBannerModel;
import com.home.apisdk.apiModel.HomeFeaturePageSectionDetailsModel;
import com.home.apisdk.apiModel.HomeFeaturePageSectionModel;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
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
 * This Class gives a short note about the company/organisation to its users.
 *
 * @author MUVI
 */

public class GetAppHomeFeatureAsyncTask extends AsyncTask<AppHomeFeatureInputModel, Void, Void> {


    private AppHomeFeatureInputModel appHomeFeatureInputModel;
    private int status;
    private String message;
    private String PACKAGE_NAME;
    private String responseStr;
    private AppHomeFeature listener;
    private Context context;
    private int code = 0;

    /**
     * Interface used to allow the caller of a AboutUsAsync to run some code when get
     * responses.
     */
    public interface AppHomeFeature {

        /**
         * This method will be invoked before controller start execution.
         * This method to handle pre-execution work.
         */
        void AppHomeFeaturePreExecute();

        /**
         * @param appHomeFeatureOutputModel
         * @param status
         */
        void AppHomeFeaturePostExecute(AppHomeFeatureOutputModel appHomeFeatureOutputModel, int status);
    }

    AppHomeFeatureOutputModel appHomeFeatureOutputModel = new AppHomeFeatureOutputModel();
    ArrayList<HomeFeaturePageSectionModel> homePageSectionModelArrayList = new ArrayList<HomeFeaturePageSectionModel>();
    ArrayList<HomeFeaturePageBannerModel> homePageBannerModelArrayList = new ArrayList<HomeFeaturePageBannerModel>();


    /**
     * Constructor to initialise the private data members.
     *
     * @param appHomeFeatureInputModel
     * @param listener
     * @param context
     */
    public GetAppHomeFeatureAsyncTask(AppHomeFeatureInputModel appHomeFeatureInputModel, AppHomeFeature listener, Context context) {
        this.listener = listener;
        this.context = context;

        this.appHomeFeatureInputModel = appHomeFeatureInputModel;
        PACKAGE_NAME = context.getPackageName();


    }

    /**
     * Background thread to execute.
     *
     * @return Null
     * @throws org.apache.http.conn.ConnectTimeoutException,IOException,JSONException
     */

    @Override
    protected Void doInBackground(AppHomeFeatureInputModel... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getGetAppHomeFeature());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader(HeaderConstants.AUTH_TOKEN, this.appHomeFeatureInputModel.getAuthToken());
            httppost.addHeader(HeaderConstants.FEATURE_SECTION_LIMIT, this.appHomeFeatureInputModel.getFeatureSectionLimit());
            httppost.addHeader(HeaderConstants.FEATURE_SECTION_OFFSET, this.appHomeFeatureInputModel.getGetFeatureSectionOffset());
            httppost.addHeader(HeaderConstants.USER_ID, this.appHomeFeatureInputModel.getUserId());
            httppost.addHeader(HeaderConstants.LANG_CODE, this.appHomeFeatureInputModel.getLang_code());

            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("MUVISDK", "RES" + responseStr);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            JSONObject myJson = null;
            if (responseStr != null) {
                try {
                    myJson = new JSONObject(responseStr);
                    code = Integer.parseInt(myJson.optString("code"));
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            if (code == 200) {
                try {

                    //************************************************************************************************************//

                    HomeFeaturePageBannerModel homeFeaturePageBannerModel = null;
                    if (myJson.has("BannerSectionList")) {
                        JSONArray jsonMainNode = myJson.getJSONArray("BannerSectionList");
                        int lengthJsonArr = jsonMainNode.length();
                        if (lengthJsonArr > 0) {
                            for (int i = 0; i < lengthJsonArr; i++) {

                                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                                String image_path = jsonChildNode.optString("image_path");
                                String banner_url = jsonChildNode.getString("banner_url");
                                homeFeaturePageBannerModel = new HomeFeaturePageBannerModel();
                                homeFeaturePageBannerModel.setImage_path(image_path);
                                homeFeaturePageBannerModel.setBanner_url(banner_url);
                                homePageBannerModelArrayList.add(homeFeaturePageBannerModel);
                            }
                        } else {
                            homeFeaturePageBannerModel = new HomeFeaturePageBannerModel();
                            homeFeaturePageBannerModel.setImage_path("https://d2gx0xinochgze.cloudfront.net/public/no-image-a.png");
                            homePageBannerModelArrayList.add(homeFeaturePageBannerModel);
                        }
                    }


                    //************************************************************************************************************//


                    if (myJson.has("SectionName")) {
                        JSONArray sectionName = myJson.getJSONArray("SectionName");
                        HomeFeaturePageSectionModel homeFeaturePageSectionModel;

                        if (sectionName.length() > 0) {
                            for (int j = 0; j < sectionName.length(); j++) {
                                homeFeaturePageSectionModel = new HomeFeaturePageSectionModel();
                                JSONObject jsonChildNode = sectionName.getJSONObject(j);

                                homeFeaturePageSectionModel.setStudio_id(jsonChildNode.optString("studio_id"));
                                homeFeaturePageSectionModel.setLanguage_id(jsonChildNode.optString("language_id"));
                                homeFeaturePageSectionModel.setSection_id(jsonChildNode.optString("section_id"));
                                homeFeaturePageSectionModel.setSection_type(jsonChildNode.optString("section_type"));
                                homeFeaturePageSectionModel.setTitle(jsonChildNode.optString("title"));
                                homeFeaturePageSectionModel.setTotal(jsonChildNode.optString("total"));


                                JSONArray section_item_list = jsonChildNode.optJSONArray("data");
                                ArrayList<HomeFeaturePageSectionDetailsModel> arrayList = new ArrayList<>();

                                if (section_item_list.length() > 0) {
                                    for (int k = 0; k < section_item_list.length(); k++) {

                                        HomeFeaturePageSectionDetailsModel homeFeaturePageSectionDetailsModel = new HomeFeaturePageSectionDetailsModel();
                                        JSONObject object = section_item_list.getJSONObject(k);

                                        homeFeaturePageSectionDetailsModel.setIs_episode(object.optString("is_episode"));
                                        homeFeaturePageSectionDetailsModel.setMovie_stream_uniq_id(object.optString("movie_stream_uniq_id"));
                                        homeFeaturePageSectionDetailsModel.setMovie_id(object.optString("movie_id"));
                                        homeFeaturePageSectionDetailsModel.setMovie_stream_id(object.optString("movie_stream_id"));
                                        homeFeaturePageSectionDetailsModel.setMuvi_uniq_id(object.optString("muvi_uniq_id"));

                                        homeFeaturePageSectionDetailsModel.setPpv_plan_id(object.optString("ppv_plan_id"));
                                        homeFeaturePageSectionDetailsModel.setPermalink(object.optString("permalink"));
                                        homeFeaturePageSectionDetailsModel.setName(object.optString("name"));
                                        homeFeaturePageSectionDetailsModel.setStory(object.optString("story"));
                                        homeFeaturePageSectionDetailsModel.setContent_types_id(object.optString("content_types_id"));

                                        homeFeaturePageSectionDetailsModel.setIs_converted(object.optString("is_converted"));
                                        homeFeaturePageSectionDetailsModel.setPoster_url(object.optString("poster_url"));
                                        homeFeaturePageSectionDetailsModel.setEmbeddedUrl(object.optString("embeddedUrl"));
                                        homeFeaturePageSectionDetailsModel.setFreeContnet(object.optString("isFreeContent"));
                                        homeFeaturePageSectionDetailsModel.setBanner(object.optString("banner"));

                                        arrayList.add(homeFeaturePageSectionDetailsModel);
                                    }
                                }

                                homeFeaturePageSectionModel.setHomeFeaturePageSectionDetailsModel(arrayList);
                                homePageSectionModelArrayList.add(homeFeaturePageSectionModel);
                            }
                        }

                    }

//************************************************************************************************************************//

                    appHomeFeatureOutputModel.setHomePageBannerModelArrayList(homePageBannerModelArrayList);
                    appHomeFeatureOutputModel.setHomePageSectionModelArrayList(homePageSectionModelArrayList);

//************************************************************************************************************************//


                } catch (Exception e) {

                }
            }
        } catch (Exception e) {


        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.AppHomeFeaturePreExecute();
        status = 0;
        if (!PACKAGE_NAME.equals(SDKInitializer.getUser_Package_Name_At_Api(context))) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.AppHomeFeaturePostExecute(appHomeFeatureOutputModel, code);
            return;
        }
        if (SDKInitializer.getHashKey(context).equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.AppHomeFeaturePostExecute(appHomeFeatureOutputModel, code);
        }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.AppHomeFeaturePostExecute(appHomeFeatureOutputModel, code);
    }
}