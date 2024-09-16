package dev.marcgil.hexagon.film.adapter.postgress.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "films")
public class FilmDbo {

  @Id
  private String id;
  @Column(name = "year", nullable = false)
  private int year;
  @Column(name = "title", nullable = false)
  private String title;
  @Column(name = "duration", nullable = false)
  private String duration;
  @Column(name = "genres", columnDefinition = "text[]", nullable = false)
  private List<String> genres;
  @JoinTable(
      name = "film_actor",
      joinColumns = @JoinColumn(name = "film_id"),
      inverseJoinColumns = @JoinColumn(name = "actor_id")
  )
  @ManyToMany(fetch = FetchType.EAGER)
  private List<ActorDbo> cast;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "director_id", nullable = false)
  private DirectorDbo director;

}
