package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 7/20/2017.
 */

public class AddToFavInputModel {

    String authToken,movie_uniq_id,isEpisodeStr,loggedInStr;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getMovie_uniq_id() {
        return movie_uniq_id;
    }

    public void setMovie_uniq_id(String movie_uniq_id) {
        this.movie_uniq_id = movie_uniq_id;
    }

    public String getIsEpisodeStr() {
        return isEpisodeStr;
    }

    public void setIsEpisodeStr(String isEpisodeStr) {
        this.isEpisodeStr = isEpisodeStr;
    }

    public String getLoggedInStr() {
        return loggedInStr;
    }

    public void setLoggedInStr(String loggedInStr) {
        this.loggedInStr = loggedInStr;
    }
}
