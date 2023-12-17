package com.movii.service.dto;

import jakarta.persistence.Lob;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.movii.domain.Actor} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ActorDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private Instant birthDate;

    @Lob
    private String bio;

    private String nationality;

    private Set<MovieDTO> movies = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Instant getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Instant birthDate) {
        this.birthDate = birthDate;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Set<MovieDTO> getMovies() {
        return movies;
    }

    public void setMovies(Set<MovieDTO> movies) {
        this.movies = movies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActorDTO)) {
            return false;
        }

        ActorDTO actorDTO = (ActorDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, actorDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActorDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", bio='" + getBio() + "'" +
            ", nationality='" + getNationality() + "'" +
            ", movies=" + getMovies() +
            "}";
    }
}
