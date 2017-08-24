package com.home.apisdk.apiModel;

import java.util.ArrayList;

/**
 * Created by Muvi on 9/21/2016.
 */
public class GetCastDetailsOutputModel {


    String name;
    String summary;
    String castImage;
    ArrayList<CastDetails> castdetails = new ArrayList<>();


    public ArrayList<CastDetails> getCastdetails() {
        return castdetails;
    }

    public void setCastdetails(ArrayList<CastDetails> castdetails) {
        this.castdetails = castdetails;
    }


    public String getCastImage() {
        return castImage;
    }

    public void setCastImage(String castImage) {
        this.castImage = castImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }





    public class CastDetails{
        String genre;
        String name;
        String posterUrl;
        String permalink;
        String contentTypesId;
        int isConverted;
        int isAdvance;
        int isPPV;

        public String getIsEpisode() {
            return isEpisode;
        }

        public void setIsEpisode(String isEpisode) {
            this.isEpisode = isEpisode;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPosterUrl() {
            return posterUrl;
        }

        public void setPosterUrl(String posterUrl) {
            this.posterUrl = posterUrl;
        }

        public String getPermalink() {
            return permalink;
        }

        public void setPermalink(String permalink) {
            this.permalink = permalink;
        }

        public String getContentTypesId() {
            return contentTypesId;
        }

        public void setContentTypesId(String contentTypesId) {
            this.contentTypesId = contentTypesId;
        }

        public int getIsConverted() {
            return isConverted;
        }

        public void setIsConverted(int isConverted) {
            this.isConverted = isConverted;
        }

        public int getIsAdvance() {
            return isAdvance;
        }

        public void setIsAdvance(int isAdvance) {
            this.isAdvance = isAdvance;
        }

        public int getIsPPV() {
            return isPPV;
        }

        public void setIsPPV(int isPPV) {
            this.isPPV = isPPV;
        }

        String isEpisode;
    }



}
