package com.movii.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.movii.domain.Movie} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MovieDTO implements Serializable {

    private Long id;

    @NotNull
    private String title;

    @Lob
    private String description;

    private String tmbdId;

    @Lob
    private byte[] thumbnail;

    private String thumbnailContentType;

    @Lob
    private byte[] banner;

    private String bannerContentType;

    @NotNull
    private Instant releaseDate;

    private String videoUrl;

    private String duration;

    private String youtubeTrailer;

    private Long views;

    private String director;

    private Double averageRating;

    private GenreDTO genre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTmbdId() {
        return tmbdId;
    }

    public void setTmbdId(String tmbdId) {
        this.tmbdId = tmbdId;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailContentType() {
        return thumbnailContentType;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }

    public byte[] getBanner() {
        return banner;
    }

    public void setBanner(byte[] banner) {
        this.banner = banner;
    }

    public String getBannerContentType() {
        return bannerContentType;
    }

    public void setBannerContentType(String bannerContentType) {
        this.bannerContentType = bannerContentType;
    }

    public Instant getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Instant releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getYoutubeTrailer() {
        return youtubeTrailer;
    }

    public void setYoutubeTrailer(String youtubeTrailer) {
        this.youtubeTrailer = youtubeTrailer;
    }

    public Long getViews() {
        return views;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public GenreDTO getGenre() {
        return genre;
    }

    public void setGenre(GenreDTO genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MovieDTO)) {
            return false;
        }

        MovieDTO movieDTO = (MovieDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, movieDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovieDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", tmbdId='" + getTmbdId() + "'" +
            ", thumbnail='" + getThumbnail() + "'" +
            ", banner='" + getBanner() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", videoUrl='" + getVideoUrl() + "'" +
            ", duration='" + getDuration() + "'" +
            ", youtubeTrailer='" + getYoutubeTrailer() + "'" +
            ", views=" + getViews() +
            ", director='" + getDirector() + "'" +
            ", averageRating=" + getAverageRating() +
            ", genre=" + getGenre() +
            "}";
    }
}
