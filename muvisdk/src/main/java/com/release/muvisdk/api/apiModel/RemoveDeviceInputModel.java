package com.release.muvisdk.api.apiModel;

/**
 * This Model Class Holds All The Input Attributes For RemoveDeviceAsynTask
 *
 * @author MUVI
 */

public class RemoveDeviceInputModel {

    String authToken;
    String lang_code;
    String user_id;
    String device;

    /**
     * This Method  is use to Get the Device Details
     *
     * @return device
     */
    public String getDevice() {
        return device;
    }

    /**
     * This Method  is use to Set the Device Details
     *
     * @param device For Setting The Device Details
     */

    public void setDevice(String device) {
        this.device = device;
    }

    /**
     * This Method  is use to Get the Language Code
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

    /**
     * This Method is use to Get the User Id
     *
     * @return user_id
     */

    public String getUser_id() {
        return user_id;
    }

    /**
     * This Method is use to Set the User Id
     *
     * @param user_id For Setting The User Id
     */

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

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

}
