package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class CelibrityInputModel {

    String authToken;
    String movie_id;

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }


    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }
    public String getAuthToken(){
        return authToken;
    }

}
