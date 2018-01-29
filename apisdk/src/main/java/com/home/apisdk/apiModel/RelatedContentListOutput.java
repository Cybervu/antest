package com.home.apisdk.apiModel;

/**
 * This Model Class Holds All The Output Attributes For GetContentListAsynTask
 *
 * @author MUVI
 */
public class RelatedContentListOutput {
    String contentId;

    public String getContentStreamId() {
        return contentStreamId;
    }

    public void setContentStreamId(String contentStreamId) {
        this.contentStreamId = contentStreamId;
    }

    String contentStreamId;

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    String story;
    String permalink;
    String name;
  /*
    String releaseDate;*/
    String contentTypesId;
    String posterUrl;
  /*  String genre;
    String duration_field_display_name;
    String duration_value;
    String repetition_field_display_name;
    String repetition_value;
    String difficulty_level_field_display_name;
    String difficulty_level_value;
    String benefits_field_display_name;*/

  /*  public String getBenefits_value() {
        return benefits_value;
    }

    public void setBenefits_value(String benefits_value) {
        this.benefits_value = benefits_value;
    }

    public String getDuration_field_display_name() {
        return duration_field_display_name;
    }

    public void setDuration_field_display_name(String duration_field_display_name) {
        this.duration_field_display_name = duration_field_display_name;
    }

    public String getDuration_value() {
        return duration_value;
    }

    public void setDuration_value(String duration_value) {
        this.duration_value = duration_value;
    }

    public String getRepetition_field_display_name() {
        return repetition_field_display_name;
    }

    public void setRepetition_field_display_name(String repetition_field_display_name) {
        this.repetition_field_display_name = repetition_field_display_name;
    }

    public String getRepetition_value() {
        return repetition_value;
    }

    public void setRepetition_value(String repetition_value) {
        this.repetition_value = repetition_value;
    }

    public String getDifficulty_level_field_display_name() {
        return difficulty_level_field_display_name;
    }

    public void setDifficulty_level_field_display_name(String difficulty_level_field_display_name) {
        this.difficulty_level_field_display_name = difficulty_level_field_display_name;
    }

    public String getDifficulty_level_value() {
        return difficulty_level_value;
    }

    public void setDifficulty_level_value(String difficulty_level_value) {
        this.difficulty_level_value = difficulty_level_value;
    }

    public String getBenefits_field_display_name() {
        return benefits_field_display_name;
    }

    public void setBenefits_field_display_name(String benefits_field_display_name) {
        this.benefits_field_display_name = benefits_field_display_name;
    }

    String benefits_value;
*/



    /**
     * This Method is use to Get the Is Episode Details
     * @return isEpisodeStr
     */
 /*   public String getIsEpisodeStr() {
        return isEpisodeStr;
    }

    *//**
     * This Method is use to Set the Is Episode Details
     * @param isEpisodeStr For Setting The Is Episode Details
     *//*
    public void setIsEpisodeStr(String isEpisodeStr) {
        this.isEpisodeStr = isEpisodeStr;
    }

    String isEpisodeStr;
    int isConverted,isPPV,isAPV;*/

    /**
     * This Method is use to Get the Is PPV Details
     * @return isPPV
     */
   /* public int getIsPPV() {
        return isPPV;
    }

    *//**
     * This Method is use to Set the Is PPV Details
     * @param isPPV For Setting The Is PPV Details
     *//*
    public void setIsPPV(int isPPV) {
        this.isPPV = isPPV;
    }

    *//**
     * This Method is use to Get the Is APV Details
     * @return isAPV
     *//*
    public int getIsAPV() {
        return isAPV;
    }

    *//**
     * This Method is use to Set the Is APV Details
     * @param isAPV For Setting The Is APV Details
     *//*
    public void setIsAPV(int isAPV) {
        this.isAPV = isAPV;
    }*/

    /**
     * This Method is use to Get the Movie ID
     * @return movieId
     */
    public String getContentId() {
        return contentId;
    }

    /**
     * This Method is use to Set the Movie ID
     * @param movieId For Setting The Movie ID
     */
    public void setContentId(String movieId) {
        this.contentId = contentId;
    }

    /**
     * This Method is use to Get the Permalink
     * @return permalink
     */
    public String getPermalink() {
        return permalink;
    }

    /**
     * This Method is use to Set the Permalink
     * @param permalink For Setting The Permalink
     */
    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    /**
     * This Method is use to Get the Name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * This Method is use to Set the Name
     * @param name For Setting The Name
     */
    public void setName(String name) {
        this.name = name;
    }

  /*  *//**
     * This Method is use to Get the Story
     * @return story
     *//*
    public String getStory() {
        return story;
    }

    *//**
     * This Method is use to Set the Story
     * @param story For Setting The Story
     *//*
    public void setStory(String story) {
        this.story = story;
    }

    *//**
     * This Method is use to Get the Release Date
     * @return releaseDate
     *//*
    public String getReleaseDate() {
        return releaseDate;
    }

    *//**
     * This Method is use to Set the Release Date
     * @param releaseDate For Setting The Release Date
     *//*
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }
*/
    /**
     * This Method is use to Get the Content ID Type
     * @return contentTypesId
     */
    public String getContentTypesId() {
        return contentTypesId;
    }

    /**
     * This Method is use to Set the Content ID Type
     * @param contentTypesId For Setting The Content ID Type
     */
    public void setContentTypesId(String contentTypesId) {
        this.contentTypesId = contentTypesId;
    }

    /**
     * This Method is use to Get the Poster URL
     * @return posterUrl
     */
    public String getPosterUrl() {
        return posterUrl;
    }

    /**
     * This Method is use to Set the Poster URL
     * @param posterUrl For Setting The Poster URL
     */
    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

   /* *//**
     * This Method is use to Get the Genre List
     * @return genre
     *//*
    public String getGenre() {
        return genre;
    }

    *//**
     * This Method is use to Set the Genre List
     * @param genre For Setting The Genre List
     *//*
    public void setGenre(String genre) {
        this.genre = genre;
    }*/

   /* *//**
     * This Method is use to Get the Is Converted Details
     * @return isConverted
     *//*
    public int getIsConverted() {
        return isConverted;
    }

    *//**
     * This Method is use to Set the Is Converted Details
     * @param isConverted For Setting The Is Converted Details
     *//*
    public void setIsConverted(int isConverted) {
        this.isConverted = isConverted;
    }*/
}
