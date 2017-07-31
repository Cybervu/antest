package com.home.apisdk.apiModel.apiController;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.apiModel.APIUrlConstant;
import com.home.apisdk.apiModel.CommonConstants;
import com.home.apisdk.apiModel.apiModel.MenuListInput;
import com.home.apisdk.apiModel.apiModel.MenuListOutput;

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

public class GetMenuListAsynctask extends AsyncTask<MenuListInput,Void ,Void > {

    public MenuListInput menuListInput;
    String PACKAGE_NAME,message,responseStr;
    int code;

    public interface GetMenuList{
        void onGetMenuListPreExecuteStarted();
        void onGetMenuListPostExecuteCompleted(ArrayList<MenuListOutput> menuListOutput, ArrayList<MenuListOutput> footermenuListOutput, int status, String message);
    }

    private GetMenuList listener;
    private Context context;
    ArrayList<MenuListOutput> menuListOutput = new ArrayList<MenuListOutput>();
    ArrayList<MenuListOutput> footermenuListOutput = new ArrayList<MenuListOutput>();
    public GetMenuListAsynctask(MenuListInput menuListInput, GetMenuList listener, Context context) {
        this.listener = listener;
        this.context=context;


        this.menuListInput = menuListInput;
        PACKAGE_NAME=context.getPackageName();
        Log.v("MUVISDK", "pkgnm :"+PACKAGE_NAME);
        Log.v("MUVISDK","GetMenuListAsynctask");

    }

    @Override
    protected Void doInBackground(MenuListInput... params) {

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(APIUrlConstant.getMenuListUrl());
            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");

            httppost.addHeader(CommonConstants.AUTH_TOKEN, this.menuListInput.getAuthToken());
            httppost.addHeader(CommonConstants.COUNTRY, this.menuListInput.getCountry());
            httppost.addHeader(CommonConstants.LANG_CODE, this.menuListInput.getLang_code());

            // Execute HTTP Post Request
            try {
                HttpResponse response = httpclient.execute(httppost);
                responseStr = EntityUtils.toString(response.getEntity());
                Log.v("MUVISDK", "RES" + responseStr);

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
                message = myJson.optString("msg");
            }

            if (code == 200) {

                JSONArray jsonMainNode = myJson.getJSONArray("menu");
                JSONArray jsonFooterNode = myJson.getJSONArray("footer_menu");
                int jsonFooterNodeArr = jsonFooterNode.length();
                int lengthJsonArr = jsonMainNode.length();
                for (int i = 0; i < lengthJsonArr; i++) {
                    JSONObject jsonChildNode;
                    try {
                        jsonChildNode = jsonMainNode.getJSONObject(i);
                        MenuListOutput content = new MenuListOutput();
                        content.setLink_type(jsonChildNode.optString("link_type"));
                        content.setDisplay_name(jsonChildNode.optString("display_name"));
                        content.setPermalink(jsonChildNode.optString("permalink"));
                        content.setEnable(true);
                        menuListOutput.add(content);
                    } catch (Exception e) {
                        code = 0;
                        message = "";
                    }
                }


                /*** footer menu******/
                for (int i = 0; i < jsonFooterNodeArr; i++) {
                    JSONObject jsonChildNode;
                    try {
                        jsonChildNode = jsonFooterNode.getJSONObject(i);
                        MenuListOutput content = new MenuListOutput();
                        content.setDisplay_name(jsonChildNode.optString("display_name"));
                        content.setPermalink(jsonChildNode.optString("permalink"));
                        content.setUrl(jsonChildNode.optString("url"));
                        content.setEnable(false);
                        footermenuListOutput.add(content);

                    } catch (Exception e) {
                        code = 0;
                        message = "";
                        // TODO Auto-generated catch block
                        e.printStackTrace();
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
        listener.onGetMenuListPreExecuteStarted();
        code= 0;
        if(!PACKAGE_NAME.equals(CommonConstants.user_Package_Name_At_Api))
        {
            this.cancel(true);
            message = "Packge Name Not Matched";
            listener.onGetMenuListPostExecuteCompleted(menuListOutput,footermenuListOutput,code,message);
            return;
        }
        if(CommonConstants.hashKey.equals(""))
        {
            this.cancel(true);
            message = "Hash Key Is Not Available. Please Initialize The SDK";
            listener.onGetMenuListPostExecuteCompleted(menuListOutput,footermenuListOutput,code,message);
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        listener.onGetMenuListPostExecuteCompleted(menuListOutput,footermenuListOutput,code,message);
    }
}
