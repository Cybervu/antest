package com.home.apisdk.apiModel;

/**
 * This Model Class Holds All The Input Attributes For GetContentListAsynTask
 *
 * @author MUVI
 */
public class TutorialScreenInput {

    String authToken;

    String app_type;




    /**
     * This Method is use to Get the App_type
     *
     * @return app_type
     */
    public String getApp_type() {
        return app_type;
    }

    /**
     * This Method is use to Set the App_Type
     *
     * @param app_type For Setting The App_Type
     */
    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }






    /**
     * This Method is use to Get the Auth Token
     *
     * @return authToken
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * This Method is use to Set the Auth Token
     *
     * @param authToken For Setting The Auth Token
     */
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }


}
