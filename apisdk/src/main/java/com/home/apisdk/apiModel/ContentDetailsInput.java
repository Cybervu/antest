package com.home.apisdk.apiModel;

/**
 * This Model Class Holds All The Input Attributes For GetContentDetailsAsynTask
 *
 * @author MUVI
 */
public class ContentDetailsInput {
    String permalink;
    String authtoken;
    String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    String language;

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

    String user_id;

    public ContentDetailsInput() {

    }

    /**
     * This Method is use to Get the Permalink
     *
     * @return permalink
     */
    public String getPermalink() {
        return permalink;
    }

    /**
     * This Method is use to Set the Permalink
     *
     * @param permalink For Setting The Permalink
     */
    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    /**
     * This Method is use to Get the Auth Token
     *
     * @return authtoken
     */
    public String getAuthToken() {
        return authtoken;
    }

    /**
     * This Method is use to Set the Auth Token
     *
     * @param authtoken For Setting The Auth Token
     */
    public void setAuthToken(String authtoken) {
        this.authtoken = authtoken;
    }
}
