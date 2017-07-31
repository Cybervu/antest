package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class AddContentRatingInputModel {

    String authToken;
    String content_id ;
    String lang_code;
    String user_id;
    String rating;
    String review;

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getLang_code() {
        return lang_code;
    }

    public void setLang_code(String lang_code) {
        this.lang_code = lang_code;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }




    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }
    public String getAuthToken(){
        return authToken;
    }

}
