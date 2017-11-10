/**
 * SDK initialization, platform and device information classes.
 */


package com.home.apisdk.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.CategoryListInput;
import com.home.apisdk.apiModel.CategoryListOutputModel;

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
 * This Class is use to show the content list to the users.
 * Among the movies they can select their favorite movies/series and can check all the details about that particular movie/series.
 *
 * @author MUVI
 */
public class GetCategoryListAsynTask extends AsyncTask<CategoryListInput, Void, Void> {

    private CategoryListInput categoryListInput;
    private String responseStr;
    private int status;
    private int totalItems;
    private String message;
    private String PACKAGE_NAME;
    private GetCategoryListListener listener;
    private Context context;

    /**
     * Interface used to allow the caller of a GetContentListAsynTask to run some code when get
     * responses.
     */

    public interface GetCategoryListListener {

        /**
         * This method will be invoked before controller start execution.
         * This method to handle pre-execution work.
         */


        void onGetCategoryListPreExecuteStarted();

        /**
         * This method will be invoked after controller complete execution.
         * This method to handle post-execution work.
         *
         * @param categoryListOutputArray A Model Class which which contain responses. To get that responses we need to call the respective getter methods.
         * @param status                 Response Code From the Server
         * @param totalItems             Total Item Present
         * @param message                On Success Message
         */

        void onGetCategoryListPostExecuteCompleted(ArrayList<CategoryListOutputModel> categoryListOutputArray, int status, int totalItems, String message);
    }


    ArrayList<CategoryListOutputModel> categoryListOutput = new ArrayList<CategoryListOutputModel>();

    /**
     * Constructor to initialise the private data members.
     *
     * @param categoryListInput A Model Class which is use for background task, we need to set all the attributes through setter methods of input model class,
     *                         For Example: to use this API we have to set following attributes:
     *                         setAuthToken(),setOffset() etc.
     * @param listener         GetContentList Listener
     * @param context          android.content.Context
     */

    public GetCategoryListAsynTask(CategoryListInput categoryListInput, GetCategoryListListener listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.categoryListInput = categoryListInput;
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
    protected Void doInBackground(CategoryListInput... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getGetCategoryListUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader(HeaderConstants.AUTH_TOKEN, this.categoryListInput.getAuthToken());
            httppost.addHeader(HeaderConstants.COUNTRY, this.categoryListInput.getCountry());
            httppost.addHeader(HeaderConstants.LANG_CODE, this.categoryListInput.getLanguage());

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
                status = Integer.parseInt(myJson.optString("code"));
                message = myJson.optString("msg");
            }


            if (status == 200) {

                JSONArray jsonMainNode = myJson.getJSONArray("category_list");

                int lengthJsonArr = jsonMainNode.length();
                for (int i = 0; i < lengthJsonArr; i++) {
                    JSONObject jsonChildNode;
                    try {
                        jsonChildNode = jsonMainNode.getJSONObject(i);
                        CategoryListOutputModel content = new CategoryListOutputModel();

                        if ((jsonChildNode.has("category_id")) && jsonChildNode.optString("category_id").trim() != null && !jsonChildNode.optString("category_id").trim().isEmpty() && !jsonChildNode.optString("category_id").trim().equals("null") && !jsonChildNode.optString("category_id").trim().matches("")) {
                            content.setCategory_id(jsonChildNode.optString("category_id"));

                        }
                        if ((jsonChildNode.has("category_name")) && jsonChildNode.optString("category_name").trim() != null && !jsonChildNode.optString("category_name").trim().isEmpty() && !jsonChildNode.optString("category_name").trim().equals("null") && !jsonChildNode.optString("category_name").trim().matches("")) {
                            content.setCategory_name(jsonChildNode.optString("category_name"));
                        }
                        if ((jsonChildNode.has("permalink")) && jsonChildNode.optString("poster_url").trim() != null && !jsonChildNode.optString("permalink").trim().isEmpty() && !jsonChildNode.optString("permalink").trim().equals("null") && !jsonChildNode.optString("permalink").trim().matches("")) {
                            content.setPermalink(jsonChildNode.optString("permalink"));

                        }
                        if ((jsonChildNode.has("category_img_url")) && jsonChildNode.optString("category_img_url").trim() != null && !jsonChildNode.optString("category_img_url").trim().isEmpty() && !jsonChildNode.optString("category_img_url").trim().equals("null") && !jsonChildNode.optString("category_img_url").trim().matches("")) {
                            content.setCategory_img_url(jsonChildNode.optString("category_img_url"));
                            Log.v("Nihar_sdk",jsonChildNode.optString("category_img_url"));
                        }

                        categoryListOutput.add(content);
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
        listener.onGetCategoryListPreExecuteStarted();
        responseStr = "0";
        status = 0;
       /* if (!PACKAGE_NAME.equals(SDKInitializer.getUser_Package_Name_At_Api(context))) {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetCategoryListPostExecuteCompleted(categoryListOutput, status, totalItems, message);
            return;
        }
        if (SDKInitializer.getHashKey(context).equals("")) {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetCategoryListPostExecuteCompleted(categoryListOutput, status, totalItems, message);
        }*/

    }


    @Override
    protected void onPostExecute(Void result) {
        listener.onGetCategoryListPostExecuteCompleted(categoryListOutput, status, totalItems, message);

    }

    //}
}
