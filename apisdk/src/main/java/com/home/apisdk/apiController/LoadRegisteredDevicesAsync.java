package com.home.apisdk.apiController;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.LoadRegisteredDevicesInput;
import com.home.apisdk.apiModel.LoadRegisteredDevicesOutput;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by MUVI on 7/5/2017.
 */

public class LoadRegisteredDevicesAsync extends AsyncTask<LoadRegisteredDevicesInput, Void, Void> {
    LoadRegisteredDevicesInput loadRegisteredDevicesInput;
    String responseStr;
    int status;
    String message, PACKAGE_NAME;

    public interface LoadRegisteredDevices {
        void onLoadRegisteredDevicesPreExecuteStarted();

        void onLoadRegisteredDevicesPostExecuteCompleted(ArrayList<LoadRegisteredDevicesOutput> loadRegisteredDevicesOutputs, int status, String message);
    }

    private LoadRegisteredDevices listener;
    private Context context;
    ArrayList<LoadRegisteredDevicesOutput> loadRegisteredDevicesOutputArrayList = new ArrayList<LoadRegisteredDevicesOutput>();

    public LoadRegisteredDevicesAsync(LoadRegisteredDevicesInput languageListInputModel, LoadRegisteredDevices listener, Context context) {
        this.listener = listener;
        this.context = context;


        this.loadRegisteredDevicesInput = loadRegisteredDevicesInput;
        PACKAGE_NAME = context.getPackageName();

    }

    @Override
    protected Void doInBackground(LoadRegisteredDevicesInput... params) {

        try {

            try {
                URL url = new URL(APIUrlConstant.getManageDevices());
                HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("authToken", this.loadRegisteredDevicesInput.getAuthToken())
                        .appendQueryParameter("user_id", this.loadRegisteredDevicesInput.getUser_id())
                        .appendQueryParameter("device", this.loadRegisteredDevicesInput.getDevice())
                        .appendQueryParameter("lang_code", this.loadRegisteredDevicesInput.getLang_code());
                String query = builder.build().getEncodedQuery();

                Log.v("AbhishekMishraAuth",this.loadRegisteredDevicesInput.getAuthToken());
                Log.v("AbhishekMishraUserid",this.loadRegisteredDevicesInput.getUser_id());
                Log.v("AbhishekMishraDevice",this.loadRegisteredDevicesInput.getDevice());
                Log.v("AbhishekMishraLang",this.loadRegisteredDevicesInput.getLang_code());

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                InputStream ins = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(ins);
                BufferedReader in = new BufferedReader(isr);

                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                    responseStr = inputLine;
                    Log.v("MUVISDK", "responseStr" + responseStr);

                }
                in.close();

            }
            // Execute HTTP Post Request
            catch (org.apache.http.conn.ConnectTimeoutException e) {
                Log.v("MUVISDK", "org.apache.http.conn.ConnectTimeoutException e" + e.toString());

                status = 0;
                message = "";

            } catch (IOException e) {
                Log.v("MUVISDK", "IOException" + e.toString());

                status = 0;
                message = "";
            }

            JSONObject myJson = null;
            if (responseStr != null) {
                myJson = new JSONObject(responseStr);
                status = Integer.parseInt(myJson.optString("code"));
                if (myJson.has("msg")) {
                    message = myJson.optString("msg");
                }
            }

            if (status == 200) {
                  myJson = new JSONObject(responseStr);
                JSONArray jsonMainNode = myJson.getJSONArray("device_list");

                int lengthJsonArr = jsonMainNode.length();
                for (int i = 0; i < lengthJsonArr; i++) {
                    JSONObject jsonChildNode;
                    try {
                        jsonChildNode = jsonMainNode.getJSONObject(i);
                        LoadRegisteredDevicesOutput loadRegisteredDevicesOutput = new LoadRegisteredDevicesOutput();

                        if ((jsonChildNode.has("device")) && jsonChildNode.optString("device").trim() != null && !jsonChildNode.optString("device").trim().isEmpty() && !jsonChildNode.optString("device").trim().equals("null") && !jsonChildNode.optString("device").trim().matches("")) {
                            loadRegisteredDevicesOutput.setDevice(jsonChildNode.optString("device"));

                        }
                        if ((jsonChildNode.has("device_info")) && jsonChildNode.optString("device_info").trim() != null && !jsonChildNode.optString("device_info").trim().isEmpty() && !jsonChildNode.optString("device_info").trim().equals("null") && !jsonChildNode.optString("device_info").trim().matches("")) {
                            loadRegisteredDevicesOutput.setDevice_info(jsonChildNode.optString("device_info"));
                        }
                        if ((jsonChildNode.has("flag")) && jsonChildNode.optString("flag").trim() != null && !jsonChildNode.optString("flag").trim().isEmpty() && !jsonChildNode.optString("flag").trim().equals("null") && !jsonChildNode.optString("flag").trim().matches("")) {
                            loadRegisteredDevicesOutput.setFlag(jsonChildNode.optString("flag"));
                        }


                        loadRegisteredDevicesOutputArrayList.add(loadRegisteredDevicesOutput);
                    } catch (Exception e) {
                        status = 0;
                        message = "";
                    }
                }
            }

        } catch (Exception e) {
            Log.v("MUVISDK", "Exception" + e.toString());

            status = 0;
            message = "";
        }
        return null;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onLoadRegisteredDevicesPreExecuteStarted();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        listener.onLoadRegisteredDevicesPostExecuteCompleted(loadRegisteredDevicesOutputArrayList,status,responseStr);
    }
}