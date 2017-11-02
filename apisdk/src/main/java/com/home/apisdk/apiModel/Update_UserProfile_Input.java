package com.home.apisdk.apiModel;

/**
 * This Model Class Holds All The Input Attributes For UpadteUserProfileAsynctask
 *
 * @author MUVI
 */

public class Update_UserProfile_Input {

    String authToken;
    String user_id;
    String name;
    String password;
    String lang_code;
    String display_image;

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

    public String getCustom_country() {
        return custom_country;
    }

    public void setCustom_country(String custom_country) {
        this.custom_country = custom_country;
    }

    public String getCustom_languages() {
        return custom_languages;
    }

    public void setCustom_languages(String custom_languages) {
        this.custom_languages = custom_languages;
    }

    String custom_country;
    String custom_languages;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_id() {
        return user_id;
    }

    /**
     * This Method is use to Set the Password
     *
     * @param password For Setting The Password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * This Method is use to Get the Password
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    public String getDisplay_image() {
        return display_image;
    }

    public void setDisplay_image(String display_image) {
        this.display_image = display_image;
    }
}
