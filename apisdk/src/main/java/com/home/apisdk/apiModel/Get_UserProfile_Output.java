package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class Get_UserProfile_Output {

    String id;
    String display_name;
    String email;
    String studio_id;
    String profile_image;
    String isSubscribed;
    String custom_languages;

    public String getCustom_languages() {
        return custom_languages;
    }

    public void setCustom_languages(String custom_languages) {
        this.custom_languages = custom_languages;
    }

    public String getCustom_country() {
        return custom_country;
    }

    public void setCustom_country(String custom_country) {
        this.custom_country = custom_country;
    }

    String custom_country;
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return id;
    }

    public void setDisplay_name(String display_name){this.display_name = display_name;}
    public String getDisplay_name(){return display_name;}

    public void setEmail(String email){this.email = email;}
    public String getEmail(){return email;}

    public void setStudio_id(String studio_id){this.studio_id = studio_id;}
    public String getStudio_id(){return studio_id;}

    public void setProfile_image(String profile_image){this.profile_image = profile_image;}
    public String getProfile_image(){return profile_image;}

    public void setIsSubscribed(String isSubscribed){this.isSubscribed = isSubscribed;}
    public String getIsSubscribed(){return isSubscribed;}

}
