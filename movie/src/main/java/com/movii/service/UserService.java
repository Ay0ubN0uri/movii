package com.movii.service;

import org.springframework.stereotype.Service;
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

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserFeignClient userFeignClient;
    private final UserMapper userMapper;

    public UserService(UserFeignClient userFeignClient, UserMapper userMapper) {
        this.userFeignClient = userFeignClient;
        this.userMapper = userMapper;
    }

    public AdminUserDTO userExistsById(String id) {
        log.debug("Request to determine if user exists : {}", id);
        try {
            return userFeignClient.userId(id);
        } catch (Exception e) {
            return null;
        }
    }

}
