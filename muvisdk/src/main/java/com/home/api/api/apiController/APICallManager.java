package com.home.api.api.apiController;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.home.api.api.APIUrlConstant;
import com.home.api.api.apiModel.AddContentRatingModel;
import com.home.api.api.apiModel.AddToFavModel;
import com.home.api.api.apiModel.AuthUserPaymentInfoModel;
import com.home.api.api.apiModel.CheckDeviceModel;
import com.home.api.api.apiModel.CheckFbUserDetailsModel;
import com.home.api.api.apiModel.CheckGeoBlockModel;
import com.home.api.api.apiModel.ContactUsModel;
import com.home.api.api.apiModel.DeleteFavModel;
import com.home.api.api.apiModel.DeleteInvoicePDFModel;
import com.home.api.api.apiModel.EpisodeDetailsModel;
import com.home.api.api.apiModel.ForgotPasswordModel;
import com.home.api.api.apiModel.GenreListModel;
import com.home.api.api.apiModel.GetAppFeauturedContentModel;
import com.home.api.api.apiModel.GetAppHomePageModel;
import com.home.api.api.apiModel.GetAppMenuModel;
import com.home.api.api.apiModel.GetCardListModel;
import com.home.api.api.apiModel.GetCastDetailsModel;
import com.home.api.api.apiModel.GetCelebrityModel;
import com.home.api.api.apiModel.GetContentDetailsList;
import com.home.api.api.apiModel.GetContentListModel;
import com.home.api.api.apiModel.GetImageForDownloadModel;
import com.home.api.api.apiModel.GetInvoicePDFModel;
import com.home.api.api.apiModel.GetLanguageListModel;
import com.home.api.api.apiModel.GetMonetizationDetailsModel;
import com.home.api.api.apiModel.GetProfileDetailsModel;
import com.home.api.api.apiModel.GetStaticPagedetailsModel;
import com.home.api.api.apiModel.GetStudioPlanListsModel;
import com.home.api.api.apiModel.GetVideoDetailsModel;
import com.home.api.api.apiModel.IPAddressModel;
import com.home.api.api.apiModel.InitializeSDKModel;

import com.home.api.api.apiModel.LoginModel;
import com.home.api.api.apiModel.LogoutModel;
import com.home.api.api.apiModel.ManageDeviceModel;
import com.home.api.api.apiModel.MenuListModel;
import com.home.api.api.apiModel.MyLibraryModel;
import com.home.api.api.apiModel.PurchaseHistory_Model;
import com.home.api.api.apiModel.RegisterUserModel;
import com.home.api.api.apiModel.RegisterUserPayment;
import com.home.api.api.apiModel.RemoveDeviceModel;
import com.home.api.api.apiModel.SearchDataModel;
import com.home.api.api.apiModel.SimultaneousLogoutModel;
import com.home.api.api.apiModel.SocialAuthDetailsModel;
import com.home.api.api.apiModel.TransactionDetailsModel;
import com.home.api.api.apiModel.TranslateLanguageModel;
import com.home.api.api.apiModel.UpdateUserProfileModel;
import com.home.api.api.apiModel.ValidateCouponCodeModel;
import com.home.api.api.apiModel.ValidateUserModel;
import com.home.api.api.apiModel.ValidateVoucherModel;
import com.home.api.api.apiModel.ViewContentRatingModel;
import com.home.api.api.apiModel.ViewFavModel;
import com.home.api.api.apiModel.VoucherPlanModel;
import com.home.api.api.apiModel.VoucherSubscriptionModel;
import com.home.api.api.apiModel.WatchHistoryModel;
import com.home.api.api.apiModel.WithoutPaymentSubscriptionModel;

import com.home.api.api.apiModel.IsRegistrationEnableModel;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class manages all the API call
 *
 * @author Abhishek
 */

public class APICallManager {

    private Gson gson;

    /**
     * Interface used to allow to run some code when get responses.
     */

