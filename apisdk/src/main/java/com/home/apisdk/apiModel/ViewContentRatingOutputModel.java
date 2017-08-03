package com.home.apisdk.apiModel;

import java.util.ArrayList;

/**
 * Created by MUVI on 1/20/2017.
 */

public class ViewContentRatingOutputModel {

    ArrayList<Rating> RatingArray = new ArrayList<>();
    int showrating;
    public int getShowrating() {
        return showrating;
    }

    public void setShowrating(int showrating) {
        this.showrating = showrating;
    }

    public ArrayList<Rating> getRatingArray() {
        return RatingArray;
    }

    public void setRatingValue(ArrayList<Rating> ratingArray) {
        RatingArray = ratingArray;
    }



    public class Rating{

    String display_name;
    String rating;
    String review;
    String status;




    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    }







}
