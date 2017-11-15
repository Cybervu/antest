package com.home.vod.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.crashlytics.android.Crashlytics;
import com.home.apisdk.apiController.GetLanguageListAsynTask;
import com.home.apisdk.apiController.GetTranslateLanguageAsync;
import com.home.apisdk.apiController.LogoutAsynctask;
import com.home.apisdk.apiController.PurchaseHistoryAsyntask;
import com.home.apisdk.apiModel.LanguageListInputModel;
import com.home.apisdk.apiModel.LanguageListOutputModel;
import com.home.apisdk.apiModel.LogoutInput;
import com.home.apisdk.apiModel.PurchaseHistoryInputModel;
import com.home.apisdk.apiModel.PurchaseHistoryOutputModel;
import com.home.vod.EpisodeListOptionMenuHandler;
import com.home.vod.R;
import com.home.vod.SearchIntentHandler;
import com.home.vod.adapter.LanguageCustomAdapter;
import com.home.vod.adapter.PurchaseHistoryAdapter;
import com.home.vod.model.PurchaseHistoryModel;
import com.home.vod.model.RecyclerItemClickListener;
import com.home.vod.network.NetworkStatus;
import com.home.vod.preferences.LanguagePreference;
import com.home.vod.preferences.PreferenceManager;
import com.home.vod.util.FontUtls;
import com.home.vod.util.LogUtil;
import com.home.vod.util.ProgressBarHandler;
import com.home.vod.util.Util;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;

import static com.home.vod.preferences.LanguagePreference.DEFAULT_IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_NO_INTERNET_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PURCHASE_HISTORY;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_TRY_AGAIN;
import static com.home.vod.preferences.LanguagePreference.IS_ONE_STEP_REGISTRATION;
import static com.home.vod.preferences.LanguagePreference.LOGOUT_SUCCESS;
import static com.home.vod.preferences.LanguagePreference.NO;
import static com.home.vod.preferences.LanguagePreference.NO_INTERNET_NO_DATA;
import static com.home.vod.preferences.LanguagePreference.PURCHASE_HISTORY;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.TRY_AGAIN;
import static com.home.vod.util.Constant.authTokenStr;

public class PurchaseHistoryActivity extends AppCompatActivity implements
        PurchaseHistoryAsyntask.PurchaseHistoryListener ,GetLanguageListAsynTask.GetLanguageListListener ,LogoutAsynctask.LogoutListener, GetTranslateLanguageAsync.GetTranslateLanguageInfoListener{
    Toolbar mActionBarToolbar;
    String email, id;
    LanguageCustomAdapter languageCustomAdapter;
    String Default_Language = "";
    String Previous_Selected_Language = "";
    private EpisodeListOptionMenuHandler episodeListOptionMenuHandler;
    int prevPosition = 0;
    String isEpisodeStr;
    AlertDialog alert;
    public static ProgressBarHandler progressBarHandler;
    RecyclerView recyclerView;
    ArrayList<PurchaseHistoryModel> purchaseData = new ArrayList<PurchaseHistoryModel>();
    LinearLayout primary_layout;
    Button tryAgainButton;
    RelativeLayout noInternet;
    RelativeLayout noData;
    boolean isNetwork;
    ProgressBarHandler pDialog;
    int corePoolSize = 60;
    int maximumPoolSize = 80;
    int keepAliveTime = 10;
    BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(maximumPoolSize);
    Executor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue);
