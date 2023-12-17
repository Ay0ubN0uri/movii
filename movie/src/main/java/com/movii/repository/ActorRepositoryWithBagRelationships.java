package com.movii.repository;

import com.movii.domain.Actor;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface ActorRepositoryWithBagRelationships {
    Optional<Actor> fetchBagRelationships(Optional<Actor> actor);

    List<Actor> fetchBagRelationships(List<Actor> actors);

    Page<Actor> fetchBagRelationships(Page<Actor> actors);
}
