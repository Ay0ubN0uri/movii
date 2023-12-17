package com.movii.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Movie.
 */
@Entity
@Table(name = "movie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "tmbd_id")
    private String tmbdId;

    @Lob
    @Column(name = "thumbnail", nullable = false)
    private byte[] thumbnail;

    @NotNull
    @Column(name = "thumbnail_content_type", nullable = false)
    private String thumbnailContentType;

    @Lob
    @Column(name = "banner")
    private byte[] banner;

    @Column(name = "banner_content_type")
    private String bannerContentType;

    @NotNull
    @Column(name = "release_date", nullable = false)
    private Instant releaseDate;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "duration")
    private String duration;

    @Column(name = "youtube_trailer")
    private String youtubeTrailer;

    @Column(name = "views")
    private Long views;

    @Column(name = "director")
    private String director;

    @Column(name = "average_rating")
    private Double averageRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "movies" }, allowSetters = true)
    private Genre genre;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "movies")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "movies" }, allowSetters = true)
    private Set<Actor> actors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Movie id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public Movie title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Movie description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTmbdId() {
        return this.tmbdId;
    }

    public Movie tmbdId(String tmbdId) {
        this.setTmbdId(tmbdId);
        return this;
    }

    public void setTmbdId(String tmbdId) {
        this.tmbdId = tmbdId;
    }

    public byte[] getThumbnail() {
        return this.thumbnail;
    }

    public Movie thumbnail(byte[] thumbnail) {
        this.setThumbnail(thumbnail);
        return this;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailContentType() {
        return this.thumbnailContentType;
    }

    public Movie thumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
        return this;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }

    public byte[] getBanner() {
        return this.banner;
    }

    public Movie banner(byte[] banner) {
        this.setBanner(banner);
        return this;
    }

    public void setBanner(byte[] banner) {
        this.banner = banner;
    }

    public String getBannerContentType() {
        return this.bannerContentType;
    }

    public Movie bannerContentType(String bannerContentType) {
        this.bannerContentType = bannerContentType;
        return this;
    }

    public void setBannerContentType(String bannerContentType) {
        this.bannerContentType = bannerContentType;
    }

    public Instant getReleaseDate() {
        return this.releaseDate;
    }

    public Movie releaseDate(Instant releaseDate) {
        this.setReleaseDate(releaseDate);
        return this;
    }

    public void setReleaseDate(Instant releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getVideoUrl() {
        return this.videoUrl;
    }

    public Movie videoUrl(String videoUrl) {
        this.setVideoUrl(videoUrl);
        return this;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getDuration() {
        return this.duration;
    }

    public Movie duration(String duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getYoutubeTrailer() {
        return this.youtubeTrailer;
    }

    public Movie youtubeTrailer(String youtubeTrailer) {
        this.setYoutubeTrailer(youtubeTrailer);
        return this;
    }

    public void setYoutubeTrailer(String youtubeTrailer) {
        this.youtubeTrailer = youtubeTrailer;
    }

    public Long getViews() {
        return this.views;
    }

    public Movie views(Long views) {
        this.setViews(views);
        return this;
    }

    public void setViews(Long views) {
        this.views = views;
    }

    public String getDirector() {
        return this.director;
    }

    public Movie director(String director) {
        this.setDirector(director);
        return this;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public Double getAverageRating() {
        return this.averageRating;
    }

    public Movie averageRating(Double averageRating) {
        this.setAverageRating(averageRating);
        return this;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Genre getGenre() {
        return this.genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Movie genre(Genre genre) {
        this.setGenre(genre);
        return this;
    }

    public Set<Actor> getActors() {
        return this.actors;
    }

    public void setActors(Set<Actor> actors) {
        if (this.actors != null) {
            this.actors.forEach(i -> i.removeMovies(this));
        }
        if (actors != null) {
            actors.forEach(i -> i.addMovies(this));
        }
        this.actors = actors;
    }

    public Movie actors(Set<Actor> actors) {
        this.setActors(actors);
        return this;
    }

    public Movie addActors(Actor actor) {
        this.actors.add(actor);
        actor.getMovies().add(this);
        return this;
    }

    public Movie removeActors(Actor actor) {
        this.actors.remove(actor);
        actor.getMovies().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Movie)) {
            return false;
        }
        return getId() != null && getId().equals(((Movie) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Movie{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", tmbdId='" + getTmbdId() + "'" +
            ", thumbnail='" + getThumbnail() + "'" +
            ", thumbnailContentType='" + getThumbnailContentType() + "'" +
            ", banner='" + getBanner() + "'" +
            ", bannerContentType='" + getBannerContentType() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", videoUrl='" + getVideoUrl() + "'" +
            ", duration='" + getDuration() + "'" +
            ", youtubeTrailer='" + getYoutubeTrailer() + "'" +
            ", views=" + getViews() +
            ", director='" + getDirector() + "'" +
            ", averageRating=" + getAverageRating() +
            "}";
    }
}