LanguagePreference languagePreference;
    String Invoice,Id,PutrcahseDate,TranactionStatus,Ppvstatus,Amount;
    private String Currency_symbol = "";
    private String currency_code  = "";
    String user_id = "";
    TextView purchaseHistoryTitleTextView,no_internet_text,noDataTextView;
    PreferenceManager preferenceManager;
    PurchaseHistoryModel purchaseHistoryModel;
    ArrayList<String> Id_Purchase_History;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_purchase_history);
        episodeListOptionMenuHandler=new EpisodeListOptionMenuHandler(this);

        languagePreference = LanguagePreference.getLanguagePreference(PurchaseHistoryActivity.this);
        noInternet = (RelativeLayout)findViewById(R.id.noInternet);
        primary_layout = (LinearLayout)findViewById(R.id.primary_layout);
        noData = (RelativeLayout)findViewById(R.id.noData);
        noDataTextView = (TextView)  findViewById(R.id.noDataTextView);
        tryAgainButton = (Button)  findViewById(R.id.tryAgainButton);
        no_internet_text = (TextView)  findViewById(R.id.no_internet_text);
        recyclerView = (RecyclerView) findViewById(R.id.purchase_history_recyclerview);
        purchaseHistoryTitleTextView = (TextView)findViewById(R.id.purchaseHistoryTitleTextView);

        no_internet_text.setText(languagePreference.getTextofLanguage(NO_INTERNET_NO_DATA,DEFAULT_NO_INTERNET_NO_DATA));
        tryAgainButton.setText(languagePreference.getTextofLanguage(TRY_AGAIN,DEFAULT_TRY_AGAIN));

        preferenceManager = PreferenceManager.getPreferenceManager(this);
        user_id = preferenceManager.getUseridFromPref();
        FontUtls.loadFont(PurchaseHistoryActivity.this, getResources().getString(R.string.regular_fonts),purchaseHistoryTitleTextView);

        purchaseHistoryTitleTextView.setText(languagePreference.getTextofLanguage(PURCHASE_HISTORY,DEFAULT_PURCHASE_HISTORY
        ));


        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        mActionBarToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back));
        mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkStatus.getInstance().isConnected(PurchaseHistoryActivity.this))
                    GetPurchaseHistoryDetails();
                else
                {
                    noInternet.setVisibility(View.VISIBLE);
                    primary_layout.setVisibility(View.GONE);
                }
            }
        });

        GetPurchaseHistoryDetails();


     /*   for(int i = 0 ;i<20 ;i++)
        {
            PurchaseHistoryModel purchaseHistoryModel = new PurchaseHistoryModel
                    ("Invoie Data","Order "+i,"12-10-20","Success","$299","Active");
            purchaseData.add(purchaseHistoryModel);
        }

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        PurchaseHistoryAdapter purchaseHistoryAdapter = new PurchaseHistoryAdapter(PurchaseHistoryActivity.this,purchaseData);
        recyclerView.setAdapter(purchaseHistoryAdapter);*/

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(PurchaseHistoryActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click

                        final Intent detailsIntent = new Intent(PurchaseHistoryActivity.this, TransactionDetailsActivity.class);

                        detailsIntent.putExtra("id",Id_Purchase_History.get(position));
                        detailsIntent.putExtra("user_id",user_id);

                        LogUtil.showLog("MUVI","ID = "+Id_Purchase_History.get(position));
                        LogUtil.showLog("MUVI","user_id = "+user_id);

                        detailsIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(detailsIntent);
                    }
                })
        );

    }




    public void GetPurchaseHistoryDetails()
    {
        noInternet.setVisibility(View.GONE);
        primary_layout.setVisibility(View.VISIBLE);
        PurchaseHistoryInputModel purchaseHistoryInputModel=new PurchaseHistoryInputModel();
        purchaseHistoryInputModel.setUser_id(user_id);
        purchaseHistoryInputModel.setAuthToken(authTokenStr);
        purchaseHistoryInputModel.setLang_code(languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE,DEFAULT_SELECTED_LANGUAGE_CODE));
        PurchaseHistoryAsyntask asynGetPurchaseDetail = new PurchaseHistoryAsyntask(purchaseHistoryInputModel,this,this);
        asynGetPurchaseDetail.executeOnExecutor(threadPoolExecutor);
    }

    @Override
    public void onPurchaseHistoryPreExecuteStarted() {
        pDialog = new ProgressBarHandler(PurchaseHistoryActivity.this);
        pDialog.show();
    }

    @Override
    public void onPurchaseHistoryPostExecuteCompleted(ArrayList<PurchaseHistoryOutputModel> purchaseHistoryOutputModel, int status) {
        try{
            if(pDialog.isShowing())
                pDialog.hide();
        }
        catch(IllegalArgumentException ex)
        {
        }
        Id_Purchase_History = new ArrayList<>();
        if (status>0){
            if (status==200){

               if (purchaseHistoryOutputModel!=null && purchaseHistoryOutputModel.size()>0){
                   for (PurchaseHistoryOutputModel model:purchaseHistoryOutputModel
                        ) {

                       purchaseData.add(new PurchaseHistoryModel(model.getInvoice_id(), model.getId(), model.getTransaction_date(), model.getTransaction_status(), model.getAmount(), model.getStatusppv()));
                       Id_Purchase_History.add( model.getId());
                   }
               }

                if(purchaseData.size()>0){

                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(layoutManager);
                PurchaseHistoryAdapter purchaseHistoryAdapter = new PurchaseHistoryAdapter(PurchaseHistoryActivity.this,purchaseData);
                recyclerView.setAdapter(purchaseHistoryAdapter);

            }else {
                    primary_layout.setVisibility(View.GONE);

                    noData.setVisibility(View.VISIBLE);
                    noDataTextView.setText(languagePreference.getTextofLanguage(NO,DEFAULT_NO) + "  "+ languagePreference.getTextofLanguage(PURCHASE_HISTORY,DEFAULT_PURCHASE_HISTORY) );


                }
            }

        }else {
            primary_layout.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);

        }


    }

    //Asyntask for getDetails of the csat and crew members.

