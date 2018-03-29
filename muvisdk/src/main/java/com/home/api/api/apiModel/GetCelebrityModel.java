package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This Model Class Holds All The Attributes For Celebrity details
 *
 * @author Abhishek
 */

public class GetCelebrityModel {

    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";
    @SerializedName("celibrity")
    @Expose
    private ArrayList<Celibrity> celibrity = null;
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
     * This method is used to get the array list of celebrity
     *
     * @return celibrity
     */
    public ArrayList<Celibrity> getCelibrity() {
        return celibrity;
    }

    /**
     * This method is used to set the array list of celebrity
     *
     * @param celibrity For setting the celebrity list
     */
    public void setCelibrity(ArrayList<Celibrity> celibrity) {
        this.celibrity = celibrity;
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

    public class Celibrity {

        @SerializedName("name")
        @Expose
        private String name = "";
        @SerializedName("permalink")
        @Expose
        private String permalink = "";
        @SerializedName("summary")
        @Expose
        private String summary = "";
        @SerializedName("cast_type")
        @Expose
        private String castType = "";
        @SerializedName("celebrity_image")
        @Expose
        private String celebrityImage = "";

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
         * This method is used to get the permalink
         *
         * @return permalink
         */
        public String getPermalink() {
            return permalink;
        }

        /**
         * This method is used to set the permalink
         *
         * @param permalink For setting the permalink
         */
        public void setPermalink(String permalink) {
            this.permalink = permalink;
        }

        /**
         * This method is used to get the summary
         *
         * @return summary
         */
        public String getSummary() {
            return summary;
        }

        /**
         * This method is used to set the summary
         *
         * @param summary For setting the summary
         */
        public void setSummary(String summary) {
            this.summary = summary;
        }

        /**
         * This method is used to get the cast type
         *
         * @return castType
         */
        public String getCastType() {
            return castType;
        }

        /**
         * This method is used to set the cast type
         *
         * @param castType For setting the cast type
         */
        public void setCastType(String castType) {
            this.castType = castType;
        }

        /**
         * This method is used to get the celebrity image
         *
         * @return celebrityImage
         */
        public String getCelebrityImage() {
            return celebrityImage;
        }

        /**
         * This method is used to set the celebrity image
         *
         * @param celebrityImage For setting the celebrity image
         */
        public void setCelebrityImage(String celebrityImage) {
            this.celebrityImage = celebrityImage;
        }

    }
}
