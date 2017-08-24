package com.home.apisdk.apiModel;

/**
 * This Model Class Holds All The Input Attributes For SearchDataAsynTask
 *
 * @author MUVI
 */

public class Search_Data_input {
    String limit = "10";
    String authToken;
    String offset = "0";
    String q;

    /**
     * This Method  is use to Get the Language Code
     *
     * @return language_code
     */

    public String getLanguage_code() {
        return language_code;
    }

    /**
     * This Method is use to Set the Language Code
     *
     * @param language_code For Setting The Language Code
     */
    public void setLanguage_code(String language_code) {
        this.language_code = language_code;
    }

    String language_code;

    /**
     * This Method is use to Get the Country
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * This Method is use to Set the Country
     * @param country For Setting The Country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    String country;

   /* public Search_Data_input(String limit, String authToken, String offset, String q) {
        this.limit = limit;
        this.authToken = authToken;
        this.offset = offset;
        this.q = q;
    }*/

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
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

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
