package com.home.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This Model Class Holds All The Attributes For Manage Device
 *
 * @author Abhishek
 */

public class ManageDeviceModel {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("msg")
    @Expose
    private String msg = "";
    @SerializedName("device_list")
    @Expose
    private ArrayList<DeviceList> deviceList = null;

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
     * This method is used to get the array list of Device
     *
     * @return deviceList
     */
    public ArrayList<DeviceList> getDeviceList() {
        return deviceList;
    }

    /**
     * This method is used to set the array list of Device
     *
     * @param deviceList For setting the device list
     */
    public void setDeviceList(ArrayList<DeviceList> deviceList) {
        this.deviceList = deviceList;
    }

    public class DeviceList {

        @SerializedName("id")
        @Expose
        private String id = "";
        @SerializedName("user_id")
        @Expose
        private String userId = "";
        @SerializedName("studio_id")
        @Expose
        private String studioId = "";
        @SerializedName("device")
        @Expose
        private String device = "";
        @SerializedName("device_info")
        @Expose
        private String deviceInfo = "";
        @SerializedName("device_type")
        @Expose
        private String deviceType = "";
        @SerializedName("google_id")
        @Expose
        private String googleId = "";
        @SerializedName("created_by")
        @Expose
        private String createdBy = "";
        @SerializedName("created_date")
        @Expose
        private String createdDate = "";
        @SerializedName("other")
        @Expose
        private String other = "";
        @SerializedName("flag")
        @Expose
        private String flag = "";
        @SerializedName("deleted_date")
        @Expose
        private String deletedDate = "";

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
         * This method is used to get the user id
         *
         * @return userId
         */
        public String getUserId() {
            return userId;
        }

        /**
         * This method is used to set the user id
         *
         * @param userId For setting the user id
         */
        public void setUserId(String userId) {
            this.userId = userId;
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
         * This method is used to get the device
         *
         * @return device
         */
        public String getDevice() {
            return device;
        }

        /**
         * This method is used to set the device
         *
         * @param device For setting the device
         */
        public void setDevice(String device) {
            this.device = device;
        }

        /**
         * This method is used to get the device information
         *
         * @return deviceInfo
         */
        public String getDeviceInfo() {
            return deviceInfo;
        }

        /**
         * This method is used to set the device information
         *
         * @param deviceInfo For setting the device information
         */
        public void setDeviceInfo(String deviceInfo) {
            this.deviceInfo = deviceInfo;
        }

        /**
         * This method is used to get the device type
         *
         * @return deviceType
         */
        public String getDeviceType() {
            return deviceType;
        }

        /**
         * This method is used to set the device type
         *
         * @param deviceType For setting the device type
         */
        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        /**
         * This method is used to get the google id
         *
         * @return googleId
         */
        public String getGoogleId() {
            return googleId;
        }

        /**
         * This method is used to set the google id
         *
         * @param googleId For setting the google id
         */
        public void setGoogleId(String googleId) {
            this.googleId = googleId;
        }

        /**
         * This method is used to get the created by
         *
         * @return createdBy
         */
        public String getCreatedBy() {
            return createdBy;
        }

        /**
         * This method is used to set the created by
         *
         * @param createdBy For setting the created by
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
         * This method is used to get the other details
         *
         * @return other
         */
        public String getOther() {
            return other;
        }

        /**
         * This method is used to set the other details
         *
         * @param other For setting the others details
         */
        public void setOther(String other) {
            this.other = other;
        }

        /**
         * This method is used to get the flag
         *
         * @return flag
         */
        public String getFlag() {
            return flag;
        }

        /**
         * This method is used to set the flag
         *
         * @param flag For setting the flag
         */
        public void setFlag(String flag) {
            this.flag = flag;
        }

        /**
         * This method is used to get the delete date
         *
         * @return deletedDate
         */
        public String getDeletedDate() {
            return deletedDate;
        }

        /**
         * This method is used to set the delete date
         *
         * @param deletedDate For setting the delete date
         */
        public void setDeletedDate(String deletedDate) {
            this.deletedDate = deletedDate;
        }

    }
}
