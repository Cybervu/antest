package com.home.apisdk.apiModel;

/**
 * This Model Class Holds All The Input Attributes For GetGenreListAsynctask
 *
 * @author MUVI
 */

public class GenreListInput {

    String authToken;
    String lang_code;



    /**
     * This Method is use to Set the Auth Token
     *
     * @param authToken For Setting The Auth Token
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * This Method is use to Get the Auth Token
     *
     * @return authToken
     */
    public String getAuthToken() {
        return authToken;
    }

    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }

}
