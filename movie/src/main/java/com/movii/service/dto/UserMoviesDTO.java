package com.movii.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.movii.domain.UserMovies} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserMoviesDTO implements Serializable {

    private Long id;

    private Long userId;

    private MovieDTO movie;

    // private UserDTO user;

    // public UserDTO getUser() {
    // return user;
    // }

    // public void setUser(UserDTO user) {
    // this.user = user;
    // }

    // public UserMoviesDTO user(UserDTO user) {
    // this.setUser(user);
    // return this;
    // }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public MovieDTO getMovie() {
        return movie;
    }

    public void setMovie(MovieDTO movie) {
        this.movie = movie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserMoviesDTO)) {
            return false;
        }

        UserMoviesDTO userMoviesDTO = (UserMoviesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userMoviesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserMoviesDTO{" +
                "id=" + getId() +
                ", userId=" + getUserId() +
                ", movie=" + getMovie() +
                "}";
    }
}
