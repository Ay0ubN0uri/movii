package com.movii.service;

import com.movii.domain.Actor;
import com.movii.repository.ActorRepository;
import com.movii.service.dto.ActorDTO;
import com.movii.service.mapper.ActorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.movii.domain.Actor}.
 */
@Service
@Transactional
public class ActorService {

    private final Logger log = LoggerFactory.getLogger(ActorService.class);

    private final ActorRepository actorRepository;

    private final ActorMapper actorMapper;

    public ActorService(ActorRepository actorRepository, ActorMapper actorMapper) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
    }

    /**
     * Save a actor.
     *
     * @param actorDTO the entity to save.
     * @return the persisted entity.
     */
    public ActorDTO save(ActorDTO actorDTO) {
        log.debug("Request to save Actor : {}", actorDTO);
        Actor actor = actorMapper.toEntity(actorDTO);
        actor = actorRepository.save(actor);
        return actorMapper.toDto(actor);
    }

    /**
     * Update a actor.
     *
     * @param actorDTO the entity to save.
     * @return the persisted entity.
     */
    public ActorDTO update(ActorDTO actorDTO) {
        log.debug("Request to update Actor : {}", actorDTO);
        Actor actor = actorMapper.toEntity(actorDTO);
        actor = actorRepository.save(actor);
        return actorMapper.toDto(actor);
    }

    /**
     * Partially update a actor.
     *
     * @param actorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ActorDTO> partialUpdate(ActorDTO actorDTO) {
        log.debug("Request to partially update Actor : {}", actorDTO);

        return actorRepository
            .findById(actorDTO.getId())
            .map(existingActor -> {
                actorMapper.partialUpdate(existingActor, actorDTO);

                return existingActor;
            })
            .map(actorRepository::save)
            .map(actorMapper::toDto);
    }

    /**
     * Get all the actors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ActorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Actors");
        return actorRepository.findAll(pageable).map(actorMapper::toDto);
    }

    /**
     * Get all the actors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ActorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return actorRepository.findAllWithEagerRelationships(pageable).map(actorMapper::toDto);
    }

    /**
     * Get one actor by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ActorDTO> findOne(Long id) {
        log.debug("Request to get Actor : {}", id);
        return actorRepository.findOneWithEagerRelationships(id).map(actorMapper::toDto);
    }

    /**
     * Delete the actor by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Actor : {}", id);
        actorRepository.deleteById(id);
    }
}
