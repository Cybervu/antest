package com.home.apisdk.apiModel;

/**
 * This Model Class Holds All The Input Attributes For GetContentListAsynTask
 *
 * @author MUVI
 */
public class CategoryListInput {

    String authToken;
    String country;

    /**
     * This Method is use to Get the Language
     *
     * @return Language
     */
    public String getLanguage() {
        return Language;
    }

    /**
     * This Method is use to Set the Language
     *
     * @param language For Setting The Language
     */
    public void setLanguage(String language) {
        Language = language;
    }

    String Language;


    /**
     * This Method is use to Get the Country
     *
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * This Method is use to Set the Country
     *
     * @param country For Setting The Country
     */
    public void setCountry(String country) {
        this.country = country;
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
