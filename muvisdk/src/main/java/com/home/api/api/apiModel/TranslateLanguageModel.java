package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created on 2/9/2018.
 *
 * @author Abhishek
 */

public class TranslateLanguageModel {
    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("translation")
    @Expose
    private Translation translation;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setTranslation(Translation translation) {
        this.translation = translation;
    }

    public class Translation {

        @SerializedName("embed_content")
        @Expose
        private String embedContent = "";
        @SerializedName("update_message")
        @Expose
        private String updateMessage = "";
        @SerializedName("atoz")
        @Expose
        private String atoz = "";
        @SerializedName("already_member")
        @Expose
        private String alreadyMember = "";
        @SerializedName("about_us")
        @Expose
        private String aboutUs = "";
        @SerializedName("access_periods_ends")
        @Expose
        private String accessPeriodsEnds = "";
        @SerializedName("activate_plan_title")
        @Expose
        private String activatePlanTitle = "";
        @SerializedName("transaction_status_active")
        @Expose
        private String transactionStatusActive = "";
        @SerializedName("active")
        @Expose
        private String active = "";
        @SerializedName("actors")
        @Expose
        private String actors = "";
        @SerializedName("add_content")
        @Expose
        private String addContent = "";
        @SerializedName("add_to_playlist")
        @Expose
        private String addToPlaylist = "";
        @SerializedName("add_to_queue")
        @Expose
        private String addToQueue = "";
        @SerializedName("add_a_review")
        @Expose
        private String addAReview = "";
        @SerializedName("add_to_calender")
        @Expose
        private String addToCalender = "";
        @SerializedName("add_to_fav")
        @Expose
        private String addToFav = "";
        @SerializedName("added_to_queue")
        @Expose
        private String addedToQueue = "";
        @SerializedName("added_to_calender")
        @Expose
        private String addedToCalender = "";
        @SerializedName("added_to_fav")
        @Expose
        private String addedToFav = "";
        @SerializedName("address_line_1")
        @Expose
        private String addressLine1 = "";
        @SerializedName("address_line_2")
        @Expose
        private String addressLine2 = "";
        @SerializedName("alert")
        @Expose
        private String alert = "";
        @SerializedName("episodes_title")
        @Expose
        private String episodesTitle = "";
        @SerializedName("all_notification_read")
        @Expose
        private String allNotificationRead = "";
        @SerializedName("sort_alpha_a_z")
        @Expose
        private String sortAlphaAZ = "";
        @SerializedName("sort_alpha_z_a")
        @Expose
        private String sortAlphaZA = "";
        @SerializedName("already_added_playlist")
        @Expose
        private String alreadyAddedPlaylist = "";
        @SerializedName("already_added_to_queue")
        @Expose
        private String alreadyAddedToQueue = "";
        @SerializedName("amount")
        @Expose
        private String amount = "";
        @SerializedName("android_version")
        @Expose
        private String androidVersion = "";
        @SerializedName("apple_store")
        @Expose
        private String appleStore = "";
        @SerializedName("coupon_cancelled")
        @Expose
        private String couponCancelled = "";
        @SerializedName("btn_apply")
        @Expose
        private String btnApply = "";
        @SerializedName("Arabic")
        @Expose
        private String arabic = "";
        @SerializedName("confirm_delete_message")
        @Expose
        private String confirmDeleteMessage = "";
        @SerializedName("are_you_sure_delete_device")
        @Expose
        private String areYouSureDeleteDevice = "";
        @SerializedName("are_you_sure_to_delete_content")
        @Expose
        private String areYouSureToDeleteContent = "";
        @SerializedName("are_you_sure_to_delete_playlist")
        @Expose
        private String areYouSureToDeletePlaylist = "";
        @SerializedName("sign_out_warning")
        @Expose
        private String signOutWarning = "";
        @SerializedName("api_sucscription_confirm_msg")
        @Expose
        private String apiSucscriptionConfirmMsg = "";
        @SerializedName("as_actor")
        @Expose
        private String asActor = "";
        @SerializedName("auto")
        @Expose
        private String auto = "";
        @SerializedName("discount_freetrial")
        @Expose
        private String discountFreetrial = "";
        @SerializedName("discount_on_coupon")
        @Expose
        private String discountOnCoupon = "";
        @SerializedName("backorder")
        @Expose
        private String backorder = "";
        @SerializedName("book")
        @Expose
        private String book = "";
        @SerializedName("booked")
        @Expose
        private String booked = "";
        @SerializedName("booking_time")
        @Expose
        private String bookingTime = "";
        @SerializedName("booking_time_should_greater_than_current")
        @Expose
        private String bookingTimeShouldGreaterThanCurrent = "";
        @SerializedName("booking_time_required")
        @Expose
        private String bookingTimeRequired = "";
        @SerializedName("browse_music")
        @Expose
        private String browseMusic = "";
        @SerializedName("buy_now")
        @Expose
        private String buyNow = "";
        @SerializedName("chk_over_18")
        @Expose
        private String chkOver18 = "";
        @SerializedName("credit_card_cvv_hint")
        @Expose
        private String creditCardCvvHint = "";
        @SerializedName("btn_cancel")
        @Expose
        private String btnCancel = "";
        @SerializedName("cancel_subscription_plan")
        @Expose
        private String cancelSubscriptionPlan = "";
        @SerializedName("subscription_bundles_cancel")
        @Expose
        private String subscriptionBundlesCancel = "";
        @SerializedName("cancel_subscription_bundles")
        @Expose
        private String cancelSubscriptionBundles = "";
        @SerializedName("cancel_subscription_header_msg")
        @Expose
        private String cancelSubscriptionHeaderMsg = "";
        @SerializedName("canceled")
        @Expose
        private String canceled = "";
        @SerializedName("cancelled")
        @Expose
        private String cancelled = "";
        @SerializedName("castlist")
        @Expose
        private String castlist = "";
        @SerializedName("cast_crew_button_title")
        @Expose
        private String castCrewButtonTitle = "";
        @SerializedName("cast_crew_details")
        @Expose
        private String castCrewDetails = "";
        @SerializedName("cast")
        @Expose
        private String cast = "";
        @SerializedName("no_celebrity_found")
        @Expose
        private String noCelebrityFound = "";
        @SerializedName("censor_rating")
        @Expose
        private String censorRating = "";
        @SerializedName("change_country")
        @Expose
        private String changeCountry = "";
        @SerializedName("change_password")
        @Expose
        private String changePassword = "";
        @SerializedName("change_video")
        @Expose
        private String changeVideo = "";
        @SerializedName("change_profile_picture")
        @Expose
        private String changeProfilePicture = "";
        @SerializedName("choose_from_gallery")
        @Expose
        private String chooseFromGallery = "";
        @SerializedName("subscribe_btn")
        @Expose
        private String subscribeBtn = "";
        @SerializedName("click_here")
        @Expose
        private String clickHere = "";
        @SerializedName("complete_season")
        @Expose
        private String completeSeason = "";
        @SerializedName("confirm_password")
        @Expose
        private String confirmPassword = "";
        @SerializedName("confirm_email_address")
        @Expose
        private String confirmEmailAddress = "";
        @SerializedName("contact_us")
        @Expose
        private String contactUs = "";
        @SerializedName("content")
        @Expose
        private String content = "";
        @SerializedName("content_name")
        @Expose
        private String contentName = "";
        @SerializedName("content_remove_playlist")
        @Expose
        private String contentRemovePlaylist = "";
        @SerializedName("content_remove_favourite")
        @Expose
        private String contentRemoveFavourite = "";
        @SerializedName("content_not_found")
        @Expose
        private String contentNotFound = "";
        @SerializedName("content_permalink_invalid")
        @Expose
        private String contentPermalinkInvalid = "";
        @SerializedName("content_permalink_required")
        @Expose
        private String contentPermalinkRequired = "";
        @SerializedName("content_saved_to_calender")
        @Expose
        private String contentSavedToCalender = "";
        @SerializedName("content_type_permalink_required")
        @Expose
        private String contentTypePermalinkRequired = "";
        @SerializedName("continue")
        @Expose
        private String _continue = "";
        @SerializedName("resume_watching")
        @Expose
        private String resumeWatching = "";
        @SerializedName("copied")
        @Expose
        private String copied = "";
        @SerializedName("copy")
        @Expose
        private String copy = "";
        @SerializedName("coupon_already_used")
        @Expose
        private String couponAlreadyUsed = "";
        @SerializedName("coupon_apply_success")
        @Expose
        private String couponApplySuccess = "";
        @SerializedName("create_playlist")
        @Expose
        private String createPlaylist = "";
        @SerializedName("credit")
        @Expose
        private String credit = "";
        @SerializedName("credit_card_detail")
        @Expose
        private String creditCardDetail = "";
        @SerializedName("director")
        @Expose
        private String director = "";
        @SerializedName("download_button_title")
        @Expose
        private String downloadButtonTitle = "";
        @SerializedName("ppv_default")
        @Expose
        private String ppvDefault = "";
        @SerializedName("delete_btn")
        @Expose
        private String deleteBtn = "";
        @SerializedName("delete_content")
        @Expose
        private String deleteContent = "";
        @SerializedName("delivered")
        @Expose
        private String delivered = "";
        @SerializedName("deregister")
        @Expose
        private String deregister = "";
        @SerializedName("description_ppv")
        @Expose
        private String descriptionPpv = "";
        @SerializedName("description")
        @Expose
        private String description = "";
        @SerializedName("device_id_not_found")
        @Expose
        private String deviceIdNotFound = "";
        @SerializedName("device_list")
        @Expose
        private String deviceList = "";
        @SerializedName("device_name")
        @Expose
        private String deviceName = "";
        @SerializedName("device_not_registerd")
        @Expose
        private String deviceNotRegisterd = "";
        @SerializedName("device_registerd")
        @Expose
        private String deviceRegisterd = "";
        @SerializedName("device_restriction_not_enable")
        @Expose
        private String deviceRestrictionNotEnable = "";
        @SerializedName("device_added_success")
        @Expose
        private String deviceAddedSuccess = "";
        @SerializedName("device_information_not_found")
        @Expose
        private String deviceInformationNotFound = "";
        @SerializedName("device_not_found_deleted")
        @Expose
        private String deviceNotFoundDeleted = "";
        @SerializedName("directors")
        @Expose
        private String directors = "";
        @SerializedName("btn_discard")
        @Expose
        private String btnDiscard = "";
        @SerializedName("want_download_cancel")
        @Expose
        private String wantDownloadCancel = "";
        @SerializedName("download")
        @Expose
        private String download = "";
        @SerializedName("download_cancelled")
        @Expose
        private String downloadCancelled = "";
        @SerializedName("download_completed")
        @Expose
        private String downloadCompleted = "";
        @SerializedName("download_interrupted")
        @Expose
        private String downloadInterrupted = "";
        @SerializedName("save_offline_video")
        @Expose
        private String saveOfflineVideo = "";
        @SerializedName("edit_btn")
        @Expose
        private String editBtn = "";
        @SerializedName("edit_content")
        @Expose
        private String editContent = "";
        @SerializedName("edit_playlist_name")
        @Expose
        private String editPlaylistName = "";
        @SerializedName("email_exists")
        @Expose
        private String emailExists = "";
        @SerializedName("email_does_not_exist")
        @Expose
        private String emailDoesNotExist = "";
        @SerializedName("email_not_registered")
        @Expose
        private String emailNotRegistered = "";
        @SerializedName("email_password_invalid")
        @Expose
        private String emailPasswordInvalid = "";
        @SerializedName("embed")
        @Expose
        private String embed = "";
        @SerializedName("embed_url")
        @Expose
        private String embedUrl = "";
        @SerializedName("embed_from_3rd_party")
        @Expose
        private String embedFrom3rdParty = "";
        @SerializedName("subscribe_msg")
        @Expose
        private String subscribeMsg = "";
        @SerializedName("opt_placeholder")
        @Expose
        private String optPlaceholder = "";
        @SerializedName("coupon_code_hint")
        @Expose
        private String couponCodeHint = "";
        @SerializedName("mobile")
        @Expose
        private String mobile = "";
        @SerializedName("enter_voucher_code")
        @Expose
        private String enterVoucherCode = "";
        @SerializedName("text_email_placeholder")
        @Expose
        private String textEmailPlaceholder = "";
        @SerializedName("text_name_placeholder")
        @Expose
        private String textNamePlaceholder = "";
        @SerializedName("text_message_placeholder")
        @Expose
        private String textMessagePlaceholder = "";
        @SerializedName("search_alert")
        @Expose
        private String searchAlert = "";
        @SerializedName("credit_card_number_hint")
        @Expose
        private String creditCardNumberHint = "";
        @SerializedName("text_email")
        @Expose
        private String textEmail = "";
        @SerializedName("name_hint")
        @Expose
        private String nameHint = "";
        @SerializedName("credit_card_name_hint")
        @Expose
        private String creditCardNameHint = "";
        @SerializedName("text_password")
        @Expose
        private String textPassword = "";
        @SerializedName("enter_review_here")
        @Expose
        private String enterReviewHere = "";
        @SerializedName("error_in_subscription")
        @Expose
        private String errorInSubscription = "";
        @SerializedName("error_data_fetching")
        @Expose
        private String errorDataFetching = "";
        @SerializedName("error_in_payment_validation")
        @Expose
        private String errorInPaymentValidation = "";
        @SerializedName("error_in_registration")
        @Expose
        private String errorInRegistration = "";
        @SerializedName("error_transc_process")
        @Expose
        private String errorTranscProcess = "";
        @SerializedName("error_in_profile_update")
        @Expose
        private String errorInProfileUpdate = "";
        @SerializedName("exclude_out_of_stock")
        @Expose
        private String excludeOutOfStock = "";
        @SerializedName("transaction_status_expired")
        @Expose
        private String transactionStatusExpired = "";
        @SerializedName("free")
        @Expose
        private String free = "";
        @SerializedName("facebook")
        @Expose
        private String facebook = "";
        @SerializedName("details_not_found_alert")
        @Expose
        private String detailsNotFoundAlert = "";
        @SerializedName("failure")
        @Expose
        private String failure = "";
        @SerializedName("section_not_found")
        @Expose
        private String sectionNotFound = "";
        @SerializedName("file")
        @Expose
        private String file = "";
        @SerializedName("fill_form_below")
        @Expose
        private String fillFormBelow = "";
        @SerializedName("enter_register_fields_data")
        @Expose
        private String enterRegisterFieldsData = "";
        @SerializedName("filmography")
        @Expose
        private String filmography = "";
        @SerializedName("filter_by")
        @Expose
        private String filterBy = "";
        @SerializedName("first_name")
        @Expose
        private String firstName = "";
        @SerializedName("forgot_password")
        @Expose
        private String forgotPassword = "";
        @SerializedName("format")
        @Expose
        private String format = "";
        @SerializedName("from_computer")
        @Expose
        private String fromComputer = "";
        @SerializedName("from_dropbox")
        @Expose
        private String fromDropbox = "";
        @SerializedName("generate_otp")
        @Expose
        private String generateOtp = "";
        @SerializedName("genre")
        @Expose
        private String genre = "";
        @SerializedName("google_play")
        @Expose
        private String googlePlay = "";
        @SerializedName("google_plus")
        @Expose
        private String googlePlus = "";
        @SerializedName("have_account")
        @Expose
        private String haveAccount = "";
        @SerializedName("home")
        @Expose
        private String home = "";
        @SerializedName("agree_terms")
        @Expose
        private String agreeTerms = "";
        @SerializedName("image_upload_failed")
        @Expose
        private String imageUploadFailed = "";
        @SerializedName("initial")
        @Expose
        private String initial = "";
        @SerializedName("instagram")
        @Expose
        private String instagram = "";
        @SerializedName("introducing_cast")
        @Expose
        private String introducingCast = "";
        @SerializedName("invalid_content")
        @Expose
        private String invalidContent = "";
        @SerializedName("invalid_content_type")
        @Expose
        private String invalidContentType = "";
        @SerializedName("invalid_coupon")
        @Expose
        private String invalidCoupon = "";
        @SerializedName("invalid_data")
        @Expose
        private String invalidData = "";
        @SerializedName("invalid_permalink")
        @Expose
        private String invalidPermalink = "";
        @SerializedName("invalid_playlist")
        @Expose
        private String invalidPlaylist = "";
        @SerializedName("invalid_voucher")
        @Expose
        private String invalidVoucher = "";
        @SerializedName("invalid_filetype")
        @Expose
        private String invalidFiletype = "";
        @SerializedName("invoice")
        @Expose
        private String invoice = "";
        @SerializedName("item-s")
        @Expose
        private String itemS = "";
        @SerializedName("authenticating_card")
        @Expose
        private String authenticatingCard = "";
        @SerializedName("btn_keep")
        @Expose
        private String btnKeep = "";
        @SerializedName("login_facebook")
        @Expose
        private String loginFacebook = "";
        @SerializedName("language_popup_language")
        @Expose
        private String languagePopupLanguage = "";
        @SerializedName("last_name")
        @Expose
        private String lastName = "";
        @SerializedName("sort_last_uploaded")
        @Expose
        private String sortLastUploaded = "";
        @SerializedName("language_popup_login")
        @Expose
        private String languagePopupLogin = "";
        @SerializedName("login")
        @Expose
        private String login = "";
        @SerializedName("google_signin")
        @Expose
        private String googleSignin = "";
        @SerializedName("logout")
        @Expose
        private String logout = "";
        @SerializedName("logout_success")
        @Expose
        private String logoutSuccess = "";
        @SerializedName("manage_content")
        @Expose
        private String manageContent = "";
        @SerializedName("manage_device")
        @Expose
        private String manageDevice = "";
        @SerializedName("text_message")
        @Expose
        private String textMessage = "";
        @SerializedName("mobile_number_exists_us")
        @Expose
        private String mobileNumberExistsUs = "";
        @SerializedName("mobilenumber_not_registered")
        @Expose
        private String mobilenumberNotRegistered = "";
        @SerializedName("queue_list_empty")
        @Expose
        private String queueListEmpty = "";
        @SerializedName("my_content")
        @Expose
        private String myContent = "";
        @SerializedName("my_download")
        @Expose
        private String myDownload = "";
        @SerializedName("my_favourite")
        @Expose
        private String myFavourite = "";
        @SerializedName("my_library")
        @Expose
        private String myLibrary = "";
        @SerializedName("my_playlist")
        @Expose
        private String myPlaylist = "";
        @SerializedName("my_uploads")
        @Expose
        private String myUploads = "";
        @SerializedName("test_transaction")
        @Expose
        private String testTransaction = "";
        @SerializedName("need_to_sign_in")
        @Expose
        private String needToSignIn = "";
        @SerializedName("new_password")
        @Expose
        private String newPassword = "";
        @SerializedName("new_here_title")
        @Expose
        private String newHereTitle = "";
        @SerializedName("btn_next")
        @Expose
        private String btnNext = "";
        @SerializedName("other_episodes")
        @Expose
        private String otherEpisodes = "";
        @SerializedName("no")
        @Expose
        private String no = "";
        @SerializedName("no_data")
        @Expose
        private String noData = "";
        @SerializedName("no_device")
        @Expose
        private String noDevice = "";
        @SerializedName("no_internet_connection")
        @Expose
        private String noInternetConnection = "";
        @SerializedName("no_internet_no_data")
        @Expose
        private String noInternetNoData = "";
        @SerializedName("no_playlist_found")
        @Expose
        private String noPlaylistFound = "";
        @SerializedName("no_record_found")
        @Expose
        private String noRecordFound = "";
        @SerializedName("no_categories_found")
        @Expose
        private String noCategoriesFound = "";
        @SerializedName("not_found")
        @Expose
        private String notFound = "";
        @SerializedName("no_details_available")
        @Expose
        private String noDetailsAvailable = "";
        @SerializedName("no_devices_available")
        @Expose
        private String noDevicesAvailable = "";
        @SerializedName("no_downloaded_videos_available")
        @Expose
        private String noDownloadedVideosAvailable = "";
        @SerializedName("no_genre_found")
        @Expose
        private String noGenreFound = "";
        @SerializedName("no_notification")
        @Expose
        private String noNotification = "";
        @SerializedName("no_result_found_refine_your_search")
        @Expose
        private String noResultFoundRefineYourSearch = "";
        @SerializedName("no_saved_address_found")
        @Expose
        private String noSavedAddressFound = "";
        @SerializedName("no_track")
        @Expose
        private String noTrack = "";
        @SerializedName("notification_title")
        @Expose
        private String notificationTitle = "";
        @SerializedName("notification_read")
        @Expose
        private String notificationRead = "";
        @SerializedName("otp")
        @Expose
        private String otp = "";
        @SerializedName("otp_failed")
        @Expose
        private String otpFailed = "";
        @SerializedName("otp_success_email")
        @Expose
        private String otpSuccessEmail = "";
        @SerializedName("otp_success_mobile")
        @Expose
        private String otpSuccessMobile = "";
        @SerializedName("btn_ok")
        @Expose
        private String btnOk = "";
        @SerializedName("old_password")
        @Expose
        private String oldPassword = "";
        @SerializedName("app_on")
        @Expose
        private String appOn = "";
        @SerializedName("player_watchable_message")
        @Expose
        private String playerWatchableMessage = "";
        @SerializedName("invalid_phone_number")
        @Expose
        private String invalidPhoneNumber = "";
        @SerializedName("oops_invalid_email")
        @Expose
        private String oopsInvalidEmail = "";
        @SerializedName("oops_you_have_no_access")
        @Expose
        private String oopsYouHaveNoAccess = "";
        @SerializedName("transaction_error")
        @Expose
        private String transactionError = "";
        @SerializedName("open")
        @Expose
        private String open = "";
        @SerializedName("order")
        @Expose
        private String order = "";
        @SerializedName("transaction_detail_order_id")
        @Expose
        private String transactionDetailOrderId = "";
        @SerializedName("no_pdf")
        @Expose
        private String noPdf = "";
        @SerializedName("password_reset_link")
        @Expose
        private String passwordResetLink = "";
        @SerializedName("password_donot_match")
        @Expose
        private String passwordDonotMatch = "";
        @SerializedName("pay_by_paypal")
        @Expose
        private String payByPaypal = "";
        @SerializedName("btn_paynow")
        @Expose
        private String btnPaynow = "";
        @SerializedName("pay_with_credit_card")
        @Expose
        private String payWithCreditCard = "";
        @SerializedName("Pay_Per_View")
        @Expose
        private String payPerView = "";
        @SerializedName("payment_options_title")
        @Expose
        private String paymentOptionsTitle = "";
        @SerializedName("pending")
        @Expose
        private String pending = "";
        @SerializedName("pending_refund")
        @Expose
        private String pendingRefund = "";
        @SerializedName("pending_return")
        @Expose
        private String pendingReturn = "";
        @SerializedName("plan_name")
        @Expose
        private String planName = "";
        @SerializedName("play_now")
        @Expose
        private String playNow = "";
        @SerializedName("playing_now")
        @Expose
        private String playingNow = "";
        @SerializedName("playlist_created")
        @Expose
        private String playlistCreated = "";
        @SerializedName("playlist_deleted")
        @Expose
        private String playlistDeleted = "";
        @SerializedName("playlist_name_not_blank")
        @Expose
        private String playlistNameNotBlank = "";
        @SerializedName("playlist_updated")
        @Expose
        private String playlistUpdated = "";
        @SerializedName("playlist_already_exist")
        @Expose
        private String playlistAlreadyExist = "";
        @SerializedName("voucher_vaildate_message")
        @Expose
        private String voucherVaildateMessage = "";
        @SerializedName("donot_close_or_refresh")
        @Expose
        private String donotCloseOrRefresh = "";
        @SerializedName("enable_flash_message_header")
        @Expose
        private String enableFlashMessageHeader = "";
        @SerializedName("enable_flash_message")
        @Expose
        private String enableFlashMessage = "";
        @SerializedName("coupon_alert")
        @Expose
        private String couponAlert = "";
        @SerializedName("min_length")
        @Expose
        private String minLength = "";
        @SerializedName("max_length")
        @Expose
        private String maxLength = "";
        @SerializedName("search_min_character")
        @Expose
        private String searchMinCharacter = "";
        @SerializedName("valid_confirm_password")
        @Expose
        private String validConfirmPassword = "";
        @SerializedName("cvv_alert")
        @Expose
        private String cvvAlert = "";
        @SerializedName("email_required")
        @Expose
        private String emailRequired = "";
        @SerializedName("otp_required")
        @Expose
        private String otpRequired = "";
        @SerializedName("mobile_number_required")
        @Expose
        private String mobileNumberRequired = "";
        @SerializedName("required_fields")
        @Expose
        private String requiredFields = "";
        @SerializedName("reactivate_subscription_watch_video")
        @Expose
        private String reactivateSubscriptionWatchVideo = "";
        @SerializedName("select_product")
        @Expose
        private String selectProduct = "";
        @SerializedName("upload_image")
        @Expose
        private String uploadImage = "";
        @SerializedName("upload_image_dimension")
        @Expose
        private String uploadImageDimension = "";
        @SerializedName("btn_post_review")
        @Expose
        private String btnPostReview = "";
        @SerializedName("pre_order")
        @Expose
        private String preOrder = "";
        @SerializedName("advance_purchase")
        @Expose
        private String advancePurchase = "";
        @SerializedName("price_hightolow")
        @Expose
        private String priceHightolow = "";
        @SerializedName("price_lowtohigh")
        @Expose
        private String priceLowtohigh = "";
        @SerializedName("processing")
        @Expose
        private String processing = "";
        @SerializedName("profile")
        @Expose
        private String profile = "";
        @SerializedName("profile_updated")
        @Expose
        private String profileUpdated = "";
        @SerializedName("provided_permalink_invalid")
        @Expose
        private String providedPermalinkInvalid = "";
        @SerializedName("purchase")
        @Expose
        private String purchase = "";
        @SerializedName("transaction_detail_purchase_date")
        @Expose
        private String transactionDetailPurchaseDate = "";
        @SerializedName("purchase_history")
        @Expose
        private String purchaseHistory = "";
        @SerializedName("qty")
        @Expose
        private String qty = "";
        @SerializedName("queue_clear_successfully")
        @Expose
        private String queueClearSuccessfully = "";
        @SerializedName("register_facebook")
        @Expose
        private String registerFacebook = "";
        @SerializedName("reedem_and_payment")
        @Expose
        private String reedemAndPayment = "";
        @SerializedName("reedem_watch")
        @Expose
        private String reedemWatch = "";
        @SerializedName("reedem_and_watch")
        @Expose
        private String reedemAndWatch = "";
        @SerializedName("btn_register")
        @Expose
        private String btnRegister = "";
        @SerializedName("register_step")
        @Expose
        private String registerStep = "";
        @SerializedName("google_signup")
        @Expose
        private String googleSignup = "";
        @SerializedName("related_videos")
        @Expose
        private String relatedVideos = "";
        @SerializedName("release_date")
        @Expose
        private String releaseDate = "";
        @SerializedName("sort_release_date")
        @Expose
        private String sortReleaseDate = "";
        @SerializedName("remove")
        @Expose
        private String remove = "";
        @SerializedName("remove_device_request_succ")
        @Expose
        private String removeDeviceRequestSucc = "";
        @SerializedName("renews_on_valid_till")
        @Expose
        private String renewsOnValidTill = "";
        @SerializedName("required_data_not_found")
        @Expose
        private String requiredDataNotFound = "";
        @SerializedName("resend_otp")
        @Expose
        private String resendOtp = "";
        @SerializedName("resume")
        @Expose
        private String resume = "";
        @SerializedName("returned")
        @Expose
        private String returned = "";
        @SerializedName("reviews")
        @Expose
        private String reviews = "";
        @SerializedName("btn_save")
        @Expose
        private String btnSave = "";
        @SerializedName("save_playlist")
        @Expose
        private String savePlaylist = "";
        @SerializedName("save_this_card")
        @Expose
        private String saveThisCard = "";
        @SerializedName("text_search_placeholder")
        @Expose
        private String textSearchPlaceholder = "";
        @SerializedName("search_parameter_required")
        @Expose
        private String searchParameterRequired = "";
        @SerializedName("season")
        @Expose
        private String season = "";
        @SerializedName("app_select_language")
        @Expose
        private String appSelectLanguage = "";
        @SerializedName("select_purchase_type")
        @Expose
        private String selectPurchaseType = "";
        @SerializedName("select_shipping_method")
        @Expose
        private String selectShippingMethod = "";
        @SerializedName("select_option_title")
        @Expose
        private String selectOptionTitle = "";
        @SerializedName("select_plan")
        @Expose
        private String selectPlan = "";
        @SerializedName("btn_send")
        @Expose
        private String btnSend = "";
        @SerializedName("server_to_server")
        @Expose
        private String serverToServer = "";
        @SerializedName("share_in_twitter")
        @Expose
        private String shareInTwitter = "";
        @SerializedName("share_in_facebook")
        @Expose
        private String shareInFacebook = "";
        @SerializedName("shipped")
        @Expose
        private String shipped = "";
        @SerializedName("shop_now")
        @Expose
        private String shopNow = "";
        @SerializedName("shop_products")
        @Expose
        private String shopProducts = "";
        @SerializedName("signup_title")
        @Expose
        private String signupTitle = "";
        @SerializedName("sl_no")
        @Expose
        private String slNo = "";
        @SerializedName("slow_internet_connection")
        @Expose
        private String slowInternetConnection = "";
        @SerializedName("slow_issue_internet_connection")
        @Expose
        private String slowIssueInternetConnection = "";
        @SerializedName("content_added_to_playlist")
        @Expose
        private String contentAddedToPlaylist = "";
        @SerializedName("sorry")
        @Expose
        private String sorry = "";
        @SerializedName("generate_otp_msg")
        @Expose
        private String generateOtpMsg = "";
        @SerializedName("sorry_you_subscribed_similar_plan")
        @Expose
        private String sorryYouSubscribedSimilarPlan = "";
        @SerializedName("watch_duration_exceed_message")
        @Expose
        private String watchDurationExceedMessage = "";
        @SerializedName("account_not_activated")
        @Expose
        private String accountNotActivated = "";
        @SerializedName("api_subscribe_failed")
        @Expose
        private String apiSubscribeFailed = "";
        @SerializedName("app_expired")
        @Expose
        private String appExpired = "";
        @SerializedName("geo_blocked_alert")
        @Expose
        private String geoBlockedAlert = "";
        @SerializedName("sorry_country_not_avaliable")
        @Expose
        private String sorryCountryNotAvaliable = "";
        @SerializedName("sign_out_error")
        @Expose
        private String signOutError = "";
        @SerializedName("already_purchase_this_content")
        @Expose
        private String alreadyPurchaseThisContent = "";
        @SerializedName("sort_by")
        @Expose
        private String sortBy = "";
        @SerializedName("star")
        @Expose
        private String star = "";
        @SerializedName("start_in_minutes")
        @Expose
        private String startInMinutes = "";
        @SerializedName("state")
        @Expose
        private String state = "";
        @SerializedName("state_province_region")
        @Expose
        private String stateProvinceRegion = "";
        @SerializedName("stop_saving_this_video")
        @Expose
        private String stopSavingThisVideo = "";
        @SerializedName("story_title")
        @Expose
        private String storyTitle = "";
        @SerializedName("btn_submit")
        @Expose
        private String btnSubmit = "";
        @SerializedName("submit_rating")
        @Expose
        private String submitRating = "";
        @SerializedName("subscribe_to_announcement")
        @Expose
        private String subscribeToAnnouncement = "";
        @SerializedName("subscription_bundles")
        @Expose
        private String subscriptionBundles = "";
        @SerializedName("subtitles")
        @Expose
        private String subtitles = "";
        @SerializedName("success")
        @Expose
        private String success = "";
        @SerializedName("transaction_success")
        @Expose
        private String transactionSuccess = "";
        @SerializedName("successful")
        @Expose
        private String successful = "";
        @SerializedName("terms_and_conditions")
        @Expose
        private String termsAndConditions = "";
        @SerializedName("success_msg")
        @Expose
        private String successMsg = "";
        @SerializedName("thanks_for_contact")
        @Expose
        private String thanksForContact = "";
        @SerializedName("access_period_expired")
        @Expose
        private String accessPeriodExpired = "";
        @SerializedName("watch_period_expired")
        @Expose
        private String watchPeriodExpired = "";
        @SerializedName("no_purchase_history")
        @Expose
        private String noPurchaseHistory = "";
        @SerializedName("video_issue")
        @Expose
        private String videoIssue = "";
        @SerializedName("no_content")
        @Expose
        private String noContent = "";
        @SerializedName("no_video_available")
        @Expose
        private String noVideoAvailable = "";
        @SerializedName("variant_mismatch")
        @Expose
        private String variantMismatch = "";
        @SerializedName("restrict_platform_Android")
        @Expose
        private String restrictPlatformAndroid = "";
        @SerializedName("restrict_platform_ios")
        @Expose
        private String restrictPlatformIos = "";
        @SerializedName("content_not_available_in_your_country")
        @Expose
        private String contentNotAvailableInYourCountry = "";
        @SerializedName("preorder_purchase")
        @Expose
        private String preorderPurchase = "";
        @SerializedName("required_default_msg")
        @Expose
        private String requiredDefaultMsg = "";
        @SerializedName("free_content")
        @Expose
        private String freeContent = "";
        @SerializedName("video_restiction_in_your_country")
        @Expose
        private String videoRestictionInYourCountry = "";
        @SerializedName("track")
        @Expose
        private String track = "";
        @SerializedName("tracklist")
        @Expose
        private String tracklist = "";
        @SerializedName("transaction_title")
        @Expose
        private String transactionTitle = "";
        @SerializedName("transaction_date")
        @Expose
        private String transactionDate = "";
        @SerializedName("transaction_detail")
        @Expose
        private String transactionDetail = "";
        @SerializedName("transaction_status")
        @Expose
        private String transactionStatus = "";
        @SerializedName("transaction")
        @Expose
        private String transaction = "";
        @SerializedName("try_again")
        @Expose
        private String tryAgain = "";
        @SerializedName("twitter")
        @Expose
        private String twitter = "";
        @SerializedName("type_to_search")
        @Expose
        private String typeToSearch = "";
        @SerializedName("unit_price")
        @Expose
        private String unitPrice = "";
        @SerializedName("unpaid")
        @Expose
        private String unpaid = "";
        @SerializedName("update_title")
        @Expose
        private String updateTitle = "";
        @SerializedName("btn_update_profile")
        @Expose
        private String btnUpdateProfile = "";
        @SerializedName("upload")
        @Expose
        private String upload = "";
        @SerializedName("upload_method")
        @Expose
        private String uploadMethod = "";
        @SerializedName("upload_poster")
        @Expose
        private String uploadPoster = "";
        @SerializedName("upload_video")
        @Expose
        private String uploadVideo = "";
        @SerializedName("use_new_card")
        @Expose
        private String useNewCard = "";
        @SerializedName("user_data_retrieved")
        @Expose
        private String userDataRetrieved = "";
        @SerializedName("user_not_found")
        @Expose
        private String userNotFound = "";
        @SerializedName("user_id_required")
        @Expose
        private String userIdRequired = "";
        @SerializedName("user_information_not_found")
        @Expose
        private String userInformationNotFound = "";
        @SerializedName("user_login_profile")
        @Expose
        private String userLoginProfile = "";
        @SerializedName("user_profile")
        @Expose
        private String userProfile = "";
        @SerializedName("view_more")
        @Expose
        private String viewMore = "";
        @SerializedName("viewall")
        @Expose
        private String viewall = "";
        @SerializedName("subscription_bundle_view_details")
        @Expose
        private String subscriptionBundleViewDetails = "";
        @SerializedName("view_less")
        @Expose
        private String viewLess = "";
        @SerializedName("view_trailer")
        @Expose
        private String viewTrailer = "";
        @SerializedName("loading")
        @Expose
        private String loading = "";
        @SerializedName("visit_shop")
        @Expose
        private String visitShop = "";
        @SerializedName("voucher_applied_on_for_download")
        @Expose
        private String voucherAppliedOnForDownload = "";
        @SerializedName("voucher_applied_success")
        @Expose
        private String voucherAppliedSuccess = "";
        @SerializedName("voucher_code")
        @Expose
        private String voucherCode = "";
        @SerializedName("voucher_already_used")
        @Expose
        private String voucherAlreadyUsed = "";
        @SerializedName("voucher_will_be_applied_on")
        @Expose
        private String voucherWillBeAppliedOn = "";
        @SerializedName("want_to_download")
        @Expose
        private String wantToDownload = "";
        @SerializedName("want_to_delete")
        @Expose
        private String wantToDelete = "";
        @SerializedName("watch")
        @Expose
        private String watch = "";
        @SerializedName("watch_history")
        @Expose
        private String watchHistory = "";
        @SerializedName("watch_now")
        @Expose
        private String watchNow = "";
        @SerializedName("btn_watch_now")
        @Expose
        private String btnWatchNow = "";
        @SerializedName("watch_periods_ends")
        @Expose
        private String watchPeriodsEnds = "";
        @SerializedName("update_profile_alert")
        @Expose
        private String updateProfileAlert = "";
        @SerializedName("sign_out_alert")
        @Expose
        private String signOutAlert = "";
        @SerializedName("api_subscribe_msg")
        @Expose
        private String apiSubscribeMsg = "";
        @SerializedName("yes")
        @Expose
        private String yes = "";
        @SerializedName("activate_subscription_watch_video")
        @Expose
        private String activateSubscriptionWatchVideo = "";
        @SerializedName("downloaded_access_expired")
        @Expose
        private String downloadedAccessExpired = "";
        @SerializedName("perimission_denied")
        @Expose
        private String perimissionDenied = "";
        @SerializedName("already_added_favourite")
        @Expose
        private String alreadyAddedFavourite = "";
        @SerializedName("already_added_your_review")
        @Expose
        private String alreadyAddedYourReview = "";
        @SerializedName("already_purchased_content")
        @Expose
        private String alreadyPurchasedContent = "";
        @SerializedName("purchesed_content")
        @Expose
        private String purchesedContent = "";
        @SerializedName("logged_out_from_all_devices")
        @Expose
        private String loggedOutFromAllDevices = "";
        @SerializedName("incorrect_password")
        @Expose
        private String incorrectPassword = "";
        @SerializedName("incorrect_login_credential")
        @Expose
        private String incorrectLoginCredential = "";
        @SerializedName("crossed_max_limit_of_watching")
        @Expose
        private String crossedMaxLimitOfWatching = "";
        @SerializedName("exceed_no_devices")
        @Expose
        private String exceedNoDevices = "";
        @SerializedName("login_restriction")
        @Expose
        private String loginRestriction = "";
        @SerializedName("you_selected")
        @Expose
        private String youSelected = "";
        @SerializedName("purchase_success_alert")
        @Expose
        private String purchaseSuccessAlert = "";
        @SerializedName("need_login_to_review")
        @Expose
        private String needLoginToReview = "";
        @SerializedName("subscribe_login_msg")
        @Expose
        private String subscribeLoginMsg = "";
        @SerializedName("youtube")
        @Expose
        private String youtube = "";
        @SerializedName("your_device")
        @Expose
        private String yourDevice = "";
        @SerializedName("account_deactivate")
        @Expose
        private String accountDeactivate = "";
        @SerializedName("restrict-streaming-device")
        @Expose
        private String restrictStreamingDevice = "";
        @SerializedName("card_will_charge")
        @Expose
        private String cardWillCharge = "";
        @SerializedName("triel_desc")
        @Expose
        private String trielDesc = "";
        @SerializedName("free_for_coupon")
        @Expose
        private String freeForCoupon = "";
        @SerializedName("auto_play_info")
        @Expose
        private String autoPlayInfo = "";
        @SerializedName("download_complete_msg")
        @Expose
        private String downloadCompleteMsg = "";
        @SerializedName("subscription completed")
        @Expose
        private String subscriptionCompleted = "";
        @SerializedName("your_video_can_not_be_saved")
        @Expose
        private String yourVideoCanNotBeSaved = "";
        @SerializedName("ztoa")
        @Expose
        private String ztoa = "";
        @SerializedName("days")
        @Expose
        private String days = "";
        @SerializedName("off")
        @Expose
        private String off = "";
        @SerializedName("releasedate")
        @Expose
        private String releasedate = "";
        @SerializedName("search_hint")
        @Expose
        private String searchHint = "";
        @SerializedName("terms")
        @Expose
        private String terms = "";
        @SerializedName("to_login")
        @Expose
        private String toLogin = "";