    public interface ApiInterafce {
        /**
         * This method will be invoked before controller start execution.
         * This method to handle pre-execution work.
         */
        public void onTaskPreExecute(int requestID);

        /**
         * This method will be invoked after controller complete execution.
         * This method to handle post-execution work.
         *
         * @param object    Which hold the model class
         * @param requestID Request id for the API call
         * @param response  Json response
         */
        public void onTaskPostExecute(Object object, int requestID, String response);

    }

    private static String BASE_URL = "";
    private String apiName = "";
    private int requestID = 0;
    private String customBaseUrl = "";
    private HashMap<String, String> apiParameter;
    private ApiInterafce listener;

   /* public APICallManager(ApiInterafce listener,String apiName, HashMap<String, String> apiParameter, int requestID) {
        this.listener = listener;
        this.apiName = apiName;
        this.apiParameter = apiParameter;
        this.requestID = requestID;
    }*/

    /**
     * Constructor to initialise the private data members.
     *
     * @param listener      ApiInterafce
     * @param apiName       End point of an API
     * @param apiParameter  Request parameter for the API call
     * @param requestID     Request id for the API call
     * @param customBaseUrl Base URL
     */
    public APICallManager(ApiInterafce listener, String apiName, HashMap<String, String> apiParameter, int requestID, String customBaseUrl) {
        this.listener = listener;
        this.apiName = apiName;
        this.apiParameter = apiParameter;
        this.requestID = requestID;
        this.customBaseUrl = customBaseUrl;
        BASE_URL = customBaseUrl;
    }

