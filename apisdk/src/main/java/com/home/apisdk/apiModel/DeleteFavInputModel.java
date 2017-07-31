package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 7/21/2017.
 */

public class DeleteFavInputModel {
    String authTokenStr,movieUniqueId,isEpisode,loggedInStr;

    public String getAuthTokenStr() {
        return authTokenStr;
    }

    public void setAuthTokenStr(String authTokenStr) {
        this.authTokenStr = authTokenStr;
    }

    public String getMovieUniqueId() {
        return movieUniqueId;
    }

    public void setMovieUniqueId(String movieUniqueId) {
        this.movieUniqueId = movieUniqueId;
    }

    public String getIsEpisode() {
        return isEpisode;
    }

    public void setIsEpisode(String isEpisode) {
        this.isEpisode = isEpisode;
    }

    public String getLoggedInStr() {
        return loggedInStr;
    }

    public void setLoggedInStr(String loggedInStr) {
        this.loggedInStr = loggedInStr;
    }
}
