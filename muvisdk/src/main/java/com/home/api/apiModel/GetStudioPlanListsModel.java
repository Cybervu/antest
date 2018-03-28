package com.home.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This Model Class Holds All The Attributes For Studio Plan List
 *
 * @author Abhishek
 */

public class GetStudioPlanListsModel {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("plans")
    @Expose
    private ArrayList<Plan> plans = null;
    @SerializedName("default_plan")
    @Expose
    private String defaultPlan = "";

    /**
     * This method is used to get the gateways
     *
     * @return gateways
     */
    public String getGateways() {
        return gateways;
    }

    /**
     * This method is used to set the gateways
     *
     * @param gateways For setting the gateways
     */
    public void setGateways(String gateways) {
        this.gateways = gateways;
    }

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

    @SerializedName("gateways")
    @Expose
    private String gateways = "";
    @SerializedName("msg")
    @Expose
    private String msg = "";

    /**
     * This method is used to get the code
     *
     * @return code
     */
    public Integer getCode() {
        return code;
    }

    /**
     * This method is used to set the code
     *
     * @param code For setting the code
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
     * This method is used to get the array list of plans
     *
     * @return plans
     */
    public ArrayList<Plan> getPlans() {
        return plans;
    }

    /**
     * This method is used to set the array list of plans
     *
     * @param plans For setting the plans
     */
    public void setPlans(ArrayList<Plan> plans) {
        this.plans = plans;
    }

    /**
     * This method is used to get the default plan
     *
     * @return defaultPlan
     */
    public String getDefaultPlan() {
        return defaultPlan;
    }

    /**
     * This method is used to set the default plan
     *
     * @param defaultPlan For setting the default plan
     */
    public void setDefaultPlan(String defaultPlan) {
        this.defaultPlan = defaultPlan;
    }

    public class Currency {

        @SerializedName("id")
        @Expose
        private String id = "";
        @SerializedName("country_code")
        @Expose
        private String countryCode = "";
        @SerializedName("code")
        @Expose
        private String code = "";
        @SerializedName("title")
        @Expose
        private String title = "";
        @SerializedName("symbol")
        @Expose
        private String symbol = "";
        @SerializedName("iso_num")
        @Expose
        private String isoNum = "";

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
         * This method is used to get the country code
         *
         * @return countryCode
         */
        public String getCountryCode() {
            return countryCode;
        }

        /**
         * This method is used to set the country code
         *
         * @param countryCode For setting the country code
         */
        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        /**
         * This method is used to get the code
         *
         * @return code
         */
        public String getCode() {
            return code;
        }

        /**
         * This method is used to set the code
         *
         * @param code For setting the code
         */
        public void setCode(String code) {
            this.code = code;
        }

        /**
         * This method is used to get the title
         *
         * @return title
         */
        public String getTitle() {
            return title;
        }

        /**
         * This method is used to set the title
         *
         * @param title For setting the title
         */
        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * This method is used to get the symbol
         *
         * @return symbol
         */
        public String getSymbol() {
            return symbol;
        }

        /**
         * This method is used to set the symbol
         *
         * @param symbol For setting the symbol
         */
        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getIsoNum() {
            return isoNum;
        }

        public void setIsoNum(String isoNum) {
            this.isoNum = isoNum;
        }

    }

    public class Plan {

        @SerializedName("id")
        @Expose
        private String id = "";
        @SerializedName("unique_id")
        @Expose
        private String uniqueId = "";
        @SerializedName("name")
        @Expose
        private String name = "";
        @SerializedName("short_desc")
        @Expose
        private String shortDesc = "";
        @SerializedName("duration")
        @Expose
        private String duration = "";
        @SerializedName("recurrence")
        @Expose
        private String recurrence = "";
        @SerializedName("frequency")
        @Expose
        private String frequency = "";
        @SerializedName("startup_discount")
        @Expose
        private String startupDiscount = "";
        @SerializedName("monthly_fee")
        @Expose
        private String monthlyFee = "";
        @SerializedName("studio_id")
        @Expose
        private String studioId = "";
        @SerializedName("status")
        @Expose
        private String status = "";
        @SerializedName("trial_recurrence")
        @Expose
        private String trialRecurrence = "";
        @SerializedName("trial_period")
        @Expose
        private String trialPeriod = "";
        @SerializedName("is_post_paid")
        @Expose
        private String isPostPaid = "";
        @SerializedName("ip")
        @Expose
        private String ip = "";
        @SerializedName("created_by")
        @Expose
        private String createdBy = "";
        @SerializedName("created_date")
        @Expose
        private String createdDate = "";
        @SerializedName("last_updated_by")
        @Expose
        private String lastUpdatedBy = "";
        @SerializedName("last_updated_date")
        @Expose
        private String lastUpdatedDate = "";
        @SerializedName("language_id")
        @Expose
        private String languageId = "";
        @SerializedName("parent_id")
        @Expose
        private String parentId = "";
        @SerializedName("rules_id")
        @Expose
        private String rulesId = "";
        @SerializedName("is_default")
        @Expose
        private String isDefault = "";
        @SerializedName("id_sequency")
        @Expose
        private String idSequency = "";
        @SerializedName("is_subscription_bundle")
        @Expose
        private String isSubscriptionBundle = "";
        @SerializedName("price")
        @Expose
        private String price = "";
        @SerializedName("currency")
        @Expose
        private Currency currency;

