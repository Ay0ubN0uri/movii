package com.movii.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.movii.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserMoviesDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserMoviesDTO.class);
        UserMoviesDTO userMoviesDTO1 = new UserMoviesDTO();
        userMoviesDTO1.setId(1L);
        UserMoviesDTO userMoviesDTO2 = new UserMoviesDTO();
        assertThat(userMoviesDTO1).isNotEqualTo(userMoviesDTO2);
        userMoviesDTO2.setId(userMoviesDTO1.getId());
        assertThat(userMoviesDTO1).isEqualTo(userMoviesDTO2);
        userMoviesDTO2.setId(2L);
        assertThat(userMoviesDTO1).isNotEqualTo(userMoviesDTO2);
        userMoviesDTO1.setId(null);
        assertThat(userMoviesDTO1).isNotEqualTo(userMoviesDTO2);
    }
}
