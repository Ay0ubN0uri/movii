package com.movii.repository;

import com.movii.domain.Actor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class ActorRepositoryWithBagRelationshipsImpl implements ActorRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Actor> fetchBagRelationships(Optional<Actor> actor) {
        return actor.map(this::fetchMovies);
    }

    @Override
    public Page<Actor> fetchBagRelationships(Page<Actor> actors) {
        return new PageImpl<>(fetchBagRelationships(actors.getContent()), actors.getPageable(), actors.getTotalElements());
    }

    @Override
    public List<Actor> fetchBagRelationships(List<Actor> actors) {
        return Optional.of(actors).map(this::fetchMovies).orElse(Collections.emptyList());
    }

    Actor fetchMovies(Actor result) {
        return entityManager
            .createQuery("select actor from Actor actor left join fetch actor.movies where actor.id = :id", Actor.class)
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Actor> fetchMovies(List<Actor> actors) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, actors.size()).forEach(index -> order.put(actors.get(index).getId(), index));
        List<Actor> result = entityManager
            .createQuery("select actor from Actor actor left join fetch actor.movies where actor in :actors", Actor.class)
            .setParameter("actors", actors)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