    /**
     * A method to start API processing
     */
    public void startApiProcessing() {

        listener.onTaskPreExecute(requestID);

        if (apiName.equals("")) {
            listener.onTaskPostExecute(null, requestID, "Please provide your API name");
            return;
        }
        if (BASE_URL.equals("")) {
            listener.onTaskPostExecute(null, requestID, "Please provide your base URL");
            return;
        }

        final RetrofitApiInterface retrofitApiInterface = RetrofitApiClient.getClient(BASE_URL).create(RetrofitApiInterface.class);
        Call<ResponseBody> dyResponseBodyCall = null;


        if (BASE_URL.contains("api.ipify.org")) {
            dyResponseBodyCall = retrofitApiInterface.getIpAddress(apiParameter);
        } else {
            dyResponseBodyCall = retrofitApiInterface.dynamicApi(apiName, apiParameter);
            Log.v("Abhishek", apiName);
            Log.v("Abhishek BAseUrl", BASE_URL);
        }
       /* Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {
                                          listener.onTaskPostExecute(null,requestID,"There is some error");
                                      }
                                  },
                0,
                5000);*/


        dyResponseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                gson = new Gson();
                String responsedata = null;
                try {
                    responsedata = response.body().string();
                    Log.v("Abhishek responsedata", responsedata);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    switch (apiName) {
                        case APIUrlConstant.IS_REGISTRATIONENABLED_URL:
                            IsRegistrationEnableModel isRegistrationEnableModel = gson.fromJson(responsedata, IsRegistrationEnableModel.class);
                            listener.onTaskPostExecute(isRegistrationEnableModel, requestID, responsedata);
                            return;

                        case APIUrlConstant.ADD_TO_FAV_LIST:
                            AddToFavModel addToFavModel = gson.fromJson(responsedata, AddToFavModel.class);
                            listener.onTaskPostExecute(addToFavModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.AUTH_USER_PAYMENT_INFO_URL:
                            AuthUserPaymentInfoModel authUserPaymentInfoModel = gson.fromJson(responsedata, AuthUserPaymentInfoModel.class);
                            listener.onTaskPostExecute(authUserPaymentInfoModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.CHECK_DEVICE:
                            CheckDeviceModel checkDeviceModel = gson.fromJson(responsedata, CheckDeviceModel.class);
                            listener.onTaskPostExecute(checkDeviceModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.CHECK_GEO_BLOCK_URL:
                            CheckGeoBlockModel checkGeoBlockModel = gson.fromJson(responsedata, CheckGeoBlockModel.class);
                            listener.onTaskPostExecute(checkGeoBlockModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.CONTACT_US_URL:
                            ContactUsModel contactUsModel = gson.fromJson(responsedata, ContactUsModel.class);
                            listener.onTaskPostExecute(contactUsModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.DELETE_FAV_LIST:
                            DeleteFavModel deleteFavModel = gson.fromJson(responsedata, DeleteFavModel.class);
                            listener.onTaskPostExecute(deleteFavModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.GET_EPISODE_DETAILS_URL:
                            EpisodeDetailsModel episodeDetailsModel = gson.fromJson(responsedata, EpisodeDetailsModel.class);
                            listener.onTaskPostExecute(episodeDetailsModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.FORGOT_PASSWORD_URL:
                            ForgotPasswordModel forgotPasswordModel = gson.fromJson(responsedata, ForgotPasswordModel.class);
                            listener.onTaskPostExecute(forgotPasswordModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.GENRE_LIST_URL:
                            GenreListModel genreListModel = gson.fromJson(responsedata, GenreListModel.class);
                            listener.onTaskPostExecute(genreListModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.GET_FEATURE_CONTENT_URL:
                            GetAppFeauturedContentModel getAppFeauturedContentModel = gson.fromJson(responsedata, GetAppFeauturedContentModel.class);
                            listener.onTaskPostExecute(getAppFeauturedContentModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.HOMEPAGE_URL:
                            GetAppHomePageModel getAppHomePageModel = gson.fromJson(responsedata, GetAppHomePageModel.class);
                            listener.onTaskPostExecute(getAppHomePageModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.GET_APP_MENU:
                            GetAppMenuModel getAppMenuModel = gson.fromJson(responsedata, GetAppMenuModel.class);
                            listener.onTaskPostExecute(getAppMenuModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.GET_CARD_LIST_FOR_PPV_URL:
                            GetCardListModel getCardListModel = gson.fromJson(responsedata, GetCardListModel.class);
                            listener.onTaskPostExecute(getCardListModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.GET_CAST_DETAILS:
                            GetCastDetailsModel getCastDetailsModel = gson.fromJson(responsedata, GetCastDetailsModel.class);
                            listener.onTaskPostExecute(getCastDetailsModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.CONTENT_DETAILS_URL:
                            GetContentDetailsList getContentDetailsList = gson.fromJson(responsedata, GetContentDetailsList.class);
                            listener.onTaskPostExecute(getContentDetailsList, requestID, responsedata);
                            return;
                        case APIUrlConstant.GET_CONTENT_LIST_URL:
                            GetContentListModel getContentListModel = gson.fromJson(responsedata, GetContentListModel.class);
                            listener.onTaskPostExecute(getContentListModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.GET_IMAGE_FOR_DOWNLOAD_URL:
                            GetImageForDownloadModel getImageForDownloadModel = gson.fromJson(responsedata, GetImageForDownloadModel.class);
                            listener.onTaskPostExecute(getImageForDownloadModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.GET_LANGUAGE_LIST_URL:
                            GetLanguageListModel getLanguageListModel = gson.fromJson(responsedata, GetLanguageListModel.class);
                            listener.onTaskPostExecute(getLanguageListModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.GET_MONETIZATION_DETAILS_URL:
                            GetMonetizationDetailsModel getMonetizationDetailsModel = gson.fromJson(responsedata, GetMonetizationDetailsModel.class);
                            listener.onTaskPostExecute(getMonetizationDetailsModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.GET_PROFILE_DETAILS_URL:
                            GetProfileDetailsModel getProfileDetailsModel = gson.fromJson(responsedata, GetProfileDetailsModel.class);
                            listener.onTaskPostExecute(getProfileDetailsModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.GETSTATICPAGES_URL:
                            GetStaticPagedetailsModel getStaticPagedetailsModel = gson.fromJson(responsedata, GetStaticPagedetailsModel.class);
                            listener.onTaskPostExecute(getStaticPagedetailsModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.SUBSCRIPTION_PLAN_LISTS:
                            GetStudioPlanListsModel getStudioPlanListsModel = gson.fromJson(responsedata, GetStudioPlanListsModel.class);
                            listener.onTaskPostExecute(getStudioPlanListsModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.VIDEO_DETAILS_URL:
                            GetVideoDetailsModel getVideoDetailsModel = gson.fromJson(responsedata, GetVideoDetailsModel.class);
                            listener.onTaskPostExecute(getVideoDetailsModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.INITIALIZATION_URL:
                            InitializeSDKModel initializeSDKModel = gson.fromJson(responsedata, InitializeSDKModel.class);
                            listener.onTaskPostExecute(initializeSDKModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.IP_ADDRESS_URL:
                            IPAddressModel ipAddressModel = gson.fromJson(responsedata, IPAddressModel.class);
                            listener.onTaskPostExecute(ipAddressModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.LOGIN_URL:
                            LoginModel loginModel = gson.fromJson(responsedata, LoginModel.class);
                            listener.onTaskPostExecute(loginModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.LOGOUT_URL:
                            LogoutModel logoutModel = gson.fromJson(responsedata, LogoutModel.class);
                            listener.onTaskPostExecute(logoutModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.MANAGE_DEVICE:
                            ManageDeviceModel manageDeviceModel = gson.fromJson(responsedata, ManageDeviceModel.class);
                            listener.onTaskPostExecute(manageDeviceModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.MENU_LIST_URL:
                            MenuListModel menuListModel = gson.fromJson(responsedata, MenuListModel.class);
                            listener.onTaskPostExecute(menuListModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.REGISTER_URL:
                            RegisterUserModel registerUserModel = gson.fromJson(responsedata, RegisterUserModel.class);
                            listener.onTaskPostExecute(registerUserModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.REMOVE_DEVICE:
                            RemoveDeviceModel removeDeviceModel = gson.fromJson(responsedata, RemoveDeviceModel.class);
                            listener.onTaskPostExecute(removeDeviceModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.SEARCH_DATA_URL:
                            SearchDataModel searchDataModel = gson.fromJson(responsedata, SearchDataModel.class);
                            listener.onTaskPostExecute(searchDataModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.LANGUAGE_TRANSLATION:
                            TranslateLanguageModel translateLanguageModel = gson.fromJson(responsedata, TranslateLanguageModel.class);
                            listener.onTaskPostExecute(translateLanguageModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.UPDATE_PROFILE_URL:
                            UpdateUserProfileModel updateUserProfileModel = gson.fromJson(responsedata, UpdateUserProfileModel.class);
                            listener.onTaskPostExecute(updateUserProfileModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.VIEW_CONTENT_RATING:
                            ViewContentRatingModel viewContentRatingModel = gson.fromJson(responsedata, ViewContentRatingModel.class);
                            listener.onTaskPostExecute(viewContentRatingModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.VIEW_FAVOURITE:
                            ViewFavModel viewFavouriteModel = gson.fromJson(responsedata, ViewFavModel.class);
                            listener.onTaskPostExecute(viewFavouriteModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.VALIDATE_COUPON_CODE_URL:
                            ValidateCouponCodeModel validateCouponCodeModel = gson.fromJson(responsedata, ValidateCouponCodeModel.class);
                            listener.onTaskPostExecute(validateCouponCodeModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.VALIDATE_VOUCHER_URL:
                            ValidateVoucherModel validateVoucherModel = gson.fromJson(responsedata, ValidateVoucherModel.class);
                            listener.onTaskPostExecute(validateVoucherModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.MYLIBRARY_URL:
                            MyLibraryModel myLibraryModel = gson.fromJson(responsedata, MyLibraryModel.class);
                            listener.onTaskPostExecute(myLibraryModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.PURCHASE_HISTORY_URL:
                            PurchaseHistory_Model purchaseHistoryModel = gson.fromJson(responsedata, PurchaseHistory_Model.class);
                            listener.onTaskPostExecute(purchaseHistoryModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.GET_CELIBRITY_URL:
                            GetCelebrityModel getCelebrityModel = gson.fromJson(responsedata, GetCelebrityModel.class);
                            listener.onTaskPostExecute(getCelebrityModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.VALIDATE_USER_FOR_CONTENT_URL:
                            ValidateUserModel validateUserModel = gson.fromJson(responsedata, ValidateUserModel.class);
                            listener.onTaskPostExecute(validateUserModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.TRANSACTION_URL:
                            TransactionDetailsModel transactionDetailsModel = gson.fromJson(responsedata, TransactionDetailsModel.class);
                            listener.onTaskPostExecute(transactionDetailsModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.VOUCHER_SUBSCRIPTION_URL:
                            VoucherSubscriptionModel voucherSubscriptionModel = gson.fromJson(responsedata, VoucherSubscriptionModel.class);
                            listener.onTaskPostExecute(voucherSubscriptionModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.GET_VOUCHER_PLAN_URL:
                            VoucherPlanModel voucherPlanModel = gson.fromJson(responsedata, VoucherPlanModel.class);
                            listener.onTaskPostExecute(voucherPlanModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.ADD_CONTENT_RATING:
                            AddContentRatingModel addContentRatingModel = gson.fromJson(responsedata, AddContentRatingModel.class);
                            listener.onTaskPostExecute(addContentRatingModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.REGISTER_USER_PAYMENT_URL:
                            RegisterUserPayment registerUserPayment = gson.fromJson(responsedata, RegisterUserPayment.class);
                            listener.onTaskPostExecute(registerUserPayment, requestID, responsedata);
                            return;
                        case APIUrlConstant.DELETE_INVOICE_PDF_URL:
                            DeleteInvoicePDFModel deleteInvoicePDFModel = gson.fromJson(responsedata, DeleteInvoicePDFModel.class);
                            listener.onTaskPostExecute(deleteInvoicePDFModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.GET_INVOICE_PDF_URL:
                            GetInvoicePDFModel getInvoicePDFModel = gson.fromJson(responsedata, GetInvoicePDFModel.class);
                            listener.onTaskPostExecute(getInvoicePDFModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.LOGOUT_ALL:
                            SimultaneousLogoutModel simultaneousLogoutModel = gson.fromJson(responsedata, SimultaneousLogoutModel.class);
                            listener.onTaskPostExecute(simultaneousLogoutModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.FB_USER_EXISTS_URL:
                            CheckFbUserDetailsModel checkFbUserDetailsModel = gson.fromJson(responsedata, CheckFbUserDetailsModel.class);
                            listener.onTaskPostExecute(checkFbUserDetailsModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.SOCIALAUTH_URL:
                            SocialAuthDetailsModel socialAuthDetailsModel = gson.fromJson(responsedata, SocialAuthDetailsModel.class);
                            listener.onTaskPostExecute(socialAuthDetailsModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.ADD_SUBSCRIPTION_URL:
                            WithoutPaymentSubscriptionModel withoutPaymentSubscriptionModel = gson.fromJson(responsedata, WithoutPaymentSubscriptionModel.class);
                            listener.onTaskPostExecute(withoutPaymentSubscriptionModel, requestID, responsedata);
                            return;
                        case APIUrlConstant.WATCH_HISTORY:
                            WatchHistoryModel watchHistoryModel = gson.fromJson(responsedata, WatchHistoryModel.class);
                            listener.onTaskPostExecute(watchHistoryModel, requestID, responsedata);
                            return;

                        default:
                            listener.onTaskPostExecute(null, requestID, "There is some error");
                            break;


                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                listener.onTaskPostExecute(null, requestID, t.getMessage());
            }
        });
    }


}

