package com.home.vod.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.home.apisdk.apiModel.APVModel;
import com.home.apisdk.apiModel.CurrencyModel;
import com.home.apisdk.apiModel.PPVModel;
import com.home.vod.QueueDataProvider;
import com.home.vod.R;
import com.home.vod.activity.SplashScreen;
import com.home.vod.expandedcontrols.ExpandedControlsActivity;
import com.home.vod.model.DataModel;
import com.home.vod.model.LanguageModel;
import com.home.vod.preferences.LanguagePreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.home.vod.preferences.LanguagePreference.*;
import static com.home.vod.preferences.LanguagePreference.ALREADY_PURCHASE_THIS_CONTENT;
import static com.home.vod.preferences.LanguagePreference.APP_ON;
import static com.home.vod.preferences.LanguagePreference.APP_SELECT_LANGUAGE;
import static com.home.vod.preferences.LanguagePreference.BTN_SUBMIT;
import static com.home.vod.preferences.LanguagePreference.CARD_WILL_CHARGE;
import static com.home.vod.preferences.LanguagePreference.CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY;
import static com.home.vod.preferences.LanguagePreference.CROSSED_MAXIMUM_LIMIT;
import static com.home.vod.preferences.LanguagePreference.DEFAULT_PURCHASE;
import static com.home.vod.preferences.LanguagePreference.FILL_FORM_BELOW;
import static com.home.vod.preferences.LanguagePreference.GEO_BLOCKED_ALERT;
import static com.home.vod.preferences.LanguagePreference.LOGIN_STATUS_MESSAGE;
import static com.home.vod.preferences.LanguagePreference.MESSAGE;
import static com.home.vod.preferences.LanguagePreference.NO_CONTENT;
import static com.home.vod.preferences.LanguagePreference.PURCHASE;
import static com.home.vod.preferences.LanguagePreference.PURCHASE_SUCCESS_ALERT;
import static com.home.vod.preferences.LanguagePreference.SEARCH_HINT;
import static com.home.vod.preferences.LanguagePreference.SELECTED_LANGUAGE_CODE;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_ALERT;
import static com.home.vod.preferences.LanguagePreference.SIGN_OUT_ERROR;
import static com.home.vod.preferences.LanguagePreference.SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE;
import static com.home.vod.preferences.LanguagePreference.SLOW_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.SLOW_ISSUE_INTERNET_CONNECTION;
import static com.home.vod.preferences.LanguagePreference.SORT_BY;
import static com.home.vod.preferences.LanguagePreference.STORY_TITLE;
import static com.home.vod.preferences.LanguagePreference.TERMS;
import static com.home.vod.preferences.LanguagePreference.TRANASCTION_DETAIL;
import static com.home.vod.preferences.LanguagePreference.TRANSACTION;
import static com.home.vod.preferences.LanguagePreference.TRANSACTION_DATE;
import static com.home.vod.preferences.LanguagePreference.TRANSACTION_STATUS;
import static com.home.vod.preferences.LanguagePreference.TRY_AGAIN;
import static com.home.vod.preferences.LanguagePreference.UNPAID;
import static com.home.vod.preferences.LanguagePreference.UPDATE_PROFILE;
import static com.home.vod.preferences.LanguagePreference.UPDATE_PROFILE_ALERT;
import static com.home.vod.preferences.LanguagePreference.USE_NEW_CARD;
import static com.home.vod.preferences.LanguagePreference.VIDEO_ISSUE;
import static com.home.vod.preferences.LanguagePreference.VIEW_MORE;
import static com.home.vod.preferences.LanguagePreference.VIEW_TRAILER;
import static com.home.vod.preferences.LanguagePreference.WATCH;
import static com.home.vod.preferences.LanguagePreference.WATCH_NOW;
import static com.home.vod.preferences.LanguagePreference.YES;

/**
 * Created by User on 24-07-2015.
 */
public class Util {


   public static boolean drawer_line_visibility = true;
   public static boolean itemclicked = false;
   public static String DEFAULT_IS_ONE_STEP_REGISTRATION = "0";
    public static final String UpdateGoogleid = "UpdateGoogleid";


   public static PPVModel ppvModel = null;
   public static APVModel apvModel = null;
   public static CurrencyModel currencyModel = null;
   public static DataModel dataModel = null;
   public static ArrayList<LanguageModel> languageModel = null;

