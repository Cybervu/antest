package com.home.apisdk.apiModel;

/**
 * Created by Muvi on 02-Aug-17.
 */

public class CastAndCrewModel {

    public String celeb_image;
    public String celeb_name;
    public String celeb_id;
    public String permalink;
    public String cast_type;

    public String getCast_type() {
        return cast_type;
    }

    public void setCast_type(String cast_type) {
        this.cast_type = cast_type;
    }

    public String getCeleb_image() {
        return celeb_image;
    }

    public void setCeleb_image(String celeb_image) {
        this.celeb_image = celeb_image;
    }

    public String getCeleb_name() {
        return celeb_name;
    }

    public void setCeleb_name(String celeb_name) {
        this.celeb_name = celeb_name;
    }

    public String getCeleb_id() {
        return celeb_id;
    }

    public void setCeleb_id(String celeb_id) {
        this.celeb_id = celeb_id;
    }

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

}
