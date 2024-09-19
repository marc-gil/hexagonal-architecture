package dev.marcgil.hexagon.film.application.port.api;

import dev.marcgil.hexagon.film.domain.Film;
import java.util.List;
import org.jspecify.annotations.NonNull;

public interface GetFilmsByDirectorUseCase {

  @NonNull
  List<Film> getFilmsByDirector(@NonNull String directorId);

}
