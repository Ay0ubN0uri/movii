package com.movii.service.mapper;

import com.movii.domain.Genre;
import com.movii.domain.Movie;
import com.movii.service.dto.GenreDTO;
import com.movii.service.dto.MovieDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Movie} and its DTO {@link MovieDTO}.
 */
@Mapper(componentModel = "spring")
public interface MovieMapper extends EntityMapper<MovieDTO, Movie> {
    @Mapping(target = "genre", source = "genre", qualifiedByName = "genreName")
    MovieDTO toDto(Movie s);

    @Named("genreName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    GenreDTO toDtoGenreName(Genre genre);
}
