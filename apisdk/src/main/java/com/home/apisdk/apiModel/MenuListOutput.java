package com.home.apisdk.apiModel;

/**
 * Created by MUVI on 1/20/2017.
 */

public class MenuListOutput {

    String link_type,display_name,permalink,url;
    boolean isEnable;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public void setLink_type(String link_type){
        this.link_type = link_type;
    }
    public String getLink_type(){
        return link_type;
    }
    public void setDisplay_name(String display_name){
        this.display_name =  display_name;
    }
    public String getDisplay_name(){
        return display_name;
    }
}
