package dev.marcgil.hexagon.film.adapter.postgres;

import dev.marcgil.hexagon.film.adapter.postgres.model.DirectorDbo;
import dev.marcgil.hexagon.film.domain.Film.Genre;
import java.time.Year;
import java.util.List;
import java.util.Optional;

public interface DirectorRepository {

  Optional<DirectorDbo> findById(String directorId);

  DirectorDbo save(DirectorDbo director);

  List<DirectorDbo> findByName(String name);

  List<DirectorDbo> findBy(String directorId, Genre genre, Year yearOfRecording);

}
