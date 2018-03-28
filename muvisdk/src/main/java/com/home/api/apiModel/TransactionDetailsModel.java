package com.home.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This Model Class Holds All The Attributes For Transaction detail
 *
 * @author Abhishek
 */

public class TransactionDetailsModel {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("section")
    @Expose
    private Section section;

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
     * This method is used to get the section
     *
     * @return section
     */
    public Section getSection() {
        return section;
    }

    /**
     * This method is used to set the section
     *
     * @param section For setting the section
     */
    public void setSection(Section section) {
        this.section = section;
    }

    public class Section {

        @SerializedName("payment_method")
        @Expose
        private String paymentMethod = "";
        @SerializedName("transaction_status")
        @Expose
        private String transactionStatus = "";
        @SerializedName("transaction_date")
        @Expose
        private String transactionDate = "";
        @SerializedName("amount")
        @Expose
        private String amount = "";
        @SerializedName("currency_symbol")
        @Expose
        private String currencySymbol = "";
        @SerializedName("currency_code")
        @Expose
        private String currencyCode = "";
        @SerializedName("invoice_id")
        @Expose
        private String invoiceId = "";
        @SerializedName("order_number")
        @Expose
        private String orderNumber = "";
        @SerializedName("plan_name")
        @Expose
        private String planName = "";
        @SerializedName("plan_recurrence")
        @Expose
        private String planRecurrence = "";

        /**
         * This method is used to get the payment method
         *
         * @return paymentMethod
         */
        public String getPaymentMethod() {
            return paymentMethod;
        }

        /**
         * This method is used to set the payment method
         *
         * @param paymentMethod For setting the payment method
         */
        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
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
         * This method is used to get the order number
         *
         * @return orderNumber
         */
        public String getOrderNumber() {
            return orderNumber;
        }

        /**
         * This method is used to set the order number
         *
         * @param orderNumber For setting the order number
         */
        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        /**
         * This method is used to get the plan name
         *
         * @return planName
         */
        public String getPlanName() {
            return planName;
        }

        /**
         * This method is used to set the plan name
         *
         * @param planName For setting the plan name
         */
        public void setPlanName(String planName) {
            this.planName = planName;
        }

        /**
         * This method is used to get the plan recurrence
         *
         * @return planRecurrence
         */
        public String getPlanRecurrence() {
            return planRecurrence;
        }

        /**
         * This method is used to set the plan recurrence
         *
         * @param planRecurrence For setting the plan recurrence
         */
        public void setPlanRecurrence(String planRecurrence) {
            this.planRecurrence = planRecurrence;
        }

    }
}
