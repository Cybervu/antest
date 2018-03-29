package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This Model Class Holds All The Attributes For Validate Coupon code
 *
 * @author Abhishek
 */

public class ValidateCouponCodeModel {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("msg")
    @Expose
    private String msg = "";
    @SerializedName("discount")
    @Expose
    private String discount = "";
    @SerializedName("discount_type")
    @Expose
    private String discountType = "";

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
     * This method is used to get the discount
     *
     * @return discount
     */
    public String getDiscount() {
        return discount;
    }

    /**
     * This method is used to set the discount
     *
     * @param discount For setting the discount
     */
    public void setDiscount(String discount) {
        this.discount = discount;
    }

    /**
     * This method is used to get the discount type
     *
     * @return discountType
     */
    public String getDiscountType() {
        return discountType;
    }

    /**
     * This method is used to set the discount type
     *
     * @param discountType For setting the discount type
     */
    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
}
