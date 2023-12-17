package com.movii.domain;

import static com.movii.domain.CommentTestSamples.*;
import static com.movii.domain.MovieTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.movii.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CommentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Comment.class);
        Comment comment1 = getCommentSample1();
        Comment comment2 = new Comment();
        assertThat(comment1).isNotEqualTo(comment2);

        comment2.setId(comment1.getId());
        assertThat(comment1).isEqualTo(comment2);

        comment2 = getCommentSample2();
        assertThat(comment1).isNotEqualTo(comment2);
    }

    @Test
    void movieTest() throws Exception {
        Comment comment = getCommentRandomSampleGenerator();
        Movie movieBack = getMovieRandomSampleGenerator();

        comment.setMovie(movieBack);
        assertThat(comment.getMovie()).isEqualTo(movieBack);

        comment.movie(null);
        assertThat(comment.getMovie()).isNull();
    }
}