   public static boolean goToLibraryplayer = false;
   public static boolean my_library_visibility = false;

   public static ArrayList<Integer> image_orentiation = new ArrayList<>();

   public static String GOOGLE_FCM_TOKEN = "GOOGLE_FCM_TOKEN";
   public static String DEFAULT_GOOGLE_FCM_TOKEN = "0";
    public static boolean favorite_clicked = false;

   public static int check_for_subscription = 0;

   public static String selected_season_id = "0";
   public static String selected_episode_id = "0";

   public static String VideoResolution = "Auto";
   public static String DefaultSubtitle = "Off";
   public static boolean player_description = true;
   public static boolean landscape = true;

   public static boolean hide_pause = false;
   public static boolean call_finish_at_onUserLeaveHint = true;

   public static String Dwonload_pdf_rootUrl = "https://www.muvi.com/docs/";

   /*public static boolean checkNetwork(Context context) {
      ConnectivityManager cm =
              (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
      boolean isConnected = activeNetwork != null &&
              activeNetwork.isConnectedOrConnecting();
      if (isConnected == false) {
         return false;
      }
      return true;
   }*/


   //Array Contains a string
   public static boolean containsString(String[] list, String str) {
       return Arrays.asList(list).contains(str);
   }


   /*public static String rootUrl() {
      //String rootUrl = "https://sonydadc.muvi.com/rest/";
//        String rootUrl = "http://muvistudio.edocent.com/rest/";
      String rootUrl = "https://www.muvi.com/rest/";
//        String rootUrl = "https://www.idogic.com/rest/";
      return rootUrl;

   }*/


   public static int isDouble(String str) {
      Double d = Double.parseDouble(str);
      int i = d.intValue();
      return i;
   }

   public static String formateDateFromstring(String inputFormat, String outputFormat, String inputDate) {

      Date parsed = null;
      String outputDate = "";

      SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
      SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());

