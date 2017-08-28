package com.home.vod;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;

import java.util.Arrays;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;
import static xdroid.core.Global.getResources;

/**
 * Created by BISHAL on 21-08-2017.
 */

public class RegisterUIHandler {
    private Activity context;

    public  String selected_Language_Id="", selected_Country_Id="";

    public RegisterUIHandler(Activity context){
        this.context=context;

    }
    public void setCountryList(PreferenceManager preferenceManager){


    }

}
