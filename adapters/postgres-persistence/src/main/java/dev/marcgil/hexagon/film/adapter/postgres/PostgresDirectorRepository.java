package dev.marcgil.hexagon.film.adapter.postgres;

import dev.marcgil.hexagon.film.adapter.postgres.model.DirectorDbo;
import dev.marcgil.hexagon.film.domain.Film.Genre;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  @SuppressWarnings("unchecked")
  public List<DirectorDbo> findBy(@Nullable String directorId, @Nullable Genre genre,
      @Nullable Year yearOfRecording) {
    try (EntityManager entityManager = entityManagerFactory.createEntityManager()) {
      StringBuilder sql = new StringBuilder(
          "SELECT DISTINCT d.* FROM directors d JOIN films f ON d.id = f.director_id WHERE 1=1");
      Map<String, Object> params = new HashMap<>();

      if (directorId != null) {
        sql.append(" AND d.id = :directorId");
        params.put("directorId", directorId);
      }

      if (genre != null) {
        sql.append(" AND ARRAY_POSITION(f.genres, :genre) IS NOT NULL");
        params.put("genre", genre.name());
      }

      if (yearOfRecording != null) {
        sql.append(" AND f.year = :year");
        params.put("year", yearOfRecording.getValue());
      }

      Query query = entityManager.createNativeQuery(sql.toString(), DirectorDbo.class);
      params.forEach(query::setParameter);
      return query.getResultList();
    }
  }

}
