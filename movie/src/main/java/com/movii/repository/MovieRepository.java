package com.movii.repository;

import com.movii.domain.Movie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Movie entity.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    default Optional<Movie> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Movie> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Movie> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select movie from Movie movie left join fetch movie.genre", countQuery = "select count(movie) from Movie movie")
    Page<Movie> findAllWithToOneRelationships(Pageable pageable);

    @Query("select movie from Movie movie left join fetch movie.genre")
    List<Movie> findAllWithToOneRelationships();

    @Query("select movie from Movie movie left join fetch movie.genre where movie.id =:id")
    Optional<Movie> findOneWithToOneRelationships(@Param("id") Long id);
}
