package com.home.vod.model;


public class YogaItem {
    private String title;
    private String videoType;
    private String imageId;
    private String permalink;
    private String ContentTypesId;
    private int isFavorite;
    public boolean isFavoriteClicked() {
        return favoriteClicked;
    }

    public void setFavoriteClicked(boolean favoriteClicked) {
        this.favoriteClicked = favoriteClicked;
    }

    private String content_id;
    private  boolean favoriteClicked;

    public String getContent_stream_id() {
        return content_stream_id;
    }

    public void setContent_stream_id(String content_stream_id) {
        this.content_stream_id = content_stream_id;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    private String content_stream_id;

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public int getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    private String story;
    private int isConverted;
    private int isPPV;
    private int isAPV;

    private boolean isClicked;
    private boolean isSelected;


    public boolean isClicked() {
        return isClicked;
    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public int getIsAPV() {
        return isAPV;
    }

    public void setIsAPV(int isAPV) {
        this.isAPV = isAPV;
    }

    public int getIsPPV() {
        return isPPV;
    }

    public void setIsPPV(int isPPV) {
        this.isPPV = isPPV;
    }

    public int getIsConverted() {
        return isConverted;
    }

    public void setIsConverted(int isConverted) {
        this.isConverted = isConverted;
    }


    public String getMovieUniqueId() {
        return movieUniqueId;
    }

    public void setMovieUniqueId(String movieUniqueId) {
        this.movieUniqueId = movieUniqueId;
    }

    public String getMovieStreamUniqueId() {
        return movieStreamUniqueId;
    }

    public void setMovieStreamUniqueId(String movieStreamUniqueId) {
        this.movieStreamUniqueId = movieStreamUniqueId;
    }

    private String movieUniqueId;
    private String movieStreamUniqueId;

    public String getIsEpisode() {
        return isEpisode;
    }

    public void setIsEpisode(String isEpisode) {
        this.isEpisode = isEpisode;
    }

    private String isEpisode;

    public String getPermalink() {
        return permalink;
    }

    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    private String videoUrl;

    public String getVideoTypeId() {
        return videoTypeId;
    }

    public void setVideoTypeId(String videoTypeId) {
        this.videoTypeId = videoTypeId;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public String getContentTypesId() {
        return ContentTypesId;
    }

    public void setContentTypesId(String contentTypesId) {
        ContentTypesId = contentTypesId;
    }

    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }

    private String videoTypeId;
    private String movieGenre;
    public String getVideoType() {

        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }



    //	public GridItem(int imageId, String title,String rating) {
//		super();
//		this.imageId = imageId;
//		this.title = title;
//		this.rating = rating;
//	}
    public YogaItem(String imageId, String title, String videoType, String videoTypeId, String movieGenre, String videoUrl, String permalink, String isEpisode, String movieUniqueId, String movieStreamUniqueId, int isConverted, int isPPV, int isAPV, String story, String ContentTypesId,int isFavorite,String content_id,String content_stream_id) {
        super();
        this.imageId = imageId;
        this.title = title;
        this.videoType = videoType;
        this.videoTypeId = videoTypeId;
        this.movieGenre = movieGenre;
        this.videoUrl = videoUrl;
        this.permalink = permalink;
        this.isEpisode = isEpisode;
        this.movieUniqueId = movieUniqueId;
        this.movieStreamUniqueId = movieStreamUniqueId;
        this.isConverted = isConverted;
        this.isPPV = isPPV;
        this.isAPV = isAPV;
        this.story = story;
        this.ContentTypesId = ContentTypesId;
        this.isFavorite=isFavorite;
        this.imageId = imageId;
        this.content_id = content_id;
        this.content_stream_id = content_stream_id;

    }
    public YogaItem(String imageId, String title, String videoType, String videoTypeId, String movieGenre, String videoUrl, String permalink, String isEpisode, String movieUniqueId, String movieStreamUniqueId, int isConverted, int isPPV, int isAPV, String story, String ContentTypesId,int isFavorite,String content_id,String content_stream_id,boolean favoriteClicked) {
        super();
        this.imageId = imageId;
        this.title = title;
        this.videoType = videoType;
        this.videoTypeId = videoTypeId;
        this.movieGenre = movieGenre;
        this.videoUrl = videoUrl;
        this.permalink = permalink;
        this.isEpisode = isEpisode;
        this.movieUniqueId = movieUniqueId;
        this.movieStreamUniqueId = movieStreamUniqueId;
        this.isConverted = isConverted;
        this.isPPV = isPPV;
        this.isAPV = isAPV;
        this.story = story;
        this.ContentTypesId = ContentTypesId;
        this.isFavorite=isFavorite;
        this.imageId = imageId;
        this.content_id = content_id;
        this.content_stream_id = content_stream_id;
        this.favoriteClicked=favoriteClicked;

    }

    public String getImage() {
        return imageId;
    }

    public void setImage(String imageId) {

        this.imageId = imageId;
    }
	/*public String getImage() {
		return imageId;
	}

	public void setImage(String imageId) {
		this.imageId = imageId;
	}*/

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
