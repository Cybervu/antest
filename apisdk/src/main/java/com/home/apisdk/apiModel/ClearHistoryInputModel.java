package com.home.apisdk.apiModel;

/**
 * This Model Class Holds All The Input Attributes For ClearWatchHistoryAsyn Task
 *
 * @author MUVI
 */

public class ClearHistoryInputModel {

    String authToken,user_id,lang_code;
    /**
     * This Method is use to Get the Authtoken
     *
     * @return authToken
     */
    public String getAuthToken() {
        return authToken;
    }
    /**
     * This Method is use to Set the Authtoken
     *
     * @param authToken For Setting The User Id
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    /**
     * This Method is use to Get the User ID
     *
     * @return user_id
     */
    public String getUser_id() {
        return user_id;
    }
    /**
     * This Method is use to Get the Auth Token
     *
     * @return authToken
     */
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    /**
     * This Method is use to Get the Language Code
     *
     * @return lang_code
     */
    public String getLang_code() {
        return lang_code;
    }
    /**
     * This Method is use to Set the Language Code
     *
     * @param lang_code For Setting The Language Code
     */
    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }
}
