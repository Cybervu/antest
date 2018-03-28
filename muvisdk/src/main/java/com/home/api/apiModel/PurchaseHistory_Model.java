package com.home.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This Model Class Holds All The Attributes For Purchase History
 *
 * @author Abhishek
 */

public class PurchaseHistory_Model {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("section")
    @Expose
    private ArrayList<Section> section = null;

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
     * This method is used to get the array list of section
     *
     * @return section
     */
    public ArrayList<Section> getSection() {
        return section;
    }

    /**
     * This method is used to set the array list of section
     *
     * @param section For setting the section
     */
    public void setSection(ArrayList<Section> section) {
        this.section = section;
    }

    public class Section {

        @SerializedName("invoice_id")
        @Expose
        private String invoiceId = "";
        @SerializedName("transaction_date")
        @Expose
        private String transactionDate = "";
        @SerializedName("amount")
        @Expose
        private String amount = "";
        @SerializedName("transaction_status")
        @Expose
        private String transactionStatus = "";
        @SerializedName("currency_symbol")
        @Expose
        private String currencySymbol = "";
        @SerializedName("currency_code")
        @Expose
        private String currencyCode = "";
        @SerializedName("id")
        @Expose
        private String id = "";
        @SerializedName("movie_name")
        @Expose
        private String movieName = "";
        @SerializedName("expiry_dateppv")
        @Expose
        private String expiryDateppv = "";
        @SerializedName("statusppv")
        @Expose
        private String statusppv = "";
        @SerializedName("Content_type")
        @Expose
        private String contentType = "";
        @SerializedName("transaction_for")
        @Expose
        private String transactionFor = "";

        /**
         * This method is used to get the invoice id
         *
         * @return invoiceId
         */
        public String getInvoiceId() {
            return invoiceId;
        }

        /**
         * This method is used to set the invoice id
         *
         * @param invoiceId For setting the invoice id
         */
        public void setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
        }

        /**
         * This method is used to get the transaction date
         *
         * @return transactionDate
         */
        public String getTransactionDate() {
            return transactionDate;
        }

        /**
         * This method is used to set the transaction date
         *
         * @param transactionDate For setting the transaction date
         */
        public void setTransactionDate(String transactionDate) {
            this.transactionDate = transactionDate;
        }

        /**
         * This method is used to get the amount
         *
         * @return amount
         */
        public String getAmount() {
            return amount;
        }

        /**
         * This method is used to set the amount
         *
         * @param amount For setting the amount
         */
        public void setAmount(String amount) {
            this.amount = amount;
        }

        /**
         * This method is used to get the transaction status
         *
         * @return transactionStatus
         */
        public String getTransactionStatus() {
            return transactionStatus;
        }

        /**
         * This method is used to set the transaction status
         *
         * @param transactionStatus For setting the transaction status
         */
        public void setTransactionStatus(String transactionStatus) {
            this.transactionStatus = transactionStatus;
        }

        /**
         * This method is used to get the currency symbol
         *
         * @return currencySymbol
         */
        public String getCurrencySymbol() {
            return currencySymbol;
        }

        /**
         * This method is used to set the currency symbol
         *
         * @param currencySymbol For setting the currency symbol
         */
        public void setCurrencySymbol(String currencySymbol) {
            this.currencySymbol = currencySymbol;
        }

        /**
         * This method is used to get the currency code
         *
         * @return currencyCode
         */
        public String getCurrencyCode() {
            return currencyCode;
        }

        /**
         * This method is used to set the currency code
         *
         * @param currencyCode For setting the currency code
         */
        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        /**
         * This method is used to get the id
         *
         * @return id
         */
        public String getId() {
            return id;
        }

        /**
         * This method is used to set the id
         *
         * @param id For setting the id
         */
        public void setId(String id) {
            this.id = id;
        }

        /**
         * This method is used to get the movie name
         *
         * @return movieName
         */
        public String getMovieName() {
            return movieName;
        }

        /**
         * This method is used to set the movie name
         *
         * @param movieName For setting the movie name
         */
        public void setMovieName(String movieName) {
            this.movieName = movieName;
        }

        /**
         * This method is used to get the expiry date ppv
         *
         * @return expiryDateppv
         */
        public String getExpiryDateppv() {
            return expiryDateppv;
        }

        /**
         * This method is used to set the expiry date ppv
         *
         * @param expiryDateppv For setting the expiry date for ppv
         */
        public void setExpiryDateppv(String expiryDateppv) {
            this.expiryDateppv = expiryDateppv;
        }

        /**
         * This method is used to get the status for ppv
         *
         * @return statusppv
         */
        public String getStatusppv() {
            return statusppv;
        }

        /**
         * This method is used to set the status for ppv
         *
         * @param statusppv For setting the status for ppv
         */
        public void setStatusppv(String statusppv) {
            this.statusppv = statusppv;
        }

        /**
         * This method is used to get the content type
         *
         * @return contentType
         */
        public String getContentType() {
            return contentType;
        }

        /**
         * This method is used to set the content type
         *
         * @param contentType For setting the content type
         */
        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        /**
         * This method is used to get the transaction details
         *
         * @return transactionFor
         */
        public String getTransactionFor() {
            return transactionFor;
        }

        /**
         * This method is used to set the transaction details
         *
         * @param transactionFor For setting the transaction details
         */
        public void setTransactionFor(String transactionFor) {
            this.transactionFor = transactionFor;
        }

    }
}
