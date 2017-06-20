package com.home.vod.activity;


import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.home.apisdk.apiController.ContactUsAsynTask;
import com.home.apisdk.apiModel.ContactUsInputModel;
import com.home.apisdk.apiModel.ContactUsOutputModel;
import com.home.vod.R;
import com.home.vod.util.Util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUs extends Fragment implements ContactUsAsynTask.ContactUs {
    Context context;
    String regEmailStr, regNameStr,regMessageStr;
    EditText editEmailStr, editNameStr,editMessageStr;
    TextView contactFormTitle;
    Button submit;
    String sucessMsg,statusmsg;
    String contEmail;
    ContactUsAsynTask asynContactUs;
    boolean validate = true;



    public ContactUs() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       /* getActionBar().setTitle(getArguments().getString(""));
        setHasOptionsMenu(true);*/
        View v = inflater.inflate(R.layout.fragment_contact_us, container, false);
        context = getActivity();

        TextView categoryTitle = (TextView) v.findViewById(R.id.categoryTitle);
        Typeface castDescriptionTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.regular_fonts));
        categoryTitle.setTypeface(castDescriptionTypeface);
        categoryTitle.setText(getArguments().getString("title"));

        contactFormTitle = (TextView) v.findViewById(R.id.contactFormTitle);
        Typeface contactFormTitleTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.light_fonts));
        contactFormTitle.setTypeface(contactFormTitleTypeface);
        contactFormTitle.setHint(Util.getTextofLanguage(context, Util.FILL_FORM_BELOW, Util.DEFAULT_FILL_FORM_BELOW));

        editEmailStr=(EditText) v.findViewById(R.id.contact_email) ;
        Typeface editEmailStrTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.light_fonts));
        editEmailStr.setTypeface(editEmailStrTypeface);
        editEmailStr.setHint(Util.getTextofLanguage(context, Util.TEXT_EMIAL, Util.DEFAULT_TEXT_EMIAL));

        editNameStr=(EditText) v.findViewById(R.id.contact_name) ;
        Typeface editNameStrTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.light_fonts));
        editNameStr.setTypeface(editNameStrTypeface);
        editNameStr.setHint(Util.getTextofLanguage(context, Util.NAME_HINT, Util.DEFAULT_NAME_HINT));

        editMessageStr=(EditText) v.findViewById(R.id.contact_msg) ;
        Typeface editMessageStrTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.light_fonts));
        editMessageStr.setTypeface(editMessageStrTypeface);
        editMessageStr.setHint(Util.getTextofLanguage(context, Util.MESSAGE, Util.DEFAULT_MESSAGE));

        submit = (Button) v.findViewById(R.id.submit_cont);
        Typeface submitTypeface = Typeface.createFromAsset(context.getAssets(),context.getResources().getString(R.string.regular_fonts));
        submit.setTypeface(submitTypeface);
        submit.setText(Util.getTextofLanguage(context, Util.BTN_SUBMIT, Util.DEFAULT_BTN_SUBMIT));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Submitted successfully", Toast.LENGTH_SHORT).show();

                SubmmitClicked();

            }
        });

        editNameStr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    editNameStr.setError(null);
                }
                else{
                    editNameStr.setError("Required Field . ");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editEmailStr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                   if (Util.isValidMail((editEmailStr.getText().toString().trim()))){
                       editEmailStr.setError(null);
                   }else
                   {
                       editEmailStr.setError("Invalid Email.");
                   }
                }
                else{
                    editEmailStr.setError("Required Field.");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        editMessageStr.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    editMessageStr.setError(null);
                }
                else{
                    editMessageStr.setError("Required Field.");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });







        return v;
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public void SubmmitClicked() {

        regEmailStr = editEmailStr.getText().toString().trim();
        regNameStr = editNameStr.getText().toString().trim();
        regMessageStr = editMessageStr.getText().toString().trim();


        if (regNameStr.equals("")){
            editNameStr.setError("Required Field.");
            validate=false;
        }

        if (regEmailStr.equals("")){
            editEmailStr.setError("Required Field.");
            validate=false;
        }else {
            if (Util.isValidMail((editEmailStr.getText().toString().trim()))){
                editEmailStr.setError(null);
            }else
            {
                editEmailStr.setError("Invalid Email.");
                validate=false;
            }

        }

        if (regMessageStr.equals("")){
            editMessageStr.setError("Required Field.");
            validate=false;
        }

    if (validate){

        boolean isNetwork = Util.checkNetwork(getActivity());
        if (isNetwork){
            ContactUsInputModel contactUsInputModel=new ContactUsInputModel();
            contactUsInputModel.setAuthToken(Util.authTokenStr);
            contactUsInputModel.setEmail(contactUsInputModel.getEmail());
            contactUsInputModel.setName(contactUsInputModel.getName());
            contactUsInputModel.setMessage(contactUsInputModel.getMessage());
            ContactUsAsynTask asynContactUs = new ContactUsAsynTask(contactUsInputModel, context);
            asynContactUs.execute();

        }else{
            Toast.makeText(getActivity(), Util.getTextofLanguage(getActivity(), Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
        }
    }else {
        validate=true;
        return ;
    }



//
//        boolean isNetwork = Util.checkNetwork(getActivity());
//        if (isNetwork) {
//            if (!regEmailStr.equals("")) {
//                boolean isValidEmail = Util.isValidMail(regEmailStr);
//                boolean isValidname = Util.isValidMail(regNameStr);
//                boolean isValidMessage = Util.isValidMail(regEmailStr);
//                if (isValidEmail == true) {
//
////                    Toast.makeText(getActivity(), sucessMsg, Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), Util.getTextofLanguage(getActivity(), Util.OOPS_INVALID_EMAIL, Util.DEFAULT_OOPS_INVALID_EMAIL), Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(getActivity(), Util.getTextofLanguage(getActivity(), Util.EMPTY_FIELD, Util.DEFAULT_EMPTY_FIELD), Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(getActivity(), Util.getTextofLanguage(getActivity(), Util.NO_INTERNET_CONNECTION, Util.DEFAULT_NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
//        }
//
//        return isNetwork;
    }

    @Override
    public void onContactUsPreExecuteStarted() {

    }

    @Override
    public void onContactUsPostExecuteCompleted(ContactUsOutputModel contactUsOutputModel, int code, String message, String status) {
        Toast.makeText(getActivity(), sucessMsg, Toast.LENGTH_SHORT).show();

//        try {
//            if (pDialog != null && pDialog.isShowing()) {
//                pDialog.hide();
//                pDialog = null;
//            }
//        } catch (IllegalArgumentException ex) {
//            status = 0;
//
//        }
//        if (status == 0) {
//
//        }
        editMessageStr.setText("");
        editNameStr.setText("");
        editEmailStr.setText("");
        editMessageStr.setError(null);
        editNameStr.setError(null);
        editEmailStr.setError(null);

    }


//    private class AsynContactUs extends AsyncTask<String, Void, Void> {
////    ProgressBarHandler pDialog;
//    String contName;
//    JSONObject myJson = null;
//    int status;
//
//    String contMessage;
//    String responseStr;
//
////    @Override
////    protected void onPreExecute() {
////        pDialog = new ProgressBarHandler(getActivity().getBaseContext());
////        pDialog.show();
////        Log.v("NIhar","onpreExecution");
////    }
//
//    @Override
//    protected Void doInBackground(String... params) {
//
//        String urlRouteList = Util.rootUrl().trim() + Util.ContactUs.trim();
//
//        try {
//            HttpClient httpclient = new DefaultHttpClient();
//            HttpPost httppost = new HttpPost(urlRouteList);
//            httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//            httppost.addHeader("authToken", Util.authTokenStr.trim());
//            httppost.addHeader("name", String.valueOf(regNameStr));
//            httppost.addHeader("email", String.valueOf(regEmailStr));
//            httppost.addHeader("message", String.valueOf(regMessageStr));
//            httppost.addHeader("lang_code",Util.getTextofLanguage(context,Util.SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));
//
//            try {
//                HttpResponse response = httpclient.execute(httppost);
//                responseStr = EntityUtils.toString(response.getEntity());
//
//
//            } catch (org.apache.http.conn.ConnectTimeoutException e) {
////                getActivity().runOnUiThread(new Runnable() {
////                    @Override
////                    public void run() {
////                        if (pDialog != null && pDialog.isShowing()) {
////                            pDialog.hide();
////                            pDialog = null;
////                        }
////                        status = 0;
////
////                    }
////
////                });
//            }
//        } catch (IOException e) {
////            if (pDialog != null && pDialog.isShowing()) {
////                pDialog.hide();
////                pDialog = null;
////            }
////            status = 0;
//            e.printStackTrace();
//        }
//        if (responseStr != null) {
//            try {
//                myJson = new JSONObject(responseStr);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            status = Integer.parseInt(myJson.optString("code"));
//            sucessMsg = myJson.optString("success_msg");
//            statusmsg = myJson.optString("status");
//
//
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//        Toast.makeText(getActivity(), sucessMsg, Toast.LENGTH_SHORT).show();
//
////        try {
////            if (pDialog != null && pDialog.isShowing()) {
////                pDialog.hide();
////                pDialog = null;
////            }
////        } catch (IllegalArgumentException ex) {
////            status = 0;
////
////        }
////        if (status == 0) {
////
////        }
//        editMessageStr.setText("");
//        editNameStr.setText("");
//        editEmailStr.setText("");
//        editMessageStr.setError(null);
//        editNameStr.setError(null);
//        editEmailStr.setError(null);
//
//    }
//}
}
