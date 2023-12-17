package com.movii.service;

import com.movii.domain.UserMovies;
import com.movii.repository.UserMoviesRepository;
import com.movii.service.dto.UserMoviesDTO;
import com.movii.service.mapper.UserMoviesMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.movii.domain.UserMovies}.
 */
@Service
@Transactional
public class UserMoviesService {

    private final Logger log = LoggerFactory.getLogger(UserMoviesService.class);

    private final UserMoviesRepository userMoviesRepository;

    private final UserMoviesMapper userMoviesMapper;

    public UserMoviesService(UserMoviesRepository userMoviesRepository, UserMoviesMapper userMoviesMapper) {
        this.userMoviesRepository = userMoviesRepository;
        this.userMoviesMapper = userMoviesMapper;
    }

    /**
     * Save a userMovies.
     *
     * @param userMoviesDTO the entity to save.
     * @return the persisted entity.
     */
    public UserMoviesDTO save(UserMoviesDTO userMoviesDTO) {
        log.debug("Request to save UserMovies : {}", userMoviesDTO);
        UserMovies userMovies = userMoviesMapper.toEntity(userMoviesDTO);
        userMovies = userMoviesRepository.save(userMovies);
        return userMoviesMapper.toDto(userMovies);
    }

    /**
     * Update a userMovies.
     *
     * @param userMoviesDTO the entity to save.
     * @return the persisted entity.
     */
    public UserMoviesDTO update(UserMoviesDTO userMoviesDTO) {
        log.debug("Request to update UserMovies : {}", userMoviesDTO);
        UserMovies userMovies = userMoviesMapper.toEntity(userMoviesDTO);
        userMovies = userMoviesRepository.save(userMovies);
        return userMoviesMapper.toDto(userMovies);
    }

    /**
     * Partially update a userMovies.
     *
     * @param userMoviesDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserMoviesDTO> partialUpdate(UserMoviesDTO userMoviesDTO) {
        log.debug("Request to partially update UserMovies : {}", userMoviesDTO);

        return userMoviesRepository
            .findById(userMoviesDTO.getId())
            .map(existingUserMovies -> {
                userMoviesMapper.partialUpdate(existingUserMovies, userMoviesDTO);

                return existingUserMovies;
            })
            .map(userMoviesRepository::save)
            .map(userMoviesMapper::toDto);
    }

    /**
     * Get all the userMovies.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserMoviesDTO> findAll() {
        log.debug("Request to get all UserMovies");
        return userMoviesRepository.findAll().stream().map(userMoviesMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the userMovies with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<UserMoviesDTO> findAllWithEagerRelationships(Pageable pageable) {
        return userMoviesRepository.findAllWithEagerRelationships(pageable).map(userMoviesMapper::toDto);
    }

    /**
     * Get one userMovies by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserMoviesDTO> findOne(Long id) {
        log.debug("Request to get UserMovies : {}", id);
        return userMoviesRepository.findOneWithEagerRelationships(id).map(userMoviesMapper::toDto);
    }

    /**
     * Delete the userMovies by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserMovies : {}", id);
        userMoviesRepository.deleteById(id);
    }
}