        /**
         * This method is used to Get the id
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
         * This method is used to get the unique id
         *
         * @return uniqueId
         */
        public String getUniqueId() {
            return uniqueId;
        }

        /**
         * This method is used to set the unique id
         *
         * @param uniqueId For setting the unique id
         */
        public void setUniqueId(String uniqueId) {
            this.uniqueId = uniqueId;
        }

        /**
         * This method is used to get the name
         *
         * @return name
         */
        public String getName() {
            return name;
        }

        /**
         * This method is used to set the name
         *
         * @param name For setting the name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * This method is used to get the descending order
         *
         * @return shortDesc
         */
        public String getShortDesc() {
            return shortDesc;
        }

        /**
         * This method is used to set the descending order
         *
         * @param shortDesc For setting the descending order
         */
        public void setShortDesc(String shortDesc) {
            this.shortDesc = shortDesc;
        }

        /**
         * This method is used to get the duration
         *
         * @return duration
         */
        public String getDuration() {
            return duration;
        }

        /**
         * This method is used to set the duration
         *
         * @param duration For setting the duration
         */
        public void setDuration(String duration) {
            this.duration = duration;
        }

        /**
         * This method is used to get the recurrence
         *
         * @return recurrence
         */
        public String getRecurrence() {
            return recurrence;
        }

        /**
         * This method is used to set the recurrence
         *
         * @param recurrence For setting the recurrence
         */
        public void setRecurrence(String recurrence) {
            this.recurrence = recurrence;
        }

        /**
         * This method is used to get the frequency
         *
         * @return frequency
         */
        public String getFrequency() {
            return frequency;
        }

        /**
         * This method is used to set the frequency
         *
         * @param frequency For setting the frequency
         */
        public void setFrequency(String frequency) {
            this.frequency = frequency;
        }

        /**
         * This method is used to get the start up discount
         *
         * @return startupDiscount
         */
        public String getStartupDiscount() {
            return startupDiscount;
        }

        /**
         * This method is used to set the start up discount
         *
         * @param startupDiscount For setting the start up discount
         */
        public void setStartupDiscount(String startupDiscount) {
            this.startupDiscount = startupDiscount;
        }

        /**
         * This method is used to get the monthly fee
         *
         * @return monthlyFee
         */
        public String getMonthlyFee() {
            return monthlyFee;
        }

        /**
         * This method is used to set the monthly fee
         *
         * @param monthlyFee For setting the monthly fee
         */
        public void setMonthlyFee(String monthlyFee) {
            this.monthlyFee = monthlyFee;
        }

        /**
         * This method is used to get the studio id
         *
         * @return studioId
         */
        public String getStudioId() {
            return studioId;
        }

