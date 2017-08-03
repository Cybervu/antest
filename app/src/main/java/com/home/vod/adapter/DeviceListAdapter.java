package com.home.vod.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.home.apisdk.APIUrlConstant;
import com.home.apisdk.apiController.RemoveDeviceAsynTask;
import com.home.apisdk.apiModel.RemoveDeviceInputModel;
import com.home.apisdk.apiModel.RemoveDeviceOutputModel;
import com.home.vod.R;
import com.home.vod.activity.ManageDevices;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DeviceListAdapter extends BaseAdapter implements RemoveDeviceAsynTask.RemoveDeviceListner {
    private Context mContext;
    ArrayList<String> deviceName = new ArrayList<>();
    ArrayList<String> deviceInfo = new ArrayList<>();
    ArrayList<String> deviceFlag = new ArrayList<>();

    String devie_id="";

    PreferenceManager preferenceManager;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);



    public DeviceListAdapter(Context mContext, ArrayList<String> deviceName, ArrayList<String> deviceInfo, ArrayList<String> deviceFlag) {
        this.mContext = mContext;
        this.deviceName = deviceName;
        this.deviceInfo = deviceInfo;
        this.deviceFlag = deviceFlag;
        preferenceManager = PreferenceManager.getPreferenceManager(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return deviceName.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View view;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.device_list_adapter, null);
        } else {
            view = (View) convertView;
        }

        TextView device_name = (TextView) view.findViewById(R.id.device_name);
        TextView os_version = (TextView) view.findViewById(R.id.os_version);
        TextView delete_device = (TextView) view.findViewById(R.id.delete_device);

        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), mContext.getResources().getString(R.string.regular_fonts));
        device_name.setTypeface(typeface);
        delete_device.setTypeface(typeface);


        if(deviceInfo.get(position).trim().contains(","))
        {
            String info[] = (deviceInfo.get(position).trim()).split(",");
            device_name.setText(info[0]);
            os_version.setText(info[1]);
        }

        delete_device.setText(Util.getTextofLanguage(mContext, Util.DEREGISTER,Util.DEFAULT_DEREGISTER));

        if((deviceFlag.get(position)).trim().equals("1"))
        {
            delete_device.setTextColor(mContext.getResources().getColor(R.color.disableTextColor));
            device_name.setTextColor(mContext.getResources().getColor(R.color.disableTextColor));
            os_version.setTextColor(mContext.getResources().getColor(R.color.disableTextColor));
        }


        delete_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNetwork = Util.checkNetwork(mContext);
                if(isNetwork)
                {

                    if((deviceFlag.get(position)).trim().equals("0"))
                    {
                        devie_id = deviceName.get(position);

                        RemoveDeviceInputModel removeDeviceInputModel = new RemoveDeviceInputModel();
                        removeDeviceInputModel.setAuthToken(Util.authTokenStr);
                        removeDeviceInputModel.setDevice(devie_id);
                        removeDeviceInputModel.setUser_id(preferenceManager.getUseridFromPref());
                        removeDeviceInputModel.setLang_code(Util.getTextofLanguage(mContext,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));
                        RemoveDeviceAsynTask asynGetPlanid = new RemoveDeviceAsynTask(removeDeviceInputModel, (RemoveDeviceAsynTask.RemoveDeviceListner) mContext, mContext);
                        asynGetPlanid.executeOnExecutor(threadPoolExecutor);

                      /*  AsynDeleteDevices asynDeleteDevices = new AsynDeleteDevices();
                        asynDeleteDevices.executeOnExecutor(threadPoolExecutor);*/
                    }

                }
                else {
                    Toast.makeText(mContext,Util.getTextofLanguage(mContext,Util.NO_INTERNET_CONNECTION,Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onRemoveDevicePreExecuteStarted() {

    }

    @Override
    public void onRemoveDevicePostExecuteCompleted(RemoveDeviceOutputModel removeDeviceOutputModel, int status, String message) {


        if (status==200) {
            // Show Success Message

            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(message);
            dlgAlert.setTitle(null);
            dlgAlert.setPositiveButton(Util.getTextofLanguage(mContext,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(Util.getTextofLanguage(mContext,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            Intent intent = new Intent(mContext,ManageDevices.class);
                            ((Activity)mContext).startActivity(intent);
                            ((Activity)mContext).finish();
                        }
                    });
            dlgAlert.create().show();


        }
        else
        {
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);
            dlgAlert.setMessage(message);
            dlgAlert.setTitle(Util.getTextofLanguage(mContext, Util.SORRY, Util.DEFAULT_SORRY));
            dlgAlert.setPositiveButton(Util.getTextofLanguage(mContext,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK), null);
            dlgAlert.setCancelable(false);
            dlgAlert.setPositiveButton(Util.getTextofLanguage(mContext,Util.BUTTON_OK,Util.DEFAULT_BUTTON_OK),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });
            dlgAlert.create().show();
        }

    }


}