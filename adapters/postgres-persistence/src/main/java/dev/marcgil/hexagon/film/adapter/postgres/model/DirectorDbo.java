package dev.marcgil.hexagon.film.adapter.postgres.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Data;

@Data
@Entity(name = "DirectorDbo")
@Table(name = DirectorDbo.DIRECTORS_TABLE)
public class DirectorDbo {

  public static final String DIRECTORS_TABLE = "directors";

  @Id
  private String id;
  private String name;
  private String birthDate;
  @OneToMany(mappedBy = "director", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<FilmDbo> films;

}