//    private class AsynGetPurchaseDetails extends AsyncTask<Void, Void, Void> {
//        ProgressBarHandler pDialog;
//        String responseStr = "";
//        int status;
//
//        @Override
//        protected Void doInBackground(Void... params) {
//
//            try {
//                HttpClient httpclient=new DefaultHttpClient();
//                HttpPost httppost = new HttpPost(Util.rootUrl().trim()+Util.PurchaseHistory.trim());
//                httppost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=UTF-8");
//                httppost.addHeader("authToken",Util.authTokenStr);
//                httppost.addHeader("user_id",user_id);
//                httppost.addHeader("lang_code",languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE,Util.DEFAULT_SELECTED_LANGUAGE_CODE));
//
//
//                // Execute HTTP Post Request
//                try {
//                    HttpResponse response = httpclient.execute(httppost);
//                    responseStr = EntityUtils.toString(response.getEntity());
//
//                } catch (Exception e){
//
//                }
//
//                JSONObject myJson =null;
//                if(responseStr!=null){
//                    myJson = new JSONObject(responseStr);
//                    status = Integer.parseInt(myJson.optString("code"));
//                }
//                if (status > 0) {
//                    if (status == 200) {
//                        Id_Purchase_History = new ArrayList<>();
//                        JSONArray jsonArray = myJson.getJSONArray("section");
//                        for(int i=0 ;i<jsonArray.length();i++)
//                        {
//                            Invoice = jsonArray.getJSONObject(i).optString("invoice_id");
//                            if(Invoice.equals("") || Invoice==null || Invoice.equals("null"))
//                                Invoice = "";
//                            Id = jsonArray.getJSONObject(i).optString("id");
//                            if(Id.equals("") || Id==null || Id.equals("null"))
//                                Id = "";
//
//                            Id_Purchase_History.add(Id);
//                            LogUtil.showLog("MUVI","ID =========================== "+Id);
//
//                            PutrcahseDate = jsonArray.getJSONObject(i).optString("transaction_date");
//                            if(PutrcahseDate.equals("") || PutrcahseDate==null || PutrcahseDate.equals("null"))
//                                PutrcahseDate = "";
//
//                            TranactionStatus = jsonArray.getJSONObject(i).optString("transaction_status");
//                            if(TranactionStatus.equals("") || TranactionStatus==null || TranactionStatus.equals("null"))
//                                TranactionStatus = "";
//
//                            Ppvstatus = jsonArray.getJSONObject(i).optString("statusppv");
//                            if(Ppvstatus.equals("") || Ppvstatus==null || Ppvstatus.equals("null"))
//                                Ppvstatus = "";
//
//                            Currency_symbol = (jsonArray.getJSONObject(i).optString("currency_symbol")).trim();
//                            if(Currency_symbol.equals("") || Currency_symbol==null || Currency_symbol.equals("null"))
//                                Currency_symbol = "";
//
//                            LogUtil.showLog("MUVI","currency_symbol = "+Currency_symbol);
//
//                            currency_code = jsonArray.getJSONObject(i).optString("currency_code");
//                            if(currency_code.equals("") || currency_code==null || currency_code.equals("null"))
//                                currency_code = "";
//
//                            LogUtil.showLog("MUVI","currency_code = "+currency_code);
//
//
//                            Amount = jsonArray.getJSONObject(i).optString("amount");
//                            if(Amount.equals("") || Amount==null || Amount.equals("null"))
//                                Amount = "";
//                            else{
//
//                                if(Currency_symbol.equals("") || Currency_symbol==null || Currency_symbol.trim().equals(null))
//                                {
//                                    Amount = currency_code+ " "+ Amount;
//                                }
//                                else
//                                {
//                                    Amount = Currency_symbol+ " "+ Amount;
//                                }
//                            }
//
//                            LogUtil.showLog("MUVI","amount"+ Amount);
//
//
//                            purchaseHistoryModel = new PurchaseHistoryModel(Invoice,Id,PutrcahseDate,TranactionStatus,Amount,Ppvstatus);
//                            purchaseData.add(purchaseHistoryModel);
//
//                        }
//
//
//
//                    }else{  responseStr = "0";}
//                }
//                else{
//                    responseStr = "0";
//
//                }
//            } catch (final JSONException e1) {
//                responseStr = "0";
//            }
//            catch (Exception e)
//            {
//                responseStr = "0";
//            }
//            return null;
//
//        }
//
//        protected void onPostExecute(Void result) {
//
//            try{
//                if(pDialog.isShowing())
//                    pDialog.hide();
//            }
//            catch(IllegalArgumentException ex)
//            {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        primary_layout.setVisibility(View.GONE);
//                        noInternet.setVisibility(View.VISIBLE);
//
//                    }
//
//                });
//                responseStr = "0";
//            }
//            if(responseStr == null)
//                responseStr = "0";
//
//            if((responseStr.trim().equals("0"))){
//                primary_layout.setVisibility(View.GONE);
//                noInternet.setVisibility(View.VISIBLE);
//            }else{
//                // Set the recycler adapter here.
//
//                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
//                recyclerView.setLayoutManager(layoutManager);
//                PurchaseHistoryAdapter purchaseHistoryAdapter = new PurchaseHistoryAdapter(PurchaseHistoryActivity.this,purchaseData);
//                recyclerView.setAdapter(purchaseHistoryAdapter);
//            }
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//            pDialog = new ProgressBarHandler(PurchaseHistoryActivity.this);
//            pDialog.show();
//        }
//    }

    @Override
    public void onBackPressed()
    {
        finish();
        overridePendingTransition(0, 0);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        id = preferenceManager.getUseridFromPref();
        email = preferenceManager.getEmailIdFromPref();
        episodeListOptionMenuHandler.createOptionMenu(menu,preferenceManager,languagePreference);

     /*   MenuItem favorite_menu;
        favorite_menu = menu.findItem(R.id.menu_item_favorite);
        favorite_menu.setVisible(false);*/
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                final Intent searchIntent = new SearchIntentHandler(PurchaseHistoryActivity.this).handleSearchIntent();
                searchIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(searchIntent);
                // Not implemented here
                return false;
            case R.id.action_filter:

                // Not implemented here
                return false;
            case R.id.action_login:

                Intent loginIntent = new Intent(PurchaseHistoryActivity.this, LoginActivity.class);
                player.utils.Util.check_for_subscription = 0;
                startActivity(loginIntent);
                // Not implemented here
                return false;
            case R.id.action_register:

                Intent registerIntent = new Intent(PurchaseHistoryActivity.this, RegisterActivity.class);
                player.utils.Util.check_for_subscription = 0;
                startActivity(registerIntent);
                // Not implemented here
                return false;
            case R.id.menu_item_language:

                // Not implemented here
                Default_Language = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE);
                Previous_Selected_Language = languagePreference.getTextofLanguage(SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE);

                if (com.home.vod.util.Util.languageModel!=null && com.home.vod.util.Util.languageModel.size() > 0){


                    ShowLanguagePopup();

                } else {
                    LanguageListInputModel languageListInputModel = new LanguageListInputModel();
                    languageListInputModel.setAuthToken(authTokenStr);
                    GetLanguageListAsynTask asynGetLanguageList = new GetLanguageListAsynTask(languageListInputModel, this, this);
                    asynGetLanguageList.executeOnExecutor(threadPoolExecutor);
                }
                return false;
            case R.id.menu_item_favorite:

                Intent favoriteIntent = new Intent(this, FavoriteActivity.class);
