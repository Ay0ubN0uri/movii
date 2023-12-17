package com.movii.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class UserMoviesMapperTest {

    private UserMoviesMapper userMoviesMapper;

    @BeforeEach
    public void setUp() {
        userMoviesMapper = new UserMoviesMapperImpl();
    }
}