        public String getEmbedContent() {
            return embedContent;
        }

        public void setEmbedContent(String embedContent) {
            this.embedContent = embedContent;
        }

        public String getUpdateMessage() {
            return updateMessage;
        }

        public void setUpdateMessage(String updateMessage) {
            this.updateMessage = updateMessage;
        }

        public String getAtoz() {
            return atoz;
        }

        public void setAtoz(String atoz) {
            this.atoz = atoz;
        }

        public String getAlreadyMember() {
            return alreadyMember;
        }

        public void setAlreadyMember(String alreadyMember) {
            this.alreadyMember = alreadyMember;
        }

        public String getAboutUs() {
            return aboutUs;
        }

        public void setAboutUs(String aboutUs) {
            this.aboutUs = aboutUs;
        }

        public String getAccessPeriodsEnds() {
            return accessPeriodsEnds;
        }

        public void setAccessPeriodsEnds(String accessPeriodsEnds) {
            this.accessPeriodsEnds = accessPeriodsEnds;
        }

        public String getActivatePlanTitle() {
            return activatePlanTitle;
        }

        public void setActivatePlanTitle(String activatePlanTitle) {
            this.activatePlanTitle = activatePlanTitle;
        }

        public String getTransactionStatusActive() {
            return transactionStatusActive;
        }

