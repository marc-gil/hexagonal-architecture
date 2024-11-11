package dev.marcgil.hexagon.film.application.port.api;

import dev.marcgil.hexagon.film.domain.Film;
import java.util.List;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface GetFilmsByDirectorUseCase {

  List<Film> getFilmsByDirector(String directorId);

}
