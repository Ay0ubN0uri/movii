package com.movii.repository;

import com.movii.domain.UserMovies;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserMovies entity.
 */
@Repository
public interface UserMoviesRepository extends JpaRepository<UserMovies, Long> {
    default Optional<UserMovies> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UserMovies> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UserMovies> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select userMovies from UserMovies userMovies left join fetch userMovies.movie",
        countQuery = "select count(userMovies) from UserMovies userMovies"
    )
    Page<UserMovies> findAllWithToOneRelationships(Pageable pageable);

    @Query("select userMovies from UserMovies userMovies left join fetch userMovies.movie")
    List<UserMovies> findAllWithToOneRelationships();

    @Query("select userMovies from UserMovies userMovies left join fetch userMovies.movie where userMovies.id =:id")
    Optional<UserMovies> findOneWithToOneRelationships(@Param("id") Long id);
}