        public void setTransactionStatusActive(String transactionStatusActive) {
            this.transactionStatusActive = transactionStatusActive;
        }

        public String getActive() {
            return active;
        }

        public void setActive(String active) {
            this.active = active;
        }

        public String getActors() {
            return actors;
        }

        public void setActors(String actors) {
            this.actors = actors;
        }

        public String getAddContent() {
            return addContent;
        }

        public void setAddContent(String addContent) {
            this.addContent = addContent;
        }

        public String getAddToPlaylist() {
            return addToPlaylist;
        }

        public void setAddToPlaylist(String addToPlaylist) {
            this.addToPlaylist = addToPlaylist;
        }

        public String getAddToQueue() {
            return addToQueue;
        }

        public void setAddToQueue(String addToQueue) {
            this.addToQueue = addToQueue;
        }

        public String getAddAReview() {
            return addAReview;
        }

        public void setAddAReview(String addAReview) {
            this.addAReview = addAReview;
        }

        public String getAddToCalender() {
            return addToCalender;
        }

        public void setAddToCalender(String addToCalender) {
            this.addToCalender = addToCalender;
        }

        public String getAddToFav() {
            return addToFav;
        }

        public void setAddToFav(String addToFav) {
            this.addToFav = addToFav;
        }

        public String getAddedToQueue() {
            return addedToQueue;
        }

        public void setAddedToQueue(String addedToQueue) {
            this.addedToQueue = addedToQueue;
        }

        public String getAddedToCalender() {
            return addedToCalender;
        }

        public void setAddedToCalender(String addedToCalender) {
            this.addedToCalender = addedToCalender;
        }

        public String getAddedToFav() {
            return addedToFav;
        }

        public void setAddedToFav(String addedToFav) {
            this.addedToFav = addedToFav;
        }

        public String getAddressLine1() {
            return addressLine1;
        }

        public void setAddressLine1(String addressLine1) {
            this.addressLine1 = addressLine1;
        }

        public String getAddressLine2() {
            return addressLine2;
        }

        public void setAddressLine2(String addressLine2) {
            this.addressLine2 = addressLine2;
        }

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public String getEpisodesTitle() {
            return episodesTitle;
        }

        public void setEpisodesTitle(String episodesTitle) {
            this.episodesTitle = episodesTitle;
        }

        public String getAllNotificationRead() {
            return allNotificationRead;
        }

        public void setAllNotificationRead(String allNotificationRead) {
            this.allNotificationRead = allNotificationRead;
        }

        public String getSortAlphaAZ() {
            return sortAlphaAZ;
        }

        public void setSortAlphaAZ(String sortAlphaAZ) {
            this.sortAlphaAZ = sortAlphaAZ;
        }

        public String getSortAlphaZA() {
            return sortAlphaZA;
        }

        public void setSortAlphaZA(String sortAlphaZA) {
            this.sortAlphaZA = sortAlphaZA;
        }

        public String getAlreadyAddedPlaylist() {
            return alreadyAddedPlaylist;
        }

        public void setAlreadyAddedPlaylist(String alreadyAddedPlaylist) {
            this.alreadyAddedPlaylist = alreadyAddedPlaylist;
        }

        public String getAlreadyAddedToQueue() {
            return alreadyAddedToQueue;
        }

