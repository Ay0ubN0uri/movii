package com.movii.service;

import com.movii.client.UserFeignClient;
import com.movii.domain.Comment;
import com.movii.repository.CommentRepository;
import com.movii.service.dto.AdminUserDTO;
import com.movii.service.dto.CommentDTO;
import com.movii.service.mapper.CommentMapper;
import com.movii.service.mapper.UserMapper;

import reactor.core.publisher.Mono;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.movii.domain.Comment}.
 */
@Service
@Transactional
public class CommentService {

    private final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final UserFeignClient userFeignClient;
    private final UserMapper userMapper;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper,
            UserFeignClient userFeignClient, UserMapper userMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.userFeignClient = userFeignClient;
        this.userMapper = userMapper;
    }

    /**
     * Save a comment.
     *
     * @param commentDTO the entity to save.
     * @return the persisted entity.
     */
    public CommentDTO save(CommentDTO commentDTO) {
        log.debug("Request to save Comment : {}", commentDTO);
        Comment comment = commentMapper.toEntity(commentDTO);
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment).user(commentDTO.getUser());
    }

    /**
     * Update a comment.
     *
     * @param commentDTO the entity to save.
     * @return the persisted entity.
     */
    public CommentDTO update(CommentDTO commentDTO) {
        log.debug("Request to update Comment : {}", commentDTO);
        Comment comment = commentMapper.toEntity(commentDTO);
        comment = commentRepository.save(comment);
        return commentMapper.toDto(comment).user(commentDTO.getUser());
    }

    /**
     * Partially update a comment.
     *
     * @param commentDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CommentDTO> partialUpdate(CommentDTO commentDTO) {
        log.debug("Request to partially update Comment : {}", commentDTO);

        return commentRepository
                .findById(commentDTO.getId())
                .map(existingComment -> {
                    commentMapper.partialUpdate(existingComment, commentDTO);

                    return existingComment;
                })
                .map(commentRepository::save)
                .map(commentMapper::toDto);
    }

    /**
     * Get all the comments.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CommentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Comments");
        var comments = commentRepository.findAll(pageable);
        return comments.map(commentMapper::toDto).map(this::addUser);
    }

    /**
     * Get all the comments with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CommentDTO> findAllWithEagerRelationships(Pageable pageable) {
        var comments = commentRepository.findAllWithEagerRelationships(pageable);
        return comments.map(commentMapper::toDto)
                .map(this::addUser);
    }

    public CommentDTO addUser(CommentDTO comment) {
        try {
            AdminUserDTO user = userFeignClient.userId("" + comment.getUserId());
            comment.setUser(userMapper
                    .userToUserDTO(userMapper.userDTOToUser(user)));
        } catch (Exception e) {
            comment.setUser(null);
        }
        return comment;
    }

    /**
     * Get one comment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommentDTO> findOne(Long id) {
        log.debug("Request to get Comment : {}", id);
        return commentRepository.findOneWithEagerRelationships(id).map(commentMapper::toDto).map(this::addUser);
    }

    /**
     * Delete the comment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Comment : {}", id);
        commentRepository.deleteById(id);
    }
}
