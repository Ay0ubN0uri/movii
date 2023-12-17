package com.movii.service.mapper;

import com.movii.domain.Genre;
import com.movii.service.dto.GenreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Genre} and its DTO {@link GenreDTO}.
 */
@Mapper(componentModel = "spring")
public interface GenreMapper extends EntityMapper<GenreDTO, Genre> {}
