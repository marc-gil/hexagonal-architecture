package dev.marcgil.hexagon.film.adapter.postgres;

import dev.marcgil.hexagon.film.adapter.postgres.model.DirectorDbo;
import dev.marcgil.hexagon.film.adapter.postgres.model.FilmDbo;
import dev.marcgil.hexagon.film.domain.Film.Genre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@RequiredArgsConstructor
public class PostgresDirectorRepository implements DirectorRepository {

  @NonNull
  private final EntityManagerFactory entityManagerFactory;

  @Override
  @SneakyThrows
  public Optional<DirectorDbo> findById(String directorId) {
    try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
      return Optional.ofNullable(entityManager.find(DirectorDbo.class, directorId));
    }
  }

  @Override
  public DirectorDbo save(DirectorDbo director) {
    try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
      EntityTransaction transaction = entityManager.getTransaction();
      transaction.begin();
      entityManager.merge(director);
      transaction.commit();
      return director;
    }
  }

  @Override
  public List<DirectorDbo> findByName(String name) {
    try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
      CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
      CriteriaQuery<DirectorDbo> query = criteriaBuilder.createQuery(DirectorDbo.class);
      Root<DirectorDbo> director = query.from(DirectorDbo.class);
      Predicate namePredicate = criteriaBuilder.like(director.get("name"), "%" + name + "%");

      query.where(namePredicate);
      return entityManager.createQuery(query).getResultList();
    }
  }

  @Override
  public List<DirectorDbo> findBy(@Nullable String directorId, @Nullable Genre genre,
      @Nullable Year yearOfRecording) {
    try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
      CriteriaBuilder cb = entityManager.getCriteriaBuilder();
      CriteriaQuery<DirectorDbo> query = cb.createQuery(DirectorDbo.class);
      Root<DirectorDbo> director = query.from(DirectorDbo.class);

      Join<DirectorDbo, FilmDbo> films = director.join("films", JoinType.LEFT);

      Predicate predicate = cb.conjunction();

      if (directorId != null) {
        predicate = cb.and(predicate, cb.equal(director.get("id"), directorId));
      }

      if (genre != null) {
        predicate = cb.and(predicate, cb.isMember(genre.name(), films.get("genres")));
      }

      if (yearOfRecording != null) {
        predicate = cb.and(predicate, cb.equal(films.get("year"), yearOfRecording.getValue()));
      }

      query.where(predicate);
      query.distinct(true);  // To avoid duplicate directors in the result
      return entityManager.createQuery(query).getResultList();
    }
  }

}
