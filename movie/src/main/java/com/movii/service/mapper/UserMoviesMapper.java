package com.movii.service.mapper;

import com.movii.domain.Movie;
import com.movii.domain.UserMovies;
import com.movii.service.dto.MovieDTO;
import com.movii.service.dto.UserMoviesDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserMovies} and its DTO {@link UserMoviesDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserMoviesMapper extends EntityMapper<UserMoviesDTO, UserMovies> {
    @Mapping(target = "movie", source = "movie", qualifiedByName = "movieTitle")
    UserMoviesDTO toDto(UserMovies s);

    @Named("movieTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    MovieDTO toDtoMovieTitle(Movie movie);
}
