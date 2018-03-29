package com.home.api.api.apiModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * This Model Class Holds All The Attributes For Genre List
 *
 * @author Abhishek
 */

public class GenreListModel {
    @SerializedName("genre_list")
    @Expose
    private ArrayList<String> genreList = null;
    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("status")
    @Expose
    private String status = "";

    /**
     * This method is used to get the array list of genre list
     *
     * @return genreList
     */

    public ArrayList<String> getGenreList() {
        return genreList;
    }

    /**
     * This method is used to set the array list of genre list
     *
     * @param genreList For setting the genre list
     */

    public void setGenreList(ArrayList<String> genreList) {
        this.genreList = genreList;
    }

    /**
     * This method is used to get the server code
     *
     * @return code
     */

    public Integer getCode() {
        return code;
    }

    /**
     * This method is used to set the server code
     *
     * @param code For setting the server code
     */

    public void setCode(Integer code) {
        this.code = code;
    }

    /**
     * This method is used to get the status
     *
     * @return status
     */

    public String getStatus() {
        return status;
    }

    /**
     * This method is used to set the status
     *
     * @param status For setting the status
     */

    public void setStatus(String status) {
        this.status = status;
    }
}
