package com.home.apisdk.apiModel.apiModel;

/**
 * Created by Muvi on 10/4/2016.
 */
public class DeleteInvoicePdfInputModel {
    String authToken;

    public String getLanguage_code() {
        return Language_code;
    }

    public void setLanguage_code(String language_code) {
        Language_code = language_code;
    }

    String Language_code;

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    String filepath;




}
