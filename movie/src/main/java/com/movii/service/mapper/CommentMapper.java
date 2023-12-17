package com.movii.service.mapper;

import com.movii.domain.Comment;
import com.movii.domain.Movie;
import com.movii.service.dto.CommentDTO;
import com.movii.service.dto.MovieDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Comment} and its DTO {@link CommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper extends EntityMapper<CommentDTO, Comment> {
    @Mapping(target = "movie", source = "movie", qualifiedByName = "movieTitle")
    CommentDTO toDto(Comment s);

    @Named("movieTitle")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "title", source = "title")
    MovieDTO toDtoMovieTitle(Movie movie);
}
