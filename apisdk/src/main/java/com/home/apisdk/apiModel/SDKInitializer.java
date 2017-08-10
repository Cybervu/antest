package com.home.apisdk.apiModel;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by muvi on 25/7/17.
 */

public class SDKInitializer {


    public void init(SDKInitializerListner sdkInitializerListner,
                     Context context,
                     String authToken){
        this.sdkInitializerListner=sdkInitializerListner;
        this.context = context;
        this.authToken=authToken;
        new InitAsync().execute();
    }

    public void init(Context context,
                     String authToken){
        this.context = context;
        this.authToken=authToken;
        new InitAsync().execute();
    }


    private SDKInitializerListner sdkInitializerListner;
    private Context context;
    private String authToken;
    private String message,responseStr;
    private int status ;


    public interface SDKInitializerListner{
        public void onPreExexuteListner();
        public void onPostExecuteListner();
    }



    private class InitAsync extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sdkInitializerListner.onPreExexuteListner();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            sdkInitializerListner.onPostExecuteListner();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {


                HttpClient httpclient=new DefaultHttpClient();
                HttpPost httppost = new HttpPost(APIUrlConstant.getInitializationUrl());
                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
                httppost.addHeader(CommonConstants.AUTH_TOKEN,authToken);

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
                }

                if (status > 0) {

                    if (status == 200) {
                        if ((myJson.has("pkgname")) && myJson.getString("pkgname").trim() != null && !myJson.getString("pkgname").trim().isEmpty() && !myJson.getString("pkgname").trim().equals("null") && !myJson.getString("pkgname").trim().matches("")) {
                            //HeaderConstants.User_Package_Name_At_Api = (myJson.getString("pkgname"));

                            String PNAME=myJson.getString("pkgname");
                            CommonConstants.user_Package_Name_At_Api = PNAME;
                           /* sp = context.getSharedPreferences(mypreference,0);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("PNME", PNAME);
                            editor.commit();*/

                            Log.v("BIBHU", "pkgname at class="+(myJson.getString("pkgname")));
                            //  Log.v("SANJAY", "pkgname FROM sharedpref="+sp.getString("PNME",""));
                        }else{
//                        HeaderConstants.User_Package_Name_At_Api = "";
//                        Log.v("BIBHU", "pkgname at else class="+(myJson.getString("pkgname")));
                        }


                        if ((myJson.has("hashkey")) && myJson.getString("hashkey").trim() != null && !myJson.getString("hashkey").trim().isEmpty() && !myJson.getString("hashkey").trim().equals("null") && !myJson.getString("hashkey").trim().matches("")) {
                            CommonConstants.hashKey = (myJson.getString("hashkey"));
//                        String HASH=myJson.getString("hashkey");
//                        sp = context.getSharedPreferences(mypreference,0);
//                        SharedPreferences.Editor editor1 = sp.edit();
//                        editor1.putString("hashket", HASH);
//                        editor1.commit();

                            Log.v("BIBHU", "Hash Key at class="+(myJson.getString("hashkey")));
                            //  Log.v("SANJAY", "pkgname FROM commonconstant="+HeaderConstants.HashKey);
                        }else{
                            //HeaderConstants.HashKey = "";
                        }
                    }
                }else{

                    responseStr = "0";
                    status = 0;
                    message = "Error";
                }
            } catch (Exception e)
            {

                responseStr = "0";
                status = 0;
                message = "Error";
            }
            return null;
        }
    }

}
