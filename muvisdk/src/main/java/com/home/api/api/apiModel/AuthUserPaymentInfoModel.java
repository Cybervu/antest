package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This Model Class Holds All The Attributes For Auth User Payment Information
 *
 * @author Abhishek
 */

public class AuthUserPaymentInfoModel {

    @SerializedName("isSuccess")
    @Expose
    private int isSuccess = 0;

    @SerializedName("card")
    @Expose
    private Card card;

    @SerializedName("Message")
    @Expose
    private String Message;
    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }



    /**
     * This method is used to get the success message
     *
     * @return isSuccess
     */
    public Integer getIsSuccess() {
        return isSuccess;
    }

    /**
     * This method is used to set the success message
     *
     * @param isSuccess For setting the success message
     */

    public void setIsSuccess(Integer isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     * This method is used to get the card list
     *
     * @return card
     */

    public Card getCard() {
        return card;
    }

    /**
     * This method is used to set the card list
     *
     * @param card For setting the card list
     */

    public void setCard(Card card) {
        this.card = card;
    }

    public class Card {

        @SerializedName("code")
        @Expose
        private Integer code = 0;
        @SerializedName("status")
        @Expose
        private String status = "";
        @SerializedName("profile_id")
        @Expose
        private String profileId = "";
        @SerializedName("card_type")
        @Expose
        private String cardType = "";
        @SerializedName("card_last_fourdigit")
        @Expose
        private String cardLastFourdigit = "";
        @SerializedName("token")
        @Expose
        private String token = "";
        @SerializedName("response_text")
        @Expose
        private String responseText = "";
        @SerializedName("transaction_invoice_id")
        @Expose
        private String transaction_invoice_id;
        @SerializedName("transaction_order_number")
        @Expose
        private String transaction_order_number;
        @SerializedName("transaction_dollar_amount")
        @Expose
        private String transaction_dollar_amount;
        @SerializedName("transaction_amount")
        @Expose
        private String transaction_amount;
        @SerializedName("transaction_response_text")
        @Expose
        private String transaction_response_text;

        public String getTransaction_invoice_id() {
            return transaction_invoice_id;
        }

        public void setTransaction_invoice_id(String transaction_invoice_id) {
            this.transaction_invoice_id = transaction_invoice_id;
        }

        public String getTransaction_order_number() {
            return transaction_order_number;
        }

        public void setTransaction_order_number(String transaction_order_number) {
            this.transaction_order_number = transaction_order_number;
        }

        public String getTransaction_dollar_amount() {
            return transaction_dollar_amount;
        }

        public void setTransaction_dollar_amount(String transaction_dollar_amount) {
            this.transaction_dollar_amount = transaction_dollar_amount;
        }

        public String getTransaction_amount() {
            return transaction_amount;
        }

        public void setTransaction_amount(String transaction_amount) {
            this.transaction_amount = transaction_amount;
        }

        public String getTransaction_response_text() {
            return transaction_response_text;
        }

        public void setTransaction_response_text(String transaction_response_text) {
            this.transaction_response_text = transaction_response_text;
        }

        public String getTransaction_is_success() {
            return transaction_is_success;
        }

        public void setTransaction_is_success(String transaction_is_success) {
            this.transaction_is_success = transaction_is_success;
        }

        public String getTransaction_status() {
            return transaction_status;
        }

        public void setTransaction_status(String transaction_status) {
            this.transaction_status = transaction_status;
        }

        public String getIsSuccess() {
            return isSuccess;
        }

        public void setIsSuccess(String isSuccess) {
            this.isSuccess = isSuccess;
        }

        @SerializedName("transaction_is_success")
        @Expose
        private String transaction_is_success;
        @SerializedName("transaction_status")
        @Expose
        private String transaction_status;
        @SerializedName("isSuccess")
        @Expose
        private String isSuccess;

        /**
         * This method is used to get the server code
         *
         * @return code
         */

        public Integer getCode() {
            return code;
        }

        /**
         * This method is used to set the server code
         *
         * @param code For setting the server code
         */

        public void setCode(Integer code) {
            this.code = code;
        }

        /**
         * This method is used to get the status
         *
         * @return status
         */

        public String getStatus() {
            return status;
        }

        /**
         * This method is used to set the status
         *
         * @param status For setting the status
         */

        public void setStatus(String status) {
            this.status = status;
        }

        /**
         * This method is used to get the profile Id
         *
         * @return profileId
         */

        public String getProfileId() {
            return profileId;
        }

        /**
         * This method is used to set the profile id
         *
         * @param profileId For setting the profile Id
         */

        public void setProfileId(String profileId) {
            this.profileId = profileId;
        }

        /**
         * This method is used to get the card type
         *
         * @return cardType
         */

        public String getCardType() {
            return cardType;
        }

        /**
         * This method is used to set tghe card type
         *
         * @param cardType For setting the card type
         */

        public void setCardType(String cardType) {
            this.cardType = cardType;
        }

        /**
         * This method is used to get the card last four digit number
         *
         * @return cardLastFourdigit
         */

        public String getCardLastFourdigit() {
            return cardLastFourdigit;
        }

        /**
         * This method is used to set the card last four digit number
         *
         * @param cardLastFourdigit For setting the card last four digit number
         */

        public void setCardLastFourdigit(String cardLastFourdigit) {
            this.cardLastFourdigit = cardLastFourdigit;
        }

        /**
         * This method is used to get the token
         *
         * @return token
         */

        public String getToken() {
            return token;
        }

        /**
         * This method is used to set the token
         *
         * @param token For setting the token
         */

        public void setToken(String token) {
            this.token = token;
        }

        /**
         * This method is used to get the response text
         *
         * @return responseText
         */

        public String getResponseText() {
            return responseText;
        }

        /**
         * This method is used to set the response text
         *
         * @param responseText For setting the response text
         */

        public void setResponseText(String responseText) {
            this.responseText = responseText;
        }
    }
}
