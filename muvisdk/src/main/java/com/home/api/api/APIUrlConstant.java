package com.home.api.api;

/**
 * A Class Which Contains all the apinames and their request id
 *
 * @author Abhishek
 */

public class APIUrlConstant {


    public static String BASE_URl = "https://sonydadc.muvi.com/rest/";

    //public static String BASE_URl = "https://www.muvi.com/rest/";

    /**
     * endpoint to initialize SDK API
     */
    public static final String INITIALIZATION_URL = "initialiseSdk";
    /**
     * Request id for initialize SDK API
     */
    public static final int INITIALIZATION_URL_REQUEST_ID = 1;
    /**
     * endpoint to get Menu List API
     */
    public static final String MENU_LIST_URL = "getMenuList";
    /**
     * Request id for Menu list API
     */
    public static final int MENU_LIST_URL_REQUEST_ID = 2;
    /**
     * endpoint to get Genre List API
     */
    public static final String GENRE_LIST_URL = "getGenreList";
    /**
     * Request id for Genre List API
     */
    public static final int GENRE_LIST_URL_REQUEST_ID = 3;
    /**
     * endpoint to get Profile Details API
     */
    public static final String GET_PROFILE_DETAILS_URL = "getProfileDetails";
    /**
     * Request id for Profile Details API
     */
    public static final int GET_PROFILE_DETAILS_URL_REQUEST_ID = 4;
    /**
     * endpoint to Update User Profile API
     */
    public static final String UPDATE_PROFILE_URL = "updateUserProfile";
    /**
     * Request id for Update Profile API
     */
    public static final int UPDATE_PROFILE_URL_REQUEST_ID = 5;
    /**
     * endpoint to get Logout API
     */
    public static final String LOGOUT_URL = "logout";
    /**
     * Request id for Logout API
     */
    public static final int LOGOUT_URL_REQUEST_ID = 6;
    /**
     * endpoint to get Video Details API
     */
    public static final String VIDEO_DETAILS_URL = "getVideoDetails";
    /**
     * Request id for Video details API
     */
    public static final int VIDEO_DETAILS_URL_REQUEST_ID = 7;

    /**
     * endpoint to getMonitization Details API
     */
    // public static final String GET_MONITIZATION_DETAILS = "GetMonetizationDetails";
    /**
     * Request id for Get Monitization API
     */
    //public static final int GET_MONITIZATION_DETAILS_REQUEST_ID = 8;

    /**
     * endpoint to get Content List API
     */
    public static final String GET_CONTENT_LIST_URL = "getContentList";
    /**
     * Request id for Content List API
     */
    public static final int GET_CONTENT_LIST_URL_REQUEST_ID = 9;

    public static final int LOAD_FILTER_VIDEO_REQUEST_ID = 70;
    /**
     * endpoint to get Forgot Password API
     */

    public static final String FORGOT_PASSWORD_URL = "forgotPassword";
    /**
     * Request id for Forgot Password API
     */
    public static final int FORGOT_PASSWORD_URL_REQUEST_ID = 10;
    /**
     * endpoint to get Login API
     */
    public static final String LOGIN_URL = "login";
    /**
     * Request id for Login API
     */
    public static final int LOGIN_URL_REQUEST_ID = 11;
    /**
     * endpoint to get Register User API
     */
    public static final String REGISTER_URL = "registerUser";
    /**
     * Request id for Register API
     */
    public static final int REGISTER_URL_REQUEST_ID = 12;
    /**
     * endpoint to get Episode Details API
     */
    public static final String GET_EPISODE_DETAILS_URL = "episodeDetails";
    /**
     * Request id for Episode details API
     */
    public static final int GET_EPISODE_DETAILS_URL_REQUEST_ID = 13;
    /**
     * endpoint to get Search Data API
     */
    public static final String SEARCH_DATA_URL = "searchData";
    /**
     * Request id for Search Data API
     */
    public static final int SEARCH_DATA_URL_REQUEST_ID = 14;
    /**
     * endpoint to get Content Details API
     */
    public static final String CONTENT_DETAILS_URL = "getContentDetails";
    /**
     * Request id for Content Details API
     */
    public static final int CONTENT_DETAILS_URL_REQUEST_ID = 15;


    //New Api Added

    /**
     * endpoint to get Studio PLan Lists API
     */
    public static final String SUBSCRIPTION_PLAN_LISTS = "getStudioPlanLists";
    /**
     * Request id for Studio Plan List API
     */
    public static final int SUBSCRIPTION_PLAN_LISTS_REQUEST_ID = 16;
    /**
     * endpoint to get Home Page API
     */
    // public static String HOMEPAGE_URL = "homePage";

