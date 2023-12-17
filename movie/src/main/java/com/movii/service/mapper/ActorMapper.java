package com.movii.service.mapper;

import com.movii.domain.Actor;
import com.movii.domain.Movie;
import com.movii.service.dto.ActorDTO;
import com.movii.service.dto.MovieDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Actor} and its DTO {@link ActorDTO}.
 */
@Mapper(componentModel = "spring")
public interface ActorMapper extends EntityMapper<ActorDTO, Actor> {
    @Mapping(target = "movies", source = "movies", qualifiedByName = "movieTitleSet")
    ActorDTO toDto(Actor s);

    @Mapping(target = "removeMovies", ignore = true)
    Actor toEntity(ActorDTO actorDTO);

    @Named("movieTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    MovieDTO toDtoMovieTitle(Movie movie);

    @Named("movieTitleSet")
    default Set<MovieDTO> toDtoMovieTitleSet(Set<Movie> movie) {
        return movie.stream().map(this::toDtoMovieTitle).collect(Collectors.toSet());
    }
}
