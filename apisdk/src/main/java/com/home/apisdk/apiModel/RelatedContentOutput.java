package com.home.apisdk.apiModel;

import java.util.ArrayList;

/**
 * Created by User on 11-10-2017.
 */
public class RelatedContentOutput {
    int code = 0;
    int total_count = 0;
    String message ="";
    String status ="";
    ArrayList<ContentData> contentData = new ArrayList<>();



    /**
     * This method is used to generate code
     *
     * @return code
     */
    public int getCode() {
        return code;
    }

    /**
     * This method is used to set code
     *
     * @param code
     */
    public void setCode(int code) {
        this.code = code;
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public ArrayList<ContentData> getContentData() {
        return contentData;
    }

    public void setContentData(ArrayList<ContentData> contentData) {
        this.contentData = contentData;
    }




}
