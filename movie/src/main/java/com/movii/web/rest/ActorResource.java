package com.movii.web.rest;

import com.movii.repository.ActorRepository;
import com.movii.service.ActorService;
import com.movii.service.dto.ActorDTO;
import com.movii.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.movii.domain.Actor}.
 */
@RestController
@RequestMapping("/api/actors")
public class ActorResource {

    private final Logger log = LoggerFactory.getLogger(ActorResource.class);

    private static final String ENTITY_NAME = "movieActor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActorService actorService;

    private final ActorRepository actorRepository;

    public ActorResource(ActorService actorService, ActorRepository actorRepository) {
        this.actorService = actorService;
        this.actorRepository = actorRepository;
    }

    /**
     * {@code POST  /actors} : Create a new actor.
     *
     * @param actorDTO the actorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new actorDTO, or with status {@code 400 (Bad Request)} if the actor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ActorDTO> createActor(@RequestBody ActorDTO actorDTO) throws URISyntaxException {
        log.debug("REST request to save Actor : {}", actorDTO);
        if (actorDTO.getId() != null) {
            throw new BadRequestAlertException("A new actor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActorDTO result = actorService.save(actorDTO);
        return ResponseEntity
            .created(new URI("/api/actors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /actors/:id} : Updates an existing actor.
     *
     * @param id the id of the actorDTO to save.
     * @param actorDTO the actorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actorDTO,
     * or with status {@code 400 (Bad Request)} if the actorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the actorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ActorDTO> updateActor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActorDTO actorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Actor : {}, {}", id, actorDTO);
        if (actorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, actorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!actorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ActorDTO result = actorService.update(actorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, actorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /actors/:id} : Partial updates given fields of an existing actor, field will ignore if it is null
     *
     * @param id the id of the actorDTO to save.
     * @param actorDTO the actorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actorDTO,
     * or with status {@code 400 (Bad Request)} if the actorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the actorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the actorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ActorDTO> partialUpdateActor(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ActorDTO actorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Actor partially : {}, {}", id, actorDTO);
        if (actorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, actorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!actorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ActorDTO> result = actorService.partialUpdate(actorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, actorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /actors} : get all the actors.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of actors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ActorDTO>> getAllActors(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get a page of Actors");
        Page<ActorDTO> page;
        if (eagerload) {
            page = actorService.findAllWithEagerRelationships(pageable);
        } else {
            page = actorService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /actors/:id} : get the "id" actor.
     *
     * @param id the id of the actorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the actorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActorDTO> getActor(@PathVariable("id") Long id) {
        log.debug("REST request to get Actor : {}", id);
        Optional<ActorDTO> actorDTO = actorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(actorDTO);
    }

    /**
     * {@code DELETE  /actors/:id} : delete the "id" actor.
     *
     * @param id the id of the actorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable("id") Long id) {
        log.debug("REST request to delete Actor : {}", id);
        actorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