    public static final String HOMEPAGE_URL = "getAppHomePage";
    /**
     * Request id for Home Page API
     */
    public static final int HOMEPAGE_URL_REQUEST_ID = 17;
    /**
     * endpoint to get Featured Content Details API
     */
    // public static String GET_FEATURE_CONTENT_URL = "getFeaturedContent";

    public static final String GET_FEATURE_CONTENT_URL = "getAPPFeaturedContent";
    /**
     * Request id for Featured Content Details API
     */
    public static final int GET_FEATURE_CONTENT_URL_REQUEST_ID = 18;

    /**
     * endpoint to get Image For Download API
     */

    public static final String GET_IMAGE_FOR_DOWNLOAD_URL = "GetImageForDownload";
    /**
     * Request id for Image for download API
     */
    public static final int GET_IMAGE_FOR_DOWNLOAD_URL_REQUEST_ID = 19;
    /**
     * endpoint to get Country Details API
     */
    public static final String CHECK_GEO_BLOCK_URL = "checkGeoBlock";
    /**
     * Request id for Country details API
     */
    public static final int CHECK_GEO_BLOCK_URL_REQUEST_ID = 20;
    /**
     * endpoint to get Registration Enable details API
     */
    public static final String IS_REGISTRATIONENABLED_URL = "isRegistrationEnabled";
    /**
     * Request id for Registration Enable API
     */
    public static final int IS_REGISTRATIONENABLED_URL_REQUEST_ID = 21;
    /**
     * endpoint to get Static Page Details API
     */
    public static final String GETSTATICPAGES_URL = "getStaticPagedetails";
    /**
     * Request id for static page API
     */
    public static final int GETSTATICPAGES_URL_REQUEST_ID = 22;
    /**
     * endpoint to get Contact Us API
     */
    public static final String CONTACT_US_URL = "contactUs";
    /**
     * Request id for Contact Us API
     */
    public static final int CONTACT_US_URL_REQUEST_ID = 23;
    /**
     * endpoint to get Celebrity Details API
     */
    public static final String GET_CELIBRITY_URL = "getCelibrity";
    /**
     * Request id for Celebrity Details API
     */
    public static final int GET_CELIBRITY_URL_REQUEST_ID = 24;
    /**
     * endpoint to get Purchase History Details API
     */
    public static final String PURCHASE_HISTORY_URL = "PurchaseHistory";
    /**
     * Request id for Purchase history API
     */
    public static final int PURCHASE_HISTORY_URL_REQUEST_ID = 25;
    /**
     * endpoint to get Transaction Details API
     */
    public static final String TRANSACTION_URL = "Transaction";
    /**
     * Request id for Transaction Details API
     */
    public static final int TRANSACTION_URL_REQUEST_ID = 26;
    /**
     * endpoint to get Invoice Pdf Details API
     */
    public static final String GET_INVOICE_PDF_URL = "GetInvoicePDF";
    /**
     * Request id for Invoice pdf details API
     */
    public static final int GET_INVOICE_PDF_URL_REQUEST_ID = 27;
    /**
     * endpoint to Delete Invoice Pdf Details API
     */
    public static final String DELETE_INVOICE_PDF_URL = "DeleteInvoicePath";
    /**
     * Request id for Delete Invoice PDF API
     */
    public static final int DELETE_INVOICE_PDF_URL_REQUEST_ID = 28;

