package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This Model Class Holds All The Attributes For Monetization
 *
 * @author Abhishek
 */

public class GetMonetizationDetailsModel {

    @SerializedName("msg")
    @Expose
    private String msg = "";

    @SerializedName("code")
    @Expose
    private Integer code = 0;

    @SerializedName("status")
    @Expose
    private String status = "";

   /* @SerializedName("items")
    @Expose
    private Items items;
*/
    /**
     * This method is used to get the message
     *
     * @return msg
     */

    public String getMsg() {
        return msg;
    }

    /**
     * This method is used to set the message
     *
     * @param msg For setting the message
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

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
     * This method is used to get the items
     *
     * @return items
     */
/*
    public Items getItems() {
        return items;
    }

    *//**
     * This method is used to set the items
     *
     * @param items For setting the items
     *//*

    public void setItems(Items items) {
        this.items = items;
    }*/


    @SerializedName("monetizations")
    @Expose
    private Monetizations monetizations;
    @SerializedName("monetization_plans")
    @Expose
    private MonetizationPlans monetizationPlans;

    /**
     * This method is used to get the monitization
     *
     * @return monetizations
     */
    public Monetizations getMonetizations() {
        return monetizations;
    }

    /**
     * This method is used to set the monitization
     *
     * @param monetizations For setting the monitization
     */
    public void setMonetizations(Monetizations monetizations) {
        this.monetizations = monetizations;
    }

    /**
     * This method is used to get the monitization plans
     *
     * @return monetizationPlans
     */
    public MonetizationPlans getMonetizationPlans() {
        return monetizationPlans;
    }

    /**
     * This method is used to set the monitization plans
     *
     * @param monetizationPlans For setting the monitization plan
     */

    public void setMonetizationPlans(MonetizationPlans monetizationPlans) {
        this.monetizationPlans = monetizationPlans;
    }
   /* public class Items {



    }
*/
    public class MonetizationPlans {

        @SerializedName("ppv")
        @Expose
        private String ppv = "";
        @SerializedName("pre_order")
        @Expose
        private String preOrder = "";
        @SerializedName("voucher")
        @Expose
        private Integer voucher = 0;
        @SerializedName("ppv_bundle")
        @Expose
        private String ppvBundle = "";
        @SerializedName("subscription_bundles")
        @Expose
        private String subscription_bundles;

        /**
         * This method is used to get the ppv details
         *
         * @return ppv
         */
        public String getPpv() {
            return ppv;
        }

        /**
         * This method is used to set the ppv details
         *
         * @param ppv For setting the ppv details
         */

        public void setPpv(String ppv) {
            this.ppv = ppv;
        }

        /**
         * This method is used to get the pre order
         *
         * @return preOrder
         */
        public String getPreOrder() {
            return preOrder;
        }

        /**
         * This method is used to set the pre order
         *
         * @param preOrder For setting the pre order
         */
        public void setPreOrder(String preOrder) {
            this.preOrder = preOrder;
        }

        /**
         * This method is used to get the voucher
         *
         * @return voucher
         */
        public Integer getVoucher() {
            return voucher;
        }

        /**
         * This method is used to set the voucher
         *
         * @param voucher For setting the voucher
         */
        public void setVoucher(Integer voucher) {
            this.voucher = voucher;
        }

        /**
         * This method is used to get the ppv bundle
         *
         * @return ppvBundle
         */
        public String getPpvBundle() {
            return ppvBundle;
        }

        /**
         * This method is used to set the ppv bundle
         *
         * @param ppvBundle For setting the ppv bundle
         */
        public void setPpvBundle(String ppvBundle) {
            this.ppvBundle = ppvBundle;
        }

    }

    public class Monetizations {

        @SerializedName("subscription_bundles")
        @Expose
        private String subscriptionBundles = "";

        /**
         * This method is used to get the subscription bundle
         *
         * @return subscriptionBundles
         */
        public String getSubscriptionBundles() {
            return subscriptionBundles;
        }

        /**
         * This method is used to set the subscription bundle
         *
         * @param subscriptionBundles For setting the subscription bundle
         */
        public void setSubscriptionBundles(String subscriptionBundles) {
            this.subscriptionBundles = subscriptionBundles;
        }

    }
}
