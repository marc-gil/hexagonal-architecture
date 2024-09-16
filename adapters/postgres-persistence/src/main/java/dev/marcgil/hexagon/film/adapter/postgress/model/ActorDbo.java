package dev.marcgil.hexagon.film.adapter.postgress.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "actors")
public class ActorDbo {

  @Id
  private String id;
  @Column(name = "name", nullable = false)
  private String name;
  @Column(name = "birthDate")
  private String birthDate;
  @ManyToMany(mappedBy = "cast")
  private List<FilmDbo> films;

}