        public void setAlreadyAddedToQueue(String alreadyAddedToQueue) {
            this.alreadyAddedToQueue = alreadyAddedToQueue;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getAndroidVersion() {
            return androidVersion;
        }

        public void setAndroidVersion(String androidVersion) {
            this.androidVersion = androidVersion;
        }

        public String getAppleStore() {
            return appleStore;
        }

        public void setAppleStore(String appleStore) {
            this.appleStore = appleStore;
        }

        public String getCouponCancelled() {
            return couponCancelled;
        }

        public void setCouponCancelled(String couponCancelled) {
            this.couponCancelled = couponCancelled;
        }

        public String getBtnApply() {
            return btnApply;
        }

        public void setBtnApply(String btnApply) {
            this.btnApply = btnApply;
        }

        public String getArabic() {
            return arabic;
        }

        public void setArabic(String arabic) {
            this.arabic = arabic;
        }

        public String getConfirmDeleteMessage() {
            return confirmDeleteMessage;
        }

        public void setConfirmDeleteMessage(String confirmDeleteMessage) {
            this.confirmDeleteMessage = confirmDeleteMessage;
        }

        public String getAreYouSureDeleteDevice() {
            return areYouSureDeleteDevice;
        }

        public void setAreYouSureDeleteDevice(String areYouSureDeleteDevice) {
            this.areYouSureDeleteDevice = areYouSureDeleteDevice;
        }

        public String getAreYouSureToDeleteContent() {
            return areYouSureToDeleteContent;
        }

        public void setAreYouSureToDeleteContent(String areYouSureToDeleteContent) {
            this.areYouSureToDeleteContent = areYouSureToDeleteContent;
        }

        public String getAreYouSureToDeletePlaylist() {
            return areYouSureToDeletePlaylist;
        }

        public void setAreYouSureToDeletePlaylist(String areYouSureToDeletePlaylist) {
            this.areYouSureToDeletePlaylist = areYouSureToDeletePlaylist;
        }

        public String getSignOutWarning() {
            return signOutWarning;
        }

        public void setSignOutWarning(String signOutWarning) {
            this.signOutWarning = signOutWarning;
        }

        public String getApiSucscriptionConfirmMsg() {
            return apiSucscriptionConfirmMsg;
        }

        public void setApiSucscriptionConfirmMsg(String apiSucscriptionConfirmMsg) {
            this.apiSucscriptionConfirmMsg = apiSucscriptionConfirmMsg;
        }

        public String getAsActor() {
            return asActor;
        }

        public void setAsActor(String asActor) {
            this.asActor = asActor;
        }

        public String getAuto() {
            return auto;
        }

        public void setAuto(String auto) {
            this.auto = auto;
        }

        public String getDiscountFreetrial() {
            return discountFreetrial;
        }

        public void setDiscountFreetrial(String discountFreetrial) {
            this.discountFreetrial = discountFreetrial;
        }

        public String getDiscountOnCoupon() {
            return discountOnCoupon;
        }

        public void setDiscountOnCoupon(String discountOnCoupon) {
            this.discountOnCoupon = discountOnCoupon;
        }

        public String getBackorder() {
            return backorder;
        }

        public void setBackorder(String backorder) {
            this.backorder = backorder;
        }

        public String getBook() {
            return book;
        }

        public void setBook(String book) {
            this.book = book;
        }

        public String getBooked() {
            return booked;
        }

        public void setBooked(String booked) {
            this.booked = booked;
        }

        public String getBookingTime() {
            return bookingTime;
        }

        public void setBookingTime(String bookingTime) {
            this.bookingTime = bookingTime;
        }

        public String getBookingTimeShouldGreaterThanCurrent() {
            return bookingTimeShouldGreaterThanCurrent;
        }

        public void setBookingTimeShouldGreaterThanCurrent(String bookingTimeShouldGreaterThanCurrent) {
            this.bookingTimeShouldGreaterThanCurrent = bookingTimeShouldGreaterThanCurrent;
        }

        public String getBookingTimeRequired() {
            return bookingTimeRequired;
        }

        public void setBookingTimeRequired(String bookingTimeRequired) {
            this.bookingTimeRequired = bookingTimeRequired;
        }

        public String getBrowseMusic() {
            return browseMusic;
        }

        public void setBrowseMusic(String browseMusic) {
            this.browseMusic = browseMusic;
        }

        public String getBuyNow() {
            return buyNow;
        }

        public void setBuyNow(String buyNow) {
            this.buyNow = buyNow;
        }

        public String getChkOver18() {
            return chkOver18;
        }

        public void setChkOver18(String chkOver18) {
            this.chkOver18 = chkOver18;
        }

        public String getCreditCardCvvHint() {
            return creditCardCvvHint;
        }

        public void setCreditCardCvvHint(String creditCardCvvHint) {
            this.creditCardCvvHint = creditCardCvvHint;
        }

        public String getBtnCancel() {
            return btnCancel;
        }

        public void setBtnCancel(String btnCancel) {
            this.btnCancel = btnCancel;
        }

        public String getCancelSubscriptionPlan() {
            return cancelSubscriptionPlan;
        }

        public void setCancelSubscriptionPlan(String cancelSubscriptionPlan) {
            this.cancelSubscriptionPlan = cancelSubscriptionPlan;
        }

        public String getSubscriptionBundlesCancel() {
            return subscriptionBundlesCancel;
        }

        public void setSubscriptionBundlesCancel(String subscriptionBundlesCancel) {
            this.subscriptionBundlesCancel = subscriptionBundlesCancel;
        }

        public String getCancelSubscriptionBundles() {
            return cancelSubscriptionBundles;
        }

        public void setCancelSubscriptionBundles(String cancelSubscriptionBundles) {
            this.cancelSubscriptionBundles = cancelSubscriptionBundles;
        }

        public String getCancelSubscriptionHeaderMsg() {
            return cancelSubscriptionHeaderMsg;
        }

        public void setCancelSubscriptionHeaderMsg(String cancelSubscriptionHeaderMsg) {
            this.cancelSubscriptionHeaderMsg = cancelSubscriptionHeaderMsg;
        }

        public String getCanceled() {
            return canceled;
        }

        public void setCanceled(String canceled) {
            this.canceled = canceled;
        }

        public String getCancelled() {
            return cancelled;
        }

        public void setCancelled(String cancelled) {
            this.cancelled = cancelled;
        }

        public String getCastlist() {
            return castlist;
        }

        public void setCastlist(String castlist) {
            this.castlist = castlist;
        }

        public String getCastCrewButtonTitle() {
            return castCrewButtonTitle;
        }

        public void setCastCrewButtonTitle(String castCrewButtonTitle) {
            this.castCrewButtonTitle = castCrewButtonTitle;
        }

        public String getCastCrewDetails() {
            return castCrewDetails;
        }

        public void setCastCrewDetails(String castCrewDetails) {
            this.castCrewDetails = castCrewDetails;
        }

        public String getCast() {
            return cast;
        }

        public void setCast(String cast) {
            this.cast = cast;
        }

        public String getNoCelebrityFound() {
            return noCelebrityFound;
        }

        public void setNoCelebrityFound(String noCelebrityFound) {
            this.noCelebrityFound = noCelebrityFound;
        }

        public String getCensorRating() {
            return censorRating;
        }

        public void setCensorRating(String censorRating) {
            this.censorRating = censorRating;
        }

        public String getChangeCountry() {
            return changeCountry;
        }

        public void setChangeCountry(String changeCountry) {
            this.changeCountry = changeCountry;
        }

        public String getChangePassword() {
            return changePassword;
        }

        public void setChangePassword(String changePassword) {
            this.changePassword = changePassword;
        }

        public String getChangeVideo() {
            return changeVideo;
        }

        public void setChangeVideo(String changeVideo) {
            this.changeVideo = changeVideo;
        }

        public String getChangeProfilePicture() {
            return changeProfilePicture;
        }

        public void setChangeProfilePicture(String changeProfilePicture) {
            this.changeProfilePicture = changeProfilePicture;
        }

        public String getChooseFromGallery() {
            return chooseFromGallery;
        }

        public void setChooseFromGallery(String chooseFromGallery) {
            this.chooseFromGallery = chooseFromGallery;
        }

        public String getSubscribeBtn() {
            return subscribeBtn;
        }

        public void setSubscribeBtn(String subscribeBtn) {
            this.subscribeBtn = subscribeBtn;
        }

        public String getClickHere() {
            return clickHere;
        }

        public void setClickHere(String clickHere) {
            this.clickHere = clickHere;
        }

        public String getCompleteSeason() {
            return completeSeason;
        }

        public void setCompleteSeason(String completeSeason) {
            this.completeSeason = completeSeason;
        }

        public String getConfirmPassword() {
            return confirmPassword;
        }

        public void setConfirmPassword(String confirmPassword) {
            this.confirmPassword = confirmPassword;
        }

        public String getConfirmEmailAddress() {
            return confirmEmailAddress;
        }

        public void setConfirmEmailAddress(String confirmEmailAddress) {
            this.confirmEmailAddress = confirmEmailAddress;
        }

        public String getContactUs() {
            return contactUs;
        }

        public void setContactUs(String contactUs) {
            this.contactUs = contactUs;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContentName() {
            return contentName;
        }

        public void setContentName(String contentName) {
            this.contentName = contentName;
        }

        public String getContentRemovePlaylist() {
            return contentRemovePlaylist;
        }

        public void setContentRemovePlaylist(String contentRemovePlaylist) {
            this.contentRemovePlaylist = contentRemovePlaylist;
        }

        public String getContentRemoveFavourite() {
            return contentRemoveFavourite;
        }

        public void setContentRemoveFavourite(String contentRemoveFavourite) {
            this.contentRemoveFavourite = contentRemoveFavourite;
        }

        public String getContentNotFound() {
            return contentNotFound;
        }

        public void setContentNotFound(String contentNotFound) {
            this.contentNotFound = contentNotFound;
        }

        public String getContentPermalinkInvalid() {
            return contentPermalinkInvalid;
        }

        public void setContentPermalinkInvalid(String contentPermalinkInvalid) {
            this.contentPermalinkInvalid = contentPermalinkInvalid;
        }

        public String getContentPermalinkRequired() {
            return contentPermalinkRequired;
        }

        public void setContentPermalinkRequired(String contentPermalinkRequired) {
            this.contentPermalinkRequired = contentPermalinkRequired;
        }

        public String getContentSavedToCalender() {
            return contentSavedToCalender;
        }

        public void setContentSavedToCalender(String contentSavedToCalender) {
            this.contentSavedToCalender = contentSavedToCalender;
        }

        public String getContentTypePermalinkRequired() {
            return contentTypePermalinkRequired;
        }

        public void setContentTypePermalinkRequired(String contentTypePermalinkRequired) {
            this.contentTypePermalinkRequired = contentTypePermalinkRequired;
        }

        public String getContinue() {
            return _continue;
        }

        public void setContinue(String _continue) {
            this._continue = _continue;
        }

        public String getResumeWatching() {
            return resumeWatching;
        }

        public void setResumeWatching(String resumeWatching) {
            this.resumeWatching = resumeWatching;
        }

        public String getCopied() {
            return copied;
        }

        public void setCopied(String copied) {
            this.copied = copied;
        }

        public String getCopy() {
            return copy;
        }

        public void setCopy(String copy) {
            this.copy = copy;
        }

        public String getCouponAlreadyUsed() {
            return couponAlreadyUsed;
        }

        public void setCouponAlreadyUsed(String couponAlreadyUsed) {
            this.couponAlreadyUsed = couponAlreadyUsed;
        }

        public String getCouponApplySuccess() {
            return couponApplySuccess;
        }

        public void setCouponApplySuccess(String couponApplySuccess) {
            this.couponApplySuccess = couponApplySuccess;
        }

        public String getCreatePlaylist() {
            return createPlaylist;
        }

        public void setCreatePlaylist(String createPlaylist) {
            this.createPlaylist = createPlaylist;
        }

        public String getCredit() {
            return credit;
        }

        public void setCredit(String credit) {
            this.credit = credit;
        }

        public String getCreditCardDetail() {
            return creditCardDetail;
        }

        public void setCreditCardDetail(String creditCardDetail) {
            this.creditCardDetail = creditCardDetail;
        }

        public String getDirector() {
            return director;
        }

        public void setDirector(String director) {
            this.director = director;
        }

        public String getDownloadButtonTitle() {
            return downloadButtonTitle;
        }

        public void setDownloadButtonTitle(String downloadButtonTitle) {
            this.downloadButtonTitle = downloadButtonTitle;
        }

        public String getPpvDefault() {
            return ppvDefault;
        }

        public void setPpvDefault(String ppvDefault) {
            this.ppvDefault = ppvDefault;
        }

        public String getDeleteBtn() {
            return deleteBtn;
        }

        public void setDeleteBtn(String deleteBtn) {
            this.deleteBtn = deleteBtn;
        }

        public String getDeleteContent() {
            return deleteContent;
        }

        public void setDeleteContent(String deleteContent) {
            this.deleteContent = deleteContent;
        }

        public String getDelivered() {
            return delivered;
        }

        public void setDelivered(String delivered) {
            this.delivered = delivered;
        }

        public String getDeregister() {
            return deregister;
        }

        public void setDeregister(String deregister) {
            this.deregister = deregister;
        }

        public String getDescriptionPpv() {
            return descriptionPpv;
        }

        public void setDescriptionPpv(String descriptionPpv) {
            this.descriptionPpv = descriptionPpv;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getDeviceIdNotFound() {
            return deviceIdNotFound;
        }

        public void setDeviceIdNotFound(String deviceIdNotFound) {
            this.deviceIdNotFound = deviceIdNotFound;
        }

        public String getDeviceList() {
            return deviceList;
        }

        public void setDeviceList(String deviceList) {
            this.deviceList = deviceList;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceNotRegisterd() {
            return deviceNotRegisterd;
        }

        public void setDeviceNotRegisterd(String deviceNotRegisterd) {
            this.deviceNotRegisterd = deviceNotRegisterd;
        }

        public String getDeviceRegisterd() {
            return deviceRegisterd;
        }

        public void setDeviceRegisterd(String deviceRegisterd) {
            this.deviceRegisterd = deviceRegisterd;
        }

        public String getDeviceRestrictionNotEnable() {
            return deviceRestrictionNotEnable;
        }

        public void setDeviceRestrictionNotEnable(String deviceRestrictionNotEnable) {
            this.deviceRestrictionNotEnable = deviceRestrictionNotEnable;
        }

        public String getDeviceAddedSuccess() {
            return deviceAddedSuccess;
        }

        public void setDeviceAddedSuccess(String deviceAddedSuccess) {
            this.deviceAddedSuccess = deviceAddedSuccess;
        }

        public String getDeviceInformationNotFound() {
            return deviceInformationNotFound;
        }

        public void setDeviceInformationNotFound(String deviceInformationNotFound) {
            this.deviceInformationNotFound = deviceInformationNotFound;
        }

        public String getDeviceNotFoundDeleted() {
            return deviceNotFoundDeleted;
        }

        public void setDeviceNotFoundDeleted(String deviceNotFoundDeleted) {
            this.deviceNotFoundDeleted = deviceNotFoundDeleted;
        }

        public String getDirectors() {
            return directors;
        }

        public void setDirectors(String directors) {
            this.directors = directors;
        }

        public String getBtnDiscard() {
            return btnDiscard;
        }

        public void setBtnDiscard(String btnDiscard) {
            this.btnDiscard = btnDiscard;
        }

        public String getWantDownloadCancel() {
            return wantDownloadCancel;
        }

        public void setWantDownloadCancel(String wantDownloadCancel) {
            this.wantDownloadCancel = wantDownloadCancel;
        }

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }

        public String getDownloadCancelled() {
            return downloadCancelled;
        }

        public void setDownloadCancelled(String downloadCancelled) {
            this.downloadCancelled = downloadCancelled;
        }

        public String getDownloadCompleted() {
            return downloadCompleted;
        }

        public void setDownloadCompleted(String downloadCompleted) {
            this.downloadCompleted = downloadCompleted;
        }

        public String getDownloadInterrupted() {
            return downloadInterrupted;
        }

        public void setDownloadInterrupted(String downloadInterrupted) {
            this.downloadInterrupted = downloadInterrupted;
        }

        public String getSaveOfflineVideo() {
            return saveOfflineVideo;
        }

        public void setSaveOfflineVideo(String saveOfflineVideo) {
            this.saveOfflineVideo = saveOfflineVideo;
        }

        public String getEditBtn() {
            return editBtn;
        }

        public void setEditBtn(String editBtn) {
            this.editBtn = editBtn;
        }

        public String getEditContent() {
            return editContent;
        }

        public void setEditContent(String editContent) {
            this.editContent = editContent;
        }

        public String getEditPlaylistName() {
            return editPlaylistName;
        }

        public void setEditPlaylistName(String editPlaylistName) {
            this.editPlaylistName = editPlaylistName;
        }

        public String getEmailExists() {
            return emailExists;
        }

        public void setEmailExists(String emailExists) {
            this.emailExists = emailExists;
        }

        public String getEmailDoesNotExist() {
            return emailDoesNotExist;
        }

        public void setEmailDoesNotExist(String emailDoesNotExist) {
            this.emailDoesNotExist = emailDoesNotExist;
        }

        public String getEmailNotRegistered() {
            return emailNotRegistered;
        }

        public void setEmailNotRegistered(String emailNotRegistered) {
            this.emailNotRegistered = emailNotRegistered;
        }

        public String getEmailPasswordInvalid() {
            return emailPasswordInvalid;
        }

        public void setEmailPasswordInvalid(String emailPasswordInvalid) {
            this.emailPasswordInvalid = emailPasswordInvalid;
        }

        public String getEmbed() {
            return embed;
        }

        public void setEmbed(String embed) {
            this.embed = embed;
        }

        public String getEmbedUrl() {
            return embedUrl;
        }

        public void setEmbedUrl(String embedUrl) {
            this.embedUrl = embedUrl;
        }

        public String getEmbedFrom3rdParty() {
            return embedFrom3rdParty;
        }

        public void setEmbedFrom3rdParty(String embedFrom3rdParty) {
            this.embedFrom3rdParty = embedFrom3rdParty;
        }

        public String getSubscribeMsg() {
            return subscribeMsg;
        }

        public void setSubscribeMsg(String subscribeMsg) {
            this.subscribeMsg = subscribeMsg;
        }

        public String getOptPlaceholder() {
            return optPlaceholder;
        }

        public void setOptPlaceholder(String optPlaceholder) {
            this.optPlaceholder = optPlaceholder;
        }

        public String getCouponCodeHint() {
            return couponCodeHint;
        }

        public void setCouponCodeHint(String couponCodeHint) {
            this.couponCodeHint = couponCodeHint;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEnterVoucherCode() {
            return enterVoucherCode;
        }

        public void setEnterVoucherCode(String enterVoucherCode) {
            this.enterVoucherCode = enterVoucherCode;
        }

        public String getTextEmailPlaceholder() {
            return textEmailPlaceholder;
        }

        public void setTextEmailPlaceholder(String textEmailPlaceholder) {
            this.textEmailPlaceholder = textEmailPlaceholder;
        }

        public String getTextNamePlaceholder() {
            return textNamePlaceholder;
        }

        public void setTextNamePlaceholder(String textNamePlaceholder) {
            this.textNamePlaceholder = textNamePlaceholder;
        }

        public String getTextMessagePlaceholder() {
            return textMessagePlaceholder;
        }

        public void setTextMessagePlaceholder(String textMessagePlaceholder) {
            this.textMessagePlaceholder = textMessagePlaceholder;
        }

        public String getSearchAlert() {
            return searchAlert;
        }

        public void setSearchAlert(String searchAlert) {
            this.searchAlert = searchAlert;
        }

        public String getCreditCardNumberHint() {
            return creditCardNumberHint;
        }

        public void setCreditCardNumberHint(String creditCardNumberHint) {
            this.creditCardNumberHint = creditCardNumberHint;
        }

        public String getTextEmail() {
            return textEmail;
        }

        public void setTextEmail(String textEmail) {
            this.textEmail = textEmail;
        }

        public String getNameHint() {
            return nameHint;
        }

        public void setNameHint(String nameHint) {
            this.nameHint = nameHint;
        }

        public String getCreditCardNameHint() {
            return creditCardNameHint;
        }

        public void setCreditCardNameHint(String creditCardNameHint) {
            this.creditCardNameHint = creditCardNameHint;
        }

        public String getTextPassword() {
            return textPassword;
        }

        public void setTextPassword(String textPassword) {
            this.textPassword = textPassword;
        }

        public String getEnterReviewHere() {
            return enterReviewHere;
        }

        public void setEnterReviewHere(String enterReviewHere) {
            this.enterReviewHere = enterReviewHere;
        }

        public String getErrorInSubscription() {
            return errorInSubscription;
        }

        public void setErrorInSubscription(String errorInSubscription) {
            this.errorInSubscription = errorInSubscription;
        }

        public String getErrorDataFetching() {
            return errorDataFetching;
        }

        public void setErrorDataFetching(String errorDataFetching) {
            this.errorDataFetching = errorDataFetching;
        }

        public String getErrorInPaymentValidation() {
            return errorInPaymentValidation;
        }

        public void setErrorInPaymentValidation(String errorInPaymentValidation) {
            this.errorInPaymentValidation = errorInPaymentValidation;
        }

        public String getErrorInRegistration() {
            return errorInRegistration;
        }

        public void setErrorInRegistration(String errorInRegistration) {
            this.errorInRegistration = errorInRegistration;
        }

        public String getErrorTranscProcess() {
            return errorTranscProcess;
        }

        public void setErrorTranscProcess(String errorTranscProcess) {
            this.errorTranscProcess = errorTranscProcess;
        }

        public String getErrorInProfileUpdate() {
            return errorInProfileUpdate;
        }

        public void setErrorInProfileUpdate(String errorInProfileUpdate) {
            this.errorInProfileUpdate = errorInProfileUpdate;
        }

        public String getExcludeOutOfStock() {
            return excludeOutOfStock;
        }

        public void setExcludeOutOfStock(String excludeOutOfStock) {
            this.excludeOutOfStock = excludeOutOfStock;
        }

        public String getTransactionStatusExpired() {
            return transactionStatusExpired;
        }

        public void setTransactionStatusExpired(String transactionStatusExpired) {
            this.transactionStatusExpired = transactionStatusExpired;
        }

        public String getFree() {
            return free;
        }

        public void setFree(String free) {
            this.free = free;
        }

        public String getFacebook() {
            return facebook;
        }

        public void setFacebook(String facebook) {
            this.facebook = facebook;
        }

        public String getDetailsNotFoundAlert() {
            return detailsNotFoundAlert;
        }

        public void setDetailsNotFoundAlert(String detailsNotFoundAlert) {
            this.detailsNotFoundAlert = detailsNotFoundAlert;
        }

        public String getFailure() {
            return failure;
        }

        public void setFailure(String failure) {
            this.failure = failure;
        }

        public String getSectionNotFound() {
            return sectionNotFound;
        }

        public void setSectionNotFound(String sectionNotFound) {
            this.sectionNotFound = sectionNotFound;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getFillFormBelow() {
            return fillFormBelow;
        }

        public void setFillFormBelow(String fillFormBelow) {
            this.fillFormBelow = fillFormBelow;
        }

        public String getEnterRegisterFieldsData() {
            return enterRegisterFieldsData;
        }

        public void setEnterRegisterFieldsData(String enterRegisterFieldsData) {
            this.enterRegisterFieldsData = enterRegisterFieldsData;
        }

        public String getFilmography() {
            return filmography;
        }

        public void setFilmography(String filmography) {
            this.filmography = filmography;
        }

        public String getFilterBy() {
            return filterBy;
        }

        public void setFilterBy(String filterBy) {
            this.filterBy = filterBy;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getForgotPassword() {
            return forgotPassword;
        }

        public void setForgotPassword(String forgotPassword) {
            this.forgotPassword = forgotPassword;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        public String getFromComputer() {
            return fromComputer;
        }

        public void setFromComputer(String fromComputer) {
            this.fromComputer = fromComputer;
        }

        public String getFromDropbox() {
            return fromDropbox;
        }

        public void setFromDropbox(String fromDropbox) {
            this.fromDropbox = fromDropbox;
        }

        public String getGenerateOtp() {
            return generateOtp;
        }

        public void setGenerateOtp(String generateOtp) {
            this.generateOtp = generateOtp;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public String getGooglePlay() {
            return googlePlay;
        }

        public void setGooglePlay(String googlePlay) {
            this.googlePlay = googlePlay;
        }

        public String getGooglePlus() {
            return googlePlus;
        }

        public void setGooglePlus(String googlePlus) {
            this.googlePlus = googlePlus;
        }

        public String getHaveAccount() {
            return haveAccount;
        }

        public void setHaveAccount(String haveAccount) {
            this.haveAccount = haveAccount;
        }

        public String getHome() {
            return home;
        }

        public void setHome(String home) {
            this.home = home;
        }

        public String getAgreeTerms() {
            return agreeTerms;
        }

        public void setAgreeTerms(String agreeTerms) {
            this.agreeTerms = agreeTerms;
        }

        public String getImageUploadFailed() {
            return imageUploadFailed;
        }

        public void setImageUploadFailed(String imageUploadFailed) {
            this.imageUploadFailed = imageUploadFailed;
        }

        public String getInitial() {
            return initial;
        }

        public void setInitial(String initial) {
            this.initial = initial;
        }

        public String getInstagram() {
            return instagram;
        }

        public void setInstagram(String instagram) {
            this.instagram = instagram;
        }

        public String getIntroducingCast() {
            return introducingCast;
        }

        public void setIntroducingCast(String introducingCast) {
            this.introducingCast = introducingCast;
        }

        public String getInvalidContent() {
            return invalidContent;
        }

        public void setInvalidContent(String invalidContent) {
            this.invalidContent = invalidContent;
        }

        public String getInvalidContentType() {
            return invalidContentType;
        }

        public void setInvalidContentType(String invalidContentType) {
            this.invalidContentType = invalidContentType;
        }

        public String getInvalidCoupon() {
            return invalidCoupon;
        }

        public void setInvalidCoupon(String invalidCoupon) {
            this.invalidCoupon = invalidCoupon;
        }

        public String getInvalidData() {
            return invalidData;
        }

        public void setInvalidData(String invalidData) {
            this.invalidData = invalidData;
        }

        public String getInvalidPermalink() {
            return invalidPermalink;
        }

        public void setInvalidPermalink(String invalidPermalink) {
            this.invalidPermalink = invalidPermalink;
        }

        public String getInvalidPlaylist() {
            return invalidPlaylist;
        }

        public void setInvalidPlaylist(String invalidPlaylist) {
            this.invalidPlaylist = invalidPlaylist;
        }

        public String getInvalidVoucher() {
            return invalidVoucher;
        }

        public void setInvalidVoucher(String invalidVoucher) {
            this.invalidVoucher = invalidVoucher;
        }

        public String getInvalidFiletype() {
            return invalidFiletype;
        }

        public void setInvalidFiletype(String invalidFiletype) {
            this.invalidFiletype = invalidFiletype;
        }

        public String getInvoice() {
            return invoice;
        }

        public void setInvoice(String invoice) {
            this.invoice = invoice;
        }

        public String getItemS() {
            return itemS;
        }

        public void setItemS(String itemS) {
            this.itemS = itemS;
        }

        public String getAuthenticatingCard() {
            return authenticatingCard;
        }

        public void setAuthenticatingCard(String authenticatingCard) {
            this.authenticatingCard = authenticatingCard;
        }

        public String getBtnKeep() {
            return btnKeep;
        }

        public void setBtnKeep(String btnKeep) {
            this.btnKeep = btnKeep;
        }

        public String getLoginFacebook() {
            return loginFacebook;
        }

        public void setLoginFacebook(String loginFacebook) {
            this.loginFacebook = loginFacebook;
        }

        public String getLanguagePopupLanguage() {
            return languagePopupLanguage;
        }

        public void setLanguagePopupLanguage(String languagePopupLanguage) {
            this.languagePopupLanguage = languagePopupLanguage;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getSortLastUploaded() {
            return sortLastUploaded;
        }

        public void setSortLastUploaded(String sortLastUploaded) {
            this.sortLastUploaded = sortLastUploaded;
        }

        public String getLanguagePopupLogin() {
            return languagePopupLogin;
        }

        public void setLanguagePopupLogin(String languagePopupLogin) {
            this.languagePopupLogin = languagePopupLogin;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getGoogleSignin() {
            return googleSignin;
        }

        public void setGoogleSignin(String googleSignin) {
            this.googleSignin = googleSignin;
        }

        public String getLogout() {
            return logout;
        }

        public void setLogout(String logout) {
            this.logout = logout;
        }

        public String getLogoutSuccess() {
            return logoutSuccess;
        }

        public void setLogoutSuccess(String logoutSuccess) {
            this.logoutSuccess = logoutSuccess;
        }

        public String getManageContent() {
            return manageContent;
        }

        public void setManageContent(String manageContent) {
            this.manageContent = manageContent;
        }

        public String getManageDevice() {
            return manageDevice;
        }

        public void setManageDevice(String manageDevice) {
            this.manageDevice = manageDevice;
        }

        public String getTextMessage() {
            return textMessage;
        }

        public void setTextMessage(String textMessage) {
            this.textMessage = textMessage;
        }

        public String getMobileNumberExistsUs() {
            return mobileNumberExistsUs;
        }

        public void setMobileNumberExistsUs(String mobileNumberExistsUs) {
            this.mobileNumberExistsUs = mobileNumberExistsUs;
        }

        public String getMobilenumberNotRegistered() {
            return mobilenumberNotRegistered;
        }

        public void setMobilenumberNotRegistered(String mobilenumberNotRegistered) {
            this.mobilenumberNotRegistered = mobilenumberNotRegistered;
        }

        public String getQueueListEmpty() {
            return queueListEmpty;
        }

        public void setQueueListEmpty(String queueListEmpty) {
            this.queueListEmpty = queueListEmpty;
        }

        public String getMyContent() {
            return myContent;
        }

        public void setMyContent(String myContent) {
            this.myContent = myContent;
        }

        public String getMyDownload() {
            return myDownload;
        }

        public void setMyDownload(String myDownload) {
            this.myDownload = myDownload;
        }

        public String getMyFavourite() {
            return myFavourite;
        }

        public void setMyFavourite(String myFavourite) {
            this.myFavourite = myFavourite;
        }

        public String getMyLibrary() {
            return myLibrary;
        }

        public void setMyLibrary(String myLibrary) {
            this.myLibrary = myLibrary;
        }

        public String getMyPlaylist() {
            return myPlaylist;
        }

        public void setMyPlaylist(String myPlaylist) {
            this.myPlaylist = myPlaylist;
        }

        public String getMyUploads() {
            return myUploads;
        }

        public void setMyUploads(String myUploads) {
            this.myUploads = myUploads;
        }

        public String getTestTransaction() {
            return testTransaction;
        }

        public void setTestTransaction(String testTransaction) {
            this.testTransaction = testTransaction;
        }

        public String getNeedToSignIn() {
            return needToSignIn;
        }

        public void setNeedToSignIn(String needToSignIn) {
            this.needToSignIn = needToSignIn;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }

        public String getNewHereTitle() {
            return newHereTitle;
        }

        public void setNewHereTitle(String newHereTitle) {
            this.newHereTitle = newHereTitle;
        }

        public String getBtnNext() {
            return btnNext;
        }

        public void setBtnNext(String btnNext) {
            this.btnNext = btnNext;
        }

        public String getOtherEpisodes() {
            return otherEpisodes;
        }

        public void setOtherEpisodes(String otherEpisodes) {
            this.otherEpisodes = otherEpisodes;
        }

        public String getNo() {
            return no;
        }

        public void setNo(String no) {
            this.no = no;
        }

        public String getNoData() {
            return noData;
        }

        public void setNoData(String noData) {
            this.noData = noData;
        }

        public String getNoDevice() {
            return noDevice;
        }

        public void setNoDevice(String noDevice) {
            this.noDevice = noDevice;
        }

        public String getNoInternetConnection() {
            return noInternetConnection;
        }

        public void setNoInternetConnection(String noInternetConnection) {
            this.noInternetConnection = noInternetConnection;
        }

        public String getNoInternetNoData() {
            return noInternetNoData;
        }

        public void setNoInternetNoData(String noInternetNoData) {
            this.noInternetNoData = noInternetNoData;
        }

        public String getNoPlaylistFound() {
            return noPlaylistFound;
        }

        public void setNoPlaylistFound(String noPlaylistFound) {
            this.noPlaylistFound = noPlaylistFound;
        }

        public String getNoRecordFound() {
            return noRecordFound;
        }

        public void setNoRecordFound(String noRecordFound) {
            this.noRecordFound = noRecordFound;
        }

        public String getNoCategoriesFound() {
            return noCategoriesFound;
        }

        public void setNoCategoriesFound(String noCategoriesFound) {
            this.noCategoriesFound = noCategoriesFound;
        }

        public String getNotFound() {
            return notFound;
        }

        public void setNotFound(String notFound) {
            this.notFound = notFound;
        }

        public String getNoDetailsAvailable() {
            return noDetailsAvailable;
        }

        public void setNoDetailsAvailable(String noDetailsAvailable) {
            this.noDetailsAvailable = noDetailsAvailable;
        }

        public String getNoDevicesAvailable() {
            return noDevicesAvailable;
        }

        public void setNoDevicesAvailable(String noDevicesAvailable) {
            this.noDevicesAvailable = noDevicesAvailable;
        }

        public String getNoDownloadedVideosAvailable() {
            return noDownloadedVideosAvailable;
        }

        public void setNoDownloadedVideosAvailable(String noDownloadedVideosAvailable) {
            this.noDownloadedVideosAvailable = noDownloadedVideosAvailable;
        }

        public String getNoGenreFound() {
            return noGenreFound;
        }

        public void setNoGenreFound(String noGenreFound) {
            this.noGenreFound = noGenreFound;
        }

        public String getNoNotification() {
            return noNotification;
        }

        public void setNoNotification(String noNotification) {
            this.noNotification = noNotification;
        }

        public String getNoResultFoundRefineYourSearch() {
            return noResultFoundRefineYourSearch;
        }

        public void setNoResultFoundRefineYourSearch(String noResultFoundRefineYourSearch) {
            this.noResultFoundRefineYourSearch = noResultFoundRefineYourSearch;
        }

        public String getNoSavedAddressFound() {
            return noSavedAddressFound;
        }

        public void setNoSavedAddressFound(String noSavedAddressFound) {
            this.noSavedAddressFound = noSavedAddressFound;
        }

        public String getNoTrack() {
            return noTrack;
        }

        public void setNoTrack(String noTrack) {
            this.noTrack = noTrack;
        }

        public String getNotificationTitle() {
            return notificationTitle;
        }

        public void setNotificationTitle(String notificationTitle) {
            this.notificationTitle = notificationTitle;
        }

        public String getNotificationRead() {
            return notificationRead;
        }

        public void setNotificationRead(String notificationRead) {
            this.notificationRead = notificationRead;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getOtpFailed() {
            return otpFailed;
        }

        public void setOtpFailed(String otpFailed) {
            this.otpFailed = otpFailed;
        }

        public String getOtpSuccessEmail() {
            return otpSuccessEmail;
        }

        public void setOtpSuccessEmail(String otpSuccessEmail) {
            this.otpSuccessEmail = otpSuccessEmail;
        }

        public String getOtpSuccessMobile() {
            return otpSuccessMobile;
        }

        public void setOtpSuccessMobile(String otpSuccessMobile) {
            this.otpSuccessMobile = otpSuccessMobile;
        }

        public String getBtnOk() {
            return btnOk;
        }

        public void setBtnOk(String btnOk) {
            this.btnOk = btnOk;
        }

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getAppOn() {
            return appOn;
        }

        public void setAppOn(String appOn) {
            this.appOn = appOn;
        }

        public String getPlayerWatchableMessage() {
            return playerWatchableMessage;
        }

        public void setPlayerWatchableMessage(String playerWatchableMessage) {
            this.playerWatchableMessage = playerWatchableMessage;
        }

        public String getInvalidPhoneNumber() {
            return invalidPhoneNumber;
        }

        public void setInvalidPhoneNumber(String invalidPhoneNumber) {
            this.invalidPhoneNumber = invalidPhoneNumber;
        }

        public String getOopsInvalidEmail() {
            return oopsInvalidEmail;
        }

        public void setOopsInvalidEmail(String oopsInvalidEmail) {
            this.oopsInvalidEmail = oopsInvalidEmail;
        }

        public String getOopsYouHaveNoAccess() {
            return oopsYouHaveNoAccess;
        }

        public void setOopsYouHaveNoAccess(String oopsYouHaveNoAccess) {
            this.oopsYouHaveNoAccess = oopsYouHaveNoAccess;
        }

        public String getTransactionError() {
            return transactionError;
        }

        public void setTransactionError(String transactionError) {
            this.transactionError = transactionError;
        }

        public String getOpen() {
            return open;
        }

        public void setOpen(String open) {
            this.open = open;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getTransactionDetailOrderId() {
            return transactionDetailOrderId;
        }

        public void setTransactionDetailOrderId(String transactionDetailOrderId) {
            this.transactionDetailOrderId = transactionDetailOrderId;
        }

        public String getNoPdf() {
            return noPdf;
        }

        public void setNoPdf(String noPdf) {
            this.noPdf = noPdf;
        }

        public String getPasswordResetLink() {
            return passwordResetLink;
        }

        public void setPasswordResetLink(String passwordResetLink) {
            this.passwordResetLink = passwordResetLink;
        }

        public String getPasswordDonotMatch() {
            return passwordDonotMatch;
        }

        public void setPasswordDonotMatch(String passwordDonotMatch) {
            this.passwordDonotMatch = passwordDonotMatch;
        }

        public String getPayByPaypal() {
            return payByPaypal;
        }

        public void setPayByPaypal(String payByPaypal) {
            this.payByPaypal = payByPaypal;
        }

        public String getBtnPaynow() {
            return btnPaynow;
        }

        public void setBtnPaynow(String btnPaynow) {
            this.btnPaynow = btnPaynow;
        }

        public String getPayWithCreditCard() {
            return payWithCreditCard;
        }

        public void setPayWithCreditCard(String payWithCreditCard) {
            this.payWithCreditCard = payWithCreditCard;
        }

        public String getPayPerView() {
            return payPerView;
        }

        public void setPayPerView(String payPerView) {
            this.payPerView = payPerView;
        }

        public String getPaymentOptionsTitle() {
            return paymentOptionsTitle;
        }

        public void setPaymentOptionsTitle(String paymentOptionsTitle) {
            this.paymentOptionsTitle = paymentOptionsTitle;
        }

        public String getPending() {
            return pending;
        }

        public void setPending(String pending) {
            this.pending = pending;
        }

        public String getPendingRefund() {
            return pendingRefund;
        }

        public void setPendingRefund(String pendingRefund) {
            this.pendingRefund = pendingRefund;
        }

        public String getPendingReturn() {
            return pendingReturn;
        }

        public void setPendingReturn(String pendingReturn) {
            this.pendingReturn = pendingReturn;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public String getPlayNow() {
            return playNow;
        }

        public void setPlayNow(String playNow) {
            this.playNow = playNow;
        }

        public String getPlayingNow() {
            return playingNow;
        }

        public void setPlayingNow(String playingNow) {
            this.playingNow = playingNow;
        }

        public String getPlaylistCreated() {
            return playlistCreated;
        }

        public void setPlaylistCreated(String playlistCreated) {
            this.playlistCreated = playlistCreated;
        }

        public String getPlaylistDeleted() {
            return playlistDeleted;
        }

        public void setPlaylistDeleted(String playlistDeleted) {
            this.playlistDeleted = playlistDeleted;
        }

        public String getPlaylistNameNotBlank() {
            return playlistNameNotBlank;
        }

        public void setPlaylistNameNotBlank(String playlistNameNotBlank) {
            this.playlistNameNotBlank = playlistNameNotBlank;
        }

        public String getPlaylistUpdated() {
            return playlistUpdated;
        }

        public void setPlaylistUpdated(String playlistUpdated) {
            this.playlistUpdated = playlistUpdated;
        }

        public String getPlaylistAlreadyExist() {
            return playlistAlreadyExist;
        }

        public void setPlaylistAlreadyExist(String playlistAlreadyExist) {
            this.playlistAlreadyExist = playlistAlreadyExist;
        }

        public String getVoucherVaildateMessage() {
            return voucherVaildateMessage;
        }

        public void setVoucherVaildateMessage(String voucherVaildateMessage) {
            this.voucherVaildateMessage = voucherVaildateMessage;
        }

        public String getDonotCloseOrRefresh() {
            return donotCloseOrRefresh;
        }

        public void setDonotCloseOrRefresh(String donotCloseOrRefresh) {
            this.donotCloseOrRefresh = donotCloseOrRefresh;
        }

        public String getEnableFlashMessageHeader() {
            return enableFlashMessageHeader;
        }

        public void setEnableFlashMessageHeader(String enableFlashMessageHeader) {
            this.enableFlashMessageHeader = enableFlashMessageHeader;
        }

        public String getEnableFlashMessage() {
            return enableFlashMessage;
        }

        public void setEnableFlashMessage(String enableFlashMessage) {
            this.enableFlashMessage = enableFlashMessage;
        }

        public String getCouponAlert() {
            return couponAlert;
        }

        public void setCouponAlert(String couponAlert) {
            this.couponAlert = couponAlert;
        }

        public String getMinLength() {
            return minLength;
        }

        public void setMinLength(String minLength) {
            this.minLength = minLength;
        }

        public String getMaxLength() {
            return maxLength;
        }

        public void setMaxLength(String maxLength) {
            this.maxLength = maxLength;
        }

        public String getSearchMinCharacter() {
            return searchMinCharacter;
        }

        public void setSearchMinCharacter(String searchMinCharacter) {
            this.searchMinCharacter = searchMinCharacter;
        }

        public String getValidConfirmPassword() {
            return validConfirmPassword;
        }

        public void setValidConfirmPassword(String validConfirmPassword) {
            this.validConfirmPassword = validConfirmPassword;
        }

        public String getCvvAlert() {
            return cvvAlert;
        }

        public void setCvvAlert(String cvvAlert) {
            this.cvvAlert = cvvAlert;
        }

        public String getEmailRequired() {
            return emailRequired;
        }

        public void setEmailRequired(String emailRequired) {
            this.emailRequired = emailRequired;
        }

        public String getOtpRequired() {
            return otpRequired;
        }

        public void setOtpRequired(String otpRequired) {
            this.otpRequired = otpRequired;
        }

        public String getMobileNumberRequired() {
            return mobileNumberRequired;
        }

        public void setMobileNumberRequired(String mobileNumberRequired) {
            this.mobileNumberRequired = mobileNumberRequired;
        }

        public String getRequiredFields() {
            return requiredFields;
        }

        public void setRequiredFields(String requiredFields) {
            this.requiredFields = requiredFields;
        }

        public String getReactivateSubscriptionWatchVideo() {
            return reactivateSubscriptionWatchVideo;
        }

        public void setReactivateSubscriptionWatchVideo(String reactivateSubscriptionWatchVideo) {
            this.reactivateSubscriptionWatchVideo = reactivateSubscriptionWatchVideo;
        }

        public String getSelectProduct() {
            return selectProduct;
        }

        public void setSelectProduct(String selectProduct) {
            this.selectProduct = selectProduct;
        }

        public String getUploadImage() {
            return uploadImage;
        }

        public void setUploadImage(String uploadImage) {
            this.uploadImage = uploadImage;
        }

        public String getUploadImageDimension() {
            return uploadImageDimension;
        }

        public void setUploadImageDimension(String uploadImageDimension) {
            this.uploadImageDimension = uploadImageDimension;
        }

        public String getBtnPostReview() {
            return btnPostReview;
        }

        public void setBtnPostReview(String btnPostReview) {
            this.btnPostReview = btnPostReview;
        }

        public String getPreOrder() {
            return preOrder;
        }

        public void setPreOrder(String preOrder) {
            this.preOrder = preOrder;
        }

        public String getAdvancePurchase() {
            return advancePurchase;
        }

        public void setAdvancePurchase(String advancePurchase) {
            this.advancePurchase = advancePurchase;
        }

        public String getPriceHightolow() {
            return priceHightolow;
        }

        public void setPriceHightolow(String priceHightolow) {
            this.priceHightolow = priceHightolow;
        }

        public String getPriceLowtohigh() {
            return priceLowtohigh;
        }

        public void setPriceLowtohigh(String priceLowtohigh) {
            this.priceLowtohigh = priceLowtohigh;
        }

        public String getProcessing() {
            return processing;
        }

        public void setProcessing(String processing) {
            this.processing = processing;
        }

        public String getProfile() {
            return profile;
        }

        public void setProfile(String profile) {
            this.profile = profile;
        }

        public String getProfileUpdated() {
            return profileUpdated;
        }

        public void setProfileUpdated(String profileUpdated) {
            this.profileUpdated = profileUpdated;
        }

        public String getProvidedPermalinkInvalid() {
            return providedPermalinkInvalid;
        }

        public void setProvidedPermalinkInvalid(String providedPermalinkInvalid) {
            this.providedPermalinkInvalid = providedPermalinkInvalid;
        }

        public String getPurchase() {
            return purchase;
        }

        public void setPurchase(String purchase) {
            this.purchase = purchase;
        }

        public String getTransactionDetailPurchaseDate() {
            return transactionDetailPurchaseDate;
        }

        public void setTransactionDetailPurchaseDate(String transactionDetailPurchaseDate) {
            this.transactionDetailPurchaseDate = transactionDetailPurchaseDate;
        }

        public String getPurchaseHistory() {
            return purchaseHistory;
        }

        public void setPurchaseHistory(String purchaseHistory) {
            this.purchaseHistory = purchaseHistory;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getQueueClearSuccessfully() {
            return queueClearSuccessfully;
        }

        public void setQueueClearSuccessfully(String queueClearSuccessfully) {
            this.queueClearSuccessfully = queueClearSuccessfully;
        }

        public String getRegisterFacebook() {
            return registerFacebook;
        }

        public void setRegisterFacebook(String registerFacebook) {
            this.registerFacebook = registerFacebook;
        }

        public String getReedemAndPayment() {
            return reedemAndPayment;
        }

        public void setReedemAndPayment(String reedemAndPayment) {
            this.reedemAndPayment = reedemAndPayment;
        }

        public String getReedemWatch() {
            return reedemWatch;
        }

        public void setReedemWatch(String reedemWatch) {
            this.reedemWatch = reedemWatch;
        }

        public String getReedemAndWatch() {
            return reedemAndWatch;
        }

        public void setReedemAndWatch(String reedemAndWatch) {
            this.reedemAndWatch = reedemAndWatch;
        }

        public String getBtnRegister() {
            return btnRegister;
        }

        public void setBtnRegister(String btnRegister) {
            this.btnRegister = btnRegister;
        }

        public String getRegisterStep() {
            return registerStep;
        }

        public void setRegisterStep(String registerStep) {
            this.registerStep = registerStep;
        }

        public String getGoogleSignup() {
            return googleSignup;
        }

        public void setGoogleSignup(String googleSignup) {
            this.googleSignup = googleSignup;
        }

        public String getRelatedVideos() {
            return relatedVideos;
        }

        public void setRelatedVideos(String relatedVideos) {
            this.relatedVideos = relatedVideos;
        }

        public String getReleaseDate() {
            return releaseDate;
        }

        public void setReleaseDate(String releaseDate) {
            this.releaseDate = releaseDate;
        }

        public String getSortReleaseDate() {
            return sortReleaseDate;
        }

        public void setSortReleaseDate(String sortReleaseDate) {
            this.sortReleaseDate = sortReleaseDate;
        }

        public String getRemove() {
            return remove;
        }

        public void setRemove(String remove) {
            this.remove = remove;
        }

        public String getRemoveDeviceRequestSucc() {
            return removeDeviceRequestSucc;
        }

        public void setRemoveDeviceRequestSucc(String removeDeviceRequestSucc) {
            this.removeDeviceRequestSucc = removeDeviceRequestSucc;
        }

        public String getRenewsOnValidTill() {
            return renewsOnValidTill;
        }

        public void setRenewsOnValidTill(String renewsOnValidTill) {
            this.renewsOnValidTill = renewsOnValidTill;
        }

        public String getRequiredDataNotFound() {
            return requiredDataNotFound;
        }

        public void setRequiredDataNotFound(String requiredDataNotFound) {
            this.requiredDataNotFound = requiredDataNotFound;
        }

        public String getResendOtp() {
            return resendOtp;
        }

        public void setResendOtp(String resendOtp) {
            this.resendOtp = resendOtp;
        }

        public String getResume() {
            return resume;
        }

        public void setResume(String resume) {
            this.resume = resume;
        }

        public String getReturned() {
            return returned;
        }

        public void setReturned(String returned) {
            this.returned = returned;
        }

        public String getReviews() {
            return reviews;
        }

        public void setReviews(String reviews) {
            this.reviews = reviews;
        }

        public String getBtnSave() {
            return btnSave;
        }

        public void setBtnSave(String btnSave) {
            this.btnSave = btnSave;
        }

        public String getSavePlaylist() {
            return savePlaylist;
        }

        public void setSavePlaylist(String savePlaylist) {
            this.savePlaylist = savePlaylist;
        }

        public String getSaveThisCard() {
            return saveThisCard;
        }

        public void setSaveThisCard(String saveThisCard) {
            this.saveThisCard = saveThisCard;
        }

        public String getTextSearchPlaceholder() {
            return textSearchPlaceholder;
        }

        public void setTextSearchPlaceholder(String textSearchPlaceholder) {
            this.textSearchPlaceholder = textSearchPlaceholder;
        }

        public String getSearchParameterRequired() {
            return searchParameterRequired;
        }

        public void setSearchParameterRequired(String searchParameterRequired) {
            this.searchParameterRequired = searchParameterRequired;
        }

        public String getSeason() {
            return season;
        }

        public void setSeason(String season) {
            this.season = season;
        }

        public String getAppSelectLanguage() {
            return appSelectLanguage;
        }

        public void setAppSelectLanguage(String appSelectLanguage) {
            this.appSelectLanguage = appSelectLanguage;
        }

        public String getSelectPurchaseType() {
            return selectPurchaseType;
        }

        public void setSelectPurchaseType(String selectPurchaseType) {
            this.selectPurchaseType = selectPurchaseType;
        }

        public String getSelectShippingMethod() {
            return selectShippingMethod;
        }

        public void setSelectShippingMethod(String selectShippingMethod) {
            this.selectShippingMethod = selectShippingMethod;
        }

        public String getSelectOptionTitle() {
            return selectOptionTitle;
        }

        public void setSelectOptionTitle(String selectOptionTitle) {
            this.selectOptionTitle = selectOptionTitle;
        }

        public String getSelectPlan() {
            return selectPlan;
        }

        public void setSelectPlan(String selectPlan) {
            this.selectPlan = selectPlan;
        }

        public String getBtnSend() {
            return btnSend;
        }

        public void setBtnSend(String btnSend) {
            this.btnSend = btnSend;
        }

        public String getServerToServer() {
            return serverToServer;
        }

        public void setServerToServer(String serverToServer) {
            this.serverToServer = serverToServer;
        }

        public String getShareInTwitter() {
            return shareInTwitter;
        }

        public void setShareInTwitter(String shareInTwitter) {
            this.shareInTwitter = shareInTwitter;
        }

        public String getShareInFacebook() {
            return shareInFacebook;
        }

        public void setShareInFacebook(String shareInFacebook) {
            this.shareInFacebook = shareInFacebook;
        }

        public String getShipped() {
            return shipped;
        }

        public void setShipped(String shipped) {
            this.shipped = shipped;
        }

        public String getShopNow() {
            return shopNow;
        }

        public void setShopNow(String shopNow) {
            this.shopNow = shopNow;
        }

        public String getShopProducts() {
            return shopProducts;
        }

        public void setShopProducts(String shopProducts) {
            this.shopProducts = shopProducts;
        }

        public String getSignupTitle() {
            return signupTitle;
        }

        public void setSignupTitle(String signupTitle) {
            this.signupTitle = signupTitle;
        }

        public String getSlNo() {
            return slNo;
        }

        public void setSlNo(String slNo) {
            this.slNo = slNo;
        }

        public String getSlowInternetConnection() {
            return slowInternetConnection;
        }

        public void setSlowInternetConnection(String slowInternetConnection) {
            this.slowInternetConnection = slowInternetConnection;
        }

        public String getSlowIssueInternetConnection() {
            return slowIssueInternetConnection;
        }

        public void setSlowIssueInternetConnection(String slowIssueInternetConnection) {
            this.slowIssueInternetConnection = slowIssueInternetConnection;
        }

        public String getContentAddedToPlaylist() {
            return contentAddedToPlaylist;
        }

        public void setContentAddedToPlaylist(String contentAddedToPlaylist) {
            this.contentAddedToPlaylist = contentAddedToPlaylist;
        }

        public String getSorry() {
            return sorry;
        }

        public void setSorry(String sorry) {
            this.sorry = sorry;
        }

        public String getGenerateOtpMsg() {
            return generateOtpMsg;
        }

        public void setGenerateOtpMsg(String generateOtpMsg) {
            this.generateOtpMsg = generateOtpMsg;
        }

        public String getSorryYouSubscribedSimilarPlan() {
            return sorryYouSubscribedSimilarPlan;
        }

        public void setSorryYouSubscribedSimilarPlan(String sorryYouSubscribedSimilarPlan) {
            this.sorryYouSubscribedSimilarPlan = sorryYouSubscribedSimilarPlan;
        }

        public String getWatchDurationExceedMessage() {
            return watchDurationExceedMessage;
        }

        public void setWatchDurationExceedMessage(String watchDurationExceedMessage) {
            this.watchDurationExceedMessage = watchDurationExceedMessage;
        }

        public String getAccountNotActivated() {
            return accountNotActivated;
        }

        public void setAccountNotActivated(String accountNotActivated) {
            this.accountNotActivated = accountNotActivated;
        }

        public String getApiSubscribeFailed() {
            return apiSubscribeFailed;
        }

        public void setApiSubscribeFailed(String apiSubscribeFailed) {
            this.apiSubscribeFailed = apiSubscribeFailed;
        }

        public String getAppExpired() {
            return appExpired;
        }

        public void setAppExpired(String appExpired) {
            this.appExpired = appExpired;
        }

        public String getGeoBlockedAlert() {
            return geoBlockedAlert;
        }

        public void setGeoBlockedAlert(String geoBlockedAlert) {
            this.geoBlockedAlert = geoBlockedAlert;
        }

        public String getSorryCountryNotAvaliable() {
            return sorryCountryNotAvaliable;
        }

        public void setSorryCountryNotAvaliable(String sorryCountryNotAvaliable) {
            this.sorryCountryNotAvaliable = sorryCountryNotAvaliable;
        }

        public String getSignOutError() {
            return signOutError;
        }

        public void setSignOutError(String signOutError) {
            this.signOutError = signOutError;
        }

        public String getAlreadyPurchaseThisContent() {
            return alreadyPurchaseThisContent;
        }

        public void setAlreadyPurchaseThisContent(String alreadyPurchaseThisContent) {
            this.alreadyPurchaseThisContent = alreadyPurchaseThisContent;
        }

        public String getSortBy() {
            return sortBy;
        }

        public void setSortBy(String sortBy) {
            this.sortBy = sortBy;
        }

        public String getStar() {
            return star;
        }

        public void setStar(String star) {
            this.star = star;
        }

        public String getStartInMinutes() {
            return startInMinutes;
        }

        public void setStartInMinutes(String startInMinutes) {
            this.startInMinutes = startInMinutes;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getStateProvinceRegion() {
            return stateProvinceRegion;
        }

        public void setStateProvinceRegion(String stateProvinceRegion) {
            this.stateProvinceRegion = stateProvinceRegion;
        }

        public String getStopSavingThisVideo() {
            return stopSavingThisVideo;
        }

        public void setStopSavingThisVideo(String stopSavingThisVideo) {
            this.stopSavingThisVideo = stopSavingThisVideo;
        }

        public String getStoryTitle() {
            return storyTitle;
        }

        public void setStoryTitle(String storyTitle) {
            this.storyTitle = storyTitle;
        }

        public String getBtnSubmit() {
            return btnSubmit;
        }

        public void setBtnSubmit(String btnSubmit) {
            this.btnSubmit = btnSubmit;
        }

        public String getSubmitRating() {
            return submitRating;
        }

        public void setSubmitRating(String submitRating) {
            this.submitRating = submitRating;
        }

        public String getSubscribeToAnnouncement() {
            return subscribeToAnnouncement;
        }

        public void setSubscribeToAnnouncement(String subscribeToAnnouncement) {
            this.subscribeToAnnouncement = subscribeToAnnouncement;
        }

        public String getSubscriptionBundles() {
            return subscriptionBundles;
        }

        public void setSubscriptionBundles(String subscriptionBundles) {
            this.subscriptionBundles = subscriptionBundles;
        }

        public String getSubtitles() {
            return subtitles;
        }

        public void setSubtitles(String subtitles) {
            this.subtitles = subtitles;
        }

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public String getTransactionSuccess() {
            return transactionSuccess;
        }

        public void setTransactionSuccess(String transactionSuccess) {
            this.transactionSuccess = transactionSuccess;
        }

        public String getSuccessful() {
            return successful;
        }

        public void setSuccessful(String successful) {
            this.successful = successful;
        }

        public String getTermsAndConditions() {
            return termsAndConditions;
        }

        public void setTermsAndConditions(String termsAndConditions) {
            this.termsAndConditions = termsAndConditions;
        }

        public String getSuccessMsg() {
            return successMsg;
        }

        public void setSuccessMsg(String successMsg) {
            this.successMsg = successMsg;
        }

        public String getThanksForContact() {
            return thanksForContact;
        }

        public void setThanksForContact(String thanksForContact) {
            this.thanksForContact = thanksForContact;
        }

        public String getAccessPeriodExpired() {
            return accessPeriodExpired;
        }

        public void setAccessPeriodExpired(String accessPeriodExpired) {
            this.accessPeriodExpired = accessPeriodExpired;
        }

        public String getWatchPeriodExpired() {
            return watchPeriodExpired;
        }

        public void setWatchPeriodExpired(String watchPeriodExpired) {
            this.watchPeriodExpired = watchPeriodExpired;
        }

        public String getNoPurchaseHistory() {
            return noPurchaseHistory;
        }

        public void setNoPurchaseHistory(String noPurchaseHistory) {
            this.noPurchaseHistory = noPurchaseHistory;
        }

        public String getVideoIssue() {
            return videoIssue;
        }

        public void setVideoIssue(String videoIssue) {
            this.videoIssue = videoIssue;
        }

        public String getNoContent() {
            return noContent;
        }

        public void setNoContent(String noContent) {
            this.noContent = noContent;
        }

        public String getNoVideoAvailable() {
            return noVideoAvailable;
        }

        public void setNoVideoAvailable(String noVideoAvailable) {
            this.noVideoAvailable = noVideoAvailable;
        }

        public String getVariantMismatch() {
            return variantMismatch;
        }

        public void setVariantMismatch(String variantMismatch) {
            this.variantMismatch = variantMismatch;
        }

        public String getRestrictPlatformAndroid() {
            return restrictPlatformAndroid;
        }

        public void setRestrictPlatformAndroid(String restrictPlatformAndroid) {
            this.restrictPlatformAndroid = restrictPlatformAndroid;
        }

        public String getRestrictPlatformIos() {
            return restrictPlatformIos;
        }

        public void setRestrictPlatformIos(String restrictPlatformIos) {
            this.restrictPlatformIos = restrictPlatformIos;
        }

        public String getContentNotAvailableInYourCountry() {
            return contentNotAvailableInYourCountry;
        }

        public void setContentNotAvailableInYourCountry(String contentNotAvailableInYourCountry) {
            this.contentNotAvailableInYourCountry = contentNotAvailableInYourCountry;
        }

        public String getPreorderPurchase() {
            return preorderPurchase;
        }

        public void setPreorderPurchase(String preorderPurchase) {
            this.preorderPurchase = preorderPurchase;
        }

        public String getRequiredDefaultMsg() {
            return requiredDefaultMsg;
        }

        public void setRequiredDefaultMsg(String requiredDefaultMsg) {
            this.requiredDefaultMsg = requiredDefaultMsg;
        }

        public String getFreeContent() {
            return freeContent;
        }

        public void setFreeContent(String freeContent) {
            this.freeContent = freeContent;
        }

        public String getVideoRestictionInYourCountry() {
            return videoRestictionInYourCountry;
        }

        public void setVideoRestictionInYourCountry(String videoRestictionInYourCountry) {
            this.videoRestictionInYourCountry = videoRestictionInYourCountry;
        }

        public String getTrack() {
            return track;
        }

        public void setTrack(String track) {
            this.track = track;
        }

        public String getTracklist() {
            return tracklist;
        }

        public void setTracklist(String tracklist) {
            this.tracklist = tracklist;
        }

        public String getTransactionTitle() {
            return transactionTitle;
        }

        public void setTransactionTitle(String transactionTitle) {
            this.transactionTitle = transactionTitle;
        }

        public String getTransactionDate() {
            return transactionDate;
        }

        public void setTransactionDate(String transactionDate) {
            this.transactionDate = transactionDate;
        }

        public String getTransactionDetail() {
            return transactionDetail;
        }

        public void setTransactionDetail(String transactionDetail) {
            this.transactionDetail = transactionDetail;
        }

        public String getTransactionStatus() {
            return transactionStatus;
        }

        public void setTransactionStatus(String transactionStatus) {
            this.transactionStatus = transactionStatus;
        }

        public String getTransaction() {
            return transaction;
        }

        public void setTransaction(String transaction) {
            this.transaction = transaction;
        }

        public String getTryAgain() {
            return tryAgain;
        }

        public void setTryAgain(String tryAgain) {
            this.tryAgain = tryAgain;
        }

        public String getTwitter() {
            return twitter;
        }

        public void setTwitter(String twitter) {
            this.twitter = twitter;
        }

        public String getTypeToSearch() {
            return typeToSearch;
        }

        public void setTypeToSearch(String typeToSearch) {
            this.typeToSearch = typeToSearch;
        }

        public String getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(String unitPrice) {
            this.unitPrice = unitPrice;
        }

        public String getUnpaid() {
            return unpaid;
        }

        public void setUnpaid(String unpaid) {
            this.unpaid = unpaid;
        }

        public String getUpdateTitle() {
            return updateTitle;
        }

        public void setUpdateTitle(String updateTitle) {
            this.updateTitle = updateTitle;
        }

        public String getBtnUpdateProfile() {
            return btnUpdateProfile;
        }

        public void setBtnUpdateProfile(String btnUpdateProfile) {
            this.btnUpdateProfile = btnUpdateProfile;
        }

        public String getUpload() {
            return upload;
        }

        public void setUpload(String upload) {
            this.upload = upload;
        }

        public String getUploadMethod() {
            return uploadMethod;
        }

        public void setUploadMethod(String uploadMethod) {
            this.uploadMethod = uploadMethod;
        }

        public String getUploadPoster() {
            return uploadPoster;
        }

        public void setUploadPoster(String uploadPoster) {
            this.uploadPoster = uploadPoster;
        }

        public String getUploadVideo() {
            return uploadVideo;
        }

        public void setUploadVideo(String uploadVideo) {
            this.uploadVideo = uploadVideo;
        }

        public String getUseNewCard() {
            return useNewCard;
        }

        public void setUseNewCard(String useNewCard) {
            this.useNewCard = useNewCard;
        }

        public String getUserDataRetrieved() {
            return userDataRetrieved;
        }

        public void setUserDataRetrieved(String userDataRetrieved) {
            this.userDataRetrieved = userDataRetrieved;
        }

        public String getUserNotFound() {
            return userNotFound;
        }

        public void setUserNotFound(String userNotFound) {
            this.userNotFound = userNotFound;
        }

        public String getUserIdRequired() {
            return userIdRequired;
        }

        public void setUserIdRequired(String userIdRequired) {
            this.userIdRequired = userIdRequired;
        }

        public String getUserInformationNotFound() {
            return userInformationNotFound;
        }

        public void setUserInformationNotFound(String userInformationNotFound) {
            this.userInformationNotFound = userInformationNotFound;
        }

        public String getUserLoginProfile() {
            return userLoginProfile;
        }

        public void setUserLoginProfile(String userLoginProfile) {
            this.userLoginProfile = userLoginProfile;
        }

        public String getUserProfile() {
            return userProfile;
        }

        public void setUserProfile(String userProfile) {
            this.userProfile = userProfile;
        }

        public String getViewMore() {
            return viewMore;
        }

        public void setViewMore(String viewMore) {
            this.viewMore = viewMore;
        }

        public String getViewall() {
            return viewall;
        }

        public void setViewall(String viewall) {
            this.viewall = viewall;
        }

        public String getSubscriptionBundleViewDetails() {
            return subscriptionBundleViewDetails;
        }

        public void setSubscriptionBundleViewDetails(String subscriptionBundleViewDetails) {
            this.subscriptionBundleViewDetails = subscriptionBundleViewDetails;
        }

        public String getViewLess() {
            return viewLess;
        }

        public void setViewLess(String viewLess) {
            this.viewLess = viewLess;
        }

        public String getViewTrailer() {
            return viewTrailer;
        }

        public void setViewTrailer(String viewTrailer) {
            this.viewTrailer = viewTrailer;
        }

        public String getLoading() {
            return loading;
        }

        public void setLoading(String loading) {
            this.loading = loading;
        }

        public String getVisitShop() {
            return visitShop;
        }

        public void setVisitShop(String visitShop) {
            this.visitShop = visitShop;
        }

        public String getVoucherAppliedOnForDownload() {
            return voucherAppliedOnForDownload;
        }

        public void setVoucherAppliedOnForDownload(String voucherAppliedOnForDownload) {
            this.voucherAppliedOnForDownload = voucherAppliedOnForDownload;
        }

        public String getVoucherAppliedSuccess() {
            return voucherAppliedSuccess;
        }

        public void setVoucherAppliedSuccess(String voucherAppliedSuccess) {
            this.voucherAppliedSuccess = voucherAppliedSuccess;
        }

        public String getVoucherCode() {
            return voucherCode;
        }

        public void setVoucherCode(String voucherCode) {
            this.voucherCode = voucherCode;
        }

        public String getVoucherAlreadyUsed() {
            return voucherAlreadyUsed;
        }

        public void setVoucherAlreadyUsed(String voucherAlreadyUsed) {
            this.voucherAlreadyUsed = voucherAlreadyUsed;
        }

        public String getVoucherWillBeAppliedOn() {
            return voucherWillBeAppliedOn;
        }

        public void setVoucherWillBeAppliedOn(String voucherWillBeAppliedOn) {
            this.voucherWillBeAppliedOn = voucherWillBeAppliedOn;
        }

        public String getWantToDownload() {
            return wantToDownload;
        }

        public void setWantToDownload(String wantToDownload) {
            this.wantToDownload = wantToDownload;
        }

        public String getWantToDelete() {
            return wantToDelete;
        }

        public void setWantToDelete(String wantToDelete) {
            this.wantToDelete = wantToDelete;
        }

        public String getWatch() {
            return watch;
        }

        public void setWatch(String watch) {
            this.watch = watch;
        }

        public String getWatchHistory() {
            return watchHistory;
        }

        public void setWatchHistory(String watchHistory) {
            this.watchHistory = watchHistory;
        }

        public String getWatchNow() {
            return watchNow;
        }

        public void setWatchNow(String watchNow) {
            this.watchNow = watchNow;
        }

        public String getBtnWatchNow() {
            return btnWatchNow;
        }

        public void setBtnWatchNow(String btnWatchNow) {
            this.btnWatchNow = btnWatchNow;
        }

        public String getWatchPeriodsEnds() {
            return watchPeriodsEnds;
        }

        public void setWatchPeriodsEnds(String watchPeriodsEnds) {
            this.watchPeriodsEnds = watchPeriodsEnds;
        }

        public String getUpdateProfileAlert() {
            return updateProfileAlert;
        }

        public void setUpdateProfileAlert(String updateProfileAlert) {
            this.updateProfileAlert = updateProfileAlert;
        }

        public String getSignOutAlert() {
            return signOutAlert;
        }

        public void setSignOutAlert(String signOutAlert) {
            this.signOutAlert = signOutAlert;
        }

        public String getApiSubscribeMsg() {
            return apiSubscribeMsg;
        }

        public void setApiSubscribeMsg(String apiSubscribeMsg) {
            this.apiSubscribeMsg = apiSubscribeMsg;
        }

        public String getYes() {
            return yes;
        }

        public void setYes(String yes) {
            this.yes = yes;
        }

        public String getActivateSubscriptionWatchVideo() {
            return activateSubscriptionWatchVideo;
        }

        public void setActivateSubscriptionWatchVideo(String activateSubscriptionWatchVideo) {
            this.activateSubscriptionWatchVideo = activateSubscriptionWatchVideo;
        }

        public String getDownloadedAccessExpired() {
            return downloadedAccessExpired;
        }

        public void setDownloadedAccessExpired(String downloadedAccessExpired) {
            this.downloadedAccessExpired = downloadedAccessExpired;
        }

        public String getPerimissionDenied() {
            return perimissionDenied;
        }

        public void setPerimissionDenied(String perimissionDenied) {
            this.perimissionDenied = perimissionDenied;
        }

        public String getAlreadyAddedFavourite() {
            return alreadyAddedFavourite;
        }

        public void setAlreadyAddedFavourite(String alreadyAddedFavourite) {
            this.alreadyAddedFavourite = alreadyAddedFavourite;
        }

        public String getAlreadyAddedYourReview() {
            return alreadyAddedYourReview;
        }

        public void setAlreadyAddedYourReview(String alreadyAddedYourReview) {
            this.alreadyAddedYourReview = alreadyAddedYourReview;
        }

        public String getAlreadyPurchasedContent() {
            return alreadyPurchasedContent;
        }

        public void setAlreadyPurchasedContent(String alreadyPurchasedContent) {
            this.alreadyPurchasedContent = alreadyPurchasedContent;
        }

        public String getPurchesedContent() {
            return purchesedContent;
        }

        public void setPurchesedContent(String purchesedContent) {
            this.purchesedContent = purchesedContent;
        }

        public String getLoggedOutFromAllDevices() {
            return loggedOutFromAllDevices;
        }

        public void setLoggedOutFromAllDevices(String loggedOutFromAllDevices) {
            this.loggedOutFromAllDevices = loggedOutFromAllDevices;
        }

        public String getIncorrectPassword() {
            return incorrectPassword;
        }

        public void setIncorrectPassword(String incorrectPassword) {
            this.incorrectPassword = incorrectPassword;
        }

        public String getIncorrectLoginCredential() {
            return incorrectLoginCredential;
        }

        public void setIncorrectLoginCredential(String incorrectLoginCredential) {
            this.incorrectLoginCredential = incorrectLoginCredential;
        }

        public String getCrossedMaxLimitOfWatching() {
            return crossedMaxLimitOfWatching;
        }

        public void setCrossedMaxLimitOfWatching(String crossedMaxLimitOfWatching) {
            this.crossedMaxLimitOfWatching = crossedMaxLimitOfWatching;
        }

        public String getExceedNoDevices() {
            return exceedNoDevices;
        }

        public void setExceedNoDevices(String exceedNoDevices) {
            this.exceedNoDevices = exceedNoDevices;
        }

        public String getLoginRestriction() {
            return loginRestriction;
        }

        public void setLoginRestriction(String loginRestriction) {
            this.loginRestriction = loginRestriction;
        }

        public String getYouSelected() {
            return youSelected;
        }

        public void setYouSelected(String youSelected) {
            this.youSelected = youSelected;
        }

        public String getPurchaseSuccessAlert() {
            return purchaseSuccessAlert;
        }

        public void setPurchaseSuccessAlert(String purchaseSuccessAlert) {
            this.purchaseSuccessAlert = purchaseSuccessAlert;
        }

        public String getNeedLoginToReview() {
            return needLoginToReview;
        }

        public void setNeedLoginToReview(String needLoginToReview) {
            this.needLoginToReview = needLoginToReview;
        }

        public String getSubscribeLoginMsg() {
            return subscribeLoginMsg;
        }

        public void setSubscribeLoginMsg(String subscribeLoginMsg) {
            this.subscribeLoginMsg = subscribeLoginMsg;
        }

        public String getYoutube() {
            return youtube;
        }

        public void setYoutube(String youtube) {
            this.youtube = youtube;
        }

        public String getYourDevice() {
            return yourDevice;
        }

        public void setYourDevice(String yourDevice) {
            this.yourDevice = yourDevice;
        }

        public String getAccountDeactivate() {
            return accountDeactivate;
        }

        public void setAccountDeactivate(String accountDeactivate) {
            this.accountDeactivate = accountDeactivate;
        }

        public String getRestrictStreamingDevice() {
            return restrictStreamingDevice;
        }

        public void setRestrictStreamingDevice(String restrictStreamingDevice) {
            this.restrictStreamingDevice = restrictStreamingDevice;
        }

        public String getCardWillCharge() {
            return cardWillCharge;
        }

        public void setCardWillCharge(String cardWillCharge) {
            this.cardWillCharge = cardWillCharge;
        }

        public String getTrielDesc() {
            return trielDesc;
        }

        public void setTrielDesc(String trielDesc) {
            this.trielDesc = trielDesc;
        }

        public String getFreeForCoupon() {
            return freeForCoupon;
        }

        public void setFreeForCoupon(String freeForCoupon) {
            this.freeForCoupon = freeForCoupon;
        }

        public String getAutoPlayInfo() {
            return autoPlayInfo;
        }

        public void setAutoPlayInfo(String autoPlayInfo) {
            this.autoPlayInfo = autoPlayInfo;
        }

        public String getDownloadCompleteMsg() {
            return downloadCompleteMsg;
        }

        public void setDownloadCompleteMsg(String downloadCompleteMsg) {
            this.downloadCompleteMsg = downloadCompleteMsg;
        }

        public String getSubscriptionCompleted() {
            return subscriptionCompleted;
        }

        public void setSubscriptionCompleted(String subscriptionCompleted) {
            this.subscriptionCompleted = subscriptionCompleted;
        }

        public String getYourVideoCanNotBeSaved() {
            return yourVideoCanNotBeSaved;
        }

        public void setYourVideoCanNotBeSaved(String yourVideoCanNotBeSaved) {
            this.yourVideoCanNotBeSaved = yourVideoCanNotBeSaved;
        }

        public String getZtoa() {
            return ztoa;
        }

        public void setZtoa(String ztoa) {
            this.ztoa = ztoa;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getOff() {
            return off;
        }

        public void setOff(String off) {
            this.off = off;
        }

        public String getReleasedate() {
            return releasedate;
        }

        public void setReleasedate(String releasedate) {
            this.releasedate = releasedate;
        }

        public String getSearchHint() {
            return searchHint;
        }

        public void setSearchHint(String searchHint) {
            this.searchHint = searchHint;
        }

        public String getTerms() {
            return terms;
        }

        public void setTerms(String terms) {
            this.terms = terms;
        }

        public String getToLogin() {
            return toLogin;
        }

        public void setToLogin(String toLogin) {
            this.toLogin = toLogin;
        }
    }
}
