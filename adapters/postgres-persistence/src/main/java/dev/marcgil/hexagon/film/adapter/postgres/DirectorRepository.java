package dev.marcgil.hexagon.film.adapter.postgres;

import dev.marcgil.hexagon.film.adapter.postgres.model.DirectorDbo;
import dev.marcgil.hexagon.film.domain.Film.Genre;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public interface DirectorRepository {

  Optional<DirectorDbo> findById(String directorId);

  DirectorDbo save(DirectorDbo director);

  List<DirectorDbo> findByName(String name);

  List<DirectorDbo> findBy(@Nullable String directorId, @Nullable Genre genre,
      @Nullable Year yearOfRecording);

}
