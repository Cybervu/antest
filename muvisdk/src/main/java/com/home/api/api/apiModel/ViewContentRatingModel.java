package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This Model Class Holds All The Attributes For View Content Rating
 *
 * @author Abhishek
 */

public class ViewContentRatingModel {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("rating")
    @Expose
    private ArrayList<Rating> rating = new ArrayList<>();
    @SerializedName("showrating")
    @Expose
    private Integer showrating = 0;
    @SerializedName("msg")
    @Expose
    private String msg = "";

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
     * This method is used to get the array list of rating
     *
     * @return rating
     */
    public ArrayList<Rating> getRating() {
        return rating;
    }

    /**
     * This method is used to set the array list of rating
     *
     * @param rating For setting the rating
     */
    public void setRating(ArrayList<Rating> rating) {
        this.rating = rating;
    }

    /**
     * This method is used to get the show rating
     *
     * @return showrating
     */
    public Integer getShowrating() {
        return showrating;
    }

    /**
     * This method is used to set the show rating
     *
     * @param showrating For setting the show rating
     */
    public void setShowrating(Integer showrating) {
        this.showrating = showrating;
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

    public class Rating {

        @SerializedName("display_name")
        @Expose
        private String displayName = "";
        @SerializedName("created_date")
        @Expose
        private String createdDate = "";
        @SerializedName("rating")
        @Expose
        private String rating = "";
        @SerializedName("review")
        @Expose
        private String review = "";
        @SerializedName("status")
        @Expose
        private String status = "";

        /**
         * This method is used to get the display name
         *
         * @return displayName
         */
        public String getDisplayName() {
            return displayName;
        }

        /**
         * This method is used to set the display name
         *
         * @param displayName For setting the display name
         */
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
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
         * This method is used to get the rating
         *
         * @return rating
         */
        public String getRating() {
            return rating;
        }

        /**
         * This method is used to set the rating
         *
         * @param rating For setting the rating
         */
        public void setRating(String rating) {
            this.rating = rating;
        }

        /**
         * This method is used to get the review
         *
         * @return review
         */
        public String getReview() {
            return review;
        }

        /**
         * This method is used to set the review
         *
         * @param review For setting the review
         */
        public void setReview(String review) {
            this.review = review;
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

    }
}