      try {

         parsed = df_input.parse(inputDate);
         outputDate = df_output.format(parsed);

      } catch (ParseException e) {
         e.printStackTrace();
         outputDate = "";
      }
      return outputDate;

   }

   public static long calculateDays(Date dateEarly, Date dateLater) {
      return (dateLater.getTime() - dateEarly.getTime()) / (24 * 60 * 60 * 1000);
   }

   //Email Validation for login
   public static boolean isValidMail(String email2) {
      boolean check;
      Pattern p;
      Matcher m;
      String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
              + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
      p = Pattern.compile(EMAIL_STRING);
      m = p.matcher(email2);
      check = m.matches();
      if (!check) {
      }
      return check;
   }

   public static boolean isConfirmPassword(String password, String confirmPassword) {
      Pattern pattern = Pattern.compile(password, Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(confirmPassword);

       return matcher.matches();
   }

   public static boolean containsIgnoreCase(List<Integer> list, int soughtFor) {
      for (Integer current : list) {
         if (current == soughtFor) {
            return true;
         }
      }
      return false;
   }




  /* public static SharedPreferences getLanguageSharedPrefernce(Context context) {
      SharedPreferences languageSharedPref = context.getSharedPreferences(Util.LANGUAGE_SHARED_PRE, 0); // 0 - for private mode
      return languageSharedPref;
   }

   public static void setLanguageSharedPrefernce(Context context, String Key, String Value) {
      SharedPreferences languageSharedPref = context.getSharedPreferences(Util.LANGUAGE_SHARED_PRE, 0); // 0 - for private mode
      SharedPreferences.Editor editor = languageSharedPref.edit();
      editor.putString(Key, Value);
      editor.commit();
   }*/

   /*public static String getTextofLanguage(Context context, String tempKey, String defaultValue) {
      SharedPreferences sp = Util.getLanguageSharedPrefernce(context);
      String str = sp.getString(tempKey, defaultValue);
      return str;
   }
*/




   /*****************
    * chromecvast*-------------------------------------
    */
   public static void showQueuePopup(final Context context, View view, final MediaInfo mediaInfo) {
      CastSession castSession =
              CastContext.getSharedInstance(context).getSessionManager().getCurrentCastSession();
      if (castSession == null || !castSession.isConnected()) {
         return;
      }
      final RemoteMediaClient remoteMediaClient = castSession.getRemoteMediaClient();
      if (remoteMediaClient == null) {
         return;
      }
      final QueueDataProvider provider = QueueDataProvider.getInstance(context);
      PopupMenu popup = new PopupMenu(context, view);
      popup.getMenuInflater().inflate(
              provider.isQueueDetached() || provider.getCount() == 0
                      ? R.menu.detached_popup_add_to_queue
                      : R.menu.popup_add_to_queue, popup.getMenu());
      PopupMenu.OnMenuItemClickListener clickListener = new PopupMenu.OnMenuItemClickListener() {
         @Override
         public boolean onMenuItemClick(MenuItem menuItem) {
            QueueDataProvider provider = QueueDataProvider.getInstance(context);
            MediaQueueItem queueItem = new MediaQueueItem.Builder(mediaInfo).setAutoplay(
                    true).setPreloadTime(Constant.PRELOAD_TIME_S).build();
            MediaQueueItem[] newItemArray = new MediaQueueItem[]{queueItem};
            String toastMessage = null;
            if (provider.isQueueDetached() && provider.getCount() > 0) {
               if ((menuItem.getItemId() == R.id.action_play_now)) {
                  MediaQueueItem[] items = Util
                          .rebuildQueueAndAppend(provider.getItems(), queueItem);
                  remoteMediaClient.queueLoad(items, provider.getCount(),
                          MediaStatus.REPEAT_MODE_REPEAT_OFF, null);
               } else {
                  return false;
               }
            } else {
               if (provider.getCount() == 0) {
                  remoteMediaClient.queueLoad(newItemArray, 0,
                          MediaStatus.REPEAT_MODE_REPEAT_OFF, null);
               } else {
                  int currentId = provider.getCurrentItemId();
                  if (menuItem.getItemId() == R.id.action_play_now) {
                     remoteMediaClient.queueInsertAndPlayItem(queueItem, currentId, null);
                  } else {
                     return false;
                  }
               }
            }
            if (menuItem.getItemId() == R.id.action_play_now) {
               Intent intent = new Intent(context, ExpandedControlsActivity.class);
               context.startActivity(intent);
            }
            if (!TextUtils.isEmpty(toastMessage)) {
               Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
            }
            return true;
         }
      };
      popup.setOnMenuItemClickListener(clickListener);
      popup.show();
   }

   public static MediaQueueItem[] rebuildQueue(List<MediaQueueItem> items) {
      if (items == null || items.isEmpty()) {
         return null;
      }
      MediaQueueItem[] rebuiltQueue = new MediaQueueItem[items.size()];
      for (int i = 0; i < items.size(); i++) {
         rebuiltQueue[i] = rebuildQueueItem(items.get(i));
      }

      return rebuiltQueue;
   }

   public static MediaQueueItem[] rebuildQueueAndAppend(List<MediaQueueItem> items,
                                                        MediaQueueItem currentItem) {
      if (items == null || items.isEmpty()) {
         return new MediaQueueItem[]{currentItem};
      }
      MediaQueueItem[] rebuiltQueue = new MediaQueueItem[items.size() + 1];
      for (int i = 0; i < items.size(); i++) {
         rebuiltQueue[i] = rebuildQueueItem(items.get(i));
      }
      rebuiltQueue[items.size()] = currentItem;

      return rebuiltQueue;
   }

   public static MediaQueueItem rebuildQueueItem(MediaQueueItem item) {
      return new MediaQueueItem.Builder(item).clearItemId().build();
   }

    /**
     * Method to convert hr to sec.
     * @param time
     * @return
     */
   public static int HoursToSecond(String time) {

      StringTokenizer tokens = new StringTokenizer(time, ":");
      int hour = Integer.parseInt(tokens.nextToken()) * 3600;
      int minute = Integer.parseInt(tokens.nextToken()) * 60;
      int second = Integer.parseInt(tokens.nextToken()) + hour + minute;
      return second;
   }


   /**
    * Method to get display size of device.
    * @param context
    * @return
    */
   public static Point getDisplaySize(Context context) {
      WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
      Display display = wm.getDefaultDisplay();
      int width = display.getWidth();
      int height = display.getHeight();
      return new Point(width, height);
   }

   /**
    * Method to check portrait orientation.
    * @param context
    * @return
    */
   public static boolean isOrientationPortrait(Context context) {
      return context.getResources().getConfiguration().orientation
              == Configuration.ORIENTATION_PORTRAIT;
   }

   public static String getAppVersionName(Context context) {
      String versionString = null;
      try {
         PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                 0 /* basic info */);
         versionString = info.versionName;
      } catch (Exception e) {
         // do nothing
      }
      return versionString;
   }



   /**
    * Show toast message
    *
    * @param mContext
    * @param message
    */
   public static void showToast(Context mContext, String message) {
      Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
   }


   /**
    * @param mContext Method to show no video available alert.
    * @auther alok
    */
   public static void showNoDataAlert(Context mContext) {
      LanguagePreference languagePreference = LanguagePreference.getLanguagePreference(mContext);
      AlertDialog.Builder dlgAlert = new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle);
      dlgAlert.setMessage(languagePreference.getTextofLanguage(NO_VIDEO_AVAILABLE, DEFAULT_NO_VIDEO_AVAILABLE));
      dlgAlert.setTitle(languagePreference.getTextofLanguage( SORRY, DEFAULT_SORRY));
      dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK), null);
      dlgAlert.setCancelable(false);
      dlgAlert.setPositiveButton(languagePreference.getTextofLanguage(BUTTON_OK, DEFAULT_BUTTON_OK),
              new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                 }
              });
      dlgAlert.create().show();
   }


   public static void printMD5Key(Context mContext){

      try {
         PackageInfo info = mContext.getPackageManager().getPackageInfo(
                 "com.release.cmaxtv",  // replace with your unique package name
                 PackageManager.GET_SIGNATURES);
         for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            //  Log.v("MUVI:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

         }
      } catch (PackageManager.NameNotFoundException e) {

      } catch (NoSuchAlgorithmException e) {

      }
   }

   /**
    * Parse language key and store in prefernces.
    * @param languagePreference
    * @param jsonResponse
    * @param default_Language
    * @throws JSONException
    */

   public static void parseLanguage(LanguagePreference languagePreference, String jsonResponse,String default_Language) throws JSONException {
         JSONObject json = new JSONObject(jsonResponse);


         languagePreference.setLanguageSharedPrefernce( ALREADY_MEMBER, json.optString("already_member").trim());
         languagePreference.setLanguageSharedPrefernce( ACTIAVTE_PLAN_TITLE, json.optString("activate_plan_title").trim());
         languagePreference.setLanguageSharedPrefernce( TRANSACTION_STATUS_ACTIVE, json.optString("transaction_status_active").trim());
         languagePreference.setLanguageSharedPrefernce( ADD_TO_FAV, json.optString("add_to_fav").trim());
         languagePreference.setLanguageSharedPrefernce( ADDED_TO_FAV, json.optString("added_to_fav").trim());
         languagePreference.setLanguageSharedPrefernce( ENTER_EMPTY_FIELD, json.optString("enter_register_fields_data").trim());

         languagePreference.setLanguageSharedPrefernce( ADVANCE_PURCHASE, json.optString("advance_purchase").trim());
         languagePreference.setLanguageSharedPrefernce( ALERT, json.optString("alert").trim());
         languagePreference.setLanguageSharedPrefernce( EPISODE_TITLE, json.optString("episodes_title").trim());
         languagePreference.setLanguageSharedPrefernce( SORT_ALPHA_A_Z, json.optString("sort_alpha_a_z").trim());
         languagePreference.setLanguageSharedPrefernce( SORT_ALPHA_Z_A, json.optString("sort_alpha_z_a").trim());

         languagePreference.setLanguageSharedPrefernce( AMOUNT, json.optString("amount").trim());
         languagePreference.setLanguageSharedPrefernce( LanguagePreference.COUPON_CANCELLED, json.optString("coupon_cancelled").trim());
         languagePreference.setLanguageSharedPrefernce( BUTTON_APPLY, json.optString("btn_apply").trim());
         languagePreference.setLanguageSharedPrefernce( SIGN_OUT_WARNING, json.optString("sign_out_warning").trim());
         languagePreference.setLanguageSharedPrefernce( DISCOUNT_ON_COUPON, json.optString("discount_on_coupon").trim());
         languagePreference.setLanguageSharedPrefernce( MY_LIBRARY, json.optString("my_library").trim());
         languagePreference.setLanguageSharedPrefernce( CREDIT_CARD_CVV_HINT, json.optString("credit_card_cvv_hint").trim());
         languagePreference.setLanguageSharedPrefernce( CAST, json.optString("cast").trim());
         languagePreference.setLanguageSharedPrefernce( CAST_CREW_BUTTON_TITLE, json.optString("cast_crew_button_title").trim());
         languagePreference.setLanguageSharedPrefernce( CENSOR_RATING, json.optString("censor_rating").trim());
         if (json.optString("change_password").trim() == null || json.optString("change_password").trim().equals("")) {
            languagePreference.setLanguageSharedPrefernce( CHANGE_PASSWORD, DEFAULT_CHANGE_PASSWORD);
         } else {
            languagePreference.setLanguageSharedPrefernce( CHANGE_PASSWORD, json.optString("change_password").trim());
         }
         languagePreference.setLanguageSharedPrefernce( CANCEL_BUTTON, json.optString("btn_cancel").trim());
         languagePreference.setLanguageSharedPrefernce( RESUME_MESSAGE, json.optString("resume_watching").trim());
         languagePreference.setLanguageSharedPrefernce( CONTINUE_BUTTON, json.optString("continue").trim());


         languagePreference.setLanguageSharedPrefernce( CONFIRM_PASSWORD, json.optString("confirm_password").trim());
         languagePreference.setLanguageSharedPrefernce( CREDIT_CARD_DETAILS, json.optString("credit_card_detail").trim());
         languagePreference.setLanguageSharedPrefernce( DIRECTOR, json.optString("director").trim());
         languagePreference.setLanguageSharedPrefernce( DOWNLOAD_BUTTON_TITLE, json.optString("download_button_title").trim());
         languagePreference.setLanguageSharedPrefernce( DESCRIPTION, json.optString("description").trim());
         languagePreference.setLanguageSharedPrefernce( HOME, json.optString("home").trim());

         languagePreference.setLanguageSharedPrefernce( EMAIL_EXISTS, json.optString("email_exists").trim());
         languagePreference.setLanguageSharedPrefernce( EMAIL_DOESNOT_EXISTS, json.optString("email_does_not_exist").trim());
         languagePreference.setLanguageSharedPrefernce( EMAIL_PASSWORD_INVALID, json.optString("email_password_invalid").trim());
         languagePreference.setLanguageSharedPrefernce( COUPON_CODE_HINT, json.optString("coupon_code_hint").trim());
         languagePreference.setLanguageSharedPrefernce( SEARCH_ALERT, json.optString("search_alert").trim());

         languagePreference.setLanguageSharedPrefernce( CREDIT_CARD_NUMBER_HINT, json.optString("credit_card_number_hint").trim());
         languagePreference.setLanguageSharedPrefernce( TEXT_EMIAL, json.optString("text_email").trim());
         languagePreference.setLanguageSharedPrefernce( NAME_HINT, json.optString("name_hint").trim());
         languagePreference.setLanguageSharedPrefernce( CREDIT_CARD_NAME_HINT, json.optString("credit_card_name_hint").trim());
         languagePreference.setLanguageSharedPrefernce( TEXT_PASSWORD, json.optString("text_password").trim());

         languagePreference.setLanguageSharedPrefernce( ERROR_IN_PAYMENT_VALIDATION, json.optString("error_in_payment_validation").trim());
         languagePreference.setLanguageSharedPrefernce( ERROR_IN_REGISTRATION, json.optString("error_in_registration").trim());
         languagePreference.setLanguageSharedPrefernce( TRANSACTION_STATUS_EXPIRED, json.optString("transaction_status_expired").trim());
         languagePreference.setLanguageSharedPrefernce( DETAILS_NOT_FOUND_ALERT, json.optString("details_not_found_alert").trim());

         languagePreference.setLanguageSharedPrefernce( FAILURE, json.optString("failure").trim());
         languagePreference.setLanguageSharedPrefernce( FILTER_BY, json.optString("filter_by").trim());
         languagePreference.setLanguageSharedPrefernce( FORGOT_PASSWORD, json.optString("forgot_password").trim());
         languagePreference.setLanguageSharedPrefernce( GENRE, json.optString("genre").trim());

         languagePreference.setLanguageSharedPrefernce( AGREE_TERMS, json.optString("agree_terms").trim());
         languagePreference.setLanguageSharedPrefernce( INVALID_COUPON, json.optString("invalid_coupon").trim());
         languagePreference.setLanguageSharedPrefernce( INVOICE, json.optString("invoice").trim());
         languagePreference.setLanguageSharedPrefernce( LANGUAGE_POPUP_LANGUAGE, json.optString("language_popup_language").trim());
         languagePreference.setLanguageSharedPrefernce( SORT_LAST_UPLOADED, json.optString("sort_last_uploaded").trim());

         languagePreference.setLanguageSharedPrefernce( LANGUAGE_POPUP_LOGIN, json.optString("language_popup_login").trim());
         languagePreference.setLanguageSharedPrefernce( LOGIN, json.optString("login").trim());
         languagePreference.setLanguageSharedPrefernce( LOGOUT, json.optString("logout").trim());
         languagePreference.setLanguageSharedPrefernce( LOGOUT_SUCCESS, json.optString("logout_success").trim());
         languagePreference.setLanguageSharedPrefernce( MY_FAVOURITE, json.optString("my_favourite").trim());

         languagePreference.setLanguageSharedPrefernce( NEW_PASSWORD, json.optString("new_password").trim());
         languagePreference.setLanguageSharedPrefernce( NEW_HERE_TITLE, json.optString("new_here_title").trim());
         languagePreference.setLanguageSharedPrefernce( NO, json.optString("no").trim());
         languagePreference.setLanguageSharedPrefernce( NO_DATA, json.optString("no_data").trim());
         languagePreference.setLanguageSharedPrefernce( NO_INTERNET_CONNECTION, json.optString("no_internet_connection").trim());
         languagePreference.setLanguageSharedPrefernce( ENTER_REGISTER_FIELDS_DATA, json.optString("enter_register_fields_data").trim());

         languagePreference.setLanguageSharedPrefernce( NO_INTERNET_NO_DATA, json.optString("no_internet_no_data").trim());
         languagePreference.setLanguageSharedPrefernce( NO_DETAILS_AVAILABLE, json.optString("no_details_available").trim());
         languagePreference.setLanguageSharedPrefernce( BUTTON_OK, json.optString("btn_ok").trim());
         languagePreference.setLanguageSharedPrefernce( OLD_PASSWORD, json.optString("old_password").trim());
         languagePreference.setLanguageSharedPrefernce( OOPS_INVALID_EMAIL, json.optString("oops_invalid_email").trim());

         languagePreference.setLanguageSharedPrefernce( ORDER, json.optString("order").trim());
         languagePreference.setLanguageSharedPrefernce( TRANSACTION_DETAILS_ORDER_ID, json.optString("transaction_detail_order_id").trim());
         languagePreference.setLanguageSharedPrefernce( PASSWORD_RESET_LINK, json.optString("password_reset_link").trim());
         languagePreference.setLanguageSharedPrefernce( PASSWORDS_DO_NOT_MATCH, json.optString("password_donot_match").trim());
         languagePreference.setLanguageSharedPrefernce( PAY_BY_PAYPAL, json.optString("pay_by_paypal").trim());

         languagePreference.setLanguageSharedPrefernce( BTN_PAYNOW, json.optString("btn_paynow").trim());
         languagePreference.setLanguageSharedPrefernce( PAY_WITH_CREDIT_CARD, json.optString("pay_with_credit_card").trim());
         languagePreference.setLanguageSharedPrefernce( PAYMENT_OPTIONS_TITLE, json.optString("payment_options_title").trim());
         languagePreference.setLanguageSharedPrefernce( PLAN_NAME, json.optString("plan_name").trim());
         languagePreference.setLanguageSharedPrefernce( ACTIVATE_SUBSCRIPTION_WATCH_VIDEO, json.optString("activate_subscription_watch_video").trim());

         languagePreference.setLanguageSharedPrefernce( COUPON_ALERT, json.optString("coupon_alert").trim());
         languagePreference.setLanguageSharedPrefernce( VALID_CONFIRM_PASSWORD, json.optString("valid_confirm_password").trim());
         languagePreference.setLanguageSharedPrefernce( PROFILE, json.optString("profile").trim());
         languagePreference.setLanguageSharedPrefernce( PROFILE_UPDATED, json.optString("profile_updated").trim());

         languagePreference.setLanguageSharedPrefernce( PURCHASE, json.optString("purchase").trim());
         languagePreference.setLanguageSharedPrefernce( TRANSACTION_DETAIL_PURCHASE_DATE, json.optString("transaction_detail_purchase_date").trim());
         languagePreference.setLanguageSharedPrefernce( PURCHASE_HISTORY, json.optString("purchase_history").trim());
         languagePreference.setLanguageSharedPrefernce( BTN_REGISTER, json.optString("btn_register").trim());
         languagePreference.setLanguageSharedPrefernce( SORT_RELEASE_DATE, json.optString("sort_release_date").trim());

         languagePreference.setLanguageSharedPrefernce( SAVE_THIS_CARD, json.optString("save_this_card").trim());
         languagePreference.setLanguageSharedPrefernce( TEXT_SEARCH_PLACEHOLDER, json.optString("text_search_placeholder").trim());
         languagePreference.setLanguageSharedPrefernce( SEASON, json.optString("season").trim());
         languagePreference.setLanguageSharedPrefernce( SELECT_OPTION_TITLE, json.optString("select_option_title").trim());
         languagePreference.setLanguageSharedPrefernce( SELECT_PLAN, json.optString("select_plan").trim());

         languagePreference.setLanguageSharedPrefernce( SIGN_UP_TITLE, json.optString("signup_title").trim());
         languagePreference.setLanguageSharedPrefernce( SLOW_INTERNET_CONNECTION, json.optString("slow_internet_connection").trim());
         languagePreference.setLanguageSharedPrefernce( SLOW_ISSUE_INTERNET_CONNECTION, json.optString("slow_issue_internet_connection").trim());
         languagePreference.setLanguageSharedPrefernce( SORRY, json.optString("sorry").trim());
         languagePreference.setLanguageSharedPrefernce( GEO_BLOCKED_ALERT, json.optString("geo_blocked_alert").trim());

         languagePreference.setLanguageSharedPrefernce( SIGN_OUT_ERROR, json.optString("sign_out_error").trim());
         languagePreference.setLanguageSharedPrefernce( ALREADY_PURCHASE_THIS_CONTENT, json.optString("already_purchase_this_content").trim());
         languagePreference.setLanguageSharedPrefernce( CROSSED_MAXIMUM_LIMIT, json.optString("crossed_max_limit_of_watching").trim());
         languagePreference.setLanguageSharedPrefernce( SORT_BY, json.optString("sort_by").trim());
         languagePreference.setLanguageSharedPrefernce( STORY_TITLE, json.optString("story_title").trim());

         languagePreference.setLanguageSharedPrefernce( BTN_SUBMIT, json.optString("btn_submit").trim());
         languagePreference.setLanguageSharedPrefernce( TRANSACTION_STATUS, json.optString("transaction_success").trim());
         languagePreference.setLanguageSharedPrefernce( VIDEO_ISSUE, json.optString("video_issue").trim());
         languagePreference.setLanguageSharedPrefernce( NO_CONTENT, json.optString("no_content").trim());
         languagePreference.setLanguageSharedPrefernce( NO_VIDEO_AVAILABLE, json.optString("no_video_available").trim());

         languagePreference.setLanguageSharedPrefernce( CONTENT_NOT_AVAILABLE_IN_YOUR_COUNTRY, json.optString("content_not_available_in_your_country").trim());
         languagePreference.setLanguageSharedPrefernce( TRANSACTION_DATE, json.optString("transaction_date").trim());
         languagePreference.setLanguageSharedPrefernce( TRANASCTION_DETAIL, json.optString("transaction_detail").trim());
         languagePreference.setLanguageSharedPrefernce( TRANSACTION_STATUS, json.optString("transaction_status").trim());
         languagePreference.setLanguageSharedPrefernce( TRANSACTION, json.optString("transaction").trim());

         languagePreference.setLanguageSharedPrefernce( TRY_AGAIN, json.optString("try_again").trim());
         languagePreference.setLanguageSharedPrefernce( UNPAID, json.optString("unpaid").trim());
         languagePreference.setLanguageSharedPrefernce( USE_NEW_CARD, json.optString("use_new_card").trim());
         languagePreference.setLanguageSharedPrefernce( VIEW_MORE, json.optString("view_more").trim());
         languagePreference.setLanguageSharedPrefernce( VIEW_TRAILER, json.optString("view_trailer").trim());

         languagePreference.setLanguageSharedPrefernce( WATCH, json.optString("watch").trim());
         languagePreference.setLanguageSharedPrefernce( WATCH_NOW, json.optString("watch_now").trim());
         languagePreference.setLanguageSharedPrefernce( SIGN_OUT_ALERT, json.optString("sign_out_alert").trim());
         languagePreference.setLanguageSharedPrefernce( UPDATE_PROFILE_ALERT, json.optString("update_profile_alert").trim());
         languagePreference.setLanguageSharedPrefernce( YES, json.optString("yes").trim());

         languagePreference.setLanguageSharedPrefernce( PURCHASE_SUCCESS_ALERT, json.optString("purchase_success_alert").trim());
         languagePreference.setLanguageSharedPrefernce( CARD_WILL_CHARGE, json.optString("card_will_charge").trim());
         languagePreference.setLanguageSharedPrefernce( SEARCH_HINT, json.optString("search_hint").trim());
         languagePreference.setLanguageSharedPrefernce( TERMS, json.optString("terms").trim());
         languagePreference.setLanguageSharedPrefernce( UPDATE_PROFILE, json.optString("btn_update_profile").trim());
         languagePreference.setLanguageSharedPrefernce( APP_ON, json.optString("app_on").trim());
         languagePreference.setLanguageSharedPrefernce( APP_SELECT_LANGUAGE, json.optString("app_select_language").trim());
         languagePreference.setLanguageSharedPrefernce( FILL_FORM_BELOW, json.optString("Fill_form_below").trim());
         languagePreference.setLanguageSharedPrefernce( MESSAGE, json.optString("text_message").trim());


         languagePreference.setLanguageSharedPrefernce( SIMULTANEOUS_LOGOUT_SUCCESS_MESSAGE, json.optString("simultaneous_logout_message").trim());
         languagePreference.setLanguageSharedPrefernce( LOGIN_STATUS_MESSAGE, json.optString("login_status_message").trim());
         languagePreference.setLanguageSharedPrefernce( FILL_FORM_BELOW, json.optString("fill_form_below").trim());
         languagePreference.setLanguageSharedPrefernce( MESSAGE, json.optString("text_message").trim());

         languagePreference.setLanguageSharedPrefernce( SELECTED_LANGUAGE_CODE, default_Language);

   }


    public static void getDPI(Context _context) {

        int density = _context.getResources().getDisplayMetrics().densityDpi;
        float density1 = _context.getResources().getDisplayMetrics().density;

        Activity act = (Activity) _context;
        Display display = act.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float dpHeight = outMetrics.heightPixels / density1;
        float dpWidth = outMetrics.widthPixels / density1;
        Log.d("Login", "height-" + dpHeight + ",Width:-" + dpWidth);
        switch (density) {
            case DisplayMetrics.DENSITY_LOW: {
                Log.d("Login", "LDPI height-" + dpHeight + ",Width:-" + dpWidth);
            }
            break;
            case DisplayMetrics.DENSITY_MEDIUM: {
                Log.d("Login", "MDPI height-" + dpHeight + ",Width:-" + dpWidth);

            }
            break;
            case DisplayMetrics.DENSITY_HIGH: {
                Log.d("Login", "HDPI height-" + dpHeight + ",Width:-" + dpWidth);

            }
            break;
            case DisplayMetrics.DENSITY_XHIGH: {
                Log.d("Login", "XHDPI height-" + dpHeight + ",Width:-" + dpWidth);

            }
            break;
            case DisplayMetrics.DENSITY_XXHIGH: {
                Log.d("Login", "XXHDPI height-" + dpHeight + ",Width:-" + dpWidth);

            }
            break;
            case DisplayMetrics.DENSITY_XXXHIGH: {
                Log.d("Login", "XXXHDPI height-" + dpHeight + ",Width:-" + dpWidth);

            }
            break;
            case DisplayMetrics.DENSITY_TV: {
                Log.d("Login", "TVDPI height-" + dpHeight + ",Width:-" + dpWidth);

            }
            break;
        }
    }
}
