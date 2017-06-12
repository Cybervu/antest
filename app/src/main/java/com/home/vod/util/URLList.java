package com.home.vod.util;


import com.home.vod.BuildConfig;

/**
 * Created by MUVI on 1/18/2017.
 */

public class URLList {

    public static final String BASEURL = BuildConfig.SERVICE_BASE_PATH;


    public static String INITIALIZATION_URL = BASEURL+"initialiseSdk";
    public static String MENU_LIST_URL = BASEURL+"getMenuList";
    public static String GENRE_LIST_URL = BASEURL+"getGenreList";
    public static String GET_PROFILE_DETAILS_URL = BASEURL+"getProfileDetails";
    public static String UPDATE_PROFILE_URL = BASEURL+"updateUserProfile";
    public static String LOGOUT_URL = BASEURL+"logout";
    public static String VIDEO_DETAILS_URL = BASEURL+"getVideoDetails";
    public static String GET_CONTENT_LIST_URL = BASEURL+"getContentList";
    public static String FORGOT_PASSWORD_URL = BASEURL+"forgotPassword";
    public static String LOGIN_URL = BASEURL+"login";
    public static String REGISTER_URL = BASEURL+"registerUser";
    public static String GET_EPISODE_DETAILS_URL = BASEURL+"episodeDetails";
    public static String SEARCH_DATA_URL = BASEURL+"searchData";
    public static String CONTENT_DETAILS_URL = BASEURL+"getContentDetails";


    //New Api Added

    public static String SUBSCRIPTION_PLAN_LISTS = BASEURL+"getStudioPlanLists";
    public static String HOMEPAGE_URL = BASEURL+"homePage";
    public static String GET_FEATURE_CONTENT_URL = BASEURL+"getFeaturedContent";
    public static String GET_IMAGE_FOR_DOWNLOAD_URL = BASEURL+"GetImageForDownload";
    public static String CHECK_GEO_BLOCK_URL = BASEURL+"checkGeoBlock";
    public static String IS_REGISTRATIONENABLED_URL = BASEURL+"isRegistrationEnabled";
    public static String GETSTATICPAGES_URL = BASEURL+"getStaticPagedetails";
    public static String CONTACT_US_URL = BASEURL+"contactUs";
    public static String GET_CELIBRITY_URL = BASEURL+"getCelibrity";
    public static String PURCHASE_HISTORY_URL = BASEURL+"PurchaseHistory";
    public static String TRANSACTION_URL = BASEURL+"Transaction";
    public static String GET_INVOICE_PDF_URL = BASEURL+"GetInvoicePDF";
    public static String DELETE_INVOICE_PDF_URL = BASEURL+"DeleteInvoicePath";
    public static String GET_MONETIZATION_DETAILS_URL = BASEURL+"GetMonetizationDetails";
    public static String SOCIALAUTH_URL = BASEURL+"socialAuth";
    public static String VALIDATE_VOUCHER_URL = BASEURL+"ValidateVoucher";
    public static String GET_VOUCHER_PLAN_URL = BASEURL+"GetVoucherPlan";
    public static String VOUCHER_SUBSCRIPTION_URL = BASEURL+"VoucherSubscription";
    public static String MYLIBRARY_URL = BASEURL+"MyLibrary";
    public static String REGISTER_USER_PAYMENT_URL = BASEURL+"registerUserPayment";
    public static String AUTH_USER_PAYMENT_INFO_URL = BASEURL+"authUserPaymentInfo";
    public static String GET_CARD_LIST_FOR_PPV_URL = BASEURL+"getCardsListForPPV";
    public static String VALIDATE_COUPON_CODE_URL = BASEURL+"validateCouponCode";


    public static String IP_ADDRESS_URL = "https://api.ipify.org/?format=json";
    public static String GET_LANGUAGE_LIST_URL = BASEURL+"getLanguageList";
    public static String VIDEO_LOGS_URL = BASEURL+"videoLogs";
    public static String VIDEO_BUFFER_LOGS_URL = BASEURL+"bufferLogs";
    public static String VALIDATE_USER_FOR_CONTENT_URL = BASEURL+"isContentAuthorized";



}