    /**
     * endpoint to get Monitization Details API
     */
    public static final String GET_MONETIZATION_DETAILS_URL = "GetMonetizationDetails";
    /**
     * Request id for Monitization Details API
     */
    public static final int GET_MONETIZATION_DETAILS_URL_REQUEST_ID = 29;
    /**
     * endpoint to get Social Auth Details API
     */
    public static final String SOCIALAUTH_URL = "socialAuth";
    /**
     * Request id for Social Auth API
     */
    public static final int SOCIALAUTH_URL_REQUEST_ID = 30;
    /**
     * endpoint to get Validate Voucher Details API
     */
    public static final String VALIDATE_VOUCHER_URL = "ValidateVoucher";
    /**
     * Request id for Validate Voucher API
     */
    public static final int VALIDATE_VOUCHER_URL_REQUEST_ID = 31;
    /**
     * endpoint to get Voucher Plan Details API
     */
    public static final String GET_VOUCHER_PLAN_URL = "GetVoucherPlan";
    /**
     * Request id for Voucher Plan API
     */
    public static final int GET_VOUCHER_PLAN_URL_REQUEST_ID = 32;
    /**
     * endpoint to get Voucher Subscription Details API
     */
    public static final String VOUCHER_SUBSCRIPTION_URL = "VoucherSubscription";
    /**
     * Request id for Voucher Subscription API
     */
    public static final int VOUCHER_SUBSCRIPTION_URL_REQUEST_ID = 33;
    /**
     * endpoint to get My Library Details API
     */
    public static final String MYLIBRARY_URL = "MyLibrary";
    /**
     * Request id for My Library API
     */
    public static final int MYLIBRARY_URL_REQUEST_ID = 34;
    /**
     * endpoint to get Register User Payment Details API
     */
    public static final String REGISTER_USER_PAYMENT_URL = "registerUserPayment";
    /**
     * Request id for Register User Payment API
     */
    public static final int REGISTER_USER_PAYMENT_URL_REQUEST_ID = 35;
    /**
     * endpoint to get Auth User Payment Information Details API
     */
    public static final String AUTH_USER_PAYMENT_INFO_URL = "authUserPaymentInfo";
    /**
     * Request id for Auth User Payment API
     */
    public static final int AUTH_USER_PAYMENT_INFO_URL_REQUEST_ID = 36;
    /**
     * endpoint to get Card List For PPV Details API
     */
    public static final String GET_CARD_LIST_FOR_PPV_URL = "getCardsListForPPV";
    /**
     * Request id for Card List API
     */
    public static final int GET_CARD_LIST_FOR_PPV_URL_REQUEST_ID = 37;
    /**
     * endpoint to get Language Translation Details API
     */
    public static final String LANGUAGE_TRANSLATION = "textTranslation";
    /**
     * Request id for Language Translation API
     */
    public static final int LANGUAGE_TRANSLATION_REQUEST_ID = 38;
    /**
     * endpoint to Check Device Details API
     */
    public static final String CHECK_DEVICE = "CheckDevice";
    /**
     * Request id for Check Device API
     */
    public static final int CHECK_DEVICE_REQUEST_ID = 39;
    /**
     * endpoint to Logout All Details API
     */
    public static final String LOGOUT_ALL = "LogoutAll";
    /**
     * Request id for Logout All Details API
     */
    public static final int LOGOUT_ALL_REQUEST_ID = 40;
    /**
     * endpoint to get Facebook User Status Details API
     */
    public static final String FB_USER_EXISTS_URL = "getFbUserStatus";
    /**
     * Request id for Facebook User Status API
     */
    public static final int FB_USER_EXISTS_URL_REQUEST_ID = 41;
    /**
     * endpoint to get About Us Details API
     */
    public static final String ABOUT_US = "getStaticPagedetails";
    /**
     * Request id for About Us API
     */
    public static final int ABOUT_US_REQUEST_ID = 42;

    /**
     * endpoint to get Gmail Registration Details API
     */
    public static final String GMAIL_REG_URL = "socialAuth";
    /**
     * Request id for Gmail Registration API
     */
    public static final int GMAIL_REG_URL_REQUEST_ID = 43;

    /**
     * endpoint to get View Favorite Details API
     */
    public static final String VIEW_FAVOURITE = "ViewFavourite";
    /**
     * Request id for View Favorite API
     */
    public static final int VIEW_FAVOURITE_REQUEST_ID = 44;
    /**
     * endpoint to get Add to Favorite Details API
     */
    public static final String ADD_TO_FAV_LIST = "AddtoFavlist";
    /**
     * Request id for Add Favorite API
     */
    public static final int ADD_TO_FAV_LIST_REQUEST_ID = 45;
    /**
     * endpoint to get Delete Favorite List Details API
     */
    public static final String DELETE_FAV_LIST = "DeleteFavList";
    /**
     * Request id for Delete Favorite API
     */
    public static final int DELETE_FAV_LIST_REQUEST_ID = 46;
    /**
     * endpoint to get Menu Details API
     */
    public static final String GET_MENUS_URL = "GetMenus";
    /**
     * Request id for Get Menu Details API
     */
    public static final int GET_MENUS_URL_REQUEST_ID = 47;
    /**
     * endpoint to get Update Google Id Details API
     */
    public static final String UPDATE_GOOGLE_ID = "UpdateGoogleid";
    /**
     * Request id for Update Google ID API
     */
    public static final int UPDATE_GOOGLE_ID_REQUEST_ID = 48;
    /**
     * endpoint to get Cast Details API
     */
    public static final String GET_CAST_DETAILS = "getCastDetail";
    /**
     * Request id for Cast Details API
     */
    public static final int GET_CAST_DETAILS_REQUEST_ID = 49;
    /**
     * endpoint to get Content Rating View Details API
     */
    public static final String VIEW_CONTENT_RATING = "ViewContentRating";
    /**
     * Request id for View Content Rating API
     */
    public static final int VIEW_CONTENT_RATING_REQUEST_ID = 50;
    /**
     * endpoint to get Content Rating Add Details API
     */
    public static final String ADD_CONTENT_RATING = "AddContentRating";
    /**
     * Request id for Add Content rating API
     */
    public static final int ADD_CONTENT_RATING_REQUEST_ID = 51;
    /**
     * endpoint to get PPV Payment Details API
     */
    public static final String ADD_SUBSCRIPTION_URL = "ppvpayment";
    /**
     * Request id for PPV Payment Details API
     */
    public static final int ADD_SUBSCRIPTION_URL_REQUEST_ID = 52;

