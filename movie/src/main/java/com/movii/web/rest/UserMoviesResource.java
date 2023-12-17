package com.movii.web.rest;

import com.movii.repository.UserMoviesRepository;
import com.movii.service.UserMoviesService;
import com.movii.service.dto.UserMoviesDTO;
import com.movii.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.movii.domain.UserMovies}.
 */
@RestController
@RequestMapping("/api/user-movies")
public class UserMoviesResource {

    private final Logger log = LoggerFactory.getLogger(UserMoviesResource.class);

    private static final String ENTITY_NAME = "movieUserMovies";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserMoviesService userMoviesService;

    private final UserMoviesRepository userMoviesRepository;

    public UserMoviesResource(UserMoviesService userMoviesService, UserMoviesRepository userMoviesRepository) {
        this.userMoviesService = userMoviesService;
        this.userMoviesRepository = userMoviesRepository;
    }

    /**
     * {@code POST  /user-movies} : Create a new userMovies.
     *
     * @param userMoviesDTO the userMoviesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userMoviesDTO, or with status {@code 400 (Bad Request)} if the userMovies has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserMoviesDTO> createUserMovies(@RequestBody UserMoviesDTO userMoviesDTO) throws URISyntaxException {
        log.debug("REST request to save UserMovies : {}", userMoviesDTO);
        if (userMoviesDTO.getId() != null) {
            throw new BadRequestAlertException("A new userMovies cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UserMoviesDTO result = userMoviesService.save(userMoviesDTO);
        return ResponseEntity
            .created(new URI("/api/user-movies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /user-movies/:id} : Updates an existing userMovies.
     *
     * @param id the id of the userMoviesDTO to save.
     * @param userMoviesDTO the userMoviesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userMoviesDTO,
     * or with status {@code 400 (Bad Request)} if the userMoviesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userMoviesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserMoviesDTO> updateUserMovies(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserMoviesDTO userMoviesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserMovies : {}, {}", id, userMoviesDTO);
        if (userMoviesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userMoviesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userMoviesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UserMoviesDTO result = userMoviesService.update(userMoviesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userMoviesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /user-movies/:id} : Partial updates given fields of an existing userMovies, field will ignore if it is null
     *
     * @param id the id of the userMoviesDTO to save.
     * @param userMoviesDTO the userMoviesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userMoviesDTO,
     * or with status {@code 400 (Bad Request)} if the userMoviesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userMoviesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userMoviesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserMoviesDTO> partialUpdateUserMovies(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserMoviesDTO userMoviesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserMovies partially : {}, {}", id, userMoviesDTO);
        if (userMoviesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userMoviesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userMoviesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserMoviesDTO> result = userMoviesService.partialUpdate(userMoviesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userMoviesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-movies} : get all the userMovies.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userMovies in body.
     */
    @GetMapping("")
    public List<UserMoviesDTO> getAllUserMovies(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all UserMovies");
        return userMoviesService.findAll();
    }

    /**
     * {@code GET  /user-movies/:id} : get the "id" userMovies.
     *
     * @param id the id of the userMoviesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userMoviesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserMoviesDTO> getUserMovies(@PathVariable("id") Long id) {
        log.debug("REST request to get UserMovies : {}", id);
        Optional<UserMoviesDTO> userMoviesDTO = userMoviesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userMoviesDTO);
    }

    /**
     * {@code DELETE  /user-movies/:id} : delete the "id" userMovies.
     *
     * @param id the id of the userMoviesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserMovies(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserMovies : {}", id);
        userMoviesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
