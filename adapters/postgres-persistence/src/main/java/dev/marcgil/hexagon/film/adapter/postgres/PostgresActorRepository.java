package dev.marcgil.hexagon.film.adapter.postgres;

import dev.marcgil.hexagon.film.adapter.postgres.model.ActorDbo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;

@NullMarked
@RequiredArgsConstructor
public class PostgresActorRepository implements ActorRepository {

  @NonNull
  private final EntityManagerFactory entityManagerFactory;

  @Override
  public Optional<ActorDbo> findByName(String name) {
    try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
      CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
      CriteriaQuery<ActorDbo> query = criteriaBuilder.createQuery(ActorDbo.class);
      Root<ActorDbo> actor = query.from(ActorDbo.class);
      Predicate namePredicate = criteriaBuilder.equal(actor.get("name"), name);

      query.where(namePredicate);
      return entityManager.createQuery(query).getResultStream().findFirst();
    }
  }

}