    public static final int PPV_PAYEMNT_REQUEST_ID = 64;


    /**
     * endpoint to get Manage Device Details API
     */
    public static final String MANAGE_DEVICE = "ManageDevices";
    /**
     * Request id for Manage Device API
     */
    public static final int MANAGE_DEVICE_REQUEST_ID = 53;
    /**
     * endpoint to get Validate Coupon Code Details API
     */
    public static final String VALIDATE_COUPON_CODE_URL = "validateCouponCode";
    /**
     * Request id for Validate Coupon Code API
     */
    public static final int VALIDATE_COUPON_CODE_URL_REQUEST_ID = 54;
    /**
     * endpoint to get Update Buffer Logs Details API
     */
    public static final String UPDATE_BUFFER_LOGS = "updateBufferLogs";
    /**
     * Request id for Update Buffer Log API
     */
    public static final int UPDATE_BUFFER_LOGS_REQUEST_ID = 55;
    /**
     * endpoint to get Video Buffer Logs Details API
     */
    public static final String VIDEO_BUFFER_LOGS_URL = "bufferLogs";
    /**
     * Request id for Video Buffer Log API
     */
    public static final int VIDEO_BUFFER_LOGS_URL_REQUEST_ID = 56;
    /**
     * endpoint to get Validate User For Content Details API
     */
    public static final String VALIDATE_USER_FOR_CONTENT_URL = "isContentAuthorized";
    /**
     * Request id for Validate User For Content API
     */
    public static final int VALIDATE_USER_FOR_CONTENT_URL_REQUEST_ID = 57;
    /**
     * endpoint to get IP Address Details API
     */
    public static final String IP_ADDRESS_URL = "?format=json";
    /**
     * Request id for IP Address API
     */
    public static final int IP_ADDRESS_URL_REQUEST_ID = 63;
    /**
     * endpoint to get Language LIst Details API
     */
    public static final String GET_LANGUAGE_LIST_URL = "getLanguageList";
    /**
     * Request id for Language List API
     */
    public static final int GET_LANGUAGE_LIST_URL_REQUEST_ID = 58;
    /**
     * endpoint to get Video Logs Details API
     */

    public static final String VIDEO_LOGS_URL = "VideoLogNew";
    /**
     * Request id for Video Log Details API
     */
    public static final int VIDEO_LOGS_URL_REQUEST_ID = 59;
    /**
     * endpoint to get Remove Device Details API
     */
    public static final String REMOVE_DEVICE = "RemoveDevice";
    /**
     * Request id for Remove Device API
     */
    public static final int REMOVE_DEVICE_REQUEST_ID = 60;
    /**
     * endpoint to get Check If User Is Logged In Details API
     */

    public static final String CHECK_IF_USER_LOGGED_IN = "CheckIfUserLoggedIn";
    /**
     * Request id for Check If User Is Logged in API
     */
    public static final int CHECK_IF_USER_LOGGED_IN_REQUEST_ID = 61;

    /**
     * endpoint to get all the menu lists including sub menus
     */

    public static final String GET_APP_MENU = "getAppMenu";
    /**
     * Request id for all the menu lists API
     */
    public static final int GET_APP_MENU_REQUEST_ID = 62;

    /**
     * endpoint to get all the Sub Category lists including
     */

    public static final String GetSubCategoryList = "getSubCategoryList";

    public static final String WATCH_HISTORY = "watchHistory";

    public static final int WATCH_HISTORY_REQUEST_ID = 68;


    public static String getVideoLogsUrl() {
        return BASE_URl + VIDEO_LOGS_URL;
    }

    public static String getVideoBufferLogsUrl() {
        return BASE_URl + VIDEO_BUFFER_LOGS_URL;
    }

    public static String getGetAppMenu() {
        return BASE_URl + GET_APP_MENU;
    }

}