//                favoriteIntent.putExtra("EMAIL",email);
//                favoriteIntent.putExtra("LOGID",id);
                favoriteIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(favoriteIntent);
                // Not implemented here
                return false;
            case R.id.menu_item_profile:

                Intent profileIntent = new Intent(PurchaseHistoryActivity.this, ProfileActivity.class);
                profileIntent.putExtra("EMAIL", email);
                profileIntent.putExtra("LOGID", id);
                startActivity(profileIntent);
                // Not implemented here
                return false;
            case R.id.action_purchage:

                Intent purchaseintent = new Intent(PurchaseHistoryActivity.this, PurchaseHistoryActivity.class);
                startActivity(purchaseintent);
                // Not implemented here
                return false;
            case R.id.action_logout:

                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(PurchaseHistoryActivity.this, R.style.MyAlertDialogStyle);
                dlgAlert.setMessage(player.utils.Util.getTextofLanguage(PurchaseHistoryActivity.this, player.utils.Util.SIGN_OUT_WARNING, player.utils.Util.DEFAULT_SIGN_OUT_WARNING));
                dlgAlert.setTitle("");

                dlgAlert.setPositiveButton(player.utils.Util.getTextofLanguage(PurchaseHistoryActivity.this, player.utils.Util.YES, player.utils.Util.DEFAULT_YES), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        // dialog.cancel();
                        LogoutInput logoutInput = new LogoutInput();
                        logoutInput.setAuthToken(authTokenStr);
                        logoutInput.setLogin_history_id(preferenceManager.getLoginHistIdFromPref());
                        logoutInput.setLang_code(player.utils.Util.getTextofLanguage(PurchaseHistoryActivity.this, player.utils.Util.SELECTED_LANGUAGE_CODE, DEFAULT_SELECTED_LANGUAGE_CODE));
                        LogoutAsynctask asynLogoutDetails = new LogoutAsynctask(logoutInput, PurchaseHistoryActivity.this, PurchaseHistoryActivity.this);
                        asynLogoutDetails.executeOnExecutor(threadPoolExecutor);


                        dialog.dismiss();
                    }
                });

                dlgAlert.setNegativeButton(player.utils.Util.getTextofLanguage(PurchaseHistoryActivity.this, player.utils.Util.NO, player.utils.Util.DEFAULT_NO), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });
                // dlgAlert.setPositiveButton(getResources().getString(R.string.yes_str), null);
                dlgAlert.setCancelable(false);

                dlgAlert.create().show();

                return false;
            default:
                break;
        }

        return false;
    }


    public void ShowLanguagePopup() {

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(PurchaseHistoryActivity.this, R.style.MyAlertDialogStyle);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View convertView = inflater.inflate(R.layout.language_pop_up, null);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.languagePopupTitle);
        titleTextView.setText(player.utils.Util.getTextofLanguage(PurchaseHistoryActivity.this, player.utils.Util.APP_SELECT_LANGUAGE, player.utils.Util.DEFAULT_APP_SELECT_LANGUAGE));

        alertDialog.setView(convertView);
        alertDialog.setTitle("");

        RecyclerView recyclerView = (RecyclerView) convertView.findViewById(R.id.language_recycler_view);
        Button apply = (Button) convertView.findViewById(R.id.apply_btn);
        apply.setText(player.utils.Util.getTextofLanguage(PurchaseHistoryActivity.this, player.utils.Util.BUTTON_APPLY, player.utils.Util.DEFAULT_BUTTON_APPLY));

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        //  languageCustomAdapter = new LanguageCustomAdapter(DigiOsmosisDownloads.this, Util.languageModel);
        // Util.languageModel.get(0).setSelected(true);
      /*  if (Util.languageModel.get(i).getLanguageId().equalsIgnoreCase(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE))) {
            prevPosition = i;
            Util.languageModel.get(i).setSelected(true);

        }
        Util.languageModel.get(0).setSelected(true);*/

        recyclerView.setAdapter(languageCustomAdapter);



    /*    for (int i = 0 ; i < Util.languageModel.size() - 1 ; i ++){
                if (Util.languageModel.get(i).getLanguageId().equalsIgnoreCase(Util.getTextofLanguage(MovieDetailsActivity.this, Util.SELECTED_LANGUAGE_CODE, Util.DEFAULT_SELECTED_LANGUAGE_CODE))) {
                    prevPosition = i;
                    Util.languageModel.get(i).setSelected(true);
                    break;

            }else {
                prevPosition = 0;

                Util.languageModel.get(0).setSelected(true);
                break;

            }
        }
*/
        recyclerView.addOnItemTouchListener(new MovieDetailsActivity.RecyclerTouchListener1(PurchaseHistoryActivity.this, recyclerView, new MovieDetailsActivity.ClickListener1() {
            @Override
            public void onClick(View view, int position) {
                player.utils.Util.itemclicked = true;

                player.utils.Util.languageModel.get(position).setSelected(true);


                if (prevPosition != position) {
                    player.utils.Util.languageModel.get(prevPosition).setSelected(false);
                    prevPosition = position;

                }

                Default_Language = player.utils.Util.languageModel.get(position).getLanguageId();


                player.utils.Util.setLanguageSharedPrefernce(PurchaseHistoryActivity.this, player.utils.Util.SELECTED_LANGUAGE_CODE, player.utils.Util.languageModel.get(position).getLanguageId());
                languageCustomAdapter.notifyDataSetChanged();

                // Default_Language = Util.languageModel.get(position).getLanguageId();
             /*   AsynGetTransalatedLanguage asynGetTransalatedLanguage = new AsynGetTransalatedLanguage();
                asynGetTransalatedLanguage.executeOnExecutor(threadPoolExecutor);*/


                // new LanguageAsyncTask(new Get).executeOnExecutor(threadPoolExecutor);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();


                if (!Previous_Selected_Language.equals(Default_Language)) {



                    LanguageListInputModel languageListInputModel = new LanguageListInputModel();
                    languageListInputModel.setAuthToken(authTokenStr);
                    languageListInputModel.setLangCode(Default_Language);

                    GetTranslateLanguageAsync getTranslateLanguageAsync = new GetTranslateLanguageAsync(languageListInputModel,PurchaseHistoryActivity.this,PurchaseHistoryActivity.this);
                    getTranslateLanguageAsync.executeOnExecutor(threadPoolExecutor);

                }

            }
        });


        alert = alertDialog.show();


        alert.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                player.utils.Util.setLanguageSharedPrefernce(PurchaseHistoryActivity.this, player.utils.Util.SELECTED_LANGUAGE_CODE, Previous_Selected_Language);
            }
        });

    }

    @Override
    public void onGetLanguageListPreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(PurchaseHistoryActivity.this);
        progressBarHandler.show();
    }

    @Override
    public void onGetLanguageListPostExecuteCompleted(ArrayList<LanguageListOutputModel> languageListOutputArray, int status, String message, String defaultLanguage) {
        {

            if (progressBarHandler.isShowing()) {
                progressBarHandler.hide();
                progressBarHandler = null;

            }
            if (status > 0 && status == 200) {
                ShowLanguagePopup();
            }
        }
    }

    @Override
    public void onGetTranslateLanguagePreExecuteStarted() {
        progressBarHandler = new ProgressBarHandler(PurchaseHistoryActivity.this);
        progressBarHandler.show();
    }

    @Override
    public void onGetTranslateLanguagePostExecuteCompleted(String jsonResponse, int status) {

    }

    @Override
    public void onLogoutPreExecuteStarted() {
        pDialog = new ProgressBarHandler(PurchaseHistoryActivity.this);
        pDialog.show();
    }

    @Override
    public void onLogoutPostExecuteCompleted(int code, String status, String message) {

        if (status == null) {
            Toast.makeText(PurchaseHistoryActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code == 0) {
            Toast.makeText(PurchaseHistoryActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

        }
        if (code > 0) {
            if (code == 200) {
                preferenceManager.clearLoginPref();
                if ((languagePreference.getTextofLanguage(IS_ONE_STEP_REGISTRATION,DEFAULT_IS_ONE_STEP_REGISTRATION)
                        .trim()).equals("1")) {
                    final Intent startIntent = new Intent(PurchaseHistoryActivity.this, SplashScreen.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(PurchaseHistoryActivity.this, languagePreference.getTextofLanguage(LOGOUT_SUCCESS, DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                } else {
                    final Intent startIntent = new Intent(PurchaseHistoryActivity.this, MainActivity.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(startIntent);
                            Toast.makeText(PurchaseHistoryActivity.this, languagePreference.getTextofLanguage(LOGOUT_SUCCESS,DEFAULT_LOGOUT_SUCCESS), Toast.LENGTH_LONG).show();
                            finish();

                        }
                    });
                }

            } else {
                Toast.makeText(PurchaseHistoryActivity.this, languagePreference.getTextofLanguage(SIGN_OUT_ERROR, DEFAULT_SIGN_OUT_ERROR), Toast.LENGTH_LONG).show();

            }
        }

    }



    public static class RecyclerTouchListener1 implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener1 clickListener;

        public RecyclerTouchListener1(Context context, final RecyclerView recyclerView, final ClickListener1 clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }



    public interface ClickListener1 {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }



}
