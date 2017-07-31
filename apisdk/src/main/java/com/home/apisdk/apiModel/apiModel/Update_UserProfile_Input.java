package com.home.apisdk.apiModel.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class Update_UserProfile_Input {

    String authToken;
    String user_id;
    String name;
    String password;
    String lang_code;

    public String getLang_code() {
        return lang_code;
    }

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
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public void setAuthToken(String authToken){this.authToken = authToken;}
    public String getAuthToken(){return authToken;}

    public void setUser_id(String user_id){this.user_id = user_id;}
    public String getUser_id(){return user_id;}

    public void setPassword(String password){this.password = password;}
    public String getPassword(){return password;}

}