        /**
         * This method is used to set the studio id
         *
         * @param studioId For setting the studio id
         */
        public void setStudioId(String studioId) {
            this.studioId = studioId;
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
         * @param status For setting status
         */
        public void setStatus(String status) {
            this.status = status;
        }

        /**
         * This method is used to get the trail recurrence
         *
         * @return trialRecurrence
         */
        public String getTrialRecurrence() {
            return trialRecurrence;
        }

        /**
         * This method is used to set the trail recurrence
         *
         * @param trialRecurrence For setting the recurrence
         */
        public void setTrialRecurrence(String trialRecurrence) {
            this.trialRecurrence = trialRecurrence;
        }

        /**
         * This method is used to get the trail period
         *
         * @return trialPeriod
         */
        public String getTrialPeriod() {
            return trialPeriod;
        }

        /**
         * This method is used to set the trail period
         *
         * @param trialPeriod For setting the trail period
         */
        public void setTrialPeriod(String trialPeriod) {
            this.trialPeriod = trialPeriod;
        }

        /**
         * This method is used to get the post paid
         *
         * @return isPostPaid
         */
        public String getIsPostPaid() {
            return isPostPaid;
        }

        /**
         * This method is used to set the post paid
         *
         * @param isPostPaid For setting the post paid
         */
        public void setIsPostPaid(String isPostPaid) {
            this.isPostPaid = isPostPaid;
        }

        /**
         * This method is used to get the ip
         *
         * @return ip
         */
        public String getIp() {
            return ip;
        }

        /**
         * This method is used to set the ip
         *
         * @param ip For setting the ip
         */
        public void setIp(String ip) {
            this.ip = ip;
        }

        /**
         * This method is used to get the created details
         *
         * @return createdBy
         */
        public String getCreatedBy() {
            return createdBy;
        }

        /**
         * This method is used to set the created details
         *
         * @param createdBy For setting the created details
         */
        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        /**
         * This method is used to get the created date
         *
         * @return createdDate
         */
        public String getCreatedDate() {
            return createdDate;
        }

        /**
         * This method is used to set the created date
         *
         * @param createdDate For setting the created date
         */
        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        /**
         * This method is used to get the Last update details
         *
         * @return lastUpdatedBy
         */
        public String getLastUpdatedBy() {
            return lastUpdatedBy;
        }

        /**
         * This method is used to set the Last update details
         *
         * @param lastUpdatedBy For setting the last update details
         */
        public void setLastUpdatedBy(String lastUpdatedBy) {
            this.lastUpdatedBy = lastUpdatedBy;
        }

        /**
         * This method is used to get the Last update date
         *
         * @return lastUpdatedDate
         */
        public String getLastUpdatedDate() {
            return lastUpdatedDate;
        }

        /**
         * This method is used to set the Last update date
         *
         * @param lastUpdatedDate For setting the last update details
         */
        public void setLastUpdatedDate(String lastUpdatedDate) {
            this.lastUpdatedDate = lastUpdatedDate;
        }

        /**
         * This method is used to get the Language id
         *
         * @return languageId
         */
        public String getLanguageId() {
            return languageId;
        }

        /**
         * This method is used to set the Language id
         *
         * @param languageId For setting the language id
         */
        public void setLanguageId(String languageId) {
            this.languageId = languageId;
        }

        /**
         * This method is used to get the parent id
         *
         * @return parentId
         */
        public String getParentId() {
            return parentId;
        }

        /**
         * This method is used to set the parent id
         *
         * @param parentId For setting the parent id
         */
        public void setParentId(String parentId) {
            this.parentId = parentId;
        }

        /**
         * This method is used to get the rules id
         *
         * @return rulesId
         */
        public String getRulesId() {
            return rulesId;
        }

        /**
         * This method is used to set the rules id
         *
         * @param rulesId For setting the rule id
         */
        public void setRulesId(String rulesId) {
            this.rulesId = rulesId;
        }

        /**
         * This method is used to get the default details
         *
         * @return isDefault
         */
        public String getIsDefault() {
            return isDefault;
        }

        /**
         * This method is used to set the default details
         *
         * @param isDefault For setting the default details
         */
        public void setIsDefault(String isDefault) {
            this.isDefault = isDefault;
        }

        /**
         * This method is used to get the sequence id
         *
         * @return idSequency
         */
        public String getIdSequency() {
            return idSequency;
        }

        /**
         * This method is used to set the sequence id
         *
         * @param idSequency For setting the sequence id
         */
        public void setIdSequency(String idSequency) {
            this.idSequency = idSequency;
        }

        /**
         * This method is used to get the subscription bundle
         *
         * @return isSubscriptionBundle
         */
        public String getIsSubscriptionBundle() {
            return isSubscriptionBundle;
        }

        /**
         * This method is used to set the subscription bundle
         *
         * @param isSubscriptionBundle For setting the subscription bundle
         */
        public void setIsSubscriptionBundle(String isSubscriptionBundle) {
            this.isSubscriptionBundle = isSubscriptionBundle;
        }

        /**
         * This method is used to get the price
         *
         * @return price
         */
        public String getPrice() {
            return price;
        }

        /**
         * This method is used to set the price
         *
         * @param price For setting the price
         */
        public void setPrice(String price) {
            this.price = price;
        }

        /**
         * This method is used to get the currency
         *
         * @return currency
         */
        public Currency getCurrency() {
            return currency;
        }

        /**
         * This method is used to set the currency
         *
         * @param currency For setting the currency
         */
        public void setCurrency(Currency currency) {
            this.currency = currency;
        }

        @Override
        public String toString() {
            return "plan id ::" + getId() + "  Name :: " + getName() + " Frequency :: " + getFrequency();
        }
    }
}
